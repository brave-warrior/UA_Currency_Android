/**
 * 
 */
package com.gui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.khmelenko.lab.currency.R;

/**
 * @author Dmytro Khmelenko
 *
 */
public class AboutScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		// TODO Banner place
//		ImageView bannerView = (ImageView) findViewById(R.id.bannerView);
//		bannerView.setBackgroundColor(Color.WHITE);
		
		TextView version = (TextView) findViewById(R.id.version);
		version.append(MainScreen.VERSION);
	
	}
}
