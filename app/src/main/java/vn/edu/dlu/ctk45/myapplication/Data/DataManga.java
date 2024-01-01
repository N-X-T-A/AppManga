package vn.edu.dlu.ctk45.myapplication.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;

import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.Chapter;
import vn.edu.dlu.ctk45.myapplication.Model.Image;
import vn.edu.dlu.ctk45.myapplication.Model.ReadManga;

public class DataManga {
    public static List<ReadManga> processData() {
        List<ReadManga> mangaItemList = new ArrayList<>();

        String result = ApiReaderTask.getResult();

        try {
            JSONObject mangaObject = new JSONObject(result);

            int id = mangaObject.getInt("idTruyen");
            String title = mangaObject.getString("Ten");
            int numChapter = mangaObject.getInt("SoChuong");

            List<Image> imageList = new ArrayList<>();
            JSONArray arraylistImage = mangaObject.getJSONArray("BoAnh");
            for (int i = 0; i < arraylistImage.length(); i++) {
                JSONObject chapterObject = arraylistImage.getJSONObject(i);

                int Num = chapterObject.getInt("STT");
                String Url = chapterObject.getString("Anh");
                String imageUrl = Home.url + Url;

                Image image = new Image(Num, imageUrl);
                imageList.add(image);
            }

            List<Chapter> chapterList = new ArrayList<>();
            JSONArray arraylistChapter = mangaObject.getJSONArray("DanhSachChuong");
            for (int i = 0; i < arraylistChapter.length(); i++) {
                JSONObject chapterObject = arraylistChapter.getJSONObject(i);

                int chapterId = chapterObject.getInt("idChuongTruyen");
                int numListChapter = chapterObject.getInt("SoChuong");
                int page = chapterObject.getInt("PAGE");

                Chapter chapter = new Chapter(chapterId, numListChapter, page);
                chapterList.add(chapter);
            }

            ReadManga item = new ReadManga(
                    id,
                    title,
                    numChapter,
                    imageList,
                    chapterList
            );
            mangaItemList.add(item);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return mangaItemList;
    }
}
