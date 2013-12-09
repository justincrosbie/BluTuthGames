package com.jucrobile.blututhgames;

import android.app.Application;

import com.jucrobile.blututhgames.blututh.BluetoothChatService;
import com.jucrobile.blututhgames.xsandos.BoardView;

public class BluTuthApplication extends Application {

    public BluetoothChatService mChatService = null;
    public BoardView mBoardView;
	
    public BoardView getBoardView() {
		return mBoardView;
	}

	public void setBoardView(BoardView mBoardView) {
		this.mBoardView = mBoardView;
	}

	public BluetoothChatService getChatService() {

		if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
            	mChatService.start();
            }
        }
		return mChatService;
	}

	public void setChatService(BluetoothChatService mChatService) {
		this.mChatService = mChatService;
	}

	
	@Override
    public void onCreate()
    {
      super.onCreate();
       
    }
}
