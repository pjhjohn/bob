package com.appspot.wecookbob.lib;

import java.util.TimerTask;

public class AsyncTimer extends TimerTask {
	private final int timeInterval;
	private final AsyncTimer.OnTime listener;
	public static interface OnTime {
		public void onTime();
	}
	public AsyncTimer(int timeInMilliSeconds, AsyncTimer.OnTime listener) {
		super();
		this.timeInterval = timeInMilliSeconds;
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(this.timeInterval);
			this.listener.onTime();			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}
