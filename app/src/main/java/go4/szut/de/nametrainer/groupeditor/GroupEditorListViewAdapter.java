package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private ArrayList<String> groups;


    public GroupEditorListViewAdapter(Context context) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        groups = Util.createArrayListWithNonSense(10);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_groupeditor_list_textview, parent, false);
        }

        TextView groupNameTextView = (TextView)convertView;
        groupNameTextView.setText(groups.get(position));

        return convertView;
    }
}
