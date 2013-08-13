package com.skula.seriestv.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetworkUtils  {
	
	public static boolean isConnectedToNetwork(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null){
			State networkState = networkInfo.getState();
			if (networkState.compareTo(State.CONNECTED) == 0){
				return true;
			}
		}
		return false;
	}
}
