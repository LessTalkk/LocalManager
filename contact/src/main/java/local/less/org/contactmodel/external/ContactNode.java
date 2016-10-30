
package local.less.org.contactmodel.external;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedHashSet;

import local.less.org.contactmodel.internal.ContactManager;
import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.bean.ContactInfo;
import local.less.org.contactmodel.internal.bean.GroupInfo;

public class ContactNode {

    /**
     * 联系人详细信息
     */
    private ContactInfo mContactInfo;

    /**
     * 联系人本地媒体库ID
     */
    public long contact_id = 0;

    /**
     * 需要执行的操作
     * 
     * @see #OPERATE_NONE
     * @see #OPERATE_ADD
     * @see #OPERATE_UPDATE
     * @see #OPERATE_DELETE
     */
    public int operate = OPERATE_NONE;

    public static final int OPERATE_NONE = ContactManager.LocalContactOperate.NONE;
    public static final int OPERATE_ADD = ContactManager.LocalContactOperate.ADD;
    public static final int OPERATE_UPDATE = ContactManager.LocalContactOperate.UPDATE;
    public static final int OPERATE_DELETE = ContactManager.LocalContactOperate.DELETE;

    /**
     * 联系人云端唯一标识
     */
    public String remote_id = "0";

    /**
     * 联系人姓名首字母
     */
    public String mFirstAlpha = "#";

    public ContactNode() {
        mContactInfo = new ContactInfo();
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toString()).append(", ")
                .append("contact_id=").append(contact_id).append(", ")
                .append("operate=").append(operate).append(", ")
                .append("remote_id=").append(remote_id);
        if (mContactInfo != null) {
            buf.append(", ").append(mContactInfo.toString());
        }
        return buf.toString();
    }

    /**
     * 获取联系人详细信息
     * 
     * @return 联系人详细信息
     */
    public ContactInfo getContactInfo() {
        return mContactInfo;
    }

    /**
     * 设置联系人详细信息
     * 
     * @param info 联系人详细信息
     */
    public void setContactInfo(ContactInfo info) {
        this.mContactInfo = info;
    }


    //把Rid转化为群组名
    public void initRemoteGroup(HashMap<String, String> allGroups) {
        LinkedHashSet<GroupInfo> resultGroup = new LinkedHashSet<GroupInfo>();
        if (!ContactUtil.isEmpty(mContactInfo.getGroups())) {
            for (GroupInfo info : mContactInfo.getGroups()) {
                GroupInfo temp = new GroupInfo();
                String remoteName = info.getRemoteName();
                String realName = "";
                if (allGroups.containsKey(remoteName)) {
                    realName = allGroups.get(info.getRemoteName());
                }
                if (!TextUtils.isEmpty(realName)) {
                    temp.setName(realName);
                }
                temp.setRemoteName(info.getRemoteName());
                resultGroup.add(temp);
            }
            mContactInfo.getGroups().clear();
            mContactInfo.setGroups(resultGroup);
        }
    }

}
