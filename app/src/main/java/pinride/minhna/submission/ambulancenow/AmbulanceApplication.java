package pinride.minhna.submission.ambulancenow;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import pinride.minhna.submission.ambulancenow.compo.AC;
import pinride.minhna.submission.ambulancenow.compo.AS;
import pinride.minhna.submission.ambulancenow.compo.ApiClient;

/**
 * Created by Minh on 3/25/2016.
 */
public class AmbulanceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        AS.myFirebaseRef = new Firebase(AC.firebaseUrl);
        AS.apiClient = new ApiClient(new OkHttpClient(), new Gson(), this);
        AS.vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    }

}
