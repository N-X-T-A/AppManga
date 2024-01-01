package vn.edu.dlu.ctk45.myapplication.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.Chapter;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItemDetail;

public class DataMangaDetail {
    public static List<MangaItemDetail> processData() {
        List<MangaItemDetail> mangaItemList = new ArrayList<>();

        String result = ApiReaderTask.getResult();


        try {
            JSONObject mangaObject = new JSONObject(result);

            int id = mangaObject.getInt("idTruyen");
            String title = mangaObject.getString("Ten");
            String Url = mangaObject.getString("Anh");
            String imageUrl = Home.url + Url;

            String describe = mangaObject.getString("MoTa");

            String releaseDateStr = mangaObject.getString("NamPhatHanh");
            Date releaseDate = parseReleaseDate(releaseDateStr);

            int favorites = mangaObject.getInt("LuotYeuThich");

            JSONArray tacGiaArray = mangaObject.getJSONArray("TacGia");
            String authorName = "";
            if (tacGiaArray.length() > 0) {
                JSONObject tacGiaObject = tacGiaArray.getJSONObject(0);
                authorName = tacGiaObject.getString("Ten");
            }

            JSONArray theLoaiArray = mangaObject.getJSONArray("TheLoai");
            StringBuilder genresBuilder = new StringBuilder();

            for (int i = 0; i < theLoaiArray.length(); i++) {
                JSONObject theLoaiObject = theLoaiArray.getJSONObject(i);
                String genre = theLoaiObject.getString("TenTheLoai");

                genresBuilder.append(genre);
                if (i < theLoaiArray.length() - 1) {
                    genresBuilder.append(", ");
                }
            }
            String genres = genresBuilder.toString();

            List<Chapter> chapterList = new ArrayList<>();
            JSONArray arraylistChapter = mangaObject.getJSONArray("DanhSachChuong");
            for (int i = 0; i < arraylistChapter.length(); i++) {
                JSONObject chapterObject = arraylistChapter.getJSONObject(i);

                int chapterId = chapterObject.getInt("idChuongTruyen");
                int numChapter = chapterObject.getInt("SoChuong");
                int page = chapterObject.getInt("PAGE");

                Chapter chapter = new Chapter(chapterId, numChapter, page);
                chapterList.add(chapter);
            }

            MangaItemDetail mangaItem = new MangaItemDetail(
                    id,
                    title,
                    releaseDate,
                    favorites,
                    imageUrl,
                    describe,
                    authorName,
                    genres,
                    chapterList
            );

            mangaItemList.add(mangaItem);

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
