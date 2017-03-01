// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IMemoryCache.java

package net.tsz.afinal.bitmap.core;

import android.graphics.Bitmap;

public interface IMemoryCache
{

    public abstract void put(String s, Bitmap bitmap);

    public abstract Bitmap get(String s);

    public abstract void evictAll();

    public abstract void remove(String s);
}
