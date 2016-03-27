package pinride.minhna.submission.ambulancenow.compo;

import android.content.Context;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.Vibrator;

/**
 * Created by Minh on 3/27/2016.
 */
public class SoundHelper {
    private static MediaPlayer mp;
    private static Vibrator vi;

    public static void run(final Context context, final int file) {
        run(context, file, 0);
    }

    public static void run(final Context context, final int file, int delay) {
        HandlerHelper.run(new HandlerHelper.IHandlerDo() {
            @Override
            public void doThis() {
                try {
                    if (context != null) {
                        mp = MediaPlayer.create(context, file);
                        vi = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {0, 100, 1000};
                        vi.vibrate(pattern, 0);
                        mp.setLooping(true);
                        mp.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay);
    }

    public static void runWithoutLooping(final Context context, final int file, int delay) {
        HandlerHelper.run(new HandlerHelper.IHandlerDo() {
            @Override
            public void doThis() {
                try {
                    if (context != null) {
                        mp = MediaPlayer.create(context, file);
                        vi = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {0, 100, 1000};
                        //vi.vibrate(pattern, 0);
                        mp.setLooping(false);
                        mp.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay);
    }

    public static void stop() {
        try {
            mp.stop();

            vi.cancel();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

