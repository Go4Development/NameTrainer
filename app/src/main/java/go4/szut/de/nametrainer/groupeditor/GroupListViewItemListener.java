package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;

/**
 * Created by Rene on 31.03.2016.
 */
public class GroupListViewItemListener implements View.OnLongClickListener {

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
                context, groupNameEditText, adapter, group);

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

}
