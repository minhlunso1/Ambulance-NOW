package pinride.minhna.submission.ambulancenow.compo;

import android.os.Vibrator;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Minh on 3/25/2016.
 */
public class AS {
    public static Firebase myFirebaseRef;
    public static String userName;
    public static String profileImageUrl;
    public static LatLng fromLocation;
    public static LatLng endLocation;
    public static LatLng currentLocation = new LatLng(11.054282, 106.683191);
    public static ApiClient apiClient;
    public static String ambulanceId;
    public static String ambulanceName;
    public static String key = "status_hackathon";
    public static Vibrator vibrator;
}
