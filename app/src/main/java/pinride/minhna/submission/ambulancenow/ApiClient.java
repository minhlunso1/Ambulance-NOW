package pinride.minhna.submission.ambulancenow;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class ApiClient {
    private final OkHttpClient mHttpClient;
    private final Gson mGson;
    private Context mContext;

    public ApiClient(@NonNull OkHttpClient httpClient,
                     @NonNull Gson mGson,
                     @NonNull Context context) {
        this.mHttpClient = httpClient;
        this.mGson = mGson;
        this.mContext = context;
    }

    /**
     * @param url String : the url to request
     * @return an Observable that will emit the object result
     */
    public Observable<String> getAsync(String url) {
        final Request request = new Request.Builder()
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/json;charset=UTF-8")
                .url(url)
                .build();
        return networkAsync(request);
    }

    private Observable<String> networkAsync(final Request request) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Call call = mHttpClient.newCall(request);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        call.cancel();
                    }
                }));
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        handleResponse(response, subscriber);
                    }
                });
            }
        });
    }

    private void handleResponse(Response response, Subscriber<? super String> subscriber) {
        if (!response.isSuccessful()) {
            subscriber.onError(new Exception("Unexpected code " + response));
        } else {
            try {
                final String json = response.body().string();
                subscriber.onNext(json);
                subscriber.onCompleted();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        }
    }


}

