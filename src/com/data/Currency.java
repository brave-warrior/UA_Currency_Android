/**
 * 
 */
package com.data;


/**
 * @author Dmytro Khmelenko
 * 
 */
public class Currency {

	public static final int UNDEFINED = -1;
	
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

	private int iCurrencyId = UNDEFINED;

	private String iCurrencyName;

	private int iCurrencyImg = UNDEFINED;

	public double iSellCourse;

	public double iBuyCourse;

	public double iSellDiff;

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
	 * @return
	 */
	public int getCurrencyId() {
		return iCurrencyId;
	}

	/**
	 * Gets currency img
	 * 
	 * @return
	 */
	public int getCurrencyImg() {
		return iCurrencyImg;
	}
}
