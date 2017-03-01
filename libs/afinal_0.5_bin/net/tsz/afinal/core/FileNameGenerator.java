// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileNameGenerator.java

package net.tsz.afinal.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileNameGenerator
{

    public FileNameGenerator()
    {
    }

    public static String generator(String key)
    {
        String cacheKey;
        try
        {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte bytes[])
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if(hex.length() == 1)
                sb.append('0');
            sb.append(hex);
        }

        return sb.toString();
    }
}
