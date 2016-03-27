package pinride.minhna.submission.ambulancenow.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pinride.minhna.submission.ambulancenow.compo.AC;
import pinride.minhna.submission.ambulancenow.compo.AS;
import pinride.minhna.submission.ambulancenow.compo.AmbulanceAdapter;
import pinride.minhna.submission.ambulancenow.R;
import pinride.minhna.submission.ambulancenow.compo.SoundHelper;
import pinride.minhna.submission.ambulancenow.compo.Utils;
import pinride.minhna.submission.ambulancenow.map.CloudbikeLocation;
import pinride.minhna.submission.ambulancenow.map.RouteResult;
import pinride.minhna.submission.ambulancenow.map.Step;
import pinride.minhna.submission.ambulancenow.map.ambLoc;
import pinride.minhna.submission.ambulancenow.module.Individual;
import pinride.minhna.submission.ambulancenow.module.Status;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Minh on 3/26/2016.
 */
public class VictimFragment extends MapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AmbulanceAdapter.ItemClickListener {

    @Bind(R.id.tv_from_ex)
    TextView pickAddress;
    @Bind(R.id.tv_to)
    TextView status;
    @Bind(R.id.tv_to_ex)
    TextView communicationWith;
    @Bind(R.id.btn_create_trip)
    Button btnCreateTrip;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.content_lv)
    RelativeLayout content;
    @Bind(R.id.img_phone)
    ImageView imgPhone;

    private ArrayList<Individual> ambulanceList;
    private AmbulanceAdapter adapter;
    private ProgressDialog progressDialog;

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 2;
    private String deviceId =Build.DEVICE;
    private boolean isOnTrip;
    ValueEventListener statusListener;
    private Map<String, pinride.minhna.submission.ambulancenow.module.Place> maps;

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
                    AS.vibrator.vibrate(500);
                    if (statusCode == AC.ACCEPT_CODE) {
                        SoundHelper.run(context, R.raw.notify, 0);
                        btnCreateTrip.setText(context.getString(R.string.Cancel));
                        status.setText(context.getString(R.string.Accepted));
                        if (AS.ambulanceName == null)
                            communicationWith.setText(context.getString(R.string.from)+ " "+ context.getString(R.string.vehicle));
                        else
                            communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
                        SoundHelper.stop();
                    } else if (statusCode == AC.ARRIVE_CODE) {
                        SoundHelper.run(context, R.raw.notify, 0);
                        btnCreateTrip.setText(context.getString(R.string.Cancel));
                        status.setText(context.getString(R.string.Arrived));
                        if (AS.ambulanceName == null)
                            communicationWith.setText(context.getString(R.string.from)+ " "+ context.getString(R.string.vehicle));
                        else
                            communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
                        SoundHelper.stop();
                    } else if (statusCode == AC.END_CODE) {
                        SoundHelper.run(context, R.raw.notify, 0);
                        btnCreateTrip.setText(context.getString(R.string.Find_driver_cap));
                        status.setText(context.getString(R.string.End));
                        if (AS.ambulanceName == null)
                            communicationWith.setText(context.getString(R.string.no_pick_driver));
                        else
                            communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
                        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeEventListener(statusListener);
                        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeValue();
                        imgPhone.setVisibility(View.GONE);
                        SoundHelper.stop();
                    }
                } catch (Exception e){
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        };

        prepareAmbulanceList();
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
                    imgPhone.setVisibility(View.GONE);
                } else if (btnCreateTrip.getText().equals(context.getString(R.string.Cancel))) {
                    values.setVictimId(deviceId);
                    values.setAmbulanceId(AS.ambulanceId);
                    values.setStatusCode(AC.CANCEL_CODE);
                    values.setAmbulanceName(AS.ambulanceName);
                    values.setVictimName(AS.userName);
                    btnCreateTrip.setText(context.getString(R.string.Find_driver_cap));
                    btnCreateTrip.setEnabled(true);
                    status.setText(context.getString(R.string.Static));
                    communicationWith.setText(context.getString(R.string.no_pick_driver));
                    AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeEventListener(statusListener);
                    AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).setValue(values);
                    imgPhone.setVisibility(View.GONE);
                } else if (btnCreateTrip.getText().equals(context.getString(R.string.Find_driver_cap))) {
                    btnCreateTrip.setText(context.getString(R.string.Cancel));
                    status.setText(context.getString(R.string.Wait_for_response));
                    communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
                    lv.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                }

            }
        });

    }

    private void prepareAmbulanceList() {
        ambulanceList = new ArrayList<>();
        adapter = new AmbulanceAdapter(getActivity(), ambulanceList, this);
        lv.setAdapter(adapter);

        AS.myFirebaseRef.child(AC.AMBULANCE_STR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ambulanceList.clear();
                for (DataSnapshot item:dataSnapshot.getChildren()) {
                    if (item.getValue().equals("ready")) {
                        maps = ambLoc.ambLoc();
                        pinride.minhna.submission.ambulancenow.module.Place place = maps.get(item.getKey());
                        ambulanceList.add(new Individual(item.getKey(), place.getAddress(), place.getLat(), place.getLng()));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @OnClick(R.id.btn_current_location)
    public void updateCurrentLocation(){
        progressDialog = ProgressDialog.show(context, "Chờ", "Đang xử lý", true);
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
                        progressDialog.dismiss();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        progressDialog.dismiss();
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

    @Override
    public void onItemClick(int position) {
        AS.ambulanceName=ambulanceList.get(position).getName();
        lv.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).addValueEventListener(statusListener);
        status.setText(context.getString(R.string.Wait_for_response));
        communicationWith.setText(context.getString(R.string.from)+ " "+ AS.ambulanceName);
        Status values = new Status();
        values.setStatusCode(AC.REQUEST_CODE);
        values.setVictimName(AS.userName);
        values.setVictimImgUrl(AS.profileImageUrl);
        values.setLat(AS.endLocation.latitude);
        values.setLng(AS.endLocation.longitude);
        values.setAddress(pickAddress.getText().toString());

        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).setValue(values);
        imgPhone.setVisibility(View.VISIBLE);

        doMap();
    }

    public void doMap(){
        pinride.minhna.submission.ambulancenow.module.Place place = maps.get(AS.ambulanceName);
        AS.fromLocation = new LatLng(place.getLat(), place.getLng());

        addMarker(AS.fromLocation, R.drawable.fa_map_marker_32_0_84ffff);
        addMarker(AS.endLocation, R.drawable.fa_map_marker_32_0_ffe57f);
        Utils.getGoogleResult(Utils.routingGoogleUrl(AS.fromLocation, AS.endLocation))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RouteResult>() {
                    @Override
                    public void call(RouteResult routeResult) {
                        if (routeResult.getListRoute().size() > 0) {
                            clearMap();
                            addMarker(AS.fromLocation, R.drawable.fa_map_marker_32_0_84ffff);
                            addMarker(AS.endLocation, R.drawable.fa_map_marker_32_0_ffe57f);
                            doPolyline(routeResult);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public void doPolyline(RouteResult routeResult) {
        clearLatLngBounds();
        List<Step> mainStepList = routeResult.getListRoute().get(0).getListLegs().get(0).getListSteps();
        CloudbikeLocation mainStartLocation = routeResult.getListRoute().get(0).getListLegs().get(0).getStartLocation();
        CloudbikeLocation mainEndLocation = routeResult.getListRoute().get(0).getListLegs().get(0).getEndLocation();

        getLatLngBounds().include(new LatLng(mainStartLocation.getLat(), mainStartLocation.getLng()));
        getLatLngBounds().include(new LatLng(mainEndLocation.getLat(), mainEndLocation.getLng()));
        setMainStep(mainStepList);
        for (int i = 0; i < mainStepList.size(); i++) {
            ArrayList<LatLng> polylineList = Utils.decodePoly(mainStepList.get(i).getPolyline().getPoints());
            for (int j = 0; j < polylineList.size(); j++) {
                getLatLngBounds().include(polylineList.get(j));
            }
            addPolyline(polylineList);
        }
        LatLngBounds bounds = getLatLngBounds().build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - 200;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, Utils.dptopx(getContext(), 80));
        moveToLocation(cu);
        setCanMove(false);
    }

    @OnClick(R.id.img_phone)
    public void call(){
        Utils.call(context);
    }
}
