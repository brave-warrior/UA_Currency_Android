package com.gui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.khmelenko.lab.currency.R;

/**
 * Provides application settings
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class SettingsScreen extends PreferenceActivity {

	// IDs of setting items
	public static final String CITY_PREF_ID = "city";
	public static final String BANK_PREF_ID = "bank";

	// setting items
	private ListPreference iCityPref;
	private ListPreference iBankPref;

	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_screen);

		iCityPref = (ListPreference) findPreference(CITY_PREF_ID);
		iBankPref = (ListPreference) findPreference(BANK_PREF_ID);

		String currCity = iCityPref.getEntry().toString();
		iCityPref.setSummary(currCity);

		String currBank = iBankPref.getEntry().toString();
		iBankPref.setSummary(currBank);

		iCityPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference aPreference,
							Object aNewValue) {
						updateCitySummary(Integer.valueOf((String) aNewValue));
						return true;
					}
				});

		iBankPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference aPreference,
							Object aNewValue) {
						updateBankSummary(Integer.valueOf((String) aNewValue));
						return true;
					}
				});
	}

	/**
	 * Updates city summary
	 * 
	 * @param aCityId
	 *            City Id
	 */
	private void updateCitySummary(int aCityId) {
		String[] cities = getResources().getStringArray(R.array.Cities);
		String newCity = cities[aCityId];
		iCityPref.setSummary(newCity);
	}

	/**
	 * Updates bank summary
	 * 
	 * @param aBankId
	 *            Bank Id
	 */
	private void updateBankSummary(int aBankId) {
		String[] cities = getResources().getStringArray(R.array.Banks);
		String newCity = cities[aBankId];
		iBankPref.setSummary(newCity);
	}

}
