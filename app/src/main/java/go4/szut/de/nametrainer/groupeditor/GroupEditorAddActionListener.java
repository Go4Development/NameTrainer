package go4.szut.de.nametrainer.groupeditor;


import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;

/**
 * Created by Rene on 31.03.2016.
 */
public class GroupEditorAddActionListener implements DialogInterface.OnClickListener {

    private GroupEditorActivity activity;
    private EditText groupNameEditText;

    public GroupEditorAddActionListener(GroupEditorActivity activity, EditText groupNameEditText) {
        this.activity = activity;
        this.groupNameEditText = groupNameEditText;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which) {
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(activity, "Negative Button Selected", Toast.LENGTH_LONG).show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                String groupName = groupNameEditText.getText().toString();
                Toast.makeText(activity, "Positive Button Selected " + groupName, Toast.LENGTH_LONG).show();
                //DataSource.getDataSourceInstance(activity).insertGroup(groupName);
                break;
        }
        dialog.dismiss();
    }



}
