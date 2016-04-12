package go4.szut.de.nametrainer.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Rene on 24.03.2016.
 */
public class Util {

    /**
     * Hides the ActionBar of the passed Activity, if one is present.
     * @param activity - the activity with the corresponding ActionBar that needs to be hidden
     * @return true if the ActionBar was hidden, or false if no ActionBar is present
     * which can be hidden
     */
    public static boolean hideActionBar(Activity activity) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
            return true;
        }
        return false;
    }

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



}
