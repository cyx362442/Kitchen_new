package com.duowei.kitchen_china.uitls;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.duowei.kitchen_china.R;

/**
 * Created by Administrator on 2017-07-04.
 */

public class ColorAnim {

    private Handler mHandler;
    private Runnable mRunnable;
    private Context context;
    private static int count=0;
    private ColorAnim(Context context){
        this.context=context;
    }
    private static ColorAnim ca=null;
    public static ColorAnim getInstacne(Context context){
        if(ca==null){
            ca=new ColorAnim(context);
        }
        count=0;
        return ca;
    }
    private int clo=0;
    public void startColor(final TextView text){
        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable=new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(mRunnable,300);
                if(clo==0){
                    text.setTextColor(Color.RED);
                    clo=1;
                }else if(clo==1){
                    text.setTextColor(Color.YELLOW);
                    clo=2;
                }else if(clo==2){
                    text.setTextColor(Color.BLUE);
                    clo=3;
                }else if(clo==3){
                    text.setTextColor(Color.GREEN);
                    clo=4;
                }else if(clo==4){
                    text.setTextColor(Color.RED);
                    clo=5;
                }else if(clo==5){
                    text.setTextColor(Color.YELLOW);
                    clo=6;
                }else if(clo==6){
                    text.setTextColor(Color.BLUE);
                    clo=7;
                }else if(clo==7){
                    text.setTextColor(Color.GREEN);
                    clo=8;
                }else if(clo==8){
                    text.setTextColor(context.getResources().getColor(R.color.yellow));
                    clo=0;
                    mHandler.removeCallbacks(mRunnable);
                }
            }
        },0);
    }

    public void startBackground(final Button text){
        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable=new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(mRunnable,200);
                if(count%3==0){
                    text.setBackground(context.getResources().getDrawable(R.drawable.button_red));
                }else if(count%3==1){
                    text.setBackground(context.getResources().getDrawable(R.drawable.button_blue));
                }else if(count%3==2){
                    text.setBackground(context.getResources().getDrawable(R.drawable.button_green));
                }
                count++;
                if(count>=30){
                    text.setBackground(context.getResources().getDrawable(R.drawable.button_blue));
                    count=0;
                    mHandler.removeCallbacks(mRunnable);
                }
            }
        },0);
    }
}
