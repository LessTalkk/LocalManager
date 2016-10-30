
package local.less.org.contactmodel.internal.bean;


import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class PhoneInfo extends BaseBean {
    private String number;
    private int type;
    private int isPrimary;
    private String label;

    public String getNumber() {
        return number;
    }

    public void setNumber(String num) {
        //        if (num != null)
        //            num = num.replaceAll("-", "");
        this.number = ContactUtil.getCleanPhone(num);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        if (type == 0) {
            this.type = 2;
        } else {
            this.type = type;
        }
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
        builder.append("{type=");
        builder.append(type);
        builder.append(", number=");
        builder.append(number);
        builder.append(", label:");
        builder.append(label);
        builder.append(", isPrimary:");
        builder.append(isPrimary);
        builder.append("}");
        return builder.toString();
    }

    /*
     * TEL;HOME;VOICE:234-324-32432
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(getNumber())) {
            sb.append(VCardConstants.PROPERTY_TEL);
            switch (type) {
                case 1:
                    sb.append(";HOME");
                    break;
                case 2:
                    sb.append(";CELL");
                    break;
                case 3:
                    sb.append(";WORK");
                    break;
                case 4:
                    sb.append(";FAX;WORK");
                    break;
                case 5:
                    sb.append(";FAX;HOME");
                    break;
                case 6:
                    sb.append(";PAGER");
                    break;
                case 7:
                    break;
                case 8:
                    sb.append(";X-CALLBACK");
                    break;
                case 9:
                    sb.append(";CAR");
                    break;
                case 10:
                    sb.append(";WORK");
                    break;
                case 11:
                    sb.append(";ISDN");
                    break;
                case 12:
                    sb.append(";PREF");
                    break;
                case 13:
                    sb.append(";FAX");
                    break;
                case 14:
                    sb.append(";X-RADIO");
                    break;
                case 15:
                    sb.append(";X-TELEX");
                    break;
                case 16:
                    sb.append(";X-TTY");
                    break;
                case 17:
                    sb.append(";CELL;WORK");
                    break;
                case 18:
                    sb.append(";PAGER;WORK");
                    break;
                case 19:
                    sb.append(";X-ASSISTANT");
                    break;
                case 20:
                    sb.append(";VIDEO");
                    break;
            }
            sb.append(":").append(getNumber());
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        String number1 = getNumber();
        if (ContactUtil.isEmpty(number1))
            return 0;

        return number1.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PhoneInfo)) {
            return false;
        }
        PhoneInfo phoneData = (PhoneInfo) obj;
        String number1 = getNumber();
        String number2 = phoneData.getNumber();
        return ((number1 != null ? number1.equalsIgnoreCase(number2)
                : (number2 == null)));
    }
}
