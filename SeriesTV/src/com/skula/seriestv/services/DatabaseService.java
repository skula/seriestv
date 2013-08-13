package com.skula.seriestv.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.skula.seriestv.definitions.Definitions;
import com.skula.seriestv.models.BetaserieAccount;
import com.skula.seriestv.models.TransmissionAccount;

public class DatabaseService {
	private static final String DATABASE_NAME = "myShows.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME_SHOWS = "shows";
	private static final String TABLE_NAME_PARAM = "param";

	private Context context;
	private SQLiteDatabase database;
	private SQLiteStatement statement;

	public DatabaseService(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.database = openHelper.getWritableDatabase();
	}

	public void bouchon() {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SHOWS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PARAM);
		database.execSQL("CREATE TABLE " + TABLE_NAME_SHOWS + "(title TEXT PRIMARY KEY)");
		database.execSQL("CREATE TABLE " + TABLE_NAME_PARAM + "(label TEXT PRIMARY KEY, val TEXT)");

		insertParameter(Definitions.BETASERIE_LOGIN, "skula");
		insertParameter(Definitions.BETASERIE_MD5, "98ef623f2e599df9958202b190d4ecdf");
		insertParameter(Definitions.BETASERIE_KEY, "8044679e84db");

		//insertParameter(Definitions.TRANSMISSION_HOST, "192.168.1.52");
		insertParameter(Definitions.TRANSMISSION_HOST, "89.82.148.107");
		insertParameter(Definitions.TRANSMISSION_PORT, "9091");
		insertParameter(Definitions.TRANSMISSION_LOGIN, "slown");
		insertParameter(Definitions.TRANSMISSION_PASS, "salope");
		insertParameter(Definitions.TRANSMISSION_URL, "/transmission/rpc");

		insertShow("Gossip Girl");
		insertShow("How I Met Your Mother");
		insertShow("Fringe");
		insertShow("Bones");
		insertShow("White Collar");
		insertShow("Californication");
		insertShow("Once Upon A Time");
		insertShow("The Mentalist");
		insertShow("The Vampire Diaries");
		insertShow("Pretty Little Liars");
		insertShow("The Big Bang Theory");
	}

	public void insertShow(String title) {
		String sql = "insert into " + TABLE_NAME_SHOWS + "(title) values (?)";
		statement = database.compileStatement(sql);
		statement.bindString(1, getFormatTitle(title));
		statement.executeInsert();
	}

	private String getFormatTitle(String title) {
		String[] parts = title.toLowerCase().split(" ");
		String res = "";
		for (int i = 0; i < parts.length; i++) {
			res += !res.equals("") ? " " : "";
			res += parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
		}
		return res;
	}
	
	public boolean showExists(String title) {
		Cursor cursor = database.query(TABLE_NAME_SHOWS, new String[] { "title" }, "title='"+title+"'", null, null, null, "title asc");
		List<String> showList = new ArrayList<String>();

		if (cursor.moveToFirst()) {
			do {
				return true;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return false;
	}

	public void deleteShow(String title) {
		database.delete(TABLE_NAME_SHOWS, "title='" + title + "'", null);
	}

	public String[] selectShows() {
		Cursor cursor = database.query(TABLE_NAME_SHOWS, new String[] { "title" }, null, null, null, null, "title asc");
		List<String> showList = new ArrayList<String>();

		if (cursor.moveToFirst()) {
			do {
				showList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return (String[]) showList.toArray(new String[showList.size()]);
	}

	public void insertParameter(String label, String val) {
		String sql = "insert into " + TABLE_NAME_PARAM + "(label, val) values (?,?)";
		statement = database.compileStatement(sql);
		statement.bindString(1, label);
		statement.bindString(2, val);
		statement.executeInsert();
	}
	
	public void updateParameter(String label, String newValue) {
		ContentValues args = new ContentValues();
		args.put("val", newValue);
		database.update(TABLE_NAME_PARAM, args, "label='" + label+"'", null);
	}

	public BetaserieAccount getBetaserieAccount() {
		String where = "label='" + Definitions.BETASERIE_LOGIN + "'";
		where += " or label='" + Definitions.BETASERIE_MD5 + "'";
		where += " or label='" + Definitions.BETASERIE_KEY + "'";
		Cursor cursor = database.query(TABLE_NAME_PARAM, new String[] { "label", "val" }, where, null, null, null, null);

		Map<String, String> map = new HashMap<String, String>();
		if (cursor.moveToFirst()) {
			do {
				map.put(cursor.getString(0), cursor.getString(1));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return new BetaserieAccount(map);
	}

	public TransmissionAccount getTransmissionAccount() {
		String where = "label='" + Definitions.TRANSMISSION_HOST + "'";
		where += " or label='" + Definitions.TRANSMISSION_PORT + "'";
		where += " or label='" + Definitions.TRANSMISSION_LOGIN + "'";
		where += " or label='" + Definitions.TRANSMISSION_PASS + "'";
		where += " or label='" + Definitions.TRANSMISSION_URL + "'";
		Cursor cursor = database.query(TABLE_NAME_PARAM, new String[] { "label", "val" }, where, null, null, null, null);
		Map<String, String> map = new HashMap<String, String>();
		if (cursor.moveToFirst()) {
			do {
				map.put(cursor.getString(0), cursor.getString(1));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return new TransmissionAccount(map);
	}

	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME_SHOWS + "(title TEXT PRIMARY KEY)");
			db.execSQL("CREATE TABLE " + TABLE_NAME_PARAM + "(label TEXT PRIMARY KEY, val TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SHOWS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PARAM);
			onCreate(db);
		}
	}
}