package main.java.org.app.entity;

import main.java.org.app.entity.Error;

public class Message {
	private String channel;
	private String tranCode;
	private String tranDate;
	private String retCode;
	private Error[] error;
	private Data[] data;
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTranCode() {
		return tranCode;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public Error[] getError() {
		return error;
	}
	public void setError(Error[] error) {
		this.error = error;
	}
	public Data[] getData() {
		return data;
	}
	public void setData(Data[] data) {
		this.data = data;
	}
	
	

}
