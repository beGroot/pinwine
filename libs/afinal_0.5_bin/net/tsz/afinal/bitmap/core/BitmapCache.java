// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BitmapCache.java

package net.tsz.afinal.bitmap.core;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.tsz.afinal.utils.Utils;

// Referenced classes of package net.tsz.afinal.bitmap.core:
//            SoftMemoryCacheImpl, BaseMemoryCacheImpl, DiskCache, IMemoryCache, 
//            BytesBufferPool

public class BitmapCache
{
    public static class ImageCacheParams
    {

        public void setMemCacheSizePercent(Context context, float percent)
        {
            if(percent < 0.05F || percent > 0.8F)
            {
                throw new IllegalArgumentException("setMemCacheSizePercent - percent must be between 0.05 and 0.8 (inclusive)");
            } else
            {
                memCacheSize = Math.round(percent * (float)getMemoryClass(context) * 1024F * 1024F);
                return;
            }
        }

        public void setMemCacheSize(int memCacheSize)
        {
            this.memCacheSize = memCacheSize;
        }

        public void setDiskCacheSize(int diskCacheSize)
        {
            this.diskCacheSize = diskCacheSize;
        }

        private static int getMemoryClass(Context context)
        {
            return ((ActivityManager)context.getSystemService("activity")).getMemoryClass();
        }

        public void setDiskCacheCount(int diskCacheCount)
        {
            this.diskCacheCount = diskCacheCount;
        }

        public void setRecycleImmediately(boolean recycleImmediately)
        {
            this.recycleImmediately = recycleImmediately;
        }

        public int memCacheSize;
        public int diskCacheSize;
        public int diskCacheCount;
        public File diskCacheDir;
        public boolean memoryCacheEnabled;
        public boolean diskCacheEnabled;
        public boolean recycleImmediately;

        public ImageCacheParams(File diskCacheDir)
        {
            memCacheSize = 0x800000;
            diskCacheSize = 0x3200000;
            diskCacheCount = 10000;
            memoryCacheEnabled = true;
            diskCacheEnabled = true;
            recycleImmediately = true;
            this.diskCacheDir = diskCacheDir;
        }

        public ImageCacheParams(String diskCacheDir)
        {
            memCacheSize = 0x800000;
            diskCacheSize = 0x3200000;
            diskCacheCount = 10000;
            memoryCacheEnabled = true;
            diskCacheEnabled = true;
            recycleImmediately = true;
            this.diskCacheDir = new File(diskCacheDir);
        }
    }


    public BitmapCache(ImageCacheParams cacheParams)
    {
        init(cacheParams);
    }

    private void init(ImageCacheParams cacheParams)
    {
        mCacheParams = cacheParams;
        if(mCacheParams.memoryCacheEnabled)
            if(mCacheParams.recycleImmediately)
                mMemoryCache = new SoftMemoryCacheImpl(mCacheParams.memCacheSize);
            else
                mMemoryCache = new BaseMemoryCacheImpl(mCacheParams.memCacheSize);
        if(cacheParams.diskCacheEnabled)
            try
            {
                String path = mCacheParams.diskCacheDir.getAbsolutePath();
                mDiskCache = new DiskCache(path, mCacheParams.diskCacheCount, mCacheParams.diskCacheSize, false);
            }
            catch(IOException ioexception) { }
    }

    public void addToMemoryCache(String url, Bitmap bitmap)
    {
        if(url == null || bitmap == null)
        {
            return;
        } else
        {
            mMemoryCache.put(url, bitmap);
            return;
        }
    }

    public void addToDiskCache(String url, byte data[])
    {
        if(mDiskCache == null || url == null || data == null)
            return;
        byte key[] = Utils.makeKey(url);
        long cacheKey = Utils.crc64Long(key);
        ByteBuffer buffer = ByteBuffer.allocate(key.length + data.length);
        buffer.put(key);
        buffer.put(data);
        synchronized(mDiskCache)
        {
            try
            {
                mDiskCache.insert(cacheKey, buffer.array());
            }
            catch(IOException ioexception) { }
        }
    }

    public boolean getImageData(String url, BytesBufferPool.BytesBuffer buffer)
    {
        byte key[];
        long cacheKey;
        if(mDiskCache == null)
            return false;
        key = Utils.makeKey(url);
        cacheKey = Utils.crc64Long(key);
        DiskCache.LookupRequest request;
        request = new DiskCache.LookupRequest();
        request.key = cacheKey;
        request.buffer = buffer.data;
        synchronized(mDiskCache)
        {
            if(mDiskCache.lookup(request))
                break MISSING_BLOCK_LABEL_70;
        }
        return false;
        diskcache;
        JVM INSTR monitorexit ;
        if(!Utils.isSameKey(key, request.buffer))
            break MISSING_BLOCK_LABEL_125;
        buffer.data = request.buffer;
        buffer.offset = key.length;
        buffer.length = request.length - buffer.offset;
        return true;
        IOException ioexception;
        ioexception;
        return false;
    }

    public Bitmap getBitmapFromMemoryCache(String data)
    {
        if(mMemoryCache != null)
            return mMemoryCache.get(data);
        else
            return null;
    }

    public void clearCache()
    {
        clearMemoryCache();
        clearDiskCache();
    }

    public void clearDiskCache()
    {
        if(mDiskCache != null)
            mDiskCache.delete();
    }

    public void clearMemoryCache()
    {
        if(mMemoryCache != null)
            mMemoryCache.evictAll();
    }

    public void clearCache(String key)
    {
        clearMemoryCache(key);
        clearDiskCache(key);
    }

    public void clearDiskCache(String url)
    {
        addToDiskCache(url, new byte[0]);
    }

    public void clearMemoryCache(String key)
    {
        if(mMemoryCache != null)
            mMemoryCache.remove(key);
    }

    public void close()
    {
        if(mDiskCache != null)
            mDiskCache.close();
    }

    private static final int DEFAULT_MEM_CACHE_SIZE = 0x800000;
    private static final int DEFAULT_DISK_CACHE_SIZE = 0x3200000;
    private static final int DEFAULT_DISK_CACHE_COUNT = 10000;
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    private DiskCache mDiskCache;
    private IMemoryCache mMemoryCache;
    private ImageCacheParams mCacheParams;
}
