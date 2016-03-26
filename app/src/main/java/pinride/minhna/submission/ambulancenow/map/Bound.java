package pinride.minhna.submission.ambulancenow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Created by nongdenchet on 7/20/15.
 */
public class Bound implements Parcelable {

    @SerializedName("northeast")
    private CloudbikeLocation northeast;

    @SerializedName("southwest")
    private CloudbikeLocation southwest;

    protected Bound(Parcel in) {
        northeast = in.readParcelable(CloudbikeLocation.class.getClassLoader());
        southwest = in.readParcelable(CloudbikeLocation.class.getClassLoader());
    }

    public static final Creator<Bound> CREATOR = new Creator<Bound>() {
        @Override
        public Bound createFromParcel(Parcel in) {
            return new Bound(in);
        }

        @Override
        public Bound[] newArray(int size) {
            return new Bound[size];
        }
    };

    public CloudbikeLocation getNortheast() {
        return northeast;
    }

    public void setNortheast(CloudbikeLocation northeast) {
        this.northeast = northeast;
    }

    public CloudbikeLocation getSouthwest() {
        return southwest;
    }

    public void setSouthwest(CloudbikeLocation southwest) {
        this.southwest = southwest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(northeast, flags);
        dest.writeParcelable(southwest, flags);
    }
}
