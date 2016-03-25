package go4.szut.de.nametrainer.groupeditor;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import go4.szut.de.nametrainer.R;

/**
 * Created by Rene on 24.03.2016.
 */
public class GroupListViewItem extends LinearLayout {

    //displays an image of the currently selected student
    private ImageView galleryImageView;
    //displays the name of the currently selected student
    private TextView galleryNameTextView;

    private String galleryPath;
    private String galleryName;


    public GroupListViewItem(Context context, String galleryPath, String galleryName) {
        super(context);

        this.galleryPath = galleryPath;
        this.galleryName = galleryName;

        //inflates the layout for the GroupListViewItem class
        inflate(context, R.layout.activity_groupeditor_listviewitem, this);

        //the ImageView that shows an image of the currently selected student
        galleryImageView = (ImageView)findViewById(R.id.gallery_imageview);
        //the TextView that shows the name of the currently selected student
        galleryNameTextView = (TextView)findViewById(R.id.gallery_name_textview);

        galleryImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        galleryNameTextView.setText(galleryName);

    }

    public String getGalleryName() {
        return galleryName;
    }



}
