package pinride.minhna.submission.ambulancenow.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import pinride.minhna.submission.ambulancenow.AC;
import pinride.minhna.submission.ambulancenow.AS;
import pinride.minhna.submission.ambulancenow.AmbulanceDialog;
import pinride.minhna.submission.ambulancenow.R;
import pinride.minhna.submission.ambulancenow.Utils;
import pinride.minhna.submission.ambulancenow.module.Status;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Minh on 3/26/2016.
 */
public class VictimFragment extends MapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.tv_from_ex)
    TextView pickAddress;
    @Bind(R.id.tv_to)
    TextView status;
    @Bind(R.id.tv_to_ex)
    TextView communicationWith;
    @Bind(R.id.btn_create_trip)
    Button btnCreateTrip;

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 2;
    private String deviceId =Build.DEVICE;
    private boolean isOnTrip;
    ValueEventListener statusListener;

    public static VictimFragment newInstance() {
        Bundle args = new Bundle();
        VictimFragment fragment = new VictimFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        statusListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    Status status2 = snapshot.getValue(Status.class);
                    String ambulanceId = status2.getAmbulanceId();
                    int statusCode = status2.getStatusCode();
                    if (statusCode == AC.ACCEPT_CODE) {
                        Status values = new Status();
                        btnCreateTrip.setText(context.getString(R.string.Cancel));
                        status.setText(context.getString(R.string.Accepted));
                        communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
                    } else if (statusCode == AC.ARRIVE_CODE) {
                        Status values = new Status();
                        btnCreateTrip.setText(context.getString(R.string.Cancel));
                        status.setText(context.getString(R.string.Arrived));
                        communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
                    } else if (statusCode == AC.END_CODE) {
                        Status values = new Status();
                        btnCreateTrip.setText(context.getString(R.string.Cancel));
                        status.setText(context.getString(R.string.End));
                        communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
                        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeEventListener(statusListener);
                        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeValue();
                    }
                } catch (Exception e){
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_victim, container, false);
        ButterKnife.bind(this, view);
        setupMap(view, savedInstanceState);
        setupView();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setupView() {
        btnCreateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status values = new Status();
                if (btnCreateTrip.getText().equals(context.getString(R.string.End))) {
                    btnCreateTrip.setText(context.getString(R.string.Find_driver_cap));
                    status.setText(context.getString(R.string.Static));
                    communicationWith.setText(context.getString(R.string.no_pick_driver));

                    values.setVictimId(deviceId);
                    values.setAmbulanceId(AS.ambulanceId);
                    values.setStatusCode(AC.END_CODE);
                    values.setAmbulanceName(AS.ambulanceName);
                    values.setVictimName(AS.userName);
                    btnCreateTrip.setEnabled(false);
                    AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeEventListener(statusListener);
                    AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).setValue(values);
                } else if (btnCreateTrip.getText().equals("Cancel")) {
                    values.setVictimId(deviceId);
                    values.setAmbulanceId(AS.ambulanceId);
                    values.setStatusCode(AC.CANCEL_CODE);
                    values.setAmbulanceName(AS.ambulanceName);
                    values.setVictimName(AS.userName);
                    btnCreateTrip.setEnabled(false);
                    btnCreateTrip.setText(context.getString(R.string.Find_driver_cap));
                    status.setText(context.getString(R.string.Static));
                    communicationWith.setText(context.getString(R.string.no_pick_driver));
                    AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeEventListener(statusListener);
                    AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).setValue(values);
                } else if (btnCreateTrip.getText().equals(context.getString(R.string.Find_driver_cap))) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    AmbulanceDialog fragment = AmbulanceDialog.newInstance();
                    fragment.show(fm, "tag");
                }

            }
        });
    }

    @OnClick(R.id.btn_current_location)
    public void updateCurrentLocation(){
        Utils.getAddress(AS.currentLocation)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        pickAddress.setTextColor(getResources().getColor(R.color.v2_greyish_brown));
                        pickAddress.setText(s);
                        AS.endLocation = AS.currentLocation;
                        moveToLocation(AS.currentLocation);
                        btnCreateTrip.setEnabled(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
    @OnClick(R.id.rl_from)
    public void switchMap(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(data, context);
                if (!place.getAddress().equals("")) {
                    pickAddress.setText(place.getName() + ", " + place.getAddress());
                    AS.endLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    btnCreateTrip.setEnabled(true);
                } else
                    Toast.makeText(context, context.getString(R.string.Please_get_another_location), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onEventMainThread(String event){
        if (event.equals("request")){
            Status values = new Status();
            btnCreateTrip.setText(context.getString(R.string.Cancel));
            status.setText(context.getString(R.string.Wait_for_response));
            communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
            values.setVictimId(deviceId);
            values.setAmbulanceId(AS.ambulanceId);
            values.setStatusCode(AC.REQUEST_CODE);
            values.setAmbulanceName(AS.ambulanceName);
            values.setVictimName(AS.userName);
            values.setVictimImgUrl(AS.profileImageUrl);
            AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).setValue(values);
        }
    }
}
