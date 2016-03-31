package go4.szut.de.nametrainer.groupeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    //option identifier for editing a member
    private static final int DIALOG_OPTION_EDIT = 0;
    //option identifier for deleting a member
    private static final int DIALOG_OPTION_DELETE = 1;

    //request code identifier for editing a member
    private static final int RC_MEMBER_EDITOR = 1337;

    //holds a bunch of horizontal positioned images of students of the current selected group
    private CustomHorizontalScrollView portraitScrollView;

    private ArrayList<HorizontalScrollViewItem> portraitItems;

    //holds a list of groups containing multiple students
    private ListView groupListView;
    //holds the data for the groupListView
    private GroupListViewAdapter groupListViewAdapter;

    //holds all options that needs to be displayed in OptionsDialog
    //of a GroupListViewItem if a onLongClick event occurs
    private ArrayAdapter<String> groupItemDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the layout to the specified "groupeditor layout"
        setContentView(R.layout.activity_groupeditor);

        //hides the ActionBar of AppCompatActivity class
        //Util.hideActionBar(this);

        /**
         * Load Data - Start
         */

        //TODO Database Request right here

        groupItemDialogAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item,
                getResources().getStringArray(R.array.groupeditor_item_dialog_options));

        groupListViewAdapter = new GroupListViewAdapter(this);
        portraitItems = new ArrayList<HorizontalScrollViewItem>();

        for(int i = 0; i < 10; i++) {
            HorizontalScrollViewItem item = new HorizontalScrollViewItem(this, "Hans Vadder", "Vorname" + i, "Nachname" + i);
            item.setOnClickListener(this);
            item.setOnLongClickListener(this);
            portraitItems.add(item);
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
        portraitScrollView.setPortraitItems(portraitItems);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.groupeditor_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.group_add_action:
                onAddGroup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        HorizontalScrollViewItem item = (HorizontalScrollViewItem)v;
        //to something here
    }

    @Override
    public boolean onLongClick(View v) {
        HorizontalScrollViewItem item = (HorizontalScrollViewItem)v;
        openItemDialog(item);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case RC_MEMBER_EDITOR:
                    //TODO hier Daten in der Datenbank updaten
                    break;
            }
        }
    }

    private void openItemDialog(final HorizontalScrollViewItem item) {
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

    private void onEdit(HorizontalScrollViewItem item) {
        Intent intent = new Intent(this, MemberEditorActivity.class);
        //intent.putExtra( HERE PARCELABLE EXTRA )
        startActivityForResult(intent, RC_MEMBER_EDITOR);
    }

    private void onDelete(HorizontalScrollViewItem item) {
        //TODO Do a delete on the data in database corresponding to this item
        Toast.makeText(this, item.getName() + " on Remove", Toast.LENGTH_LONG).show();
    }

    private void onAddGroup() {
        //TODO Komponenten vll auslagern
        //EditText that is on AlertDialog for input
        EditText groupNameEditText = new EditText(this);
        groupNameEditText.setHint(getResources().getString(R.string.groupeditor_add_action_edittext));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        groupNameEditText.setLayoutParams(layoutParams);

        //Listener for AlertDialog buttons
        GroupEditorAddActionListener listener = new GroupEditorAddActionListener(
                this, groupNameEditText, groupListViewAdapter);

        //AlertDialog that pops up
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.groupeditor_add_action_posbutton), listener)
                .setNegativeButton(getResources().getString(R.string.groupeditor_add_action_negbutton), listener);
        alertDialog.setView(groupNameEditText);
        alertDialog.show();
    }


}