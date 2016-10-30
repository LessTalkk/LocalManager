package local.less.org.contactmodel.internal.bean;

/**
 * ===============================================
 * DEVELOPER : RenYang <br/>
 * DATE : 2016/10/30 <br/>
 * DESCRIPTION :
 */
public abstract class BaseBean {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    abstract public String toVCardString();

}
