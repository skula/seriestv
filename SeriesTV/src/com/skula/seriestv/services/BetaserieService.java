package com.skula.seriestv.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.skula.seriestv.models.BetaserieAccount;
import com.skula.seriestv.models.EpisodeShow;
import com.skula.seriestv.utils.XmlUtils;

public class BetaserieService {
	private static final String DOMAIN = "http://api.betaseries.com/";
	private static final String DEFAULT_KEY = "8044679e84db";
	private static final String DEFAULT_PW_MD5 = "98ef623f2e599df9958202b190d4ecdf";
	private static final String DEFAULT_LOGIN = "skula";

	private String key;
	private String pwMD5;
	private String login;

	private String token;
	private XPath xpath;

	public BetaserieService(BetaserieAccount account) {
		this.key = account.getKey();
		this.pwMD5 = account.getPass();
		this.login = account.getLogin();
		this.xpath = XPathFactory.newInstance().newXPath();
		this.setToken();
	}
	
	public BetaserieService() {
		this.key = DEFAULT_KEY;
		this.pwMD5 = DEFAULT_PW_MD5;
		this.login = DEFAULT_LOGIN;
		this.xpath = XPathFactory.newInstance().newXPath();
		this.setToken();
	}

	private void setToken() {
		try {
			String url = DOMAIN + "members/auth.xml?key=" + key + "&login="+ login + "&password=" + pwMD5;	
			String xml = XmlUtils.getStringFromUrl(url); 
			InputSource is = new InputSource(new StringReader(xml));
			this.token = (String)this.xpath.evaluate("//member/token/text() ", is, XPathConstants.STRING);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public Map<String,List<EpisodeShow>> getUnseenEpisode(){
		if(this.token==null){
			return null;
		}
		Map<String, List<EpisodeShow>> map = new HashMap<String, List<EpisodeShow>>();
		
		try{
			String url = DOMAIN + "members/episodes/all.xml?view=all&token=" + this.token + "&key=" + key;
			String xml = XmlUtils.getStringFromUrl(url);
			InputSource is = new InputSource(new StringReader(xml));
			NodeList nl = (NodeList) this.xpath.evaluate("//episodes/episode", is,
					XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node d = nl.item(i);
				Element e = (Element) nl.item(i);
				String title = XmlUtils.getValue(e, "show");
				String number = XmlUtils.getValue(e, "number");
				String showUrl = XmlUtils.getValue(e, "url");
				EpisodeShow episodeShow = new EpisodeShow(title, number, showUrl);

				if (!map.containsKey(episodeShow.getTitle())) {
					map.put(episodeShow.getTitle(), new ArrayList<EpisodeShow>());
				}
				map.get(episodeShow.getTitle()).add(episodeShow);
			}
		}catch(XPathExpressionException e){
		}
		return map;
	}
	
	public boolean setEpisodeDownloaded(String shortCutTitle, String season, String episode){
		if(this.token==null){
			return false;
		}
		try {
			String urlString = DOMAIN + "members/watched/"+shortCutTitle+".xml?season=" + season+"&episode="+episode
				+ "&token=" + this.token + "&key=" + key;

			HttpPost httpPost = new HttpPost(urlString);
			DefaultHttpClient httpClient = new DefaultHttpClient(); 		
			httpClient.execute(httpPost);			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}