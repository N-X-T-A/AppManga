package vn.edu.dlu.ctk45.myapplication.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.dlu.ctk45.myapplication.Model.Genre;
import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.R;

public class GenreAdapter extends BaseAdapter {
    ArrayList<Genre> list;
    private int[] imageIds;
    public GenreAdapter(ArrayList<Genre> list, int[] imageIds) {
        this.list = list;
        this.imageIds = imageIds;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.custom_list_genre, null);

        Genre item = (Genre)getItem(position);
        TextView title = view.findViewById(R.id.titleTextView);
        ImageView imageView = view.findViewById(R.id.imageView);

        title.setText(item.getTenTheLoai());
        imageView.setImageResource(imageIds[position]);

        return view;
    }
}
