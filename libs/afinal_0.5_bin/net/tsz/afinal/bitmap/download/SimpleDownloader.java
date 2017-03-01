// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SimpleDownloader.java

package net.tsz.afinal.bitmap.download;

import android.util.Log;
import java.io.*;
import java.net.*;

// Referenced classes of package net.tsz.afinal.bitmap.download:
//            Downloader

public class SimpleDownloader
    implements Downloader
{
    public class FlushedInputStream extends FilterInputStream
    {

        public long skip(long n)
            throws IOException
        {
            long totalBytesSkipped;
            long bytesSkipped;
            for(totalBytesSkipped = 0L; totalBytesSkipped < n; totalBytesSkipped += bytesSkipped)
            {
                bytesSkipped = in.skip(n - totalBytesSkipped);
                if(bytesSkipped != 0L)
                    continue;
                int by_te = read();
                if(by_te < 0)
                    break;
                bytesSkipped = 1L;
            }

            return totalBytesSkipped;
        }

        final SimpleDownloader this$0;

        public FlushedInputStream(InputStream inputStream)
        {
            this$0 = SimpleDownloader.this;
            super(inputStream);
        }
    }


    public SimpleDownloader()
    {
    }

    public byte[] download(String urlString)
    {
        if(urlString == null)
            return null;
        if(urlString.trim().toLowerCase().startsWith("http"))
            return getFromHttp(urlString);
        if(urlString.trim().toLowerCase().startsWith("file:"))
        {
            try
            {
                File f = new File(new URI(urlString));
                if(f.exists() && f.canRead())
                    return getFromFile(f);
            }
            catch(URISyntaxException e)
            {
                Log.e(TAG, (new StringBuilder("Error in read from file - ")).append(urlString).append(" : ").append(e).toString());
            }
        } else
        {
            File f = new File(urlString);
            if(f.exists() && f.canRead())
                return getFromFile(f);
        }
        return null;
    }

    private byte[] getFromFile(File file)
    {
        FileInputStream fis;
        if(file == null)
            return null;
        fis = null;
        byte abyte0[];
        fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte buffer[] = new byte[1024];
        while((len = fis.read(buffer)) != -1) 
            baos.write(buffer, 0, len);
        abyte0 = baos.toByteArray();
        if(fis != null)
            try
            {
                fis.close();
                fis = null;
            }
            catch(IOException ioexception) { }
        return abyte0;
        Exception e;
        e;
        Log.e(TAG, (new StringBuilder("Error in read from file - ")).append(file).append(" : ").append(e).toString());
        if(fis != null)
            try
            {
                fis.close();
                fis = null;
            }
            catch(IOException ioexception1) { }
        break MISSING_BLOCK_LABEL_155;
        Exception exception;
        exception;
        if(fis != null)
            try
            {
                fis.close();
                fis = null;
            }
            catch(IOException ioexception2) { }
        throw exception;
        return null;
    }

    private byte[] getFromHttp(String urlString)
    {
        HttpURLConnection urlConnection;
        BufferedOutputStream out;
        FlushedInputStream in;
        urlConnection = null;
        out = null;
        in = null;
        byte abyte0[];
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection)url.openConnection();
        in = new FlushedInputStream(new BufferedInputStream(urlConnection.getInputStream(), 8192));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while((b = in.read()) != -1) 
            baos.write(b);
        abyte0 = baos.toByteArray();
        if(urlConnection != null)
            urlConnection.disconnect();
        try
        {
            if(out != null)
                out.close();
            if(in != null)
                in.close();
        }
        catch(IOException ioexception) { }
        return abyte0;
        IOException e;
        e;
        Log.e(TAG, (new StringBuilder("Error in downloadBitmap - ")).append(urlString).append(" : ").append(e).toString());
        if(urlConnection != null)
            urlConnection.disconnect();
        try
        {
            if(out != null)
                out.close();
            if(in != null)
                in.close();
        }
        catch(IOException ioexception1) { }
        break MISSING_BLOCK_LABEL_227;
        Exception exception;
        exception;
        if(urlConnection != null)
            urlConnection.disconnect();
        try
        {
            if(out != null)
                out.close();
            if(in != null)
                in.close();
        }
        catch(IOException ioexception2) { }
        throw exception;
        return null;
    }

    private static final String TAG = net/tsz/afinal/bitmap/download/SimpleDownloader.getSimpleName();
    private static final int IO_BUFFER_SIZE = 8192;

}
