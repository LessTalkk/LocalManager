package local.less.org.contactmodel.internal.bean;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class EventInfo extends BaseBean {
    private String startDate;
    private String label;
    private int type;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        String temp;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            temp = format.format(format.parse(startDate));
        } catch (Exception e) {
            temp = startDate;
        }
        this.startDate = temp;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{label:");
        builder.append(label);
        builder.append(", startDate:");
        builder.append(startDate);
        builder.append(", type:");
        builder.append(type);
        builder.append("}");
        return builder.toString();
    }

    /*
     * EMAIL;HOME:afaf@ac.com
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(startDate)) {
            sb.append(VCardConstants.PROPERTY_X_EVENT);
            switch (type) {
            case 1:
                sb.append(";ANNIVERSARY");
                break;
            case 3:
                sb.append(";BIRTHDAY");
                break;
            case 0:
                if (!ContactUtil.isEmpty(this.label))
                    sb.append(";X-").append(this.label);
                break;
            }
            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(this.startDate)) {
                sb.append(":").append(this.startDate);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(this.startDate));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(startDate))
            return 0;

        return startDate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EventInfo)) {
            return false;
        }
        EventInfo event = (EventInfo) obj;
        return ((type == event.type)
                && (label != null ? label.equalsIgnoreCase(event.label)
                        : (event.label == null)) && (startDate != null ? startDate
                .equalsIgnoreCase(event.startDate) : (event.startDate == null)));
    }
}
