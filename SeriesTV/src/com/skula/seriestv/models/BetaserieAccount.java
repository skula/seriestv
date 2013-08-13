package com.skula.seriestv.models;

import java.util.Map;

import com.skula.seriestv.definitions.Definitions;

public class BetaserieAccount {
	private String login;
	private String pass;
	private String key;	
	
	public BetaserieAccount(Map<String,String> map){
		this.login=map.get(Definitions.BETASERIE_LOGIN);
		this.pass=map.get(Definitions.BETASERIE_MD5);
		this.key=map.get(Definitions.BETASERIE_KEY);
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
