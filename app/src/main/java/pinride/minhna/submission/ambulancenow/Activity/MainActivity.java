package pinride.minhna.submission.ambulancenow.Activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.FrameLayout;

        import pinride.minhna.submission.ambulancenow.AC;
        import pinride.minhna.submission.ambulancenow.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);

        if (savedInstanceState == null) {
            Fragment fragment=null;
            if (getIntent().getIntExtra("type", 0)== AC.AMBULANCE)
                fragment = AmbulanceFragment.newInstance();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment).commit();
        }

        setContentView(frame);
    }

}