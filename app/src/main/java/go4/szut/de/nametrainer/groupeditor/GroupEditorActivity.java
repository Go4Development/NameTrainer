package go4.szut.de.nametrainer.groupeditor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    private static final int DIALOG_OPTION_EDIT = 0;
    private static final int DIALOG_OPTION_DELETE = 1;

    //holds a bunch of horizontal positioned images of students of the current selected group
    private CustomHorizontalScrollView portraitScrollView;

    private ArrayList<GroupListViewItem> groupListViewItemList;

    //holds a list of groups containing multiple students
    private ListView groupListView;
    //holds the data for the groupListView
    private GroupEditorListViewAdapter groupListViewAdapter;

    //holds all options that needs to be displayed in OptionsDialog
    //of a GroupListViewItem if a onLongClick event occurs
    private ArrayAdapter<String> groupItemDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the layout to the specified "groupeditor layout"
        setContentView(R.layout.activity_groupeditor);

        //hides the ActionBar of AppCompatActivity class
        Util.hideActionBar(this);

        /**
         * Load Data - Start
         */

        //TODO Database Request right here

        groupItemDialogAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item,
                getResources().getStringArray(R.array.groupeditor_item_dialog_options));

        groupListViewAdapter = new GroupEditorListViewAdapter(this);
        groupListViewItemList = new ArrayList<GroupListViewItem>();

        for(int i = 0; i < 10; i++) {
            GroupListViewItem item = new GroupListViewItem(this, "Hans Vadder", "Vorname" + i, "Nachname" + i);
            item.setOnClickListener(this);
            item.setOnLongClickListener(this);
            groupListViewItemList.add(item);
        }

        /**
         * Load Data - End
         */

        //portrait stuff
        portraitScrollView = (CustomHorizontalScrollView)findViewById(R.id.portrait_scrollview);

        //grouplist stuff
        groupListView = (ListView)findViewById(R.id.group_listview);
        groupListView.setAdapter(groupListViewAdapter);

        //sets the list containing the GroupListViewItems
        portraitScrollView.setGroupViewItemList(groupListViewItemList);

    }

    @Override
    public void onClick(View v) {
        GroupListViewItem item = (GroupListViewItem)v;
        //TODO Maybe for other function or displaying information
    }

    @Override
    public boolean onLongClick(View v) {
        GroupListViewItem item = (GroupListViewItem)v;
        openItemDialog(item);
        return true;
    }



    private void openItemDialog(final GroupListViewItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.getName());
        builder.setCancelable(true);
        builder.setAdapter(groupItemDialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DIALOG_OPTION_EDIT:
                        onEdit(item);
                        break;
                    case DIALOG_OPTION_DELETE:
                        onDelete(item);
                        break;
                }
            }
        });
        builder.show();
    }

    private void onEdit(GroupListViewItem item) {
        //TODO Open an EditDialog in order to be able to change data of item and save it to database
        Toast.makeText(this, item.getName() + " on Edit", Toast.LENGTH_LONG).show();
    }

    private void onDelete(GroupListViewItem item) {
        //TODO Do a delete on the data in database corresponding to this item
        Toast.makeText(this, item.getName() + " on Remove", Toast.LENGTH_LONG).show();
    }



}