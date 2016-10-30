package local.less.org.base.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class LessTask {

    private static final BlockingQueue<Runnable> sPoolWorkQueueLight =
            new LinkedBlockingQueue<Runnable>(20);

    private static final Executor THREAD_POOL_EXECUTOR_LIGHT
            = new ThreadPoolExecutor(1, 10, 1,
            TimeUnit.SECONDS, sPoolWorkQueueLight);



    private LessTask() {
        throw new UnsupportedOperationException();
    }

    public static <T> void executeHeavyInBackground(Context context, BackgroundWork<T> backgroundWork, Completion<T> completion) {
        execute(context,backgroundWork,completion,AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static <T> void executeLightInBackground(Context context, BackgroundWork<T> backgroundWork, Completion<T> completion) {
        execute(context,backgroundWork,completion,THREAD_POOL_EXECUTOR_LIGHT);
    }

    private static <T> void execute(Context context, BackgroundWork<T> backgroundWork, Completion<T> completion,Executor executor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            executeInBackground(context, backgroundWork, completion,THREAD_POOL_EXECUTOR_LIGHT);
        } else {
            new Task<T>(context, backgroundWork, completion).execute();
        }
    }

    private static <T> void executeInBackground(Context context, BackgroundWork<T> backgroundWork, Completion<T> completion, Executor executor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new Task<T>(context, backgroundWork, completion).executeOnExecutor(executor);
        } else {
            throw new RuntimeException("you cannot use a custom executor on pre honeycomb devices");
        }
    }

}
