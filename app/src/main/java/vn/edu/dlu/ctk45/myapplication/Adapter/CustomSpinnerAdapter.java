package vn.edu.dlu.ctk45.myapplication.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Model.Chapter;

public class CustomSpinnerAdapter extends ArrayAdapter<Chapter> {

    public CustomSpinnerAdapter(Context context, int resource, List<Chapter> items) {
        super(context, resource, items);
    }

    @Override
    public boolean isEnabled(int position) {
        // Disable the first item (index 0) to make it appear as a hint
        return position != 0;
    }
}