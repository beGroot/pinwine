// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseMemoryCacheImpl.java

package net.tsz.afinal.bitmap.core;

import android.graphics.Bitmap;
import net.tsz.afinal.utils.Utils;

// Referenced classes of package net.tsz.afinal.bitmap.core:
//            IMemoryCache, LruMemoryCache

public class BaseMemoryCacheImpl
    implements IMemoryCache
{

    public BaseMemoryCacheImpl(int size)
    {
        mMemoryCache = new LruMemoryCache(size) {

            protected int sizeOf(String key, Bitmap bitmap)
            {
                return Utils.getBitmapSize(bitmap);
            }

            protected volatile int sizeOf(Object obj, Object obj1)
            {
                return sizeOf((String)obj, (Bitmap)obj1);
            }

            final BaseMemoryCacheImpl this$0;

            
            {
                this$0 = BaseMemoryCacheImpl.this;
                super($anonymous0);
            }
        }
;
    }

    public void put(String key, Bitmap bitmap)
    {
        mMemoryCache.put(key, bitmap);
    }

    public Bitmap get(String key)
    {
        return (Bitmap)mMemoryCache.get(key);
    }

    public void evictAll()
    {
        mMemoryCache.evictAll();
    }

    public void remove(String key)
    {
        mMemoryCache.remove(key);
    }

    private final LruMemoryCache mMemoryCache;
}
