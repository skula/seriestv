package com.skula.seriestv.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpisodeShow {
	private String title;
	private String url;
	private int season;
	private int episode;

	public static Map<String, List<EpisodeShow>> getStaticMap(){
		Map<String, List<EpisodeShow>> map = new HashMap<String, List<EpisodeShow>>();
		List<EpisodeShow> list = new ArrayList<EpisodeShow>();
		list.add(new EpisodeShow("Gossip Girl", 1, 1));
		list.add(new EpisodeShow("Gossip Girl", 1, 2));
		list.add(new EpisodeShow("Gossip Girl", 1, 3));
		map.put("Gossip Girl", list);
		list = new ArrayList<EpisodeShow>();
		list.add(new EpisodeShow("Bones", 6, 15));
		list.add(new EpisodeShow("Bones", 6, 16));
		map.put("Bones", list);
		return map;
	}
	
	public static void main(String args[]) {

	}

	public EpisodeShow() {
	}

	public EpisodeShow(EpisodeShow ep) {
		this.title = ep.getTitle();
		this.season = ep.getSeason();
		this.episode = ep.getEpisode();
	}

	public EpisodeShow(String title, int season, int episode) {
		this.title = title;
		this.season = season;
		this.episode = episode;
	}
	
	public EpisodeShow(String title, int season, int episode, String url) {
		this.title = title;
		this.season = season;
		this.episode = episode;
		this.url = url;
	}
	
	public EpisodeShow(String title, String number, String url) {
		this.title = title;
		this.url = url;
		String season = number.substring(number.indexOf("S")+1,number.indexOf("E"));		
		String episode = number.substring(number.indexOf("E")+1);
		this.season = Integer.valueOf(season);
		this.episode = Integer.valueOf(episode);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getSeason() {
		return this.season;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}

	public int getEpisode() {
		return this.episode;
	}

	public String getNumberLabel() {
		String seasonString = "s" + (season < 10 ? "0" : "") + season;
		String episodeString = "e" + (episode < 10 ? "0" : "") + episode;
		return seasonString + episodeString;
	}
	
	public String toString(){
		return title+" "+getNumberLabel();
	}
}