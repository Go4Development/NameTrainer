package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<Group> groups;
    private DataSource source;
    private GroupEditorActivity.MemberAddActionListener addActionListener;
    private GroupListViewItemListener listener;


    public GroupListViewAdapter(GroupEditorActivity activity, GroupEditorActivity.MemberAddActionListener addActionListener) {
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
    public Group getItem(int position) {
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

    public GroupEditorActivity.MemberAddActionListener getAddActionListener() {
        return addActionListener;
    }

    /**
     * Created by Rene on 31.03.2016.
     */
    public static class GroupListViewItemListener implements View.OnLongClickListener {

        //option identifier for editing a member
        private static final int DIALOG_OPTION_EDIT = 0;
        //option identifier for deleting a member
        private static final int DIALOG_OPTION_DELETE = 1;

        private Context context;
        private GroupListViewAdapter adapter;
        private ArrayAdapter<String> groupItemDialogAdapter;

        public GroupListViewItemListener(Context context, GroupListViewAdapter adapter) {
            this.context = context;
            this.adapter = adapter;

            groupItemDialogAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.select_dialog_item,
                    context.getResources().getStringArray(R.array.groupeditor_item_dialog_options));
        }

        @Override
        public boolean onLongClick(View v) {
            TextView textView = (TextView)v;
            final Group group = (Group)textView.getTag();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(group.getName());
            builder.setCancelable(true);
            builder.setAdapter(groupItemDialogAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch(which) {
                        case DIALOG_OPTION_EDIT:
                            onEdit(group);
                            break;
                        case DIALOG_OPTION_DELETE:
                            onDelete(group);
                            break;
                    }
                }
            });
            builder.show();
            return false;
        }

        private void onEdit(Group group) {
            EditText groupNameEditText = new EditText(context);
            groupNameEditText.setText(group.getName());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            groupNameEditText.setLayoutParams(layoutParams);

            //Listener for AlertDialog buttons
            GroupEditorEditActionListener listener = new GroupEditorEditActionListener(
                    context, groupNameEditText, group);

            //AlertDialog that pops up
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setPositiveButton(context.getResources().getString(R.string.groupeditor_edit_action_posbutton), listener)
                    .setNegativeButton(context.getResources().getString(R.string.groupeditor_edit_action_negbutton), listener);
            alertDialog.setView(groupNameEditText);
            alertDialog.show();
        }

        private void onDelete(Group group) {
            DataSource source = DataSource.getDataSourceInstance(context);
            source.open();
            source.deleteGroup(group);
            source.close();
            adapter.notifyDataSetChanged();
        }

        /**
         * Created by Rene on 31.03.2016.
         */
        public static class GroupEditorEditActionListener implements DialogInterface.OnClickListener {

            private Context context;
            private EditText groupNameEditText;
            private Group group;

            public GroupEditorEditActionListener(Context context, EditText groupNameEditText, Group group) {
                this.context = context;
                this.groupNameEditText = groupNameEditText;
                this.group = group;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        //chill out
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        String groupName = groupNameEditText.getText().toString();
                        group.setName(groupName);
                        DataSource source = DataSource.getDataSourceInstance(context);
                        source.open();
                        source.updateGroup(group);
                        source.close();
                        break;
                }
                dialog.dismiss();
            }
        }
    }
}
