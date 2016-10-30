
package local.less.org.contactmodel.internal.bean;

/**
 * @author weijiantao on 2015/8/17.
 * 用来关联本地数据和云端数据的对象
 */
public class LocalContactInfo {
    public long contact_id = 0;
    public String remote_id = "0";
    public long local_version = 0;
    public String lookup_key = "";
    public int operate = 0;
    public String mAccount_name = "";
    public String mAccount_type = "";
    public String mFullName = "";
    public String starred = "0";
    public long photo_id = 0;
}
