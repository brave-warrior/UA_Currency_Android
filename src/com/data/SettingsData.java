/**
 * 
 */
package com.data;


/**
 * Application settings data
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class SettingsData implements Cloneable {

	/** The ID of actual city*/
	public int iCity;
	
	/** The ID of actual bank */
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
}
