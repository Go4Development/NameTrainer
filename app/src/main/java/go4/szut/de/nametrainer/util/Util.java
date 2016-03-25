package go4.szut.de.nametrainer.util;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


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
     * Wrapper method for Log.d
     * @param activity - the activity for TAG
     * @param message - the message that should be displayed
     */
    public static void l(Activity activity, String message) {
        Log.d(activity.getClass().getSimpleName(), message);
    }


}
