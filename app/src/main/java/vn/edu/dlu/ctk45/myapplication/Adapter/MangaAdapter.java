package vn.edu.dlu.ctk45.myapplication.Adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.edu.dlu.ctk45.myapplication.Model.MangaItem;
import vn.edu.dlu.ctk45.myapplication.R;

public class MangaAdapter extends BaseAdapter {

    ArrayList<MangaItem> list;

    public MangaAdapter(ArrayList<MangaItem> list) {
        this.list = list;
    }

    public void updateData(ArrayList<MangaItem> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
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
        View view = View.inflate(parent.getContext(), R.layout.custom_item_list, null);

        MangaItem item = (MangaItem)getItem(position);

        ImageView img = view.findViewById(R.id.imageView);
        TextView title = view.findViewById(R.id.titleTextView);
        TextView author = view.findViewById(R.id.authorTextView);

        Picasso.get().load(item.getImageUrl()).into(img);
        title.setText(item.getTitle());
        author.setText(item.getAuthorName());

        notifyDataSetChanged();

        return view;
    }
}


