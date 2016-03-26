package pinride.minhna.submission.ambulancenow.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pinride.minhna.submission.ambulancenow.AC;
import pinride.minhna.submission.ambulancenow.AS;
import pinride.minhna.submission.ambulancenow.R;

/**
 * Created by Minh on 3/26/2016.
 */
public class AmbulanceLoginActivity extends FirebaseLoginBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setEnabledAuthProvider(AuthProviderType.PASSWORD);
    }

    @OnClick(R.id.btn_login)
    public void login(){
        showFirebaseLoginPrompt();
    }
    @OnClick(R.id.btn_quit)
    public void quit(){
        finish();
    }
    @OnClick(R.id.tv_change_mode)
    public void changeMode(){
        startActivity(new Intent(AmbulanceLoginActivity.this, UserLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    protected Firebase getFirebaseRef() {
        return AS.myFirebaseRef;
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {
        dismissFirebaseLoginPrompt();
        Toast.makeText(this, getString(R.string.Login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {
        resetFirebaseLoginPrompt();
        Toast.makeText(this, getString(R.string.Login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onFirebaseLoggedIn(AuthData authData) {
        super.onFirebaseLoggedIn(authData);
        AS.ambulanceName = authData.getProviderData().get("email").toString();
        AS.ambulanceName = AS.ambulanceName.substring(0, AS.ambulanceName.indexOf('@'));
        AS.profileImageUrl = authData.getProviderData().get("profileImageURL").toString();
        Intent intent = new Intent(AmbulanceLoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("type", AC.AMBULANCE);
        startActivity(intent);
    }

    @Override
    protected void onFirebaseLoggedOut() {
        super.onFirebaseLoggedOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
