
package local.less.org.contactmodel.internal.vcard;

import java.util.ArrayList;

public class ContactNodeCreate {

    private static ContactNodeCreate instance;
    private int MAX_CACHE_SIZ = 5; //缓冲池的大小,如果池中有大于这个数量的不在放入
    final private ArrayList<VCardParser_V21> actionPools = new ArrayList<>();
    final private ArrayList<VNodeBuilder> interpreterPools = new ArrayList<>();

    private ContactNodeCreate() {

    }

    public static ContactNodeCreate getInstance() {
        if (instance == null) {
            synchronized (ContactNodeCreate.class) {
                instance = new ContactNodeCreate();
            }
        }
        return instance;
    }
    
    private VCardParser_V21 getVCardAction() {
        synchronized (actionPools) {
            if (actionPools.isEmpty()) {
                return new VCardParser_V21();
            } else {
                return actionPools.remove(0);
            }
        }
    }

    private void addVCardAction(VCardParser_V21 action) {
        synchronized (actionPools) {
            if (actionPools.size() < MAX_CACHE_SIZ) {
                actionPools.add(action);
            }
        }
    }

    private void addInterpreter(VNodeBuilder builder) {
        synchronized (interpreterPools) {
            if (interpreterPools.size() < MAX_CACHE_SIZ) {
                interpreterPools.add(builder);
            }
        }
    }

    public VNodeBuilder getVCardInterpreter() {
        synchronized (interpreterPools) {
            if (interpreterPools.isEmpty()) {
                return new VNodeBuilder(VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8);
            } else {
                return interpreterPools.remove(0);
            }
        }
    }

}
