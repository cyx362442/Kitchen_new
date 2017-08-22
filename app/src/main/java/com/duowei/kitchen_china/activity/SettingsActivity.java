package com.duowei.kitchen_china.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.event.Update;
import com.duowei.kitchen_china.fragment.UpdateFragment;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SettingsActivity extends AppCompatActivity {

    private SettingFragment mFragment;
    private static PreferenceUtils mPreferenceUtils;
    private static EditTextPreference mEtServiceIP;
    private static EditTextPreference mEtPrinterIP;
    private static CheckBoxPreference mCheckbox;
    private static ListPreference listPrint;
    private static EditTextPreference etKetchen;

    public final static int RESULTCODE=300;
    private static Preference etVersion;
    private static String mVersionName;
    private static int mVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EventBus.getDefault().register(this);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getAPPVersionName();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mFragment = new SettingFragment();
        ft.replace(R.id.frame_setting, mFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Subscribe
    public void updateNow(Update event){
       android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UpdateFragment fragment = UpdateFragment.newInstance(event.url,event.name);
        fragment.show(ft,getString(R.string.update));
//        Uri uri = Uri.parse(event.url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
    }

    public static class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPreferenceUtils=PreferenceUtils.getInstance(getActivity());
            addPreferencesFromResource(R.xml.preference);
            initPreference();
        }
        private void initPreference() {
            mEtServiceIP = (EditTextPreference)findPreference("et_serviceIP");
            etKetchen = (EditTextPreference) findPreference("et_kitchenName");
            listPrint = (ListPreference) findPreference("printStytle");
            mEtPrinterIP = (EditTextPreference) findPreference("et_printerIP");
            etVersion = findPreference("et_version");
            mCheckbox = (CheckBoxPreference) findPreference("checkbox");
            mEtServiceIP.setSummary(mPreferenceUtils.getServiceIp("serviceIP",""));
            etKetchen.setSummary(mPreferenceUtils.getKetchen("et_kitchenName",""));
            listPrint.setSummary(mPreferenceUtils.getPrintStytle("printStytle",getResources().getString(R.string.print_usb)));
            mEtPrinterIP.setSummary(mPreferenceUtils.getPrinterIp("printerIP",""));
            etVersion.setSummary(mVersionName);
            etVersion.setTitle("版本更新(V"+mVersionCode+")");
            etVersion.setOnPreferenceClickListener(this);
            mCheckbox.setChecked(mPreferenceUtils.getAutoStart("auto",true));
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("et_serviceIP")){
                String serviceIP = sharedPreferences.getString("et_serviceIP", "");
                mEtServiceIP.setSummary(serviceIP);
                mPreferenceUtils.setServiceIp("serviceIP",serviceIP);
            }else if(key.equals("et_kitchenName")){
                String kitChen = sharedPreferences.getString("et_kitchenName", "");
                etKetchen.setSummary(kitChen);
                mPreferenceUtils.setKetchen("et_kitchenName",kitChen);
            }else if(key.equals("printStytle")){
                String printStytle = sharedPreferences.getString("printStytle", "");
                listPrint.setSummary(printStytle);
                mPreferenceUtils.setPrintStytle("printStytle",printStytle);
            }else if(key.equals("et_printerIP")){
                String printerIP = sharedPreferences.getString("et_printerIP", "");
                mEtPrinterIP.setSummary(printerIP);
                mPreferenceUtils.setPrinterIp("printerIP",printerIP);
            }else if(key.equals("checkbox")){
                boolean auto=mPreferenceUtils.getAutoStart("auto", true);
                auto=!auto;
                mCheckbox.setChecked(auto);
                mPreferenceUtils.setAutoStart("auto",auto);
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            Post.getInstance().checkUpdate(getActivity(),false);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_exit){
            setResult(RESULTCODE);
            finish();
        }
        return true;
    }

    //当前APP版本号
    public void getAPPVersionName() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            // 版本名
            mVersionName = info.versionName;
            mVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
