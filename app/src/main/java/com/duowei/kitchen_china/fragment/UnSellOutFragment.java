package com.duowei.kitchen_china.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.SellOutAdapter;
import com.duowei.kitchen_china.bean.Jyxmsz;
import com.duowei.kitchen_china.event.SaleOut;
import com.duowei.kitchen_china.event.StartAnim;
import com.duowei.kitchen_china.event.StopAnim;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.VolleyResultListener;
import com.duowei.kitchen_china.uitls.KeyboardUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnSellOutFragment extends Fragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {

    private EditText mEtInput;
    private List<Jyxmsz> listJyxmsz;
    private SellOutAdapter mAdapter;
    private Jyxmsz mJyxmsz;

    public UnSellOutFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_un_sell_out, container, false);
        listJyxmsz=new ArrayList<>();
        mEtInput = (EditText) inflate.findViewById(R.id.et_input);
        mEtInput.setOnClickListener(this);
        mEtInput.setInputType(InputType.TYPE_NULL);
        mEtInput.addTextChangedListener(this);

        inflate.findViewById(R.id.ll_guqing).setOnClickListener(this);
        inflate.findViewById(R.id.btn_close).setOnClickListener(this);

        ListView lv = (ListView) inflate.findViewById(R.id.listView);
        mAdapter = new SellOutAdapter(getActivity(), listJyxmsz);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_input:
                KeyboardUtil.instance().initKeyboard(getActivity(),getActivity(),mEtInput);
                KeyboardUtil.instance().showKeyboard();
                break;
            case R.id.ll_guqing:
                if(listJyxmsz.size()<=0){
                    return;
                }
                EventBus.getDefault().post(new StartAnim());
                String sql="update jyxmsz set gq='1' where xmbh='"+mJyxmsz.getXmbh()+"'|";
                DownHTTP.postVolley7(Net.url, sql, new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        EventBus.getDefault().post(new StopAnim());
                    }
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("richado")){
                            EventBus.getDefault().post(new SaleOut(mJyxmsz));
                            mEtInput.setText("");
                        }
                    }
                });
                break;
            case R.id.btn_close:
                getActivity().finish();
                break;
        }
    }
   /* 搜索框变化监听事件*/
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String s = charSequence.toString();
        if(s.matches("[0-9]+")){//按编码查询
            listJyxmsz=DataSupport.where("xmbh like?",s + "%").find(Jyxmsz.class);
        }else{//助记符查询
            listJyxmsz = DataSupport.where("py like?", "%" + s + "%").find(Jyxmsz.class);
        }
        mAdapter.setList(listJyxmsz);
        mAdapter.notifyDataSetChanged();
        if(listJyxmsz.size()>0){
            mJyxmsz=listJyxmsz.get(0);
        }
    }
    @Override
    public void afterTextChanged(Editable editable) {
    }
  /*listview监听事件*/
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mJyxmsz = listJyxmsz.get(i);
        mAdapter.setIndex(i);
        mAdapter.notifyDataSetChanged();
    }
}
