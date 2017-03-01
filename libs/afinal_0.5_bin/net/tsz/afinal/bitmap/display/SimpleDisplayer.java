// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SimpleDisplayer.java

package net.tsz.afinal.bitmap.display;

import android.graphics.Bitmap;
import android.graphics.drawable.*;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;

// Referenced classes of package net.tsz.afinal.bitmap.display:
//            Displayer

public class SimpleDisplayer
    implements Displayer
{

    public SimpleDisplayer()
    {
    }

    public void loadCompletedisplay(View imageView, Bitmap bitmap, BitmapDisplayConfig config)
    {
        switch(config.getAnimationType())
        {
        case 1: // '\001'
            fadeInDisplay(imageView, bitmap);
            break;

        case 0: // '\0'
            animationDisplay(imageView, bitmap, config.getAnimation());
            break;
        }
    }

    public void loadFailDisplay(View imageView, Bitmap bitmap)
    {
        if(imageView instanceof ImageView)
            ((ImageView)imageView).setImageBitmap(bitmap);
        else
            imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    private void fadeInDisplay(View imageView, Bitmap bitmap)
    {
        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
            new ColorDrawable(0x106000d), new BitmapDrawable(imageView.getResources(), bitmap)
        });
        if(imageView instanceof ImageView)
            ((ImageView)imageView).setImageDrawable(td);
        else
            imageView.setBackgroundDrawable(td);
        td.startTransition(300);
    }

    private void animationDisplay(View imageView, Bitmap bitmap, Animation animation)
    {
        animation.setStartTime(AnimationUtils.currentAnimationTimeMillis());
        if(imageView instanceof ImageView)
            ((ImageView)imageView).setImageBitmap(bitmap);
        else
            imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        imageView.startAnimation(animation);
    }
}
