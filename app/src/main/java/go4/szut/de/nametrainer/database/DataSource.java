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
public class DataSource {


    //database instance
    private SQLiteDatabase database;
    //database helper instance
    private DatabaseHelper dbHelper;

    //all columns of table member
    private static final String[] ALL_COLUMNS_MEMBER = {
            DatabaseHelper.COLUMN_ID,
            Member.COLUMN_GROUP_ID,
            Member.COLUMN_SURNAME,
            Member.COLUMN_FIRSTNAME,
            Member.COLUMN_IMAGEPATH };

    //all columns of table group
    private static final String[] ALL_COLUMNS_GROUP = {
            DatabaseHelper.COLUMN_ID,
            Group.COLUMN_GROUP_NAME
    };

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Opens a writable database instance.
     * @throws SQLException - exception that gets thrown if an error occurs
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the current database instance.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Inserts a member to database.
     * @param groupID - the groupid that the member is in
     * @param surname - the surname of the member
     * @param firstname - the firstname of the member
     * @param imagePath - the path to image of the member
     * @return a complete member object instance containing the id of row entry
     */
    public Member insertMember(Integer groupID, String surname, String firstname, String imagePath) {
        Member preMember = Member.toMember(groupID, firstname, surname, imagePath);
        ContentValues values = preMember.getContentValues();
        long insertId = database.insert(DatabaseHelper.TABLE_MEMBER, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_MEMBER,
                ALL_COLUMNS_MEMBER, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Member member = Member.toMember(cursor);
        cursor.close();
        return member;
    }

    /**
     * Inserts a group to database.
     * @param name - the name of the group
     * @return a complete group object instance containing the id of row entry
     */
    public Group insertGroup(String name){
        Group preGroup = Group.toGroup(name);
        ContentValues values = preGroup.getContentValues();
        long insertId = database.insert(DatabaseHelper.TABLE_GROUP, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_GROUP,
                ALL_COLUMNS_GROUP, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Group group = Group.toGroup(cursor);
        cursor.close();
        return group;

    }

    /**
     * Deletes a member from the database.
     * @param member - the member to delete
     */
    public void deleteMember(Member member) {
        Integer id = member.getId();
        database.delete(DatabaseHelper.TABLE_MEMBER, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    /**
     * Returns a list of all members.
     * @return list containing all members
     */
    public ArrayList<Member> getAllMember() {
        ArrayList<Member> members = new ArrayList<Member>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_MEMBER,
                ALL_COLUMNS_MEMBER, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Member member = Member.toMember(cursor);
            members.add(member);
            cursor.moveToNext();
        }
        cursor.close();
        return members;
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

