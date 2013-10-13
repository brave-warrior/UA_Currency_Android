/**
 * Copyright Khmelenko Lab
 * Author: Dmytro Khmelenko
 */
package com.gui.widget;

import java.util.Collections;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;

import com.data.Currency;
import com.data.DbEngine;
import com.data.SettingsData;
import com.gui.MainScreen;
import com.khmelenko.lab.currency.R;

/**
 * Handles widget actions
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class WidgetProvider extends AppWidgetProvider {

	/** Defines action for widget on receiving intent */
	public static final String ACTION_WIDGET_BTN_NEXT = "WIDGET_ACTION_NEXT";
	public static final String ACTION_WIDGET_BTN_PREV = "WIDGET_ACTION_PREV";

	// list of currencies
	private static List<Currency> iCurrencyList;

	private static int iCurrentCurrency = 0;

	/*
	 * @see
	 * android.appwidget.AppWidgetProvider#onUpdate(android.content.Context,
	 * android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context aContext, AppWidgetManager aAppWidgetManager,
			int[] aAppWidgetIds) {
		for (int i = 0; i < aAppWidgetIds.length; i++) {
			doWidgetUpdate(aContext, aAppWidgetManager, aAppWidgetIds);
		}
		super.onUpdate(aContext, aAppWidgetManager, aAppWidgetIds);
	}

	/*
	 * @see
	 * android.appwidget.AppWidgetProvider#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context aContext, Intent aIntent) {
		boolean actionNext = aIntent.getAction().equals(ACTION_WIDGET_BTN_NEXT);
		boolean actionPrev = aIntent.getAction().equals(ACTION_WIDGET_BTN_PREV);
		if (actionNext || actionPrev) {

			// if button next is clicked, increment number of subjects.
			// Otherwise, just make update of current context
			if (actionNext) {
				iCurrentCurrency++;
				iCurrencyList = getCurrencies(aContext);

				if (iCurrentCurrency >= iCurrencyList.size()) {
					iCurrentCurrency = 0;
				}
			} else if (actionPrev) {
				iCurrentCurrency--;
				iCurrencyList = getCurrencies(aContext);

				if (iCurrentCurrency < 0) {
					iCurrentCurrency = iCurrencyList.size() - 1;
				}
			}

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(aContext);
			ComponentName thisAppWidget = new ComponentName(
					aContext.getPackageName(), WidgetProvider.class.getName());
			int[] appWidgetIds = appWidgetManager
					.getAppWidgetIds(thisAppWidget);
			// initiate updating
			onUpdate(aContext, appWidgetManager, appWidgetIds);
		}
		super.onReceive(aContext, aIntent);
	}

	/**
	 * Does updating widget layout
	 * 
	 * @param aContext
	 *            Context
	 * @param aManager
	 *            Widget manager
	 * @param aWidgetIds
	 *            Widget ids
	 */
	private void doWidgetUpdate(Context aContext, AppWidgetManager aManager,
			int[] aWidgetIds) {

		RemoteViews remoteView = new RemoteViews(aContext.getPackageName(),
				R.layout.widget_layout);

		remoteView.setViewVisibility(R.id.widget_loading, View.GONE);

		// update city and bank
		updateWidgetHeader(aContext, remoteView);

		iCurrencyList = getCurrencies(aContext);
		if (iCurrencyList.size() > 0) {
			Currency currency = iCurrencyList.get(iCurrentCurrency);
			updateControls(aContext, remoteView, currency);
		} else {
			// no items for the selected day
			updateWithNoData(aContext, remoteView);
		}

		aManager.updateAppWidget(aWidgetIds, remoteView);
	}

	/**
	 * Updates widget header with the active city and bank
	 * 
	 * @param aContext
	 *            Context
	 * @param aViews
	 *            Remote views
	 */
	private void updateWidgetHeader(Context aContext, RemoteViews aViews) {
		String[] cities = aContext.getResources()
				.getStringArray(R.array.Cities);
		String[] banks = aContext.getResources().getStringArray(R.array.Banks);

		SettingsData appSettings = new SettingsData();
		appSettings.loadSettings(aContext);

		aViews.setTextViewText(R.id.header_city, cities[appSettings.iCity]);
		aViews.setTextViewText(R.id.header_bank, banks[appSettings.iBank]);
	}

	/**
	 * Updates widget with the label of empty data
	 * 
	 * @param aContext
	 *            Context
	 * @param aViews
	 *            Remote views
	 */
	private void updateWithNoData(Context aContext, RemoteViews aViews) {
		// show special label when no data is available
		aViews.setViewVisibility(R.id.widget_no_data, View.VISIBLE);
		
		// hide all controls when no data
		updateControlsVisibility(aViews, View.GONE);

		registerActionHandlers(aContext, aViews);
	}

	/**
	 * Updates controls visibility
	 * 
	 * @param aViews
	 *            Remote views
	 * @param aVisibility
	 *            Visibility
	 */
	private void updateControlsVisibility(RemoteViews aViews, int aVisibility) {
		// currency name and image
		aViews.setViewVisibility(R.id.currencyName, aVisibility);
		aViews.setViewVisibility(R.id.currencyImg, aVisibility);

		// labels
		aViews.setViewVisibility(R.id.buyLabel, aVisibility);
		aViews.setViewVisibility(R.id.sellLabel, aVisibility);

		// buy course
		aViews.setViewVisibility(R.id.buyCourse, aVisibility);
		aViews.setViewVisibility(R.id.buyDiff, aVisibility);

		// sell course
		aViews.setViewVisibility(R.id.sellCourse, aVisibility);
		aViews.setViewVisibility(R.id.sellDiff, aVisibility);
	}

	/**
	 * Updates controls with the new data
	 * 
	 * @param aContext
	 *            Context
	 * @param aViews
	 *            Remote views for updating
	 * @param aCurrentCurrency
	 *            Current currency
	 */
	private void updateControls(Context aContext, RemoteViews aViews,
			Currency aCurrentCurrency) {

		// update controls visibility
		aViews.setViewVisibility(R.id.widget_no_data, View.GONE);
		updateControlsVisibility(aViews, View.VISIBLE);
		
		// currency name and image
		aViews.setTextViewText(R.id.currencyName,
				aCurrentCurrency.getCurrencyName());
		aViews.setImageViewResource(R.id.currencyImg,
				aCurrentCurrency.getCurrencyImg());

		String dataFormat = "%.4f";
		// buy course
		String buyCourse = String.format(dataFormat,
				aCurrentCurrency.iBuyCourse);
		aViews.setTextViewText(R.id.buyCourse, buyCourse);

		String buyDiff = String.format(dataFormat, aCurrentCurrency.iBuyDiff);
		aViews.setTextViewText(R.id.buyDiff, buyDiff);

		// sell course
		String sellCourse = String.format(dataFormat,
				aCurrentCurrency.iSellCourse);
		aViews.setTextViewText(R.id.sellCourse, sellCourse);

		String sellDiff = String.format(dataFormat, aCurrentCurrency.iSellDiff);
		aViews.setTextViewText(R.id.sellDiff, sellDiff);

		// color of Sell delta
		int diffColor = Color.GRAY;
		if (aCurrentCurrency.iSellDiff > 0) {
			diffColor = Color.GREEN;
		} else if (aCurrentCurrency.iSellDiff < 0) {
			diffColor = Color.RED;
		}
		aViews.setTextColor(R.id.sellDiff, diffColor);

		// color of Buy delta
		diffColor = Color.GRAY;
		if (aCurrentCurrency.iBuyDiff > 0) {
			diffColor = Color.GREEN;
		} else if (aCurrentCurrency.iBuyDiff < 0) {
			diffColor = Color.RED;
		}
		aViews.setTextColor(R.id.buyDiff, diffColor);

		registerActionHandlers(aContext, aViews);
	}

	/**
	 * Gets all available currencies
	 * 
	 * @return Available currencies for the day
	 */
	private List<Currency> getCurrencies(Context aContext) {
		DbEngine dbEngine = new DbEngine(aContext);
		dbEngine.open();

		List<Currency> availableCurrencies = dbEngine.getAll();
		dbEngine.close();
		
		if (availableCurrencies == null) {
			availableCurrencies = Collections.emptyList();
		}
		
		return availableCurrencies;
	}

	/**
	 * Registers actions handlers
	 * 
	 * @param aContext
	 *            Context
	 * @param aViews
	 *            Remote views
	 */
	private void registerActionHandlers(Context aContext, RemoteViews aViews) {
		// action Next
		Intent intent = new Intent(aContext, WidgetProvider.class);
		intent.setAction(ACTION_WIDGET_BTN_NEXT);
		PendingIntent nextClickListener = PendingIntent.getBroadcast(aContext,
				0, intent, 0);
		aViews.setOnClickPendingIntent(R.id.next_currency, nextClickListener);

		// action Previous
		intent = new Intent(aContext, WidgetProvider.class);
		intent.setAction(ACTION_WIDGET_BTN_PREV);
		PendingIntent prevClickListener = PendingIntent.getBroadcast(aContext,
				0, intent, 0);
		aViews.setOnClickPendingIntent(R.id.prev_currency, prevClickListener);

		// handler on main click
		Intent activateMain = new Intent(aContext, MainScreen.class);
		PendingIntent mainTouchListener = PendingIntent.getActivity(aContext,
				0, activateMain, 0);
		aViews.setOnClickPendingIntent(R.id.widget_content, mainTouchListener);
	}
}
