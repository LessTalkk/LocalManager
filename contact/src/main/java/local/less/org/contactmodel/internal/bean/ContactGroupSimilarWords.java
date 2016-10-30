package local.less.org.contactmodel.internal.bean;

import java.util.ArrayList;

/**
 * 处理“家人”，“家庭”，“朋友”， “好友”，coworkers, co-worker
 * @author zjmting@163.com
 *
 */
public class ContactGroupSimilarWords {
    public ArrayList<SimilarWordGroup> mSimilars = new ArrayList<SimilarWordGroup>();

    private void init() {
        // coworkers co-worker colleagues 同事
        SimilarWordGroup group1 = new SimilarWordGroup();
        group1.mSimilarGroup.add(new SimilarWordItem("coworkers"));
        group1.mSimilarGroup.add(new SimilarWordItem("co-worker"));
        group1.mSimilarGroup.add(new SimilarWordItem("colleagues"));
        group1.mSimilarGroup.add(new SimilarWordItem("同事"));
        group1.mSimilarGroup.add(new SimilarWordItem("工作"));

        mSimilars.add(group1);

        // 家人，家庭
        SimilarWordGroup group2 = new SimilarWordGroup();
        group2.mSimilarGroup.add(new SimilarWordItem("家庭"));
        group2.mSimilarGroup.add(new SimilarWordItem("家人"));
        group2.mSimilarGroup.add(new SimilarWordItem("family"));
        mSimilars.add(group2);

        // 朋友，好友
        SimilarWordGroup group3 = new SimilarWordGroup();
        group3.mSimilarGroup.add(new SimilarWordItem("朋友"));
        group3.mSimilarGroup.add(new SimilarWordItem("好友"));
        group3.mSimilarGroup.add(new SimilarWordItem("friends"));
        mSimilars.add(group3);

        // schoolmate 同学
        SimilarWordGroup group4 = new SimilarWordGroup();
        group4.mSimilarGroup.add(new SimilarWordItem("同学"));
        group4.mSimilarGroup.add(new SimilarWordItem("schoolmate"));
        mSimilars.add(group4);
    }

    public ContactGroupSimilarWords() {
        init();
    }

    public static class SimilarWordGroup {
        public ArrayList<SimilarWordItem> mSimilarGroup = new ArrayList<SimilarWordItem>();
    }
    public static class SimilarWordItem {
        public SimilarWordItem(String word) {
            mWord = word;
        }
        public String mWord;
    }
}
