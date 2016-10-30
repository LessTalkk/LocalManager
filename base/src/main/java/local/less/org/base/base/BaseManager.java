package local.less.org.base.base;

import android.content.Context;

import local.less.org.base.task.Completion;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/10/30 <br/>
 * DESCRIPTION :
 */
public abstract class BaseManager {

    public static final String EMPTY = "";

    public abstract BaseManager getCount(Context context,Completion<Integer> completion);

    public abstract BaseManager init(Context context);

}
