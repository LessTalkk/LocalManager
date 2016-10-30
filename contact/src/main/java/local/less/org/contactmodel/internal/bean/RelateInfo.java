
package local.less.org.contactmodel.internal.bean;

import android.text.TextUtils;

import java.util.LinkedHashSet;

import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConfig;
import local.less.org.contactmodel.internal.vcard.VCardUtils;

/**
 * @author weijiantao on 2015/9/24.
 */
public class RelateInfo {
    private long mCid = 0;
    private String fullName = "";
    private String mRelateName = "";
    private LinkedHashSet<PhoneInfo> phoneNumbers;

    private String firstName = ""; // 名
    private String lastName = ""; // 姓
    private String middleName = "";
    private String prefix = "";
    private String suffix = "";

    public void setFullName(String fullname) {
        if (TextUtils.isEmpty(fullname)) {
            fullName = "";
        } else {
            fullName = fullname;
        }
    }

    public String getFullName() {
        if (fullName == null) {
            return "";
        }
        return fullName;
    }

    /**
     * 获取用来关联名字
     * @return 可以用来关联的名字
     */
    public String getRelateName() {
        if (!TextUtils.isEmpty(mRelateName)) {
            return mRelateName;
        }
        if (!(ContactUtil.isEmpty(getLastName()) && ContactUtil
                .isEmpty(getFirstName()))) {
            mRelateName = VCardUtils.constructNameFromElements(VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8,
                    getLastName(), getMiddleName(), getFirstName(),
                    getPrefix(), getSuffix());
        } else if (!TextUtils.isEmpty(fullName)) {
            mRelateName = fullName;
        } else {
            mRelateName = "";
        }
        mRelateName = mRelateName.trim();
        return mRelateName;
    }

    public boolean haveRelateName() {
        return !TextUtils.isEmpty(mRelateName);
    }

    public void setCid(long id) {
        mCid = id;
    }

    public long getCid() {
        return mCid;
    }

    public LinkedHashSet<PhoneInfo> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(LinkedHashSet<PhoneInfo> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
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

}
