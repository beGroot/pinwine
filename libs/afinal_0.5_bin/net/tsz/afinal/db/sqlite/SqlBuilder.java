// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlBuilder.java

package net.tsz.afinal.db.sqlite;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.tsz.afinal.db.table.Id;
import net.tsz.afinal.db.table.KeyValue;
import net.tsz.afinal.db.table.ManyToOne;
import net.tsz.afinal.db.table.Property;
import net.tsz.afinal.db.table.TableInfo;
import net.tsz.afinal.exception.DbException;

// Referenced classes of package net.tsz.afinal.db.sqlite:
//            SqlInfo, ManyToOneLazyLoader

public class SqlBuilder
{

    public SqlBuilder()
    {
    }

    public static SqlInfo buildInsertSql(Object entity)
    {
        List keyValueList = getSaveKeyValueListByEntity(entity);
        StringBuffer strSQL = new StringBuffer();
        SqlInfo sqlInfo = null;
        if(keyValueList != null && keyValueList.size() > 0)
        {
            sqlInfo = new SqlInfo();
            strSQL.append("INSERT INTO ");
            strSQL.append(TableInfo.get(entity.getClass()).getTableName());
            strSQL.append(" (");
            KeyValue kv;
            for(Iterator iterator = keyValueList.iterator(); iterator.hasNext(); sqlInfo.addValue(kv.getValue()))
            {
                kv = (KeyValue)iterator.next();
                strSQL.append(kv.getKey()).append(",");
            }

            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(") VALUES ( ");
            int length = keyValueList.size();
            for(int i = 0; i < length; i++)
                strSQL.append("?,");

            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(")");
            sqlInfo.setSql(strSQL.toString());
        }
        return sqlInfo;
    }

    public static List getSaveKeyValueListByEntity(Object entity)
    {
        List keyValueList = new ArrayList();
        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);
        if(!(idvalue instanceof Integer) && (idvalue instanceof String) && idvalue != null)
        {
            KeyValue kv = new KeyValue(table.getId().getColumn(), idvalue);
            keyValueList.add(kv);
        }
        Collection propertys = table.propertyMap.values();
        for(Iterator iterator = propertys.iterator(); iterator.hasNext();)
        {
            Property property = (Property)iterator.next();
            KeyValue kv = property2KeyValue(property, entity);
            if(kv != null)
                keyValueList.add(kv);
        }

        Collection manyToOnes = table.manyToOneMap.values();
        for(Iterator iterator1 = manyToOnes.iterator(); iterator1.hasNext();)
        {
            ManyToOne many = (ManyToOne)iterator1.next();
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if(kv != null)
                keyValueList.add(kv);
        }

        return keyValueList;
    }

    private static String getDeleteSqlBytableName(String tableName)
    {
        return (new StringBuilder("DELETE FROM ")).append(tableName).toString();
    }

    public static SqlInfo buildDeleteSql(Object entity)
    {
        TableInfo table = TableInfo.get(entity.getClass());
        Id id = table.getId();
        Object idvalue = id.getValue(entity);
        if(idvalue == null)
        {
            throw new DbException((new StringBuilder("getDeleteSQL:")).append(entity.getClass()).append(" id value is null").toString());
        } else
        {
            StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
            strSQL.append(" WHERE ").append(id.getColumn()).append("=?");
            SqlInfo sqlInfo = new SqlInfo();
            sqlInfo.setSql(strSQL.toString());
            sqlInfo.addValue(idvalue);
            return sqlInfo;
        }
    }

    public static SqlInfo buildDeleteSql(Class clazz, Object idValue)
    {
        TableInfo table = TableInfo.get(clazz);
        Id id = table.getId();
        if(idValue == null)
        {
            throw new DbException("getDeleteSQL:idValue is null");
        } else
        {
            StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
            strSQL.append(" WHERE ").append(id.getColumn()).append("=?");
            SqlInfo sqlInfo = new SqlInfo();
            sqlInfo.setSql(strSQL.toString());
            sqlInfo.addValue(idValue);
            return sqlInfo;
        }
    }

    public static String buildDeleteSql(Class clazz, String strWhere)
    {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
        if(!TextUtils.isEmpty(strWhere))
        {
            strSQL.append(" WHERE ");
            strSQL.append(strWhere);
        }
        return strSQL.toString();
    }

    private static String getSelectSqlByTableName(String tableName)
    {
        return "SELECT * FROM " + tableName;
    }

    public static String getSelectSQL(Class clazz, Object idValue)
    {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
        strSQL.append(" WHERE ");
        strSQL.append(getPropertyStrSql(table.getId().getColumn(), idValue));
        return strSQL.toString();
    }

    public static SqlInfo getSelectSqlAsSqlInfo(Class clazz, Object idValue)
    {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
        strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idValue);
        return sqlInfo;
    }

    public static String getSelectSQL(Class clazz)
    {
        return getSelectSqlByTableName(TableInfo.get(clazz).getTableName());
    }

    public static String getSelectSQLByWhere(Class clazz, String strWhere)
    {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
        if(!TextUtils.isEmpty(strWhere))
            strSQL.append(" WHERE ").append(strWhere);
        return strSQL.toString();
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity)
    {
        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);
        if(idvalue == null)
            throw new DbException((new StringBuilder("this entity[")).append(entity.getClass()).append("]'s id value is null").toString());
        List keyValueList = new ArrayList();
        Collection propertys = table.propertyMap.values();
        for(Iterator iterator = propertys.iterator(); iterator.hasNext();)
        {
            Property property = (Property)iterator.next();
            KeyValue kv = property2KeyValue(property, entity);
            if(kv != null)
                keyValueList.add(kv);
        }

        Collection manyToOnes = table.manyToOneMap.values();
        for(Iterator iterator1 = manyToOnes.iterator(); iterator1.hasNext();)
        {
            ManyToOne many = (ManyToOne)iterator1.next();
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if(kv != null)
                keyValueList.add(kv);
        }

        if(keyValueList == null || keyValueList.size() == 0)
            return null;
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer strSQL = new StringBuffer("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        KeyValue kv;
        for(Iterator iterator2 = keyValueList.iterator(); iterator2.hasNext(); sqlInfo.addValue(kv.getValue()))
        {
            kv = (KeyValue)iterator2.next();
            strSQL.append(kv.getKey()).append("=?,");
        }

        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
        sqlInfo.addValue(idvalue);
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere)
    {
        TableInfo table = TableInfo.get(entity.getClass());
        List keyValueList = new ArrayList();
        Collection propertys = table.propertyMap.values();
        for(Iterator iterator = propertys.iterator(); iterator.hasNext();)
        {
            Property property = (Property)iterator.next();
            KeyValue kv = property2KeyValue(property, entity);
            if(kv != null)
                keyValueList.add(kv);
        }

        Collection manyToOnes = table.manyToOneMap.values();
        for(Iterator iterator1 = manyToOnes.iterator(); iterator1.hasNext();)
        {
            ManyToOne many = (ManyToOne)iterator1.next();
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if(kv != null)
                keyValueList.add(kv);
        }

        if(keyValueList == null || keyValueList.size() == 0)
            throw new DbException((new StringBuilder("this entity[")).append(entity.getClass()).append("] has no property").toString());
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer strSQL = new StringBuffer("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        KeyValue kv;
        for(Iterator iterator2 = keyValueList.iterator(); iterator2.hasNext(); sqlInfo.addValue(kv.getValue()))
        {
            kv = (KeyValue)iterator2.next();
            strSQL.append(kv.getKey()).append("=?,");
        }

        strSQL.deleteCharAt(strSQL.length() - 1);
        if(!TextUtils.isEmpty(strWhere))
            strSQL.append(" WHERE ").append(strWhere);
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static String getCreatTableSQL(Class clazz)
    {
        TableInfo table = TableInfo.get(clazz);
        Id id = table.getId();
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("CREATE TABLE IF NOT EXISTS ");
        strSQL.append(table.getTableName());
        strSQL.append(" ( ");
        Class primaryClazz = id.getDataType();
        if(primaryClazz == Integer.TYPE || primaryClazz == java/lang/Integer)
            strSQL.append("\"").append(id.getColumn()).append("\"    ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");
        else
            strSQL.append("\"").append(id.getColumn()).append("\"    ").append("TEXT PRIMARY KEY,");
        Collection propertys = table.propertyMap.values();
        for(Iterator iterator = propertys.iterator(); iterator.hasNext(); strSQL.append("\","))
        {
            Property property = (Property)iterator.next();
            strSQL.append("\"").append(property.getColumn());
        }

        Collection manyToOnes = table.manyToOneMap.values();
        ManyToOne manyToOne;
        for(Iterator iterator1 = manyToOnes.iterator(); iterator1.hasNext(); strSQL.append("\"").append(manyToOne.getColumn()).append("\","))
            manyToOne = (ManyToOne)iterator1.next();

        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" )");
        return strSQL.toString();
    }

    private static String getPropertyStrSql(String key, Object value)
    {
        StringBuffer sbSQL = (new StringBuffer(key)).append("=");
        if((value instanceof String) || (value instanceof Date) || (value instanceof java.sql.Date))
            sbSQL.append("'").append(value).append("'");
        else
            sbSQL.append(value);
        return sbSQL.toString();
    }

    private static KeyValue property2KeyValue(Property property, Object entity)
    {
        KeyValue kv = null;
        String pcolumn = property.getColumn();
        Object value = property.getValue(entity);
        if(value != null)
            kv = new KeyValue(pcolumn, value);
        else
        if(property.getDefaultValue() != null && property.getDefaultValue().trim().length() != 0)
            kv = new KeyValue(pcolumn, property.getDefaultValue());
        return kv;
    }

    private static KeyValue manyToOne2KeyValue(ManyToOne many, Object entity)
    {
        KeyValue kv = null;
        String manycolumn = many.getColumn();
        Object manyobject = many.getValue(entity);
        if(manyobject != null)
        {
            Object manyvalue;
            if(manyobject.getClass() == net/tsz/afinal/db/sqlite/ManyToOneLazyLoader)
                manyvalue = TableInfo.get(many.getManyClass()).getId().getValue(manyobject);
            else
                manyvalue = TableInfo.get(manyobject.getClass()).getId().getValue(manyobject);
            if(manycolumn != null && manyvalue != null)
                kv = new KeyValue(manycolumn, manyvalue);
        }
        return kv;
    }
}
