package com.duowei.kitchen_china.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duowei.kitchen_china.R;

/**
 * Created by Administrator on 2017-06-24.
 */

public class DigitInput implements View.OnClickListener{
    private TextView mTvTitle;
    private TextView mContent;
    private EditText mEtInput;

    private DigitInput(){}
    private static DigitInput mDigitInput=null;
    public static DigitInput instance(){
        if(mDigitInput==null){
            mDigitInput=new DigitInput();
        }
        return mDigitInput;
    }
    Context context;
    String str="";
    private String title;
    private float num;
    private AlertDialog mDialog;
    private LinearLayout mLayout;
    public OnconfirmClick listener;


    public interface OnconfirmClick{
        void confirmListener(String title,String inputMsg);
    }
    public void setOnconfirmClick(OnconfirmClick listener){
        this.listener=listener;
    }

    public void show(Context context, String title,String content,float num){
        this.context = context;
        this.title=title;
        this.num=num;
        str="";
        mDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = (LinearLayout) inflater.inflate(R.layout.dialog_digitinput, null);
        mDialog.setView(mLayout);
        // by zjn
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            mDialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
            mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    View decorView = mDialog.getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    decorView.setSystemUiVisibility(uiOptions);

                    mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                }
            });
        }
//
        // ---

        mDialog.show();
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        mDialog.getWindow().setAttributes(params);
        initKey(title,content);
    }

    private void initKey(String title,String contents) {
        mTvTitle = (TextView) mLayout.findViewById(R.id.tv_title);
        mContent = (TextView) mLayout.findViewById(R.id.tv_content);
        mEtInput = (EditText) mLayout.findViewById(R.id.tv_input);
        mLayout.findViewById(R.id.key1).setOnClickListener(this);
        mLayout.findViewById(R.id.key2).setOnClickListener(this);
        mLayout.findViewById(R.id.key3).setOnClickListener(this);
        mLayout.findViewById(R.id.key4).setOnClickListener(this);
        mLayout.findViewById(R.id.key5).setOnClickListener(this);
        mLayout.findViewById(R.id.key6).setOnClickListener(this);
        mLayout.findViewById(R.id.key7).setOnClickListener(this);
        mLayout.findViewById(R.id.key8).setOnClickListener(this);
        mLayout.findViewById(R.id.key9).setOnClickListener(this);
        mLayout.findViewById(R.id.keyzero).setOnClickListener(this);
        mLayout.findViewById(R.id.keydel).setOnClickListener(this);
        mLayout.findViewById(R.id.keydot).setOnClickListener(this);
        mLayout.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mLayout.findViewById(R.id.tv_confirm).setOnClickListener(this);

        mTvTitle.setText(title+"    "+num);
        mContent.setText(contents);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.key1:
                inputNum("1");
                break;
            case R.id.key2:
                inputNum("2");
                break;
            case R.id.key3:
                inputNum("3");
                break;
            case R.id.key4:
                inputNum("4");
                break;
            case R.id.key5:
                inputNum("5");
                break;
            case R.id.key6:
                inputNum("6");
                break;
            case R.id.key7:
                inputNum("7");
                break;
            case R.id.key8:
                inputNum("8");
                break;
            case R.id.key9:
                inputNum("9");
                break;
            case R.id.keyzero:
                inputNum("0");
                break;
            case R.id.keydot:
                if(str.length()>0&&!str.contains(".")){
                    inputNum(".");
                }
                break;
            case R.id.keydel:
                if(str.length()>0){
                    str=str.substring(0,str.length()-1);
                    mEtInput.setText(str);
                }
                break;
            case R.id.tv_cancel:
                cancel();
                break;
            case R.id.tv_confirm:
                String input = mEtInput.getText().toString().trim();
                if(TextUtils.isEmpty(input)){
                    Toast.makeText(context,"请输入数量", Toast.LENGTH_SHORT).show();
                }else if(num!=0&&num<Float.parseFloat(input)){
                    Toast.makeText(context,"输入数量大于现在数量", Toast.LENGTH_SHORT).show();
                }else{
                    listener.confirmListener(title,input);
                }
                break;
        }
    }

    private void inputNum(String num) {
        str=str+num;
        mEtInput.setText(str);
        mEtInput.setSelection(str.length());
    }

    public void cancel(){
        mDialog.dismiss();
    }
}
