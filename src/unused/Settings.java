/**
 * 
 */
package unused;

import android.content.Context;
import android.content.SharedPreferences;

import com.data.SettingsData;
import com.khmelenko.lab.currency.R;

/**
 * @author Dmytro Khmelenko
 * 
 */
public class Settings {

	public static final String PREFS_NAME = "MyPrefsFile";
	private static final String CURRENT_CITY = "CURRENT_CITY";
	private static final String CURRENT_BANK = "CURRENT_BANK";

	public final static int ALL_CITIES = 0;
	public final static int ALL_BANKS = 0;

	private Context iContext;

	public Settings(Context aContext) {
		iContext = aContext;
	}

	/**
	 * Stores settings to the shared preferences
	 * 
	 * @param aSettings
	 *            Settings data to store
	 */
	public void storeSettings(SettingsData aSettings) {
		SharedPreferences settings = iContext.getSharedPreferences(PREFS_NAME,
				0);
		SharedPreferences.Editor editor = settings.edit();

		int city = aSettings.iCity;
		editor.putInt(CURRENT_CITY, city);

		int bank = aSettings.iBank;
		editor.putInt(CURRENT_BANK, bank);

		// Commit the edits!
		editor.commit();
	}

	/**
	 * Loads settings from the store
	 * 
	 * @return Loaded settings data
	 */
	public SettingsData loadSettings() {
		SettingsData settingsData = new SettingsData();

		// Restore preferences
		SharedPreferences settings = iContext.getSharedPreferences(PREFS_NAME,
				0);
		settingsData.iCity = settings.getInt(CURRENT_CITY, Settings.ALL_CITIES);
		settingsData.iBank = settings.getInt(CURRENT_BANK, Settings.ALL_BANKS);

		return settingsData;
	}

	// public final static HashMap<Integer, String[]> CITIES = new
	// HashMap<Integer, String[]>() {
	//
	// };

}
