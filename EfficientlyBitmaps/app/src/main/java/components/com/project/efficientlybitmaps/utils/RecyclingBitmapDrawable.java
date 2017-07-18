package components.com.project.efficientlybitmaps.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by r028367 on 18/07/2017.
 *
 * Uma especializacao de {@link BitmapDrawable} que permite
 * verificar se a imagem esta sendo mostrada ou esta em cache
 */

public class RecyclingBitmapDrawable extends BitmapDrawable {
    public static final String TAG = RecyclingBitmapDrawable.class.getName();
    private int cacheRefCount = 0, displatRefCount = 0;
    private boolean hasBeenDisplayed;
    /**
     * Create drawable from a bitmap, setting initial target density based on
     * the display metrics of the resources.
     *
     * @param res
     * @param bitmap
     */
    public RecyclingBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    public void setInDisplayed(boolean isDisplayed) {
        synchronized (this) {
            if(isDisplayed) {
                displatRefCount++;
                hasBeenDisplayed = true;
            }
            else {
                displatRefCount--;
            }
        }
        checkState();
    }

    public void setIsCached(boolean isCached) {
        synchronized (this) {
            if(isCached) {
                cacheRefCount++;
            }
            else {
                cacheRefCount--;
            }
        }
        checkState();
    }

    private synchronized void checkState() {
        if(cacheRefCount <= 0 && displatRefCount <= 0) {
            /**
             * remover a referencia da imagem da memoria
             * */
            getBitmap().recycle();
        }
    }
}
