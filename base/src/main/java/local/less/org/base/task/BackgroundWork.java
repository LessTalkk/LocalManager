package local.less.org.base.task;

public interface BackgroundWork<T> {
    T doInBackground() throws Exception;
}
