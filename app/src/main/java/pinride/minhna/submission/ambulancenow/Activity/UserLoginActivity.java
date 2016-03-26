package pinride.minhna.submission.ambulancenow.activity;

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
import pinride.minhna.submission.ambulancenow.compo.AC;
import pinride.minhna.submission.ambulancenow.compo.AS;
import pinride.minhna.submission.ambulancenow.R;

/**
 * Created by Minh on 3/26/2016.
 */
public class UserLoginActivity extends FirebaseLoginBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        startActivity(new Intent(UserLoginActivity.this, AmbulanceLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
        if (authData.getProvider().equals("password")) {
            AS.userName = authData.getProviderData().get("email").toString();
            AS.userName = AS.userName.substring(0, AS.userName.indexOf('@'));
            AS.profileImageUrl = authData.getProviderData().get("profileImageURL").toString();
            Intent intent = new Intent(UserLoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("type", AC.VICTIM);
            startActivity(intent);
        } else {
            try {
                logout();
                resetFirebaseLoginPrompt();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onFirebaseLoggedOut() {
        super.onFirebaseLoggedOut();
    }

}
