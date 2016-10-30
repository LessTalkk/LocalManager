
package local.less.org.contactmodel.internal.bean;

import java.util.LinkedHashSet;

/**
 * @author weijiantao on 2015/12/18.
 */
public class UnbackupInfo {
    public long mVersion = 0;
    public String mName = "";
    private LinkedHashSet<PhoneInfo> mPhones;
    public String mLookUp = "";

    public void addPhone(PhoneInfo phoneInfo) {
        if (mPhones == null) {
            mPhones = new LinkedHashSet<PhoneInfo>();
        }
        mPhones.add(phoneInfo);
    }

    public LinkedHashSet<PhoneInfo> getPhones() {
        return mPhones;
    }
}
