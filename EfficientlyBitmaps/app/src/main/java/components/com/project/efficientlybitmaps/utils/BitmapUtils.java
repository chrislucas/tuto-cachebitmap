package components.com.project.efficientlybitmaps.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by r028367 on 17/07/2017.
 * https://developer.android.com/topic/performance/graphics/load-bitmap.html
 * https://stuff.mit.edu/afs/sipb/project/android/docs/training/displaying-bitmaps/load-bitmap.html#decodeSampledBitmapFromResource
 */

public class BitmapUtils {

    public static Bitmap decodeBitmapFromResource(Resources resource, int idResource
            , int reqWidth, int reqHeight) {

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resource, idResource, bitmapOptions);
        bitmapOptions.inSampleSize = calculateSampleSize(bitmapOptions, reqWidth, reqHeight);
        bitmapOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resource, idResource, bitmapOptions);
    }

    public static int calculateSampleSize(BitmapFactory.Options options, int reqWidth
            , int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int sampleSize = 1;
        if(h > reqHeight || w > reqWidth) {
            int hRatio = Math.round((float) h / (float) reqHeight);
            int wRatio = Math.round((float) w / (float) reqWidth);
            sampleSize = hRatio < wRatio ? hRatio : wRatio;
        }
        return sampleSize;
    }

}
