package vn.edu.dlu.ctk45.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.dlu.ctk45.myapplication.Adapter.MangaAdapter;
import vn.edu.dlu.ctk45.myapplication.Data.DataMangaLiked;
import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.R;

public class FavoritesManga extends AppCompatActivity {

    ListView lvitem;
    String token;
    TextView Notification,txtTitleFav;
    private ArrayList<MangaItem> mangaItems;
    private MangaAdapter mangaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_manga);

        token = MainActivity.token;

        if (token.isEmpty()) {
            Toast.makeText(FavoritesManga.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            Intent signIn = new Intent(FavoritesManga.this,SignIn.class);
            startActivity(signIn);
        } else {
            lvitem = findViewById(R.id.lvitem);
            Notification = findViewById(R.id.Notification);
            mangaItems = new ArrayList<>();
            mangaAdapter = new MangaAdapter(mangaItems);
            txtTitleFav = findViewById(R.id.txtTitleFav);
            // Set up the adapter first, then fetch data
            lvitem.setAdapter(mangaAdapter);

            updateData();

            lvitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MangaItem selectedItem = mangaItems.get(position);

                    Intent intent = new Intent(FavoritesManga.this, Manga_Detail.class);
                    intent.putExtra("id",selectedItem.getId());
                    intent.putExtra("Activity",FavoritesManga.class);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                updateData();
            }
        }
    }
    private void updateData() {
        DataMangaLiked.listLikedUser(token, new DataMangaLiked.OnMangaDataFetchListener() {
            @Override
            public void onMangaDataFetch(ArrayList<MangaItem> fetchedMangaItems) {
                mangaItems.clear();
                mangaItems.addAll(fetchedMangaItems);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mangaAdapter.notifyDataSetChanged();

                        if (mangaItems.isEmpty()) {
                            lvitem.setVisibility(View.GONE);
                            Notification.setVisibility(View.VISIBLE);
                            txtTitleFav.setVisibility(View.GONE);
                        } else {
                            lvitem.setVisibility(View.VISIBLE);
                            Notification.setVisibility(View.GONE);
                            txtTitleFav.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavoritesManga.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}