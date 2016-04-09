package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.options.OptionsActivity;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //hides the ActionBar of AppCompatActivity class
        //Util.hideActionBar(this);

        /**
         * Load Data - Start
         */


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
        Group group;
        if(groupListViewAdapter.getCount() >= 1){
            group = groupListViewAdapter.getItem(0);
        }else {
            group = new Group();
            group.setId(1);
            group.setName("T15A");
        }
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
                    horizontalScrollViewAdapter.getListener().onImageSelected(selectedImageUri.toString(), member);
                }
            } else if(requestCode == SELECT_PICTURE_ADD) {
                if(data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Member member = new Member();
                    member.setImagePath(selectedImageUri.toString());
                    getIntent().putExtra("member", member);
                    groupListViewAdapter.getAddActionListener().onImageSelected(selectedImageUri);

                }
            }
            else if(requestCode == HorizontalScrollViewAdapter.HorizontalScrollViewItem.CHOOSE_IMAGE){
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
            case R.id.action_settings:
                Intent intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View v) {
        HorizontalScrollViewAdapter.HorizontalScrollViewItem item = (HorizontalScrollViewAdapter.HorizontalScrollViewItem)v;
        openItemDialog(item);
        return true;
    }

    private void openItemDialog(final HorizontalScrollViewAdapter.HorizontalScrollViewItem item) {
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

    private void onEdit(HorizontalScrollViewAdapter.HorizontalScrollViewItem item) {

    }

    private void onDelete(HorizontalScrollViewAdapter.HorizontalScrollViewItem item) {

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

    /**
     * Created by Rene on 31.03.2016.
     */
    public static class GroupEditorAddActionListener implements DialogInterface.OnClickListener {

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
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    String groupName = groupNameEditText.getText().toString();
                    //retrieves the DataSource instance and inserts the new group
                    DataSource source = DataSource.getDataSourceInstance(context);
                    source.open();
                    source.insertGroup(groupName);
                    source.close();
                    //notifies the adapter to update
                    groupListViewAdapter.notifyDataSetChanged();
                    break;
            }
            dialog.dismiss();
        }



    }

    /**
     * Created by Rene on 03.04.2016.
     */
    public static class MemberAddActionListener implements View.OnClickListener {

        private GroupEditorActivity activity;

        private AlertDialog.Builder memberAddDialogView;
        private View memberAddDialog;
        private EditText firstnameEditText;
        private EditText surnameEditText;
        private ImageView previewImageView;

        public MemberAddActionListener(GroupEditorActivity activity) {
            this.activity = activity;
        }


        @Override
        public void onClick(View v) {
            Button button = (Button)v;
            Group group = (Group)button.getTag();

            CustomAlertDialog.CustomDialogOnClickListener listener = new CustomAlertDialog.CustomDialogOnClickListener(group) {
                @Override
                public void onClick(DialogInterface dialog, int which, Object object) {
                    Group group = (Group)object;
                    switch(which) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                        case DialogInterface.BUTTON_POSITIVE:
                            if(activity.getIntent().getParcelableExtra("member") != null) {
                                String firstname = firstnameEditText.getText().toString();
                                String surname = surnameEditText.getText().toString();
                                Member member = activity.getIntent().getParcelableExtra("member");
                                member.setFirstname(firstname);
                                member.setSurname(surname);
                                member.setGroupID(group.getId());

                                DataSource source = DataSource.getDataSourceInstance(activity);
                                source.open();
                                source.insertMember(member);
                                source.close();
                                activity.getHorizontalScrollViewAdapter().update(group.getId());
                                dialog.dismiss();
                            }else{
                                Toast.makeText(activity,"Bitte zuerst Bild auswählen",Toast.LENGTH_LONG).show();

                            }
                            break;
                    }

                }
            };

            LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            memberAddDialog = layoutInflater.inflate(R.layout.activity_groupeditor_portraititem_dialog, null);
            memberAddDialogView = new AlertDialog.Builder(activity)
                    .setCancelable(true)
                    .setPositiveButton(activity.getResources().getString(R.string.button_title_add), listener)
                    .setNegativeButton(activity.getResources().getString(R.string.button_title_cancel), listener)
                    .setView(memberAddDialog);

            memberAddDialogView.show();

            firstnameEditText = (EditText) memberAddDialog.findViewById(R.id.dialog_firstname);
            firstnameEditText.setHint(activity.getResources().getString(R.string.hint_firstname));
            surnameEditText = (EditText) memberAddDialog.findViewById(R.id.dialog_surname);
            surnameEditText.setHint(activity.getResources().getString(R.string.hint_surname));
            previewImageView = (ImageView) memberAddDialog.findViewById(R.id.dialog_preview_image);
            previewImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView previewImageView = (ImageView)v;
                    Member member = (Member)previewImageView.getTag();
                    Intent galleryChooserIntent = new Intent();
                    activity.getIntent().putExtra("member", member);
                    galleryChooserIntent.setType("image/*");
                    galleryChooserIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    activity.startActivityForResult(Intent.createChooser(galleryChooserIntent,
                            activity.getResources().getString(R.string.groupeditor_gallerychooser_title)),
                            SELECT_PICTURE_ADD);

                }
            });
            previewImageView.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));


        }

        public void onImageSelected(Uri selectedImageUri) {
            previewImageView.setImageURI(selectedImageUri);

        }
    }
}