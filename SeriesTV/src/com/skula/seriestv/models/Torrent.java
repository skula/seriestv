package com.skula.seriestv.models;

public class Torrent {
	private String downloadDir;
	private String id;
	private String leftUntilDone;
	private String percentDone;
	private String rateDownload;
	private String name;
	private String status;
	private String totalSize;

	public static String[] torrentFieldName = new String[] { "downloadDir", "id", "leftUntilDone", "percentDone", "rateDownload", "name", "status",
			"totalSize" };

	public static Torrent[] getStaticArray(){
		Torrent torrentArray[] = null;
		torrentArray = new Torrent[10];
		for (int i = 0; i < 10; i++) {
			torrentArray[i] = new Torrent("", i + "", "150", "0.25", "652", "djkqsjdkqskdj.torrent", "6", "1250");
		}
		return torrentArray;
	}
	
	public Torrent() {

	}

	public Torrent(String downloadDir, String id, String leftUntilDone, String percentDone, String rateDownload, String name, String status,
			String totalSize) {
		this.downloadDir = downloadDir;
		this.id = id;
		this.leftUntilDone = leftUntilDone;
		this.percentDone = percentDone;
		this.rateDownload = rateDownload;
		this.name = name;
		this.status = status;
		this.totalSize = totalSize;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeftUntilDone() {
		return leftUntilDone;
	}

	public void setLeftUntilDone(String leftUntilDone) {
		this.leftUntilDone = leftUntilDone;
	}

	public String getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(String percentDone) {
		this.percentDone = percentDone;
	}

	public String getRateDownload() {
		return rateDownload;
	}

	public void setRateDownload(String rateDownload) {
		this.rateDownload = rateDownload;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	public static String[] getTorrentFieldName() {
		return torrentFieldName;
	}

	public static void setTorrentFieldName(String[] torrentFieldName) {
		Torrent.torrentFieldName = torrentFieldName;
	}
}
