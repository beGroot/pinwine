package com.pin.pinwine.activity;

import com.pin.pinwine.R;
import com.pin.pinwine.R.layout;
import com.pin.pinwine.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_page);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

}
