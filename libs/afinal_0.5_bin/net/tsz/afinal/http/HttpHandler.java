// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpHandler.java

package net.tsz.afinal.http;

import android.os.SystemClock;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import net.tsz.afinal.core.AsyncTask;
import net.tsz.afinal.http.entityhandler.EntityCallBack;
import net.tsz.afinal.http.entityhandler.FileEntityHandler;
import net.tsz.afinal.http.entityhandler.StringEntityHandler;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

// Referenced classes of package net.tsz.afinal.http:
//            AjaxCallBack

public class HttpHandler extends AsyncTask
    implements EntityCallBack
{

    public HttpHandler(AbstractHttpClient client, HttpContext context, AjaxCallBack callback, String charset)
    {
        executionCount = 0;
        targetUrl = null;
        isResume = false;
        this.client = client;
        this.context = context;
        this.callback = callback;
        this.charset = charset;
    }

    private void makeRequestWithRetries(HttpUriRequest request)
        throws IOException
    {
        if(isResume && targetUrl != null)
        {
            File downloadFile = new File(targetUrl);
            long fileLen = 0L;
            if(downloadFile.isFile() && downloadFile.exists())
                fileLen = downloadFile.length();
            if(fileLen > 0L)
                request.setHeader("RANGE", (new StringBuilder("bytes=")).append(fileLen).append("-").toString());
        }
        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        while(retry) 
            try
            {
                if(!isCancelled())
                {
                    HttpResponse response = client.execute(request, context);
                    if(!isCancelled())
                        handleResponse(response);
                }
                return;
            }
            catch(UnknownHostException e)
            {
                publishProgress(new Object[] {
                    Integer.valueOf(3), e, Integer.valueOf(0), "unknownHostException\uFF1Acan't resolve host"
                });
                return;
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

    protected transient Object doInBackground(Object params[])
    {
        if(params != null && params.length == 3)
        {
            targetUrl = String.valueOf(params[1]);
            isResume = ((Boolean)params[2]).booleanValue();
        }
        try
        {
            publishProgress(new Object[] {
                Integer.valueOf(1)
            });
            makeRequestWithRetries((HttpUriRequest)params[0]);
        }
        catch(IOException e)
        {
            publishProgress(new Object[] {
                Integer.valueOf(3), e, Integer.valueOf(0), e.getMessage()
            });
        }
        return null;
    }

    protected transient void onProgressUpdate(Object values[])
    {
        int update = Integer.valueOf(String.valueOf(values[0])).intValue();
        switch(update)
        {
        default:
            break;

        case 1: // '\001'
            if(callback != null)
                callback.onStart();
            break;

        case 2: // '\002'
            if(callback != null)
                callback.onLoading(Long.valueOf(String.valueOf(values[1])).longValue(), Long.valueOf(String.valueOf(values[2])).longValue());
            break;

        case 3: // '\003'
            if(callback != null)
                callback.onFailure((Throwable)values[1], ((Integer)values[2]).intValue(), (String)values[3]);
            break;

        case 4: // '\004'
            if(callback != null)
                callback.onSuccess(values[1]);
            break;
        }
        super.onProgressUpdate(values);
    }

    public boolean isStop()
    {
        return mFileEntityHandler.isStop();
    }

    public void stop()
    {
        mFileEntityHandler.setStop(true);
    }

    private void handleResponse(HttpResponse response)
    {
        StatusLine status = response.getStatusLine();
        if(status.getStatusCode() >= 300)
        {
            String errorMsg = (new StringBuilder("response status error code:")).append(status.getStatusCode()).toString();
            if(status.getStatusCode() == 416 && isResume)
                errorMsg = (new StringBuilder(String.valueOf(errorMsg))).append(" \n maybe you have download complete.").toString();
            publishProgress(new Object[] {
                Integer.valueOf(3), new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), Integer.valueOf(status.getStatusCode()), errorMsg
            });
        } else
        {
            try
            {
                HttpEntity entity = response.getEntity();
                Object responseBody = null;
                if(entity != null)
                {
                    time = SystemClock.uptimeMillis();
                    if(targetUrl != null)
                        responseBody = mFileEntityHandler.handleEntity(entity, this, targetUrl, isResume);
                    else
                        responseBody = mStrEntityHandler.handleEntity(entity, this, charset);
                }
                publishProgress(new Object[] {
                    Integer.valueOf(4), responseBody
                });
            }
            catch(IOException e)
            {
                publishProgress(new Object[] {
                    Integer.valueOf(3), e, Integer.valueOf(0), e.getMessage()
                });
            }
        }
    }

    public void callBack(long count, long current, boolean mustNoticeUI)
    {
        if(callback != null && callback.isProgress())
            if(mustNoticeUI)
            {
                publishProgress(new Object[] {
                    Integer.valueOf(2), Long.valueOf(count), Long.valueOf(current)
                });
            } else
            {
                long thisTime = SystemClock.uptimeMillis();
                if(thisTime - time >= (long)callback.getRate())
                {
                    time = thisTime;
                    publishProgress(new Object[] {
                        Integer.valueOf(2), Long.valueOf(count), Long.valueOf(current)
                    });
                }
            }
    }

    private final AbstractHttpClient client;
    private final HttpContext context;
    private final StringEntityHandler mStrEntityHandler = new StringEntityHandler();
    private final FileEntityHandler mFileEntityHandler = new FileEntityHandler();
    private final AjaxCallBack callback;
    private int executionCount;
    private String targetUrl;
    private boolean isResume;
    private String charset;
    private static final int UPDATE_START = 1;
    private static final int UPDATE_LOADING = 2;
    private static final int UPDATE_FAILURE = 3;
    private static final int UPDATE_SUCCESS = 4;
    private long time;
}
