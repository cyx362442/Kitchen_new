package com.duowei.kitchen_china.adapter;

import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Call;
import com.duowei.kitchen_china.httputils.Post;

import java.util.List;

/**
 * Created by Administrator on 2017-10-21.
 */

public class CallAdapter extends BaseQuickAdapter<Call>{
    public CallAdapter(List<Call> data) {
        super(R.layout.call_item,data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Call call) {
        baseViewHolder.setText(R.id.tv_tableNo,call.getTableNo());
        if("0".equals(call.getYHJ())){
            baseViewHolder.setBackgroundRes(R.id.btn_call,R.drawable.selector_call_green);
        }else{
            baseViewHolder.setBackgroundRes(R.id.btn_call,R.drawable.selector_call_orange);
        }

        baseViewHolder.setOnClickListener(R.id.btn_call, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql="update kdscall set yhj = '1', xdsj = getdate() where xh = '" + call.getXH() + "'|";
                Post.getInstance().postCall7(sql);
            }
        });
        baseViewHolder.setOnClickListener(R.id.btn_complete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql="delete from kdscall where xh='"+call.getXH()+"'|";
                Post.getInstance().postCall7(sql);
            }
        });
    }
}
