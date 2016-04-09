package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;

/**
 * Created by Rene on 31.03.2016.
 */
public class HorizontalScrollViewAdapter {

    private GroupEditorActivity activity;
    private Group group;
    private DataSource source;

    private ArrayList<Member> members;
    private ArrayList<HorizontalScrollViewItem> items;

    private CustomHorizontalScrollView scrollView;
    private HorizontalScrollViewItemListener listener;

    public HorizontalScrollViewAdapter(GroupEditorActivity activity, CustomHorizontalScrollView scrollView, Group group) {
        this.activity = activity;
        this.group = group;
        this.scrollView = scrollView;

        listener = new HorizontalScrollViewItemListener(activity, this);


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
        members = source.getMembers(id);
        source.close();
        items = createHorizontalScrollViewItems();
        scrollView.update();
    }

    public ArrayList<HorizontalScrollViewItem> createHorizontalScrollViewItems() {
        ArrayList<HorizontalScrollViewItem> items = new ArrayList<HorizontalScrollViewItem>();
        for(Member member : members) {
            HorizontalScrollViewItem item = new HorizontalScrollViewItem(activity, member);
            item.setOnLongClickListener(listener);
            items.add(item);
        }
        return items;
    }

    public HorizontalScrollViewItemListener getListener() {
        return listener;
    }


    /**
     * Created by Rene on 31.03.2016.
     */
    public static class HorizontalScrollViewItemListener implements View.OnLongClickListener, View.OnClickListener {

        //option identifier for editing a member
        private static final int DIALOG_OPTION_EDIT = 0;
        //option identifier for deleting a member
        private static final int DIALOG_OPTION_DELETE = 1;

        private GroupEditorActivity activity;
        private ArrayAdapter<String> horizontalScrollViewItemDialogAdapter;

        private HorizontalScrollViewAdapter adapter;
        private DataSource source;

        private AlertDialog.Builder memberEditorDialog;
        private View memberEditorDialogView;
        private EditText firstnameEditText;
        private EditText surnameEditText;
        private ImageView previewImageView;

        public HorizontalScrollViewItemListener(GroupEditorActivity activity, HorizontalScrollViewAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;

            horizontalScrollViewItemDialogAdapter = new ArrayAdapter<String>(activity,
                    android.R.layout.select_dialog_item,
                    activity.getResources().getStringArray(R.array.groupeditor_item_dialog_options));

            source = DataSource.getDataSourceInstance(activity);
        }

        @Override
        public boolean onLongClick(View v) {
            HorizontalScrollViewItem item = (HorizontalScrollViewItem)v;
            final Member member = item.getMember();

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(member.getFullName());
            builder.setCancelable(true);
            builder.setAdapter(horizontalScrollViewItemDialogAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DIALOG_OPTION_EDIT:
                            onEdit(member);
                            break;
                        case DIALOG_OPTION_DELETE:
                            onDelete(member);
                            break;
                    }
                }
            });
            builder.show();
            return false;
        }

        private void onEdit(Member member) {


            CustomAlertDialog.CustomDialogOnClickListener listener = new CustomAlertDialog.CustomDialogOnClickListener(member) {
                @Override
                public void onClick(DialogInterface dialog, int which, Object object) {
                    Member member = (Member)object;
                    switch(which) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                        case DialogInterface.BUTTON_POSITIVE:
                            String firstname = firstnameEditText.getText().toString();
                            String surname = surnameEditText.getText().toString();
                            member.setFirstname(firstname);
                            member.setSurname(surname);
                            source.open();
                            source.updateMember(member);
                            source.close();
                            break;
                    }
                    dialog.dismiss();
                }
            };

            LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            memberEditorDialogView = layoutInflater.inflate(R.layout.activity_groupeditor_portraititem_dialog, null);
            memberEditorDialog = new AlertDialog.Builder(activity)
                    .setCancelable(true)
                    .setPositiveButton(activity.getResources().getString(R.string.groupeditor_edit_action_posbutton), listener)
                    .setNegativeButton(activity.getResources().getString(R.string.groupeditor_edit_action_negbutton), listener)
                    .setView(memberEditorDialogView);

            memberEditorDialog.show();

            firstnameEditText = (EditText)memberEditorDialogView.findViewById(R.id.dialog_firstname);
            firstnameEditText.setText(member.getFirstname());
            surnameEditText = (EditText)memberEditorDialogView.findViewById(R.id.dialog_surname);
            surnameEditText.setText(member.getSurname());
            previewImageView = (ImageView)memberEditorDialogView.findViewById(R.id.dialog_preview_image);
            previewImageView.setOnClickListener(this);
            previewImageView.setTag(member);
            previewImageView.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));

        }

        private void onDelete(Member member) {
            source.open();
            source.deleteMember(member);
            source.close();
            adapter.update(member.getGroupID());
        }

        @Override
        public void onClick(View v) {
            ImageView previewImageView = (ImageView)v;
            Member member = (Member)previewImageView.getTag();
            Intent galleryChooserIntent = new Intent();
            activity.getIntent().putExtra("member", member);
            galleryChooserIntent.setType("image/*");
            galleryChooserIntent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(galleryChooserIntent,
                    activity.getResources().getString(R.string.groupeditor_gallerychooser_title)), GroupEditorActivity.SELECT_PICTURE_EDIT);

        }

        public void onImageSelected(String selectedImageUri, Member member) {
            member.setImagePath(selectedImageUri);

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
            if(uri != null){
                galleryImageView.setImageURI(uri);
            }
            else{
                galleryImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
            }
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
