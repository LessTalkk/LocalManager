
package local.less.org.contactmodel.internal;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import local.less.org.base.base.BaseManager;
import local.less.org.base.log.LogUtil;
import local.less.org.base.save.SaveUtil;
import local.less.org.base.task.BackgroundWork;
import local.less.org.base.task.Completion;
import local.less.org.base.task.LessTask;
import local.less.org.base.util.Util;
import local.less.org.contactmodel.external.ContactNode;
import local.less.org.contactmodel.internal.bean.AddressInfo;
import local.less.org.contactmodel.internal.bean.CompanyInfo;
import local.less.org.contactmodel.internal.bean.ContactInfo;
import local.less.org.contactmodel.internal.bean.EmailInfo;
import local.less.org.contactmodel.internal.bean.EventInfo;
import local.less.org.contactmodel.internal.bean.GroupInfo;
import local.less.org.contactmodel.internal.bean.ImInfo;
import local.less.org.contactmodel.internal.bean.NicknameInfo;
import local.less.org.contactmodel.internal.bean.NoteInfo;
import local.less.org.contactmodel.internal.bean.PhoneInfo;
import local.less.org.contactmodel.internal.bean.PhotoInfo;
import local.less.org.contactmodel.internal.bean.RelateInfo;
import local.less.org.contactmodel.internal.bean.RelationInfo;
import local.less.org.contactmodel.internal.bean.SipInfo;
import local.less.org.contactmodel.internal.bean.WebsiteInfo;

import static local.less.org.base.util.Util.isSamsung;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/10/30 <br/>
 * DESCRIPTION :
 */
public class ContactManager extends BaseManager<ContactNode> {

    private static final String TAG = "ContactManager";

    public static final String CONTACT_ACCOUNT_ERROR = "-error";
    public static final String CONTACT_ACCOUNT_GROUP_NAME = "LESS_GROUP";
    public static final String CONTACT_ACCOUNT_NULL = "-null";
    public static final String CONTACT_KEY_ACCOUNT_TYPE = "key_account_type";

    private final static String[] INFO_RAW_FILTER = new String[] {
            ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts._ID, ContactsContract.RawContacts.ACCOUNT_TYPE,
            ContactsContract.RawContacts.SYNC1, ContactsContract.RawContacts.SYNC2, ContactsContract.RawContacts.SYNC3,
            ContactsContract.RawContacts.SYNC4
    };

    private static final String[] INFO_PROJECTION = new String[] {
            ContactsContract.Data._ID, ContactsContract.Data.CONTACT_ID, ContactsContract.Data.MIMETYPE, ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.Data.IS_PRIMARY,
            ContactsContract.Data.DATA1, ContactsContract.Data.DATA2, ContactsContract.Data.DATA3, ContactsContract.Data.DATA4, ContactsContract.Data.DATA5,
            ContactsContract.Data.DATA6, ContactsContract.Data.DATA7, ContactsContract.Data.DATA8, ContactsContract.Data.DATA9, ContactsContract.Data.DATA10,
            ContactsContract.Data.DATA11, ContactsContract.Data.DATA12, ContactsContract.Data.DATA13, ContactsContract.Data.DATA14, ContactsContract.Data.DATA15
    };

    public static class LocalContactOperate {
        public final static int NONE = 0;
        public final static int ADD = 1;
        public final static int UPDATE = 2;
        public final static int DELETE = 3;
    }

    private static final int INFO_CONTACT_ID_COLUMN_INDEX = 1;
    private static final int INFO_MIMETYPE_COLUMN_INDEX = 2;
    private static final int INFO_CONTACT_RAWID_COLUMN_INDEX = 3;

    private static int[] sPhoneColumnIndex = null;
    private static int[] sNameColumnIndex = null;

    private HashSet<String> mFilerAccount = new HashSet<String>();

    private static ContactManager sContactManager;

    private ContentResolver mContentResolver;

    private SparseArray<String> mGroups;

    private ContactManager() {
        initFilerAccount();
    }

    public static ContactManager getInstance() {
        if (sContactManager == null) {
            sContactManager = new ContactManager();
        }
        return sContactManager;
    }

    @Override
    public BaseManager init(Context context) {
        mContentResolver = context.getContentResolver();
        return sContactManager;
    }


    @Override
    public BaseManager getCount(Context context, Completion<Integer> completion) {
        LessTask.executeLightInBackground(context, new BackgroundWork<Integer>() {
            @Override
            public Integer doInBackground() throws Exception {
                Cursor cursor = null;
                int count = 0;
                try {
                    cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] {
                            ContactsContract.Contacts._ID
                    }, null, null, null);
                    if (cursor != null) {
                        count = cursor.getCount();
                    }
                } finally {
                    Util.close(cursor);
                }

                return count;
            }
        }, completion);
        return sContactManager;
    }

    @Override
    public BaseManager getList(final Context context,Completion<ArrayList<ContactNode>> completion){
        LessTask.executeLightInBackground(context, new BackgroundWork<ArrayList<ContactNode>>() {
            @Override
            public ArrayList<ContactNode> doInBackground() throws Exception {
                HashMap<Long, ContactInfo> allLocalContacts = initLocalContacts(context,true);
                final ArrayList<ContactNode> contactNodes = new ArrayList<ContactNode>();
                for (Long cid : allLocalContacts.keySet()) {
                    ContactInfo info = allLocalContacts.get(cid);
                    ContactNode node = new ContactNode();
                    node.contact_id = cid;
                    node.setContactInfo(info);
                    contactNodes.add(node);
                }
                return contactNodes;
            }
        },completion);
        return this;
    }

    private HashMap<Long, ContactInfo> initLocalContacts(Context context,boolean isNeedRealPhoto){
        mGroups = getSysGroups(mContentResolver);
        HashMap<Long, Long> voidAccountMap = loadVoidAccount(mContentResolver, getDefaultAccountType(context));
        if (voidAccountMap == null) {
            return new HashMap<Long, ContactInfo>();
        }
        Cursor contactCursor = null;
        HashMap<Long, ContactInfo> result = null;
        try{
            contactCursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] {
                    ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME
            }, null, null, null);
            if(contactCursor != null){
                int count = contactCursor.getCount();
                result = new HashMap<Long, ContactInfo>(count);
                while (Util.moveCursorNext(contactCursor)){
                    long cid = contactCursor.getLong(0);
                    String fullName = contactCursor.getString(1);
                    if (voidAccountMap.containsKey(cid)) {
                        ContactInfo info = new ContactInfo();
                        info.setId(cid);
                        info.setFullName(fullName);
                        info.setDefaultRawId(voidAccountMap.get(cid));
                        result.put(cid, info);
                    }
                }
            }
        }finally {
            Util.close(contactCursor);
        }
        Cursor cursor = null;
        try{
            cursor = mContentResolver.query(ContactsContract.Data.CONTENT_URI, INFO_PROJECTION, null, null, null);
            if (cursor != null) {
                while (Util.moveCursorNext(cursor)) {
                    long contactId = cursor.getLong(INFO_CONTACT_ID_COLUMN_INDEX);
                    long rawId = cursor.getLong(INFO_CONTACT_RAWID_COLUMN_INDEX);
                    if (!result.containsKey(contactId)) {
                        continue;
                    }
                    ContactInfo contactInfo = result.get(contactId);
                    contactInfo.addRawId(rawId);
                    String mineType = cursor.getString(INFO_MIMETYPE_COLUMN_INDEX);
                    if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getPhone(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getName(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getEmail(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getAddress(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getCompany(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getWebsite(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getIm(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getEvent(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getNote(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE.equals(mineType)) {
                        if (isNeedRealPhoto) {
                            getPhoto(cursor, contactInfo);
                        } else {
                            getPhotoSimple(cursor, contactInfo);
                        }
                    } else if (ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getNickname(cursor, contactInfo);
                    } else if ("vnd.android.cursor.item/sip_address".equals(mineType)) {
                        getSip(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getGroup(cursor, contactInfo);
                    } else if (ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE.equals(mineType)) {
                        getRelation(cursor, contactInfo);
                    }
                }
            }
        }finally {
            Util.close(cursor);
        }
        return result;
    }

    private HashMap<Long, Long> loadVoidAccount(ContentResolver contentResolver, String accountType) {
        HashMap<Long, Long> result = null;
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, INFO_RAW_FILTER, "deleted=0", null, null);
            if (cursor != null) {
                int count = cursor.getCount();
                result = new HashMap<Long, Long>(count);
                while (Util.moveCursorNext(cursor)) {
                    long cid = cursor.getLong(0);
                    long rawId = cursor.getLong(1);
                    String account_type = cursor.getString(2);
                    if (checkAccount(accountType, account_type)) {
                        result.put(cid, rawId);
                        continue;
                    }
                    if (isHaveAccount(accountType) && !isSamsung()) {
                        continue;
                    }
                    if (result.containsKey(cid)) {
                        continue;
                    }
                    if (mFilerAccount.contains(account_type)) {
                        continue;
                    }
                    result.put(cid, rawId);
                }
            }
        } finally {
            Util.close(cursor);
        }
        return result;
    }

    private SparseArray getSysGroups(ContentResolver resolver) {
        SparseArray mGroups = new SparseArray<String>();
        Cursor cursor = null;
        try {
            cursor = resolver.query(ContactsContract.Groups.CONTENT_URI, new String[] {
                    ContactsContract.Groups._ID, ContactsContract.Groups.TITLE
            }, ContactsContract.Groups.DELETED + "=?", new String[] {
                    0 + ""
            }, null);
            if (cursor != null) {
                while (Util.moveCursorNext(cursor)) {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    if (!TextUtils.isEmpty(title)) {
                        mGroups.put(id, title);
                    }
                }
            }
        } finally {
            Util.close(cursor);
        }
        return mGroups;
    }

    private boolean checkAccount(String account_recode, String account_item) {
        if (account_item == null) {
            return true;
        } else if (account_recode == null || account_recode.equals(CONTACT_ACCOUNT_ERROR)) {
            return false;
        } else {
            return account_recode.equals(account_item);
        }
    }

    private boolean isHaveAccount(String account) {
        if (account == null) {
            return false;
        }
        return !account.equals(CONTACT_ACCOUNT_ERROR) && !account.equals(CONTACT_ACCOUNT_ERROR);
    }

    private String getDefaultAccountType(Context context) {
        String accountType = SaveUtil.getString(context, CONTACT_KEY_ACCOUNT_TYPE);
        if (TextUtils.isEmpty(accountType)) {
            accountType = getAccountType();
            SaveUtil.putString(context, CONTACT_KEY_ACCOUNT_TYPE, accountType);
        }
        return accountType;
    }

    private void initFilerAccount() {
        mFilerAccount.add("com.tencent.mobileqq.account");
        mFilerAccount.add("com.tencent.mm.account");
        mFilerAccount.add("com.whatsapp");
        mFilerAccount.add("com.immomo.momo");
    }

    private String getAccountType() {
        String accountType = CONTACT_ACCOUNT_ERROR;
        Uri uri = mContentResolver.insert(ContactsContract.Groups.CONTENT_URI, getEmptyGroup());
        long gid = ContentUris.parseId(uri);
        LogUtil.d(TAG, "getAccountType-uri:" + uri + "|gid:" + gid);
        Cursor cursor = null;
        if (gid > 0) {
            try {
                cursor = mContentResolver.query(ContactsContract.Groups.CONTENT_URI, new String[] {
                        ContactsContract.Groups.ACCOUNT_NAME,
                        ContactsContract.Groups.ACCOUNT_TYPE
                }, ContactsContract.Groups._ID + "=" + gid, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    accountType = cursor.getString(1);
                    if (accountType == null) {
                        accountType = CONTACT_ACCOUNT_NULL;
                    }
                }

            } finally {
                Util.close(cursor);
            }
        }
        LogUtil.d(TAG, "getAccountType-accountType:" + accountType);
        return accountType;
    }

    private ContentValues getEmptyGroup() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Groups.ACCOUNT_NAME, EMPTY);
        contentValues.put(ContactsContract.Groups.ACCOUNT_TYPE, EMPTY);
        contentValues.put(ContactsContract.Groups.TITLE, CONTACT_ACCOUNT_GROUP_NAME);
        contentValues.put(ContactsContract.Groups.GROUP_VISIBLE, 0);
        contentValues.put(ContactsContract.Groups.DELETED, 1);
        return contentValues;
    }

    private void getPhone(Cursor cursor, ContactInfo contactInfo) {
        PhoneInfo info = new PhoneInfo();
        if (sPhoneColumnIndex == null) {
            sPhoneColumnIndex = new int[6];
            sPhoneColumnIndex[1] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            sPhoneColumnIndex[2] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            sPhoneColumnIndex[3] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_PRIMARY);
            sPhoneColumnIndex[4] = cursor.getColumnIndex(ContactsContract.Data._ID);
            sPhoneColumnIndex[5] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);
        }
        info.setId(cursor.getLong(sPhoneColumnIndex[4]));
        info.setNumber(cursor.getString(sPhoneColumnIndex[1]));
        info.setType(cursor.getInt(sPhoneColumnIndex[2]));
        info.setIsPrimary(cursor.getInt(sPhoneColumnIndex[3]));
        info.setLabel(cursor.getString(sPhoneColumnIndex[5]));

        LinkedHashSet<PhoneInfo> list = contactInfo.getPhoneNumbers();
        if (list == null) {
            list = new LinkedHashSet<PhoneInfo>();
            list.add(info);
            contactInfo.setPhoneNumbers(list);
        } else {
            list.add(info);
        }
    }

    private void getName(Cursor cursor, ContactInfo contactInfo) {
        if (sNameColumnIndex == null) {
            sNameColumnIndex = new int[7];
            sNameColumnIndex[0] = cursor.getColumnIndex(ContactsContract.Data._ID);
            sNameColumnIndex[1] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
            sNameColumnIndex[2] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
            sNameColumnIndex[3] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME);
            sNameColumnIndex[4] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX);
            sNameColumnIndex[5] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX);
            sNameColumnIndex[6] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);
        }
        String displayName = cursor.getString(sNameColumnIndex[6]);
        if (displayName != null && contactInfo.haveName() && !displayName.equals(contactInfo.getDisplayName())) {
            return;
        }
        contactInfo.setNameId(cursor.getLong(sNameColumnIndex[0]));
        contactInfo.setLastName(cursor.getString(sNameColumnIndex[1]));
        contactInfo.setFirstName(cursor.getString(sNameColumnIndex[2]));
        contactInfo.setMiddleName(cursor.getString(sNameColumnIndex[3]));
        contactInfo.setPrefix(cursor.getString(sNameColumnIndex[4]));
        contactInfo.setSuffix(cursor.getString(sNameColumnIndex[5]));
    }

    private void addRelateName(Cursor cursor, RelateInfo relateInfo) {
        if (sNameColumnIndex == null) {
            sNameColumnIndex = new int[7];
            sNameColumnIndex[0] = cursor.getColumnIndex(ContactsContract.Data._ID);
            sNameColumnIndex[1] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
            sNameColumnIndex[2] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
            sNameColumnIndex[3] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME);
            sNameColumnIndex[4] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX);
            sNameColumnIndex[5] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX);
            sNameColumnIndex[6] = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);
        }
        String displayName = cursor.getString(sNameColumnIndex[6]);
        if (displayName != null && relateInfo.haveRelateName() && !displayName.equals(relateInfo.getFullName())) {
            return;
        }
        relateInfo.setLastName(cursor.getString(sNameColumnIndex[1]));
        relateInfo.setFirstName(cursor.getString(sNameColumnIndex[2]));
        relateInfo.setMiddleName(cursor.getString(sNameColumnIndex[3]));
        relateInfo.setPrefix(cursor.getString(sNameColumnIndex[4]));
        relateInfo.setSuffix(cursor.getString(sNameColumnIndex[5]));
    }

    private void getRelation(Cursor cursor, ContactInfo contactInfo) {
        RelationInfo info = new RelationInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.DATA);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.TYPE);
        int idx3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.LABEL);
        int idx4 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation._ID);
        info.setId(cursor.getLong(idx4));
        info.setName(cursor.getString(idx1));
        info.setType(cursor.getInt(idx2));
        info.setLabel(cursor.getString(idx3));
        LinkedHashSet<RelationInfo> list = contactInfo.getRelations();
        if (list == null) {
            list = new LinkedHashSet<RelationInfo>();
            list.add(info);
            contactInfo.setRelations(list);
        } else {
            list.add(info);
        }
    }

    private void getEmail(Cursor cursor, ContactInfo contactInfo) {
        EmailInfo info = new EmailInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);
        int idx3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.IS_PRIMARY);
        int idx4 = cursor.getColumnIndex(ContactsContract.Data._ID);
        int idx5 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL);
        info.setId(cursor.getLong(idx4));
        info.setAddress(cursor.getString(idx1));
        info.setType(cursor.getInt(idx2));
        info.setIsPrimary(cursor.getInt(idx3));
        info.setLabel(cursor.getString(idx5));
        LinkedHashSet<EmailInfo> list = contactInfo.getEmails();
        if (list == null) {
            list = new LinkedHashSet<EmailInfo>();
            list.add(info);
            contactInfo.setEmails(list);
        } else {
            list.add(info);
        }
    }

    private void getGroup(Cursor cursor, ContactInfo contactInfo) {
        GroupInfo info = new GroupInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.DATA1);
        int id = cursor.getColumnIndex(ContactsContract.Data._ID);
        info.setId(cursor.getLong(id));
        int value = cursor.getInt(idx1);
        String name = mGroups.get(value);
        if (TextUtils.isEmpty(name)) {
            return;
        }
        info.setName(name);
        LinkedHashSet<GroupInfo> list = contactInfo.getGroups();
        if (list == null) {
            list = new LinkedHashSet<GroupInfo>();
            contactInfo.setGroups(list);
        }
        list.add(info);
    }

    private void getSip(Cursor cursor, ContactInfo contactInfo) {
        SipInfo info = new SipInfo();
        int idx1 = cursor.getColumnIndex("data1");
        int idx2 = cursor.getColumnIndex("data2");
        int idx3 = cursor.getColumnIndex(ContactsContract.Data.IS_PRIMARY);
        int idx4 = cursor.getColumnIndex(ContactsContract.Data._ID);
        int idx5 = cursor.getColumnIndex("data3");
        info.setId(cursor.getLong(idx4));
        info.setAddress(cursor.getString(idx1));
        info.setType(cursor.getInt(idx2));
        info.setIsPrimary(cursor.getInt(idx3));
        info.setLabel(cursor.getString(idx5));
        LinkedHashSet<SipInfo> list = contactInfo.getSips();
        if (list == null) {
            list = new LinkedHashSet<SipInfo>();
            list.add(info);
            contactInfo.setSips(list);
        } else {
            list.add(info);
        }
    }

    private void getAddress(Cursor cursor, ContactInfo contactInfo) {
        AddressInfo info = new AddressInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION);
        int idx3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY);
        int idx4 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET);
        int idx5 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE);
        int idx6 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE);
        int idx7 = cursor.getColumnIndex(ContactsContract.Data._ID);
        int idx8 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX);
        int idx9 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD);
        info.setId(cursor.getLong(idx7));
        info.setCountry(cursor.getString(idx1));
        info.setRegion(cursor.getString(idx2));
        info.setCity(cursor.getString(idx3));
        info.setStreet(cursor.getString(idx4));
        info.setPostCode(cursor.getString(idx5));
        info.setType(cursor.getInt(idx6));
        info.setPobox(cursor.getString(idx8));
        info.setExtendedAddress(cursor.getString(idx9));

        LinkedHashSet<AddressInfo> list = contactInfo.getAddresses();
        if (list == null) {
            list = new LinkedHashSet<AddressInfo>();
            list.add(info);
            contactInfo.setAddresses(list);
        } else {
            list.add(info);
        }
    }

    private void getCompany(Cursor cursor, ContactInfo contactInfo) {
        CompanyInfo info = new CompanyInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE);
        int idx3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE);
        int idx4 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.IS_PRIMARY);
        int idx5 = cursor.getColumnIndex(ContactsContract.Data._ID);
        int idx6 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.LABEL);
        info.setId(cursor.getLong(idx5));
        info.setName(cursor.getString(idx1));
        info.setTitle(cursor.getString(idx2));
        info.setType(cursor.getInt(idx3));
        info.setIsPrimary(cursor.getInt(idx4));
        info.setLabel(cursor.getString(idx6));

        LinkedHashSet<CompanyInfo> list = contactInfo.getCompanies();
        if (list == null) {
            list = new LinkedHashSet<CompanyInfo>();
            list.add(info);
            contactInfo.setCompanies(list);
        } else {
            list.add(info);
        }
    }

    private void getWebsite(Cursor cursor, ContactInfo contactInfo) {
        WebsiteInfo info = new WebsiteInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE);
        int idx3 = cursor.getColumnIndex(ContactsContract.Data._ID);
        info.setId(cursor.getLong(idx3));
        info.setUrl(cursor.getString(idx1));
        info.setType(cursor.getInt(idx2));

        LinkedHashSet<WebsiteInfo> list = contactInfo.getWebsites();
        if (list == null) {
            list = new LinkedHashSet<WebsiteInfo>();
            list.add(info);
            contactInfo.setWebsites(list);
        } else {
            list.add(info);
        }
    }

    private void getIm(Cursor cursor, ContactInfo contactInfo) {
        ImInfo info = new ImInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE);
        int idx3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.LABEL);
        int idx4 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL);
        int idx5 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL);
        int idx6 = cursor.getColumnIndex(ContactsContract.Data._ID);
        info.setId(cursor.getLong(idx6));
        info.setData(cursor.getString(idx1));
        info.setType(cursor.getInt(idx2));
        info.setLabel(cursor.getString(idx3));
        info.setProtocol(cursor.getString(idx4));
        info.setCustomProtocol(cursor.getString(idx5));

        LinkedHashSet<ImInfo> list = contactInfo.getIms();
        if (list == null) {
            list = new LinkedHashSet<ImInfo>();
            list.add(info);
            contactInfo.setIms(list);
        } else {
            list.add(info);
        }
    }

    private void getEvent(Cursor cursor, ContactInfo contactInfo) {
        EventInfo info = new EventInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL);
        int idx3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE);
        int idx4 = cursor.getColumnIndex(ContactsContract.Data._ID);
        info.setId(cursor.getLong(idx4));
        info.setStartDate(cursor.getString(idx1));
        info.setLabel(cursor.getString(idx2));
        info.setType(cursor.getInt(idx3));

        LinkedHashSet<EventInfo> list = contactInfo.getEvents();
        if (list == null) {
            list = new LinkedHashSet<EventInfo>();
            list.add(info);
            contactInfo.setEvents(list);
        } else {
            list.add(info);
        }
    }

    private void getNickname(Cursor cursor, ContactInfo contactInfo) {
        NicknameInfo info = new NicknameInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME);
        int idx2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL);
        int idx3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE);
        int idx4 = cursor.getColumnIndex(ContactsContract.Data._ID);
        info.setId(cursor.getLong(idx4));
        info.setName(cursor.getString(idx1));
        info.setLabel(cursor.getString(idx2));
        info.setType(cursor.getInt(idx3));

        LinkedHashSet<NicknameInfo> list = contactInfo.getNicknames();
        if (list == null) {
            list = new LinkedHashSet<NicknameInfo>();
            list.add(info);
            contactInfo.setNicknames(list);
        } else {
            list.add(info);
        }
    }

    private void getNote(Cursor cursor, ContactInfo contactInfo) {
        NoteInfo info = new NoteInfo();
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE);
        int idx2 = cursor.getColumnIndex(ContactsContract.Data._ID);
        info.setId(cursor.getLong(idx2));
        info.setNote(cursor.getString(idx1));

        LinkedHashSet<NoteInfo> list = contactInfo.getNotes();
        if (list == null) {
            list = new LinkedHashSet<NoteInfo>();
            list.add(info);
            contactInfo.setNotes(list);
        } else {
            list.add(info);
        }
    }

    private void getPhoto(Cursor cursor, ContactInfo contactInfo) {
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
        int idx2 = cursor.getColumnIndex(ContactsContract.Data._ID);
        if (!cursor.isNull(idx1)) {
            byte[] bs = cursor.getBlob(idx1);
            if (bs != null) {
                PhotoInfo info = new PhotoInfo();
                info.setId(cursor.getLong(idx2));
                info.setBinaryData(bs);
                LinkedHashSet<PhotoInfo> list = contactInfo.getPhotoes();
                if (list == null) {
                    list = new LinkedHashSet<PhotoInfo>();
                    list.add(info);
                    contactInfo.setPhotoes(list);
                } else {
                    list.add(info);
                }
            }
        }
    }

    private void getPhotoSimple(Cursor cursor, ContactInfo contactInfo) {
        int idx1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
        int idx2 = cursor.getColumnIndex(ContactsContract.Data._ID);
        if (!cursor.isNull(idx1)) {
            PhotoInfo info = new PhotoInfo();
            info.setId(cursor.getLong(idx2));
            LinkedHashSet<PhotoInfo> list = contactInfo.getPhotoes();
            if (list == null) {
                list = new LinkedHashSet<PhotoInfo>();
                list.add(info);
                contactInfo.setPhotoes(list);
            } else {
                list.add(info);
            }
        }
    }
}
