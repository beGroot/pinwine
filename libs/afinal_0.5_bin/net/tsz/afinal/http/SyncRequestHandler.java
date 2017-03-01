// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SyncRequestHandler.java

package net.tsz.afinal.http;

import java.io.IOException;
import java.net.UnknownHostException;
import net.tsz.afinal.http.entityhandler.StringEntityHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class SyncRequestHandler
{

    public SyncRequestHandler(AbstractHttpClient client, HttpContext context, String charset)
    {
        executionCount = 0;
        this.client = client;
        this.context = context;
        this.charset = charset;
    }

    private Object makeRequestWithRetries(HttpUriRequest request)
        throws IOException
    {
        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        while(retry) 
            try
            {
                HttpResponse response = client.execute(request, context);
                return entityHandler.handleEntity(response.getEntity(), null, charset);
            }
            catch(UnknownHostException e)
            {
                cause = e;
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            }
            catch(IOException e)
            {
                cause = e;
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            }
            catch(NullPointerException e)
            {
                cause = new IOException((new StringBuilder("NPE in HttpClient")).append(e.getMessage()).toString());
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            }
            catch(Exception e)
            {
                cause = new IOException((new StringBuilder("Exception")).append(e.getMessage()).toString());
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
            }
        if(cause != null)
            throw cause;
        else
            throw new IOException("\u672A\u77E5\u7F51\u7EDC\u9519\u8BEF");
    }

    public transient Object sendRequest(HttpUriRequest params[])
    {
        try
        {
            return makeRequestWithRetries(params[0]);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private final AbstractHttpClient client;
    private final HttpContext context;
    private final StringEntityHandler entityHandler = new StringEntityHandler();
    private int executionCount;
    private String charset;
}
