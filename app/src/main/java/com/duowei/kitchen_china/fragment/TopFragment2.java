package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.event.InputMsg;
import com.duowei.kitchen_china.event.SearchFood;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.uitls.KeyboardUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment2 extends Fragment implements View.OnClickListener, TextWatcher {

    public EditText mEt;
    private KeyboardUtil mKeyboardUtil;

    public TopFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_top_fragment2, container, false);
        mKeyboardUtil = KeyboardUtil.instance();

        mEt = (EditText) inflate.findViewById(R.id.et_input);
        mEt.setFocusable(false);
        inflate.findViewById(R.id.ll_return).setOnClickListener(this);
        mEt.setOnClickListener(this);
        mEt.addTextChangedListener(this);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        mKeyboardUtil.initKeyboard(getActivity(),getActivity(),mEt);
        mKeyboardUtil.showKeyboard();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.ll_return){
            EventBus.getDefault().post(new SearchFood(getResources().getString(R.string.allfood)));
            //返回主界面马上发起查询服务
            Post.getInstance().postCfpb(Net.sql_cfpb);
            mKeyboardUtil.hideKeyboard();
        }else if(view.getId()==R.id.et_input){
            mEt.setSelectAllOnFocus(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String s = charSequence.toString();
        if(s!=null){
            EventBus.getDefault().post(new InputMsg(s));
        }
    }
    @Override
    public void afterTextChanged(Editable editable) {
    }
}
