// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FinalBitmap.java

package net.tsz.afinal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.*;
import net.tsz.afinal.bitmap.core.BitmapCache;
import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;
import net.tsz.afinal.bitmap.core.BitmapProcess;
import net.tsz.afinal.bitmap.display.Displayer;
import net.tsz.afinal.bitmap.display.SimpleDisplayer;
import net.tsz.afinal.bitmap.download.Downloader;
import net.tsz.afinal.bitmap.download.SimpleDownloader;
import net.tsz.afinal.core.AsyncTask;
import net.tsz.afinal.utils.Utils;

public class FinalBitmap
{
    private static class AsyncDrawable extends BitmapDrawable
    {

        public BitmapLoadAndDisplayTask getBitmapWorkerTask()
        {
            return (BitmapLoadAndDisplayTask)bitmapWorkerTaskReference.get();
        }

        private final WeakReference bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapLoadAndDisplayTask bitmapWorkerTask)
        {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference(bitmapWorkerTask);
        }
    }

    private class BitmapLoadAndDisplayTask extends AsyncTask
    {

        protected transient Bitmap doInBackground(Object params[])
        {
            data = params[0];
            String dataString = String.valueOf(data);
            Bitmap bitmap = null;
            synchronized(mPauseWorkLock)
            {
                while(mPauseWork && !isCancelled()) 
                    try
                    {
                        mPauseWorkLock.wait();
                    }
                    catch(InterruptedException interruptedexception) { }
            }
            if(bitmap == null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly)
                bitmap = processBitmap(dataString, displayConfig);
            if(bitmap != null)
                mImageCache.addToMemoryCache(dataString, bitmap);
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap)
        {
            if(isCancelled() || mExitTasksEarly)
                bitmap = null;
            View imageView = getAttachedImageView();
            if(bitmap != null && imageView != null)
                mConfig.displayer.loadCompletedisplay(imageView, bitmap, displayConfig);
            else
            if(bitmap == null && imageView != null)
                mConfig.displayer.loadFailDisplay(imageView, displayConfig.getLoadfailBitmap());
        }

        protected void onCancelled(Bitmap bitmap)
        {
            super.onCancelled(bitmap);
            synchronized(mPauseWorkLock)
            {
                mPauseWorkLock.notifyAll();
            }
        }

        private View getAttachedImageView()
        {
            View imageView = (View)imageViewReference.get();
            BitmapLoadAndDisplayTask bitmapWorkerTask = FinalBitmap.getBitmapTaskFromImageView(imageView);
            if(this == bitmapWorkerTask)
                return imageView;
            else
                return null;
        }

        protected volatile void onPostExecute(Object obj)
        {
            onPostExecute((Bitmap)obj);
        }

        protected volatile void onCancelled(Object obj)
        {
            onCancelled((Bitmap)obj);
        }

        protected volatile transient Object doInBackground(Object aobj[])
        {
            return doInBackground((Object[])aobj);
        }

        private Object data;
        private final WeakReference imageViewReference;
        private final BitmapDisplayConfig displayConfig;
        final FinalBitmap this$0;


        public BitmapLoadAndDisplayTask(View imageView, BitmapDisplayConfig config)
        {
            this$0 = FinalBitmap.this;
            super();
            imageViewReference = new WeakReference(imageView);
            displayConfig = config;
        }
    }

    private class CacheExecutecTask extends AsyncTask
    {

        protected transient Void doInBackground(Object params[])
        {
            switch(((Integer)params[0]).intValue())
            {
            case 1: // '\001'
                clearCacheInternalInBackgroud();
                break;

            case 2: // '\002'
                closeCacheInternalInBackgroud();
                break;

            case 3: // '\003'
                clearDiskCacheInBackgroud();
                break;

            case 4: // '\004'
                clearCacheInBackgroud(String.valueOf(params[1]));
                break;

            case 5: // '\005'
                clearDiskCacheInBackgroud(String.valueOf(params[1]));
                break;
            }
            return null;
        }

        protected volatile transient Object doInBackground(Object aobj[])
        {
            return doInBackground((Object[])aobj);
        }

        public static final int MESSAGE_CLEAR = 1;
        public static final int MESSAGE_CLOSE = 2;
        public static final int MESSAGE_CLEAR_DISK = 3;
        public static final int MESSAGE_CLEAR_KEY = 4;
        public static final int MESSAGE_CLEAR_KEY_IN_DISK = 5;
        final FinalBitmap this$0;

        private CacheExecutecTask()
        {
            this$0 = FinalBitmap.this;
            super();
        }

        CacheExecutecTask(CacheExecutecTask cacheexecutectask)
        {
            this();
        }
    }

    private class FinalBitmapConfig
    {

        public String cachePath;
        public Displayer displayer;
        public Downloader downloader;
        public BitmapDisplayConfig defaultDisplayConfig;
        public float memCacheSizePercent;
        public int memCacheSize;
        public int diskCacheSize;
        public int poolSize;
        public boolean recycleImmediately;
        final FinalBitmap this$0;

        public FinalBitmapConfig(Context context)
        {
            this$0 = FinalBitmap.this;
            super();
            poolSize = 3;
            recycleImmediately = true;
            defaultDisplayConfig = new BitmapDisplayConfig();
            defaultDisplayConfig.setAnimation(null);
            defaultDisplayConfig.setAnimationType(1);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int defaultWidth = (int)Math.floor(displayMetrics.widthPixels / 2);
            defaultDisplayConfig.setBitmapHeight(defaultWidth);
            defaultDisplayConfig.setBitmapWidth(defaultWidth);
        }
    }


    private FinalBitmap(Context context)
    {
        mExitTasksEarly = false;
        mPauseWork = false;
        mInit = false;
        configMap = new HashMap();
        mContext = context;
        mConfig = new FinalBitmapConfig(context);
        configDiskCachePath(Utils.getDiskCacheDir(context, "afinalCache").getAbsolutePath());
        configDisplayer(new SimpleDisplayer());
        configDownlader(new SimpleDownloader());
    }

    public static synchronized FinalBitmap create(Context ctx)
    {
        if(mFinalBitmap == null)
            mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
        return mFinalBitmap;
    }

    public FinalBitmap configLoadingImage(Bitmap bitmap)
    {
        mConfig.defaultDisplayConfig.setLoadingBitmap(bitmap);
        return this;
    }

    public FinalBitmap configLoadingImage(int resId)
    {
        mConfig.defaultDisplayConfig.setLoadingBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
        return this;
    }

    public FinalBitmap configLoadfailImage(Bitmap bitmap)
    {
        mConfig.defaultDisplayConfig.setLoadfailBitmap(bitmap);
        return this;
    }

    public FinalBitmap configLoadfailImage(int resId)
    {
        mConfig.defaultDisplayConfig.setLoadfailBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
        return this;
    }

    public FinalBitmap configBitmapMaxHeight(int bitmapHeight)
    {
        mConfig.defaultDisplayConfig.setBitmapHeight(bitmapHeight);
        return this;
    }

    public FinalBitmap configBitmapMaxWidth(int bitmapWidth)
    {
        mConfig.defaultDisplayConfig.setBitmapWidth(bitmapWidth);
        return this;
    }

    public FinalBitmap configDownlader(Downloader downlader)
    {
        mConfig.downloader = downlader;
        return this;
    }

    public FinalBitmap configDisplayer(Displayer displayer)
    {
        mConfig.displayer = displayer;
        return this;
    }

    public FinalBitmap configDiskCachePath(String strPath)
    {
        if(!TextUtils.isEmpty(strPath))
            mConfig.cachePath = strPath;
        return this;
    }

    public FinalBitmap configMemoryCacheSize(int size)
    {
        mConfig.memCacheSize = size;
        return this;
    }

    public FinalBitmap configMemoryCachePercent(float percent)
    {
        mConfig.memCacheSizePercent = percent;
        return this;
    }

    public FinalBitmap configDiskCacheSize(int size)
    {
        mConfig.diskCacheSize = size;
        return this;
    }

    public FinalBitmap configBitmapLoadThreadSize(int size)
    {
        if(size >= 1)
            mConfig.poolSize = size;
        return this;
    }

    public FinalBitmap configRecycleImmediately(boolean recycleImmediately)
    {
        mConfig.recycleImmediately = recycleImmediately;
        return this;
    }

    private FinalBitmap init()
    {
        if(!mInit)
        {
            net.tsz.afinal.bitmap.core.BitmapCache.ImageCacheParams imageCacheParams = new net.tsz.afinal.bitmap.core.BitmapCache.ImageCacheParams(mConfig.cachePath);
            if((double)mConfig.memCacheSizePercent > 0.050000000000000003D && (double)mConfig.memCacheSizePercent < 0.80000000000000004D)
                imageCacheParams.setMemCacheSizePercent(mContext, mConfig.memCacheSizePercent);
            else
            if(mConfig.memCacheSize > 0x200000)
                imageCacheParams.setMemCacheSize(mConfig.memCacheSize);
            else
                imageCacheParams.setMemCacheSizePercent(mContext, 0.3F);
            if(mConfig.diskCacheSize > 0x500000)
                imageCacheParams.setDiskCacheSize(mConfig.diskCacheSize);
            imageCacheParams.setRecycleImmediately(mConfig.recycleImmediately);
            mImageCache = new BitmapCache(imageCacheParams);
            bitmapLoadAndDisplayExecutor = Executors.newFixedThreadPool(mConfig.poolSize, new ThreadFactory() {

                public Thread newThread(Runnable r)
                {
                    Thread t = new Thread(r);
                    t.setPriority(4);
                    return t;
                }

                final FinalBitmap this$0;

            
            {
                this$0 = FinalBitmap.this;
                super();
            }
            }
);
            mBitmapProcess = new BitmapProcess(mConfig.downloader, mImageCache);
            mInit = true;
        }
        return this;
    }

    public void display(View imageView, String uri)
    {
        doDisplay(imageView, uri, null);
    }

    public void display(View imageView, String uri, int imageWidth, int imageHeight)
    {
        BitmapDisplayConfig displayConfig = (BitmapDisplayConfig)configMap.get((new StringBuilder(String.valueOf(imageWidth))).append("_").append(imageHeight).toString());
        if(displayConfig == null)
        {
            displayConfig = getDisplayConfig();
            displayConfig.setBitmapHeight(imageHeight);
            displayConfig.setBitmapWidth(imageWidth);
            configMap.put((new StringBuilder(String.valueOf(imageWidth))).append("_").append(imageHeight).toString(), displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, Bitmap loadingBitmap)
    {
        BitmapDisplayConfig displayConfig = (BitmapDisplayConfig)configMap.get(String.valueOf(loadingBitmap));
        if(displayConfig == null)
        {
            displayConfig = getDisplayConfig();
            displayConfig.setLoadingBitmap(loadingBitmap);
            configMap.put(String.valueOf(loadingBitmap), displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, Bitmap loadingBitmap, Bitmap laodfailBitmap)
    {
        BitmapDisplayConfig displayConfig = (BitmapDisplayConfig)configMap.get((new StringBuilder(String.valueOf(String.valueOf(loadingBitmap)))).append("_").append(String.valueOf(laodfailBitmap)).toString());
        if(displayConfig == null)
        {
            displayConfig = getDisplayConfig();
            displayConfig.setLoadingBitmap(loadingBitmap);
            displayConfig.setLoadfailBitmap(laodfailBitmap);
            configMap.put((new StringBuilder(String.valueOf(String.valueOf(loadingBitmap)))).append("_").append(String.valueOf(laodfailBitmap)).toString(), displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, int imageWidth, int imageHeight, Bitmap loadingBitmap, Bitmap laodfailBitmap)
    {
        BitmapDisplayConfig displayConfig = (BitmapDisplayConfig)configMap.get((new StringBuilder(String.valueOf(imageWidth))).append("_").append(imageHeight).append("_").append(String.valueOf(loadingBitmap)).append("_").append(String.valueOf(laodfailBitmap)).toString());
        if(displayConfig == null)
        {
            displayConfig = getDisplayConfig();
            displayConfig.setBitmapHeight(imageHeight);
            displayConfig.setBitmapWidth(imageWidth);
            displayConfig.setLoadingBitmap(loadingBitmap);
            displayConfig.setLoadfailBitmap(laodfailBitmap);
            configMap.put((new StringBuilder(String.valueOf(imageWidth))).append("_").append(imageHeight).append("_").append(String.valueOf(loadingBitmap)).append("_").append(String.valueOf(laodfailBitmap)).toString(), displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, BitmapDisplayConfig config)
    {
        doDisplay(imageView, uri, config);
    }

    private void doDisplay(View imageView, String uri, BitmapDisplayConfig displayConfig)
    {
        if(!mInit)
            init();
        if(TextUtils.isEmpty(uri) || imageView == null)
            return;
        if(displayConfig == null)
            displayConfig = mConfig.defaultDisplayConfig;
        Bitmap bitmap = null;
        if(mImageCache != null)
            bitmap = mImageCache.getBitmapFromMemoryCache(uri);
        if(bitmap != null)
        {
            if(imageView instanceof ImageView)
                ((ImageView)imageView).setImageBitmap(bitmap);
            else
                imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        } else
        if(checkImageTask(uri, imageView))
        {
            BitmapLoadAndDisplayTask task = new BitmapLoadAndDisplayTask(imageView, displayConfig);
            AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), displayConfig.getLoadingBitmap(), task);
            if(imageView instanceof ImageView)
                ((ImageView)imageView).setImageDrawable(asyncDrawable);
            else
                imageView.setBackgroundDrawable(asyncDrawable);
            task.executeOnExecutor(bitmapLoadAndDisplayExecutor, new Object[] {
                uri
            });
        }
    }

    private BitmapDisplayConfig getDisplayConfig()
    {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setAnimation(mConfig.defaultDisplayConfig.getAnimation());
        config.setAnimationType(mConfig.defaultDisplayConfig.getAnimationType());
        config.setBitmapHeight(mConfig.defaultDisplayConfig.getBitmapHeight());
        config.setBitmapWidth(mConfig.defaultDisplayConfig.getBitmapWidth());
        config.setLoadfailBitmap(mConfig.defaultDisplayConfig.getLoadfailBitmap());
        config.setLoadingBitmap(mConfig.defaultDisplayConfig.getLoadingBitmap());
        return config;
    }

    private void clearCacheInternalInBackgroud()
    {
        if(mImageCache != null)
            mImageCache.clearCache();
    }

    private void clearDiskCacheInBackgroud()
    {
        if(mImageCache != null)
            mImageCache.clearDiskCache();
    }

    private void clearCacheInBackgroud(String key)
    {
        if(mImageCache != null)
            mImageCache.clearCache(key);
    }

    private void clearDiskCacheInBackgroud(String key)
    {
        if(mImageCache != null)
            mImageCache.clearDiskCache(key);
    }

    private void closeCacheInternalInBackgroud()
    {
        if(mImageCache != null)
        {
            mImageCache.close();
            mImageCache = null;
            mFinalBitmap = null;
        }
    }

    private Bitmap processBitmap(String uri, BitmapDisplayConfig config)
    {
        if(mBitmapProcess != null)
            return mBitmapProcess.getBitmap(uri, config);
        else
            return null;
    }

    public Bitmap getBitmapFromCache(String key)
    {
        Bitmap bitmap = getBitmapFromMemoryCache(key);
        if(bitmap == null)
            bitmap = getBitmapFromDiskCache(key);
        return bitmap;
    }

    public Bitmap getBitmapFromMemoryCache(String key)
    {
        return mImageCache.getBitmapFromMemoryCache(key);
    }

    public Bitmap getBitmapFromDiskCache(String key)
    {
        return getBitmapFromDiskCache(key, null);
    }

    public Bitmap getBitmapFromDiskCache(String key, BitmapDisplayConfig config)
    {
        return mBitmapProcess.getFromDisk(key, config);
    }

    public void setExitTasksEarly(boolean exitTasksEarly)
    {
        mExitTasksEarly = exitTasksEarly;
    }

    public void onResume()
    {
        setExitTasksEarly(false);
    }

    public void onPause()
    {
        setExitTasksEarly(true);
    }

    public void onDestroy()
    {
        closeCache();
    }

    public void clearCache()
    {
        (new CacheExecutecTask(null)).execute(new Object[] {
            Integer.valueOf(1)
        });
    }

    public void clearCache(String key)
    {
        (new CacheExecutecTask(null)).execute(new Object[] {
            Integer.valueOf(4), key
        });
    }

    public void clearMemoryCache()
    {
        if(mImageCache != null)
            mImageCache.clearMemoryCache();
    }

    public void clearMemoryCache(String key)
    {
        if(mImageCache != null)
            mImageCache.clearMemoryCache(key);
    }

    public void clearDiskCache()
    {
        (new CacheExecutecTask(null)).execute(new Object[] {
            Integer.valueOf(3)
        });
    }

    public void clearDiskCache(String key)
    {
        (new CacheExecutecTask(null)).execute(new Object[] {
            Integer.valueOf(5), key
        });
    }

    public void closeCache()
    {
        (new CacheExecutecTask(null)).execute(new Object[] {
            Integer.valueOf(2)
        });
    }

    public void exitTasksEarly(boolean exitTasksEarly)
    {
        mExitTasksEarly = exitTasksEarly;
        if(exitTasksEarly)
            pauseWork(false);
    }

    public void pauseWork(boolean pauseWork)
    {
        synchronized(mPauseWorkLock)
        {
            mPauseWork = pauseWork;
            if(!mPauseWork)
                mPauseWorkLock.notifyAll();
        }
    }

    private static BitmapLoadAndDisplayTask getBitmapTaskFromImageView(View imageView)
    {
        if(imageView != null)
        {
            Drawable drawable = null;
            if(imageView instanceof ImageView)
                drawable = ((ImageView)imageView).getDrawable();
            else
                drawable = imageView.getBackground();
            if(drawable instanceof AsyncDrawable)
            {
                AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static boolean checkImageTask(Object data, View imageView)
    {
        BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);
        if(bitmapWorkerTask != null)
        {
            Object bitmapData = bitmapWorkerTask.data;
            if(bitmapData == null || !bitmapData.equals(data))
                bitmapWorkerTask.cancel(true);
            else
                return false;
        }
        return true;
    }

    private FinalBitmapConfig mConfig;
    private BitmapCache mImageCache;
    private BitmapProcess mBitmapProcess;
    private boolean mExitTasksEarly;
    private boolean mPauseWork;
    private final Object mPauseWorkLock = new Object();
    private Context mContext;
    private boolean mInit;
    private ExecutorService bitmapLoadAndDisplayExecutor;
    private static FinalBitmap mFinalBitmap;
    private HashMap configMap;












}
