package com.skula.seriestv.services;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.skula.seriestv.models.EpisodeShow;

public class ExtraTorrentService {
	private static final String ET_SEARCH_URL = "http://extratorrent.com/search/?new=1&x=0&y=0&search=";
	private static final String ET_DOWNLOAD_URL = "http://extratorrent.com/download/";
	private static final String LINE_START_MARK = "torrent_download";
	private static final String LINE_END_MARK = ".torrent";

	public static String getTorrent(String line){
		String regexpr = checkResult(line);
		for(String url : getUrls(buildParam(line))){
			if (url.matches(regexpr)) {
				return url;
			}
		}
		return "";
	}
	
	public static String getTorrent(EpisodeShow ep, String filter){
		String param = buildParam(ep);
		if(!filter.equals("")){
			param += "+" + buildParam(filter);
		}
		
		String regexpr = checkResult(ep.getTitle()+" "+filter);
		for(String url : getUrls(buildParam(param))){
			if (url.matches(regexpr)) {
				return url;
			}
		}
		return "";
	}
	
	private static List<String> getUrls(String param) {
		List<String> urls = new ArrayList<String>();
		try{
			URLConnection connection = new URL(ET_SEARCH_URL + param).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine = "";
			String suffix = "";
			int start = 0;
			int end = 0;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains(".torrent")) {
					start = inputLine.indexOf(LINE_START_MARK);
					end = inputLine.indexOf(".torrent");
					while (start >= 0 && end >= 0) {
						suffix = inputLine.substring(start + LINE_START_MARK.length(), end);
						urls.add(ET_DOWNLOAD_URL + suffix);
						inputLine = inputLine.substring(end + LINE_START_MARK.length());
						start = inputLine.indexOf(LINE_START_MARK);
						end = inputLine.indexOf(LINE_END_MARK);
					}
				}
			}
			in.close();
		} catch (IOException e) {
		}
		return urls;
	}
	
	private static String buildParam(EpisodeShow e) {
		return e.toString().replaceAll(" ", "+");
	}
	
	private static String buildParam(String line) {
		String res = line.replaceAll(" ", "+");
		return res;
	}

	private static String checkResult(String line)  {
		// tester si un split de "ezklekzlk" renvoie un element
		String parts[] = line.split(" ");
		if (parts.length == 0) {
			return ".*(?i)" + line + ".*";
		}

		String regexpr = ".*";
		for (int i = 0; i < parts.length; i++) {
			regexpr += "(?i)" + parts[i] + ".*";
		}
		return regexpr;
	}
}
