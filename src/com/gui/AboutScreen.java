/**
 * 
 */
package com.gui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.khmelenko.lab.currency.R;

/**
 * About activity
 * 
 * @author Dmytro Khmelenko
 * 
 */
public class AboutScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		TextView version = (TextView) findViewById(R.id.version);
		version.append(MainScreen.VERSION);

	}
}
