package pinride.minhna.submission.ambulancenow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Polyline implements Parcelable {

    @SerializedName("points")
    private String points;

    protected Polyline(Parcel in) {
        points = in.readString();
    }

    public static final Creator<Polyline> CREATOR = new Creator<Polyline>() {
        @Override
        public Polyline createFromParcel(Parcel in) {
            return new Polyline(in);
        }

        @Override
        public Polyline[] newArray(int size) {
            return new Polyline[size];
        }
    };

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(points);
    }
}
