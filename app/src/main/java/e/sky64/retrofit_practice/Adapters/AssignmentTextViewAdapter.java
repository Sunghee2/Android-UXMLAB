package e.sky64.retrofit_practice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import e.sky64.retrofit_practice.R;

/**
 * Created by seungyeonlee on 2018. 2. 12..
 */

public class AssignmentTextViewAdapter extends BaseAdapter {
    private ArrayList<AssignmentTextViewItem> AsTextViewItem = new ArrayList<AssignmentTextViewItem>();
    private String asContent= new String();


    @Override
    public int getCount() {
        return AsTextViewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return AsTextViewItem.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_assignment, parent, false);

        }

        TextView contentTextView = (TextView) convertView.findViewById(R.id.content);

        AssignmentTextViewItem TextViewItem = AsTextViewItem.get(position);

        contentTextView.setText(TextViewItem.getContentItem());


        return convertView;
    }
    public void addContent(String content) {
        AssignmentTextViewItem item = new AssignmentTextViewItem();

        item.setContentItem(content);
        AsTextViewItem.add(item);
    }
}
