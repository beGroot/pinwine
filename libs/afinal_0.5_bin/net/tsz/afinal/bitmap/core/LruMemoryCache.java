// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LruMemoryCache.java

package net.tsz.afinal.bitmap.core;

import java.util.*;

public class LruMemoryCache
{

    public LruMemoryCache(int maxSize)
    {
        if(maxSize <= 0)
        {
            throw new IllegalArgumentException("maxSize <= 0");
        } else
        {
            this.maxSize = maxSize;
            map = new LinkedHashMap(0, 0.75F, true);
            return;
        }
    }

    public final Object get(Object key)
    {
        if(key == null)
            throw new NullPointerException("key == null");
        LruMemoryCache lrumemorycache = this;
        JVM INSTR monitorenter ;
        Object mapValue = map.get(key);
        if(mapValue != null)
        {
            hitCount++;
            return mapValue;
        }
        missCount++;
        lrumemorycache;
        JVM INSTR monitorexit ;
          goto _L1
        lrumemorycache;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        Object createdValue = create(key);
        if(createdValue == null)
            return null;
        Object mapValue;
        synchronized(this)
        {
            createCount++;
            mapValue = map.put(key, createdValue);
            if(mapValue != null)
                map.put(key, mapValue);
            else
                size += safeSizeOf(key, createdValue);
        }
        if(mapValue != null)
        {
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else
        {
            trimToSize(maxSize);
            return createdValue;
        }
    }

    public final Object put(Object key, Object value)
    {
        if(key == null || value == null)
            throw new NullPointerException("key == null || value == null");
        Object previous;
        synchronized(this)
        {
            putCount++;
            size += safeSizeOf(key, value);
            previous = map.put(key, value);
            if(previous != null)
                size -= safeSizeOf(key, previous);
        }
        if(previous != null)
            entryRemoved(false, key, previous, value);
        trimToSize(maxSize);
        return previous;
    }

    private void trimToSize(int maxSize)
    {
_L2:
        Object key;
        Object value;
label0:
        {
            java.util.Map.Entry toEvict;
            synchronized(this)
            {
                if(size < 0 || map.isEmpty() && size != 0)
                    throw new IllegalStateException((new StringBuilder(String.valueOf(getClass().getName()))).append(".sizeOf() is reporting inconsistent results!").toString());
                if(size > maxSize && !map.isEmpty())
                    break label0;
            }
            break; /* Loop/switch isn't completed */
        }
        toEvict = (java.util.Map.Entry)map.entrySet().iterator().next();
        key = toEvict.getKey();
        value = toEvict.getValue();
        map.remove(key);
        size -= safeSizeOf(key, value);
        evictionCount++;
        lrumemorycache;
        JVM INSTR monitorexit ;
        entryRemoved(true, key, value, null);
        if(true) goto _L2; else goto _L1
_L1:
    }

    public final Object remove(Object key)
    {
        if(key == null)
            throw new NullPointerException("key == null");
        Object previous;
        synchronized(this)
        {
            previous = map.remove(key);
            if(previous != null)
                size -= safeSizeOf(key, previous);
        }
        if(previous != null)
            entryRemoved(false, key, previous, null);
        return previous;
    }

    protected void entryRemoved(boolean flag, Object obj, Object obj1, Object obj2)
    {
    }

    protected Object create(Object key)
    {
        return null;
    }

    private int safeSizeOf(Object key, Object value)
    {
        int result = sizeOf(key, value);
        if(result < 0)
            throw new IllegalStateException((new StringBuilder("Negative size: ")).append(key).append("=").append(value).toString());
        else
            return result;
    }

    protected int sizeOf(Object key, Object value)
    {
        return 1;
    }

    public final void evictAll()
    {
        trimToSize(-1);
    }

    public final synchronized int size()
    {
        return size;
    }

    public final synchronized int maxSize()
    {
        return maxSize;
    }

    public final synchronized int hitCount()
    {
        return hitCount;
    }

    public final synchronized int missCount()
    {
        return missCount;
    }

    public final synchronized int createCount()
    {
        return createCount;
    }

    public final synchronized int putCount()
    {
        return putCount;
    }

    public final synchronized int evictionCount()
    {
        return evictionCount;
    }

    public final synchronized Map snapshot()
    {
        return new LinkedHashMap(map);
    }

    public final synchronized String toString()
    {
        int accesses = hitCount + missCount;
        int hitPercent = accesses == 0 ? 0 : (100 * hitCount) / accesses;
        return String.format("LruMemoryCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[] {
            Integer.valueOf(maxSize), Integer.valueOf(hitCount), Integer.valueOf(missCount), Integer.valueOf(hitPercent)
        });
    }

    private final LinkedHashMap map;
    private int size;
    private int maxSize;
    private int putCount;
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private int missCount;
}
