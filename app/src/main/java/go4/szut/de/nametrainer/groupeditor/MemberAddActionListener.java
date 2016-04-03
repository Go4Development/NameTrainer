package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Group;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 03.04.2016.
 */
public class MemberAddActionListener implements View.OnClickListener {

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

        CustomDialogOnClickListener listener = new CustomDialogOnClickListener(group) {
            @Override
            public void onClick(DialogInterface dialog, int which, Object object) {
                Group group = (Group)object;
                switch(which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
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
                        activity.getHorizontalScrollViewAdapter().update();

                        break;
                }
                dialog.dismiss();
            }
        };

        LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                galleryChooserIntent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(Intent.createChooser(galleryChooserIntent,
                        activity.getResources().getString(R.string.groupeditor_gallerychooser_title)),
                        GroupEditorActivity.SELECT_PICTURE_ADD);

            }
        });
        previewImageView.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));


    }

    public void onImageSelected(Uri selectedImageUri) {
        previewImageView.setImageURI(selectedImageUri);
        Util.l(this, "pURI" + selectedImageUri.getPath());
    }
}
