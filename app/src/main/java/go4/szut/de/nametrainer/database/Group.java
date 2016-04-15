package go4.szut.de.nametrainer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by ramazan on 26.03.2016.
 */
public class Group implements Parcelable {

    //parcelable identifier group
    public static final String DEFAULT_PARCELABLE_IDENTIFIER = "parcelable_group";

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

    public int getMemberCount(Context context) {
        DataSource source = DataSource.getDataSourceInstance(context);
        source.open();
        int memberCount = -1;
        if(id != null) {
             memberCount = source.getMemberCount(id);
        }
        source.close();
        return memberCount;
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

    //PARCELABLE STUFF STARTS HERE

    protected Group(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
