package local.less.org.contactmodel.internal;

import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import local.less.org.base.util.QuotedPrintableCodec;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/10/30 <br/>
 * DESCRIPTION :
 */
public class ContactUtil {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static boolean containsOnlyNonCrLfPrintableAscii(final String... values) {
        if (values == null) {
            return true;
        }
        return containsOnlyNonCrLfPrintableAscii(Arrays.asList(values));
    }

    private static boolean containsOnlyNonCrLfPrintableAscii(final Collection<String> values) {
        if (values == null) {
            return true;
        }
        final int asciiFirst = 0x20;
        final int asciiLast = 0x7E; // included
        for (final String value : values) {
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            final int length = value.length();
            for (int i = 0; i < length; i = value.offsetByCodePoints(i, 1)) {
                final int c = value.codePointAt(i);
                if (!(asciiFirst <= c && c <= asciiLast)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(List<?> list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Set<?> set) {
        if (set != null && set.size() > 0) {
            return false;
        }
        return true;
    }

    public static String qpEncoding(String str) {
        QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();
        String result;
        try {
            result = quotedPrintableCodec.encode(str);
        } catch (Exception e) {
            result = qpEncodingV2(str);
        }
        return result;
    }

    private static String qpEncodingV2(String str) {
        char[] encode = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < encode.length; i++) {
            if ((encode[i] >= '!') && (encode[i] <= '~')) {
                if (encode[i] < 127 && encode[i] > -128) {
                    sb.append('='); // 分两次执行append， 可以避免"=" +
                    // hexString过程中隐式构造的StringBuilder(减少18%)
                    sb.append(byteToHexMap[encode[i] + 128]);
                } else {
                    String s16 = Integer.toHexString(encode[i]);
                    char c16_0 = s16.charAt(0);
                    char c16_1 = s16.charAt(1);
                    if (c16_0 >= 97 && c16_0 <= 122) {
                        c16_0 -= 32;
                    }
                    if (c16_1 >= 97 && c16_1 <= 122) {
                        c16_1 -= 32;
                    }
                    sb.append('=');
                    sb.append(c16_0);
                    sb.append(c16_1);
                }
            } else if (encode[i] == ' ') {
                sb.append('=');
                sb.append("20");
            } else if (encode[i] == '\t') {
                sb.append('=');
                sb.append("09");
            } else if (encode[i] == '\n') {
                sb.append('=');
                sb.append("0A");
            } else if (encode[i] == '\r') {
                sb.append('=');
                sb.append("0D");
            } else {
                byte[] buf = null;
                try {
                    buf = str.substring(i, i + 1).getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (buf != null && buf.length == 3) {
                    for (int j = 0; j < 3; j++) {
                        sb.append('=');
                        sb.append(byteToHexMap[buf[j] + 128]);
                    }
                }
            }
        }
        return sb.toString();
    }

    private static String[] byteToHexMap = new String[256];
    static {
        StringBuffer sb = new StringBuffer();
        for (int i = -128; i < 127; ++i) {
            sb.delete(0, sb.length());
            sb.append(Integer.toHexString(i & 0xff));
            byteToHexMap[i + 128] = sb.toString().toUpperCase();
        }
    }

    public static String getCleanPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            return phone.replaceAll("[\\s-]", "");
        }
        return phone;
    }

    public static long getFileSize(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        return new File(filePath).length();
    }

    public static boolean isChineseName(String middleName) {
        for (int i = 0; i < middleName.length(); ++i) {
            final char c = middleName.charAt(i);
            if (c < '\u4e00' || c > '\u9fa5') {
                return false;
            }
        }
        return true;
    }
}
