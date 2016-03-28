package go4.szut.de.nametrainer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ramazan on 26.03.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //name of the table member
    public static final String TABLE_MEMBER = Member.class.getSimpleName();
    //name of table group
    public static final String TABLE_GROUP = Group.class.getSimpleName();

    //column identifier id
    public static final String COLUMN_ID = "_id";

    //database attributes
    private static final String DATABASE_NAME = "groups.db";
    private static final int DATABASE_VERSION = 1;

    //database creation statement of table member
    private static final String DATABASE_CREATE_MEMBER =
            " create table " + TABLE_MEMBER
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + Member.COLUMN_GROUP_ID + " integer, "
            + Member.COLUMN_SURNAME + " text, "
            + Member.COLUMN_FIRSTNAME + " text not null, "
            + Member.COLUMN_IMAGEPATH + " text not null" + ");";

    //database creation statement of table group
    private static final String DATABASE_CREATE_GROUP =
            " create table " + TABLE_GROUP
                    + " (" + COLUMN_ID + " integer primary key autoincrement, "
                    + Group.COLUMN_GROUP_NAME + " text not null );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_GROUP);
        sqLiteDatabase.execSQL(DATABASE_CREATE_MEMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
