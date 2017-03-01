// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   KeyValue.java

package net.tsz.afinal.db.table;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyValue
{

    public KeyValue(String key, Object value)
    {
        this.key = key;
        this.value = value;
    }

    public KeyValue()
    {
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public Object getValue()
    {
        if((value instanceof Date) || (value instanceof java.sql.Date))
            return sdf.format(value);
        else
            return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    private String key;
    private Object value;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
