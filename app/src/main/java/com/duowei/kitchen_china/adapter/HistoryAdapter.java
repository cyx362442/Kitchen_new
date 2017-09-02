package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.activity.MainActivity;
import com.duowei.kitchen_china.application.MyApplication;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.event.UsbState;
import com.duowei.kitchen_china.print.PrintHandler;
import com.duowei.kitchen_china.uitls.PreferenceUtils;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2017-07-01.
 */

public class HistoryAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Cfpb> listCfpb;
    private String mPrintStytle;

    public HistoryAdapter(Context context,List<Cfpb> listCfpb) {
        this.context = context;
        this.listCfpb = listCfpb;
        mLayoutInflater = LayoutInflater.from(context);
        mPrintStytle = PreferenceUtils.getInstance(context).getPrintStytle("printStytle", context.getResources().getString(R.string.closeprint));
    }

    public void setList(List<Cfpb>listCfpb){
        this.listCfpb=listCfpb;
    }

    @Override
    public int getCount() {
        return listCfpb.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHold hold=null;
        Cfpb cfpb =null;
        if(convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.history_item, null);
            hold = new ViewHold();
            hold.tvIndex= (TextView) convertView.findViewById(R.id.tv_index);
            hold.tvName= (TextView) convertView.findViewById(R.id.tv_name);
            hold.tvTable= (TextView) convertView.findViewById(R.id.tv_table);
            hold.tvNum= (TextView) convertView.findViewById(R.id.tv_num);
            hold.tvBeizhu= (TextView) convertView.findViewById(R.id.tv_beizhu);
            hold.tvOrderTime= (TextView) convertView.findViewById(R.id.tv_ordertime);
            hold.tvCompleteTime= (TextView) convertView.findViewById(R.id.tv_completetime);
            hold.llPrint= (RelativeLayout) convertView.findViewById(R.id.ll_print);
            convertView.setTag(hold);
        }else{
            hold= (ViewHold) convertView.getTag();
        }
        cfpb = listCfpb.get(position);
        hold.tvIndex.setText((position+1)+"");
        hold.tvName.setText(cfpb.getXmmc());
        hold.tvTable.setText(cfpb.getCzmc());
        hold.tvNum.setText(cfpb.getSl()+"");
        hold.tvBeizhu.setText(cfpb.getPz());
        hold.tvOrderTime.setText(cfpb.getXdsj());
        String wcsj = cfpb.getWcsj();
        String sb = wcsj.substring(wcsj.length() - 8, wcsj.length());
        hold.tvCompleteTime.setText(sb);

        //打印
        final Cfpb finalCfpb = cfpb;
        hold.llPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPrintStytle.equals(context.getResources().getString(R.string.print_usb))){
                    if (usbPrintConnect()) return;
                    PrintHandler.getInstance().printUsb(MainActivity.mGpService,finalCfpb);
                }else if(mPrintStytle.equals(context.getResources().getString(R.string.print_net))){
                    PrintHandler.getInstance().printSingle(finalCfpb);
                }
            }
        });
        return convertView;
    }

    class ViewHold {
        TextView tvIndex;
        TextView tvName;
        TextView tvTable;
        TextView tvNum;
        TextView tvBeizhu;
        TextView tvOrderTime;
        TextView tvCompleteTime;
        RelativeLayout llPrint;
    }

    private boolean usbPrintConnect() {
        EscCommand esc = new EscCommand();
        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = MainActivity.mGpService.sendEscCommand(0, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(context,"打印机己断开，请重新连接",Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
