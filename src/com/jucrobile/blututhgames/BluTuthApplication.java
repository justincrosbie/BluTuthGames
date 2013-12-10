package com.jucrobile.blututhgames;

import android.app.ActionBar;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.jucrobile.blututhgames.xsandos.BoardView;

public class BluTuthApplication extends Application {

    private static final String TAG = "BluTuthApplication";
    private static final boolean D = true;

	private ActionBar mActionBar;
    public BoardView mBoardView;
    
    private String mConnectedDeviceName = null;

    public ActionBar getActionBar() {
		return mActionBar;
	}

	public void setActionBar(ActionBar mActionBar) {
		this.mActionBar = mActionBar;
	}
    public BoardView getBoardView() {
		return mBoardView;
	}

	public void setBoardView(BoardView mBoardView) {
		this.mBoardView = mBoardView;
	}

	Handler.Callback realCallback = null;
//	Handler handler = new Handler() {
//	    public void handleMessage(android.os.Message msg) {
//	        if (realCallback != null) {
//	            realCallback.handleMessage(msg);
//	        }
//	    };
//	};
	public Handler getHandler() {
	    return mHandler;
	}
	public void setCallBack(Handler.Callback callback) {
	    this.realCallback = callback;
	}
	
    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }

    // The Handler that gets information back from the BluetoothChatService
    protected final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluTuthService.MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluTuthService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    break;
                case BluTuthService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BluTuthService.STATE_LISTEN:
                case BluTuthService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case BluTuthService.MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                break;
            case BluTuthService.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                if ( getBoardView() != null ) {
                	getBoardView().drawOpponentsPiece(readMessage);
                }
                Toast.makeText(getApplicationContext(), "Player move: "
                        + readMessage, Toast.LENGTH_SHORT).show();
                break;
            case BluTuthService.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(BluTuthService.DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case BluTuthService.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(BluTuthService.TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
	
	@Override
    public void onCreate()
    {
      super.onCreate();
       
    }
}
