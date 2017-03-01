// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TableInfo.java

package net.tsz.afinal.db.table;

import java.lang.reflect.Field;
import java.util.*;
import net.tsz.afinal.exception.DbException;
import net.tsz.afinal.utils.ClassUtils;
import net.tsz.afinal.utils.FieldUtils;

// Referenced classes of package net.tsz.afinal.db.table:
//            Id, Property, ManyToOne, OneToMany

public class TableInfo
{

    private TableInfo()
    {
    }

    public static TableInfo get(Class clazz)
    {
        if(clazz == null)
            throw new DbException("table info get error,because the clazz is null");
        TableInfo tableInfo = (TableInfo)tableInfoMap.get(clazz.getName());
        if(tableInfo == null)
        {
            tableInfo = new TableInfo();
            tableInfo.setTableName(ClassUtils.getTableName(clazz));
            tableInfo.setClassName(clazz.getName());
            Field idField = ClassUtils.getPrimaryKeyField(clazz);
            if(idField != null)
            {
                Id id = new Id();
                id.setColumn(FieldUtils.getColumnByField(idField));
                id.setFieldName(idField.getName());
                id.setSet(FieldUtils.getFieldSetMethod(clazz, idField));
                id.setGet(FieldUtils.getFieldGetMethod(clazz, idField));
                id.setDataType(idField.getType());
                tableInfo.setId(id);
            } else
            {
                throw new DbException((new StringBuilder("the class[")).append(clazz).append("]'s idField is null , \n you can define _id,id property or use annotation @id to solution this exception").toString());
            }
            List pList = ClassUtils.getPropertyList(clazz);
            if(pList != null)
            {
                for(Iterator iterator = pList.iterator(); iterator.hasNext();)
                {
                    Property p = (Property)iterator.next();
                    if(p != null)
                        tableInfo.propertyMap.put(p.getColumn(), p);
                }

            }
            List mList = ClassUtils.getManyToOneList(clazz);
            if(mList != null)
            {
                for(Iterator iterator1 = mList.iterator(); iterator1.hasNext();)
                {
                    ManyToOne m = (ManyToOne)iterator1.next();
                    if(m != null)
                        tableInfo.manyToOneMap.put(m.getColumn(), m);
                }

            }
            List oList = ClassUtils.getOneToManyList(clazz);
            if(oList != null)
            {
                for(Iterator iterator2 = oList.iterator(); iterator2.hasNext();)
                {
                    OneToMany o = (OneToMany)iterator2.next();
                    if(o != null)
                        tableInfo.oneToManyMap.put(o.getColumn(), o);
                }

            }
            tableInfoMap.put(clazz.getName(), tableInfo);
        }
        if(tableInfo == null)
            throw new DbException((new StringBuilder("the class[")).append(clazz).append("]'s table is null").toString());
        else
            return tableInfo;
    }

    public static TableInfo get(String className)
    {
        try
        {
            return get(Class.forName(className));
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public Id getId()
    {
        return id;
    }

    public void setId(Id id)
    {
        this.id = id;
    }

    public boolean isCheckDatabese()
    {
        return checkDatabese;
    }

    public void setCheckDatabese(boolean checkDatabese)
    {
        this.checkDatabese = checkDatabese;
    }

    private String className;
    private String tableName;
    private Id id;
    public final HashMap propertyMap = new HashMap();
    public final HashMap oneToManyMap = new HashMap();
    public final HashMap manyToOneMap = new HashMap();
    private boolean checkDatabese;
    private static final HashMap tableInfoMap = new HashMap();

}
