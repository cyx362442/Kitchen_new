package com.duowei.kitchen_china.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.HistoryAdapter;
import com.duowei.kitchen_china.bean.Cfpb;

import org.litepal.crud.DataSupport;

import java.util.List;

public class PastRecordsActivity extends AppCompatActivity {

    private List<Cfpb> mCfpbList;
    private HistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_records);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        mCfpbList = DataSupport.order("wcsj desc").find(Cfpb.class);

        ListView lv = (ListView) findViewById(R.id.listView);
        mAdapter = new HistoryAdapter(this, mCfpbList);
        lv.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        MenuItem item = menu.findItem(R.id.menu_clear);
        item.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_exit){
            finish();
        }else if(item.getItemId()==R.id.menu_clear){
            DataSupport.deleteAll(Cfpb.class);
            mCfpbList = DataSupport.order("wcsj desc").find(Cfpb.class);
            mAdapter.setList(mCfpbList);
            mAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
