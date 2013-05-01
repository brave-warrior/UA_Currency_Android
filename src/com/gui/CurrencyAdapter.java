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
 * @author Dmytro Khmelenko
 *
 */
public class CurrencyAdapter extends CursorAdapter {

	private Context iContext;
	
	/**
	 * Constructor
	 * @param aContext Context
	 * @param aCursor Cursor
	 */
	public CurrencyAdapter(Context aContext, Cursor aCursor) {
		super(aContext, aCursor);
		iContext = aContext;
	}
	
	/**
	 * Constructor
	 * @param aContext
	 * @param aTextViewResourceId
	 * @param aObjects
	 */
//	public CurrencyAdapter(Context aContext, int aTextViewResourceId,
//			List<Currency> aObjects) {
//		super(aContext, aTextViewResourceId, aObjects);
//		iContext = aContext;
//		iItems = aObjects;
//	}

	/*
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View aView, Context aContext, Cursor aCursor) {

		Currency currency = getCurrency(aCursor);
		
		// currency name
		TextView currencyName = (TextView) aView
				.findViewById(R.id.currencyName);
		currencyName.setText(currency.getCurrencyName());

		// image
		ImageView currencyImg = (ImageView) aView.findViewById(R.id.currencyImg);
		int currencyImgDrawable = currency.getCurrencyImg();
		if (currencyImgDrawable != Currency.UNDEFINED) {
			Drawable currencyDrawable = iContext.getResources().getDrawable(currencyImgDrawable);
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
	 * Helper class
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

//    /*
//     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
//     */
//    @Override
//    public View getView(int aPosition, View aConvertView, ViewGroup aParent) {
//        // ViewHolder буферизирует оценку различных полей шаблона элемента
//
//        ViewHolder holder;
//        View rowView = aConvertView;
//        if (rowView == null) {
//			LayoutInflater inflater = (LayoutInflater) iContext
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            rowView = inflater.inflate(R.layout.list_item, null, true);
//            holder = new ViewHolder();
//            holder.iCurrencyName = (TextView) rowView.findViewById(R.id.currencyName);
//            holder.iCurrencyImg = (ImageView) rowView.findViewById(R.id.currencyImg);
//            holder.iSellCourse = (TextView) rowView.findViewById(R.id.sellCourse);
//            holder.iSellDiff = (TextView) rowView.findViewById(R.id.sellDiff);
//            holder.iBuyCourse = (TextView) rowView.findViewById(R.id.buyCourse);
//            holder.iBuyDiff = (TextView) rowView.findViewById(R.id.buyDiff);
//            rowView.setTag(holder);
//        } else {
//            holder = (ViewHolder) rowView.getTag();
//        }
//
//        // Update view holder according to the currency data
//        Currency currency = iItems.get(aPosition);
//        holder.iCurrencyName.setText(currency.getCurrencyName());
//        holder.iBuyCourse.setText(String.valueOf(currency.iBuyCourse));
//        holder.iBuyDiff.setText(String.valueOf(currency.iBuyDiff));
//        holder.iSellCourse.setText(String.valueOf(currency.iSellCourse));
//        holder.iSellDiff.setText(String.valueOf(currency.iSellDiff));
//        
//        // color of Sell delta
//        int diffColor = Color.GRAY;
//        if (currency.iSellDiff > 0) {
//        	diffColor = Color.GREEN;
//        } else if (currency.iSellDiff < 0) {
//        	diffColor = Color.RED;
//        }
//        holder.iSellDiff.setTextColor(diffColor);
//        
//        // color of Buy delta
//        diffColor = Color.GRAY;
//        if (currency.iBuyDiff > 0) {
//        	diffColor = Color.GREEN;
//        } else if (currency.iBuyDiff < 0) {
//        	diffColor = Color.RED;
//        }
//        holder.iBuyDiff.setTextColor(diffColor);
//        
//        // update currency image
//        Drawable currencyImg = iContext.getResources().getDrawable(currency.getCurrencyImg());
//        holder.iCurrencyImg.setImageDrawable(currencyImg);
//        
//        return rowView;
//    }


    
}
