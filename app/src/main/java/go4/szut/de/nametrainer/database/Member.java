package go4.szut.de.nametrainer.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ramazan on 26.03.2016.
 */
public class Member implements Parcelable {

    //parcelable identifier member
    public static final String DEFAULT_PARCELABLE_IDENTIFIER = "parcelable_member";

    //table columns identifier of member
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_IMAGEPATH = "imagepath";

    private Integer id;
    private String surname;
    private String firstname;
    private Integer groupID;
    private String imagePath;

    public Member() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    /**
     * Creates a content values representation of this member instance.
     * @return the content values
     */
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GROUP_ID, groupID);
        contentValues.put(COLUMN_SURNAME, surname);
        contentValues.put(COLUMN_FIRSTNAME, firstname);
        contentValues.put(COLUMN_IMAGEPATH, imagePath);
        return contentValues;
    }

    /**
     * Creates a member object instance by the passed cursor object.
     * @param cursor - the cursor object retrieved from database request
     * @return a member object instance
     */
    public static Member toMember(Cursor cursor) {
        Member member = new Member();
        member.id = cursor.getInt(0);
        member.groupID = cursor.getInt(1);
        member.surname = cursor.getString(2);
        member.firstname = cursor.getString(3);
        member.imagePath = cursor.getString(4);
        return member;
    }

    /**
     * Creates a member object instance by the passed parameters.
     * @param groupID - the groupid the member is in
     * @param firstname - the firstname of the member
     * @param surname - the surname of the member
     * @param imagePath - the path to the image of the member
     * @return a member object instance
     */
    public static Member toMember(Integer groupID, String firstname, String surname, String imagePath) {
        Member member = new Member();
        member.groupID = groupID;
        member.firstname = firstname;
        member.surname = surname;
        member.imagePath = imagePath;
        return member;
    }

    protected Member(Parcel in) {
        id = in.readInt();
        surname = in.readString();
        firstname = in.readString();
        groupID = in.readInt();
        imagePath = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };


    @Override
    public int describeContents() {
        return 5;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(surname);
        dest.writeString(firstname);
        dest.writeInt(groupID);
        dest.writeString(imagePath);
    }

}
