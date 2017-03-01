// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RetryHandler.java

package net.tsz.afinal.http;

import android.os.SystemClock;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

public class RetryHandler
    implements HttpRequestRetryHandler
{

    public RetryHandler(int maxRetries)
    {
        this.maxRetries = maxRetries;
    }

    public boolean retryRequest(IOException exception, int executionCount, HttpContext context)
    {
        boolean retry = true;
        Boolean b = (Boolean)context.getAttribute("http.request_sent");
        boolean sent = b != null && b.booleanValue();
        if(executionCount > maxRetries)
            retry = false;
        else
        if(exceptionBlacklist.contains(exception.getClass()))
            retry = false;
        else
        if(exceptionWhitelist.contains(exception.getClass()))
            retry = true;
        else
        if(!sent)
            retry = true;
        if(retry)
        {
            HttpUriRequest currentReq = (HttpUriRequest)context.getAttribute("http.request");
            retry = currentReq != null && !"POST".equals(currentReq.getMethod());
        }
        if(retry)
            SystemClock.sleep(1000L);
        else
            exception.printStackTrace();
        return retry;
    }

    private static final int RETRY_SLEEP_TIME_MILLIS = 1000;
    private static HashSet exceptionWhitelist;
    private static HashSet exceptionBlacklist;
    private final int maxRetries;

    static 
    {
        exceptionWhitelist = new HashSet();
        exceptionBlacklist = new HashSet();
        exceptionWhitelist.add(org/apache/http/NoHttpResponseException);
        exceptionWhitelist.add(java/net/UnknownHostException);
        exceptionWhitelist.add(java/net/SocketException);
        exceptionBlacklist.add(java/io/InterruptedIOException);
        exceptionBlacklist.add(javax/net/ssl/SSLHandshakeException);
    }
}
