// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CursorUtils.java

package net.tsz.afinal.db.sqlite;

import android.database.Cursor;
import java.util.*;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.table.*;

// Referenced classes of package net.tsz.afinal.db.sqlite:
//            OneToManyLazyLoader, ManyToOneLazyLoader, DbModel

public class CursorUtils
{

    public CursorUtils()
    {
    }

    public static Object getEntity(Cursor cursor, Class clazz, FinalDb db)
    {
        try
        {
            if(cursor != null)
            {
                TableInfo table = TableInfo.get(clazz);
                int columnCount = cursor.getColumnCount();
                if(columnCount > 0)
                {
                    Object entity = clazz.newInstance();
                    for(int i = 0; i < columnCount; i++)
                    {
                        String column = cursor.getColumnName(i);
                        Property property = (Property)table.propertyMap.get(column);
                        if(property != null)
                            property.setValue(entity, cursor.getString(i));
                        else
                        if(table.getId().getColumn().equals(column))
                            table.getId().setValue(entity, cursor.getString(i));
                    }

                    for(Iterator iterator = table.oneToManyMap.values().iterator(); iterator.hasNext();)
                    {
                        OneToMany oneToManyProp = (OneToMany)iterator.next();
                        if(oneToManyProp.getDataType() == net/tsz/afinal/db/sqlite/OneToManyLazyLoader)
                        {
                            OneToManyLazyLoader oneToManyLazyLoader = new OneToManyLazyLoader(entity, clazz, oneToManyProp.getOneClass(), db);
                            oneToManyProp.setValue(entity, oneToManyLazyLoader);
                        }
                    }

                    for(Iterator iterator1 = table.manyToOneMap.values().iterator(); iterator1.hasNext();)
                    {
                        ManyToOne manyToOneProp = (ManyToOne)iterator1.next();
                        if(manyToOneProp.getDataType() == net/tsz/afinal/db/sqlite/ManyToOneLazyLoader)
                        {
                            ManyToOneLazyLoader manyToOneLazyLoader = new ManyToOneLazyLoader(entity, clazz, manyToOneProp.getManyClass(), db);
                            manyToOneProp.setValue(entity, manyToOneLazyLoader);
                        }
                    }

                    return entity;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static DbModel getDbModel(Cursor cursor)
    {
        if(cursor != null && cursor.getColumnCount() > 0)
        {
            DbModel model = new DbModel();
            int columnCount = cursor.getColumnCount();
            for(int i = 0; i < columnCount; i++)
                model.set(cursor.getColumnName(i), cursor.getString(i));

            return model;
        } else
        {
            return null;
        }
    }

    public static Object dbModel2Entity(DbModel dbModel, Class clazz)
    {
        if(dbModel != null)
        {
            HashMap dataMap = dbModel.getDataMap();
            try
            {
                Object entity = clazz.newInstance();
                for(Iterator iterator = dataMap.entrySet().iterator(); iterator.hasNext();)
                {
                    java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                    String column = (String)entry.getKey();
                    TableInfo table = TableInfo.get(clazz);
                    Property property = (Property)table.propertyMap.get(column);
                    if(property != null)
                        property.setValue(entity, entry.getValue() != null ? ((Object) (entry.getValue().toString())) : null);
                    else
                    if(table.getId().getColumn().equals(column))
                        table.getId().setValue(entity, entry.getValue() != null ? ((Object) (entry.getValue().toString())) : null);
                }

                return entity;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
