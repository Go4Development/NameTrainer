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

    //all columns of table score
    private static final String[] ALL_COLUMNS_SCORE = {
            DatabaseHelper.COLUMN_ID,
            Score.COLUMN_GROUP_ID,
            Score.COLUMN_POINTS
    };


    private static DataSource instance;

    private DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Opens a writable database instance.
     * @throws SQLException - exception that gets thrown if an error occurs
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Util.D.l(this, "Open Database");
    }

    /**
     * Closes the current database instance.
     */
    public void close() {
        dbHelper.close();
        Util.D.l(this, "Close Database");
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
        Util.D.l(this, "Insert Member To Database");
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

    public Member insertMember(Member member) {
        return insertMember(member.getGroupID(), member.getSurname(),member.getFirstname(), member.getImagePath());
    }

    /**
     * Updates a member in database.
     * @param member - the values of the member to update
     * @return the passed member object
     */
    public Member updateMember(Member member) {
        Util.D.l(this, "Update Member To Database");
        ContentValues values = member.getContentValues();
        database.update(DatabaseHelper.TABLE_MEMBER, values,
                DatabaseHelper.where(DatabaseHelper.COLUMN_ID, member.getId()), null);
        return member;
    }

    /**
     * Deletes a member from the database.
     * @param member - the member to delete
     */
    public void deleteMember(Member member) {
        Util.D.l(this, "Delete Member From Database");
        database.delete(DatabaseHelper.TABLE_MEMBER,
                DatabaseHelper.where(DatabaseHelper.COLUMN_ID, member.getId()), null);
    }

    /**
     * Inserts a group to database.
     * @param name - the name of the group
     * @return a complete group object instance containing the id of row entry
     */
    public Group insertGroup(String name) {
        Util.D.l(this, "Insert Group To Database");
        Group preGroup = Group.toGroup(name);
        ContentValues values = preGroup.getContentValues();
        long insertId = database.insert(DatabaseHelper.TABLE_GROUP, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_GROUP,
                ALL_COLUMNS_GROUP,
                DatabaseHelper.where(DatabaseHelper.COLUMN_ID, insertId), null,
                null, null, null);
        cursor.moveToFirst();
        Group group = Group.toGroup(cursor);
        cursor.close();
        return group;
    }

    /**
     * Updates a group in database.
     * @param group - the values of the group to update
     * @return the passed group object
     */
    public Group updateGroup(Group group) {
        ContentValues values = group.getContentValues();
        database.update(DatabaseHelper.TABLE_GROUP, values,
                DatabaseHelper.where(DatabaseHelper.COLUMN_ID, group.getId()), null);
        return group;
    }

    /**
     * Deletes a group from the database.
     * @param group - the group to delete
     */
    public void deleteGroup(Group group) {
        database.delete(DatabaseHelper.TABLE_GROUP,
                DatabaseHelper.where(DatabaseHelper.COLUMN_ID, group.getId()), null);
        database.delete(DatabaseHelper.TABLE_MEMBER,
                DatabaseHelper.where(Member.COLUMN_GROUP_ID, group.getId()), null);
    }

    /**
     * Returns a list of all members that are members of specified group.
     * @param id - the group id
     * @return list containing all members
     */
    public ArrayList<Member> getMembers(Integer id) {
        ArrayList<Member> members = new ArrayList<Member>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_MEMBER,
                ALL_COLUMNS_MEMBER, DatabaseHelper.where(Member.COLUMN_GROUP_ID, id), null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Member member = Member.toMember(cursor);
            members.add(member);
            cursor.moveToNext();
        }
        cursor.close();
        return members;
    }

    /**
     * Returns a list of all groups.
     * @return list containing all groups
     */
    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> groups = new ArrayList<Group>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_GROUP,
                ALL_COLUMNS_GROUP, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Group group = Group.toGroup(cursor);
            groups.add(group);
            cursor.moveToNext();
        }
        cursor.close();
        return groups;
    }

    /**
     * Returns the number of members that are in the specified group having the passed
     * groupId
     * @param groupId - the id that belongs to a certain group
     * @return the number of members
     */
    public int getMemberCount(int groupId) {
        Cursor cursor = database.rawQuery("SELECT COUNT(" + DatabaseHelper.COLUMN_ID + ") FROM " +
                DatabaseHelper.TABLE_MEMBER + " WHERE " + DatabaseHelper.where(Member.COLUMN_GROUP_ID, groupId), null);
        cursor.moveToFirst();
        int memberCount = cursor.getInt(0);
        cursor.close();
        return memberCount;
    }

    public void tables(){
        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Util.D.l(this, "Table Name=> " + c.getString(0));
                c.moveToNext();
            }
        }
        c.close();
    }

    /**
     * Singleton for DataSource instance. Only one instance of DataSource is necessary.
     * @return the instance of DataSource class
     */
    public static DataSource getDataSourceInstance(Context context) {
        if(instance == null) {
            instance = new DataSource(context);
        }
        return instance;
    }


}

