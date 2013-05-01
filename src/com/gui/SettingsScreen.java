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

//	private Spinner iCitySelector;
//	private Spinner iBankSelector;
//
//	private Settings iSettings;
//
//	private SettingsData iSettingsData;

	public static final String CITY_PREF_ID = "city";
	public static final String BANK_PREF_ID = "bank";

	private ListPreference iCityPref;
	private ListPreference iBankPref;

	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_screen);

//		iSettings = new Settings(this);
//		iSettingsData = new SettingsData();

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

	// private void prepareCities() {
	// Set<Integer> keys = Tables.CITY_NAMES.keySet();
	// // List<String> cities = new ArrayList<String>();
	// CharSequence[] cities = new String[keys.size()];
	// CharSequence[] cityIndexes = new String[keys.size()];
	// int i = 0;
	// for (int key : keys) {
	// int resId = Tables.CITY_NAMES.get(key);
	// cities[i] = getString(resId);
	// cityIndexes[i] = String.valueOf(key);
	// // cities.add(getString(resId));
	// }
	//
	// // iCityPref.setEntryValues(cityIndexes);
	// iCityPref.setEntries(cities);
	// }

	/*
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // no menu is required
	// // getMenuInflater().inflate(R.menu.settings_screen, menu);
	// return true;
	// }

	/*
	 * @see android.app.Activity#onPause()
	 */
	// @Override
	// protected void onPause() {
	// super.onPause();
	//
	// // iSettingsData.iCity = iCitySelector.getSelectedItemPosition();
	// // iSettingsData.iBank = iBankSelector.getSelectedItemPosition();
	//
	// iSettings.storeSettings(iSettingsData);
	//
	// }

	/*
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}

}
