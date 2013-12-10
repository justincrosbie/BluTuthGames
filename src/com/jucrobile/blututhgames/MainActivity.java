/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jucrobile.blututhgames;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jucrobile.blututhgames.blututh.DeviceListActivity;

/**
 * This is the main Activity that displays the current chat session.
 */
public class MainActivity extends Activity implements OnClickListener {

    String tests[] = { "XsAndOs", "RockPaperScissors" };

    protected BluTuthApplication mApplication;
    protected BluTuthService mService;
    
    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean D = true;


    private Button b1;
    private Button b2;
    
    public BluTuthService getService() {
		return mService;
	}

	public void setService(BluTuthService mService) {
		this.mService = mService;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mApplication = (BluTuthApplication)getApplication();
        
        mApplication.setActionBar(getActionBar());
        
        if(D) Log.e(TAG, "+++ ON CREATE +++");

	    setContentView(R.layout.activity_main);

		b1 = (Button) this.findViewById(R.id.button1);
		b1.setOnClickListener(this);
		b2 = (Button) this.findViewById(R.id.button2);
		b2.setOnClickListener(this);
		
		
    }

	public void onClick(View v) {
		if ( v == b1 ) {
			Log.d(TAG, "Playing XsAndOs");
            Intent intent = new Intent(this, com.jucrobile.blututhgames.XsAndOsActivity.class);
            startActivity(intent);
		}
		else if ( v == b2 ) {
			Log.d(TAG, "Playing RockPaperScissors");
            Intent intent = new Intent(this, com.jucrobile.blututhgames.RockPaperScissorsActivity.class);
            startActivity(intent);
		} else {
			Log.d(TAG, "Playing FA");
		}
	}
    
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
    }
    
    protected void hookupService(BluTuthService service) {
    	
    }

    protected ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
          mService = (((BluTuthService.MyBinder) binder).getService());
          Toast.makeText(MainActivity.this, "Service Connected", Toast.LENGTH_SHORT)
              .show();
          
          hookupService(mService);

          startBlueTooth();
        }

        public void onServiceDisconnected(ComponentName className) {
        	mService = null;
            Toast.makeText(MainActivity.this, "Service Disconnected", Toast.LENGTH_SHORT)
            .show();
        }
      };
    
    public void startBlueTooth() {

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, mService.REQUEST_ENABLE_BT);
        } 

		if ( mService.initBlueToothAdapter() == null ) {
            finish();
		}
        
		mService.startBlueTooth();
    }
    
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (BluetoothAdapter.getDefaultAdapter().getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.secure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, mService.REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        case R.id.insecure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, mService.REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }
    
    
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");
        
//        if ( mApplication.getService() == null ) {
            if(D) Log.e(TAG, "+ Binding to Service +");
            bindService(new Intent(this, BluTuthService.class), mConnection,
                    Context.BIND_AUTO_CREATE);
//        } else {
//            if(D) Log.e(TAG, "+ Already bound to Service +");
//        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
//        if (mService != null) mService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	mService.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

}
