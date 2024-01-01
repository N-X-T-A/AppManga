package vn.edu.dlu.ctk45.myapplication.Api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiReaderTask extends AsyncTask<String, Void, String> {
    private static String result;
    private OnTaskCompleted listener;
    public ApiReaderTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) return null;

        String apiUrl = params[0];
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            int apiVersion = android.os.Build.VERSION.SDK_INT;
            Log.d("API Version: " , String.valueOf(apiVersion));

            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            this.result = result;
            listener.onTaskCompleted(result);
        } else {
            listener.onTaskFailed("Failed to fetch data from API");
        }
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
        void onTaskFailed(String errorMessage);
    }

    public static String getResult() {
        return result;
    }
}
