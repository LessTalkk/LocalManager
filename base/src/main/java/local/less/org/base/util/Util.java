package local.less.org.base.util;

import android.database.Cursor;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/10/30 <br/>
 * DESCRIPTION :
 */
public class Util {

    public final static void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public static boolean moveCursorNext(Cursor cursor) {
        int curPos = cursor.getPosition();
        try {
            return cursor.moveToNext();
        } catch (Exception e) {
            int tryCount = 10;
            while (cursor.getPosition() - curPos < 2) {
                try {
                    cursor.moveToNext();
                } catch (Exception e1) {
                }
                if (--tryCount == 0) {
                    break;
                }
            }
            try {
                return cursor.moveToPrevious();
            } catch (Exception e1) {
            }
        }
        return false;
    }

    public static boolean isMIUI() {
        String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (properties.getProperty(KEY_MIUI_VERSION_CODE) != null || properties.getProperty(KEY_MIUI_VERSION_NAME) != null
                    || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE) != null) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public static boolean isSamsung() {
        return Build.BRAND.equalsIgnoreCase("samsung");
    }




}
