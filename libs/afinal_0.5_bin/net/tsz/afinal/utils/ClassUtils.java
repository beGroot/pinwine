// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClassUtils.java

package net.tsz.afinal.utils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.db.sqlite.ManyToOneLazyLoader;
import net.tsz.afinal.db.table.*;
import net.tsz.afinal.exception.DbException;

// Referenced classes of package net.tsz.afinal.utils:
//            FieldUtils

public class ClassUtils
{

    public ClassUtils()
    {
    }

    public static String getTableName(Class clazz)
    {
        Table table = (Table)clazz.getAnnotation(net/tsz/afinal/annotation/sqlite/Table);
        if(table == null || table.name().trim().length() == 0)
            return clazz.getName().replace('.', '_');
        else
            return table.name();
    }

    public static Object getPrimaryKeyValue(Object entity)
    {
        return FieldUtils.getFieldValue(entity, getPrimaryKeyField(entity.getClass()));
    }

    public static String getPrimaryKeyColumn(Class clazz)
    {
        String primaryKey = null;
        Field fields[] = clazz.getDeclaredFields();
        if(fields != null)
        {
            Id idAnnotation = null;
            Field idField = null;
            Field afield[];
            int l = (afield = fields).length;
            for(int i = 0; i < l; i++)
            {
                Field field = afield[i];
                idAnnotation = (Id)field.getAnnotation(net/tsz/afinal/annotation/sqlite/Id);
                if(idAnnotation == null)
                    continue;
                idField = field;
                break;
            }

            if(idAnnotation != null)
            {
                primaryKey = idAnnotation.column();
                if(primaryKey == null || primaryKey.trim().length() == 0)
                    primaryKey = idField.getName();
            } else
            {
                Field afield1[];
                int i1 = (afield1 = fields).length;
                for(int j = 0; j < i1; j++)
                {
                    Field field = afield1[j];
                    if("_id".equals(field.getName()))
                        return "_id";
                }

                i1 = (afield1 = fields).length;
                for(int k = 0; k < i1; k++)
                {
                    Field field = afield1[k];
                    if("id".equals(field.getName()))
                        return "id";
                }

            }
        } else
        {
            throw new RuntimeException((new StringBuilder("this model[")).append(clazz).append("] has no field").toString());
        }
        return primaryKey;
    }

    public static Field getPrimaryKeyField(Class clazz)
    {
        Field primaryKeyField = null;
        Field fields[] = clazz.getDeclaredFields();
        if(fields != null)
        {
            Field afield[];
            int l = (afield = fields).length;
            for(int i = 0; i < l; i++)
            {
                Field field = afield[i];
                if(field.getAnnotation(net/tsz/afinal/annotation/sqlite/Id) == null)
                    continue;
                primaryKeyField = field;
                break;
            }

            if(primaryKeyField == null)
            {
                Field afield1[];
                int i1 = (afield1 = fields).length;
                for(int j = 0; j < i1; j++)
                {
                    Field field = afield1[j];
                    if(!"_id".equals(field.getName()))
                        continue;
                    primaryKeyField = field;
                    break;
                }

            }
            if(primaryKeyField == null)
            {
                Field afield2[];
                int j1 = (afield2 = fields).length;
                for(int k = 0; k < j1; k++)
                {
                    Field field = afield2[k];
                    if(!"id".equals(field.getName()))
                        continue;
                    primaryKeyField = field;
                    break;
                }

            }
        } else
        {
            throw new RuntimeException((new StringBuilder("this model[")).append(clazz).append("] has no field").toString());
        }
        return primaryKeyField;
    }

    public static String getPrimaryKeyFieldName(Class clazz)
    {
        Field f = getPrimaryKeyField(clazz);
        return f != null ? f.getName() : null;
    }

    public static List getPropertyList(Class clazz)
    {
        List plist = new ArrayList();
        try
        {
            Field fs[] = clazz.getDeclaredFields();
            String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
            Field afield[];
            int j = (afield = fs).length;
            for(int i = 0; i < j; i++)
            {
                Field f = afield[i];
                if(!FieldUtils.isTransient(f) && FieldUtils.isBaseDateType(f) && !f.getName().equals(primaryKeyFieldName))
                {
                    Property property = new Property();
                    property.setColumn(FieldUtils.getColumnByField(f));
                    property.setFieldName(f.getName());
                    property.setDataType(f.getType());
                    property.setDefaultValue(FieldUtils.getPropertyDefaultValue(f));
                    property.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    property.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                    property.setField(f);
                    plist.add(property);
                }
            }

            return plist;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List getManyToOneList(Class clazz)
    {
        List mList = new ArrayList();
        try
        {
            Field fs[] = clazz.getDeclaredFields();
            Field afield[];
            int j = (afield = fs).length;
            for(int i = 0; i < j; i++)
            {
                Field f = afield[i];
                if(!FieldUtils.isTransient(f) && FieldUtils.isManyToOne(f))
                {
                    ManyToOne mto = new ManyToOne();
                    if(f.getType() == net/tsz/afinal/db/sqlite/ManyToOneLazyLoader)
                    {
                        Class pClazz = (Class)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[1];
                        if(pClazz != null)
                            mto.setManyClass(pClazz);
                    } else
                    {
                        mto.setManyClass(f.getType());
                    }
                    mto.setColumn(FieldUtils.getColumnByField(f));
                    mto.setFieldName(f.getName());
                    mto.setDataType(f.getType());
                    mto.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    mto.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                    mList.add(mto);
                }
            }

            return mList;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List getOneToManyList(Class clazz)
    {
        List oList = new ArrayList();
        try
        {
            Field fs[] = clazz.getDeclaredFields();
            Field afield[];
            int j = (afield = fs).length;
            for(int i = 0; i < j; i++)
            {
                Field f = afield[i];
                if(!FieldUtils.isTransient(f) && FieldUtils.isOneToMany(f))
                {
                    OneToMany otm = new OneToMany();
                    otm.setColumn(FieldUtils.getColumnByField(f));
                    otm.setFieldName(f.getName());
                    Type type = f.getGenericType();
                    if(type instanceof ParameterizedType)
                    {
                        ParameterizedType pType = (ParameterizedType)f.getGenericType();
                        if(pType.getActualTypeArguments().length == 1)
                        {
                            Class pClazz = (Class)pType.getActualTypeArguments()[0];
                            if(pClazz != null)
                                otm.setOneClass(pClazz);
                        } else
                        {
                            Class pClazz = (Class)pType.getActualTypeArguments()[1];
                            if(pClazz != null)
                                otm.setOneClass(pClazz);
                        }
                    } else
                    {
                        throw new DbException((new StringBuilder("getOneToManyList Exception:")).append(f.getName()).append("'s type is null").toString());
                    }
                    otm.setDataType(f.getType());
                    otm.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    otm.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                    oList.add(otm);
                }
            }

            return oList;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
