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
public class HorizontalScrollViewItem extends LinearLayout {

    //displays an image of the currently selected student
    private ImageView galleryImageView;
    //displays the name of the currently selected student
    private TextView galleryNameTextView;

    //holds the path to the corresponding image of the student
    private String galleryPath;

    //holds the name of the student
    private String firstname;
    private String surname;


    public HorizontalScrollViewItem(Context context, String galleryPath, String firstname, String surname) {
        super(context);

        this.galleryPath = galleryPath;
        this.firstname = firstname;
        this.surname = surname;

        //inflates the layout for the GroupListViewItem class
        inflate(context, R.layout.activity_groupeditor_listviewitem, this);

        //the ImageView that shows an image of the currently selected student
        galleryImageView = (ImageView)findViewById(R.id.gallery_imageview);
        //the TextView that shows the name of the currently selected student
        galleryNameTextView = (TextView)findViewById(R.id.gallery_name_textview);

        galleryImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        galleryNameTextView.setText(getName());

    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return firstname + " " + surname;
    }

    public Integer getPosition() {
        int[] location = new int[2];
        galleryImageView.getLocationOnScreen(location);
        return location[0];
    }

    public void setHighlightOn() {
        //TODO Definition for color in XML
        setBackgroundColor(Color.parseColor("#ffff00"));
    }
    public void setHighlightOff() {
        //TODO Definition for color in XML
        setBackgroundColor(Color.parseColor("#ffffff"));
    }

}
