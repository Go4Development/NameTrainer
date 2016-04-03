package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.IOException;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.util.Util;

/**
 * Created by Rene on 24.03.2016.
 */
public class HorizontalScrollViewItem extends LinearLayout {

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
        String uri = member.getImagePath();
        if(uri != null){
           Intent i = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ((GroupEditorActivity)context).startActivityForResult(i, CHOOSE_IMAGE);
        galleryImageView.setImageBitmap(BitmapFactory.decodeFile(uri));
            Util.l(this,"PATH: " + uri);
        galleryImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
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
