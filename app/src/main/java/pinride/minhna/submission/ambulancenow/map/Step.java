package pinride.minhna.submission.ambulancenow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {

    @SerializedName("distance")
    private Info distance;

    @SerializedName("duration")
    private Info duration;

    @SerializedName("start_location")
    private CloudbikeLocation startLocation;

    @SerializedName("end_location")
    private CloudbikeLocation endLocation;

    @SerializedName("html_instructions")
    private String html;

    @SerializedName("polyline")
    private Polyline polyline;

    @SerializedName("travel_mode")
    private String mode;

    public Step(Info distance, Info duration, CloudbikeLocation startLocation,
                CloudbikeLocation endLocation, String html, Polyline polyline, String mode) {
        this.distance = distance;
        this.duration = duration;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.html = html;
        this.polyline = polyline;
        this.mode = mode;
    }

    protected Step(Parcel in) {
        distance = in.readParcelable(Info.class.getClassLoader());
        duration = in.readParcelable(Info.class.getClassLoader());
        startLocation = in.readParcelable(CloudbikeLocation.class.getClassLoader());
        endLocation = in.readParcelable(CloudbikeLocation.class.getClassLoader());
        html = in.readString();
        polyline = in.readParcelable(Polyline.class.getClassLoader());
        mode = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
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
        dest.writeParcelable(endLocation, flags);
        dest.writeString(html);
        dest.writeParcelable(polyline, flags);
        dest.writeString(mode);
    }
}
