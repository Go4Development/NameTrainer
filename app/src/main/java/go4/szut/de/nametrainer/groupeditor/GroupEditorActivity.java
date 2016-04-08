package go4.szut.de.nametrainer.groupeditor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.util.RealPathUtil;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorActivity extends AppCompatActivity
    implements View.OnLongClickListener {

    public static final int SELECT_PICTURE_EDIT = 1337;
    public static final int SELECT_PICTURE_ADD = 1338;

    //option identifier for editing a member
    private static final int DIALOG_OPTION_EDIT = 0;
    //option identifier for deleting a member
    private static final int DIALOG_OPTION_DELETE = 1;

    //holds a bunch of horizontal positioned images of students of the current selected group
    private CustomHorizontalScrollView portraitScrollView;
    private HorizontalScrollViewAdapter horizontalScrollViewAdapter;
    private MemberAddActionListener addActionListener;

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
        addActionListener  = new MemberAddActionListener(this);
        groupListViewAdapter = new GroupListViewAdapter(this, addActionListener);

        /**
         * Load Data - End
         */

        //portrait stuff
        portraitScrollView = (CustomHorizontalScrollView)findViewById(R.id.portrait_scrollview);

        //grouplist stuff
        groupListView = (ListView)findViewById(R.id.group_listview);
        groupListView.setAdapter(groupListViewAdapter);

        //sets the list containing the GroupListViewItems
        Group group = new Group();
        group.setId(1);
        group.setName("T15A");

        horizontalScrollViewAdapter = new HorizontalScrollViewAdapter(this, portraitScrollView, group);
        portraitScrollView.setAdapter(horizontalScrollViewAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == SELECT_PICTURE_EDIT) {
                if(data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Member member = getIntent().getParcelableExtra("member");
                    horizontalScrollViewAdapter.getListener().onImageSelected(RealPathUtil.getRealPathFromURI_API19(this,selectedImageUri), member);
                }
            } else if(requestCode == SELECT_PICTURE_ADD) {
                if(data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Member member = new Member();
                    member.setImagePath(selectedImageUri.toString());
                    getIntent().putExtra("member", member);
                    groupListViewAdapter.getAddActionListener().onImageSelected(RealPathUtil.getRealPathFromURI_API19(this,selectedImageUri));

                }
            }
            else if(requestCode == HorizontalScrollViewItem.CHOOSE_IMAGE){
                if(data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Util.l(this, "Das ist die URI:" + selectedImageUri);

                }
            }
        }



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
    public boolean onLongClick(View v) {
        HorizontalScrollViewItem item = (HorizontalScrollViewItem)v;
        openItemDialog(item);
        return true;
    }

    private void openItemDialog(final HorizontalScrollViewItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle(item.getName());
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

    }

    private void onDelete(HorizontalScrollViewItem item) {

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

    public HorizontalScrollViewAdapter getHorizontalScrollViewAdapter() {
        return horizontalScrollViewAdapter;
    }
}