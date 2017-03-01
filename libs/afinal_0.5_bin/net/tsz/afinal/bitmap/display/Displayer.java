// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Displayer.java

package net.tsz.afinal.bitmap.display;

import android.graphics.Bitmap;
import android.view.View;
import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;

public interface Displayer
{

    public abstract void loadCompletedisplay(View view, Bitmap bitmap, BitmapDisplayConfig bitmapdisplayconfig);

    public abstract void loadFailDisplay(View view, Bitmap bitmap);
}
