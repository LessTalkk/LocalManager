package local.less.org.contactmodel.internal.bean;

import android.text.TextUtils;

import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;


public class NoteInfo extends BaseBean {
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{note:\"");
        builder.append(note);
        builder.append("\"}");
        return builder.toString();
    }

    /*
     * NOTE;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=E7=94=9F=E6=97=A5=EF=BC=9A=20
     * =32=30=30=30=2D=31=2D=31
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(note)) {
            sb.append(VCardConstants.PROPERTY_NOTE);

            if (ContactUtil.containsOnlyNonCrLfPrintableAscii(this.note)) {
                sb.append(":").append(this.note);
            } else {
                sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
                sb.append(ContactUtil.qpEncoding(this.note));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(note))
            return 0;

        return note.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NoteInfo)) {
            return false;
        }
        NoteInfo info = (NoteInfo) obj;
        return (TextUtils.equals(note, info.note));
    }
}
