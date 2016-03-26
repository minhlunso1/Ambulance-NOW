package pinride.minhna.submission.ambulancenow.compo;

import android.os.Handler;

/**
 * Created by Minh on 3/27/2016.
 */
public class HandlerHelper {
    public static void run(final IHandlerDo iHandlerDo, int timeInMilis) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iHandlerDo.doThis();
            }
        }, timeInMilis);
    }

    public interface IHandlerDo{
        void doThis();
    }
}
