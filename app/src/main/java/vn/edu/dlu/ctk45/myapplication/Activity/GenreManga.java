package vn.edu.dlu.ctk45.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Adapter.GenreAdapter;
import vn.edu.dlu.ctk45.myapplication.Adapter.MangaAdapter;
import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Data.DataGenre;
import vn.edu.dlu.ctk45.myapplication.Data.DataMangaDetail;
import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.Genre;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItemDetail;
import vn.edu.dlu.ctk45.myapplication.Model.Network;
import vn.edu.dlu.ctk45.myapplication.R;

public class GenreManga extends AppCompatActivity implements ApiReaderTask.OnTaskCompleted{
    private ProgressBar loadingProgressBar;
    public static String apiUrl;
    GridView gridView;
    LinearLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_manga);
        gridView = findViewById(R.id.gridview);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        view = findViewById(R.id.view);
        view.setVisibility(View.GONE);

        if (new Network().isNetworkAvailable(this)){
            apiUrl = Home.url + "/api/theloai/";
            new ApiReaderTask(this).execute(apiUrl);
        } else {
            Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.GONE);
        }

    }


    @Override
    public void onTaskCompleted(String result) {
        view.setVisibility(View.VISIBLE);

        ArrayList<Genre> list = new ArrayList<>();

        int[] imageIds = {R.drawable.image3, R.drawable.image3, R.drawable.phieuluu,R.drawable.hanhdong,R.drawable.bian,R.drawable.funny,R.drawable.school};

        List<Genre> genreList = DataGenre.processData();
        for (Genre genre : genreList){
            list.add(new Genre(genre.getIdTheLoai(),genre.getTenTheLoai()));
        }

        gridView.setAdapter(new GenreAdapter(list, imageIds));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Genre selectedItem = list.get(position);

                Intent intent = new Intent(GenreManga.this, Manga_genre.class);
                intent.putExtra("GenreName",selectedItem.getTenTheLoai());
                startActivity(intent);
            }
        });

        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onTaskFailed(String errorMessage) {

    }
}