package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;

/**
 * Created by Rene on 31.03.2016.
 */
public class GroupEditorEditActionListener implements DialogInterface.OnClickListener {

    private Context context;
    private EditText groupNameEditText;
    private GroupListViewAdapter groupListViewAdapter;
    private Group group;

    public GroupEditorEditActionListener(Context context, EditText groupNameEditText,
                                         GroupListViewAdapter groupListViewAdapter, Group group) {
        this.context = context;
        this.groupListViewAdapter = groupListViewAdapter;
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
