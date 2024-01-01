package vn.edu.dlu.ctk45.myapplication.Data;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;

public class DataMangaLiked {
    public interface OnMangaDataFetchListener {
        void onMangaDataFetch(ArrayList<MangaItem> mangaItems);
    }
    public interface OnCheckLikedListener {
        void onCheckLiked(boolean isChecked);
    }
    public static  void listLikedUser(final String token, final OnMangaDataFetchListener  onMangaDataFetchListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    // Replace the URL with your actual endpoint
                    URL url = new URL(Home.url + "/user/liked/");
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer " + token);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONArray danhSachArray = jsonObject.getJSONArray("danhsach");

                        ArrayList<MangaItem> mangaItems = new ArrayList<>();

                        for (int i = 0; i < danhSachArray.length(); i++) {
                            JSONObject truyenObject = danhSachArray.getJSONObject(i);
                            int id = truyenObject.getInt("idTruyen");
                            String title = truyenObject.getString("Ten");
                            String authorName = truyenObject.getJSONArray("TacGia").getJSONObject(0).getString("Ten");
                            String Url = truyenObject.getString("Anh");
                            String imageUrl = Home.url + Url;
                            int favorites = truyenObject.getInt("LuotYeuThich");

                            MangaItem mangaItem = new MangaItem(id, title, null, imageUrl, authorName, null, favorites);
                            mangaItems.add(mangaItem);
                        }

                        if (onMangaDataFetchListener != null) {
                            onMangaDataFetchListener.onMangaDataFetch(mangaItems);
                        }

                    } else {
                        // Handle error response
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void isLiked(final String token, final int id, final OnCheckLikedListener onCheckLikedListener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    URL url = new URL(Home.url + "/user/check_like/" + String.valueOf(id));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer " + token);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject likeResultObject = jsonObject.getJSONObject("likeResult");

                        boolean isChecked = likeResultObject.getBoolean("liked");
                        return isChecked;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (onCheckLikedListener != null) {
                    onCheckLikedListener.onCheckLiked(result);
                }
            }
        }.execute();
    }

    public static void Like(final String token, final int id, final OnCheckLikedListener onCheckLikedListener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    URL url = new URL(Home.url + "/user/like/" + String.valueOf(id));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "Bearer " + token);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (onCheckLikedListener != null) {
                    onCheckLikedListener.onCheckLiked(result);
                }
            }
        }.execute();
    }

    public static void UnLike(final String token, final int id, final OnCheckLikedListener onCheckLikedListener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    URL url = new URL(Home.url + "/user/unlike/" + String.valueOf(id));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.setRequestProperty("Authorization", "Bearer " + token);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (onCheckLikedListener != null) {
                    onCheckLikedListener.onCheckLiked(result);
                }
            }
        }.execute();
    }
}
