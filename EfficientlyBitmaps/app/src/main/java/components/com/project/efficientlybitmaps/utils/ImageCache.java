package components.com.project.efficientlybitmaps.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.util.LruCache;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by r028367 on 18/07/2017.
 * Exemplo do google developer android
 * https://developer.android.com/topic/performance/graphics/load-bitmap.html
 *
 * Modificando o codigo aos poucos para tentar entender como funcina
 *
 */

public class ImageCache {

    private static final String TAG = ImageCache.class.getName();
    // Default memory cache size in kilobytes
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB

    // Default disk cache size in bytes
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    // Compression settings when writing images to disk cache
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;
    private static final int DISK_CACHE_INDEX = 0;

    // Constants to easily toggle various caches
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false;

    private DiskLruCache diskLruCache;
    private LruCache<String, BitmapDrawable> memCache;
    private ImageCacheParams imageCacheParams;
    private final Object diskCacheLock = new Object();
    private boolean diskCacheStarting = true;

    private Set<SoftReference<Bitmap>> reusableBitmaps;


    private ImageCache(ImageCacheParams imageCacheParams) {
        init(imageCacheParams);
    }

    private void init(ImageCacheParams imageCacheParams) {
        this.imageCacheParams = imageCacheParams;
        if(imageCacheParams.memoryCacheEnabled) {
            if(UtilsOsVersion.supportHoneycomb()) {
                reusableBitmaps = Collections
                        .synchronizedSet(new HashSet<SoftReference<Bitmap>>());
            }
            int maxSizeMemCache = imageCacheParams.memCacheSize;
            memCache = new LruCache<String, BitmapDrawable>(maxSizeMemCache) {
                @Override
                protected void entryRemoved(boolean evicted, String key
                        , BitmapDrawable oldValue, BitmapDrawable newValue) {
                    //super.entryRemoved(evicted, key, oldValue, newValue);
                    if(RecyclingBitmapDrawable.class.isInstance(oldValue)) {
                        ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
                    }
                    else {
                        if(UtilsOsVersion.supportHoneycomb()) {
                            reusableBitmaps.add(new SoftReference<Bitmap>(oldValue.getBitmap()));
                        }
                    }
                }

                @Override
                protected int sizeOf(String key, BitmapDrawable value) {
                    //return super.sizeOf(key, value);
                    int bitmapSize = getBitmapSize(value) / 1024;
                    return bitmapSize == 0 ? 1 : bitmapSize;
                }
            };
        }
        
        if(imageCacheParams.initDiskCacheOnCreate) {
            initDiskCache();
        }
    }

    public void initDiskCache() {
        synchronized (diskCacheLock) {
            if(diskLruCache == null || diskLruCache.isClosed()) {

            }
        }
    }


    /**
     *
     * Pegar um diretorio para usar de cache (memoria externa se disponivel, interna caso contrario)
     *
     * */

    public static File getDiskCacheDir(Context context, String diskCacheDirName) {
        String path = "";
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || ! isExternalStorageRemovable() ) {
            path = getExternalCacheDir(context).getPath();
        }
        else {
            path = context.getCacheDir().getPath();
        }
        path = String.format("%s%s%s", path, File.separator, diskCacheDirName);
        return new File(path);
    }


    public static File getExternalCacheDir(Context context) {
        File file = context.getExternalCacheDir();
        return file;
    }

    public static boolean isExternalStorageRemovable() {
        return Environment.isExternalStorageRemovable();
    }

    public static int getBitmapSize(BitmapDrawable bitmapDrawable) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if(UtilsOsVersion.supportKitKat()) {
            return bitmap.getAllocationByteCount();
        }

        else if(UtilsOsVersion.supportHoneycombMR1()) {
            return bitmap.getByteCount();
        }

        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static class ImageCacheParams {
        public int memCacheSize                     = DEFAULT_MEM_CACHE_SIZE;
        public int diskCacheSize                    = DEFAULT_DISK_CACHE_SIZE;
        public File diskCacheDir;
        public Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
        public int compressQuality                  = DEFAULT_COMPRESS_QUALITY;
        public boolean memoryCacheEnabled           = DEFAULT_MEM_CACHE_ENABLED;
        public boolean diskCacheEnabled             = DEFAULT_DISK_CACHE_ENABLED;
        public boolean initDiskCacheOnCreate        = DEFAULT_INIT_DISK_CACHE_ON_CREATE;


        public ImageCacheParams(Context context, String diskCacheDirName) {
            diskCacheDir = getDiskCacheDir(context, diskCacheDirName);
        }

        public void setMemCacheSizePercent(float ratio) throws Exception {
            if(ratio < .01f || ratio>.8f) {
                throw new Exception("Valor percentual de memoria deve estar entre 0,01 e 0.8 exclusivo");
            }
            long mem = Runtime.getRuntime().maxMemory();
            memCacheSize = Math.round(ratio * mem / 1024);
        }

    }

}


