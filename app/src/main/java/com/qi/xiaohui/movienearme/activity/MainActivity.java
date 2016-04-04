package com.qi.xiaohui.movienearme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qi.xiaohui.movienearme.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private StaggeredGridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(mLayoutManager);

    }
}
