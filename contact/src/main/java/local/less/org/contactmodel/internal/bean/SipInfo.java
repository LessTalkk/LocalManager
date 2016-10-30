
package local.less.org.contactmodel.internal.bean;


import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class SipInfo extends BaseBean {
    private int type;
    private String address;
    private int isPrimary;
    private String label;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(int isPrimary) {
        this.isPrimary = isPrimary;
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
        builder.append(", address:");
        builder.append(address);
        builder.append(", label:");
        builder.append(label);
        builder.append(", isPrimary:");
        builder.append(isPrimary);
        builder.append("}");
        return builder.toString();
    }

    /*
     * EMAIL;HOME:afaf@ac.com
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(address)) {
            sb.append(VCardConstants.PROPERTY_X_SIP);
            switch (type) {
                case 1:
                    sb.append(";HOME");
                    break;
                case 2:
                    sb.append(";WORK");
                    break;
                case 0:
                    if (!ContactUtil.isEmpty(this.label))
                        sb.append(";X-").append(this.label);
                    break;
            }
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(this.address)) {
                sb.append(":").append(this.address);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(this.address));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(address))
            return 0;

        return address.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SipInfo)) {
            return false;
        }
        SipInfo emailData = (SipInfo) obj;
        return ((address != null ? address.equalsIgnoreCase(emailData.address)
                : (emailData.address == null)));
    }
}
