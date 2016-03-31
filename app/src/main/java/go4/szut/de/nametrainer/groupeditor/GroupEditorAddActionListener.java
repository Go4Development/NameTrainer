package go4.szut.de.nametrainer.groupeditor;


import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;

/**
 * Created by Rene on 31.03.2016.
 */
public class GroupEditorAddActionListener implements DialogInterface.OnClickListener {

    private Context context;
    private EditText groupNameEditText;
    private GroupListViewAdapter groupListViewAdapter;

    public GroupEditorAddActionListener(Context context, EditText groupNameEditText,
                                        GroupListViewAdapter groupListViewAdapter) {
        this.context = context;
        this.groupNameEditText = groupNameEditText;
        this.groupListViewAdapter = groupListViewAdapter;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which) {
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(context, "Negative Button Selected", Toast.LENGTH_LONG).show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                String groupName = groupNameEditText.getText().toString();
                //retrieves the DataSource instance and inserts the new group
                DataSource source = DataSource.getDataSourceInstance(context);
                source.open();
                Group group = source.insertGroup(groupName);
                source.close();
                //notifies the adapter to update
                groupListViewAdapter.notifyDataSetChanged(group);
                break;
        }
        dialog.dismiss();
    }



}
