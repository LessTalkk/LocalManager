
package local.less.org.contactmodel.internal.bean;


import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class CompanyInfo extends BaseBean {
    private int type;
    private int isPrimary;
    private String title;
    private String name;
    private String label;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(int isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        builder.append("{name:");
        builder.append(name);
        builder.append(", title:");
        builder.append(title);
        builder.append(", type:");
        builder.append(type);
        builder.append(", label:");
        builder.append(label);
        builder.append(", isPrimary:");
        builder.append(isPrimary);
        builder.append("}");
        return builder.toString();
    }

    /*
     * ORG;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=E4=B8=BA=E6=AD=A2
     * TITLE;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=E5=A4=A7=E5=95=8A
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(name)) {
            sb.append(VCardConstants.PROPERTY_ORG);
            switch (type) {
                case 1:
                    sb.append(";WORK");
                    break;
                case 0:
                    if (!ContactUtil.isEmpty(this.label))
                        sb.append(";X-").append(this.label);
                    break;
            }
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(name)) {
                sb.append(":").append(name);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(name));
            }
            sb.append("\r\n");
        }
        if (!ContactUtil.isEmpty(title)) {
            sb.append(VCardConstants.PROPERTY_TITLE);
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(title)) {
                sb.append(":").append(title);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(title));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public String getFormattedString() {
        final StringBuilder builder = new StringBuilder();
        if (!ContactUtil.isEmpty(name)) {
            builder.append(name);
        }

        if (!ContactUtil.isEmpty(title)) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(title);
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(name))
            return 0;

        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CompanyInfo)) {
            return false;
        }
        CompanyInfo organization = (CompanyInfo) obj;
        return ((name != null ? name.equalsIgnoreCase(organization.name)
                : (organization.name == null)) && (title != null ? title
                .equalsIgnoreCase(organization.title)
                : (organization.title == null)));
    }
}
