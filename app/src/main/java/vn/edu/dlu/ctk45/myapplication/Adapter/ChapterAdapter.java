package vn.edu.dlu.ctk45.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import vn.edu.dlu.ctk45.myapplication.Model.Chapter;
import vn.edu.dlu.ctk45.myapplication.R;

public class ChapterAdapter extends BaseAdapter {
    private Context context;
    private List<Chapter> chapterList;

    public ChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return chapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.lvitemchapter, parent, false);

//            if (position == 0) {
//                rowView.setBackgroundResource(R.color.pressed_color);
//            }

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.numChapterTextView = rowView.findViewById(R.id.numChapter);
            viewHolder.txtChapterTextView = rowView.findViewById(R.id.txtChapter);
            viewHolder.txtPage = rowView.findViewById(R.id.txtPage);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        Chapter chapter = chapterList.get(position);

        String formattedChapterNum = String.format("%02d", chapter.getNumChapter());

        holder.numChapterTextView.setText(formattedChapterNum);
        holder.txtChapterTextView.setText("Chapter " + String.valueOf(chapter.getNumChapter()));
        holder.txtPage.setText(String.valueOf(chapter.getPage() + " Page"));

        return rowView;
    }

    static class ViewHolder {
        TextView numChapterTextView;
        TextView txtChapterTextView;
        TextView txtPage;
    }
}
