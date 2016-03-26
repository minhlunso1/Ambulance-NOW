package pinride.minhna.submission.ambulancenow;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import pinride.minhna.submission.ambulancenow.module.Individual;

/**
 * Created by Minh on 3/26/2016.
 */
public class AmbulanceDialog extends DialogFragment {

    @Bind(R.id.lv)
    ListView lv;
    private ArrayList<Individual> ambulanceList;
    private AmbulanceAdapter adapter;

    public static AmbulanceDialog newInstance() {
        AmbulanceDialog frag = new AmbulanceDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_ambulance, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getDialog().setTitle("Tự đông cập nhật");
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareAmbulanceList();
    }

    private void prepareAmbulanceList() {
        ambulanceList = new ArrayList<>();
        adapter = new AmbulanceAdapter(getActivity(), ambulanceList);
        lv.setAdapter(adapter);

        AS.myFirebaseRef.child(AC.AMBULANCE_STR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ambulanceList.clear();
                for (DataSnapshot item:dataSnapshot.getChildren()) {
                    if (item.getValue().equals("ready"))
                        ambulanceList.add(new Individual(item.getKey(), null));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AS.ambulanceName=ambulanceList.get(position).getName();
                EventBus.getDefault().post("request");
                dismiss();
            }
        });
    }
}
