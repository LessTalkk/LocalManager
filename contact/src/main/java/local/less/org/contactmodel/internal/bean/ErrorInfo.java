package local.less.org.contactmodel.internal.bean;

import android.util.SparseArray;

public class ErrorInfo {
    // 错误列表，code->信息
    public SparseArray<String> mErrors;

    public ErrorInfo() {
        mErrors = new SparseArray<String>();
    }

    public void addError(int code, String msg) {
        if (code == 0) {
            return;
        }
        mErrors.put(code, msg);
    }

    public void addErrorList(ErrorInfo ei) {
        if (ei != null) {
            SparseArray<String> errors = ei.mErrors;
            for (int i = 0; i < errors.size(); i++) {
                mErrors.put(errors.keyAt(i), errors.valueAt(i));
            }
        }
    }

    public int getOneErrorCode() {
        if (mErrors.size() > 0) {
            return mErrors.keyAt(0);
        }
        return Integer.MIN_VALUE;
    }

    public String getOneErrorMsg() {
        if (mErrors.size() > 0) {
            return mErrors.valueAt(0);
        }
        return null;
    }

    public String getAllErrorMsg() {
        StringBuffer sbError = new StringBuffer();
        for (int i = 0; i < mErrors.size(); i++) {
            sbError.append(mErrors.valueAt(i));
            if (i != mErrors.size()) {
                sbError.append("\n");
            }
        }
        return sbError.toString();
    }

}
