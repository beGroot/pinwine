// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BitmapProcess.java

package net.tsz.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import net.tsz.afinal.bitmap.download.Downloader;

// Referenced classes of package net.tsz.afinal.bitmap.core:
//            BytesBufferPool, BitmapDisplayConfig, BitmapDecoder, BitmapCache

public class BitmapProcess
{

    public BitmapProcess(Downloader downloader, BitmapCache cache)
    {
        mDownloader = downloader;
        mCache = cache;
    }

    public Bitmap getBitmap(String url, BitmapDisplayConfig config)
    {
        Bitmap bitmap = getFromDisk(url, config);
        if(bitmap == null)
        {
            byte data[] = mDownloader.download(url);
            if(data != null && data.length > 0)
            {
                if(config != null)
                    bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(data, 0, data.length, config.getBitmapWidth(), config.getBitmapHeight());
                else
                    return BitmapFactory.decodeByteArray(data, 0, data.length);
                mCache.addToDiskCache(url, data);
            }
        }
        return bitmap;
    }

    public Bitmap getFromDisk(String key, BitmapDisplayConfig config)
    {
        BytesBufferPool.BytesBuffer buffer;
        Bitmap b;
        buffer = sMicroThumbBufferPool.get();
        b = null;
        boolean found = mCache.getImageData(key, buffer);
        if(found && buffer.length - buffer.offset > 0)
            if(config != null)
                b = BitmapDecoder.decodeSampledBitmapFromByteArray(buffer.data, buffer.offset, buffer.length, config.getBitmapWidth(), config.getBitmapHeight());
            else
                b = BitmapFactory.decodeByteArray(buffer.data, buffer.offset, buffer.length);
        break MISSING_BLOCK_LABEL_102;
        Exception exception;
        exception;
        sMicroThumbBufferPool.recycle(buffer);
        throw exception;
        sMicroThumbBufferPool.recycle(buffer);
        return b;
    }

    private Downloader mDownloader;
    private BitmapCache mCache;
    private static final int BYTESBUFFE_POOL_SIZE = 4;
    private static final int BYTESBUFFER_SIZE = 0x32000;
    private static final BytesBufferPool sMicroThumbBufferPool = new BytesBufferPool(4, 0x32000);

}
