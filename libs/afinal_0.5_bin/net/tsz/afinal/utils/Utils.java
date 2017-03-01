// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Utils.java

package net.tsz.afinal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;

public class Utils
{

    public Utils()
    {
    }

    public static File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath = "mounted".equals(Environment.getExternalStorageState()) ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
        return new File((new StringBuilder(String.valueOf(cachePath))).append(File.separator).append(uniqueName).toString());
    }

    public static int getBitmapSize(Bitmap bitmap)
    {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static File getExternalCacheDir(Context context)
    {
        String cacheDir = (new StringBuilder("/Android/data/")).append(context.getPackageName()).append("/cache/").toString();
        return new File((new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath()))).append(cacheDir).toString());
    }

    public static long getUsableSpace(File path)
    {
        try
        {
            StatFs stats = new StatFs(path.getPath());
            return (long)stats.getBlockSize() * (long)stats.getAvailableBlocks();
        }
        catch(Exception e)
        {
            Log.e("BitmapCommonUtils", "\u83B7\u53D6 sdcard \u7F13\u5B58\u5927\u5C0F \u51FA\u9519\uFF0C\u8BF7\u67E5\u770BAndroidManifest.xml \u662F\u5426\u6DFB\u52A0\u4E86sdcard\u7684\u8BBF\u95EE\u6743\u9650");
            e.printStackTrace();
            return -1L;
        }
    }

    public static byte[] getBytes(String in)
    {
        byte result[] = new byte[in.length() * 2];
        int output = 0;
        char ac[];
        int j = (ac = in.toCharArray()).length;
        for(int i = 0; i < j; i++)
        {
            char ch = ac[i];
            result[output++] = (byte)(ch & 0xff);
            result[output++] = (byte)(ch >> 8);
        }

        return result;
    }

    public static boolean isSameKey(byte key[], byte buffer[])
    {
        int n = key.length;
        if(buffer.length < n)
            return false;
        for(int i = 0; i < n; i++)
            if(key[i] != buffer[i])
                return false;

        return true;
    }

    public static byte[] copyOfRange(byte original[], int from, int to)
    {
        int newLength = to - from;
        if(newLength < 0)
        {
            throw new IllegalArgumentException((new StringBuilder(String.valueOf(from))).append(" > ").append(to).toString());
        } else
        {
            byte copy[] = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }
    }

    public static byte[] makeKey(String httpUrl)
    {
        return getBytes(httpUrl);
    }

    public static final long crc64Long(String in)
    {
        if(in == null || in.length() == 0)
            return 0L;
        else
            return crc64Long(getBytes(in));
    }

    public static final long crc64Long(byte buffer[])
    {
        long crc = -1L;
        int k = 0;
        for(int n = buffer.length; k < n; k++)
            crc = sCrcTable[((int)crc ^ buffer[k]) & 0xff] ^ crc >> 8;

        return crc;
    }

    private static final String TAG = "BitmapCommonUtils";
    private static final long POLY64REV = 0x95ac9329ac4bc9b5L;
    private static final long INITIALCRC = -1L;
    private static long sCrcTable[];

    static 
    {
        sCrcTable = new long[256];
        for(int i = 0; i < 256; i++)
        {
            long part = i;
            for(int j = 0; j < 8; j++)
            {
                long x = ((int)part & 1) == 0 ? 0L : 0x95ac9329ac4bc9b5L;
                part = part >> 1 ^ x;
            }

            sCrcTable[i] = part;
        }

    }
}
