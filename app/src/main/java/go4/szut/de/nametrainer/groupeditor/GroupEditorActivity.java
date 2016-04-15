package go4.szut.de.nametrainer.groupeditor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.options.OptionsActivity;
import go4.szut.de.nametrainer.util.CustomAlertDialog;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupEditorActivity extends AppCompatActivity implements View.OnClickListener,
    CustomAlertDialog.OnUpdateListener {

    public static final int SELECT_PICTURE_EDIT = 1337;
    public static final int SELECT_PICTURE_ADD = 1338;

    private static final int IDENTIFIER_HORIZONTALSCROLLVIEW_ADAPTER = 0x4;
    private static final int IDENTIFIER_GROUPLISTVIEW_ADAPTER = 0x8;

    //holds a bunch of horizontal positioned images of students of the current selected group
    private CustomHorizontalScrollView portraitScrollView;
    private HorizontalScrollViewAdapter horizontalScrollViewAdapter;

    //holds a list of groups containing multiple students
    private ListView groupListView;
    //holds the data for the groupListView
    private GroupListViewAdapter groupListViewAdapter;

    private CustomAlertDialog memberAddDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the layout to the specified "groupeditor layout"
        setContentView(R.layout.activity_groupeditor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupListViewAdapter = new GroupListViewAdapter(this);

        //portrait stuff
        portraitScrollView = (CustomHorizontalScrollView)findViewById(R.id.portrait_scrollview);

        //grouplist stuff
        groupListView = (ListView)findViewById(R.id.group_listview);
        groupListView.setAdapter(groupListViewAdapter);


        horizontalScrollViewAdapter = new HorizontalScrollViewAdapter(this, portraitScrollView);
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
                    Util.makeUriPersistent(data,this);

                    member.setImagePath(selectedImageUri.toString());
                    horizontalScrollViewAdapter.onImageSelected(selectedImageUri);
                }
            } else if(requestCode == SELECT_PICTURE_ADD) {
                if(data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Member member = new Member();
                    Util.makeUriPersistent(data,this);
                    member.setImagePath(selectedImageUri.toString());
                    getIntent().putExtra("member", member);
                    onImageSelected(selectedImageUri);

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

    private void onAddGroup() {
        CustomAlertDialog addGroupDialog = new CustomAlertDialog(this);
        addGroupDialog.setDialogView(R.layout.activity_groupeditor_edittext);
        addGroupDialog.addView(R.id.group_add_edittext);
        Util.setTextInputFilter(addGroupDialog.getView(EditText.class,R.id.group_add_edittext));

        addGroupDialog.setUpdateListener(IDENTIFIER_GROUPLISTVIEW_ADAPTER, this);
        addGroupDialog.setPositiveButtonTitle(R.string.groupeditor_add_action_posbutton);
        addGroupDialog.setNegativeButtonTitle(R.string.groupeditor_add_action_negbutton);
        addGroupDialog.setOptionSelectionListener(new CustomAlertDialog.SimpleOnOptionSelectionListener() {
            @Override
            public void onPositive(CustomAlertDialog.Interface i) {
                EditText groupNameEditText = i.getView(EditText.class, R.id.group_add_edittext);
                String groupName = groupNameEditText.getText().toString();
                DataSource source = i.getDataSource();
                source.open();
                source.insertGroup(groupName);
                source.close();
                i.close(null);
            }

            @Override
            public void onNegative(CustomAlertDialog.Interface i) {
                i.close(null);
            }

        });
        addGroupDialog.show();
    }

    public HorizontalScrollViewAdapter getHorizontalScrollViewAdapter() {
        return horizontalScrollViewAdapter;
    }

    public GroupListViewAdapter getGroupListViewAdapter() {
        return groupListViewAdapter;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        Group group = (Group)button.getTag();

        memberAddDialog = new CustomAlertDialog(this);
        memberAddDialog.setUpdateListener(IDENTIFIER_HORIZONTALSCROLLVIEW_ADAPTER, this);
        memberAddDialog.setDialogView(R.layout.activity_groupeditor_portraititem_dialog);
        memberAddDialog.setPositiveButtonTitle(R.string.button_title_add);
        memberAddDialog.setNegativeButtonTitle(R.string.button_title_cancel);
        memberAddDialog.setValue(group);
        memberAddDialog.addView(R.id.dialog_firstname);
        memberAddDialog.addView(R.id.dialog_surname);
        memberAddDialog.addViewIncludingOnClick(R.id.dialog_preview_image);
        memberAddDialog.getView(ImageView.class, R.id.dialog_preview_image)
                .setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        memberAddDialog.setOptionSelectionListener(new CustomAlertDialog.AdvancedSimpleOnOptionSelectionListener() {
            @Override
            public void onPositive(CustomAlertDialog.Interface i) {
                Member member = getIntent().getParcelableExtra("member");
                if(member != null) {
                    Group group = (Group) i.getValue();
                    String firstName = i.getView(EditText.class, R.id.dialog_firstname).getText().toString();
                    String surname = i.getView(EditText.class, R.id.dialog_surname).getText().toString();

                    Util.setTextInputFilter(i.getView(EditText.class, R.id.dialog_firstname));
                    Util.setTextInputFilter(i.getView(EditText.class, R.id.dialog_surname));

                    member.setFirstname(firstName);
                    member.setSurname(surname);
                    member.setGroupID(group.getId());
                    getIntent().removeExtra("member");
                    DataSource source = i.getDataSource();
                    source.open();
                    source.insertMember(member);
                    source.close();
                    i.close(group);
                } else {
                    //TODO reopen incomplete memberADDialog with previous data
                    //TODO dont allow saving a member/group without a name
                    Toast.makeText(GroupEditorActivity.this,"Bitte zuerst Bild auswählen",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNegative(CustomAlertDialog.Interface i) {
                i.close(null);
            }

            @Override
            public void onClick(CustomAlertDialog.Interface i) {
                Util.ImagePicker picker = new Util.ImagePicker(GroupEditorActivity.this);
                picker.open(SELECT_PICTURE_ADD, null);
            }
        });
        memberAddDialog.show();


    }

    public void onImageSelected(Uri selectedImageUri) {
        if(memberAddDialog != null) {
            memberAddDialog.getView(ImageView.class, R.id.dialog_preview_image)
                    .setImageURI(selectedImageUri);
        }
    }

    @Override
    public void update(int updateIdentifier, Object data) {
        switch(updateIdentifier) {
            case IDENTIFIER_GROUPLISTVIEW_ADAPTER:
                groupListViewAdapter.notifyDataSetChanged();
                break;
            case IDENTIFIER_HORIZONTALSCROLLVIEW_ADAPTER:
                Group group = (Group)data;
                if(group != null) {
                    horizontalScrollViewAdapter.update(group.getId());
                }
                break;
        }
    }
}