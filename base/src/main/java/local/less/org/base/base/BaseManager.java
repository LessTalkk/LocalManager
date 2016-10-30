package local.less.org.base.base;

import android.content.Context;

import java.util.ArrayList;

import local.less.org.base.task.Completion;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/10/30 <br/>
 * DESCRIPTION :
 */
public abstract class BaseManager<T> {

    public static final String EMPTY = "";

    public abstract BaseManager getCount(Context context,Completion<Integer> completion);

    public abstract BaseManager init(Context context);

    public abstract  BaseManager getList(final Context context,Completion<ArrayList<T>> completion);
}
