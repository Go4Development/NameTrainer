package go4.szut.de.nametrainer.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by ramazan on 26.03.2016.
 */
public class Group {

    //table columns identifier of group
    public static final String COLUMN_GROUP_NAME = "group_name";

    private Integer id;
    private String name;

    public Group() {

    }

    public String getName() {
        return name;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GROUP_NAME, name);
        return contentValues;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static Group toGroup(Cursor cursor) {
        Group group = new Group();
        group.id = cursor.getInt(0);
        group.name = cursor.getString(1);
        return group;
    }

    public static Group toGroup(String name) {
        Group group = new Group();
        group.name = name;
        return group;
    }
}
