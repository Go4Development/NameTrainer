package go4.szut.de.nametrainer.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.groupeditor.GroupEditorActivity;


/**
 * Created by Rene on 24.03.2016.
 */
public class Util {

    /**
     * Creates an ArrayList with numberOfNonSenseItems strings.
     * @param numberOfNonSenseItems - the number of non sense items
     */
    public static ArrayList<String> createArrayListWithNonSense(int numberOfNonSenseItems) {
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < numberOfNonSenseItems; i++) {
            list.add("NonSense #" + i);
        }
        return list;
    }

    /**
     * Wrapper method for Log.d
     * @param obj - the obj for TAG
     * @param message - the message that should be displayed
     */
    public static void l(Object obj, String message) {
        Log.d(obj.getClass().getSimpleName(), message);
    }

    /**
     * Wrapper method for Log.d
     * @param obj - the obj for testing
     * @param tag - the tag for Log.d
     */
    public static void n(Object obj, String tag) {
        Log.d(tag, "Is Object null? : " + (obj == null));
    }

    /**
     * Makes an uri persistent and life easier.
     * @param data - the intent from onActivityResult
     * @param activity - the activity
     */
    public static void makeUriPersistent(Intent data, Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int takeFlags = data.getFlags();
            takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
        }
    }

    /**
     * ImagePicker class
     */
    public static class ImagePicker {

        private AppCompatActivity activity;
        private Intent galleryChooserIntent;

        public ImagePicker(AppCompatActivity activity) {
            this.activity = activity;
            galleryChooserIntent = new Intent();
            galleryChooserIntent.setType("image/*");
            setIntentAction();
        }

        public void open(int requestCode, Object data) {
            if(requestCode == GroupEditorActivity.SELECT_PICTURE_EDIT)
                activity.getIntent().putExtra("member", (Member)data);
            activity.startActivityForResult(Intent.createChooser(galleryChooserIntent,
                    activity.getResources().getString(R.string.groupeditor_gallerychooser_title)),
                    requestCode);
        }

        private void setIntentAction() {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                galleryChooserIntent.setAction(Intent.ACTION_GET_CONTENT);
            else
                galleryChooserIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }

    }

}
