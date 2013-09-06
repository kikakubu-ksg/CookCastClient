package com.example.adnroid.camera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

public class CameraPreferences extends PreferenceActivity {
	private Handler handler = new Handler();
	private Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		addPreferencesFromResource(R.xml.parameters);
		
		final ListPreference lp1 = (ListPreference)findPreference("antibanding");
		final ListPreference lp2 = (ListPreference)findPreference("effect");
		final ListPreference lp3 = (ListPreference)findPreference("flash_mode");
		final ListPreference lp4 = (ListPreference)findPreference("focus_mode");
		final ListPreference lp5 = (ListPreference)findPreference("scene_mode");
		final ListPreference lp6 = (ListPreference)findPreference("white_balance");
		
		final OnPreferenceChangeListener listener = new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (preference == lp1) {
					intent.putExtra("antibanding", (String)newValue);
				} else if (preference == lp2) {
					intent.putExtra("effect", (String)newValue);
				} else if (preference == lp3) {
					intent.putExtra("flash_mode", (String)newValue);
				} else if (preference == lp4) {
					intent.putExtra("focus_mode", (String)newValue);
				} else if (preference == lp5) {
					intent.putExtra("scene_mode", (String)newValue);
				} else if (preference == lp6) {
					intent.putExtra("white_balance", (String)newValue);
				}
				preference.setSummary((String)newValue);
				setResult(0, intent);
				return true;
			}
		};
		
		lp1.setOnPreferenceChangeListener(listener);
		lp2.setOnPreferenceChangeListener(listener);
		lp3.setOnPreferenceChangeListener(listener);
		lp4.setOnPreferenceChangeListener(listener);
		lp5.setOnPreferenceChangeListener(listener);
		lp6.setOnPreferenceChangeListener(listener);
		
		handler.post(new Runnable() {
			@Override
			public void run() {
				String summary;
				if ((summary = intent.getStringExtra("antibanding")) != null) {
					listener.onPreferenceChange(lp1, summary);
				}
				if ((summary = intent.getStringExtra("effect")) != null) {
					listener.onPreferenceChange(lp2, summary);
				}
				if ((summary = intent.getStringExtra("flash_mode")) != null) {
					listener.onPreferenceChange(lp3, summary);
				}
				if ((summary = intent.getStringExtra("focus_mode")) != null) {
					listener.onPreferenceChange(lp4, summary);
				}
				if ((summary = intent.getStringExtra("scene_mode")) != null) {
					listener.onPreferenceChange(lp5, summary);
				}
				if ((summary = intent.getStringExtra("white_balance")) != null) {
					listener.onPreferenceChange(lp6, summary);
				}
			}
		});
	}
}
