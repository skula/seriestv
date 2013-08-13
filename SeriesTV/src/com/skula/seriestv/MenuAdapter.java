package com.skula.seriestv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skula.seriestv.models.SettingsItem;

public class MenuAdapter extends ArrayAdapter<SettingsItem> {
	Context context;
	int layoutResourceId;
	SettingsItem data[] = null;

	public MenuAdapter(Context context, int layoutResourceId, SettingsItem[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SettingsItem item = data[position];
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.menuitemlayout, parent, false);

		TextView id = (TextView) rowView.findViewById(R.id.id);
		id.setText(item.getId());
		TextView label = (TextView) rowView.findViewById(R.id.label);
		label.setText(item.getLabel());
		TextView value = (TextView) rowView.findViewById(R.id.value);
		if (label.getText().toString().equals("Mot de passe")) {
			value.setText("******");
		} else {
			value.setText(item.getValue());
		}

		return rowView;
	}
}
