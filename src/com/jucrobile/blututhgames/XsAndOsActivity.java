package com.jucrobile.blututhgames;

import java.util.Timer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jucrobile.blututhgames.xsandos.BoardView;
import com.jucrobile.blututhgames.xsandos.UpdateTimer;

public class XsAndOsActivity extends MainActivity {

    private static final String TAG = "XsAndOsActivity";
    private BoardView mBoardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_xs_and_os);

      getActionBar().setDisplayHomeAsUpEnabled(true);
      mApplication.setActionBar(getActionBar());
      
      Timer timer = new Timer();
      UpdateTimer ut = new UpdateTimer();
      mBoardView = (BoardView)this.findViewById(R.id.bview);
      ut.boardView = mBoardView;
      ut.boardView.setService(mService);

      timer.schedule( ut, 200, 200 );
      
      ((BluTuthApplication)getApplication()).setBoardView(ut.boardView);
    }


	protected void hookupService(BluTuthService service) {
		mBoardView.setService(service);
	}

    @Override
    public synchronized void onResume() {
        super.onResume();
        bindService(new Intent(this, BluTuthService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }
}