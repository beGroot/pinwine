// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringEntityHandler.java

package net.tsz.afinal.http.entityhandler;

import java.io.*;
import org.apache.http.HttpEntity;

// Referenced classes of package net.tsz.afinal.http.entityhandler:
//            EntityCallBack

public class StringEntityHandler
{

    public StringEntityHandler()
    {
    }

    public Object handleEntity(HttpEntity entity, EntityCallBack callback, String charset)
        throws IOException
    {
        if(entity == null)
            return null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        long count = entity.getContentLength();
        long curCount = 0L;
        int len = -1;
        InputStream is = entity.getContent();
        while((len = is.read(buffer)) != -1) 
        {
            outStream.write(buffer, 0, len);
            curCount += len;
            if(callback != null)
                callback.callBack(count, curCount, false);
        }
        if(callback != null)
            callback.callBack(count, curCount, true);
        byte data[] = outStream.toByteArray();
        outStream.close();
        is.close();
        return new String(data, charset);
    }
}
