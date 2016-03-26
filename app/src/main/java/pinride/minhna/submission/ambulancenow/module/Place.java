package pinride.minhna.submission.ambulancenow.module;

/**
 * Created by Minh on 3/26/2016.
 */
public class Place {
    private String address;
    private double lat;
    private double lng;

    public Place(String address, double lat, double lng) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
