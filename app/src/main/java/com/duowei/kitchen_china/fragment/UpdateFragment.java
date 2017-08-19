package com.duowei.kitchen_china.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadTask;
import com.arialyy.aria.util.CommonUtil;
import com.duowei.kitchen_china.R;

import java.io.File;


/**
 * A simple {@link } subclass.
 */
public class UpdateFragment extends DialogFragment{

    private ProgressBar mPb;
    private TextView mTvSize;

    public static UpdateFragment newInstance(String url,String name){
        UpdateFragment fragment = new UpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("name",name);
        fragment.setArguments(bundle);//把参数传递给该DialogFragment
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_update, null);
        mPb = (ProgressBar) inflate.findViewById(R.id.pb);
        mTvSize = (TextView) inflate.findViewById(R.id.tv_size);

        String url = getArguments().getString("url", "");
        String name = getArguments().getString("name", "");
        String filePath = Environment.getExternalStorageDirectory() + "/duowei/";
        Aria.download(getActivity()).
                load(url).
                setDownloadPath(filePath+name).
                start();
        Aria.download(getActivity()).addSchedulerListener(new MySchedulerListener());
        return new AlertDialog.Builder(getActivity()).setView(inflate).create();
    }

    class MySchedulerListener extends Aria.DownloadSchedulerListener{
        @Override
        public void onTaskRunning(DownloadTask task) {
            super.onTaskRunning(task);
            long current = task.getCurrentProgress();
            long len = task.getFileSize();
            if (len == 0) {
                mPb.setProgress(0);
            } else {
                mPb.setProgress((int) ((current * 100) / len));
            }
        }
        @Override
        public void onTaskComplete(DownloadTask task) {
            String downloadPath = task.getDownloadPath();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(downloadPath)),
                    "application/vnd.android.package-archive");
            startActivity(intent);
            dismiss();
            super.onTaskComplete(task);
        }
    }
}
