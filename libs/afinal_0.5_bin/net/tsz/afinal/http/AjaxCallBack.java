// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AjaxCallBack.java

package net.tsz.afinal.http;


public abstract class AjaxCallBack
{

    public AjaxCallBack()
    {
        progress = true;
        rate = 1000;
    }

    public boolean isProgress()
    {
        return progress;
    }

    public int getRate()
    {
        return rate;
    }

    public AjaxCallBack progress(boolean progress, int rate)
    {
        this.progress = progress;
        this.rate = rate;
        return this;
    }

    public void onStart()
    {
    }

    public void onLoading(long l, long l1)
    {
    }

    public void onSuccess(Object obj)
    {
    }

    public void onFailure(Throwable throwable, int i, String s)
    {
    }

    private boolean progress;
    private int rate;
}
