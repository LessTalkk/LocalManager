package local.less.org.base.task;

import android.content.Context;

public interface Completion<T> {
    void onSuccess(Context context, T result);
    void onError(Context context, Exception e);
}
