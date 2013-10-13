/**
 * Copyright Khmelenko Lab
 * Author: Dmytro Khmelenko
 */
package com.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gui.SettingsScreen;

/**
 * Contains application settings
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class SettingsData {

	public int iCity;
	public int iBank;

	/**
	 * Constructor
	 */
	public SettingsData() {
		iCity = 0;
		iBank = 0;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aO) {
		if (aO == null)
			return false;
		if (!(aO instanceof SettingsData))
			return false;

		SettingsData that = (SettingsData) aO;

		return iCity == that.iCity && iBank == that.iBank;
	}

	/*
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SettingsData clone() {
		SettingsData settings = new SettingsData();
		settings.iBank = this.iBank;
		settings.iCity = this.iCity;
		return settings;
	}

	/**
	 * Loads application settings from the storage
	 * 
	 * @param aContext
	 *            Application context
	 */
	public void loadSettings(Context aContext) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(aContext);

		String city = prefs.getString(SettingsScreen.CITY_PREF_ID, "0");
		String bank = prefs.getString(SettingsScreen.BANK_PREF_ID, "0");

		iCity = Integer.valueOf(city);
		iBank = Integer.valueOf(bank);
	}
}
