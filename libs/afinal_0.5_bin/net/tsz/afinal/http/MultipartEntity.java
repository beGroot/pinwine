// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MultipartEntity.java

package net.tsz.afinal.http;

import java.io.*;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

class MultipartEntity
    implements HttpEntity
{

    public MultipartEntity()
    {
        boundary = null;
        out = new ByteArrayOutputStream();
        isSetLast = false;
        isSetFirst = false;
        StringBuffer buf = new StringBuffer();
        Random rand = new Random();
        for(int i = 0; i < 30; i++)
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);

        boundary = buf.toString();
    }

    public void writeFirstBoundaryIfNeeds()
    {
        if(!isSetFirst)
            try
            {
                out.write((new StringBuilder("--")).append(boundary).append("\r\n").toString().getBytes());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds()
    {
        if(isSetLast)
            return;
        try
        {
            out.write((new StringBuilder("\r\n--")).append(boundary).append("--\r\n").toString().getBytes());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        isSetLast = true;
    }

    public void addPart(String key, String value)
    {
        writeFirstBoundaryIfNeeds();
        try
        {
            out.write((new StringBuilder("Content-Disposition: form-data; name=\"")).append(key).append("\"\r\n\r\n").toString().getBytes());
            out.write(value.getBytes());
            out.write((new StringBuilder("\r\n--")).append(boundary).append("\r\n").toString().getBytes());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addPart(String key, String fileName, InputStream fin, boolean isLast)
    {
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    public void addPart(String key, String fileName, InputStream fin, String type, boolean isLast)
    {
        writeFirstBoundaryIfNeeds();
        try
        {
            type = (new StringBuilder("Content-Type: ")).append(type).append("\r\n").toString();
            out.write((new StringBuilder("Content-Disposition: form-data; name=\"")).append(key).append("\"; filename=\"").append(fileName).append("\"\r\n").toString().getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
            byte tmp[] = new byte[4096];
            for(int l = 0; (l = fin.read(tmp)) != -1;)
                out.write(tmp, 0, l);

            if(!isLast)
                out.write((new StringBuilder("\r\n--")).append(boundary).append("\r\n").toString().getBytes());
            out.flush();
            break MISSING_BLOCK_LABEL_222;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            fin.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        break MISSING_BLOCK_LABEL_236;
        Exception exception;
        exception;
        try
        {
            fin.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        throw exception;
        try
        {
            fin.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addPart(String key, File value, boolean isLast)
    {
        try
        {
            addPart(key, value.getName(), ((InputStream) (new FileInputStream(value))), isLast);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public long getContentLength()
    {
        writeLastBoundaryIfNeeds();
        return (long)out.toByteArray().length;
    }

    public Header getContentType()
    {
        return new BasicHeader("Content-Type", (new StringBuilder("multipart/form-data; boundary=")).append(boundary).toString());
    }

    public boolean isChunked()
    {
        return false;
    }

    public boolean isRepeatable()
    {
        return false;
    }

    public boolean isStreaming()
    {
        return false;
    }

    public void writeTo(OutputStream outstream)
        throws IOException
    {
        outstream.write(out.toByteArray());
    }

    public Header getContentEncoding()
    {
        return null;
    }

    public void consumeContent()
        throws IOException, UnsupportedOperationException
    {
        if(isStreaming())
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        else
            return;
    }

    public InputStream getContent()
        throws IOException, UnsupportedOperationException
    {
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static final char MULTIPART_CHARS[] = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private String boundary;
    ByteArrayOutputStream out;
    boolean isSetLast;
    boolean isSetFirst;

}
