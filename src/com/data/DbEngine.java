/**
 * Copyright Khmelenko Lab
 * Author: Dmytro Khmelenko
 */
package com.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Provides methods for working with DB
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class DbEngine {

	public static final String DATABASE_NAME = "currency.db";

	private static final String CURRENCY_TABLE = "current_currency";
	private static final String USER_TABLE = "user_data";

	public static class DbEngineHelper extends SQLiteOpenHelper {

		private static final int DATABASE_VERSION = 1;

		// creation table of schedule
		private static final String CURRENCY_TABLE_CREATE = "create table IF NOT EXISTS "
				+ CURRENCY_TABLE
				+ " (_id integer primary key autoincrement, "
				+ "currency_id integer, currency_name text not null, sell real, "
				+ "buy real, sell_delta real, buy_delta real);";

		private static final String USER_TABLE_CREATE = "create table IF NOT EXISTS "
				+ USER_TABLE
				+ " (_id integer primary key autoincrement, "
				+ "city_id integer, bank_id integer, time_update integer);";

		/**
		 * Constructor
		 * 
		 * @param aContext
		 *            Context
		 */
		public DbEngineHelper(Context aContext) {
			super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/*
		 * Called during creation of the database
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database
		 * .sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(CURRENCY_TABLE_CREATE);
			database.execSQL(USER_TABLE_CREATE);
		}

		/*
		 * Called during an upgrade of the database
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database
		 * .sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w(DbEngineHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			onCreate(database);
		}
	}

	// Database Currency fields.
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CURRENCY_ID = "currency_id";
	public static final String KEY_CURRENCY_NAME = "currency_name";
	public static final String KEY_SELL = "sell";
	public static final String KEY_BUY = "buy";
	public static final String KEY_SELL_DELTA = "sell_delta";
	public static final String KEY_BUY_DELTA = "buy_delta";

	// Database User fields
	public static final String KEY_CITY_ID = "city_id";
	public static final String KEY_BANK_ID = "bank_id";
	public static final String KEY_TIME_UPDATE = "time_update";

	private Context iContext;
	private SQLiteDatabase iDatabase;
	private DbEngineHelper iDbHelper;

	/**
	 * Constructor
	 * 
	 * @param aContext
	 *            Context
	 */
	public DbEngine(Context aContext) {
		this.iContext = aContext;
	}

	/**
	 * Opens database
	 * 
	 * @return Opened DB
	 * @throws SQLException
	 *             Exception if error
	 */
	public DbEngine open() throws SQLiteException {
		iDbHelper = new DbEngineHelper(iContext);
		iDatabase = iDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes DB
	 */
	public void close() {
		iDbHelper.close();
	}

	/**
	 * Create a new currency item
	 * 
	 * @param aCurrency
	 *            Currency structure
	 * @return Row id, otherwise return a -1 to indicate failure
	 */
	public long insertCurrency(Currency aCurrency) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CURRENCY_ID, aCurrency.getCurrencyId());
		initialValues.put(KEY_CURRENCY_NAME, aCurrency.getCurrencyName());
		initialValues.put(KEY_SELL, aCurrency.iSellCourse);
		initialValues.put(KEY_BUY, aCurrency.iBuyCourse);
		initialValues.put(KEY_SELL_DELTA, aCurrency.iSellDiff);
		initialValues.put(KEY_BUY_DELTA, aCurrency.iBuyDiff);

		long id = iDatabase.insert(CURRENCY_TABLE, null, initialValues);

		return id;
	}

	/**
	 * Create a new user data
	 * 
	 * @return Row id, otherwise return a -1 to indicate failure
	 */
	public long insertUserData(SettingsData aSettings, long aUpdateTime) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CITY_ID, aSettings.iCity);
		initialValues.put(KEY_BANK_ID, aSettings.iBank);
		initialValues.put(KEY_TIME_UPDATE, aUpdateTime);

		long id = iDatabase.insert(USER_TABLE, null, initialValues);

		return id;
	}

	/**
	 * Update currency item
	 * 
	 * @param aCurrency
	 *            Currency item to update
	 */
	public boolean updateCurrency(Currency aCurrency) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_CURRENCY_ID, aCurrency.getCurrencyId());
		newValues.put(KEY_CURRENCY_NAME, aCurrency.getCurrencyName());
		newValues.put(KEY_SELL, aCurrency.iSellCourse);
		newValues.put(KEY_BUY, aCurrency.iBuyCourse);
		newValues.put(KEY_SELL_DELTA, aCurrency.iSellDiff);
		newValues.put(KEY_BUY_DELTA, aCurrency.iBuyDiff);

		return iDatabase.update(CURRENCY_TABLE, newValues, KEY_CURRENCY_ID
				+ "=" + aCurrency.getCurrencyId(), null) > 0;
	}

	/**
	 * Update currency item
	 */
	public boolean updateUserData(SettingsData aSettings, long aUpdateTime) {
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_CITY_ID, aSettings.iCity);
		newValues.put(KEY_BANK_ID, aSettings.iBank);
		newValues.put(KEY_TIME_UPDATE, aUpdateTime);

		return iDatabase.update(USER_TABLE, newValues, KEY_CITY_ID + "="
				+ aSettings.iCity + " AND " + KEY_BANK_ID + "="
				+ aSettings.iBank, null) > 0;
	}

	/**
	 * Deletes currency item
	 */
	public boolean deleteCurrency(Currency aCurrency) {
		return iDatabase.delete(CURRENCY_TABLE, KEY_CURRENCY_ID + "="
				+ aCurrency.getCurrencyId(), null) > 0;
	}

	/**
	 * Deletes all rows in table Currency
	 * 
	 * @return True if succeed.
	 */
	public boolean deleteAll() {
		boolean result = iDatabase.delete(CURRENCY_TABLE, null, null) > 0;
		result &= iDatabase.delete(USER_TABLE, null, null) > 0;
		return result;
	}

	/**
	 * Gets the list of all currency items in the database
	 * 
	 * @return Cursor over all notes
	 */
	public List<Currency> getAll() {
		String[] columns = { KEY_ROWID, KEY_CURRENCY_ID, KEY_CURRENCY_NAME,
				KEY_SELL, KEY_BUY, KEY_SELL_DELTA, KEY_BUY_DELTA };

		Cursor cursor = iDatabase.query(CURRENCY_TABLE, columns, null, null,
				null, null, null);
		if (cursor.getCount() == 0) {
			return null;
		}

		cursor.moveToFirst();
		List<Currency> currencies = new ArrayList<Currency>();

		do {
			// getting item id
			int columnIndex = cursor.getColumnIndex(DbEngine.KEY_CURRENCY_ID);
			int id = cursor.getInt(columnIndex);
			currencies.add(getCurrency(id));
		} while (cursor.moveToNext());

		cursor.close();

		return currencies;
	}

	/**
	 * Gets all raw data
	 * 
	 * @return Cursor
	 */
	public Cursor getAllRawData() {
		String[] columns = { KEY_ROWID, KEY_CURRENCY_ID, KEY_CURRENCY_NAME,
				KEY_SELL, KEY_BUY, KEY_SELL_DELTA, KEY_BUY_DELTA };

		Cursor cursor = iDatabase.query(CURRENCY_TABLE, columns, null, null,
				null, null, null);
		return cursor;
	}

	/**
	 * Gets object Currency by id
	 * 
	 * @param aItemId
	 *            Id for search
	 * @return Filled object Currency or null
	 * @throws SQLException
	 */
	public Currency getCurrency(int aItemId) throws SQLException {
		String[] columns = { KEY_ROWID, KEY_CURRENCY_ID, KEY_CURRENCY_NAME,
				KEY_SELL, KEY_BUY, KEY_SELL_DELTA, KEY_BUY_DELTA };
		Cursor cursor = iDatabase.query(true, CURRENCY_TABLE, columns,
				KEY_CURRENCY_ID + "=" + aItemId, null, null, null, null, null);
		cursor.moveToFirst();

		if (cursor.getCount() == 0) {
			return null;
		}

		// getting currency name
		int columnIndex = cursor.getColumnIndex(DbEngine.KEY_CURRENCY_NAME);
		String currencyName = cursor.getString(columnIndex);

		// getting subject 1
		columnIndex = cursor.getColumnIndex(DbEngine.KEY_SELL);
		double sell = cursor.getDouble(columnIndex);

		// getting auditorium 1
		columnIndex = cursor.getColumnIndex(DbEngine.KEY_BUY);
		double buy = cursor.getDouble(columnIndex);

		// getting tutor name
		columnIndex = cursor.getColumnIndex(DbEngine.KEY_SELL_DELTA);
		double sellDelta = cursor.getDouble(columnIndex);

		// getting subject type
		columnIndex = cursor.getColumnIndex(DbEngine.KEY_BUY_DELTA);
		double buyDelta = cursor.getDouble(columnIndex);

		Currency currency = new Currency(currencyName);
		currency.iSellCourse = sell;
		currency.iBuyCourse = buy;
		currency.iSellDiff = sellDelta;
		currency.iBuyDiff = buyDelta;

		cursor.close();

		return currency;
	}

	/**
	 * Gets time of update
	 * 
	 * @param aSettings
	 *            Current user settings
	 * @return time of the last currency update
	 */
	public long getUpdateTime(SettingsData aSettings) {
		String[] columns = { KEY_ROWID, KEY_CITY_ID, KEY_BANK_ID,
				KEY_TIME_UPDATE };
		Cursor cursor = iDatabase.query(true, USER_TABLE, columns, KEY_CITY_ID
				+ "=" + aSettings.iCity + " AND " + KEY_BANK_ID + "="
				+ aSettings.iBank, null, null, null, null, null);
		cursor.moveToFirst();

		if (cursor.getCount() == 0) {
			return -1;
		}

		// getting currency name
		int columnIndex = cursor.getColumnIndex(DbEngine.KEY_TIME_UPDATE);
		long updateTime = cursor.getLong(columnIndex);

		return updateTime;
	}

}
