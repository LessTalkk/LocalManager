
package local.less.org.contactmodel.internal.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConfig;
import local.less.org.contactmodel.internal.vcard.VCardConstants;
import local.less.org.contactmodel.internal.vcard.VCardUtils;

public class ContactInfo extends BaseBean {
    private int mVCardType;
    private String mDisplayName;
    private String mFullName;
    private String mPhoneticFullName;
    private String mRelateName;

    private long defaultRawId; // 默认的raw_contacts表ID
    private long nameId = 0; //本地用来更新媒体库中的名字
    private String firstName = ""; // 名
    private String lastName = ""; // 姓
    private String middleName = "";
    //    private String phoneticFirstName = "";
    //    private String phoneticLastName = "";
    //    private String phoneticMiddleName = "";
    private String prefix = "";
    private String suffix = "";
    private String account_name = "";
    private String account_type = "";

    private LinkedHashSet<GroupInfo> groups;
    private LinkedHashSet<NicknameInfo> nicknames;
    private LinkedHashSet<PhoneInfo> phoneNumbers;
    private LinkedHashSet<EmailInfo> emails;
    private LinkedHashSet<SipInfo> sips;
    private LinkedHashSet<CompanyInfo> companies;
    private LinkedHashSet<AddressInfo> addresses;
    private LinkedHashSet<WebsiteInfo> websites;
    private LinkedHashSet<ImInfo> ims;
    private LinkedHashSet<EventInfo> events;
    private LinkedHashSet<NoteInfo> notes;
    private LinkedHashSet<PhotoInfo> photoes;
    private LinkedHashSet<Long> mRawIds;
    private LinkedHashSet<RelationInfo> relations;

    private String starred = "0"; // 是否已经添加到收藏夹，默认不添加

    //    private boolean isHavePhoto = false;

    /* 是否导出联系人图片。目前联网备份不备份图片，而手机助手备份需要备份图片 */
    //    private boolean exportPhoto = true;

    public ContactInfo() {
        mVCardType = VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8;
    }

    public ContactInfo(int vcardType, String accountName, String accoutType) {
        mVCardType = vcardType;
        account_name = accountName;
        account_type = accoutType;
    }

    //    public void setExportPhoto(boolean value) {
    //        exportPhoto = value;
    //    }

    //    public void setHavePhoto(boolean isHavePhoto) {
    //        this.isHavePhoto = isHavePhoto;
    //    }
    //
    //    public boolean getHavePhoto() {
    //        return isHavePhoto;
    //    }

    public long getDefaultRawId() {
        return defaultRawId;
    }

    public void setDefaultRawId(long rawid) {
        defaultRawId = rawid;
        addRawId(rawid);
    }

    public String getFullName() {
        if (TextUtils.isEmpty(mFullName)) {
            return "";
        }
        return mFullName;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public String getPhoneticFullName() {
        return mPhoneticFullName;
    }

    public String getFirstName() {

        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setNameId(long id) {
        this.nameId = id;
    }

    public long getNameId() {
        return nameId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName == null ? "" : middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String accountName) {
        account_name = accountName;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String accountType) {
        account_type = accountType;
    }

    public LinkedHashSet<PhoneInfo> getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getPhoneNum() {
        if (ContactUtil.isEmpty(phoneNumbers)) {
            return "";
        } else {
            return ((PhoneInfo) phoneNumbers.toArray()[0]).getNumber();
        }
    }

    public LinkedHashSet<GroupInfo> getGroups() {
        return groups;
    }

    public void setPhoneNumbers(LinkedHashSet<PhoneInfo> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setGroups(LinkedHashSet<GroupInfo> groups) {
        this.groups = groups;
    }

    public LinkedHashSet<EmailInfo> getEmails() {
        return emails;
    }

    public void setEmails(LinkedHashSet<EmailInfo> emails) {
        this.emails = emails;
    }

    public LinkedHashSet<CompanyInfo> getCompanies() {
        return companies;
    }

    public void setCompanies(LinkedHashSet<CompanyInfo> companies) {
        this.companies = companies;
    }

    public LinkedHashSet<AddressInfo> getAddresses() {
        return addresses;
    }

    public void setAddresses(LinkedHashSet<AddressInfo> addresses) {
        this.addresses = addresses;
    }

    public LinkedHashSet<WebsiteInfo> getWebsites() {
        return websites;
    }

    public void setWebsites(LinkedHashSet<WebsiteInfo> websites) {
        this.websites = websites;
    }

    public LinkedHashSet<ImInfo> getIms() {
        return ims;
    }

    public void setIms(LinkedHashSet<ImInfo> ims) {
        this.ims = ims;
    }

    public LinkedHashSet<EventInfo> getEvents() {
        return events;
    }

    public void setEvents(LinkedHashSet<EventInfo> events) {
        this.events = events;
    }

    public LinkedHashSet<NoteInfo> getNotes() {
        return notes;
    }

    public void setNotes(LinkedHashSet<NoteInfo> notes) {
        this.notes = notes;
    }

    public LinkedHashSet<PhotoInfo> getPhotoes() {
        return photoes;
    }

    public Bitmap getPhoto(Context context) {
        Bitmap mUserIcon;
        if (ContactUtil.isEmpty(photoes)) {
            return null;
        }
        PhotoInfo temp = (PhotoInfo) photoes.toArray()[0];
        mUserIcon = temp.getBitmap(context);
        return mUserIcon;
    }

    public void setPhotoes(LinkedHashSet<PhotoInfo> photoes) {
        this.photoes = photoes;
    }

    public LinkedHashSet<NicknameInfo> getNicknames() {
        return nicknames;
    }

    public void setNicknames(LinkedHashSet<NicknameInfo> nicknames) {
        this.nicknames = nicknames;
    }

    public LinkedHashSet<SipInfo> getSips() {
        return sips;
    }

    public void setSips(LinkedHashSet<SipInfo> sips) {
        this.sips = sips;
    }

    public LinkedHashSet<RelationInfo> getRelations() {
        return relations;
    }

    public void setRelations(LinkedHashSet<RelationInfo> relations) {
        this.relations = relations;
    }

    public int getVCardType() {
        return mVCardType;
    }

    public String getDisplayName() {
        if (mDisplayName == null) {
            constructDisplayName();
        }
        return mDisplayName;
    }

    public String getDisplayPhone() {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            return "";
        } else {
            for (PhoneInfo info : phoneNumbers) {
                if (!ContactUtil.isEmpty(info.getNumber())) {
                    return info.getNumber();
                }
            }
        }
        return "";
    }

    public LinkedHashSet<Long> getRawIds() {
        return mRawIds;
    }

    public void addRawId(long rawId) {
        if (mRawIds == null) {
            mRawIds = new LinkedHashSet<Long>();
        }
        if (!mRawIds.contains(rawId)) {
            mRawIds.add(rawId);
        }
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("contactId:");
        builder.append(this.getId());
        builder.append(", fullName:");
        builder.append(mFullName);
        builder.append(", starred=");
        builder.append(starred);
        builder.append(", {account_name=");
        builder.append(account_name);
        builder.append(", account_type=");
        builder.append(account_type);
        builder.append(", addresses=");
        builder.append(addresses);
        builder.append(", companies=");
        builder.append(companies);
        builder.append(", emails=");
        builder.append(emails);
        builder.append(", events=");
        builder.append(events);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", rawId=");
        builder.append(getId());
        builder.append(", nicknames=");
        builder.append(nicknames);
        builder.append(", ims=");
        builder.append(ims);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", prefix=");
        builder.append(prefix);
        builder.append(", surfix=");
        builder.append(suffix);
        builder.append(", middleName=");
        builder.append(middleName);
        builder.append(", notes=");
        builder.append(notes);
        builder.append(", phoneNumbers=");
        builder.append(phoneNumbers);
        builder.append(", sips=");
        builder.append(sips);
        builder.append(", websites=");
        builder.append(websites);
        builder.append("}");
        return builder.toString();
    }

    public String getNString() {
        StringBuilder sb = new StringBuilder();
        // N
        sb.append(VCardConstants.PROPERTY_N);
        if (ContactUtil.containsOnlyNonCrLfPrintableAscii(firstName)
                && ContactUtil.containsOnlyNonCrLfPrintableAscii(lastName)
                && ContactUtil.containsOnlyNonCrLfPrintableAscii(middleName)
                && ContactUtil.containsOnlyNonCrLfPrintableAscii(prefix)
                && ContactUtil.containsOnlyNonCrLfPrintableAscii(suffix)) {
            sb.append(":");
            if (!ContactUtil.isEmpty(lastName))
                sb.append(lastName);
            sb.append(";");
            if (!ContactUtil.isEmpty(firstName))
                sb.append(firstName);
            sb.append(";");
            if (!ContactUtil.isEmpty(middleName))
                sb.append(middleName);
            sb.append(";");
            if (!ContactUtil.isEmpty(prefix))
                sb.append(prefix);
            sb.append(";");
            if (!ContactUtil.isEmpty(suffix))
                sb.append(suffix);
            sb.append(";");
        } else {
            sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
            if (!ContactUtil.isEmpty(lastName))
                sb.append(ContactUtil.qpEncoding(lastName));
            sb.append(";");
            if (!ContactUtil.isEmpty(firstName))
                sb.append(ContactUtil.qpEncoding(firstName));
            sb.append(";");
            if (!ContactUtil.isEmpty(middleName))
                sb.append(ContactUtil.qpEncoding(middleName));
            sb.append(";");
            if (!ContactUtil.isEmpty(prefix))
                sb.append(ContactUtil.qpEncoding(prefix));
            sb.append(";");
            if (!ContactUtil.isEmpty(suffix))
                sb.append(ContactUtil.qpEncoding(suffix));
        }
        sb.append("\r\n");
        return sb.toString();
    }

    //    public String getPhoneticString() {
    //        StringBuilder sb = new StringBuilder();
    //        // X-PHONETIC-FIRST-NAME
    //        if (!ContactUtil.isEmpty(phoneticFirstName)) {
    //            sb.append(VCardConstants.PROPERTY_X_PHONETIC_FIRST_NAME)
    //                    .append(":").append(phoneticFirstName);
    //            sb.append("\r\n");
    //        }
    //        // X-PHONETIC-LAST-NAME
    //        if (!ContactUtil.isEmpty(phoneticFirstName)) {
    //            sb.append(VCardConstants.PROPERTY_X_PHONETIC_LAST_NAME).append(":")
    //                    .append(phoneticLastName);
    //            sb.append("\r\n");
    //        }
    //        // X-PHONETIC-MIDDLE-NAME
    //        if (!ContactUtil.isEmpty(phoneticFirstName)) {
    //            sb.append(VCardConstants.PROPERTY_X_PHONETIC_MIDDLE_NAME)
    //                    .append(":").append(phoneticMiddleName);
    //            sb.append("\r\n");
    //        }
    //        return sb.toString();
    //    }

    public boolean compare(ContactInfo old) {

        // 收藏夹
        if (!ContactUtil.isEmpty(starred)) {
            if (ContactUtil.isEmpty(old.starred)) {
                return false;
            }
            if (!starred.equals(old.starred)) {
                return false;
            }
        }
        // 其他信息
        if (nicknames != null) {
            if (old.nicknames == null) {
                return false;
            }
            if (!nicknames.equals(old.nicknames)) {
                return false;
            }
        }
        if (phoneNumbers != null) {
            if (old.phoneNumbers == null) {
                return false;
            }
            if (!phoneNumbers.equals(old.phoneNumbers)) {
                return false;
            }
        }
        if (emails != null) {
            if (old.emails == null) {
                return false;
            }
            if (!emails.equals(old.emails)) {
                return false;
            }
        }
        if (sips != null) {
            if (old.sips == null) {
                return false;
            }
            if (!sips.equals(old.sips)) {
                return false;
            }
        }
        if (companies != null) {
            if (old.companies == null) {
                return false;
            }
            if (!companies.equals(old.companies)) {
                return false;
            }
        }
        if (addresses != null) {
            if (old.addresses == null) {
                return false;
            }
            if (!addresses.equals(old.addresses)) {
                return false;
            }
        }
        if (websites != null) {
            if (old.websites == null) {
                return false;
            }
            if (!websites.equals(old.websites)) {
                return false;
            }
        }
        if (ims != null) {
            if (old.ims == null) {
                return false;
            }
            if (!ims.equals(old.ims)) {
                return false;
            }
        }
        if (events != null) {
            if (old.events == null) {
                return false;
            }
            if (!events.equals(old.events)) {
                return false;
            }
        }
        if (notes != null) {
            if (old.notes == null) {
                return false;
            }
            if (!notes.equals(old.notes)) {
                return false;
            }
        }
        if (groups != null) {
            if (old.groups == null) {
                return false;
            }
            if (!groups.equals(old.groups)) {
                return false;
            }
        }

        if (relations != null) {
            if (old.relations == null) {
                return false;
            }
            if (!relations.equals(old.relations)) {
                return false;
            }
        }
        return true;
    }

    /*
     * 数据样例 BEGIN:VCARD VERSION:2.1 N:;111;;; FN:111 X-NICKNAME:shouma
     * X-NICKNAME;ENCODING=QUOTED-PRINTABLE:=E6=94=B6=E5=90=97
     * TEL;CELL:1-369-988-5522 URL:www.site.com URL:网址的 ORG:bcdata TITLE:title
     * END:VCARD
     */
    @Override
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        // vcard begin
        sb.append("BEGIN:VCARD").append("\r\n").append("VERSION:2.1").append("\r\n");
        // FN
        //        if (!ContactUtil.isEmpty(lastName) && !ContactUtil.isEmpty(middleName)
        //                && !ContactUtil.isEmpty(firstName) && !ContactUtil.isEmpty(prefix)
        //                && !ContactUtil.isEmpty(suffix)) {
        String temp = getRelateName();
        if (!ContactUtil.isEmpty(temp)) {
            mFullName = temp;
        }
        if (!ContactUtil.isEmpty(mFullName)) {
            sb.append(VCardConstants.PROPERTY_FN);
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(mFullName)) {
                sb.append(":").append(mFullName);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(mFullName));
            }
            sb.append("\r\n");
        }

        //账号名字
        if (!ContactUtil.isEmpty(account_name)) {
            sb.append(VCardConstants.PROPERTY_X_ACCOUNT_NAME);
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(account_name)) {
                sb.append(":").append(account_name);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(account_name));
            }
            sb.append("\r\n");
        }

        //账号类型
        if (!ContactUtil.isEmpty(account_type)) {
            sb.append(VCardConstants.PROPERTY_X_ACCOUNT_TYPE);
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(account_type)) {
                sb.append(":").append(account_type);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(account_type));
            }
            sb.append("\r\n");
        }

        // N
        sb.append(getNString());
        // 拼音
        //        sb.append(getPhoneticString());

        // 收藏夹
        if (!ContactUtil.isEmpty(starred)) {
            sb.append(VCardConstants.PROPERTY_X_STARRED);
            sb.append(":");
            sb.append(starred);
            sb.append("\r\n");
        }

        // 其他信息
        if (nicknames != null) {
            for (NicknameInfo info : nicknames) {
                sb.append(info.toVCardString());
            }
        }
        if (phoneNumbers != null) {
            for (PhoneInfo info : phoneNumbers) {
                sb.append(info.toVCardString());
            }
        }
        if (emails != null) {
            for (EmailInfo info : emails) {
                sb.append(info.toVCardString());
            }
        }
        if (sips != null) {
            for (SipInfo info : sips) {
                sb.append(info.toVCardString());
            }
        }
        if (companies != null) {
            for (CompanyInfo info : companies) {
                sb.append(info.toVCardString());
            }
        }
        if (addresses != null) {
            for (AddressInfo info : addresses) {
                sb.append(info.toVCardString());
            }
        }
        if (websites != null) {
            for (WebsiteInfo info : websites) {
                sb.append(info.toVCardString());
            }
        }
        if (ims != null) {
            for (ImInfo info : ims) {
                sb.append(info.toVCardString());
            }
        }
        if (events != null) {
            for (EventInfo info : events) {
                sb.append(info.toVCardString());
            }
        }
        if (notes != null) {
            for (NoteInfo info : notes) {
                sb.append(info.toVCardString());
            }
        }
        if (groups != null) {
            for (GroupInfo info : groups) {
                sb.append(info.toVCardString());
            }
        }
        if (relations != null) {
            for (RelationInfo info : relations) {
                sb.append(info.toVCardString());
            }
        }
        // 备份图片信息
        //        if (exportPhoto && photoes != null) {
        //            for (PhotoInfo info : photoes) {
        //                sb.append(info.toVCardString());
        //            }
        //        }
        // vcard end
        sb.append("END:VCARD").append("\r\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(mFullName))
            return 0;

        return mFullName.hashCode();
    }

    /*
     * 当FN(vcard对象的名称)字段值相同，且存在相同的TEL(电话号码)，则认为该信息为同一联系人的信息。
     * 如果FN(vcard对象的名称)字段值相同，且电话信息均为空，也认为是同一联系人
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContactInfo)) {
            return false;
        }
        final ContactInfo contact = (ContactInfo) obj;
        if (!ContactUtil.isEmpty(mFullName)) {
            if (mFullName.equalsIgnoreCase(contact.getFullName())) {
                if (!ContactUtil.isEmpty(phoneNumbers)) {
                    if (!ContactUtil.isEmpty(contact.phoneNumbers)) {
                        for (PhoneInfo phone1 : phoneNumbers) {
                            for (PhoneInfo phone2 : contact.phoneNumbers) {
                                if (phone1.equals(phone2)) {
                                    return true;
                                }
                            }
                        }
                    }
                } else if (ContactUtil.isEmpty(phoneNumbers)
                        && ContactUtil.isEmpty(contact.phoneNumbers)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean equalsInName(Object obj) {
        if (!(obj instanceof ContactInfo)) {
            return false;
        }
        final ContactInfo contact = (ContactInfo) obj;
        if (nameEquals(mDisplayName, contact.mDisplayName)) {
            return true;
        } else if (nameEquals(firstName, contact.firstName) && nameEquals(middleName, contact.middleName)
                && nameEquals(lastName, contact.lastName)) {
            return true;
        } else {
            if (!ContactUtil.isEmpty(contact.middleName) && ContactUtil.isChineseName(contact.middleName)) {
                String f1 = firstName;
                String f2 = contact.firstName;
                if (f1 == null) {
                    f1 = "";
                }
                if (f2 == null) {
                    f2 = "";
                }
                return nameEquals(middleName + f1, contact.middleName + f2);
            }
        }
        return false;
    }

    private boolean nameEquals(String nm1, String nm2) {
        if (nm1 == null) {
            nm1 = "";
        }
        if (nm2 == null) {
            nm2 = "";
        }
        return nm1.equals(nm2);
    }

    /**
     * Consolidate several fielsds (like mName) using name candidates,
     */
    public void consolidateFields() {
        constructDisplayName();
        if (mPhoneticFullName != null) {
            mPhoneticFullName = mPhoneticFullName.trim();
        }
    }

    /**
     * 获取关联名字
     * @return 用来关联的名字
     */
    public String getRelateName() {
        if (!TextUtils.isEmpty(mRelateName)) {
            return mRelateName.trim();
        }
        if (!(ContactUtil.isEmpty(getLastName()) && ContactUtil.isEmpty(getFirstName()))) {
            mRelateName = VCardUtils.constructNameFromElements(VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8,
                    getLastName(), getMiddleName(), getFirstName(),
                    getPrefix(), getSuffix());
        } else if (!TextUtils.isEmpty(mFullName)) {
            mRelateName = mFullName;
        } else {
            mRelateName = "";
        }
        //        else if (!(ContactUtil.isEmpty(getPhoneticLastName()) && ContactUtil.isEmpty(getPhoneticFirstName()))) {
        //            mRelateName = VCardUtils.constructNameFromElements(VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8,
        //                    getPhoneticLastName(), getPhoneticMiddleName(),
        //                    getPhoneticFirstName());
        //        }
        return mRelateName.trim();
    }

    /**
     * Construct the display name. The constructed data must not be null.
     */
    private void constructDisplayName() {
        if (!ContactUtil.isEmpty(mFullName)) {
            mDisplayName = mFullName;
        } else if (!(ContactUtil.isEmpty(getLastName()) && ContactUtil
                .isEmpty(getFirstName()))) {
            mDisplayName = VCardUtils.constructNameFromElements(mVCardType,
                    getLastName(), getMiddleName(), getFirstName(),
                    getPrefix(), getSuffix());
        } else if (!ContactUtil.isEmpty(getEmails())) {
            Iterator<EmailInfo> it = getEmails().iterator();
            if (it != null && it.hasNext()) {
                mDisplayName = it.next().getAddress();
            }
        } else if (!ContactUtil.isEmpty(getPhoneNumbers())) {
            Iterator<PhoneInfo> it = getPhoneNumbers().iterator();
            if (it != null && it.hasNext()) {
                mDisplayName = it.next().getNumber();
            }
        } else if (!ContactUtil.isEmpty(getAddresses())) {
            Iterator<AddressInfo> it = getAddresses().iterator();
            if (it != null && it.hasNext()) {
                mDisplayName = it.next().getFormattedAddress(mVCardType);
            }
        } else if (!ContactUtil.isEmpty(getCompanies())) {
            Iterator<CompanyInfo> it = getCompanies().iterator();
            if (it != null && it.hasNext()) {
                mDisplayName = it.next().getFormattedString();
            }
        }
        //        else if (!(ContactUtil.isEmpty(getPhoneticLastName()) && ContactUtil
        //                .isEmpty(getPhoneticFirstName()))) {
        //            mDisplayName = VCardUtils.constructNameFromElements(mVCardType,
        //                    getPhoneticLastName(), getPhoneticMiddleName(),
        //                    getPhoneticFirstName());
        //        }
        if (mDisplayName == null) {
            mDisplayName = "";
        }
    }

    public boolean haveName() {
        return !TextUtils.isEmpty(firstName) || !TextUtils.isEmpty(lastName) || !TextUtils.isEmpty(middleName);
    }

    public static class Property {
        private String mPropertyName;
        private Map<String, Collection<String>> mParameterMap = new HashMap<String, Collection<String>>();
        private List<String> mPropertyValueList = new ArrayList<String>();
        private byte[] mPropertyBytes;

        public void setPropertyName(final String propertyName) {
            mPropertyName = propertyName;
        }

        public void addParameter(final String paramName, final String paramValue) {
            Collection<String> values;
            if (!mParameterMap.containsKey(paramName)) {
                if (paramName.equals("TYPE")) {
                    values = new HashSet<String>();
                } else {
                    values = new ArrayList<String>();
                }
                mParameterMap.put(paramName, values);
            } else {
                values = mParameterMap.get(paramName);
            }
            values.add(paramValue);
        }

        public void addToPropertyValueList(final String propertyValue) {
            mPropertyValueList.add(propertyValue);
        }

        public void setPropertyBytes(final byte[] propertyBytes) {
            mPropertyBytes = propertyBytes;
        }

        public final Collection<String> getParameters(String type) {
            return mParameterMap.get(type);
        }

        public final List<String> getPropertyValueList() {
            return mPropertyValueList;
        }

        public void clear() {
            mPropertyName = null;
            mParameterMap.clear();
            mPropertyValueList.clear();
            mPropertyBytes = null;
        }
    }

    public void addProperty(final Property property) {
        final String propName = property.mPropertyName;
        final Map<String, Collection<String>> paramMap = property.mParameterMap;
        final List<String> propValueList = property.mPropertyValueList;
        byte[] propBytes = property.mPropertyBytes;
        if (propValueList.size() == 0) {
            return;
        }
        final String propValue = listToString(propValueList).trim();

        if (propName.equals(VCardConstants.PROPERTY_VERSION)) {
            // vCard version. Ignore this.
        } else if (propName.equals(VCardConstants.PROPERTY_FN)) {
            mFullName = propValue;
        } else if (propName.equals(VCardConstants.PROPERTY_X_ACCOUNT_NAME)) {
            account_name = propValue;
        } else if (propName.equals(VCardConstants.PROPERTY_X_ACCOUNT_TYPE)) {
            account_type = propValue;
        } else if (propName.equals(VCardConstants.PROPERTY_X_STARRED)) {
            starred = propValue;
        } else if (propName.equals(VCardConstants.PROPERTY_X_GROUP)) {
            addGroup(propValue);
        } else if (propName.equals(VCardConstants.PROPERTY_NAME) && mFullName == null) {
            // Only in vCard 3.0. Use this if FN, which must exist in vCard 3.0
            // but may not
            // actually exist in the real vCard data, does not exist.
            mFullName = propValue;
        } else if (propName.equals(VCardConstants.PROPERTY_N)) {
            handleNProperty(propValueList);
        } else if (propName.equals(VCardConstants.PROPERTY_SORT_STRING)) {
            mPhoneticFullName = propValue;
        } else if (propName.equals(VCardConstants.PROPERTY_NICKNAME)
                || propName.equals(VCardConstants.PROPERTY_X_EPOCSECONDNAME)
                || propName.equals(VCardConstants.ImportOnly.PROPERTY_X_NICKNAME)) {
            addNickName(propValue);
        } else if (propName.equals(VCardConstants.PROPERTY_SOUND)) {
            // todo
        } else if (propName.equals(VCardConstants.PROPERTY_ADR)) {
            boolean valuesAreAllEmpty = true;
            for (String value : propValueList) {
                if (value.length() > 0) {
                    valuesAreAllEmpty = false;
                    break;
                }
            }
            if (valuesAreAllEmpty) {
                return;
            }

            int type = -1;
            String label = null;
            boolean isPrimary = false;
            Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    typeString = typeString.toUpperCase();
                    if (typeString.equals(VCardConstants.PARAM_TYPE_PREF)) {
                        isPrimary = true;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_HOME)) {
                        type = StructuredPostal.TYPE_HOME;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_WORK)
                            || typeString.equalsIgnoreCase(VCardConstants.PARAM_EXTRA_TYPE_COMPANY)) {
                        // "COMPANY" seems emitted by Windows Mobile, which is
                        // not
                        // specifically supported by vCard 2.1. We assume this
                        // is same
                        // as "WORK".
                        type = StructuredPostal.TYPE_WORK;
                    } else if (typeString
                            .equals(VCardConstants.PARAM_ADR_TYPE_PARCEL)
                            || typeString.equals(VCardConstants.PARAM_ADR_TYPE_DOM)
                            || typeString.equals(VCardConstants.PARAM_ADR_TYPE_INTL)) {
                        // We do not have any appropriate way to store this
                        // information.
                    } else {
                        if (typeString.startsWith("X-") && type < 0) {
                            typeString = typeString.substring(2);
                        }
                        // vCard 3.0 allows iana-token. Also some vCard 2.1
                        // exporters
                        // emit non-standard types. We do not handle their
                        // values now.
                        type = StructuredPostal.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
            }
            // We use "HOME" as default
            if (type < 0) {
                type = StructuredPostal.TYPE_HOME;
            }
            addPostal(type, propValueList, label, isPrimary);
        } else if (propName.equals(VCardConstants.PROPERTY_EMAIL)) {
            int type = -1;
            String label = null;
            boolean isPrimary = false;
            Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    typeString = typeString.toUpperCase();
                    if (typeString.equals(VCardConstants.PARAM_TYPE_PREF)) {
                        isPrimary = true;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_HOME)) {
                        type = Email.TYPE_HOME;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_WORK)) {
                        type = Email.TYPE_WORK;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_CELL)) {
                        type = Email.TYPE_MOBILE;
                    } else {
                        if (typeString.startsWith("X-") && type < 0) {
                            typeString = typeString.substring(2);
                        }
                        // vCard 3.0 allows iana-token.
                        // We may have INTERNET (specified in vCard spec),
                        // SCHOOL, etc.
                        type = Email.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
            }
            if (type < 0) {
                type = Email.TYPE_OTHER;
            }
            addEmail(type, propValue, label, isPrimary);
        } else if (propName.equals(VCardConstants.PROPERTY_X_EVENT)) {
            int type = -1;
            String label = null;
            Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    typeString = typeString.toUpperCase();
                    if (typeString.equals(VCardConstants.PARAM_EXTRA_TYPE_ANNIVERSARY)) {
                        type = Event.TYPE_ANNIVERSARY;
                    } else if (typeString.equals(VCardConstants.PARAM_EXTRA_TYPE_BIRTHDAY)) {
                        type = Event.TYPE_BIRTHDAY;
                    } else {
                        if (typeString.startsWith("X-") && type < 0) {
                            typeString = typeString.substring(2);
                        }
                        type = Event.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
            }
            if (type < 0) {
                type = Event.TYPE_OTHER;
            }
            addEvent(type, propValue, label);
        } else if (propName.equals(VCardConstants.PROPERTY_BDAY)) {
            addEvent(Event.TYPE_BIRTHDAY, propValue, null);
        } else if (propName.equals(VCardConstants.PROPERTY_X_ANNIVERSARY)) {
            addEvent(Event.TYPE_ANNIVERSARY, propValue, null);
        } else if (propName.equals(VCardConstants.PROPERTY_X_SIP)) {
            int type = -1;
            String label = null;
            boolean isPrimary = false;
            Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    typeString = typeString.toUpperCase();
                    if (typeString.equals(VCardConstants.PARAM_TYPE_PREF)) {
                        isPrimary = true;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_HOME)) {
                        type = 1;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_WORK)) {
                        type = 2;
                    } else {
                        if (typeString.startsWith("X-") && type < 0) {
                            typeString = typeString.substring(2);
                        }
                        type = Email.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
            }
            if (type < 0) {
                type = Email.TYPE_OTHER;
            }
            addSip(type, propValue, label, isPrimary);
        } else if (propName.equals(VCardConstants.PROPERTY_ORG)) {
            // vCard specification does not specify other types.
            final int type = Organization.TYPE_WORK;
            boolean isPrimary = false;
            Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    if (typeString.equals(VCardConstants.PARAM_TYPE_PREF)) {
                        isPrimary = true;
                    }
                }
            }
            handleOrgValue(type, propValueList, isPrimary);
        } else if (propName.equals(VCardConstants.PROPERTY_TITLE)) {
            handleTitleValue(propValue);
        } else if (propName.equals(VCardConstants.PROPERTY_PHOTO)) {
            //                || propName.equals(VCardConstants.PROPERTY_LOGO)) {
            //            Collection<String> paramMapValue = paramMap.get("VALUE");
            //            if (paramMapValue != null && paramMapValue.contains("URL")) {
            //                // Currently we do not have appropriate example for testing this
            //                // case.
            //            } else {
            //                final Collection<String> typeCollection = paramMap.get("TYPE");
            //                String formatName = null;
            //                boolean isPrimary = false;
            //                if (typeCollection != null) {
            //                    for (String typeValue : typeCollection) {
            //                        if (VCardConstants.PARAM_TYPE_PREF.equals(typeValue)) {
            //                            isPrimary = true;
            //                        } else if (formatName == null) {
            //                            formatName = typeValue;
            //                        }
            //                    }
            //                }
            //                                addPhotoBytes(formatName, propBytes, isPrimary);
            //            }
            if (!TextUtils.isEmpty(propValue)) {
                addPhoto(propValue);
            }
        } else if (propName.equals(VCardConstants.PROPERTY_TEL)) {
            final Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            final Object typeObject = VCardUtils.getPhoneTypeFromStrings(typeCollection, propValue);
            final int type;
            final String label;
            if (typeObject instanceof Integer) {
                type = (Integer) typeObject;
                label = null;
            } else {
                type = Phone.TYPE_CUSTOM;
                label = typeObject.toString();
            }
            final boolean isPrimary;
            if (typeCollection != null && typeCollection.contains(VCardConstants.PARAM_TYPE_PREF)) {
                isPrimary = true;
            } else {
                isPrimary = false;
            }
            addPhone(type, propValue, label, isPrimary);
        } else if (propName.equals(VCardConstants.PROPERTY_X_SKYPE_PSTNNUMBER)) {
            // The phone number available via Skype.
            Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            final int type = Phone.TYPE_OTHER;
            final boolean isPrimary;
            if (typeCollection != null && typeCollection.contains(VCardConstants.PARAM_TYPE_PREF)) {
                isPrimary = true;
            } else {
                isPrimary = false;
            }
            addPhone(type, propValue, null, isPrimary);
        } else if (sImMap.containsKey(propName)) {
            final int protocol = sImMap.get(propName);
            boolean isPrimary = false;
            int type = -1;
            String label = null;
            final Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    typeString = typeString.toUpperCase();
                    if (typeString.equals(VCardConstants.PARAM_TYPE_PREF)) {
                        isPrimary = true;
                    } else if (type < 0) {
                        if (typeString.equalsIgnoreCase(VCardConstants.PARAM_TYPE_HOME)) {
                            type = Im.TYPE_HOME;
                        } else if (typeString.equalsIgnoreCase(VCardConstants.PARAM_TYPE_WORK)) {
                            type = Im.TYPE_WORK;
                        } else {
                            if (typeString.startsWith("X-")) {
                                typeString = typeString.substring(2);
                            }
                            type = Im.TYPE_CUSTOM;
                            label = typeString;
                        }
                    }
                }
            }
            if (type < 0) {
                type = Im.TYPE_HOME;
            }
            addIm(protocol, null, type, label, propValue, isPrimary);
        } else if (propName.equals(VCardConstants.PROPERTY_X_SNS)) {
            String customProtocol = null;
            int protocol = 0;
            boolean isPrimary = false;
            int type = -1;
            String label = null;
            final Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    typeString = typeString.toUpperCase();
                    if (typeString.equals(VCardConstants.PARAM_TYPE_PREF)) {
                        isPrimary = true;
                    } else if (typeString.equalsIgnoreCase(VCardConstants.PARAM_TYPE_HOME)) {
                        type = Im.TYPE_HOME;
                    } else if (typeString.equalsIgnoreCase(VCardConstants.PARAM_TYPE_WORK)) {
                        type = Im.TYPE_WORK;
                    } else if (typeString.startsWith("X-")) {
                        if (type < 0) {
                            typeString = typeString.substring(2);
                            type = Im.TYPE_CUSTOM;
                            label = typeString;
                        }
                    } else {
                        if (typeString.equals("AIM"))
                            protocol = 0;
                        else if (typeString.equals("MSN"))
                            protocol = 1;
                        else if (typeString.equals("YAHOO"))
                            protocol = 2;
                        else if (typeString.equals("SKYPE"))
                            protocol = 3;
                        else if (typeString.equals("QQ"))
                            protocol = 4;
                        else if (typeString.equals("GOOGLE_TALK"))
                            protocol = 5;
                        else if (typeString.equals("ICQ"))
                            protocol = 6;
                        else if (typeString.equals("JABBER"))
                            protocol = 7;
                        else if (typeString.equals("NETMEETING"))
                            protocol = 7;
                        else {
                            protocol = -1;
                            customProtocol = typeString;
                        }
                    }
                }
            }
            if (type < 0) {
                type = Im.TYPE_HOME;
            }
            addIm(protocol, customProtocol, type, label, propValue, isPrimary);
        } else if (propName.equals(VCardConstants.PROPERTY_NOTE)) {
            addNote(propValue);
        } else if (propName.equals(VCardConstants.PROPERTY_URL)) {
            int type = -1;
            String label = null;
            Collection<String> typeCollection = paramMap
                    .get(VCardConstants.PARAM_TYPE);
            if (typeCollection != null) {
                for (String typeString : typeCollection) {
                    typeString = typeString.toUpperCase();
                    if (typeString.equals(VCardConstants.PARAM_TYPE_HOMEPAGE)) {
                        type = Website.TYPE_HOMEPAGE;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_BLOG)) {
                        type = Website.TYPE_BLOG;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_PROFILE)) {
                        type = Website.TYPE_PROFILE;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_HOME)) {
                        type = Website.TYPE_HOME;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_WORK)) {
                        type = Website.TYPE_WORK;
                    } else if (typeString.equals(VCardConstants.PARAM_TYPE_FTP)) {
                        type = Website.TYPE_FTP;
                    } else {
                        if (typeString.startsWith("X-") && type < 0) {
                            typeString = typeString.substring(2);
                        }
                        type = Website.TYPE_CUSTOM;
                        label = typeString;
                    }
                }
            }
            if (type < 0) {
                type = Website.TYPE_HOMEPAGE;
            }
            addWebsite(type, propValue, label);
        } else if (propName.equals(VCardConstants.PROPERTY_X_RELATION)) {
            int type = -1;
            String label = null;
            Collection<String> typeCollection = paramMap.get(VCardConstants.PARAM_TYPE);
            Object object = VCardUtils.getRelationTypeFromStrings(typeCollection, propValue);
            if (object instanceof Integer) {
                type = (Integer) object;
                label = null;
            } else {
                type = Relation.TYPE_CUSTOM;
                label = object.toString();
            }
            addRelation(type, propValue, label);
        }
        //        else if (propName.equals(VCardConstants.PROPERTY_ROLE)) {
        // This conflicts with TITLE. Ignore for now...
        // handleTitleValue(propValue);
        //        }
        //        else if (propName.equals(VCardConstants.PROPERTY_X_ANDROID_CUSTOM)) {
        //            // todo
        //
        //        }
        //        else if (propName.equals(VCardConstants.PROPERTY_X_PHONETIC_FIRST_NAME)) {
        //            setPhoneticFirstName(propValue);
        //        } else if (propName.equals(VCardConstants.PROPERTY_X_PHONETIC_MIDDLE_NAME)) {
        //            setPhoneticMiddleName(propValue);
        //        } else if (propName.equals(VCardConstants.PROPERTY_X_PHONETIC_LAST_NAME)) {
        //            setPhoneticLastName(propValue);
        //        } else if (propName.equals(VCardConstants.PROPERTY_X_ANDROID_CUSTOM)) {
        //            // todo
        //
        //        }
    }

    private String listToString(List<String> list) {
        final int size = list.size();
        if (size > 1) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (String type : list) {
                builder.append(type);
                if (i < size - 1) {
                    builder.append(";");
                }
            }
            return builder.toString();
        } else if (size == 1) {
            return list.get(0);
        } else {
            return "";
        }
    }

    private void handleNProperty(List<String> elems) {
        // Family, Given, Middle, Prefix, Suffix. (1 - 5)
        int size;
        if (elems == null || (size = elems.size()) < 1) {
            return;
        }
        if (size > 5) {
            size = 5;
        }
        switch (size) {
            case 5:
                setSuffix(elems.get(4));
            case 4:
                setPrefix(elems.get(3));
            case 3:
                setMiddleName(elems.get(2));
            case 2:
                setFirstName(elems.get(1));
            default:
                setLastName(elems.get(0));
        }
    }

    private void addNickName(final String nickName) {
        if (getNicknames() == null) {
            setNicknames(new LinkedHashSet<NicknameInfo>());
        }
        NicknameInfo info = new NicknameInfo();
        info.setName(nickName);
        getNicknames().add(info);
    }

    private void addEmail(int type, String data, String label, boolean isPrimary) {
        if (getEmails() == null) {
            setEmails(new LinkedHashSet<EmailInfo>());
        }
        EmailInfo info = new EmailInfo();
        info.setType(type);
        info.setAddress(data);
        info.setLabel(label);
        info.setIsPrimary(isPrimary ? 1 : 0);
        getEmails().add(info);
    }

    private void addEvent(int type, String data, String label) {
        if (getEvents() == null) {
            setEvents(new LinkedHashSet<EventInfo>());
        }
        EventInfo info = new EventInfo();
        info.setType(type);
        info.setStartDate(data);
        info.setLabel(label);
        getEvents().add(info);
    }

    private void addRelation(int type, String data, String label) {
        if (getRelations() == null) {
            setRelations(new LinkedHashSet<RelationInfo>());
        }
        RelationInfo info = new RelationInfo();
        info.setLabel(label);
        info.setType(type);
        info.setName(data);
        getRelations().add(info);
    }

    private void addSip(int type, String data, String label, boolean isPrimary) {
        if (getSips() == null) {
            setSips(new LinkedHashSet<SipInfo>());
        }
        SipInfo info = new SipInfo();
        info.setType(type);
        info.setAddress(data);
        info.setLabel(label);
        info.setIsPrimary(isPrimary ? 1 : 0);
        getSips().add(info);
    }

    private void addPostal(int type, List<String> propValueList, String label,
            boolean isPrimary) {
        if (getAddresses() == null) {
            setAddresses(new LinkedHashSet<AddressInfo>());
        }
        AddressInfo info = new AddressInfo();
        info.setType(type);
        info.setLabel(label);
        info.setIsPrimary(isPrimary ? 1 : 0);
        if (propValueList.size() > 0)
            info.setPobox(propValueList.get(0));
        if (propValueList.size() > 1)
            info.setExtendedAddress(propValueList.get(1));
        if (propValueList.size() > 2)
            info.setStreet(propValueList.get(2));
        if (propValueList.size() > 3)
            info.setCity(propValueList.get(3));
        if (propValueList.size() > 4)
            info.setRegion(propValueList.get(4));
        if (propValueList.size() > 5)
            info.setPostCode(propValueList.get(5));
        if (propValueList.size() > 6)
            info.setCountry(propValueList.get(6));

        getAddresses().add(info);
    }

    /**
     * Should be called via {@link #handleOrgValue(int, List, boolean)} or
     * {@link #handleTitleValue(String)}.
     */
    private void addNewOrganization(int type, final String companyName,
            final String departmentName, final String titleName,
            boolean isPrimary) {
        if (getCompanies() == null) {
            setCompanies(new LinkedHashSet<CompanyInfo>());
        }
        CompanyInfo info = new CompanyInfo();
        info.setType(type);
        info.setName(companyName);
        info.setTitle(titleName);
        info.setIsPrimary(isPrimary ? 1 : 0);
        getCompanies().add(info);
    }

    private static final List<String> sEmptyList = Collections
            .unmodifiableList(new ArrayList<String>(0));

    private void handleOrgValue(final int type, List<String> orgList,
            boolean isPrimary) {
        if (orgList == null) {
            orgList = sEmptyList;
        }
        final String companyName;
        final String departmentName;
        final int size = orgList.size();
        switch (size) {
            case 0: {
                companyName = "";
                departmentName = null;
                break;
            }
            case 1: {
                companyName = orgList.get(0);
                departmentName = null;
                break;
            }
            default: { // More than 1.
                companyName = orgList.get(0);
                // We're not sure which is the correct string for department.
                // In order to keep all the data, concatinate the rest of elements.
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < size; i++) {
                    if (i > 1) {
                        builder.append(' ');
                    }
                    builder.append(orgList.get(i));
                }
                departmentName = builder.toString();
            }
        }
        if (getCompanies() == null) {
            // Create new first organization entry, with "null" title which may
            // be
            // added via handleTitleValue().
            addNewOrganization(type, companyName, departmentName, null,
                    isPrimary);
            return;
        }
        for (CompanyInfo info : getCompanies()) {
            // Not use TextUtils.isEmpty() since ORG was set but the elements
            // might be empty.
            // e.g. "ORG;PREF:;" -> Both companyName and departmentName become
            // empty but not null.
            if (info.getName() == null) {
                // Probably the "TITLE" property comes before the "ORG" property
                // via
                // handleTitleLine().
                info.setName(companyName);
                info.setIsPrimary(isPrimary ? 1 : 0);
                return;
            }
        }
        // No OrganizatioData is available. Create another one, with "null"
        // title, which may be
        // added via handleTitleValue().
        addNewOrganization(type, companyName, departmentName, null, isPrimary);
    }

    /**
     * Set "title" value to the appropriate data. If there's more than one
     * OrganizationData objects, this input is attached to the last one which
     * does not have valid title value (not including empty but only null). If
     * there's no OrganizationData object, a new OrganizationData is created,
     * whose company name is set to null.
     */
    private void handleTitleValue(final String title) {
        if (getCompanies() == null) {
            // Create new first organization entry, with "null" other info,
            // which may be
            // added via handleOrgValue().
            addNewOrganization(1, null, null, title, false);
            return;
        }
        for (CompanyInfo info : getCompanies()) {
            if (info.getTitle() == null) {
                info.setTitle(title);
                return;
            }
        }
        // No Organization is available. Create another one, with "null" other
        // info, which may be
        // added via handleOrgValue().
        addNewOrganization(1, null, null, title, false);
    }

    //    private void addPhotoBytes(String formatName, byte[] photoBytes,
    //            boolean isPrimary) {
    //        if (getPhotoes() == null) {
    //            setPhotoes(new LinkedHashSet<PhotoInfo>());
    //        }
    //        final PhotoInfo info = new PhotoInfo();
    //        info.setBinaryData(photoBytes);
    //        info.setIsPrimary(isPrimary ? 1 : 0);
    //        getPhotoes().add(info);
    //    }

    //恢复联系人头像
    private void addPhoto(String path) {
        if (getPhotoes() == null) {
            setPhotoes(new LinkedHashSet<PhotoInfo>());
        }
        final PhotoInfo info = new PhotoInfo();
        info.setRemotePath(path);
        info.setId(0);
        getPhotoes().add(info);
    }

    private void addPhone(int type, String data, String label, boolean isPrimary) {
        if (getPhoneNumbers() == null) {
            setPhoneNumbers(new LinkedHashSet<PhoneInfo>());
        }
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        final String trimed = data.trim();
        final String formattedNumber;
        if (type == Phone.TYPE_PAGER) {
            formattedNumber = trimed;
        } else {
            final int length = trimed.length();
            for (int i = 0; i < length; i++) {
                char ch = trimed.charAt(i);
                if (('0' <= ch && ch <= '9') || (i == 0 && ch == '+')) {
                    builder.append(ch);
                }
            }

            // Use NANP in default when there's no information about locale.
            final int formattingType = (VCardConfig
                    .isJapaneseDevice(mVCardType) ? PhoneNumberUtils.FORMAT_JAPAN
                    : PhoneNumberUtils.FORMAT_NANP);
            PhoneNumberUtils.formatNumber(builder, formattingType);
            formattedNumber = builder.toString();
        }
        PhoneInfo info = new PhoneInfo();
        info.setType(type);
        info.setNumber(formattedNumber);
        info.setLabel(label);
        info.setIsPrimary(isPrimary ? 1 : 0);
        getPhoneNumbers().add(info);
    }

    private void addIm(int protocol, String customProtocol, int type,
            String label, String propValue, boolean isPrimary) {
        if (getIms() == null) {
            setIms(new LinkedHashSet<ImInfo>());
        }
        ImInfo info = new ImInfo();
        info.setProtocol(String.valueOf(protocol));
        info.setCustomProtocol(customProtocol);
        info.setType(type);
        info.setLabel(label);
        info.setData(propValue);
        info.setIsPrimary(isPrimary ? 1 : 0);
        getIms().add(info);
    }

    private void addNote(final String note) {
        if (getNotes() == null) {
            setNotes(new LinkedHashSet<NoteInfo>());
        }
        NoteInfo info = new NoteInfo();
        info.setNote(note);
        getNotes().add(info);
    }

    private void addGroup(final String name) {
        if (getGroups() == null) {
            setGroups(new LinkedHashSet<GroupInfo>());
        }
        GroupInfo info = new GroupInfo();
        info.setRemoteName(name);
        getGroups().add(info);
    }

    private void addWebsite(int type, String data, String label) {
        if (getWebsites() == null) {
            setWebsites(new LinkedHashSet<WebsiteInfo>());
        }
        WebsiteInfo info = new WebsiteInfo();
        info.setType(type);
        info.setUrl(data);
        info.setLabel(label);
        getWebsites().add(info);
    }

    private static final Map<String, Integer> sImMap = new HashMap<String, Integer>();
    static {
        sImMap.put(VCardConstants.PROPERTY_X_AIM, Im.PROTOCOL_AIM);
        sImMap.put(VCardConstants.PROPERTY_X_MSN, Im.PROTOCOL_MSN);
        sImMap.put(VCardConstants.PROPERTY_X_YAHOO, Im.PROTOCOL_YAHOO);
        sImMap.put(VCardConstants.PROPERTY_X_ICQ, Im.PROTOCOL_ICQ);
        sImMap.put(VCardConstants.PROPERTY_X_QQ, Im.PROTOCOL_QQ);
        sImMap.put(VCardConstants.PROPERTY_X_JABBER, Im.PROTOCOL_JABBER);
        sImMap.put(VCardConstants.PROPERTY_X_SKYPE_USERNAME, Im.PROTOCOL_SKYPE);
        sImMap.put(VCardConstants.PROPERTY_X_GOOGLE_TALK, Im.PROTOCOL_GOOGLE_TALK);
        sImMap.put(VCardConstants.ImportOnly.PROPERTY_X_GOOGLE_TALK_WITH_SPACE, Im.PROTOCOL_GOOGLE_TALK);
    }

    //更新公司职位的信息
    public void updateCompany() {
        if (!ContactUtil.isEmpty(getCompanies())) {
            LinkedHashSet<CompanyInfo> result = new LinkedHashSet<CompanyInfo>();
            for (CompanyInfo info : getCompanies()) {
                result.add(info);
            }
            setCompanies(result);
        }
    }
}
