package com.jucrobile.blututhgames;

import java.util.Timer;

import android.os.Bundle;

import com.jucrobile.blututhgames.xsandos.BoardView;
import com.jucrobile.blututhgames.xsandos.UpdateTimer;

public class XsAndOsActivity extends MainActivity {

		@Override
	    public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);

	      setContentView(R.layout.activity_xs_and_os);

	      getActionBar().setDisplayHomeAsUpEnabled(true);
	      
	      Timer timer = new Timer();
	      UpdateTimer ut = new UpdateTimer();
	      ut.boardView = (BoardView)this.findViewById(R.id.bview);
	      ut.boardView.setApplication(this.mApplication);

	      timer.schedule( ut, 200, 200 );
	      
	      mApplication.setBoardView(ut.boardView);
	    }
	}