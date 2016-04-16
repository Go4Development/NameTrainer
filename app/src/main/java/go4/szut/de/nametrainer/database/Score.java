package go4.szut.de.nametrainer.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rene on 16.04.2016.
 */
public class Score implements Parcelable {

    public static final String COLUMN_GROUP_ID = "groupid";
    public static final String COLUMN_POINTS = "points";

    private Integer id;

    public Score() {

    }


    /*
     * PARCELABLE STUFF STARTS HERE
     */

    protected Score(Parcel in) {

    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
