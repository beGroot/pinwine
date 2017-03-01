// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BytesBufferPool.java

package net.tsz.afinal.bitmap.core;

import java.util.ArrayList;

public class BytesBufferPool
{
    public static class BytesBuffer
    {

        public byte data[];
        public int offset;
        public int length;

        private BytesBuffer(int capacity)
        {
            data = new byte[capacity];
        }

        BytesBuffer(int i, BytesBuffer bytesbuffer)
        {
            this(i);
        }
    }


    public BytesBufferPool(int poolSize, int bufferSize)
    {
        mList = new ArrayList(poolSize);
        mPoolSize = poolSize;
        mBufferSize = bufferSize;
    }

    public synchronized BytesBuffer get()
    {
        int n = mList.size();
        return n <= 0 ? new BytesBuffer(mBufferSize, null) : (BytesBuffer)mList.remove(n - 1);
    }

    public synchronized void recycle(BytesBuffer buffer)
    {
        if(buffer.data.length != mBufferSize)
            return;
        if(mList.size() < mPoolSize)
        {
            buffer.offset = 0;
            buffer.length = 0;
            mList.add(buffer);
        }
    }

    public synchronized void clear()
    {
        mList.clear();
    }

    private final int mPoolSize;
    private final int mBufferSize;
    private final ArrayList mList;
}
