package vn.edu.dlu.ctk45.myapplication.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;

public class DataAllManga {
    public static List<MangaItem> processData() {
        List<MangaItem> mangaItemList = new ArrayList<>();

        String result = ApiReaderTask.getResult();

        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mangaObject = jsonArray.getJSONObject(i);

                int id = mangaObject.getInt("idTruyen");
                String title = mangaObject.getString("Ten");
                String Url = mangaObject.getString("Anh");
                String imageUrl = Home.url + Url;

                String releaseDateStr = mangaObject.getString("NamPhatHanh");
                Date releaseDate = parseReleaseDate(releaseDateStr);

                JSONArray tacGiaArray = mangaObject.getJSONArray("TacGia");
                String authorName = "";
                if (tacGiaArray.length() > 0) {
                    JSONObject tacGiaObject = tacGiaArray.getJSONObject(0);
                    authorName = tacGiaObject.getString("Ten");
                }

                JSONArray theLoaiArray = mangaObject.getJSONArray("TheLoai");
                String genre = "";
                if (theLoaiArray.length() > 0) {
                    JSONObject theLoaiObject = theLoaiArray.getJSONObject(0);
                    genre = theLoaiObject.getString("TenTheLoai");
                }

                int favorites = mangaObject.getInt("LuotYeuThich");



                MangaItem mangaItem = new MangaItem(
                        id,
                        title,
                        releaseDate,
                        imageUrl,
                        authorName,
                        genre,
                        favorites
                );

                mangaItemList.add(mangaItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mangaItemList;
    }

    private static Date parseReleaseDate(String releaseDateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return dateFormat.parse(releaseDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing exception as needed
            return null;
        }
    }
}