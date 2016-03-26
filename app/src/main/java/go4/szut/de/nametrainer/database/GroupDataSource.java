package go4.szut.de.nametrainer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import go4.szut.de.nametrainer.util.Util;


/**
 * Created by ramazan on 26.03.2016.
 */
public class GroupDataSource {


    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_GROUP_ID,
            DatabaseHelper.COLUMN_SURNAME,
            DatabaseHelper.COLUMN_FIRSTNAME,
            DatabaseHelper.COLUMN_IMAGEPATH };

    public GroupDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Member createMember(Integer groupID ,String surname, String firstname, String imagepath) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_GROUP_ID, groupID);
        values.put(DatabaseHelper.COLUMN_SURNAME, surname);
        values.put(DatabaseHelper.COLUMN_FIRSTNAME, firstname);
        values.put(DatabaseHelper.COLUMN_IMAGEPATH, imagepath);

        long insertId = database.insert(DatabaseHelper.TABLE_MEMBER, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_MEMBER,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Member newMember = cursorToMember(cursor);
        cursor.close();
        return newMember;
    }

    public Group createGroup(String name){


        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_GROUP_NAME, name);

        long insertId = database.insert(DatabaseHelper.TABLE_GROUP, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_GROUP,
                new String[]{
                        DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_GROUP_NAME},
                DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Group newGroup = cursorToGroup(cursor);
        cursor.close();
        return newGroup;

    }

    public void deleteMember(Member member) {
        Integer id = member.get_id();
        database.delete(DatabaseHelper.TABLE_MEMBER, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Member> getAllMember() {
        ArrayList<Member> members = new ArrayList<Member>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_MEMBER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Member member = cursorToMember(cursor);
            members.add(member);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return members;
    }

    private Member cursorToMember(Cursor cursor) {

        Member member = new Member();
        member.set_id(cursor.getInt(0));
        member.setGroupID(cursor.getInt(1));
        member.setSurname(cursor.getString(2));
        member.setFirstname(cursor.getString(3));
        member.setImagePath(cursor.getString(4));

        return member;
    }

    private Group cursorToGroup(Cursor cursor){
        Group group = new Group();
        group.setId(cursor.getInt(0));
        group.setName(cursor.getString(1));

        return group;
    }

    public void tables(){
        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Util.l(this, "Table Name=> " + c.getString(0));
                c.moveToNext();
            }
        }
    }
}

