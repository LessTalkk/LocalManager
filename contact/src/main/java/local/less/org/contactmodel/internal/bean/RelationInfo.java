
package local.less.org.contactmodel.internal.bean;


import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

/**
 * @author weijiantao on 2015/11/24.
 */
public class RelationInfo extends BaseBean {

    private String value;
    private String label;
    private int type;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{label:");
        builder.append(label);
        builder.append(", name:");
        builder.append(value);
        builder.append(", type:");
        builder.append(type);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(value)) {
            sb.append(VCardConstants.PROPERTY_X_RELATION);
            switch (type) {
                case 1:
                    sb.append(";ASSISTANT");
                    break;
                case 2:
                    sb.append(";BROTHER");
                    break;
                case 3:
                    sb.append(";CHILD");
                    break;
                case 4:
                    sb.append(";DOMESTIC_PARTNER");
                    break;
                case 5:
                    sb.append(";FATHER");
                    break;
                case 6:
                    sb.append(";FRIEND");
                    break;
                case 7:
                    sb.append(";MANAGER");
                    break;
                case 8:
                    sb.append(";MOTHER");
                    break;
                case 9:
                    sb.append(";PARENT");
                    break;
                case 10:
                    sb.append(";PARTNER");
                    break;
                case 11:
                    sb.append(";REFERRED_BY");
                    break;
                case 12:
                    sb.append(";RELATIVE");
                    break;
                case 13:
                    sb.append(";SISTER");
                    break;
                case 14:
                    sb.append(";SPOUSE");
                    break;
                case 0:
                    if (!ContactUtil.isEmpty(this.label))
                        sb.append(";X-").append(this.label);
                    break;
            }
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(this.value)) {
                sb.append(":").append(this.value);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(this.value));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public void setName(String name) {
        this.value = name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public int getType() {
        return type;
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(value))
            return 0;

        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RelationInfo)) {
            return false;
        }
        RelationInfo relateData = (RelationInfo) obj;
        String name1 = getName();
        String name2 = relateData.getName();
        return ((name1 != null ? name1.equals(name2) : (name2 == null)));
    }
}
