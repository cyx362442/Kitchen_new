package com.duowei.kitchen_china;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.duowei.kitchen_china.uitls.PreferenceUtils;

public class SettingsActivity extends AppCompatActivity {

    private SettingFragment mFragment;
    private static PreferenceUtils mPreferenceUtils;
    private static EditTextPreference mEtServiceIP;
    private static EditTextPreference mEtPrinterIP;
    private static CheckBoxPreference mCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mFragment = new SettingFragment();
        ft.replace(R.id.frame_setting, mFragment).commit();
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
            mEtPrinterIP = (EditTextPreference) findPreference("et_printerIP");
            mCheckbox = (CheckBoxPreference) findPreference("checkbox");
            mEtServiceIP.setSummary(mPreferenceUtils.getServiceIp("serviceIP",""));
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
            }else if(key.equals("et_printerIP")){
                String printerIP = sharedPreferences.getString("et_printerIP", "");
                mEtPrinterIP.setSummary(printerIP);
                mPreferenceUtils.setPrinterIp("printerIP",printerIP);
            }else if(key.equals("checkbox")){
                boolean auto = sharedPreferences.getBoolean("auto", true);
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
            finish();
        }
        return true;
    }
}
