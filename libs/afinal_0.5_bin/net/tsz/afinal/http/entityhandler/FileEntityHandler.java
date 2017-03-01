// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileEntityHandler.java

package net.tsz.afinal.http.entityhandler;

import android.text.TextUtils;
import java.io.*;
import org.apache.http.HttpEntity;

// Referenced classes of package net.tsz.afinal.http.entityhandler:
//            EntityCallBack

public class FileEntityHandler
{

    public FileEntityHandler()
    {
        mStop = false;
    }

    public boolean isStop()
    {
        return mStop;
    }

    public void setStop(boolean stop)
    {
        mStop = stop;
    }

    public Object handleEntity(HttpEntity entity, EntityCallBack callback, String target, boolean isResume)
        throws IOException
    {
        if(TextUtils.isEmpty(target) || target.trim().length() == 0)
            return null;
        File targetFile = new File(target);
        if(!targetFile.exists())
            targetFile.createNewFile();
        if(mStop)
            return targetFile;
        long current = 0L;
        FileOutputStream os = null;
        if(isResume)
        {
            current = targetFile.length();
            os = new FileOutputStream(target, true);
        } else
        {
            os = new FileOutputStream(target);
        }
        if(mStop)
            return targetFile;
        InputStream input = entity.getContent();
        long count = entity.getContentLength() + current;
        if(current >= count || mStop)
            return targetFile;
        int readLen = 0;
        byte buffer[] = new byte[1024];
        while(!mStop && current < count && (readLen = input.read(buffer, 0, 1024)) > 0) 
        {
            os.write(buffer, 0, readLen);
            current += readLen;
            callback.callBack(count, current, false);
        }
        callback.callBack(count, current, true);
        if(mStop && current < count)
            throw new IOException("user stop download thread");
        else
            return targetFile;
    }

    private boolean mStop;
}
