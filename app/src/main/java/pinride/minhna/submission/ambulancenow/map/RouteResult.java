package pinride.minhna.submission.ambulancenow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteResult implements Parcelable {

    @SerializedName("status")
    private String status;

    @SerializedName("routes")
    private List<Route> listRoute;

    private String googleResult;

    protected RouteResult(Parcel in) {
        status = in.readString();
    }

    public RouteResult() {

    }

    public static final Creator<RouteResult> CREATOR = new Creator<RouteResult>() {
        @Override
        public RouteResult createFromParcel(Parcel in) {
            return new RouteResult(in);
        }

        @Override
        public RouteResult[] newArray(int size) {
            return new RouteResult[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Route> getListRoute() {
        return listRoute;
    }

    public void setListRoute(List<Route> listRoute) {
        this.listRoute = listRoute;
    }

    public String getGoogleResult() {
        return googleResult;
    }

    public void setGoogleResult(String googleResult) {
        this.googleResult = googleResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(googleResult);
        dest.writeTypedList(listRoute);
    }
}
