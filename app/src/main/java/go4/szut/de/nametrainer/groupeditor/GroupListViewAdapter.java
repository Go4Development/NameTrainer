package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<Group> groups;
    private DataSource source;
    private MemberAddActionListener addActionListener;
    private GroupListViewItemListener listener;


    public GroupListViewAdapter(GroupEditorActivity activity, MemberAddActionListener addActionListener) {
        layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        source = DataSource.getDataSourceInstance(activity);
        source.open();
        groups = source.getAllGroups();
        source.close();

        listener = new GroupListViewItemListener(activity, this);
        this.addActionListener = addActionListener;
    }


    public void notifyDataSetChanged() {
        source.open();
        groups = source.getAllGroups();
        source.close();
        super.notifyDataSetChanged();
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
            convertView = layoutInflater.inflate(R.layout.activity_groupeditor_listitem, parent, false);
        }

        Button button = (Button)convertView.findViewById(R.id.group_add_member_button);
        button.setOnClickListener(addActionListener);
        button.setTag(groups.get(position));
        button.setText("+");

        TextView groupNameTextView = (TextView)convertView.findViewById(R.id.group_name_textview);
        groupNameTextView.setTag(groups.get(position));
        groupNameTextView.setOnLongClickListener(listener);
        groupNameTextView.setText(groups.get(position).getName());

        return convertView;
    }

    public MemberAddActionListener getAddActionListener() {
        return addActionListener;
    }

}
