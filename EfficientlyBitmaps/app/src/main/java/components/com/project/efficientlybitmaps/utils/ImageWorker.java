package components.com.project.efficientlybitmaps.utils;

import android.graphics.Bitmap;

/**
 * Created by r028367 on 18/07/2017.
 */

public abstract class ImageWorker {

    private static final String TAG = ImageWorker.class.getName();
    private static final int FADE_IN_TIME = 200;


    private Bitmap bitmap;
    private boolean fadeIntMitmap = true, exitTasktEarly = false, pauseWork = false;
    private final Object pauseWorkLock = new Object();
}
