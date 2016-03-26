package pinride.minhna.submission.ambulancenow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CloudbikeLocation implements Parcelable {

    @Expose
    protected long id;

    @SerializedName("lat")
    protected double lat;

    @SerializedName("lng")
    protected double lng;

    @SerializedName("time")
    protected long time;

    public CloudbikeLocation() {
        super();
    }

    public CloudbikeLocation(Double lat, Double lon) {
        this.lat = lat;
        this.lng = lon;
    }

    protected CloudbikeLocation(Parcel in) {
        id = in.readLong();
        lat = in.readDouble();
        lng = in.readDouble();
        time = in.readLong();
    }

    public static final Creator<CloudbikeLocation> CREATOR = new Creator<CloudbikeLocation>() {
        @Override
        public CloudbikeLocation createFromParcel(Parcel in) {
            return new CloudbikeLocation(in);
        }

        @Override
        public CloudbikeLocation[] newArray(int size) {
            return new CloudbikeLocation[size];
        }
    };

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeLong(time);
    }
}