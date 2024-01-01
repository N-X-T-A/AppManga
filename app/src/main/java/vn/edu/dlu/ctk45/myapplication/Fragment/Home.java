package vn.edu.dlu.ctk45.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Adapter.AdaterFragment;
import vn.edu.dlu.ctk45.myapplication.Adapter.SearchAdapter;
import vn.edu.dlu.ctk45.myapplication.Api.ApiReaderTask;
import vn.edu.dlu.ctk45.myapplication.Data.DataAllManga;
import vn.edu.dlu.ctk45.myapplication.Activity.MainActivity;
import vn.edu.dlu.ctk45.myapplication.Activity.Manga_Detail;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.Model.Network;
import vn.edu.dlu.ctk45.myapplication.R;

public class Home extends Fragment implements ApiReaderTask.OnTaskCompleted {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ProgressBar loadingProgressBar;
    private LinearLayout loadView,linearLayout,linearLayoutSearch;
    private AdaterFragment adapter;
    private SearchView searchView;
    private ImageButton menu;
    private ListView lvitemSearch;
    TextView titleTextView, authorTextView;

    View rootView;
    public static String url = "http://192.168.1.7:3000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        loadingProgressBar = rootView.findViewById(R.id.loadingProgressBar);
        linearLayout = rootView.findViewById(R.id.linear_layout);

        viewPager = (ViewPager) rootView.findViewById(R.id.view_paper);
        tabLayout = rootView.findViewById(R.id.tab_layout);
        loadView = rootView.findViewById(R.id.loadView);

        loadView.setVisibility(View.GONE);

        if (new Network().isNetworkAvailable(requireContext())) {
            String apiUrl = url + "/api/truyen";
            new ApiReaderTask(this).execute(apiUrl);
        } else {
            Toast.makeText(requireContext(), "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onTaskCompleted(String result) {
        loadView.setVisibility(View.VISIBLE);

        List<MangaItem> mangaItemList = DataAllManga.processData();
        for (MangaItem mangaItem : mangaItemList) {
            LinearLayout customItemView = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_item, null);
            ImageView imageView = customItemView.findViewById(R.id.imageView);
            titleTextView = customItemView.findViewById(R.id.titleTextView);
            authorTextView = customItemView.findViewById(R.id.authorTextView);

            Picasso.get().load(mangaItem.getImageUrl()).into(imageView);

            titleTextView.setText(mangaItem.getTitle());
            authorTextView.setText(mangaItem.getAuthorName());

            customItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireContext(), Manga_Detail.class);
                    intent.putExtra("id", mangaItem.getId());
                    startActivity(intent);
                }
            });

            linearLayout.addView(customItemView);

            titleTextView.setSelected(true);
            authorTextView.setSelected(true);

            adapter = new AdaterFragment(getChildFragmentManager());
            adapter.AddFragment(new AllManga(), "Tất cả");
            adapter.AddFragment(new PopularManga(), "Phổ biến");
            adapter.AddFragment(new NewManga(), "Mới");

            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            Search(rootView);
        }

        loadingProgressBar.setVisibility(View.GONE);


    }

    @Override
    public void onTaskFailed(String errorMessage) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
    }
    private void Search(View view){
        searchView = view.findViewById(R.id.Search);
        linearLayoutSearch = view.findViewById(R.id.linearLayoutSearch);
        linearLayoutSearch.setVisibility(View.GONE);
        lvitemSearch = view.findViewById(R.id.lvitemSearch);

        menu = view.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity !=null)
                    mainActivity.openDrawer();
            }
        });

        ViewGroup.LayoutParams params = searchView.getLayoutParams();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                menu.setVisibility(View.GONE);
                searchView.setLayoutParams(params);
                linearLayoutSearch.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                searchView.setLayoutParams(params);
                linearLayoutSearch.setVisibility(View.GONE);
                menu.setVisibility(View.VISIBLE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2)
                    filterList(newText);
                else
                    filterList("");
                return true;
            }
        });
    }
    private void filterList(String text){
        ArrayList<MangaItem> list = new ArrayList<>();
        List<MangaItem> mangaItemList = DataAllManga.processData();

        for (MangaItem mangaItem : mangaItemList) {
            if (mangaItem.getTitle().toLowerCase().contains(text.toLowerCase()) || mangaItem.getAuthorName().toLowerCase().contains(text.toLowerCase()) || mangaItem.getGenre().toLowerCase().contains(text.toLowerCase())){
                list.add(new MangaItem(mangaItem.getId(),mangaItem.getImageUrl(),mangaItem.getTitle(),mangaItem.getAuthorName()));
            }
        }

       if (!text.isEmpty()){
            lvitemSearch.setAdapter(new SearchAdapter(list));

            lvitemSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MangaItem selectedItem = list.get(position);

                    Intent intent = new Intent(getActivity(), Manga_Detail.class);
                    intent.putExtra("id", selectedItem.getId());
                    startActivity(intent);
                }
            });
        }else {
            lvitemSearch.setAdapter(null);
       }
    }
}
