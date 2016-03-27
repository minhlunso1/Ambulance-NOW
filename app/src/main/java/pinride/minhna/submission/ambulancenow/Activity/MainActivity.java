package pinride.minhna.submission.ambulancenow.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import pinride.minhna.submission.ambulancenow.compo.AC;
import pinride.minhna.submission.ambulancenow.compo.AS;
import pinride.minhna.submission.ambulancenow.fragment.AmbulanceFragment;
import pinride.minhna.submission.ambulancenow.fragment.VictimFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);

        if (savedInstanceState == null) {
            Fragment fragment=null;
            if (getIntent().getIntExtra("type", 0)== AC.AMBULANCE)
                fragment = AmbulanceFragment.newInstance();
            else if (getIntent().getIntExtra("type", 0)== AC.VICTIM)
                fragment = VictimFragment.newInstance();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment).commit();
        }

        setContentView(frame);
    }

}