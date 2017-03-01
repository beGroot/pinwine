// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Property.java

package net.tsz.afinal.db.table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Property
{

    public Property()
    {
    }

    public void setValue(Object receiver, Object value)
    {
        if(set != null && value != null)
            try
            {
                if(dataType == java/lang/String)
                    set.invoke(receiver, new Object[] {
                        value.toString()
                    });
                else
                if(dataType == Integer.TYPE || dataType == java/lang/Integer)
                    set.invoke(receiver, new Object[] {
                        Integer.valueOf(value != null ? Integer.parseInt(value.toString()) : null.intValue())
                    });
                else
                if(dataType == Float.TYPE || dataType == java/lang/Float)
                    set.invoke(receiver, new Object[] {
                        Float.valueOf(value != null ? Float.parseFloat(value.toString()) : null.floatValue())
                    });
                else
                if(dataType == Double.TYPE || dataType == java/lang/Double)
                    set.invoke(receiver, new Object[] {
                        Double.valueOf(value != null ? Double.parseDouble(value.toString()) : null.doubleValue())
                    });
                else
                if(dataType == Long.TYPE || dataType == java/lang/Long)
                    set.invoke(receiver, new Object[] {
                        Long.valueOf(value != null ? Long.parseLong(value.toString()) : null.longValue())
                    });
                else
                if(dataType == java/util/Date || dataType == java/sql/Date)
                    set.invoke(receiver, new Object[] {
                        value != null ? stringToDateTime(value.toString()) : null
                    });
                else
                if(dataType == Boolean.TYPE || dataType == java/lang/Boolean)
                    set.invoke(receiver, new Object[] {
                        Boolean.valueOf(value != null ? "1".equals(value.toString()) : null.booleanValue())
                    });
                else
                    set.invoke(receiver, new Object[] {
                        value
                    });
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        else
            try
            {
                field.setAccessible(true);
                field.set(receiver, value);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }

    public Object getValue(Object obj)
    {
        if(obj != null && get != null)
            try
            {
                return get.invoke(obj, new Object[0]);
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

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public String getColumn()
    {
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public Class getDataType()
    {
        return dataType;
    }

    public void setDataType(Class dataType)
    {
        this.dataType = dataType;
    }

    public Method getGet()
    {
        return get;
    }

    public void setGet(Method get)
    {
        this.get = get;
    }

    public Method getSet()
    {
        return set;
    }

    public void setSet(Method set)
    {
        this.set = set;
    }

    public Field getField()
    {
        return field;
    }

    public void setField(Field field)
    {
        this.field = field;
    }

    private String fieldName;
    private String column;
    private String defaultValue;
    private Class dataType;
    private Field field;
    private Method get;
    private Method set;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
