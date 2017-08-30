package com.duowei.kitchen_china.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.RecAdapter;
import com.duowei.kitchen_china.adapter.SpacesItemDecoration;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.Completes;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.event.UsbState;
import com.duowei.kitchen_china.fragment.dialog.CookFragment;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.print.PrintHandler;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.duowei.kitchen_china.uitls.PreferenceUtils;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements RecAdapter.onItemClickListener,
        RecAdapter.onContinueClickListener {

    private RecAdapter mRecAdapter;
    private List<Cfpb> listCfpb;
    private List<Cfpb> listCfpbComplete;//己完成的
    private int tempList;
    private int currentPosition=0;
    private String mPrintStytle;

    private GpService mGpService = null;
    private boolean mMakeModel;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        EventBus.getDefault().register(MainFragment.this);
        mMakeModel = PreferenceUtils.getInstance(getActivity()).getMakeModel("spf_makeModel", false);
        listCfpb=new ArrayList<>();
        listCfpbComplete =new ArrayList<>();
        initRecy(inflate);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceUtils instance = PreferenceUtils.getInstance(getActivity());
        mPrintStytle = instance.getPrintStytle("printStytle", getResources().getString(R.string.print_usb));
    }

    @Subscribe
    public void setListCfpbComplete(Completes event){
        listCfpbComplete=event.getListCfpbComplete();
    }

    private void initRecy(View inflate) {
        RecyclerView rv = (RecyclerView) inflate.findViewById(R.id.recycleView);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),3));//gridview布局,4列
        rv.addItemDecoration(new SpacesItemDecoration(5));//设置item边距
        rv.setItemAnimator(new DefaultItemAnimator());
        mRecAdapter = new RecAdapter(getActivity(), listCfpb);
        rv.setAdapter(mRecAdapter);
        mRecAdapter.setOnItemClickListener(this);
        mRecAdapter.setOnContinueClickListener(this);
    }

    public void setRecycleView(List<Cfpb>list){
        mRecAdapter.setList(listCfpb=list);
        if(list.size()==tempList||list.size()==0){
            mRecAdapter.notifyDataSetChanged();
        }else if(list.size()<tempList){//list变小，启用删除动画
            mRecAdapter.notifyItemRemoved(currentPosition);
            mRecAdapter.notifyItemRangeChanged(currentPosition,mRecAdapter.getItemCount());
        }else if(list.size()>tempList){//list变大，启用增加动画
            mRecAdapter.notifyItemInserted(0);
        }
        tempList=list.size();
    }

    public void setRecycleView2(List<Cfpb>list){
        mRecAdapter.setList(listCfpb=list);
        mRecAdapter.notifyDataSetChanged();
    }

    public void setGpService(GpService gpService) {
        mGpService = gpService;
    }

    public void updateSuccess(){
        Post.getInstance().postCfpb(Net.sql_cfpb);
        DataSupport.saveAll(listCfpbComplete);
        if(mPrintStytle.equals(getResources().getString(R.string.print_net))){//网络打印机
            PrintHandler.getInstance().print(listCfpbComplete);
        }else if(mPrintStytle.equals(getResources().getString(R.string.print_usb))){//usb打印机
            PrintHandler.getInstance().printUsb(mGpService,listCfpbComplete);
        }
    }

    @Override
    public void setOnItemClickListener(int index) {
        mRecAdapter.setIndex(index);
        mRecAdapter.notifyDataSetChanged();
    }
    /**继续按键点击事件*/
    @Override
    public void setOnContinueClickListener(int index,float num) {
        currentPosition=index;
        if(mPrintStytle.equals(getActivity().getResources().getString(R.string.print_usb))){
            if (usbPrintConnect()) return;
        }
        if(mMakeModel==true){//启用制作模式
            CookFragment fragment = CookFragment.newInstance(listCfpb.get(index));
            fragment.show(getFragmentManager(),getString(R.string.cook));
            return;
        }
        float tempNum=0;
        Cfpb cfpb21=null;
        listCfpbComplete.clear();
        EventBus.getDefault().post(new StartProgress());
        String sql="";
        mRecAdapter.setIndex(index);
        Cfpb cfpb = listCfpb.get(index);
        List<Cfpb_item> listCfpb_item = cfpb.getListCfpb();
        for(int i=0;i<listCfpb_item.size();i++){
            if(num>0){
                Cfpb_item cfpbItem = listCfpb_item.get(i);
                if(cfpbItem.sl1<=num){//当前桌号待删除的单品数量<=num,直接删除这行
                    sql += "insert into CFPBYWC (XH, MTXH, WMDBH, XMBH, XMMC, DW, SL, PZ, XSZT, YHMC, POS, TDSL, XDSJ, WCSJ,      BY1, BY2, BY3, BY4, BY5, BY6, BY7) " +
                            "             select XH, MTXH, WMDBH, XMBH, XMMC, DW, SL, PZ, XSZT, YHMC, POS, TDSL, XDSJ, GETDATE(), BY1, BY2, BY3, BY4, BY5, BY6, BY7 " +
                            "             from CFPB where xh = " + cfpbItem.xh + "|";
                    sql+="delete from cfpb where xh="+cfpbItem.xh+"|";
                    tempNum=cfpbItem.sl1;
                }else {//当前桌号待删除的单品数量>num,更新己用数量字段
                    sql+="update cfpb set ywcsl=isnull(ywcsl,0)+"+num+" where xh="+cfpbItem.xh+"|";
                    tempNum=num;
                }
                //己完成
                cfpb21 = new Cfpb(cfpb.getXH(), cfpb.getXmbh(), cfpb.getXmmc(), cfpb.getDw(),
                            cfpbItem.sl1, cfpbItem.pz, cfpb.getXdsj(), cfpbItem.czmc1,
                            cfpbItem.fzs, cfpb.getYhmc(), tempNum, DateTimes.getTime(),
                        DateTimes.getTime2(cfpb.getXdsj()));
                listCfpbComplete.add(cfpb21);
                num=num-cfpbItem.sl1;
            }else{
                break;
            }
        }
        //只最后一次点击的单品置‘1’
        sql+="update cfpb set by9='0' where by9='1' and XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) AND GETDATE()|";
        sql+="update cfpb set by9='1' where xmbh='"+cfpb.getXmbh()+"'and XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) AND GETDATE()|";
        Post.getInstance().setPost7(sql);
    }

    private boolean usbPrintConnect() {
        EscCommand esc = new EscCommand();
        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(0, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                EventBus.getDefault().post(new UsbState(getActivity().getResources().getString(R.string.reconnect)));
                return true;
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MainFragment.this);
    }
}
