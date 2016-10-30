
package local.less.org.base.log;

import android.os.Environment;
import android.support.v4.util.ArrayMap;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/9/21 <br/>
 * DESCRIPTION :
 */
public class FileLog {

    public static final String NET_CRASH = "NetCrash";
    public static final String NET_SERVER_ERROR = "NetServerError";
    public static final String RUN_ERROR = "RunError";

    public final static File DIR_ROOT = new File(Environment.getExternalStorageDirectory(), "less");
    public final static File DIR_LOG_PATH = new File(DIR_ROOT, ".log");
    public final static File NET_CRASH_FILE_PATH = new File(DIR_LOG_PATH, "net_crash.txt");
    public final static File NET_SERVER_FILE_PATH = new File(DIR_LOG_PATH, "net_server.txt");
    public final static File RUN_ERROR_PATH = new File(DIR_LOG_PATH, "run_error.txt");

    private static FileLog sFileLog = new FileLog();

    public static FileLog getInstance() {
        return sFileLog;
    }

    private ArrayMap<String, File> mArrayMap;

    private FileLog() {
        mArrayMap = new ArrayMap<String, File>();
        mArrayMap.put(NET_CRASH, NET_CRASH_FILE_PATH);
        mArrayMap.put(NET_SERVER_ERROR, NET_SERVER_FILE_PATH);
        mArrayMap.put(RUN_ERROR, RUN_ERROR_PATH);

        for (Map.Entry<String, File> map : mArrayMap.entrySet()) {
            try {
                initPath(map.getKey(), map.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void info(String tag, String message) {
        if (!mArrayMap.containsKey(tag)) {
            throw new RuntimeException("no support tag type");
        }
        getLogger(tag).info(message);
    }

    private void initPath(String tag, File mFilePath) throws IOException {
        Logger logger = Logger.getLogger(tag);
        if (!DIR_LOG_PATH.exists()) {
            boolean pathB = DIR_LOG_PATH.mkdirs();
            LogUtil.d("pathB:" + pathB);
        }
        if (!mFilePath.exists()) {
            try {
                boolean fileB = mFilePath.createNewFile();
                LogUtil.d("fileB:" + fileB);
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e(e.getMessage());
            }
        }
        logger.addHandler(new AsyncFileHandler(mFilePath.getAbsolutePath(), 0, 1, true));
    }

}
