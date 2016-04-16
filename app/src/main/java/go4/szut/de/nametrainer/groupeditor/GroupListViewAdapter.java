package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.util.CustomAlertDialog;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupListViewAdapter extends BaseAdapter implements View.OnLongClickListener,
        CustomAlertDialog.OnUpdateListener
{

    //option identifier for editing a member
    private static final int DIALOG_OPTION_EDIT = 0;
    //option identifier for deleting a member
    private static final int DIALOG_OPTION_DELETE = 1;

    private static final int IDENTIFIER_GROUPLISTVIEW_ADAPTER = 0x8;


    private LayoutInflater layoutInflater;
    private ArrayList<Group> groups;
    private DataSource source;
    private GroupEditorActivity activity;


    public GroupListViewAdapter(GroupEditorActivity activity) {
        layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        source = DataSource.getDataSourceInstance(activity);
        source.open();
        groups = source.getAllGroups();
        source.close();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_groupeditor_listitem, parent, false);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getHorizontalScrollViewAdapter().update(getItem(position).getId());
            }
        });

        Button button = (Button)convertView.findViewById(R.id.group_add_member_button);
        button.setOnClickListener(activity);
        button.setTag(groups.get(position));
        button.setText("+");

        TextView groupNameTextView = (TextView)convertView.findViewById(R.id.group_name_textview);
        groupNameTextView.setText(groups.get(position).getName());
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.list_item_layout);
        linearLayout.setOnLongClickListener(this);
        linearLayout.setTag(groups.get(position));

        return convertView;
    }

    @Override
    public boolean onLongClick(View v) {
        LinearLayout linearLayout = (LinearLayout) v;
        final Group group = (Group) linearLayout.getTag();


        CustomAlertDialog dialog = new CustomAlertDialog(activity);
        dialog.setTitle(group.getName());
        dialog.setValue(group);
        dialog.setAdapter(this);
        dialog.setArrayAdapter(android.R.layout.select_dialog_item, R.array.groupeditor_item_dialog_options);
        dialog.setOptionSelectionListener(new CustomAlertDialog.DefaultSelectionListener() {

            @Override
            public void onDefault(CustomAlertDialog.Interface i) {
                Group group = (Group) i.getValue();
                switch (i.getSelection()) {
                    case DIALOG_OPTION_EDIT:
                        onEdit(group);
                        break;
                    case DIALOG_OPTION_DELETE:
                        onDelete(group);
                        group = i.getAdapter(GroupListViewAdapter.class).getItem(0);
                        if(group != null) {
                            activity.getHorizontalScrollViewAdapter().update(group.getId());
                        } else {

                        }
                        break;
                }
            }

        });
        dialog.show();


        return false;
    }

    private void onEdit(Group group) {

        CustomAlertDialog dialog = new CustomAlertDialog(activity);
        dialog.setDialogView(R.layout.activity_groupeditor_edittext);
        dialog.addView(R.id.group_add_edittext);
        dialog.setUpdateListener(IDENTIFIER_GROUPLISTVIEW_ADAPTER, this);
        dialog.getView(EditText.class, R.id.group_add_edittext).setText(group.getName());
        Util.Input.setTextInputFilter(dialog.getView(EditText.class, R.id.group_add_edittext));
        dialog.setPositiveButtonTitle(R.string.groupeditor_edit_action_posbutton);
        dialog.setNegativeButtonTitle(R.string.groupeditor_edit_action_negbutton);
        dialog.setValue(group);
        dialog.setOptionSelectionListener(new CustomAlertDialog.SimpleOnOptionSelectionListener() {
            @Override
            public void onPositive(CustomAlertDialog.Interface i) {
                Group group = (Group) i.getValue();
                EditText groupNameEditText = i.getView(EditText.class, R.id.group_add_edittext);
                String groupName = groupNameEditText.getText().toString();
                group.setName(groupName);
                DataSource source = i.getDataSource();
                source.open();
                source.updateGroup(group);
                source.close();
                i.close(null);
            }

            @Override
            public void onNegative(CustomAlertDialog.Interface i) {
                i.close(null);
            }

        });
        dialog.show();
    }

    private void onDelete(Group group) {
        source.open();
        source.deleteGroup(group);
        source.close();
        this.notifyDataSetChanged();
    }

    @Override
    public void update(int updateIdentifier, Object data) {
        switch(updateIdentifier) {
            case IDENTIFIER_GROUPLISTVIEW_ADAPTER:
                notifyDataSetChanged();
                break;
        }
    }
}



