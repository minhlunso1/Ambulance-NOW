package pinride.minhna.submission.ambulancenow.Fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pinride.minhna.submission.ambulancenow.R;
import pinride.minhna.submission.ambulancenow.map.Step;

/**
 * Created by Minh on 3/26/2016.
 */
public class MapFragment extends Fragment {
    @Bind(R.id.mapview)
    MapView mMapView;

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private HashMap<String, Marker> hashMarker = new HashMap<>();
    private LatLngBounds.Builder latLngBounds;
    private List<Step> mainStep;
    private boolean canMove = true;

    public LatLngBounds.Builder getLatLngBounds() {
        return latLngBounds;
    }

    public void setLatLngBounds(LatLngBounds.Builder latLngBounds) {
        this.latLngBounds = latLngBounds;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void clearLatLngBounds() {
        latLngBounds = new LatLngBounds.Builder();
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public List<Step> getMainStep() {
        return mainStep;
    }

    public void setMainStep(List<Step> mainStep) {
        this.mainStep = mainStep;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latLngBounds = new LatLngBounds.Builder();
    }

    public void setupMap(View view, final Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupMap();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void moveToLocation(LatLng latLng) {
        if (latLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    public void moveToLocation(CameraUpdate cameraUpdate) {
        mMap.moveCamera(cameraUpdate);
    }

    public void animateToLocation(LatLng latLng) {
        if (latLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setupMap();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void setupMap() {
        mMap = mMapView.getMap();
        if (mMap != null) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
//            mMap.setOnCameraChangeListener(onCameraChangeListener);
        }
    }

    public LatLng getPositionMap() {
        double x1 = mMap.getProjection().fromScreenLocation(new Point(0, 0)).latitude;
        double y1 = mMap.getProjection().fromScreenLocation(new Point(0, 0)).longitude;
        double x2 = mMap.getProjection().fromScreenLocation(new Point(mMapView.getWidth() - 1, mMapView.getHeight() - 1)).latitude;
        double y2 = mMap.getProjection().fromScreenLocation(new Point(mMapView.getWidth() - 1, mMapView.getHeight() - 1)).longitude;
        return new LatLng((x1 + x2) / 2, (y1 + y2) / 2);
    }

    public Marker addMarker(LatLng latLng, int markerResource) {
        return mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(markerResource))
                .draggable(false));
    }

    public void updateMarker(String key, LatLng latLng, int markerResource) {
        if (hashMarker.get(key) != null) {
            hashMarker.get(key).remove();
        }
        if (mMap != null) {
            final Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(markerResource))
                    .draggable(false));
            hashMarker.put(key, marker);
        }
    }

    public void addPolyline(ArrayList<LatLng> polylineList) {
        if (mMap != null) {
            try {
                mMap.addPolyline(new PolylineOptions().geodesic(true).addAll(polylineList).width(10f).color(getActivity().getResources().getColor(R.color.colorAccent)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

}
