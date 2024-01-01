package vn.edu.dlu.ctk45.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Adapter.MangaAdapter;
import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Data.DataAllManga;
import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.Model.Network;
import vn.edu.dlu.ctk45.myapplication.R;

public class Manga_genre extends AppCompatActivity implements ApiReaderTask.OnTaskCompleted{
    String GenreName;
    private ProgressBar loadingProgressBar;
    LinearLayout view;
    TextView txtTitleGenre;
    ListView lvitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_genre);

        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        view = findViewById(R.id.view);
        view.setVisibility(View.GONE);
        txtTitleGenre = findViewById(R.id.txtTitleGenre);
        Intent intent = getIntent();
        GenreName = intent.getStringExtra("GenreName");

        txtTitleGenre.setText("Thể loại: " + GenreName);
        if (new Network().isNetworkAvailable(this)){
            String apiUrl = GenreManga.apiUrl + GenreName;
            new ApiReaderTask(this).execute(apiUrl);
        } else {
            Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onTaskCompleted(String result) {
        view.setVisibility(View.VISIBLE);

        lvitem = view.findViewById(R.id.lvitem);
        ArrayList<MangaItem> list = new ArrayList<>();

        List<MangaItem> mangaItemList = DataAllManga.processData();
        for (MangaItem mangaItem : mangaItemList) {
            list.add(new MangaItem(mangaItem.getId(),mangaItem.getImageUrl(),mangaItem.getTitle(),mangaItem.getAuthorName()));
        }
        lvitem.setAdapter(new MangaAdapter(list));

        lvitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MangaItem selectedItem = list.get(position);

                Intent intent = new Intent(Manga_genre.this, Manga_Detail.class);
                intent.putExtra("id",selectedItem.getId());
                startActivity(intent);
            }
        });

        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onTaskFailed(String errorMessage) {

    }
}