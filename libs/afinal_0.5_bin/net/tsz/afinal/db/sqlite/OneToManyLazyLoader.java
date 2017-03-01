// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OneToManyLazyLoader.java

package net.tsz.afinal.db.sqlite;

import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.FinalDb;

public class OneToManyLazyLoader
{

    public OneToManyLazyLoader(Object ownerEntity, Class ownerClazz, Class listItemclazz, FinalDb db)
    {
        this.ownerEntity = ownerEntity;
        this.ownerClazz = ownerClazz;
        listItemClazz = listItemclazz;
        this.db = db;
    }

    public List getList()
    {
        if(entities == null)
            db.loadOneToMany(ownerEntity, ownerClazz, new Class[] {
                listItemClazz
            });
        if(entities == null)
            entities = new ArrayList();
        return entities;
    }

    public void setList(List value)
    {
        entities = value;
    }

    Object ownerEntity;
    Class ownerClazz;
    Class listItemClazz;
    FinalDb db;
    List entities;
}
