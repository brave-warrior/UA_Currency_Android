/**
 * Copyright Khmelenko Lab
 * Author: Dmytro Khmelenko
 */
package com.data;

/**
 * Contains information about currency
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class Currency {

	public static final int UNDEFINED = -1;

	/**
	 * Constructor
	 * 
	 * @param aCurrencyName
	 *            Currency name
	 */
	public Currency(String aCurrencyName) {
		iCurrencyName = aCurrencyName;

		Integer currencyId = Tables.CURRENCY_ID.get(aCurrencyName);
		if (currencyId != null) {
			iCurrencyId = currencyId;
		}

		Integer currencyImg = Tables.FLAGS.get(iCurrencyId);
		if (currencyImg != null) {
			iCurrencyImg = currencyImg;
		}
	}

	/** Currency ID */
	private int iCurrencyId = UNDEFINED;

	/** Currency name */
	private String iCurrencyName;

	/** Image of the currency */
	private int iCurrencyImg = UNDEFINED;

	/** Currency sell course */
	public double iSellCourse;

	/** Currency buy course */
	public double iBuyCourse;

	/** Delta of the sell course */
	public double iSellDiff;

	/** Delta of the buy course */
	public double iBuyDiff;

	/**
	 * Gets currency name
	 * 
	 * @return Currency name
	 */
	public String getCurrencyName() {
		return iCurrencyName;
	}

	/**
	 * Gets currency id
	 * 
	 * @return Currency ID
	 */
	public int getCurrencyId() {
		return iCurrencyId;
	}

	/**
	 * Gets currency img
	 * 
	 * @return ID of the currency image
	 */
	public int getCurrencyImg() {
		return iCurrencyImg;
	}
}
