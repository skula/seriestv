package com.skula.seriestv.models;

public class SettingsItem {
	private String label;
	private String value;
	private String id;

	public SettingsItem() {
	}

	public SettingsItem(String id, String label, String value) {
		this.id = id;
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
