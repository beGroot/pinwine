// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ManyToOneLazyLoader.java

package net.tsz.afinal.db.sqlite;

import net.tsz.afinal.FinalDb;

public class ManyToOneLazyLoader
{

    public ManyToOneLazyLoader(Object manyEntity, Class manyClazz, Class oneClazz, FinalDb db)
    {
        hasLoaded = false;
        this.manyEntity = manyEntity;
        this.manyClazz = manyClazz;
        this.oneClazz = oneClazz;
        this.db = db;
    }

    public Object get()
    {
        if(oneEntity == null && !hasLoaded)
        {
            db.loadManyToOne(manyEntity, manyClazz, new Class[] {
                oneClazz
            });
            hasLoaded = true;
        }
        return oneEntity;
    }

    public void set(Object value)
    {
        oneEntity = value;
    }

    Object manyEntity;
    Class manyClazz;
    Class oneClazz;
    FinalDb db;
    Object oneEntity;
    boolean hasLoaded;
}
