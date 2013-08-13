package com.skula.seriestv;

import java.util.HashMap;

import com.skula.seriestv.definitions.Definitions;
import com.skula.seriestv.models.BetaserieAccount;
import com.skula.seriestv.models.SettingsItem;
import com.skula.seriestv.models.Torrent;
import com.skula.seriestv.models.TransmissionAccount;
import com.skula.seriestv.services.DatabaseService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuActivity extends Activity {
	private ListView betaserieList;
	private ListView transmissionList;
	private DatabaseService dbService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_main);

		ProgressDialog progressDialog = ProgressDialog.show(this, "", "Connexion au serveurs...");
		this.dbService = new DatabaseService(this);
		this.betaserieList = (ListView) findViewById(R.id.betaserieList);
		fillBetaserieList();
		this.transmissionList = (ListView) findViewById(R.id.transmissionList);
		fillTransmissionList();
		progressDialog.dismiss();
		
		this.transmissionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				final SettingsItem settingsItem = (SettingsItem) transmissionList.getItemAtPosition(position);
				LayoutInflater li = LayoutInflater.from(v.getContext());
				View promptsView = li.inflate(R.layout.menuitemprompt, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
				alertDialogBuilder.setTitle(settingsItem.getLabel());
				alertDialogBuilder.setView(promptsView);
				final EditText editTitle = (EditText) promptsView.findViewById(R.id.newValue);
				final Context c= v.getContext();
				
				alertDialogBuilder.setCancelable(false).setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String newValue = editTitle.getText().toString();
						DatabaseService dbService = new DatabaseService(c);
						dbService.updateParameter(settingsItem.getId(),newValue);
						fillTransmissionList();
					}
				}).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
		
		this.betaserieList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				final SettingsItem settingsItem = (SettingsItem) betaserieList.getItemAtPosition(position);
				LayoutInflater li = LayoutInflater.from(v.getContext());
				View promptsView = li.inflate(R.layout.menuitemprompt, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
				alertDialogBuilder.setTitle(settingsItem.getLabel());
				alertDialogBuilder.setView(promptsView);
				final EditText editTitle = (EditText) promptsView.findViewById(R.id.newValue);
				final Context c= v.getContext();
				alertDialogBuilder.setCancelable(false).setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String newValue = editTitle.getText().toString();
						DatabaseService dbService = new DatabaseService(c);
						dbService.updateParameter(settingsItem.getId(),newValue);
						fillBetaserieList();
						
					}
				}).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
	}

	private void fillBetaserieList() {
		BetaserieAccount account = dbService.getBetaserieAccount();
		SettingsItem menuItemArray[] = new SettingsItem[3];
		menuItemArray[0] = new SettingsItem(Definitions.BETASERIE_LOGIN, "Utilisateur", account.getLogin());
		menuItemArray[1] = new SettingsItem(Definitions.BETASERIE_MD5, "Mot de passe", account.getPass());
		menuItemArray[2] = new SettingsItem(Definitions.BETASERIE_KEY, "Clé API", account.getKey());

		MenuAdapter adapter = new MenuAdapter(this, R.layout.menuitemlayout, menuItemArray);
		betaserieList.setAdapter(adapter);

	}

	private void fillTransmissionList() {
		TransmissionAccount account = dbService.getTransmissionAccount();
		SettingsItem menuItemArray[] = new SettingsItem[5];
		menuItemArray[0] = new SettingsItem(Definitions.TRANSMISSION_HOST, "Serveur", account.getHost());
		menuItemArray[1] = new SettingsItem(Definitions.TRANSMISSION_PORT, "Port", account.getPort());
		menuItemArray[2] = new SettingsItem(Definitions.TRANSMISSION_URL, "Url RPC", account.getUrlSuffix());
		menuItemArray[3] = new SettingsItem(Definitions.TRANSMISSION_LOGIN, "Utilisateur", account.getLogin());
		menuItemArray[4] = new SettingsItem(Definitions.TRANSMISSION_PASS, "Mot de passe", account.getPass());

		MenuAdapter adapter = new MenuAdapter(this, R.layout.menuitemlayout, menuItemArray);
		transmissionList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_home:
			Intent myIntent = new Intent(this, MainActivity.class);
			startActivityForResult(myIntent, 0);
			return true;
		default:
			return false;
		}
	}
}
