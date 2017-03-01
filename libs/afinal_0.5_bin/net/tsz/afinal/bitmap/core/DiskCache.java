// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DiskCache.java

package net.tsz.afinal.bitmap.core;

import android.util.Log;
import java.io.*;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.Adler32;

public class DiskCache
    implements Closeable
{
    public static class LookupRequest
    {

        public long key;
        public byte buffer[];
        public int length;

        public LookupRequest()
        {
        }
    }


    public DiskCache(String path, int maxEntries, int maxBytes, boolean reset)
        throws IOException
    {
        this(path, maxEntries, maxBytes, reset, 0);
    }

    public DiskCache(String path, int maxEntries, int maxBytes, boolean reset, int version)
        throws IOException
    {
        mIndexHeader = new byte[32];
        mBlobHeader = new byte[20];
        mAdler32 = new Adler32();
        mLookupRequest = new LookupRequest();
        File dir = new File(path);
        if(!dir.exists() && !dir.mkdirs())
            throw new IOException("unable to make dirs");
        mPath = path;
        mIndexFile = new RandomAccessFile((new StringBuilder(String.valueOf(path))).append(".idx").toString(), "rw");
        mDataFile0 = new RandomAccessFile((new StringBuilder(String.valueOf(path))).append(".0").toString(), "rw");
        mDataFile1 = new RandomAccessFile((new StringBuilder(String.valueOf(path))).append(".1").toString(), "rw");
        mVersion = version;
        if(!reset && loadIndex())
            return;
        resetCache(maxEntries, maxBytes);
        if(!loadIndex())
        {
            closeAll();
            throw new IOException("unable to load index");
        } else
        {
            return;
        }
    }

    public void delete()
    {
        deleteFileSilently((new StringBuilder(String.valueOf(mPath))).append(".idx").toString());
        deleteFileSilently((new StringBuilder(String.valueOf(mPath))).append(".0").toString());
        deleteFileSilently((new StringBuilder(String.valueOf(mPath))).append(".1").toString());
    }

    private static void deleteFileSilently(String path)
    {
        try
        {
            (new File(path)).delete();
        }
        catch(Throwable throwable) { }
    }

    public void close()
    {
        syncAll();
        closeAll();
    }

    private void closeAll()
    {
        closeSilently(mIndexChannel);
        closeSilently(mIndexFile);
        closeSilently(mDataFile0);
        closeSilently(mDataFile1);
    }

    private boolean loadIndex()
    {
        byte buf[];
        mIndexFile.seek(0L);
        mDataFile0.seek(0L);
        mDataFile1.seek(0L);
        buf = mIndexHeader;
        if(mIndexFile.read(buf) == 32)
            break MISSING_BLOCK_LABEL_53;
        Log.w(TAG, "cannot read header");
        return false;
        if(readInt(buf, 0) == 0xb3273030)
            break MISSING_BLOCK_LABEL_74;
        Log.w(TAG, "cannot read header magic");
        return false;
        if(readInt(buf, 24) == mVersion)
            break MISSING_BLOCK_LABEL_98;
        Log.w(TAG, "version mismatch");
        return false;
        mMaxEntries = readInt(buf, 4);
        mMaxBytes = readInt(buf, 8);
        mActiveRegion = readInt(buf, 12);
        mActiveEntries = readInt(buf, 16);
        mActiveBytes = readInt(buf, 20);
        int sum = readInt(buf, 28);
        if(checkSum(buf, 0, 28) == sum)
            break MISSING_BLOCK_LABEL_177;
        Log.w(TAG, "header checksum does not match");
        return false;
        if(mMaxEntries > 0)
            break MISSING_BLOCK_LABEL_195;
        Log.w(TAG, "invalid max entries");
        return false;
        if(mMaxBytes > 0)
            break MISSING_BLOCK_LABEL_213;
        Log.w(TAG, "invalid max bytes");
        return false;
        if(mActiveRegion == 0 || mActiveRegion == 1)
            break MISSING_BLOCK_LABEL_239;
        Log.w(TAG, "invalid active region");
        return false;
        if(mActiveEntries >= 0 && mActiveEntries <= mMaxEntries)
            break MISSING_BLOCK_LABEL_268;
        Log.w(TAG, "invalid active entries");
        return false;
        if(mActiveBytes >= 4 && mActiveBytes <= mMaxBytes)
            break MISSING_BLOCK_LABEL_298;
        Log.w(TAG, "invalid active bytes");
        return false;
        if(mIndexFile.length() == (long)(32 + mMaxEntries * 12 * 2))
            break MISSING_BLOCK_LABEL_333;
        Log.w(TAG, "invalid index file length");
        return false;
        byte magic[];
        magic = new byte[4];
        if(mDataFile0.read(magic) == 4)
            break MISSING_BLOCK_LABEL_360;
        Log.w(TAG, "cannot read data file magic");
        return false;
        if(readInt(magic, 0) == 0xbd248510)
            break MISSING_BLOCK_LABEL_382;
        Log.w(TAG, "invalid data file magic");
        return false;
        if(mDataFile1.read(magic) == 4)
            break MISSING_BLOCK_LABEL_405;
        Log.w(TAG, "cannot read data file magic");
        return false;
        if(readInt(magic, 0) == 0xbd248510)
            break MISSING_BLOCK_LABEL_427;
        Log.w(TAG, "invalid data file magic");
        return false;
        try
        {
            mIndexChannel = mIndexFile.getChannel();
            mIndexBuffer = mIndexChannel.map(java.nio.channels.FileChannel.MapMode.READ_WRITE, 0L, mIndexFile.length());
            mIndexBuffer.order(ByteOrder.LITTLE_ENDIAN);
            setActiveVariables();
        }
        catch(IOException ex)
        {
            Log.e(TAG, "loadIndex failed.", ex);
            return false;
        }
        return true;
    }

    private void setActiveVariables()
        throws IOException
    {
        mActiveDataFile = mActiveRegion != 0 ? mDataFile1 : mDataFile0;
        mInactiveDataFile = mActiveRegion != 1 ? mDataFile1 : mDataFile0;
        mActiveDataFile.setLength(mActiveBytes);
        mActiveDataFile.seek(mActiveBytes);
        mActiveHashStart = 32;
        mInactiveHashStart = 32;
        if(mActiveRegion == 0)
            mInactiveHashStart += mMaxEntries * 12;
        else
            mActiveHashStart += mMaxEntries * 12;
    }

    private void resetCache(int maxEntries, int maxBytes)
        throws IOException
    {
        mIndexFile.setLength(0L);
        mIndexFile.setLength(32 + maxEntries * 12 * 2);
        mIndexFile.seek(0L);
        byte buf[] = mIndexHeader;
        writeInt(buf, 0, 0xb3273030);
        writeInt(buf, 4, maxEntries);
        writeInt(buf, 8, maxBytes);
        writeInt(buf, 12, 0);
        writeInt(buf, 16, 0);
        writeInt(buf, 20, 4);
        writeInt(buf, 24, mVersion);
        writeInt(buf, 28, checkSum(buf, 0, 28));
        mIndexFile.write(buf);
        mDataFile0.setLength(0L);
        mDataFile1.setLength(0L);
        mDataFile0.seek(0L);
        mDataFile1.seek(0L);
        writeInt(buf, 0, 0xbd248510);
        mDataFile0.write(buf, 0, 4);
        mDataFile1.write(buf, 0, 4);
    }

    private void flipRegion()
        throws IOException
    {
        mActiveRegion = 1 - mActiveRegion;
        mActiveEntries = 0;
        mActiveBytes = 4;
        writeInt(mIndexHeader, 12, mActiveRegion);
        writeInt(mIndexHeader, 16, mActiveEntries);
        writeInt(mIndexHeader, 20, mActiveBytes);
        updateIndexHeader();
        setActiveVariables();
        clearHash(mActiveHashStart);
        syncIndex();
    }

    private void updateIndexHeader()
    {
        writeInt(mIndexHeader, 28, checkSum(mIndexHeader, 0, 28));
        mIndexBuffer.position(0);
        mIndexBuffer.put(mIndexHeader);
    }

    private void clearHash(int hashStart)
    {
        byte zero[] = new byte[1024];
        mIndexBuffer.position(hashStart);
        int todo;
        for(int count = mMaxEntries * 12; count > 0; count -= todo)
        {
            todo = Math.min(count, 1024);
            mIndexBuffer.put(zero, 0, todo);
        }

    }

    public void insert(long key, byte data[])
        throws IOException
    {
        if(24 + data.length > mMaxBytes)
            throw new RuntimeException("blob is too large!");
        if(mActiveBytes + 20 + data.length > mMaxBytes || mActiveEntries * 2 >= mMaxEntries)
            flipRegion();
        if(!lookupInternal(key, mActiveHashStart))
        {
            mActiveEntries++;
            writeInt(mIndexHeader, 16, mActiveEntries);
        }
        insertInternal(key, data, data.length);
        updateIndexHeader();
    }

    private void insertInternal(long key, byte data[], int length)
        throws IOException
    {
        byte header[] = mBlobHeader;
        int sum = checkSum(data);
        writeLong(header, 0, key);
        writeInt(header, 8, sum);
        writeInt(header, 12, mActiveBytes);
        writeInt(header, 16, length);
        mActiveDataFile.write(header);
        mActiveDataFile.write(data, 0, length);
        mIndexBuffer.putLong(mSlotOffset, key);
        mIndexBuffer.putInt(mSlotOffset + 8, mActiveBytes);
        mActiveBytes += 20 + length;
        writeInt(mIndexHeader, 20, mActiveBytes);
    }

    public byte[] lookup(long key)
        throws IOException
    {
        mLookupRequest.key = key;
        mLookupRequest.buffer = null;
        if(lookup(mLookupRequest))
            return mLookupRequest.buffer;
        else
            return null;
    }

    public boolean lookup(LookupRequest req)
        throws IOException
    {
        if(lookupInternal(req.key, mActiveHashStart) && getBlob(mActiveDataFile, mFileOffset, req))
            return true;
        int insertOffset = mSlotOffset;
        if(lookupInternal(req.key, mInactiveHashStart) && getBlob(mInactiveDataFile, mFileOffset, req))
        {
            if(mActiveBytes + 20 + req.length > mMaxBytes || mActiveEntries * 2 >= mMaxEntries)
                return true;
            mSlotOffset = insertOffset;
            try
            {
                insertInternal(req.key, req.buffer, req.length);
                mActiveEntries++;
                writeInt(mIndexHeader, 16, mActiveEntries);
                updateIndexHeader();
            }
            catch(Throwable t)
            {
                Log.e(TAG, "cannot copy over");
            }
            return true;
        } else
        {
            return false;
        }
    }

    private boolean getBlob(RandomAccessFile file, int offset, LookupRequest req)
        throws IOException
    {
        byte header[];
        long oldPosition;
        header = mBlobHeader;
        oldPosition = file.getFilePointer();
        file.seek(offset);
        if(file.read(header) == 20)
            break MISSING_BLOCK_LABEL_47;
        Log.w(TAG, "cannot read blob header");
        file.seek(oldPosition);
        return false;
        long blobKey = readLong(header, 0);
        if(blobKey == req.key)
            break MISSING_BLOCK_LABEL_98;
        Log.w(TAG, (new StringBuilder("blob key does not match: ")).append(blobKey).toString());
        file.seek(oldPosition);
        return false;
        int sum;
        sum = readInt(header, 8);
        int blobOffset = readInt(header, 12);
        if(blobOffset == offset)
            break MISSING_BLOCK_LABEL_155;
        Log.w(TAG, (new StringBuilder("blob offset does not match: ")).append(blobOffset).toString());
        file.seek(oldPosition);
        return false;
        int length;
        length = readInt(header, 16);
        if(length >= 0 && length <= mMaxBytes - offset - 20)
            break MISSING_BLOCK_LABEL_216;
        Log.w(TAG, (new StringBuilder("invalid blob length: ")).append(length).toString());
        file.seek(oldPosition);
        return false;
        byte blob[];
        if(req.buffer == null || req.buffer.length < length)
            req.buffer = new byte[length];
        blob = req.buffer;
        req.length = length;
        if(file.read(blob, 0, length) == length)
            break MISSING_BLOCK_LABEL_285;
        Log.w(TAG, "cannot read blob data");
        file.seek(oldPosition);
        return false;
        if(checkSum(blob, 0, length) == sum)
            break MISSING_BLOCK_LABEL_332;
        Log.w(TAG, (new StringBuilder("blob checksum does not match: ")).append(sum).toString());
        file.seek(oldPosition);
        return false;
        file.seek(oldPosition);
        return true;
        Throwable t;
        t;
        Log.e(TAG, "getBlob failed.", t);
        file.seek(oldPosition);
        return false;
        Exception exception;
        exception;
        file.seek(oldPosition);
        throw exception;
    }

    private boolean lookupInternal(long key, int hashStart)
    {
        int slot = (int)(key % (long)mMaxEntries);
        if(slot < 0)
            slot += mMaxEntries;
        int slotBegin = slot;
        do
        {
            do
            {
                int offset = hashStart + slot * 12;
                long candidateKey = mIndexBuffer.getLong(offset);
                int candidateOffset = mIndexBuffer.getInt(offset + 8);
                if(candidateOffset == 0)
                {
                    mSlotOffset = offset;
                    return false;
                }
                if(candidateKey == key)
                {
                    mSlotOffset = offset;
                    mFileOffset = candidateOffset;
                    return true;
                }
                if(++slot >= mMaxEntries)
                    slot = 0;
            } while(slot != slotBegin);
            Log.w(TAG, "corrupted index: clear the slot.");
            mIndexBuffer.putInt(hashStart + slot * 12 + 8, 0);
        } while(true);
    }

    public void syncIndex()
    {
        try
        {
            mIndexBuffer.force();
        }
        catch(Throwable t)
        {
            Log.w(TAG, "sync index failed", t);
        }
    }

    public void syncAll()
    {
        syncIndex();
        try
        {
            mDataFile0.getFD().sync();
        }
        catch(Throwable t)
        {
            Log.w(TAG, "sync data file 0 failed", t);
        }
        try
        {
            mDataFile1.getFD().sync();
        }
        catch(Throwable t)
        {
            Log.w(TAG, "sync data file 1 failed", t);
        }
    }

    int getActiveCount()
    {
        int count = 0;
        for(int i = 0; i < mMaxEntries; i++)
        {
            int offset = mActiveHashStart + i * 12;
            int candidateOffset = mIndexBuffer.getInt(offset + 8);
            if(candidateOffset != 0)
                count++;
        }

        if(count == mActiveEntries)
        {
            return count;
        } else
        {
            Log.e(TAG, (new StringBuilder("wrong active count: ")).append(mActiveEntries).append(" vs ").append(count).toString());
            return -1;
        }
    }

    int checkSum(byte data[])
    {
        mAdler32.reset();
        mAdler32.update(data);
        return (int)mAdler32.getValue();
    }

    int checkSum(byte data[], int offset, int nbytes)
    {
        mAdler32.reset();
        mAdler32.update(data, offset, nbytes);
        return (int)mAdler32.getValue();
    }

    static void closeSilently(Closeable c)
    {
        if(c == null)
            return;
        try
        {
            c.close();
        }
        catch(Throwable throwable) { }
    }

    static int readInt(byte buf[], int offset)
    {
        return buf[offset] & 0xff | (buf[offset + 1] & 0xff) << 8 | (buf[offset + 2] & 0xff) << 16 | (buf[offset + 3] & 0xff) << 24;
    }

    static long readLong(byte buf[], int offset)
    {
        long result = buf[offset + 7] & 0xff;
        for(int i = 6; i >= 0; i--)
            result = result << 8 | (long)(buf[offset + i] & 0xff);

        return result;
    }

    static void writeInt(byte buf[], int offset, int value)
    {
        for(int i = 0; i < 4; i++)
        {
            buf[offset + i] = (byte)(value & 0xff);
            value >>= 8;
        }

    }

    static void writeLong(byte buf[], int offset, long value)
    {
        for(int i = 0; i < 8; i++)
        {
            buf[offset + i] = (byte)(int)(value & 255L);
            value >>= 8;
        }

    }

    private static final String TAG = net/tsz/afinal/bitmap/core/DiskCache.getSimpleName();
    private static final int MAGIC_INDEX_FILE = 0xb3273030;
    private static final int MAGIC_DATA_FILE = 0xbd248510;
    private static final int IH_MAGIC = 0;
    private static final int IH_MAX_ENTRIES = 4;
    private static final int IH_MAX_BYTES = 8;
    private static final int IH_ACTIVE_REGION = 12;
    private static final int IH_ACTIVE_ENTRIES = 16;
    private static final int IH_ACTIVE_BYTES = 20;
    private static final int IH_VERSION = 24;
    private static final int IH_CHECKSUM = 28;
    private static final int INDEX_HEADER_SIZE = 32;
    private static final int DATA_HEADER_SIZE = 4;
    private static final int BH_KEY = 0;
    private static final int BH_CHECKSUM = 8;
    private static final int BH_OFFSET = 12;
    private static final int BH_LENGTH = 16;
    private static final int BLOB_HEADER_SIZE = 20;
    private RandomAccessFile mIndexFile;
    private RandomAccessFile mDataFile0;
    private RandomAccessFile mDataFile1;
    private FileChannel mIndexChannel;
    private MappedByteBuffer mIndexBuffer;
    private int mMaxEntries;
    private int mMaxBytes;
    private int mActiveRegion;
    private int mActiveEntries;
    private int mActiveBytes;
    private int mVersion;
    private RandomAccessFile mActiveDataFile;
    private RandomAccessFile mInactiveDataFile;
    private int mActiveHashStart;
    private int mInactiveHashStart;
    private byte mIndexHeader[];
    private byte mBlobHeader[];
    private Adler32 mAdler32;
    private String mPath;
    private LookupRequest mLookupRequest;
    private int mSlotOffset;
    private int mFileOffset;

}
