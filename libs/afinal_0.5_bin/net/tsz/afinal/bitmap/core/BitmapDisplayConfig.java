// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BitmapDisplayConfig.java

package net.tsz.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.view.animation.Animation;

public class BitmapDisplayConfig
{
    public class AnimationType
    {

        public static final int userDefined = 0;
        public static final int fadeIn = 1;
        final BitmapDisplayConfig this$0;

        public AnimationType()
        {
            this$0 = BitmapDisplayConfig.this;
            super();
        }
    }


    public BitmapDisplayConfig()
    {
    }

    public int getBitmapWidth()
    {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth)
    {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight()
    {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight)
    {
        this.bitmapHeight = bitmapHeight;
    }

    public Animation getAnimation()
    {
        return animation;
    }

    public void setAnimation(Animation animation)
    {
        this.animation = animation;
    }

    public int getAnimationType()
    {
        return animationType;
    }

    public void setAnimationType(int animationType)
    {
        this.animationType = animationType;
    }

    public Bitmap getLoadingBitmap()
    {
        return loadingBitmap;
    }

    public void setLoadingBitmap(Bitmap loadingBitmap)
    {
        this.loadingBitmap = loadingBitmap;
    }

    public Bitmap getLoadfailBitmap()
    {
        return loadfailBitmap;
    }

    public void setLoadfailBitmap(Bitmap loadfailBitmap)
    {
        this.loadfailBitmap = loadfailBitmap;
    }

    private int bitmapWidth;
    private int bitmapHeight;
    private Animation animation;
    private int animationType;
    private Bitmap loadingBitmap;
    private Bitmap loadfailBitmap;
}
