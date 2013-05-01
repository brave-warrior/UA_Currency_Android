/**
 * 
 */
package com.gui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.data.Currency;
import com.data.DbEngine;
import com.khmelenko.lab.currency.R;

/**
 * The adapter for the list on main screen
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class CurrencyAdapter extends CursorAdapter {

	private Context iContext;

	/**
	 * Constructor
	 * 
	 * @param aContext
	 *            Context
	 * @param aCursor
	 *            Cursor
	 */
	public CurrencyAdapter(Context aContext, Cursor aCursor) {
		super(aContext, aCursor);
		iContext = aContext;
	}

	/*
	 * @see android.widget.CursorAdapter#bindView(android.view.View,
	 * android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View aView, Context aContext, Cursor aCursor) {

		Currency currency = getCurrency(aCursor);

		// currency name
		TextView currencyName = (TextView) aView
				.findViewById(R.id.currencyName);
		currencyName.setText(currency.getCurrencyName());

		// image
		ImageView currencyImg = (ImageView) aView
				.findViewById(R.id.currencyImg);
		int currencyImgDrawable = currency.getCurrencyImg();
		if (currencyImgDrawable != Currency.UNDEFINED) {
			Drawable currencyDrawable = iContext.getResources().getDrawable(
					currencyImgDrawable);
			currencyImg.setImageDrawable(currencyDrawable);
			currencyImg.setVisibility(View.VISIBLE);
		} else {
			currencyImg.setVisibility(View.INVISIBLE);
		}

		// sell
		TextView sellCourse = (TextView) aView.findViewById(R.id.sellCourse);
		sellCourse.setText(String.format("%.4f", currency.iSellCourse));

		// sell delta
		TextView sellDiff = (TextView) aView.findViewById(R.id.sellDiff);
		sellDiff.setText(String.format("%.4f", currency.iSellDiff));

		// buy
		TextView buyCourse = (TextView) aView.findViewById(R.id.buyCourse);
		buyCourse.setText(String.format("%.4f", currency.iBuyCourse));

		// buy delta
		TextView buyDiff = (TextView) aView.findViewById(R.id.buyDiff);
		buyDiff.setText(String.format("%.4f", currency.iBuyDiff));

		// color of Sell delta
		int diffColor = Color.GRAY;
		if (currency.iSellDiff > 0) {
			diffColor = Color.GREEN;
		} else if (currency.iSellDiff < 0) {
			diffColor = Color.RED;
		}
		sellDiff.setTextColor(diffColor);

		// color of Buy delta
		diffColor = Color.GRAY;
		if (currency.iBuyDiff > 0) {
			diffColor = Color.GREEN;
		} else if (currency.iBuyDiff < 0) {
			diffColor = Color.RED;
		}
		buyDiff.setTextColor(diffColor);
	}

	/*
	 * @see android.widget.CursorAdapter#newView(android.content.Context,
	 * android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context aContext, Cursor aCursor, ViewGroup aParent) {
		LayoutInflater inflater = LayoutInflater.from(aParent.getContext());
		View retView = inflater.inflate(R.layout.list_item, aParent, false);
		return retView;

	}

	/**
	 * Gets currency from cursor
	 * 
	 * @param aCursor
	 *            Cursor
	 * @return Currency struct
	 */
	private Currency getCurrency(Cursor aCursor) {
		// getting currency name
		int columnIndex = aCursor.getColumnIndex(DbEngine.KEY_CURRENCY_NAME);
		String currencyName = aCursor.getString(columnIndex);

		// getting sell
		columnIndex = aCursor.getColumnIndex(DbEngine.KEY_SELL);
		double sell = aCursor.getDouble(columnIndex);

		// getting buy
		columnIndex = aCursor.getColumnIndex(DbEngine.KEY_BUY);
		double buy = aCursor.getDouble(columnIndex);

		// getting sell delta
		columnIndex = aCursor.getColumnIndex(DbEngine.KEY_SELL_DELTA);
		double sellDelta = aCursor.getDouble(columnIndex);

		// getting buy delta
		columnIndex = aCursor.getColumnIndex(DbEngine.KEY_BUY_DELTA);
		double buyDelta = aCursor.getDouble(columnIndex);

		Currency currency = new Currency(currencyName);
		currency.iSellCourse = sell;
		currency.iBuyCourse = buy;
		currency.iSellDiff = sellDelta;
		currency.iBuyDiff = buyDelta;

		return currency;
	}

	/**
	 * Holder class
	 * 
	 * @author Dmytro Khmelenko
	 * 
	 */
	static class ViewHolder {
		public TextView iCurrencyName;
		public ImageView iCurrencyImg;
		public TextView iSellCourse;
		public TextView iSellDiff;
		public TextView iBuyCourse;
		public TextView iBuyDiff;
	}

}
