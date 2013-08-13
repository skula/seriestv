package com.skula.seriestv.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.skula.seriestv.models.Torrent;

public class JSONService {
	public static JSONObject getTorrents() throws JSONException {
		JSONArray fieldsContent = new JSONArray();
		for(String s : Torrent.torrentFieldName){
			fieldsContent.put(s);
		}
		JSONObject fields = new JSONObject();
		fields.put("fields", fieldsContent);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("arguments", fields);
		jsonObject.put("method", "torrent-get");
		return jsonObject;
	}
	
	public static List<Torrent> getTorretList(JSONObject jsonObject) throws JSONException {
		String response = jsonObject.getString("result");
		if(!response.equals("success")){
			return null;
		}
		JSONObject arguments = jsonObject.getJSONObject("arguments");
		JSONArray torrents = arguments.getJSONArray("torrents");
		
		List<Torrent> res = new ArrayList<Torrent>();
		for(int i=0; i<torrents.length(); i++){
			JSONObject jsonTorrent = torrents.getJSONObject(i);
			Torrent torrent = new Torrent();	
			torrent.setDownloadDir(jsonTorrent.getString("downloadDir"));
			torrent.setId(jsonTorrent.getString("id"));
			torrent.setLeftUntilDone(jsonTorrent.getString("leftUntilDone"));
			torrent.setPercentDone(jsonTorrent.getString("percentDone"));
			torrent.setRateDownload(jsonTorrent.getString("rateDownload"));
			torrent.setName(jsonTorrent.getString("name"));
			torrent.setStatus(jsonTorrent.getString("status"));
			torrent.setTotalSize(jsonTorrent.getString("totalSize"));
			res.add(torrent);
		}		
		return res;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject addTorrent(String url) throws JSONException {
		JSONObject fields = new JSONObject();
		fields.put("filename", url);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("arguments", fields);
		jsonObject.put("method", "torrent-add");
		return jsonObject;
	}

	public static String getAddStatus(JSONObject jsonObject) throws JSONException {
		return jsonObject.getString("result");
	}

	@SuppressWarnings("unchecked")
	public static JSONObject removeTorrent(String id) throws JSONException {
		JSONArray list = new JSONArray();
		list.put(String.valueOf(id));

		JSONObject arguments = new JSONObject();
		arguments.put("ids", list);
		arguments.put("delete-local-data", false);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("arguments", arguments);
		jsonObject.put("method", "torrent-remove");
		return jsonObject;
	}
	
	public static String getRemoveStatus(JSONObject jsonObject) throws JSONException {
		return jsonObject.getString("result");
	}
}