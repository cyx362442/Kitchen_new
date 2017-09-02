package com.duowei.kitchen_china.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.CookListAdapter;
import com.duowei.kitchen_china.adapter.MaxListView;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.Completes;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link } subclass.
 */
public class CookFragment extends DialogFragment implements View.OnClickListener{

    private List<Cfpb_item> mListCfpb;
    private CookListAdapter mAdapter;
    private List<Cfpb> listCfpbComplete;//己完成的
    private Cfpb mCfpb;
    private String mPrintStytle;

    public static CookFragment newInstance(Cfpb cfpb) {
        Bundle args = new Bundle();
        args.putSerializable("cfpb",cfpb);
        CookFragment fragment = new CookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View inflate = layoutInflater.inflate(R.layout.fragment_cook, null);
        mCfpb = (Cfpb) getArguments().getSerializable("cfpb");
        listCfpbComplete=new ArrayList<>();
        mListCfpb = mCfpb.getListCfpb();
        PreferenceUtils instance = PreferenceUtils.getInstance(getActivity());
        mPrintStytle = instance.getPrintStytle("printStytle", getResources().getString(R.string.closeprint));

        TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        tvTitle.setText(mCfpb.getXmmc());
        inflate.findViewById(R.id.btn_cancelall).setOnClickListener(this);
        inflate.findViewById(R.id.btn_completeall).setOnClickListener(this);
        inflate.findViewById(R.id.btn_doneall).setOnClickListener(this);
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        inflate.findViewById(R.id.btn_confirm).setOnClickListener(this);

        MaxListView lv = (MaxListView) inflate.findViewById(R.id.listView);
        lv.setDividerHeight(1);
        lv.setListViewHeight(500);
        mAdapter = new CookListAdapter(mListCfpb, getActivity());
        lv.setAdapter(mAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setView(inflate).show();
        //设置dialog宽度
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //获取屏幕宽
        wlp.width = (int) (d.getWidth())*3/4;
        window.setAttributes(wlp);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    public void onClick(View view) {
        String sql="";
        switch (view.getId()){
            case R.id.btn_confirm://确定
                listCfpbComplete.clear();
                for(int i=0;i<mListCfpb.size();i++){
                    Cfpb_item item = mListCfpb.get(i);
                    //制作
                    if("1".equals(item.getBy10())){
                        sql+="update cfpb set by10='1' where xh='"+item.xh+"'|";
                    }else {
                        sql+="update cfpb set by10='0' where xh='"+item.xh+"'|";
                    }
                    //完成
                    if("1".equals(item.getWc())){
                        if(mPrintStytle.equals(getString(R.string.print_server))){//使用打印服务器的
                            sql+="insert into pbdyxxb(xh,wmdbh,xmbh,xmmc,dw,sl,pz,syyxm,xtbz,czsj,zh)" +
                                    "select xh,wmdbh,xmbh,xmmc,dw,"+item.sl1+",pz,yhmc,'3',getdate(),by1 from cfpb where XH='"+item.xh+"'|";
                        }
                        sql+="delete from cfpb where xh='"+item.xh+"'|";
                        Cfpb cfpb = new Cfpb(mCfpb.getXH(), mCfpb.getXmbh(), mCfpb.getXmmc(), mCfpb.getDw(),
                                item.sl1, item.pz, mCfpb.getXdsj(), item.czmc1,
                                item.fzs, mCfpb.getYhmc(), item.sl1, DateTimes.getTime(),
                                DateTimes.getTime2(mCfpb.getXdsj()));
                        listCfpbComplete.add(cfpb);
                    }
                }
                //己完成的
                EventBus.getDefault().post(new Completes(listCfpbComplete));
                //定位最后一次点击的单品
                sql+="update cfpb set by9='0' where by9='1' and XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) AND GETDATE()|";
                sql+="update cfpb set by9='1' where xmbh='"+mCfpb.getXmbh()+"'and XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) AND GETDATE()|";
                Post.getInstance().setPost7(sql);
                EventBus.getDefault().post(new StartProgress());
                dismiss();
                break;
            case R.id.btn_cancel://取消
                dismiss();
                break;
            case R.id.btn_completeall://全完成
                for(int i=0;i<mListCfpb.size();i++){
                    Cfpb_item item = mListCfpb.get(i);
                    item.setWc("1");
                    item.setBy10("0");
                }
                mAdapter.setListCfpb(mListCfpb);
                break;
            case R.id.btn_doneall://全制作
                for(int i=0;i<mListCfpb.size();i++){
                    Cfpb_item item = mListCfpb.get(i);
                    item.setBy10("1");
                    item.setWc("0");
                }
                mAdapter.setListCfpb(mListCfpb);
                break;
            case R.id.btn_cancelall:
                for(int i=0;i<mListCfpb.size();i++){
                    mListCfpb.get(i).setBy10("0");
                    mListCfpb.get(i).setWc("0");
                }
                mAdapter.setListCfpb(mListCfpb);
                break;
        }
    }
}
