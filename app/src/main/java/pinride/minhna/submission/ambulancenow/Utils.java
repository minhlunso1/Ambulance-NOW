package pinride.minhna.submission.ambulancenow;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Minh on 3/26/2016.
 */
public class Utils {

    public static Observable<String> getAddress(LatLng latLng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        return AS.apiClient.getAsync(url + latLng.latitude + "," + latLng.longitude).map(new Func1<String, String>() {
            @Override
            public String call(String json) {
                String str = "";
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getJSONArray("results").getJSONObject(0) != null)
                        str = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return str;
            }
        });
    }

}
