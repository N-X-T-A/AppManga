package vn.edu.dlu.ctk45.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Model.Image;
import vn.edu.dlu.ctk45.myapplication.R;

public class ImagePagerAdapter extends PagerAdapter {
    private final Context context;
    private final List<Image> imageList;

    public ImagePagerAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_image, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);

        Image currentImage = imageList.get(position);
        if (currentImage != null) {
            Picasso.get().load(currentImage.getImageUrl()).into(imageView);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
        container.removeView((View) object);
    }
}