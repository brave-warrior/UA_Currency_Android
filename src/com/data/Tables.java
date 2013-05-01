/**
 * 
 */
package com.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.khmelenko.lab.currency.R;

/**
 * @author Dmytro Khmelenko
 *
 */
public class Tables {
	
	public final static Map<Integer, String[]> CITIES = Collections
			.unmodifiableMap(

			/*
			 * This is really an anonymous inner class - a sub-class of
			 * j.u.HashMap
			 */
			new HashMap<Integer, String[]>() {
				{
					put(0, new String[] { "", "" });
					put(1, new String[] { "Vinnytsia", "vinnista" });
					put(2, new String[] { "Dnipropetrovsk", "Dnepropetrovsk" });
					put(3, new String[] { "Donetsk", "donestk" });
					put(4, new String[] { "Zhytomyr", "zhitomir" });
					put(5, new String[] { "Zaporizhzhya", "zaporozhe" });
					put(6, new String[] { "Ivano-Frankivsk", "ivano-frankovsk" });
					put(7, new String[] { "Kyiv", "kiev" });
					put(8, new String[] { "Kirovohrad", "kirovograd" });
					put(9, new String[] { "Luhansk", "lugansk" });
					put(10, new String[] { "Volyn", "lustk" });
					put(11, new String[] { "Lviv", "lvov" });
					put(12, new String[] { "Mykolaiv", "nikolaev" });
					put(13, new String[] { "Odesa", "odessa" });
					put(14, new String[] { "Poltavskaja", "poltava" });
					put(15, new String[] { "Rivne", "rovno" });
					put(16, new String[] { "Crimea", "simferopol" });
					put(17, new String[] { "Sumy", "sumy" });
					put(18, new String[] { "Tern", "ternopol" });
					put(19, new String[] { "Carpathian", "uzhgorod" });
					put(20, new String[] { "Kharkiv", "kharkov" });
					put(21, new String[] { "Kherson", "kherson" });
					put(22, new String[] { "Khmelnytskyi", "khmelnistkiy" });
					put(23, new String[] { "Cherkasy", "cherkassy" });
					put(24, new String[] { "Chernihiv", "chernigov" });
					put(25, new String[] { "Chernivtsi", "chernovsty" });
				}

			});

	public final static Map<Integer, String> BANKS = Collections
			.unmodifiableMap(

			/*
			 * This is really an anonymous inner class - a sub-class of
			 * j.u.HashMap
			 */
			new HashMap<Integer, String>() {
				{
					put(0, "");
					put(1, "indexbank");
					put(2, "finance");
					put(3, "delta");
					put(4, "aval");
					put(5, "pumb");
					put(6, "vab");
					put(7, "imexbank");
					put(8, "alpha-bank");
					put(9, "otp");
					put(10, "finance_iniciativa");
					put(11, "pib");
					put(12, "pivdenny");
					put(13, "creditprombank");
					put(14, "piraeusbank");
					put(15, "unicredit");
					put(16, "forum");
					put(17, "xcitybank");
					put(18, "pivdencombank");
					put(19, "rodovidbank");
					put(20, "mercury-bank");
					put(21, "Tavrika");
					put(22, "a-bank");
					put(23, "activebank");
				}

			});
	
	public final static Map<String, Integer> CURRENCY_ID = Collections
			.unmodifiableMap(

			/*
			 * This is really an anonymous inner class - a sub-class of
			 * j.u.HashMap
			 */
			new HashMap<String, Integer>() {
				{
					put("USD", 0);
					put("EUR", 1);
					put("RUB", 2);
					put("GBP", 3);
					put("AUD", 4);
					put("GEL", 5);
					put("DKK", 6);
					put("CAD", 7);
					put("MDL", 8);
					put("NOK", 9);
					put("PLN", 10);
					put("CZK", 11);
					put("SEK", 12);
					put("CHF", 13);
					put("JPY", 14);
					put("EEK", 15);
				}

			});
	
	public final static Map<Integer, Integer> FLAGS = Collections
			.unmodifiableMap(

			/*
			 * This is really an anonymous inner class - a sub-class of
			 * j.u.HashMap
			 */
			new HashMap<Integer, Integer>() {
				{
					put(0, R.drawable.usd);
					put(1, R.drawable.eur);
					put(2, R.drawable.rub);
					put(3, R.drawable.gbp);
					put(4, R.drawable.aud);
					put(5, R.drawable.gel);
					put(6, R.drawable.dkk);
					put(7, R.drawable.cad);
					put(8, R.drawable.mdl);
					put(9, R.drawable.nok);
					put(10, R.drawable.pln);
					put(11, R.drawable.czk);
					put(12, R.drawable.sek);
					put(13, R.drawable.chf);
					put(14, R.drawable.jpy);
					put(15, R.drawable.eek);
				}

			});
	
}
