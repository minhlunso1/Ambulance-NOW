package pinride.minhna.submission.ambulancenow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route implements Parcelable {

    // What are these fields for, are they important?
    // warnings
    // waypoint_order

    @SerializedName("bounds")
    private Bound bound;

    @SerializedName("summary")
    private String summary;

    @SerializedName("legs")
    private List<Leg> listLegs;

    public Bound getBound() {
        return bound;
    }

    public void setBound(Bound bound) {
        this.bound = bound;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Leg> getListLegs() {
        return listLegs;
    }

    public void setListLegs(List<Leg> listLegs) {
        this.listLegs = listLegs;
    }

    protected Route(Parcel in) {
        bound = in.readParcelable(Bound.class.getClassLoader());
        summary = in.readString();
        listLegs = in.createTypedArrayList(Leg.CREATOR);
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bound, flags);
        dest.writeString(summary);
        dest.writeTypedList(listLegs);
    }
}
