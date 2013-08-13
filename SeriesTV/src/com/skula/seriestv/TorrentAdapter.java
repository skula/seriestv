package com.skula.seriestv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skula.seriestv.models.Torrent;

public class TorrentAdapter extends ArrayAdapter<Torrent> {

	Context context;
	int layoutResourceId;
	Torrent data[] = null;

	public TorrentAdapter(Context context, int layoutResourceId, Torrent[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Torrent torrent = data[position];
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.torrentlayout, parent, false);
		
		TextView id = (TextView) rowView.findViewById(R.id.id);
		id.setText(torrent.getId());	
		TextView torrentName = (TextView) rowView.findViewById(R.id.torrentName);
		torrentName.setText(torrent.getName());
		TextView downloadData = (TextView) rowView.findViewById(R.id.downloadData);
		double totalSize = Integer.valueOf(torrent.getTotalSize()) / 1000000;
		double left = Integer.valueOf(torrent.getLeftUntilDone()) / 1000000;
		int speed = Integer.valueOf(torrent.getRateDownload()) == 0 ? 0 : Integer.valueOf(torrent.getRateDownload()) / 1000;
		downloadData.setText(totalSize - left + " mo / " + totalSize + " mo");
		//TextView timeLeft = (TextView) rowView.findViewById(R.id.timeLeft);
		//timeLeft.setText("12 min");
		TextView downloadSpeed = (TextView) rowView.findViewById(R.id.timeLeft);
		downloadSpeed.setText(speed + " kb/s");
		int rateDownload= (int) (Double.valueOf(torrent.getPercentDone())*100);
		ProgressBar progressBar = (ProgressBar)rowView.findViewById(R.id.progressBar);
		progressBar.setProgress(rateDownload);
		return rowView;
	}
}
