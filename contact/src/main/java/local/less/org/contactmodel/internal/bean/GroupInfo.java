
package local.less.org.contactmodel.internal.bean;

import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class GroupInfo extends BaseBean {
    private String name;
    private String remote_name;

    public String getRemoteName() {
        return remote_name;
    }

    public void setRemoteName(String rid) {
        remote_name = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalName() {
        if (name != null) {
            return name;
        }
        return remote_name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name:");
        builder.append(getTotalName());
        return builder.toString();
    }

    /*
     * GROUP;aaa
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VCardConstants.PROPERTY_X_GROUP);
        if (ContactUtil.containsOnlyNonCrLfPrintableAscii(this.name)) {
            sb.append(":").append(this.name);
        } else {
            sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
            sb.append(ContactUtil.qpEncoding(this.name));
        }
        sb.append("\r\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(getTotalName())) {
            return 0;
        }
        return (getTotalName()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupInfo)) {
            return false;
        }
        GroupInfo groupData = (GroupInfo) obj;

        String name1 = getTotalName();
        String name2 = groupData.getTotalName();

        return ((name1 != null ? name1.equals(name2) : (name2 == null)));
    }
}
