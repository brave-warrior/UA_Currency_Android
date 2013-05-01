/**
 * 
 */
package com.data;

/**
 * The structure of currency data
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class Currency {

	/** Undefined data */
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

	/** Short currency name */
	private String iCurrencyName;

	/** The ID of resources of the currency image */
	private int iCurrencyImg = UNDEFINED;

	/** Currency sell course */
	public double iSellCourse;

	/** Currency buy course */
	public double iBuyCourse;

	/** Currency sale difference */
	public double iSellDiff;

	/** Currency buy difference */
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
	 * Gets currency image ID
	 * 
	 * @return Currency image ID
	 */
	public int getCurrencyImg() {
		return iCurrencyImg;
	}
}
