package com.skula.seriestv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.skula.seriestv.models.BetaserieAccount;
import com.skula.seriestv.models.EpisodeShow;
import com.skula.seriestv.models.Torrent;
import com.skula.seriestv.models.TransmissionAccount;
import com.skula.seriestv.services.BetaserieService;
import com.skula.seriestv.services.DatabaseService;
import com.skula.seriestv.services.ExtraTorrentService;
import com.skula.seriestv.services.TransmissionService;
import com.skula.seriestv.utils.NetworkUtils;

public class MainActivity extends Activity {
	private Spinner spinTitle;
	private ListView torrentList;
	private ListView episodeList;
	private EditText episodeNumber;
	private EditText urlEdit;
	private Button btnAddUrl;
	private Button btnSearch;
	private Button btnDelShow;
	private Button btnAddShow;

	private BetaserieService betaserieService;
	private TransmissionService transmissionService;
	private DatabaseService dbService;

	private ArrayAdapter<String> showsAdapter;
	private Timer transmissionTimer;
	private int season;
	private int episode;
	private String url;

	private boolean demoMode;
	private boolean showSearch;

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showSearch = true;
		if (!demoMode) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		setContentView(R.layout.activity_main);

		this.demoMode = !NetworkUtils.isConnectedToNetwork(this);
		this.season = 1;
		this.episode = 1;

		this.dbService = new DatabaseService(this);
		//dbService.bouchon();
		if (!demoMode) {
			TransmissionAccount tAccount = dbService.getTransmissionAccount();
			BetaserieAccount bAccount = dbService.getBetaserieAccount();
			this.betaserieService = new BetaserieService(bAccount);
			this.transmissionService = new TransmissionService(tAccount);
		}

		this.spinTitle = (Spinner) findViewById(R.id.spinTitle);
		updateSpinTitle();

		this.episodeNumber = (EditText) findViewById(R.id.episodeNumber);
		setEpisodeNumber();
		this.episodeList = (ListView) findViewById(R.id.episodeList);

		this.torrentList = (ListView) findViewById(R.id.torrentlist);

		updateEpisodeList();
		//updateTorrentList();

		Button btnSeasonMinus = (Button) findViewById(R.id.btnSeasonMinus);
		btnSeasonMinus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				season = (season - 1) < 1 ? 1 : season - 1;
				setEpisodeNumber();
			}
		});

		Button btnSeasonPlus = (Button) findViewById(R.id.btnSeasonPlus);
		btnSeasonPlus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				season++;
				setEpisodeNumber();
			}
		});

		Button btnEpisodeMinus = (Button) findViewById(R.id.btnEpisodeMinus);
		btnEpisodeMinus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				episode = (episode - 1) < 1 ? 1 : episode - 1;
				setEpisodeNumber();
			}
		});

		Button btnEpisodePlus = (Button) findViewById(R.id.btnEpisodePlus);
		btnEpisodePlus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				episode++;
				setEpisodeNumber();
			}
		});

		btnAddShow = (Button) findViewById(R.id.btnAddShow);
		btnAddShow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LayoutInflater li = LayoutInflater.from(v.getContext());
				View promptsView = li.inflate(R.layout.addshowprompt, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
				alertDialogBuilder.setTitle("Ajout d'une série");
				alertDialogBuilder.setView(promptsView);
				final EditText editTitle = (EditText) promptsView.findViewById(R.id.title);

				alertDialogBuilder.setCancelable(false).setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String title = editTitle.getText().toString();
						if (!dbService.showExists(title) && !title.equals("")) {
							dbService.insertShow(title);
							updateSpinTitle();
						}
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

		btnDelShow = (Button) findViewById(R.id.btnDelShow);
		btnDelShow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String title = String.valueOf(spinTitle.getSelectedItem());
				AlertDialog.Builder helpBuilder = new AlertDialog.Builder(v.getContext());
				helpBuilder.setTitle("Suppression d'une série");
				helpBuilder.setMessage("Etes-vous sûr de vouloir supprimer la série \"" + title + "\" ?");
				helpBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dbService.deleteShow(title);
						updateSpinTitle();
					}
				});
				helpBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				AlertDialog helpDialog = helpBuilder.create();
				helpDialog.show();
			}
		});

		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String title = String.valueOf(spinTitle.getSelectedItem());
				if (!demoMode) {
					if(showSearch){
						EditText filter = (EditText) findViewById(R.id.filter);
						String filterValue = filter.getText().toString();
						url = ExtraTorrentService.getTorrent(new EpisodeShow(title, season, episode), filterValue);
					}else{
						EditText searchContent = (EditText) findViewById(R.id.searchContent);
						String searchContentValue = searchContent.getText().toString();
						url = ExtraTorrentService.getTorrent(searchContentValue);
					}
					if (url != null) {
						urlEdit.setText(url.substring(url.lastIndexOf('/') + 1));
					}
				} else {
					String seasonString = "S" + (season < 10 ? "0" : "") + season;
					String episodeString = "E" + (episode < 10 ? "0" : "") + episode;
					url = "http://" + title + seasonString + episodeString + ".torrent";
					urlEdit.setText(url);
				}
			}
		});

		this.btnAddUrl = (Button) findViewById(R.id.btnAddUrl);
		btnAddUrl.setEnabled(false);
		btnAddUrl.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!demoMode) {
					try {
						transmissionService.addTorrent(url + ".torrent");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				urlEdit.setText("");
			}
		});

		this.urlEdit = (EditText) findViewById(R.id.url);
		urlEdit.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String text = urlEdit.getText().toString();
				btnAddUrl.setEnabled(!text.equals(""));
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		this.episodeList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				HashMap<String, String> map = (HashMap<String, String>) episodeList.getItemAtPosition(position);
				String show = map.get("show");
				season = Integer.valueOf(map.get("season"));
				episode = Integer.valueOf(map.get("episode"));
				setEpisodeNumber();

				if (!dbService.showExists(show)) {
					dbService.insertShow(show);
					updateSpinTitle();
				}
				spinTitle.setSelection(showsAdapter.getPosition(show));
			}
		});

		this.episodeList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				HashMap<String, String> map = (HashMap<String, String>) episodeList.getItemAtPosition(position);
				final String url = map.get("url");
				final String se =map.get("season");
				final String ep= map.get("episode");
				AlertDialog.Builder helpBuilder = new AlertDialog.Builder(v.getContext());
				helpBuilder.setTitle("Episodes vus");
				helpBuilder.setMessage("Voulez-vous marquer comme vu, cet episode?");
				helpBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						betaserieService.setEpisodeDownloaded(url, se, ep);
						updateEpisodeList();
					}
				});
				helpBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				AlertDialog helpDialog = helpBuilder.create();
				helpDialog.show();
				
				return true;
			}
		});
		
		this.torrentList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Torrent torrent = (Torrent) episodeList.getItemAtPosition(position);
				// TODO
			}
		});
		
		this.torrentList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				final Torrent torrent = (Torrent) episodeList.getItemAtPosition(position);
				AlertDialog.Builder helpBuilder = new AlertDialog.Builder(v.getContext());
				helpBuilder.setTitle("Suppression");
				helpBuilder.setMessage("Etes-vous sûr de vouloir annuler ce torrent ?");
				helpBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//transmissionService.removeTorrent(torrent.getId());
					}
				});
				helpBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				AlertDialog helpDialog = helpBuilder.create();
				helpDialog.show();				
				return true;
			}
		});
		
		this.transmissionTimer = new Timer();
		this.transmissionTimer.schedule(new TimerTask() { 
			@Override
			public void run() {
				TimerMethod();
			}
		}, 0, 3000);
		
		RadioGroup searchTypeGroup = (RadioGroup) findViewById(R.id.searchTypeGroup);
		searchTypeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.typeShow : 
					setSearchType(true);
					break;
				case R.id.typeSimple :
					setSearchType(false);			
					break;
				default :
					break;
				}
				
			}
		});
	}
	
	private void setSearchType(boolean isShowSearch){
		this.showSearch=isShowSearch;
		int showSearchState = isShowSearch?View.VISIBLE:View.GONE;
		int simpleSearchState = isShowSearch?View.GONE:View.VISIBLE;
		
		LinearLayout simpleSerachLayout =(LinearLayout) findViewById(R.id.simpleSerachLayout);
		simpleSerachLayout.setVisibility(simpleSearchState);
		LinearLayout showSearchLayout1 =(LinearLayout) findViewById(R.id.showSearchLayout1);
		showSearchLayout1.setVisibility(showSearchState);
		LinearLayout showSearchLayout2 =(LinearLayout) findViewById(R.id.showSearchLayout2);
		showSearchLayout2.setVisibility(showSearchState);
		LinearLayout showSearchLayout3 =(LinearLayout) findViewById(R.id.showSearchLayout3);
		showSearchLayout3.setVisibility(showSearchState);
		
		EditText filter = (EditText) findViewById(R.id.filter);
		filter.setText("");
		EditText searchContent = (EditText) findViewById(R.id.searchContent);
		searchContent.setText("");
		this.urlEdit.setText("");
	}
	
	private void TimerMethod(){
		this.runOnUiThread(Timer_Tick);
	}

	private Runnable Timer_Tick = new Runnable() {
		public void run() {
			updateTorrentList();
		}
	};

	private void updateSpinTitle() {
		showsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbService.selectShows());
		showsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinTitle.setAdapter(showsAdapter);
	}

	private void setEpisodeNumber() {
		String seasonString = "S" + (season < 10 ? "0" : "") + season;
		String episodeString = "E" + (episode < 10 ? "0" : "") + episode;
		this.episodeNumber.setText(seasonString + episodeString);
	}

	private void updateTorrentList() {
		Torrent torrentArray[] = null;
		if (demoMode) {
			torrentArray = Torrent.getStaticArray();
		} else {
			torrentArray = transmissionService.getTorrents();
		}
		if (torrentArray != null) {
			TorrentAdapter adapter = new TorrentAdapter(this, R.layout.torrentlayout, torrentArray);
			torrentList.setAdapter(adapter);
		}
	}

	private void updateEpisodeList() {
		Map<String, List<EpisodeShow>> map = new HashMap<String, List<EpisodeShow>>();
		if (demoMode) {
			map = EpisodeShow.getStaticMap();
		} else {
			map = betaserieService.getUnseenEpisode();
		}
		AdvancedList adapter = new AdvancedList(this, map);
		episodeList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent = null;
		switch (item.getItemId()) {
		case R.id.menu_settings:
			myIntent = new Intent(this, MenuActivity.class);
			startActivityForResult(myIntent, 0);
			return true;
		case R.id.menu_refresh:
			updateTorrentList();
			updateEpisodeList();
			return true;
		default:
			return false;
		}
	}
}