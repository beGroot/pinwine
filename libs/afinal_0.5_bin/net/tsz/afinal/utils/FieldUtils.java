// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FieldUtils.java

package net.tsz.afinal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.ManyToOne;
import net.tsz.afinal.annotation.sqlite.OneToMany;
import net.tsz.afinal.annotation.sqlite.Property;
import net.tsz.afinal.annotation.sqlite.Transient;

// Referenced classes of package net.tsz.afinal.utils:
//            ClassUtils

public class FieldUtils
{

    public FieldUtils()
    {
    }

    public static Method getFieldGetMethod(Class clazz, Field f)
    {
        String fn = f.getName();
        Method m = null;
        if(f.getType() == Boolean.TYPE)
            m = getBooleanFieldGetMethod(clazz, fn);
        if(m == null)
            m = getFieldGetMethod(clazz, fn);
        return m;
    }

    public static Method getBooleanFieldGetMethod(Class clazz, String fieldName)
    {
        String mn = (new StringBuilder("is")).append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).toString();
        if(isISStart(fieldName))
            mn = fieldName;
        try
        {
            return clazz.getDeclaredMethod(mn, new Class[0]);
        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getBooleanFieldSetMethod(Class clazz, Field f)
    {
        String fn = f.getName();
        String mn = (new StringBuilder("set")).append(fn.substring(0, 1).toUpperCase()).append(fn.substring(1)).toString();
        if(isISStart(f.getName()))
            mn = (new StringBuilder("set")).append(fn.substring(2, 3).toUpperCase()).append(fn.substring(3)).toString();
        try
        {
            return clazz.getDeclaredMethod(mn, new Class[] {
                f.getType()
            });
        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isISStart(String fieldName)
    {
        if(fieldName == null || fieldName.trim().length() == 0)
            return false;
        return fieldName.startsWith("is") && !Character.isLowerCase(fieldName.charAt(2));
    }

    public static Method getFieldGetMethod(Class clazz, String fieldName)
    {
        String mn = (new StringBuilder("get")).append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).toString();
        try
        {
            return clazz.getDeclaredMethod(mn, new Class[0]);
        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getFieldSetMethod(Class clazz, Field f)
    {
        String fn = f.getName();
        String mn = (new StringBuilder("set")).append(fn.substring(0, 1).toUpperCase()).append(fn.substring(1)).toString();
        try
        {
            return clazz.getDeclaredMethod(mn, new Class[] {
                f.getType()
            });
        }
        catch(NoSuchMethodException e) { }
        if(f.getType() == Boolean.TYPE)
            return getBooleanFieldSetMethod(clazz, f);
        else
            return null;
    }

    public static Method getFieldSetMethod(Class clazz, String fieldName)
    {
        try
        {
            return getFieldSetMethod(clazz, clazz.getDeclaredField(fieldName));
        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldValue(Object entity, Field field)
    {
        Method method = getFieldGetMethod(entity.getClass(), field);
        return invoke(entity, method);
    }

    public static Object getFieldValue(Object entity, String fieldName)
    {
        Method method = getFieldGetMethod(entity.getClass(), fieldName);
        return invoke(entity, method);
    }

    public static void setFieldValue(Object entity, Field field, Object value)
    {
        try
        {
            Method set = getFieldSetMethod(entity.getClass(), field);
            if(set != null)
            {
                set.setAccessible(true);
                Class type = field.getType();
                if(type == java/lang/String)
                    set.invoke(entity, new Object[] {
                        value.toString()
                    });
                else
                if(type == Integer.TYPE || type == java/lang/Integer)
                    set.invoke(entity, new Object[] {
                        Integer.valueOf(value != null ? Integer.parseInt(value.toString()) : null.intValue())
                    });
                else
                if(type == Float.TYPE || type == java/lang/Float)
                    set.invoke(entity, new Object[] {
                        Float.valueOf(value != null ? Float.parseFloat(value.toString()) : null.floatValue())
                    });
                else
                if(type == Long.TYPE || type == java/lang/Long)
                    set.invoke(entity, new Object[] {
                        Long.valueOf(value != null ? Long.parseLong(value.toString()) : null.longValue())
                    });
                else
                if(type == java/util/Date)
                    set.invoke(entity, new Object[] {
                        value != null ? stringToDateTime(value.toString()) : null
                    });
                else
                    set.invoke(entity, new Object[] {
                        value
                    });
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Field getFieldByColumnName(Class clazz, String columnName)
    {
        Field field = null;
        if(columnName != null)
        {
            Field fields[] = clazz.getDeclaredFields();
            if(fields != null && fields.length > 0)
            {
                if(columnName.equals(ClassUtils.getPrimaryKeyColumn(clazz)))
                    field = ClassUtils.getPrimaryKeyField(clazz);
                if(field == null)
                {
                    Field afield[];
                    int j = (afield = fields).length;
                    for(int i = 0; i < j; i++)
                    {
                        Field f = afield[i];
                        Property property = (Property)f.getAnnotation(net/tsz/afinal/annotation/sqlite/Property);
                        if(property != null && columnName.equals(property.column()))
                        {
                            field = f;
                            break;
                        }
                        ManyToOne manyToOne = (ManyToOne)f.getAnnotation(net/tsz/afinal/annotation/sqlite/ManyToOne);
                        if(manyToOne == null || manyToOne.column().trim().length() == 0)
                            continue;
                        field = f;
                        break;
                    }

                }
                if(field == null)
                    field = getFieldByName(clazz, columnName);
            }
        }
        return field;
    }

    public static Field getFieldByName(Class clazz, String fieldName)
    {
        Field field = null;
        if(fieldName != null)
            try
            {
                field = clazz.getDeclaredField(fieldName);
            }
            catch(SecurityException e)
            {
                e.printStackTrace();
            }
            catch(NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        return field;
    }

    public static String getColumnByField(Field field)
    {
        Property property = (Property)field.getAnnotation(net/tsz/afinal/annotation/sqlite/Property);
        if(property != null && property.column().trim().length() != 0)
            return property.column();
        ManyToOne manyToOne = (ManyToOne)field.getAnnotation(net/tsz/afinal/annotation/sqlite/ManyToOne);
        if(manyToOne != null && manyToOne.column().trim().length() != 0)
            return manyToOne.column();
        OneToMany oneToMany = (OneToMany)field.getAnnotation(net/tsz/afinal/annotation/sqlite/OneToMany);
        if(oneToMany != null && oneToMany.manyColumn() != null && oneToMany.manyColumn().trim().length() != 0)
            return oneToMany.manyColumn();
        Id id = (Id)field.getAnnotation(net/tsz/afinal/annotation/sqlite/Id);
        if(id != null && id.column().trim().length() != 0)
            return id.column();
        else
            return field.getName();
    }

    public static String getPropertyDefaultValue(Field field)
    {
        Property property = (Property)field.getAnnotation(net/tsz/afinal/annotation/sqlite/Property);
        if(property != null && property.defaultValue().trim().length() != 0)
            return property.defaultValue();
        else
            return null;
    }

    public static boolean isTransient(Field f)
    {
        return f.getAnnotation(net/tsz/afinal/annotation/sqlite/Transient) != null;
    }

    private static Object invoke(Object obj, Method method)
    {
        if(obj == null || method == null)
            return null;
        try
        {
            return method.invoke(obj, new Object[0]);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isManyToOne(Field field)
    {
        return field.getAnnotation(net/tsz/afinal/annotation/sqlite/ManyToOne) != null;
    }

    public static boolean isOneToMany(Field field)
    {
        return field.getAnnotation(net/tsz/afinal/annotation/sqlite/OneToMany) != null;
    }

    public static boolean isManyToOneOrOneToMany(Field field)
    {
        return isManyToOne(field) || isOneToMany(field);
    }

    public static boolean isBaseDateType(Field field)
    {
        Class clazz = field.getType();
        return clazz.equals(java/lang/String) || clazz.equals(java/lang/Integer) || clazz.equals(java/lang/Byte) || clazz.equals(java/lang/Long) || clazz.equals(java/lang/Double) || clazz.equals(java/lang/Float) || clazz.equals(java/lang/Character) || clazz.equals(java/lang/Short) || clazz.equals(java/lang/Boolean) || clazz.equals(java/util/Date) || clazz.equals(java/util/Date) || clazz.equals(java/sql/Date) || clazz.isPrimitive();
    }

    private static Date stringToDateTime(String strDate)
    {
        if(strDate != null)
            try
            {
                return sdf.parse(strDate);
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }
        return null;
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
