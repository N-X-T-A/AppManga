package vn.edu.dlu.ctk45.myapplication.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Model.Genre;

public class DataGenre {
    public static List<Genre> processData() {
        List<Genre> genreList = new ArrayList<>();

        String result = ApiReaderTask.getResult();

        try {
            JSONArray genreArray = new JSONArray(result);

            for (int i = 0; i < genreArray.length(); i++) {
                JSONObject genreObject = genreArray.getJSONObject(i);

                int idTheLoai = genreObject.getInt("idTheLoai");
                String tenTheLoai = genreObject.getString("TenTheLoai");

                Genre genre = new Genre(idTheLoai, tenTheLoai);
                genreList.add(genre);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return genreList;
    }
}
