
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

import java.util.HashMap;
import java.util.HashSet;

import local.less.org.base.task.BackgroundWork;
import local.less.org.base.task.Completion;
import local.less.org.base.task.LessTask;
import local.less.org.base.base.BaseManager;
import local.less.org.base.log.LogUtil;
import local.less.org.base.save.SaveUtil;
import local.less.org.base.util.Util;

import static local.less.org.base.util.Util.isSamsung;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/10/30 <br/>
 * DESCRIPTION :
 */
public class ContactManager extends BaseManager {

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

    private HashSet<String> mFilerAccount = new HashSet<String>();

    private static ContactManager sContactManager;

    private ContentResolver mContentResolver;

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

    public void getList(Context context){
        HashMap<Long, Long> needBackup = loadVoidAccount(mContentResolver, getDefaultAccountType(context));
        if (needBackup == null) {
            //return new HashMap<Long, ContactInfo>();
        }

    }

    private HashMap<Long, Long> loadVoidAccount(ContentResolver contentResolver, String accountType) {
        HashMap<Long, Long> result = null;
        Cursor cursor = null;
        try{
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
        }finally {
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

    private void initFilerAccount(){
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


}
