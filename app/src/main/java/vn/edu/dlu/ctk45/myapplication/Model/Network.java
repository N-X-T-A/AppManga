package vn.edu.dlu.ctk45.myapplication.Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}