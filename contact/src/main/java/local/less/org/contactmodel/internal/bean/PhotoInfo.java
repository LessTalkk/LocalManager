
package local.less.org.contactmodel.internal.bean;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.Arrays;

import local.less.org.base.util.Util;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class PhotoInfo extends BaseBean {
    private byte[] binaryData;
    private int isPrimary;
    private String remotePath;

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public void setRemotePath(String path) {
        remotePath = path;
    }

    public int getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(int isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VCardConstants.PROPERTY_PHOTO);
        sb.append(remotePath);
        sb.append("\r\n\r\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (binaryData != null) {
            return binaryData.length;
        }
        if (!TextUtils.isEmpty(remotePath)) {
            return remotePath.hashCode();
        }
        return (int) getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PhotoInfo)) {
            return false;
        }
        PhotoInfo photoData = (PhotoInfo) obj;
        return (Arrays.equals(binaryData, photoData.binaryData));
    }

    public Bitmap getBitmap(Context context) {
        if (getId() == 0) {
            return null;
        }
        Bitmap mUserIcon = null;
        byte[] photo;
        Cursor photoCursor = null;
        try {
            photoCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] {
                    ContactsContract.CommonDataKinds.Photo.PHOTO
            }, ContactsContract.Contacts.Data._ID + "=" + getId(), null, null);
            if (photoCursor != null && photoCursor.moveToFirst()) {
                photo = photoCursor.getBlob(0);
                if (photo != null) {
                    mUserIcon = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            Util.close(photoCursor);
        }
        return mUserIcon;
    }
}
