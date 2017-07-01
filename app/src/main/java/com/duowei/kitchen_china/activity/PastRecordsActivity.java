package com.duowei.kitchen_china.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.HistoryAdapter;
import com.duowei.kitchen_china.bean.Cfpb;

import org.litepal.crud.DataSupport;

import java.util.List;

public class PastRecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_records);
        List<Cfpb> cfpbList = DataSupport.order("xdsj desc").find(Cfpb.class);

        ListView lv = (ListView) findViewById(R.id.listView);
        HistoryAdapter adapter = new HistoryAdapter(this, cfpbList);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_exit){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
