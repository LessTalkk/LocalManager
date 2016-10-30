
package local.less.org.contactmodel.internal.vcard;

import android.text.TextUtils;

import java.io.Serializable;

import local.less.org.contactmodel.internal.ContactUtil;

public class BackupVo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5012667816263745557L;
    public static final int DATA_TYPE_SYS_CONTACTS = 1;
    public static final int DATA_TYPE_SYS_SMS = 2;
    public static final int DATA_TYPE_SYS_CALLLOG = 9;

    public int type;
    public byte[] data;
    public int count;
    public String filePath;
    public Object mData;
    //数据大小
    public long mLen;

    public BackupVo() {
    }

    public BackupVo(int type, Object obj, int count, long len) {
        this(type, obj, count);
        this.mLen = len;
    }

    public BackupVo(int type, Object obj, int count) {
        if (obj == null) {
            return;
        }
        if (obj instanceof String) {
            filePath = (String) obj;
            if (TextUtils.isEmpty(filePath)) {
                return;
            }
            this.mLen = ContactUtil.getFileSize(filePath);
        } else if (obj instanceof byte[]) {
            byte[] bytes = (byte[]) obj;
            if (bytes.length == 0) {
                return;
            }
            int len = bytes.length;
            this.mLen = len;
            this.data = new byte[len];
            System.arraycopy(bytes, 0, this.data, 0, len);
        } else {
            mData = obj;
        }
        this.type = type;
        this.count = count;
    }
}
