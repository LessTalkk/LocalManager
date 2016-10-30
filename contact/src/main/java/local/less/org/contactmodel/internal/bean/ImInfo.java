
package local.less.org.contactmodel.internal.bean;

import android.text.TextUtils;

import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class ImInfo extends BaseBean {
    private String data;
    private String protocol;
    private String customProtocol;
    private String label;
    private int type;
    private int isPrimary;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getProtocol() {
        if (!TextUtils.isEmpty(protocol)) {
            int n = protocol.indexOf(":");
            if (n >= 0) {
                protocol = protocol.substring(n + 1);
            }
        }
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCustomProtocol() {
        return customProtocol;
    }

    public void setCustomProtocol(String customProtocol) {
        this.customProtocol = customProtocol;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{customProtocol:");
        builder.append(customProtocol);
        builder.append(", data:");
        builder.append(data);
        builder.append(", label:");
        builder.append(label);
        builder.append(", protocol:");
        builder.append(protocol);
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
     * X-IM;MSN:afaf@ac.com
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VCardConstants.PROPERTY_X_SNS);
        switch (type) {
            case 1:
                sb.append(";HOME");
                break;
            case 2:
                sb.append(";WORK");
                break;
            case 0:
                if (!TextUtils.isEmpty(this.label))
                    sb.append(";X-").append(this.label);
                break;
        }
        if (!TextUtils.isEmpty(protocol)) {
            int n = protocol.indexOf(":");
            if (n >= 0) {
                protocol = protocol.substring(n + 1);
            }
            if ("0".equals(protocol)) {
                sb.append(";AIM");
            } else if ("1".equals(protocol)) {
                sb.append(";MSN");
            } else if ("2".equals(protocol)) {
                sb.append(";YAHOO");
            } else if ("3".equals(protocol)) {
                sb.append(";SKYPE");
            } else if ("4".equals(protocol)) {
                sb.append(";QQ");
            } else if ("5".equals(protocol)) {
                sb.append(";GOOGLE_TALK");
            } else if ("6".equals(protocol)) {
                sb.append(";ICQ");
            } else if ("7".equals(protocol)) {
                sb.append(";JABBER");
            } else if ("8".equals(protocol)) {
                sb.append(";NETMEETING");
            } else {
                if (!TextUtils.isEmpty(this.customProtocol))
                    sb.append(";").append(this.customProtocol);
            }
        }
        if (ContactUtil.containsOnlyNonCrLfPrintableAscii(this.data)) {
            sb.append(":").append(this.data);
        } else {
            sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
            sb.append(ContactUtil.qpEncoding(this.data));
        }
        sb.append("\r\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(data))
            return 0;

        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImInfo)) {
            return false;
        }
        ImInfo imData = (ImInfo) obj;
        String pro = getProtocol();
        return ((pro != null ? pro.equalsIgnoreCase(imData.getProtocol())
                : (imData.getProtocol() == null))
                && (customProtocol != null ? customProtocol
                        .equalsIgnoreCase(imData.customProtocol)
                        : (imData.customProtocol == null)) && (data != null ? data
                .equalsIgnoreCase(imData.data) : (imData.data == null)));
    }
}
