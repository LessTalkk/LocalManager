package org.less.local;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        ContactManager.getInstance().init(this).getCount(new Completion<Integer>() {
//            @Override
//            public void onSuccess(Context context, Integer result) {
//                Toast.makeText(context,"xxxxxxxxxx:"+result,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(Context context, Exception e) {
//
//            }
//        });
    }
}
