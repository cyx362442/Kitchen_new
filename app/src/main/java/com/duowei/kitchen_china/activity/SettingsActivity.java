package com.duowei.kitchen_china.activity;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {

    private SettingFragment mFragment;
    private static PreferenceUtils mPreferenceUtils;
    private static EditTextPreference mEtServiceIP;
    private static EditTextPreference mEtPrinterIP;
    private static CheckBoxPreference mCheckbox;
    private static ListPreference listPrint;
    private static EditTextPreference etKetchen;

    public final static int RESULTCODE=300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

    public static class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
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
            mCheckbox = (CheckBoxPreference) findPreference("checkbox");
            mEtServiceIP.setSummary(mPreferenceUtils.getServiceIp("serviceIP",""));
            etKetchen.setSummary(mPreferenceUtils.getKetchen("et_kitchenName",""));
            listPrint.setSummary(mPreferenceUtils.getPrintStytle("printStytle",getResources().getString(R.string.print_usb)));
            mEtPrinterIP.setSummary(mPreferenceUtils.getPrinterIp("printerIP",""));
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
            setResult(300);
            finish();
        }
        return true;
    }
}
