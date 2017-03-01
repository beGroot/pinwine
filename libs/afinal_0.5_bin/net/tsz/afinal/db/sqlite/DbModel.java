// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DbModel.java

package net.tsz.afinal.db.sqlite;

import java.util.HashMap;

public class DbModel
{

    public DbModel()
    {
        dataMap = new HashMap();
    }

    public Object get(String column)
    {
        return dataMap.get(column);
    }

    public String getString(String column)
    {
        return String.valueOf(get(column));
    }

    public int getInt(String column)
    {
        return Integer.valueOf(getString(column)).intValue();
    }

    public boolean getBoolean(String column)
    {
        return Boolean.valueOf(getString(column)).booleanValue();
    }

    public double getDouble(String column)
    {
        return Double.valueOf(getString(column)).doubleValue();
    }

    public float getFloat(String column)
    {
        return Float.valueOf(getString(column)).floatValue();
    }

    public long getLong(String column)
    {
        return Long.valueOf(getString(column)).longValue();
    }

    public void set(String key, Object value)
    {
        dataMap.put(key, value);
    }

    public HashMap getDataMap()
    {
        return dataMap;
    }

    private HashMap dataMap;
}
