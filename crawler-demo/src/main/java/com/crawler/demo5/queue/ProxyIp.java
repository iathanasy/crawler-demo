package com.crawler.demo5.queue;

import java.io.Serializable;

public class ProxyIp implements Serializable{

	 private static final long serialVersionUID = -7583883432417635332L;
	 private String ip;
	 private int port;
	 private String type;
	 private boolean anonymous;//是否匿名
	 
	 
	public ProxyIp(String ip, int port, String type) {
		super();
		this.ip = ip;
		this.port = port;
		this.type = type;
	}
	
	public ProxyIp() {
	}

	public ProxyIp(String ip, int port, String type, boolean anonymous) {
		super();
		this.ip = ip;
		this.port = port;
		this.type = type;
		this.anonymous = anonymous;
	}


	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	public String getType() {
		return type;
	}
	public boolean isAnonymous() {
		return anonymous;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	@Override
	public String toString() {
		return "ProxyIp [ip=" + ip + ", port=" + port + ", type=" + type
				+ ", anonymous=" + anonymous + "]";
	}
	 
	 
}
