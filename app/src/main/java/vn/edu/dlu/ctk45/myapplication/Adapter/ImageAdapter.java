package vn.edu.dlu.ctk45.myapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Model.Image;
import vn.edu.dlu.ctk45.myapplication.Model.ReadManga;

public class ImageAdapter extends ArrayAdapter<Image> {
    private Context context;

    public ImageAdapter(Context context, List<Image> images) {
        super(context, 0, images);
        this.context = context;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        if (imageView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        Image currentImage = getItem(position);
        if (currentImage != null) {
            Picasso.get().load(currentImage.getImageUrl()).into(imageView);
        }

        return imageView;
    }
}
