// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SoftMemoryCacheImpl.java

package net.tsz.afinal.bitmap.core;

import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import net.tsz.afinal.utils.Utils;

// Referenced classes of package net.tsz.afinal.bitmap.core:
//            IMemoryCache, LruMemoryCache

public class SoftMemoryCacheImpl
    implements IMemoryCache
{

    public SoftMemoryCacheImpl(int size)
    {
        mMemoryCache = new LruMemoryCache(size) {

            protected int sizeOf(String key, SoftReference sBitmap)
            {
                Bitmap bitmap = sBitmap != null ? (Bitmap)sBitmap.get() : null;
                if(bitmap == null)
                    return 1;
                else
                    return Utils.getBitmapSize(bitmap);
            }

            protected volatile int sizeOf(Object obj, Object obj1)
            {
                return sizeOf((String)obj, (SoftReference)obj1);
            }

            final SoftMemoryCacheImpl this$0;

            
            {
                this$0 = SoftMemoryCacheImpl.this;
                super($anonymous0);
            }
        }
;
    }

    public void put(String key, Bitmap bitmap)
    {
        mMemoryCache.put(key, new SoftReference(bitmap));
    }

    public Bitmap get(String key)
    {
        SoftReference memBitmap = (SoftReference)mMemoryCache.get(key);
        if(memBitmap != null)
            return (Bitmap)memBitmap.get();
        else
            return null;
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
