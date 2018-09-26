package com.moqbus.app.service.mqtt;

public abstract class CmdCallback {

	public final static Long NOT_EXPIRED = Long.MAX_VALUE;
	
	public abstract boolean condition(String topic, byte[] data);
	public abstract void execute(String topic, byte[] data);
	

	public boolean once() {
		return true;
	}

	public Long expireTime() {
		return NOT_EXPIRED;
	}

}
