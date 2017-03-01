// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AjaxParams.java

package net.tsz.afinal.http;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

// Referenced classes of package net.tsz.afinal.http:
//            MultipartEntity

public class AjaxParams
{
    private static class FileWrapper
    {

        public String getFileName()
        {
            if(fileName != null)
                return fileName;
            else
                return "nofilename";
        }

        public InputStream inputStream;
        public String fileName;
        public String contentType;

        public FileWrapper(InputStream inputStream, String fileName, String contentType)
        {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
        }
    }


    public AjaxParams()
    {
        init();
    }

    public AjaxParams(Map source)
    {
        init();
        java.util.Map.Entry entry;
        for(Iterator iterator = source.entrySet().iterator(); iterator.hasNext(); put((String)entry.getKey(), (String)entry.getValue()))
            entry = (java.util.Map.Entry)iterator.next();

    }

    public AjaxParams(String key, String value)
    {
        init();
        put(key, value);
    }

    public transient AjaxParams(Object keysAndValues[])
    {
        init();
        int len = keysAndValues.length;
        if(len % 2 != 0)
            throw new IllegalArgumentException("Supplied arguments must be even");
        for(int i = 0; i < len; i += 2)
        {
            String key = String.valueOf(keysAndValues[i]);
            String val = String.valueOf(keysAndValues[i + 1]);
            put(key, val);
        }

    }

    public void put(String key, String value)
    {
        if(key != null && value != null)
            urlParams.put(key, value);
    }

    public void put(String key, File file)
        throws FileNotFoundException
    {
        put(key, ((InputStream) (new FileInputStream(file))), file.getName());
    }

    public void put(String key, InputStream stream)
    {
        put(key, stream, null);
    }

    public void put(String key, InputStream stream, String fileName)
    {
        put(key, stream, fileName, null);
    }

    public void put(String key, InputStream stream, String fileName, String contentType)
    {
        if(key != null && stream != null)
            fileParams.put(key, new FileWrapper(stream, fileName, contentType));
    }

    public void remove(String key)
    {
        urlParams.remove(key);
        fileParams.remove(key);
    }

    public String toString()
    {
        StringBuilder result = new StringBuilder();
        java.util.Map.Entry entry;
        for(Iterator iterator = urlParams.entrySet().iterator(); iterator.hasNext(); result.append((String)entry.getValue()))
        {
            entry = (java.util.Map.Entry)iterator.next();
            if(result.length() > 0)
                result.append("&");
            result.append((String)entry.getKey());
            result.append("=");
        }

        for(Iterator iterator1 = fileParams.entrySet().iterator(); iterator1.hasNext(); result.append("FILE"))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator1.next();
            if(result.length() > 0)
                result.append("&");
            result.append((String)entry.getKey());
            result.append("=");
        }

        return result.toString();
    }

    public HttpEntity getEntity()
    {
        HttpEntity entity = null;
        if(!fileParams.isEmpty())
        {
            MultipartEntity multipartEntity = new MultipartEntity();
            java.util.Map.Entry entry;
            for(Iterator iterator = urlParams.entrySet().iterator(); iterator.hasNext(); multipartEntity.addPart((String)entry.getKey(), (String)entry.getValue()))
                entry = (java.util.Map.Entry)iterator.next();

            int currentIndex = 0;
            int lastIndex = fileParams.entrySet().size() - 1;
            for(Iterator iterator1 = fileParams.entrySet().iterator(); iterator1.hasNext();)
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator1.next();
                FileWrapper file = (FileWrapper)entry.getValue();
                if(file.inputStream != null)
                {
                    boolean isLast = currentIndex == lastIndex;
                    if(file.contentType != null)
                        multipartEntity.addPart((String)entry.getKey(), file.getFileName(), file.inputStream, file.contentType, isLast);
                    else
                        multipartEntity.addPart((String)entry.getKey(), file.getFileName(), file.inputStream, isLast);
                }
                currentIndex++;
            }

            entity = multipartEntity;
        } else
        {
            try
            {
                entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        return entity;
    }

    private void init()
    {
        urlParams = new ConcurrentHashMap();
        fileParams = new ConcurrentHashMap();
    }

    protected List getParamsList()
    {
        List lparams = new LinkedList();
        java.util.Map.Entry entry;
        for(Iterator iterator = urlParams.entrySet().iterator(); iterator.hasNext(); lparams.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue())))
            entry = (java.util.Map.Entry)iterator.next();

        return lparams;
    }

    public String getParamString()
    {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }

    private static String ENCODING = "UTF-8";
    protected ConcurrentHashMap urlParams;
    protected ConcurrentHashMap fileParams;

}
