package components.com.project.efficientlybitmaps.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import components.com.project.efficientlybitmaps.utils.BitmapUtils;

/**
 * Created by r028367 on 17/07/2017.
 * https://stuff.mit.edu/afs/sipb/project/android/docs/training/displaying-bitmaps/process-bitmap.html
 * https://github.com/kesenhoo/BitmapFun/tree/master/BitmapFun/src/main/java/com/example/android/bitmapfun
 */

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference = null;
    private int data = 0;

    /**
     * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
     */
    public BitmapWorkerTask(ImageView imageView) {
        super();
        // usando WeakReference para garantir que o garbage collector recolha a referencia de imageview quando chegar a hora
        imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Bitmap doInBackground(Integer[] params) {
        final Bitmap bitmap = null;
        return bitmap;
    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param o The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(Bitmap o) {
        super.onPostExecute(o);
    }



}
