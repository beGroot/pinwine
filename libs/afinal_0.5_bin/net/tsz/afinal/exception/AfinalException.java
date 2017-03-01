// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AfinalException.java

package net.tsz.afinal.exception;


public class AfinalException extends RuntimeException
{

    public AfinalException()
    {
    }

    public AfinalException(String msg)
    {
        super(msg);
    }

    public AfinalException(Throwable ex)
    {
        super(ex);
    }

    public AfinalException(String msg, Throwable ex)
    {
        super(msg, ex);
    }

    private static final long serialVersionUID = 1L;
}
