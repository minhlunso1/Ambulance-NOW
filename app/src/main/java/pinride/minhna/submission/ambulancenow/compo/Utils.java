package pinride.minhna.submission.ambulancenow.compo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import pinride.minhna.submission.ambulancenow.map.RouteResult;
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

    public static Observable<RouteResult> getGoogleResult(String url) {
        return AS.apiClient.getAsync(url)
                .map(new Func1<String, RouteResult>() {
                    @Override
                    public RouteResult call(String json) {
                        RouteResult routeResult = new Gson().fromJson(json, RouteResult.class);
                        routeResult.setGoogleResult(json);
                        return routeResult;
                    }
                });
    }

    public static String routingGoogleUrl(LatLng start, LatLng end) {
        return "http://maps.googleapis.com/maps/api/directions/json?origin=" + start.latitude + "," + start.longitude + "&destination=" + end.latitude + "," + end.longitude + "&departure_time=" + new Date().getTime() / 1000 + "&mode=driving";
    }

    public static ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }
        return poly;
    }

    public static int dptopx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public void call(Context context){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "0915897496"));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                context.startActivity(callIntent);
    }

}
