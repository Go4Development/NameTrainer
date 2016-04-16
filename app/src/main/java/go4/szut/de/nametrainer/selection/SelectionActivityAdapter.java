package go4.szut.de.nametrainer.selection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Michelé on 26.03.2016.
 */
public class SelectionActivityAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private SelectionActivity selectionActivity;
    private DataSource source;

    private ArrayList<Group> groups;

    public SelectionActivityAdapter(SelectionActivity selectionActivity) {
        this.selectionActivity = selectionActivity;
        inflater = (LayoutInflater)selectionActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        source = DataSource.getDataSourceInstance(selectionActivity);
        loadGroupsFromDatabase();
    }

    private void loadGroupsFromDatabase() {
        source.open();
        groups = source.getAllGroups();
        source.close();
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
            convertView = inflater.inflate(R.layout.activity_selection_listitem, parent, false);
        }

        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.selection_listitem_layout);
        linearLayout.setTag(groups.get(position));
        linearLayout.setOnClickListener(selectionActivity);

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.groupname_textview);
        groupNameTextView.setText(groups.get(position).getName());

        TextView memberCountTextView = (TextView)convertView.findViewById(R.id.membercount_textview);
        memberCountTextView.setText(Util.Res.strF(selectionActivity, R.string.prefix_students_count,
                String.valueOf(groups.get(position).getMemberCount(selectionActivity))));

        return convertView;
    }
}
