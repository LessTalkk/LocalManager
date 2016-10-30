
package local.less.org.contactmodel.internal.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author weijiantao on 2015/8/31.
 */
public class HistoryInfo implements Serializable {
    private static final long serialVersionUID = 5093094083428819208L;
    public String mDeviceName = "";
    public String mcTime = "";
    public int mContacts = 0;
    public int mGroups = 0;
    public String mVersion = "";
    public String mDeviceId = "";

    public int getVersionInt() {
        int verCount = 0;
        if (!TextUtils.isEmpty(mVersion) && mVersion.contains("_")) {
            verCount = Integer.valueOf(mVersion.substring(0, mVersion.indexOf("_")));
        }
        return verCount;
    }
}
