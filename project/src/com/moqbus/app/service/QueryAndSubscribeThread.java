package com.moqbus.app.service;

import com.moqbus.app.service.mqtt.ControlProxy;

public class QueryAndSubscribeThread extends Thread {

	private static ChildThreadExceptionHandler exceptionHandler;

    static {
        exceptionHandler = new ChildThreadExceptionHandler();
    }

    public static class ChildThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(String.format("handle exception in child thread. %s", e));
        }
    }

	@Override
	public void run() {
		Thread.currentThread().setUncaughtExceptionHandler(exceptionHandler);

		System.out.println("QueryAndSubscribeThread run before");
		
		ControlProxy.queryAndSubscribeService();
		
		System.out.println("QueryAndSubscribeThread run after");
	}
	
}