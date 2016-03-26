package pinride.minhna.submission.ambulancenow.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pinride.minhna.submission.ambulancenow.compo.AC;
import pinride.minhna.submission.ambulancenow.compo.AS;
import pinride.minhna.submission.ambulancenow.R;
import pinride.minhna.submission.ambulancenow.compo.SoundHelper;
import pinride.minhna.submission.ambulancenow.compo.Utils;
import pinride.minhna.submission.ambulancenow.map.CloudbikeLocation;
import pinride.minhna.submission.ambulancenow.map.RouteResult;
import pinride.minhna.submission.ambulancenow.map.Step;
import pinride.minhna.submission.ambulancenow.map.ambLoc;
import pinride.minhna.submission.ambulancenow.module.Place;
import pinride.minhna.submission.ambulancenow.module.Status;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Minh on 3/26/2016.
 */
public class AmbulanceFragment extends MapFragment {

    @Bind(R.id.tv_from_ex)
    TextView pickAddress;
    @Bind(R.id.tv_to)
    TextView status;
    @Bind(R.id.tv_to_ex)
    TextView communicationWith;
    @Bind(R.id.btn_create_trip)
    Button btnCreateTrip;
    @Bind(R.id.img_mini_ava)
    ImageView imgMiniAva;
    @Bind(R.id.img_phone)
    ImageView imgPhone;

    private Context context;
    private String deviceId = Build.DEVICE;
    private Map<String, Place> maps;

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

    @Override
    public void onStart() {
        super.onStart();
        AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("ready");

        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    Status status2 = snapshot.getValue(Status.class);
                    int statusCode = status2.getStatusCode();
                    AS.vibrator.vibrate(500);
                    if (statusCode == AC.REQUEST_CODE) {
                        SoundHelper.run(context, R.raw.alarm, 0);
                        AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("not ready");
                        AS.userName = status2.getVictimName();
                        Picasso.with(context).load(status2.getVictimImgUrl()).error(R.drawable.fa_child_32_0_84ffff).into(imgMiniAva);
                        pickAddress.setText(status2.getAddress());
                        btnCreateTrip.setEnabled(true);
                        status.setText(context.getString(R.string.Request));
                        communicationWith.setText(context.getString(R.string.from) + " " + AS.userName);
                        imgPhone.setVisibility(View.VISIBLE);

                        maps = ambLoc.ambLoc();
                        pinride.minhna.submission.ambulancenow.module.Place place = maps.get(AS.ambulanceName);
                        AS.fromLocation = new LatLng(place.getLat(), place.getLng());
                        AS.endLocation = new LatLng(status2.getLat(), status2.getLng());
                        doMap();
                    } else if (statusCode == AC.END_CODE || statusCode == AC.CANCEL_CODE) {
                        AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("ready");
                        status.setText(context.getString(R.string.End));
                        communicationWith.setText(context.getString(R.string.from) + " " + AS.userName);
                        btnCreateTrip.setEnabled(false);
                        btnCreateTrip.setText(context.getString(R.string.Accept));
                        pickAddress.setText(context.getString(R.string.Pick_place_start));
                        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeValue();
                        imgPhone.setVisibility(View.GONE);
                    }
                } catch (Exception e){
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("not ready");
    }

    private void setupView() {
        btnCreateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status values = new Status();
                if (btnCreateTrip.getText().equals(context.getString(R.string.Accept))) {
                    SoundHelper.stop();
                    values.setStatusCode(AC.ACCEPT_CODE);
                    btnCreateTrip.setText(context.getString(R.string.Arrived));
                    status.setText(context.getString(R.string.Still_waiting));
                    communicationWith.setText(context.getString(R.string.from) + " " + AS.userName);
                } else if (btnCreateTrip.getText().equals(context.getString(R.string.Arrived))) {
                    values.setStatusCode(AC.ARRIVE_CODE);
                    btnCreateTrip.setText(context.getString(R.string.End));
                    status.setText(context.getString(R.string.Static));
                    communicationWith.setText(context.getString(R.string.no_pick_driver));
                } else if (btnCreateTrip.getText().equals(context.getString(R.string.End))) {
                    AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("ready");
                    values.setStatusCode(AC.END_CODE);
                    btnCreateTrip.setText(context.getString(R.string.Accept));
                    status.setText(context.getString(R.string.Static));
                    pickAddress.setText(context.getString(R.string.Pick_place_start));
                    communicationWith.setText(context.getString(R.string.no_pick_driver));
                    imgPhone.setVisibility(View.GONE);
                }
                AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).setValue(values);
            }
        });

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
