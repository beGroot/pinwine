// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BitmapDecoder.java

package net.tsz.afinal.bitmap.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileDescriptor;

public class BitmapDecoder
{

    private BitmapDecoder()
    {
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try
        {
            return BitmapFactory.decodeResource(res, resId, options);
        }
        catch(OutOfMemoryError e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try
        {
            return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        }
        catch(OutOfMemoryError e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte data[], int offset, int length, int reqWidth, int reqHeight)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }

    private static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth)
        {
            if(width > height)
                inSampleSize = Math.round((float)height / (float)reqHeight);
            else
                inSampleSize = Math.round((float)width / (float)reqWidth);
            float totalPixels = width * height;
            for(float totalReqPixelsCap = reqWidth * reqHeight * 2; totalPixels / (float)(inSampleSize * inSampleSize) > totalReqPixelsCap; inSampleSize++);
        }
        return inSampleSize;
    }
}
