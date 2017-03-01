// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FinalHttp.java

package net.tsz.afinal;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import net.tsz.afinal.http.RetryHandler;
import net.tsz.afinal.http.SyncRequestHandler;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.*;
import org.apache.http.protocol.*;

public class FinalHttp
{
    private static class InflatingEntity extends HttpEntityWrapper
    {

        public InputStream getContent()
            throws IOException
        {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        public long getContentLength()
        {
            return -1L;
        }

        public InflatingEntity(HttpEntity wrapped)
        {
            super(wrapped);
        }
    }


    public FinalHttp()
    {
        charset = "utf-8";
        BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, httpParams);
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(HttpRequest request, HttpContext context)
            {
                if(!request.containsHeader("Accept-Encoding"))
                    request.addHeader("Accept-Encoding", "gzip");
                String header;
                for(Iterator iterator = clientHeaderMap.keySet().iterator(); iterator.hasNext(); request.addHeader(header, (String)clientHeaderMap.get(header)))
                    header = (String)iterator.next();

            }

            final FinalHttp this$0;

            
            {
                this$0 = FinalHttp.this;
                super();
            }
        }
);
        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {

            public void process(HttpResponse response, HttpContext context)
            {
                HttpEntity entity = response.getEntity();
                if(entity == null)
                    return;
                Header encoding = entity.getContentEncoding();
                if(encoding != null)
                {
                    HeaderElement aheaderelement[];
                    int j = (aheaderelement = encoding.getElements()).length;
                    for(int i = 0; i < j; i++)
                    {
                        HeaderElement element = aheaderelement[i];
                        if(!element.getName().equalsIgnoreCase("gzip"))
                            continue;
                        response.setEntity(new InflatingEntity(response.getEntity()));
                        break;
                    }

                }
            }

            final FinalHttp this$0;

            
            {
                this$0 = FinalHttp.this;
                super();
            }
        }
);
        httpClient.setHttpRequestRetryHandler(new RetryHandler(maxRetries));
    }

    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    public HttpContext getHttpContext()
    {
        return httpContext;
    }

    public void configCharset(String charSet)
    {
        if(charSet != null && charSet.trim().length() != 0)
            charset = charSet;
    }

    public void configCookieStore(CookieStore cookieStore)
    {
        httpContext.setAttribute("http.cookie-store", cookieStore);
    }

    public void configUserAgent(String userAgent)
    {
        HttpProtocolParams.setUserAgent(httpClient.getParams(), userAgent);
    }

    public void configTimeout(int timeout)
    {
        org.apache.http.params.HttpParams httpParams = httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
    }

    public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory)
    {
        Scheme scheme = new Scheme("https", sslSocketFactory, 443);
        httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
    }

    public void configRequestExecutionRetryCount(int count)
    {
        httpClient.setHttpRequestRetryHandler(new RetryHandler(count));
    }

    public void addHeader(String header, String value)
    {
        clientHeaderMap.put(header, value);
    }

    public void get(String url, AjaxCallBack callBack)
    {
        get(url, null, callBack);
    }

    public void get(String url, AjaxParams params, AjaxCallBack callBack)
    {
        sendRequest(httpClient, httpContext, new HttpGet(getUrlWithQueryString(url, params)), null, callBack);
    }

    public void get(String url, Header headers[], AjaxParams params, AjaxCallBack callBack)
    {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if(headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, null, callBack);
    }

    public Object getSync(String url)
    {
        return getSync(url, null);
    }

    public Object getSync(String url, AjaxParams params)
    {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        return sendSyncRequest(httpClient, httpContext, request, null);
    }

    public Object getSync(String url, Header headers[], AjaxParams params)
    {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if(headers != null)
            request.setHeaders(headers);
        return sendSyncRequest(httpClient, httpContext, request, null);
    }

    public void post(String url, AjaxCallBack callBack)
    {
        post(url, null, callBack);
    }

    public void post(String url, AjaxParams params, AjaxCallBack callBack)
    {
        post(url, paramsToEntity(params), null, callBack);
    }

    public void post(String url, HttpEntity entity, String contentType, AjaxCallBack callBack)
    {
        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, callBack);
    }

    public void post(String url, Header headers[], AjaxParams params, String contentType, AjaxCallBack callBack)
    {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if(params != null)
            request.setEntity(paramsToEntity(params));
        if(headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, callBack);
    }

    public void post(String url, Header headers[], HttpEntity entity, String contentType, AjaxCallBack callBack)
    {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if(headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, callBack);
    }

    public Object postSync(String url)
    {
        return postSync(url, null);
    }

    public Object postSync(String url, AjaxParams params)
    {
        return postSync(url, paramsToEntity(params), null);
    }

    public Object postSync(String url, HttpEntity entity, String contentType)
    {
        return sendSyncRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType);
    }

    public Object postSync(String url, Header headers[], AjaxParams params, String contentType)
    {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if(params != null)
            request.setEntity(paramsToEntity(params));
        if(headers != null)
            request.setHeaders(headers);
        return sendSyncRequest(httpClient, httpContext, request, contentType);
    }

    public Object postSync(String url, Header headers[], HttpEntity entity, String contentType)
    {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if(headers != null)
            request.setHeaders(headers);
        return sendSyncRequest(httpClient, httpContext, request, contentType);
    }

    public void put(String url, AjaxCallBack callBack)
    {
        put(url, null, callBack);
    }

    public void put(String url, AjaxParams params, AjaxCallBack callBack)
    {
        put(url, paramsToEntity(params), null, callBack);
    }

    public void put(String url, HttpEntity entity, String contentType, AjaxCallBack callBack)
    {
        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPut(url), entity), contentType, callBack);
    }

    public void put(String url, Header headers[], HttpEntity entity, String contentType, AjaxCallBack callBack)
    {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
        if(headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, callBack);
    }

    public Object putSync(String url)
    {
        return putSync(url, null);
    }

    public Object putSync(String url, AjaxParams params)
    {
        return putSync(url, paramsToEntity(params), null);
    }

    public Object putSync(String url, HttpEntity entity, String contentType)
    {
        return putSync(url, null, entity, contentType);
    }

    public Object putSync(String url, Header headers[], HttpEntity entity, String contentType)
    {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
        if(headers != null)
            request.setHeaders(headers);
        return sendSyncRequest(httpClient, httpContext, request, contentType);
    }

    public void delete(String url, AjaxCallBack callBack)
    {
        HttpDelete delete = new HttpDelete(url);
        sendRequest(httpClient, httpContext, delete, null, callBack);
    }

    public void delete(String url, Header headers[], AjaxCallBack callBack)
    {
        HttpDelete delete = new HttpDelete(url);
        if(headers != null)
            delete.setHeaders(headers);
        sendRequest(httpClient, httpContext, delete, null, callBack);
    }

    public Object deleteSync(String url)
    {
        return deleteSync(url, null);
    }

    public Object deleteSync(String url, Header headers[])
    {
        HttpDelete delete = new HttpDelete(url);
        if(headers != null)
            delete.setHeaders(headers);
        return sendSyncRequest(httpClient, httpContext, delete, null);
    }

    public HttpHandler download(String url, String target, AjaxCallBack callback)
    {
        return download(url, null, target, false, callback);
    }

    public HttpHandler download(String url, String target, boolean isResume, AjaxCallBack callback)
    {
        return download(url, null, target, isResume, callback);
    }

    public HttpHandler download(String url, AjaxParams params, String target, AjaxCallBack callback)
    {
        return download(url, params, target, false, callback);
    }

    public HttpHandler download(String url, AjaxParams params, String target, boolean isResume, AjaxCallBack callback)
    {
        HttpGet get = new HttpGet(getUrlWithQueryString(url, params));
        HttpHandler handler = new HttpHandler(httpClient, httpContext, callback, charset);
        handler.executeOnExecutor(executor, new Object[] {
            get, target, Boolean.valueOf(isResume)
        });
        return handler;
    }

    protected void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, AjaxCallBack ajaxCallBack)
    {
        if(contentType != null)
            uriRequest.addHeader("Content-Type", contentType);
        (new HttpHandler(client, httpContext, ajaxCallBack, charset)).executeOnExecutor(executor, new Object[] {
            uriRequest
        });
    }

    protected Object sendSyncRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType)
    {
        if(contentType != null)
            uriRequest.addHeader("Content-Type", contentType);
        return (new SyncRequestHandler(client, httpContext, charset)).sendRequest(new HttpUriRequest[] {
            uriRequest
        });
    }

    public static String getUrlWithQueryString(String url, AjaxParams params)
    {
        if(params != null)
        {
            String paramString = params.getParamString();
            url = (new StringBuilder(String.valueOf(url))).append("?").append(paramString).toString();
        }
        return url;
    }

    private HttpEntity paramsToEntity(AjaxParams params)
    {
        HttpEntity entity = null;
        if(params != null)
            entity = params.getEntity();
        return entity;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity)
    {
        if(entity != null)
            requestBase.setEntity(entity);
        return requestBase;
    }

    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";
    private static int maxConnections = 10;
    private static int socketTimeout = 10000;
    private static int maxRetries = 5;
    private static int httpThreadCount;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
    private String charset;
    private final Map clientHeaderMap = new HashMap();
    private static final ThreadFactory sThreadFactory;
    private static final Executor executor;

    static 
    {
        httpThreadCount = 3;
        sThreadFactory = new ThreadFactory() {

            public Thread newThread(Runnable r)
            {
                Thread tread = new Thread(r, (new StringBuilder("FinalHttp #")).append(mCount.getAndIncrement()).toString());
                tread.setPriority(4);
                return tread;
            }

            private final AtomicInteger mCount = new AtomicInteger(1);

        }
;
        executor = Executors.newFixedThreadPool(httpThreadCount, sThreadFactory);
    }

}
