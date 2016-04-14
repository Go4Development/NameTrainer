package go4.szut.de.nametrainer.groupeditor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
public class GroupEditorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SELECT_PICTURE_EDIT = 1337;
    public static final int SELECT_PICTURE_ADD = 1338;

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


        //sets the list containing the GroupListViewItems
        //TODO Auslagern in Adapter
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
        addGroupDialog.setAdapter(groupListViewAdapter);
        addGroupDialog.setDialogView(R.layout.activity_groupeditor_edittext);
        addGroupDialog.addView(R.id.group_add_edittext);
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
                GroupListViewAdapter adapter = (GroupListViewAdapter)i.getAdapter();
                adapter.notifyDataSetChanged();
                i.close();
            }

            @Override
            public void onNegative(CustomAlertDialog.Interface i) {
                i.close();
            }

        });
        addGroupDialog.show();
    }

    public HorizontalScrollViewAdapter getHorizontalScrollViewAdapter() {
        return horizontalScrollViewAdapter;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        Group group = (Group)button.getTag();

        memberAddDialog = new CustomAlertDialog(this);
        memberAddDialog.setAdapter(horizontalScrollViewAdapter);
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
                if(getIntent().getParcelableExtra("member") != null) {
                    Group group = (Group) i.getValue();
                    String firstname = i.getView(EditText.class, R.id.dialog_firstname).getText().toString();
                    String surname = i.getView(EditText.class, R.id.dialog_surname).getText().toString();
                    Member member = getIntent().getParcelableExtra("member");
                    member.setFirstname(firstname);
                    member.setSurname(surname);
                    member.setGroupID(group.getId());
                    getIntent().removeExtra("member");
                    DataSource source = i.getDataSource();
                    source.open();
                    source.insertMember(member);
                    source.close();
                    ((HorizontalScrollViewAdapter)(i.getAdapter())).update(group.getId());
                    i.close();
                } else {
                    Toast.makeText(GroupEditorActivity.this,"Bitte zuerst Bild auswählen",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNegative(CustomAlertDialog.Interface i) {
                i.close();
            }

            @Override
            public void onClick(CustomAlertDialog.Interface i) {
                Intent galleryChooserIntent = new Intent();
                galleryChooserIntent.setType("image/*");
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                    galleryChooserIntent.setAction(Intent.ACTION_GET_CONTENT);
                else
                    galleryChooserIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(galleryChooserIntent,
                        getResources().getString(R.string.groupeditor_gallerychooser_title)),
                        GroupEditorActivity.SELECT_PICTURE_ADD);
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

}