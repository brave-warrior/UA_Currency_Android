/**
 * Copyright Khmelenko Lab
 * Author: Dmytro Khmelenko
 */
package com.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.DateTimeUtils;
import utils.XmlUtils;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.Currency;
import com.data.DbEngine;
import com.data.SettingsData;
import com.data.Tables;
import com.khmelenko.lab.currency.R;

/**
 * Application Main screen
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class MainScreen extends ListActivity {

	/** Resource URL */
	private final static String RESOURCE_URL = "http://cashexchange.com.ua/XmlApi.ashx";
	
	private static final int SETTINGS_REQUEST_CODE = 1;
	
	// controls
	private ListView iList;
	private TextView iUpdateTime;
	private ProgressDialog iProgressDialog;
	
	private TextView iSaleLabel;
	private TextView iBuyLabel;
	private View iLabelsSeparator;
	
	private CursorAdapter iAdapter;
	
	/** Current application settings */
	private SettingsData iCurrentSettings;
	
	/** Database engine */
	private DbEngine iDbEngine;
	
	/** Asynchronous web task */
	private WebAsyncTask iTask;
	
	/*
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		
		iDbEngine = new DbEngine(this);
		iDbEngine.open();
		
		iList = (ListView) findViewById(android.R.id.list);
		iUpdateTime = (TextView) findViewById(R.id.updateTime);
		
		iSaleLabel = (TextView) findViewById(R.id.saleLabel);
		iBuyLabel = (TextView) findViewById(R.id.buyLabel);
		iLabelsSeparator = findViewById(R.id.labelSeparator);

		
		iAdapter = new CurrencyAdapter(this, iDbEngine.getAllRawData());
        iList.setAdapter(iAdapter);
		
		iTask = new WebAsyncTask();
		
		iProgressDialog = new ProgressDialog(this);
		iProgressDialog.setMessage(getString(R.string.loading_data));
		iProgressDialog.setOnCancelListener(new Dialog.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface aDialog) {
				iTask.cancel(true);
			}
		});
		
		iCurrentSettings = new SettingsData();
		iCurrentSettings.loadSettings(this);
	}

	/**
	 * Sets visibility for the help controls
	 * @param aVisibility Visibility
	 */
	private void setHelpControlsVisibility(int aVisibility) {
		iSaleLabel.setVisibility(aVisibility);
		iBuyLabel.setVisibility(aVisibility);
		iLabelsSeparator.setVisibility(aVisibility);
	}
	
	/*
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		iCurrentSettings.loadSettings(this);
		updateTitle();
		
		long updateTime = iDbEngine.getUpdateTime(iCurrentSettings);
		if (updateTime > 0) {
			iUpdateTime.setVisibility(View.VISIBLE);
			setUpdateTime(updateTime);
			setHelpControlsVisibility(View.VISIBLE);
		} else {
			iUpdateTime.setVisibility(View.GONE);
			setHelpControlsVisibility(View.GONE);
		}
		
		if (isDataOld()) {
			doUpdate();
		}
	}
	
	@Override
	protected void onDestroy() {
		iDbEngine.close();
		super.onDestroy();
	}
	
	/*
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem aItem) {
		switch (aItem.getItemId()) {
		case R.id.menu_update:
			doUpdate();
			break;
		case R.id.menu_settings:
			startActivityForResult(new Intent(this, SettingsScreen.class), SETTINGS_REQUEST_CODE);
			break;
		case R.id.menu_about:
			startActivity(new Intent(this, AboutScreen.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(aItem);
	}
	
	/*
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int aRequestCode, int aResultCode,
			Intent aData) {
		super.onActivityResult(aRequestCode, aResultCode, aData);
		switch(aRequestCode) {
		case SETTINGS_REQUEST_CODE:
			if (aResultCode == RESULT_OK) {
				SettingsData prevSettings = iCurrentSettings.clone();
				iCurrentSettings.loadSettings(this);
				updateTitle();
				if (!prevSettings.equals(iCurrentSettings)) {
					// clear previous data before update
					iDbEngine.deleteAll();
					iAdapter.changeCursor(iDbEngine.getAllRawData());
					iUpdateTime.setVisibility(View.GONE);
					setHelpControlsVisibility(View.GONE);
					doUpdate();
				}
			}
			break;
		}
	}
	
	/**
	 * Sets time of update
	 * 
	 * @param aUpdateTime
	 *            Time of update
	 */
	private void setUpdateTime(long aUpdateTime) {
		String currentDate = DateTimeUtils.getCurrentTimeString(
				getApplicationContext(), aUpdateTime);
		String updated = getString(R.string.updateTime, currentDate);
		iUpdateTime.setText(updated);
		iUpdateTime.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Checks current day and the date of last update
	 * @return True, if current day is different than day of update. Otherwise, false
	 */
	private boolean isDataOld() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		int currDay = calendar.get(Calendar.DAY_OF_YEAR);
		
		// settings should be initialized here
		long updatedDate = iDbEngine.getUpdateTime(iCurrentSettings);
		calendar.setTimeInMillis(updatedDate);
		int updatedDay = calendar.get(Calendar.DAY_OF_YEAR);
		
		return currDay != updatedDay;
	}
	
	/**
	 * Web async task
	 * 
	 * @author Dmytro Khmelenko
	 * 
	 */
	private class WebAsyncTask extends AsyncTask<URL, Void, String> {

		/*
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(URL... aUrl) {
			String result = "";
			if (isNetworkAvailable()) {
				result = postData(aUrl[0]);
			} else {
				result = getString(R.string.no_connection);
			}
			return result;
		}

		/*
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/*
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String aResult) {
			super.onPostExecute(aResult);
			
			Document doc = XmlUtils.getDomElement(aResult); // getting DOM element
			
			if (doc != null) {
			 
				NodeList nl = doc.getElementsByTagName("Element");

				iDbEngine.deleteAll();
				
				// looping through all item nodes <item>
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
					String currencyName = XmlUtils.getValue(e, "Currency");
					String buy = XmlUtils.getValue(e, "Buy");
					String sell = XmlUtils.getValue(e, "Sale");
					String buyDelta = XmlUtils.getValue(e, "BuyDelta");
					String sellDelta = XmlUtils.getValue(e, "SaleDelta");

					Currency currency = new Currency(currencyName);
					currency.iBuyCourse = Double.valueOf(buy);
					currency.iBuyDiff = Double.valueOf(buyDelta);
					currency.iSellCourse = Double.valueOf(sell);
					currency.iSellDiff = Double.valueOf(sellDelta);
					
					iDbEngine.insertCurrency(currency);
				}
				iAdapter.changeCursor(iDbEngine.getAllRawData());
				Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				setUpdateTime(calendar.getTimeInMillis());
				setHelpControlsVisibility(View.VISIBLE);
				
				iDbEngine.insertUserData(iCurrentSettings, calendar.getTimeInMillis());
				
				if (iAdapter.isEmpty()) {
					TextView empty = (TextView) getListView().getEmptyView();
					empty.setText(R.string.no_data);
					setHelpControlsVisibility(View.GONE);
				}
			
			} else {
				// if list is empty, show the error on the page
				if (iAdapter.isEmpty()) {
					TextView empty = (TextView) getListView().getEmptyView();
					empty.setText(aResult);
					iUpdateTime.setVisibility(View.GONE);
					setHelpControlsVisibility(View.GONE);
				} else {
					Toast.makeText(getApplicationContext(), aResult, Toast.LENGTH_LONG).show();
				}
			}

			iProgressDialog.dismiss();
		}

		/**
		 * Executes HTTP post request
		 * 
		 * @param aUrl
		 *            URL for request
		 * @return Result
		 */
		public String postData(URL aUrl) {
			String result = "";
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost("http://resources.finance.ua/ua/public/currency-cash.xml");
			HttpPost httppost = new HttpPost(aUrl.toString());
			httppost.setHeader("Connection", "keep-alive");

			try {
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				int statusCode = response.getStatusLine().getStatusCode();
				
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					String xml = EntityUtils.toString(entity);
					result = xml;
				} else {
					result = response.getStatusLine().getReasonPhrase();
				}
				
			} catch (ClientProtocolException e) {
				String ex = e.toString();
				result = ex;
			} catch (IOException e) {
				String ex = e.toString();
				result = ex;
			}
			return result;
		}
		
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) 
	      getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // if no network is available networkInfo will be null
	    // otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	} 
	
	/**
	 * Updates screen title
	 */
	private void updateTitle() {
		String[] cities = getResources().getStringArray(R.array.Cities);
		String[] banks = getResources().getStringArray(R.array.Banks);

		String title = cities[iCurrentSettings.iCity] + ", "
				+ banks[iCurrentSettings.iBank];
		setTitle(title);
	}
	
	/**
	 * Does request to data update
	 */
	private void doUpdate() {
		try {
			TextView empty = (TextView) getListView().getEmptyView();
			empty.setText("");
			
			iTask = new WebAsyncTask();
			String request = prepareRequest();
			URL url = new URL(request);
			iTask.execute(url);
			
			if (!iProgressDialog.isShowing()) {
	            iProgressDialog.show();
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prepares request string
	 * @return request string
	 */
	private String prepareRequest() {
		String requestString = RESOURCE_URL;
		
		boolean districtSet = false;
		String[] district = Tables.CITIES.get(iCurrentSettings.iCity);
		if (district != null) {
			if (district[0].length() > 0 && district[1].length() > 0) {
				requestString += "?district=" + district[0] + "&city=" + district[1];
				districtSet = true;
			}
		}
		
		String bank = Tables.BANKS.get(iCurrentSettings.iBank);
		if (bank != null && bank.length() > 0) {
			char firstChar = districtSet ? '&' : '?';
			requestString += firstChar + "company=" + bank;
		}
		
		return requestString;
		
	}
}
