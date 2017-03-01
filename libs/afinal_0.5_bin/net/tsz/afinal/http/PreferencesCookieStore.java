// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PreferencesCookieStore.java

package net.tsz.afinal.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

public class PreferencesCookieStore
    implements CookieStore
{
    public class SerializableCookie
        implements Serializable
    {

        public Cookie getCookie()
        {
            Cookie bestCookie = cookie;
            if(clientCookie != null)
                bestCookie = clientCookie;
            return bestCookie;
        }

        private void writeObject(ObjectOutputStream out)
            throws IOException
        {
            out.writeObject(cookie.getName());
            out.writeObject(cookie.getValue());
            out.writeObject(cookie.getComment());
            out.writeObject(cookie.getDomain());
            out.writeObject(cookie.getExpiryDate());
            out.writeObject(cookie.getPath());
            out.writeInt(cookie.getVersion());
            out.writeBoolean(cookie.isSecure());
        }

        private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException
        {
            String name = (String)in.readObject();
            String value = (String)in.readObject();
            clientCookie = new BasicClientCookie(name, value);
            clientCookie.setComment((String)in.readObject());
            clientCookie.setDomain((String)in.readObject());
            clientCookie.setExpiryDate((Date)in.readObject());
            clientCookie.setPath((String)in.readObject());
            clientCookie.setVersion(in.readInt());
            clientCookie.setSecure(in.readBoolean());
        }

        private static final long serialVersionUID = 0x58765a8013aeb70cL;
        private final transient Cookie cookie;
        private transient BasicClientCookie clientCookie;
        final PreferencesCookieStore this$0;

        public SerializableCookie(Cookie cookie)
        {
            this$0 = PreferencesCookieStore.this;
            super();
            this.cookie = cookie;
        }
    }


    public PreferencesCookieStore(Context context)
    {
        cookiePrefs = context.getSharedPreferences("CookiePrefsFile", 0);
        String storedCookieNames = cookiePrefs.getString("names", null);
        if(storedCookieNames != null)
        {
            String cookieNames[] = TextUtils.split(storedCookieNames, ",");
            String as[];
            int j = (as = cookieNames).length;
            for(int i = 0; i < j; i++)
            {
                String name = as[i];
                String encodedCookie = cookiePrefs.getString((new StringBuilder("cookie_")).append(name).toString(), null);
                if(encodedCookie != null)
                {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if(decodedCookie != null)
                        cookies.put(name, decodedCookie);
                }
            }

            clearExpired(new Date());
        }
    }

    public void addCookie(Cookie cookie)
    {
        String name = cookie.getName();
        if(!cookie.isExpired(new Date()))
            cookies.put(name, cookie);
        else
            cookies.remove(name);
        android.content.SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString("names", TextUtils.join(",", cookies.keySet()));
        prefsWriter.putString((new StringBuilder("cookie_")).append(name).toString(), encodeCookie(new SerializableCookie(cookie)));
        prefsWriter.commit();
    }

    public void clear()
    {
        cookies.clear();
        android.content.SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        String name;
        for(Iterator iterator = cookies.keySet().iterator(); iterator.hasNext(); prefsWriter.remove((new StringBuilder("cookie_")).append(name).toString()))
            name = (String)iterator.next();

        prefsWriter.remove("names");
        prefsWriter.commit();
    }

    public boolean clearExpired(Date date)
    {
        boolean clearedAny = false;
        android.content.SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        for(Iterator iterator = cookies.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String name = (String)entry.getKey();
            Cookie cookie = (Cookie)entry.getValue();
            if(cookie.isExpired(date))
            {
                cookies.remove(name);
                prefsWriter.remove((new StringBuilder("cookie_")).append(name).toString());
                clearedAny = true;
            }
        }

        if(clearedAny)
            prefsWriter.putString("names", TextUtils.join(",", cookies.keySet()));
        prefsWriter.commit();
        return clearedAny;
    }

    public List getCookies()
    {
        return new ArrayList(cookies.values());
    }

    protected String encodeCookie(SerializableCookie cookie)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        }
        catch(Exception e)
        {
            return null;
        }
        return byteArrayToHexString(os.toByteArray());
    }

    protected Cookie decodeCookie(String cookieStr)
    {
        byte bytes[] = hexStringToByteArray(cookieStr);
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try
        {
            ObjectInputStream ois = new ObjectInputStream(is);
            cookie = ((SerializableCookie)ois.readObject()).getCookie();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return cookie;
    }

    protected String byteArrayToHexString(byte b[])
    {
        StringBuffer sb = new StringBuffer(b.length * 2);
        byte abyte0[];
        int j = (abyte0 = b).length;
        for(int i = 0; i < j; i++)
        {
            byte element = abyte0[i];
            int v = element & 0xff;
            if(v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }

        return sb.toString().toUpperCase();
    }

    protected byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte data[] = new byte[len / 2];
        for(int i = 0; i < len; i += 2)
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));

        return data;
    }

    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private final ConcurrentHashMap cookies = new ConcurrentHashMap();
    private final SharedPreferences cookiePrefs;
}
