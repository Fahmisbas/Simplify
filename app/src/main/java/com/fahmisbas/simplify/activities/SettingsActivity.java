package com.fahmisbas.simplify.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.fahmisbas.simplify.R;

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

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
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