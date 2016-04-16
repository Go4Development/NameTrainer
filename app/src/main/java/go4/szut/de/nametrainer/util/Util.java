package go4.szut.de.nametrainer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import go4.szut.de.nametrainer.R;
import go4.szut.de.nametrainer.database.DataSource;
import go4.szut.de.nametrainer.database.Member;
import go4.szut.de.nametrainer.groupeditor.GroupEditorActivity;


/**
 * Created by Rene on 24.03.2016.
 */
public class Util {

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static class D {

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
         * Wrapper method for Toast.
         * @param context - the context of the activity
         * @param object - the object to toast a message from
         */
        public static void t(Context context, Object object) {
            Toast.makeText(context, "Object Value : " + object, Toast.LENGTH_LONG).show();
        }

        /**
         * Wrapper method for Toast.
         * @param context - the context of the activity
         * @param message - the message to toast
         */
        public static void t(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        /**
         * Wrapper method for Toast including test for null.
         * @param context - the context of the activity
         * @param object - the object to toast a message from
         */
        public static void tn(Context context, Object object) {
            Toast.makeText(context, "Is Object null? : " + (object == null), Toast.LENGTH_LONG).show();
        }

    }

    public static class Res {

        /**
         * Wrapper method for getting string resources.
         * @param activity - the activity to get the resources
         * @param resId - the resource id
         * @return the resource string that belongs to the passed resId
         */
        public static String str(AppCompatActivity activity, int resId) {
            return activity.getResources().getString(resId);
        }

        /**
         * Wrapper method for getting string array resources
         * @param activity - the activity to get the resources
         * @param resId - the resource id
         * @return the resource string array that belongs to the passed resId
         */
        public static String[] strA(AppCompatActivity activity, int resId) {
            return activity.getResources().getStringArray(resId);
        }

        /**
         * Wrapper method for getting formatted string.
         * @param activity - the activity to get resources
         * @param formatStringId - the resource id for format string
         * @param params - the params to insert in format string
         * @return
         */
        public static String strF(AppCompatActivity activity, int formatStringId, Object... params) {
            return String.format(activity.getResources().getString(formatStringId), params);
        }

    }

    public static class Uri {

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

    }

    public static class Input {

        public static final int NAME_TOO_SHORT = 0;
        public static final int NAME_OK = 1;
        public static final int NAME_TOO_LONG = 2;

        public static final int[] ERROR_IDS = {
                R.string.error_input_too_short,
                R.string.error_input_okay,
                R.string.error_input_too_long
        };

        public static void setTextInputFilter(View view){
            EditText editText = (EditText) view;
            InputFilter filter = new InputFilter() {
                public CharSequence filter(CharSequence source, int start, int end,
                                           Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        if (!Character.isLetterOrDigit(source.charAt(i))) {
                            return "";
                        }
                    }
                    return null;
                }
            };
            editText.setFilters(new InputFilter[]{filter});
        }

        public static int limit(EditText editText, int min, int max) {
            String value = editText.getText().toString();
            if(value.length() >= min && value.length() <= max) {
                return NAME_OK;
            } else if(value.length() < min) {
                return NAME_TOO_SHORT;
            } else {
                return NAME_TOO_LONG;
            }
        }
    }

    public static class DB {

        /**
         * Returns true if the database has groups stored.
         * @return true if there are groups stored in the database, otherwise false.
         */
        public static boolean databaseHasStoredGroups(Context context) {
            DataSource source = DataSource.getDataSourceInstance(context);
            source.open();
            int groupCount = source.getAllGroups().size();
            source.close();
            return groupCount != 0;
        }

    }

    public static class Run {

        public static void delay(Runnable runnable, long delay) {
            new Handler().postDelayed(runnable, delay);
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
