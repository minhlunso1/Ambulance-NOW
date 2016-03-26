package pinride.minhna.submission.ambulancenow;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pinride.minhna.submission.ambulancenow.Fragment.MapFragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Minh on 3/26/2016.
 */
public class AmbulanceFragment extends MapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.tv_from_ex)
    TextView pickAddress;
    @Bind(R.id.tv_to)
    TextView status;
    @Bind(R.id.tv_to_ex)
    TextView communicationWith;

    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public static AmbulanceFragment newInstance() {
        Bundle args = new Bundle();
        AmbulanceFragment fragment = new AmbulanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ambulance, container, false);
        ButterKnife.bind(this, view);
        setupMap(view, savedInstanceState);
        setupView();
        return view;
    }

    private void setupView() {

    }

    @OnClick(R.id.btn_current_location)
    public void getCurrentLocation() {
        udateCurrentLocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        udateCurrentLocation();
    }

    private void udateCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            final LatLng tmp = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (mLastLocation != null) {
                Utils.getAddress(tmp)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                pickAddress.setTextColor(getResources().getColor(R.color.v2_greyish_brown));
                                pickAddress.setText(s);
                                AS.fromLocation = tmp;
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
            }
        }
    }

    @Override
    public void onConnectionSuspended ( int i){

    }

    @Override
    public void onConnectionFailed (@NonNull ConnectionResult connectionResult){

    }

}
