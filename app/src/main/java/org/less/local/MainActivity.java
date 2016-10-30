package org.less.local;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import local.less.org.base.task.Completion;
import local.less.org.contactmodel.external.ContactNode;
import local.less.org.contactmodel.internal.ContactManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ContactManager.getInstance().init(this).getList(this, new Completion<ArrayList<ContactNode>>() {
            @Override
            public void onSuccess(Context context, ArrayList<ContactNode> result) {
                //Toast.makeText(context,"zzzz"+result.size(),Toast.LENGTH_LONG).show();
                ((TextView)findViewById(R.id.fuck)).setText(result.toString());
            }

            @Override
            public void onError(Context context, Exception e) {

            }
        });

    }
}
