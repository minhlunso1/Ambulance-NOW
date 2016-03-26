package pinride.minhna.submission.ambulancenow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leg implements Parcelable {

    // Is this field in api important?
    // via_waypoint

    @SerializedName("distance")
    private Info distance;

    @SerializedName("duration")
    private Info duration;

    @SerializedName("start_location")
    private CloudbikeLocation startLocation;

    @SerializedName("start_address")
    private String startAddress;

    @SerializedName("end_location")
    private CloudbikeLocation endLocation;

    @SerializedName("end_address")
    private String endAddress;

    @SerializedName("steps")
    private List<Step> listSteps;

    protected Leg(Parcel in) {
        distance = in.readParcelable(Info.class.getClassLoader());
        duration = in.readParcelable(Info.class.getClassLoader());
        startLocation = in.readParcelable(CloudbikeLocation.class.getClassLoader());
        startAddress = in.readString();
        endLocation = in.readParcelable(CloudbikeLocation.class.getClassLoader());
        endAddress = in.readString();
        listSteps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Leg> CREATOR = new Creator<Leg>() {
        @Override
        public Leg createFromParcel(Parcel in) {
            return new Leg(in);
        }

        @Override
        public Leg[] newArray(int size) {
            return new Leg[size];
        }
    };

    public Info getDistance() {
        return distance;
    }

    public void setDistance(Info distance) {
        this.distance = distance;
    }

    public Info getDuration() {
        return duration;
    }

    public void setDuration(Info duration) {
        this.duration = duration;
    }

    public CloudbikeLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(CloudbikeLocation startLocation) {
        this.startLocation = startLocation;
    }

    public CloudbikeLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(CloudbikeLocation endLocation) {
        this.endLocation = endLocation;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public List<Step> getListSteps() {
        return listSteps;
    }

    public void setListSteps(List<Step> listSteps) {
        this.listSteps = listSteps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(distance, flags);
        dest.writeParcelable(duration, flags);
        dest.writeParcelable(startLocation, flags);
        dest.writeString(startAddress);
        dest.writeParcelable(endLocation, flags);
        dest.writeString(endAddress);
        dest.writeTypedList(listSteps);
    }
}
