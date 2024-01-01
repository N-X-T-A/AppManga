package vn.edu.dlu.ctk45.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Adapter.ChapterAdapter;
import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Data.DataMangaDetail;
import vn.edu.dlu.ctk45.myapplication.Data.DataMangaLiked;
import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.Chapter;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItemDetail;
import vn.edu.dlu.ctk45.myapplication.Model.Network;
import vn.edu.dlu.ctk45.myapplication.R;

public class Manga_Detail extends AppCompatActivity implements ApiReaderTask.OnTaskCompleted{
    TextView txtTitle,txtFavorites,title_manga,txtGenre;
    ImageView imgManga;
    Button btnRead;
    ListView lvChapter;
    ImageButton turnBack,btnFav;
    LinearLayout loadView;
    int id;
    private ProgressBar loadingProgressBar;
    public static String apiUrl;
    String token = MainActivity.token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_info);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        lvChapter = findViewById(R.id.lvChapter);

        loadView = findViewById(R.id.loadView);

        loadView.setVisibility(View.GONE);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        if (new Network().isNetworkAvailable(this)){
            apiUrl = Home.url + "/api/truyen/" + String.valueOf(id);
            new ApiReaderTask(this).execute(apiUrl);
        } else {
            Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.GONE);
        }

        txtTitle = findViewById(R.id.txtTitle);
        imgManga = findViewById(R.id.imgManga);
        txtFavorites = findViewById(R.id.txtScore);
        title_manga = findViewById(R.id.title_manga);
        txtGenre = findViewById(R.id.txtGenre);

        btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent read = new Intent(Manga_Detail.this, Manga.class);
                read.putExtra("id",id);
                startActivity(read);
            }
        });

        turnBack = findViewById(R.id.turnBack);
        turnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnFav = findViewById(R.id.btnFav);
        DataMangaLiked.isLiked(token, id, new DataMangaLiked.OnCheckLikedListener() {
            @Override
            public void onCheckLiked(boolean isChecked) {
                // Use a mutable boolean variable
                final boolean[] checked = {isChecked};

                handleIsLiked(checked[0]);

                btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (token.isEmpty()) {
                            Toast.makeText(Manga_Detail.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                            Intent signIn = new Intent(Manga_Detail.this, SignIn.class);
                            signIn.putExtra("Activity", Manga_Detail.class);
                            signIn.putExtra("id", id);
                            startActivity(signIn);
                        } else {
                            // Toggle the mutable boolean variable
                            checked[0] = !checked[0];

                            if (checked[0]) {
                                DataMangaLiked.Like(token, id, new DataMangaLiked.OnCheckLikedListener() {
                                    @Override
                                    public void onCheckLiked(boolean isChecked) {
                                        btnFav.setImageResource(R.drawable.ic_fav_activated);
                                        Toast.makeText(Manga_Detail.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                        txtFavorites.setText(String.valueOf(Integer.parseInt(txtFavorites.getText().toString()) + 1));
                                    }
                                });
                            } else {
                                DataMangaLiked.UnLike(token, id, new DataMangaLiked.OnCheckLikedListener() {
                                    @Override
                                    public void onCheckLiked(boolean isChecked) {
                                        btnFav.setImageResource(R.drawable.ic_fav_unactivated);
                                        setResult(RESULT_OK);
                                        Toast.makeText(Manga_Detail.this, "Đã bỏ khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                        txtFavorites.setText(String.valueOf(Integer.parseInt(txtFavorites.getText().toString()) - 1));
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

    }
    @Override
    public void onTaskCompleted(String result) {
        loadView.setVisibility(View.VISIBLE);

        ExpandableTextView expTv = (ExpandableTextView) findViewById(R.id.expand_text_view).findViewById(R.id.expand_text_view);

        List<MangaItemDetail> mangaItemList = DataMangaDetail.processData();
        for (MangaItemDetail mangaItem : mangaItemList) {
            if (mangaItem.getId() == id){
                txtTitle.setText(mangaItem.getTitle());
                Picasso.get().load(mangaItem.getImageUrl()).into(imgManga);
                txtFavorites.setText(String.valueOf(mangaItem.getFavorites()));
                title_manga.setText(mangaItem.getTitle());
                txtGenre.setText(mangaItem.getGenre());
                expTv.setText(mangaItem.getDescribe());
            }
        }


        if (!mangaItemList.isEmpty()) {
            MangaItemDetail mangaItemDetail = mangaItemList.get(0);

            List<Chapter> chapters = mangaItemDetail.getChapters();

            ChapterAdapter adapter = new ChapterAdapter(this, chapters);

            lvChapter.setAdapter(adapter);

            lvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long index) {
                    id = position + 1;
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        parent.getChildAt(i).setBackgroundResource(R.color.default_color);
                    }
                    view.setBackgroundResource(R.color.pressed_color);
                    Intent read = new Intent(Manga_Detail.this, Manga.class);
                    read.putExtra("id",id);
                    read.putExtra("totalItemCount",adapter.getCount());
                    read.putExtra("selectedItemPosition",position);
                    startActivity(read);
                }
            });

        }
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onTaskFailed(String errorMessage) {

    }

    private void handleIsLiked(boolean isChecked) {
        if (token.isEmpty()){
            btnFav.setImageResource(R.drawable.ic_fav_unactivated);
        }else {
            if (isChecked) {
                btnFav.setImageResource(R.drawable.ic_fav_activated);
            } else {
                btnFav.setImageResource(R.drawable.ic_fav_unactivated);
            }
        }
    }

    private void LoadData(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent get = getIntent();
        Class<?> activity = (Class<?>) get.getSerializableExtra("Activity");
        Intent intent;
        if (activity == null)
            intent = new Intent(Manga_Detail.this,MainActivity.class);
        else {
            intent = new Intent(Manga_Detail.this, activity);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}