package go4.szut.de.nametrainer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ramazan on 26.03.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_MEMBER = "member";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_IMAGEPATH = "imagepath";

    public static final String TABLE_GROUP = "group";
    public static final String COLUMN_GROUP_NAME = "group_name";

    private static final String DATABASE_NAME = "groups.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_MEMBER =
            "create table " + TABLE_MEMBER
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GROUP_ID + " integer , FOREIGN KEY(" + COLUMN_GROUP_ID + ") REFERENCES group(id), "
            + COLUMN_SURNAME + " text "
            + COLUMN_FIRSTNAME + " text not null "
            +COLUMN_IMAGEPATH + "text not null" + ");";

    private static final String DATABASE_CREATE_GROUP =
            "create table " + TABLE_GROUP
                    + "(" + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_GROUP_NAME + " text not null );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_GROUP + DATABASE_CREATE_MEMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
