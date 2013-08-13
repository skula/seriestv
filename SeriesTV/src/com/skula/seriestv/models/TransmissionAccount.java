package com.skula.seriestv.models;

import java.util.Map;

import com.skula.seriestv.definitions.Definitions;

public class TransmissionAccount {
	private String login;
	private String pass;

	private String host;
	private String port;
	private String urlSuffix;

	public TransmissionAccount(Map<String, String> map) {
		this.login = map.get(Definitions.TRANSMISSION_LOGIN);
		this.pass = map.get(Definitions.TRANSMISSION_PASS);
		this.host = map.get(Definitions.TRANSMISSION_HOST);
		this.port = map.get(Definitions.TRANSMISSION_PORT);
		this.urlSuffix = map.get(Definitions.TRANSMISSION_URL);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUrlSuffix() {
		return urlSuffix;
	}

	public void setUrlSuffix(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}
}
