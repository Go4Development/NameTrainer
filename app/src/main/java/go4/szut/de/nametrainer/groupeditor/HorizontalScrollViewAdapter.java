package go4.szut.de.nametrainer.groupeditor;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.util.CustomAlertDialog;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 31.03.2016.
 */
public class HorizontalScrollViewAdapter implements View.OnLongClickListener, CustomAlertDialog.OnUpdateListener {

    //option identifier for editing a member
    private static final int DIALOG_OPTION_EDIT = 0;
    //option identifier for deleting a member
    private static final int DIALOG_OPTION_DELETE = 1;

    private static final int IDENTIFIER_HORIZONTALSCROLLVIEW_ADAPTER = 0x4;

    private GroupEditorActivity activity;
    private DataSource source;

    private ArrayList<Member> members;
    private ArrayList<HorizontalScrollViewItem> items;

    private CustomHorizontalScrollView scrollView;

    private CustomAlertDialog memberEditorDialog;

    public HorizontalScrollViewAdapter(GroupEditorActivity activity, CustomHorizontalScrollView scrollView) {
        this.activity = activity;
        this.scrollView = scrollView;

        Group group;
        GroupListViewAdapter groupListViewAdapter = activity.getGroupListViewAdapter();

        if (groupListViewAdapter.getCount() > 0) {
            group = groupListViewAdapter.getItem(0);
            source = DataSource.getDataSourceInstance(activity);
            source.open();
            members = source.getMembers(group.getId());
            source.close();
        }else {
            members = new ArrayList<>();

        }
        items = createHorizontalScrollViewItems();

    }

    public HorizontalScrollViewItem getHorizontalScrollViewItemAt(int index) {
        return items.get(index);
    }

    public int getSize() {
        return items.size();
    }

    public void update(int id) {

        source = DataSource.getDataSourceInstance(activity);
        source.open();
        ArrayList<Member> t_members = source.getMembers(id);
        source.close();
            members = t_members;
            items = createHorizontalScrollViewItems();
            scrollView.update();

    }

    public ArrayList<HorizontalScrollViewItem> createHorizontalScrollViewItems() {
        ArrayList<HorizontalScrollViewItem> items = new ArrayList<>();
        for(Member member : members) {
            HorizontalScrollViewItem item = new HorizontalScrollViewItem(activity, member);
            item.setOnLongClickListener(this);
            items.add(item);
        }
        return items;
    }


    @Override
    public boolean onLongClick(View v) {
        HorizontalScrollViewItem item = (HorizontalScrollViewItem)v;
        Member member = (Member)item.getMember();

        CustomAlertDialog itemOptionsDialog = new CustomAlertDialog(activity);
        itemOptionsDialog.setArrayAdapter(android.R.layout.select_dialog_item, R.array.groupeditor_item_dialog_options);
        itemOptionsDialog.setTitle(member.getFullName());
        itemOptionsDialog.setValue(member);
        itemOptionsDialog.setOptionSelectionListener(new CustomAlertDialog.DefaultSelectionListener() {

            @Override
            public void onDefault(CustomAlertDialog.Interface i) {
                Member member = (Member)i.getValue();
                switch(i.getSelection()) {
                    case DIALOG_OPTION_EDIT:
                        onEdit(member);
                        break;
                    case DIALOG_OPTION_DELETE:
                        onDelete(member);
                        break;
                }
            }

        });

        itemOptionsDialog.show();

        return true;
    }

    private void onEdit(Member member) {

        memberEditorDialog = new CustomAlertDialog(activity);
        memberEditorDialog.setDialogView(R.layout.activity_groupeditor_portraititem_dialog);
        memberEditorDialog.setPositiveButtonTitle(R.string.groupeditor_edit_action_posbutton);
        memberEditorDialog.setNegativeButtonTitle(R.string.groupeditor_edit_action_negbutton);
        memberEditorDialog.setValue(member);
        memberEditorDialog.setUpdateListener(IDENTIFIER_HORIZONTALSCROLLVIEW_ADAPTER, this);
        memberEditorDialog.addView(R.id.dialog_firstname);
        memberEditorDialog.addView(R.id.dialog_surname);
        memberEditorDialog.addViewIncludingOnClick(R.id.dialog_preview_image);
        memberEditorDialog.getView(EditText.class, R.id.dialog_firstname).setText(member.getFirstname());
        memberEditorDialog.getView(EditText.class, R.id.dialog_surname).setText(member.getSurname());
        Util.Input.setTextInputFilter(memberEditorDialog.getView(EditText.class, R.id.dialog_firstname));
        Util.Input.setTextInputFilter(memberEditorDialog.getView(EditText.class, R.id.dialog_surname));
        memberEditorDialog.getView(ImageView.class, R.id.dialog_preview_image)
                .setImageURI(Uri.parse(member.getImagePath()));

        memberEditorDialog.setOptionSelectionListener(new CustomAlertDialog.AdvancedSimpleOnOptionSelectionListener() {
            @Override
            public void onPositive(CustomAlertDialog.Interface i) {
                Member member = (Member)i.getValue();
                String firstName = i.getView(EditText.class, R.id.dialog_firstname).getText().toString();
                String surname = i.getView(EditText.class, R.id.dialog_surname).getText().toString();
                member.setFirstname(firstName);
                member.setSurname(surname);
                DataSource source = i.getDataSource();
                source.open();
                source.updateMember(member);
                source.close();
                i.close(member);
            }

            @Override
            public void onNegative(CustomAlertDialog.Interface i) {
                i.close(null);
            }

            @Override
            public void onClick(CustomAlertDialog.Interface i) {
                Util.ImagePicker picker = new Util.ImagePicker(activity);
                picker.open(GroupEditorActivity.SELECT_PICTURE_EDIT, i.getValue());
            }
        });
        memberEditorDialog.show();


    }

    private void onDelete(Member member) {
        source.open();
        source.deleteMember(member);
        source.close();
        update(member.getGroupID());
    }

    public void onImageSelected(Uri selectedImageUri) {
        if(memberEditorDialog != null) {
            memberEditorDialog.getView(ImageView.class, R.id.dialog_preview_image)
                    .setImageURI(selectedImageUri);
        }
    }


    @Override
    public void update(int updateIdentifier, Object data) {
        switch(updateIdentifier) {
            case IDENTIFIER_HORIZONTALSCROLLVIEW_ADAPTER:
                Member member = (Member)data;
                if(member != null) {
                    update(member.getGroupID());
                }
                break;
        }
    }
}