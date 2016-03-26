package pinride.minhna.submission.ambulancenow.Fragment;

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
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import pinride.minhna.submission.ambulancenow.AC;
import pinride.minhna.submission.ambulancenow.AS;
import pinride.minhna.submission.ambulancenow.R;
import pinride.minhna.submission.ambulancenow.module.Status;

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

    private Context context;
    private String deviceId = Build.DEVICE;

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
                    if (statusCode == AC.REQUEST_CODE) {
                        AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("not ready");
                        AS.userName = status2.getVictimName();
                        Picasso.with(context).load(status2.getVictimImgUrl()).error(R.drawable.fa_child_256_0_84ffff_none).into(imgMiniAva);
                        btnCreateTrip.setEnabled(true);
                        status.setText(context.getString(R.string.Request));
                        communicationWith.setText(context.getString(R.string.from) + " " + AS.userName);
                    } else if (statusCode == AC.END_CODE || statusCode == AC.CANCEL_CODE) {
                        AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("ready");
                        status.setText(context.getString(R.string.End));
                        communicationWith.setText(context.getString(R.string.from) + " " + AS.userName);
                        btnCreateTrip.setEnabled(false);
                        btnCreateTrip.setText(context.getString(R.string.Accept));
                        AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).removeValue();
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
                    btnCreateTrip.setText(context.getString(R.string.Arrived));
                    status.setText(context.getString(R.string.Still_waiting));
                    communicationWith.setText(context.getString(R.string.from) + " " + AS.userName);

                    values.setVictimId(deviceId);
                    values.setAmbulanceId(AS.ambulanceId);
                    values.setStatusCode(AC.ACCEPT_CODE);
                    values.setAmbulanceName(AS.ambulanceName);
                    values.setVictimName(AS.userName);
                } else if (btnCreateTrip.getText().equals("Arrive")) {
                    values.setStatusCode(AC.ARRIVE_CODE);
                    btnCreateTrip.setText(context.getString(R.string.End));
                    status.setText(context.getString(R.string.Static));
                    communicationWith.setText(context.getString(R.string.no_pick_driver));
                } else if (btnCreateTrip.getText().equals(context.getString(R.string.End))) {
                    btnCreateTrip.setText(context.getString(R.string.Accept));
                    status.setText(context.getString(R.string.Static));
                    communicationWith.setText(context.getString(R.string.no_pick_driver));
                    AS.myFirebaseRef.child(AC.AMBULANCE_STR).child(AS.ambulanceName).setValue("ready");
                }
                AS.myFirebaseRef.child(AS.key).child(AS.ambulanceName).setValue(values);
            }
        });
    }

}
