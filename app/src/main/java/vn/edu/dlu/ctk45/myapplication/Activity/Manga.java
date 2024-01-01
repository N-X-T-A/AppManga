package vn.edu.dlu.ctk45.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Adapter.ChapterAdapter;
import vn.edu.dlu.ctk45.myapplication.Adapter.FlipPageTransformer;
import vn.edu.dlu.ctk45.myapplication.Adapter.ImagePagerAdapter;
import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Data.DataManga;
import vn.edu.dlu.ctk45.myapplication.Data.DataMangaDetail;
import vn.edu.dlu.ctk45.myapplication.Model.Chapter;
import vn.edu.dlu.ctk45.myapplication.Model.Image;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItemDetail;
import vn.edu.dlu.ctk45.myapplication.Model.ReadManga;
import vn.edu.dlu.ctk45.myapplication.R;

public class Manga extends AppCompatActivity implements ApiReaderTask.OnTaskCompleted {

    int id,totalItemCount;
    TextView chapter,totalPage;
    ImageButton turnBack,bottomSheet,btnNext,btnPre;
    ViewPager viewPager;
    int totalPages;
    int currentPage = 0;
    EditText pageNumberEditText;
    ListView listChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        totalItemCount = intent.getIntExtra("totalItemCount",0);

        pageNumberEditText = findViewById(R.id.pageNumberEditText);

        loadData(id);

        viewPager = findViewById(R.id.viewPager);
        chapter = findViewById(R.id.chapter);
        turnBack = findViewById(R.id.turnBack);
        turnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        totalPage = findViewById(R.id.totalPage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                updateEditText();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        pageNumberEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                // Xử lý khi người dùng nhấn Done
                String inputText = pageNumberEditText.getText().toString();
                if (!inputText.isEmpty()) {
                    int requestedPage = Integer.parseInt(inputText) - 1; // Chuyển về index
                    if (requestedPage >= 0 && requestedPage < totalPages) {
                        viewPager.setCurrentItem(requestedPage);
                    } else {
                        Toast.makeText(this, "Invalid page number", Toast.LENGTH_SHORT).show();
                    }
                }


                hideKeyboard(v);

                return true; // Đã xử lý sự kiện
            }
            return false; // Không xử lý sự kiện
        });

        pageNumberEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumberEditText.setCursorVisible(true);
                String currentPageText = pageNumberEditText.getText().toString();
                if (!currentPageText.isEmpty()) {
                    int currentPage = Integer.parseInt(currentPageText) - 1;
                    if (currentPage >= 0 && currentPage < totalPages) {
                        viewPager.setCurrentItem(currentPage);
                        pageNumberEditText.setSelection(pageNumberEditText.getText().length());
                    }
                }

            }
        });

        bottomSheet = findViewById(R.id.botttom_sheet);

        btnNext = findViewById(R.id.btnNext);
        btnPre = findViewById(R.id.btnPre);

        checkId(id);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id +=1;
                checkId(id);
                loadData(id);
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id -=1;
                checkId(id);
                loadData(id);
            }
        });
    }

    private void loadData(int selectedChapterId) {
        String apiUrl = Manga_Detail.apiUrl + "/" + String.valueOf(selectedChapterId);
        new ApiReaderTask(this).execute(apiUrl);
        pageNumberEditText.setText("1");
    }

    private void checkId(int id){
        if (id == 1){
            btnPre.setEnabled(false);
            btnPre.setImageResource(R.drawable.ic_arrow_back_enabled_false);
            btnNext.setEnabled(true);
            btnNext.setImageResource(R.drawable.ic_forward);
        }else if (id == totalItemCount){
            btnNext.setEnabled(false);
            btnNext.setImageResource(R.drawable.ic_arrow_next_enabled_false);
            btnPre.setEnabled(true);
            btnPre.setImageResource(R.drawable.ic_back);
        }else {
            btnPre.setImageResource(R.drawable.ic_back);
            btnNext.setImageResource(R.drawable.ic_forward);
            btnPre.setEnabled(true);
            btnNext.setEnabled(true);
        }
    }
    @Override
    public void onTaskCompleted(String result) {
        List<ReadManga> mangaItemList = DataManga.processData();

        if (!mangaItemList.isEmpty()) {
            List<Image> images = mangaItemList.get(0).getImages();
            ImagePagerAdapter imageAdapter = new ImagePagerAdapter(this, images);
            viewPager.setAdapter(imageAdapter);
            viewPager.setPageTransformer(true, new FlipPageTransformer());
            totalPages = images.size();
            totalPage.setText(String.valueOf(totalPages));
        }
        for (ReadManga mangaItem : mangaItemList) {
            chapter.setText("Chapter " + String.valueOf(mangaItem.getNumChapter()));
        }
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    public void onTaskFailed(String errorMessage) {
        Toast.makeText(Manga.this,"Lỗi",Toast.LENGTH_SHORT).show();
    }

    private void updateEditText() {
        EditText pageNumberEditText = findViewById(R.id.pageNumberEditText);
        pageNumberEditText.setText(String.valueOf(currentPage + 1));
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        listChapter = dialog.findViewById(R.id.listChapter);

        List<ReadManga> mangaItemList = DataManga.processData();
        if (!mangaItemList.isEmpty()) {
            ReadManga mangaItem = mangaItemList.get(0);

            List<Chapter> chapters = mangaItem.getChapters();

            ChapterAdapter adapter = new ChapterAdapter(Manga.this, chapters);

            listChapter.setAdapter(adapter);

            listChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long index) {
                    id = position + 1;
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        parent.getChildAt(i).setBackgroundResource(R.color.default_color);
                    }
                    view.setBackgroundResource(R.color.pressed_color);
                    checkId(id);
                    loadData(id);
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
