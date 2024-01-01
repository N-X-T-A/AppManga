package vn.edu.dlu.ctk45.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Adapter.MangaAdapter;
import vn.edu.dlu.ctk45.myapplication.Data.DataAllManga;
import vn.edu.dlu.ctk45.myapplication.Activity.Manga_Detail;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.R;

public class AllManga extends Fragment {
    public AllManga() {
        // Required empty public constructor
    }
    ListView lvitem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_manga, container, false);
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

                Intent intent = new Intent(getActivity(), Manga_Detail.class);
                intent.putExtra("id",selectedItem.getId());
                startActivity(intent);
            }
        });

        return view;
    }

}