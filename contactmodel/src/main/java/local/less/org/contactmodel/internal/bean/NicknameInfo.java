
package local.less.org.contactmodel.internal.bean;


import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class NicknameInfo extends BaseBean {
    private String name;
    private int type;
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{type:");
        builder.append(type);
        builder.append(", name:");
        builder.append(name);
        builder.append(", label:");
        builder.append(label);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(name)) {
            sb.append(VCardConstants.PROPERTY_NICKNAME);
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(name)) {
                sb.append(":").append(name);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(name));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(name))
            return 0;

        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NicknameInfo)) {
            return false;
        }
        NicknameInfo nickname = (NicknameInfo) obj;
        return ((name != null ? name.equalsIgnoreCase(nickname.name)
                : (nickname.name == null)));
    }
}
