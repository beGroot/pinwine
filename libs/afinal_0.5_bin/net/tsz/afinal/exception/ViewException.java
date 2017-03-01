// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ViewException.java

package net.tsz.afinal.exception;

import java.io.PrintStream;

// Referenced classes of package net.tsz.afinal.exception:
//            AfinalException

public class ViewException extends AfinalException
{

    public ViewException(String strExce)
    {
        strMsg = null;
        strMsg = strExce;
    }

    public void printStackTrace()
    {
        if(strMsg != null)
            System.err.println(strMsg);
        super.printStackTrace();
    }

    private static final long serialVersionUID = 1L;
    private String strMsg;
}
