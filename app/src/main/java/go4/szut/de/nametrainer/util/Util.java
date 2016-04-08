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

    /**
     * Saves the settings in the SharedPreferences.
     * @param context - the context that is needed to retrieve the SharedPreferences.
     * @param preferenceName - the name of the preference
     * @param settings - the settings object containing the different setting values
     */
    public static void saveSettings(Context context, String preferenceName, Util.Settings settings) {
        SharedPreferences preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        for(int i = 0; i < settings.size(); i++) {
            Settings.Type type = settings.getType(i);
            String key = settings.getKey(i);
            Object value = settings.getValue(i);
            switch(type) {
                case BOOLEAN:
                    editor.putBoolean(key, Boolean.parseBoolean(value.toString()));
                    break;
                case FLOAT:
                    editor.putFloat(key, Float.parseFloat(value.toString()));
                    break;
                case INTEGER:
                    editor.putInt(key, Integer.parseInt(value.toString()));
                    break;
                case LONG:
                    editor.putLong(key, Long.parseLong(value.toString()));
                    break;
                case STRING:
                    editor.putString(key, value.toString());
                    break;
            }
        }
        editor.apply();
    }

    /**
     *
     * @param context
     * @param preferenceName
     * @param settings
     * @return
     */
    public static Util.Settings getSettings(Context context, String preferenceName, Util.Settings settings) {
        SharedPreferences preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        Util.Settings settingsContainingValues = new Util.Settings();
        for(int i = 0; i < settings.size(); i++) {
            Settings.Type type = settings.getType(i);
            String key = settings.getKey(i);
            switch(type) {
                case BOOLEAN:
                    settingsContainingValues.addSetting(Settings.Type.BOOLEAN, key, preferences.getBoolean(key, false));
                    break;
                case FLOAT:
                    settingsContainingValues.addSetting(Settings.Type.FLOAT, key, preferences.getFloat(key, 0));
                    break;
                case INTEGER:
                    settingsContainingValues.addSetting(Settings.Type.INTEGER, key, preferences.getInt(key, 0));
                    break;
                case LONG:
                    settingsContainingValues.addSetting(Settings.Type.LONG, key, preferences.getLong(key, 0));
                    break;
                case STRING:
                    settingsContainingValues.addSetting(Settings.Type.STRING, key, preferences.getString(key, ""));
                    break;
            }
        }
        //TODO Settings
        return settingsContainingValues;
    }

    public static class Settings {

        public static enum Type {
            BOOLEAN, FLOAT, INTEGER, LONG, STRING
        }

        //holds the types of the values
        private ArrayList<Type> types;
        //holds the keys linked to the values
        private ArrayList<String> keys;
        //holds the values
        private ArrayList<Object> values;

        public Settings() {
            types = new ArrayList<Type>();
            keys = new ArrayList<String>();
            values = new ArrayList<Object>();
        }

        /**
         * Adds a setting to this settings object.
         * @param type - the type that the setting value is an instance of
         * @param key - the key for retrieving the value
         * @param value - the setting value
         */
        public void addSetting(Type type, String key, Object value) {
            if(type != null && key != null) {
                types.add(type);
                keys.add(key);
                values.add(value);
            }
        }

        /**
         * Removes the setting by the specified key.
         * @param key - the key that identifies the setting
         */
        public void removeSetting(String key) {
            int index = keys.indexOf(key);
            if(index != -1) {
                types.remove(index);
                keys.remove(index);
                values.remove(index);
            }
        }

        /**
         * Clears all settings in this settings object.
         */
        public void clear() {
            types.clear();
            keys.clear();
            values.clear();
        }

        /**
         * Returns the size of this setting object.
         * @return the number of settings
         */
        public int size() {
            return keys.size();
        }

        /**
         * Returns the type of the setting value of the specified index.
         * @param index - the index of the setting value
         * @return type
         */
        public Type getType(int index) {
            return types.get(index);
        }

        /**
         * Returns the key of the setting value of the specified index.
         * @param index - the index of the setting value
         * @return key
         */
        public String getKey(int index) {
            return keys.get(index);
        }

        /**
         * Returns the settings value of the specified index.
         * @param index - the index of the setting value
         * @return value
         */
        public Object getValue(int index) {
            return values.get(index);
        }

        /**
         * Get a string value to the corresponding key.
         * @param key - the key
         * @return the value
         */
        public String getStringValue(String key) {
            if(isTypeOf(Type.STRING, key)) {
                return values.get(keys.indexOf(key)).toString();
            }
            return null;
        }

        /**
         * Get a float value to the corresponding key.
         * @param key - the key
         * @return the value
         */
        public float getFloatValue(String key) {
            if(isTypeOf(Type.FLOAT, key)) {
                return Float.parseFloat(values.get(keys.indexOf(key)).toString());
            }
            return -1f;
        }

        /**
         * Get a integer value to the corresponding key.
         * @param key - the key
         * @return the value
         */
        public int getIntegerValue(String key) {
            if(isTypeOf(Type.INTEGER, key)) {
                return Integer.parseInt(values.get(keys.indexOf(key)).toString());
            }
            return -1;
        }

        /**
         * Get a long value to the corresponding key.
         * @param key - the key
         * @return the value
         */
        public long getLongValue(String key) {
            if(isTypeOf(Type.LONG, key)) {
                return Long.parseLong(values.get(keys.indexOf(key)).toString());
            }
            return -1;
        }

        /**
         * Get a boolean value to the corresponding key.
         * @param key - the key
         * @return the value
         */
        public boolean getBooleanValue(String key) {
            if(isTypeOf(Type.BOOLEAN, key)) {
                return Boolean.parseBoolean(values.get(keys.indexOf(key)).toString());
            }
            return false;
        }

        /**
         * Checks if the type of the value corresponding to the key is the same as
         * the stored one.
         * @param type - the type to check
         * @param key - the key corresponding to the value
         * @return returns true if the types are the same, false if they are not
         */
        private boolean isTypeOf(Settings.Type type, String key) {
            int index = keys.indexOf(key);
            if(index != -1) {
                if(type == types.get(index)) {
                    return true;
                }
                return false;
            }
            return false;
        }

    }


}
