// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlInfo.java

package net.tsz.afinal.db.sqlite;

import java.util.LinkedList;

public class SqlInfo
{

    public SqlInfo()
    {
    }

    public String getSql()
    {
        return sql;
    }

    public void setSql(String sql)
    {
        this.sql = sql;
    }

    public LinkedList getBindArgs()
    {
        return bindArgs;
    }

    public void setBindArgs(LinkedList bindArgs)
    {
        this.bindArgs = bindArgs;
    }

    public Object[] getBindArgsAsArray()
    {
        if(bindArgs != null)
            return bindArgs.toArray();
        else
            return null;
    }

    public String[] getBindArgsAsStringArray()
    {
        if(bindArgs != null)
        {
            String strings[] = new String[bindArgs.size()];
            for(int i = 0; i < bindArgs.size(); i++)
                strings[i] = bindArgs.get(i).toString();

            return strings;
        } else
        {
            return null;
        }
    }

    public void addValue(Object obj)
    {
        if(bindArgs == null)
            bindArgs = new LinkedList();
        bindArgs.add(obj);
    }

    private String sql;
    private LinkedList bindArgs;
}
