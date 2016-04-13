package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.util.CustomAlertDialog;

/**
 * Created by Rene on 31.03.2016.
 */
public class HorizontalScrollViewAdapter implements View.OnLongClickListener {

    //option identifier for editing a member
    private static final int DIALOG_OPTION_EDIT = 0;
    //option identifier for deleting a member
    private static final int DIALOG_OPTION_DELETE = 1;

    private GroupEditorActivity activity;
    private Group group;
    private DataSource source;

    private ArrayList<Member> members;
    private ArrayList<HorizontalScrollViewItem> items;

    private CustomHorizontalScrollView scrollView;

    private CustomAlertDialog memberEditorDialog;

    public HorizontalScrollViewAdapter(GroupEditorActivity activity, CustomHorizontalScrollView scrollView, Group group) {
        this.activity = activity;
        this.group = group;
        this.scrollView = scrollView;

        source = DataSource.getDataSourceInstance(activity);
        source.open();
        members = source.getMembers(group.getId());
        source.close();

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
        itemOptionsDialog.setOptionSelectionListener(new CustomAlertDialog.OnOptionSelectionListener() {
            @Override
            public void onPositiveSelection(CustomAlertDialog.Interface i) {

            }

            @Override
            public void onNegativeSelection(CustomAlertDialog.Interface i) {

            }

            @Override
            public void onNeutralSelection(CustomAlertDialog.Interface i) {

            }

            @Override
            public void onDefaultSelection(CustomAlertDialog.Interface i) {
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

            @Override
            public void onViewOnClick(CustomAlertDialog.Interface i) {

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
        memberEditorDialog.setAdapter(this);
        memberEditorDialog.addView(R.id.dialog_firstname);
        memberEditorDialog.addView(R.id.dialog_surname);
        memberEditorDialog.addViewIncludingOnClick(R.id.dialog_preview_image);
        memberEditorDialog.getView(EditText.class, R.id.dialog_firstname).setText(member.getFirstname());
        memberEditorDialog.getView(EditText.class, R.id.dialog_surname).setText(member.getSurname());
        memberEditorDialog.getView(ImageView.class, R.id.dialog_preview_image)
                .setImageURI(Uri.parse(member.getImagePath()));

        memberEditorDialog.setOptionSelectionListener(new CustomAlertDialog.OnOptionSelectionListener() {
            @Override
            public void onPositiveSelection(CustomAlertDialog.Interface i) {
                Member member = (Member)i.getValue();
                String firstname = i.getView(EditText.class, R.id.dialog_firstname).getText().toString();
                String surname = i.getView(EditText.class, R.id.dialog_surname).getText().toString();
                member.setFirstname(firstname);
                member.setSurname(surname);
                DataSource source = i.getDataSource();
                source.open();
                source.updateMember(member);
                source.close();
                ((HorizontalScrollViewAdapter)(i.getAdapter())).update(group.getId());
                i.close();
            }

            @Override
            public void onNegativeSelection(CustomAlertDialog.Interface i) {
                i.close();
            }

            @Override
            public void onNeutralSelection(CustomAlertDialog.Interface i) {

            }

            @Override
            public void onDefaultSelection(CustomAlertDialog.Interface i) {

            }

            @Override
            public void onViewOnClick(CustomAlertDialog.Interface i) {
                Member member = (Member)i.getValue();
                Intent galleryChooserIntent = new Intent();
                activity.getIntent().putExtra("member", member);
                galleryChooserIntent.setType("image/*");
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                    galleryChooserIntent.setAction(Intent.ACTION_GET_CONTENT);
                else
                    galleryChooserIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);

                activity.startActivityForResult(Intent.createChooser(galleryChooserIntent,
                        activity.getResources().getString(R.string.groupeditor_gallerychooser_title)),
                        GroupEditorActivity.SELECT_PICTURE_EDIT);
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



    /**
     * Created by Rene on 24.03.2016.
     */
    public static class HorizontalScrollViewItem extends LinearLayout {

        //displays an image of the currently selected student
        private ImageView galleryImageView;
        //displays the name of the currently selected student
        private TextView galleryNameTextView;

        //holds a member object
        private Member member;

        public static int CHOOSE_IMAGE = 1339;


        public HorizontalScrollViewItem(Context context, Member member) {
            super(context);

            this.member = member;

            //inflates the layout for the GroupListViewItem class
            inflate(context, R.layout.activity_groupeditor_portraititem, this);

            //the ImageView that shows an image of the currently selected student
            galleryImageView = (ImageView)findViewById(R.id.gallery_imageview);
            //the TextView that shows the name of the currently selected student
            galleryNameTextView = (TextView)findViewById(R.id.gallery_name_textview);
            Uri uri = Uri.parse(member.getImagePath());
            galleryImageView.setImageURI(uri);

         //TODO uri überprüfen : falls uri nicht auf Bild verweist defaultimage setzen
              //  galleryImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

            galleryNameTextView.setText(member.getFullName());

        }

        public Member getMember() {
            return member;
        }

        public Integer getPosition() {
            int[] location = new int[2];
            galleryImageView.getLocationOnScreen(location);
            return location[0];
        }

        public void setHighlightOn() {
            galleryImageView.setScaleX(1.25f);
            galleryImageView.setScaleY(1.25f);
        }
        public void setHighlightOff() {
            galleryImageView.setScaleX(1);
            galleryImageView.setScaleY(1);
        }

    }

}