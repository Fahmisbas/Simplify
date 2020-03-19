package com.revelatestudio.simplify.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.revelatestudio.simplify.BuildConfig;
import com.revelatestudio.simplify.R;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        toSettingFargment();
        setToolbar();
    }

    private void toSettingFargment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_settings, new SettingsFragment(this))
                .commit();
    }

    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.tv_toolbar_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            title.setText("Settings");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private Context context;

        SettingsFragment(Context context) {
            this.context = context;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            fontTypePref();
            autoSavePref();
            aboutPref();
            versionPref();
            moreAppPrefs();
        }

        private void moreAppPrefs() {
            Preference moreAppPreference = findPreference("moreApp_preference");
            if (moreAppPreference != null) {
                moreAppPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Revelate+Studio"));
                        startActivity(intent);
                        return true;
                    }
                });
            }
        }

        private void versionPref() {
            Preference versionPreference = findPreference("version_preference");
            String version = BuildConfig.VERSION_NAME;
            if (versionPreference != null) {
                versionPreference.setSummary(version);
            }
        }

        private void aboutPref() {
            Preference aboutPreference = findPreference("about_preference");
            if (aboutPreference != null) {
                aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setTitle("About")
                                .setMessage(R.string.about_dialog)
                                .setPositiveButton("Ok",null);
                        builder.show();
                        return true;
                    }
                });
            }
        }

        private void autoSavePref() {
            final SwitchPreference savePreference = findPreference("save_preference");
            if (savePreference != null) {
                savePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("com.fahmisbas.simplify", MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("save_preference", (Boolean) newValue).apply();
                        return true;
                    }
                });
            }
        }

        private void fontTypePref() {
            DropDownPreference fontPreference = findPreference("font_preference");
            if (fontPreference != null) {
                fontPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("com.fahmisbas.simplify", MODE_PRIVATE);
                        sharedPreferences.edit().putString("font_preference", (String) newValue).apply();
                        return true;
                    }
                });
            }
        }
    }
}