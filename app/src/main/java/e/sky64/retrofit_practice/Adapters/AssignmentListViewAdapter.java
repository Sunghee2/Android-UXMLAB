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
 * Created by seungyeonlee on 2018. 2. 7..
 */

public class AssignmentListViewAdapter extends BaseAdapter {
    private ArrayList<AssignmentListViewItem> asListViewItemList = new ArrayList<AssignmentListViewItem>();

    public AssignmentListViewAdapter() {

    }

    @Override
    public int getCount() {
        return asListViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return asListViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final int pos = position;
        int res = 0;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);


        AssignmentListViewItem listViewItem = asListViewItemList.get(position);

        titleTextView.setText(listViewItem.getAsList_title());
        descTextView.setText(listViewItem.getAsList_content());

        return convertView;
    }

    public void addAsList(String title,String listContent) {
        AssignmentListViewItem item = new AssignmentListViewItem();
        item.setAsList_title(title);
        item.setAsList_content(listContent);
        asListViewItemList.add(item);
    }


}
