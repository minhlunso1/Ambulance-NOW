package pinride.minhna.submission.ambulancenow;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

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
    }

}
