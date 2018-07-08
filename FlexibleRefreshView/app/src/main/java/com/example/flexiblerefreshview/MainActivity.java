package com.example.flexiblerefreshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.libflexiblerefreshview.FlexibleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView lv;
    private RefreshAdapter refreshAdapter;
    private FlexibleRefreshLayout prl;
    private List<String> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prl = (FlexibleRefreshLayout) findViewById(R.id.prl);
        lv = (ListView) findViewById(R.id.lv);
        lists = new ArrayList<>();
        for (int i = 0; i<16; i++){
            lists.add("欧邦飞" + (i+1));
        }

        refreshAdapter = new RefreshAdapter(lists, this);
        lv.setAdapter(refreshAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "item" + (i + 1), Toast.LENGTH_SHORT).show();

            }
        });
        prl.setOnRefreashingListener(new FlexibleRefreshLayout.OnRefreashingListener() {
            @Override
            public void refreashing() {
                Log.i(TAG, "refreashing: ");
                lists.add("欧邦飞" + (lists.size()+1));
                refreshAdapter.setStrs(lists);
                prl.stopRefreash();
            }
        });
    }
}
