package go4.szut.de.nametrainer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ramazan on 26.03.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //name of the table member
    public static final String TABLE_MEMBER = "[" + Member.class.getSimpleName() + "]";
    //name of table group
    public static final String TABLE_GROUP = "[" + Group.class.getSimpleName() + "]";

    //column identifier id
    public static final String COLUMN_ID = "id";

    //database attributes
    private static final String DATABASE_NAME = "nt_data.db";
    private static final int DATABASE_VERSION = 1;

    //database creation statement of table member
    private static final String DATABASE_CREATE_MEMBER =
            " CREATE TABLE " + TABLE_MEMBER
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Member.COLUMN_GROUP_ID + " INTEGER, "
            + Member.COLUMN_SURNAME + " TEXT, "
            + Member.COLUMN_FIRSTNAME + " TEXT NOT NULL, "
            + Member.COLUMN_IMAGEPATH + " TEXT NOT NULL" + ");";

    //database creation statement of table group
    private static final String DATABASE_CREATE_GROUP =
            " CREATE TABLE " + TABLE_GROUP
                    + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Group.COLUMN_GROUP_NAME + " TEXT NOT NULL );";

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
        sqLiteDatabase.execSQL("DROP TABLE " + DatabaseHelper.TABLE_GROUP);
        sqLiteDatabase.execSQL("DROP TABLE " + DatabaseHelper.TABLE_MEMBER);
        onCreate(sqLiteDatabase);
    }

    /**
     * Builds the where-condition string for database requests.
     * @param column - the column that should have the passed value
     * @param value - the value
     * @return the built where-condition
     */
    public static String where(String column, long value) {
        return column + " = " + value;
    }


}
