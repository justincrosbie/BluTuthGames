package com.jucrobile.blututhgames;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RockPaperScissorsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rock_paper_scissors);

	      getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rock_paper_scissors, menu);
		return true;
	}

}
