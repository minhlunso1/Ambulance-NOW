package pinride.minhna.submission.ambulancenow.module;

/**
 * Created by Minh on 3/26/2016.
 */
public class Status {
    private String victimId;
    private String ambulanceId;
    private int statusCode;
    private String victimName;
    private String victimImgUrl;
    private String ambulanceName;
    private String ambulanceImgUrl;
    private boolean isReady;
    private String address;

    public Status(){}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getVictimId() {
        return victimId;
    }

    public void setVictimId(String victimId) {
        this.victimId = victimId;
    }

    public String getAmbulanceId() {
        return ambulanceId;
    }

    public void setAmbulanceId(String ambulanceId) {
        this.ambulanceId = ambulanceId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getVictimImgUrl() {
        return victimImgUrl;
    }

    public void setVictimImgUrl(String victimImgUrl) {
        this.victimImgUrl = victimImgUrl;
    }

    public String getAmbulanceName() {
        return ambulanceName;
    }

    public void setAmbulanceName(String ambulanceName) {
        this.ambulanceName = ambulanceName;
    }

    public String getAmbulanceImgUrl() {
        return ambulanceImgUrl;
    }

    public void setAmbulanceImgUrl(String ambulanceImgUrl) {
        this.ambulanceImgUrl = ambulanceImgUrl;
    }
}
