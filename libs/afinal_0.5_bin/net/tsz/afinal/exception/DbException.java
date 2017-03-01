// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DbException.java

package net.tsz.afinal.exception;


// Referenced classes of package net.tsz.afinal.exception:
//            AfinalException

public class DbException extends AfinalException
{

    public DbException()
    {
    }

    public DbException(String msg)
    {
        super(msg);
    }

    public DbException(Throwable ex)
    {
        super(ex);
    }

    public DbException(String msg, Throwable ex)
    {
        super(msg, ex);
    }

    private static final long serialVersionUID = 1L;
}
