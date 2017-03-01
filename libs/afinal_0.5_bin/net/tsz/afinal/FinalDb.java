// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FinalDb.java

package net.tsz.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.*;
import net.tsz.afinal.db.sqlite.CursorUtils;
import net.tsz.afinal.db.sqlite.DbModel;
import net.tsz.afinal.db.sqlite.OneToManyLazyLoader;
import net.tsz.afinal.db.sqlite.SqlBuilder;
import net.tsz.afinal.db.sqlite.SqlInfo;
import net.tsz.afinal.db.table.Id;
import net.tsz.afinal.db.table.KeyValue;
import net.tsz.afinal.db.table.ManyToOne;
import net.tsz.afinal.db.table.OneToMany;
import net.tsz.afinal.db.table.TableInfo;
import net.tsz.afinal.exception.DbException;

public class FinalDb
{
    public static class DaoConfig
    {

        public Context getContext()
        {
            return mContext;
        }

        public void setContext(Context context)
        {
            mContext = context;
        }

        public String getDbName()
        {
            return mDbName;
        }

        public void setDbName(String dbName)
        {
            mDbName = dbName;
        }

        public int getDbVersion()
        {
            return dbVersion;
        }

        public void setDbVersion(int dbVersion)
        {
            this.dbVersion = dbVersion;
        }

        public boolean isDebug()
        {
            return debug;
        }

        public void setDebug(boolean debug)
        {
            this.debug = debug;
        }

        public DbUpdateListener getDbUpdateListener()
        {
            return dbUpdateListener;
        }

        public void setDbUpdateListener(DbUpdateListener dbUpdateListener)
        {
            this.dbUpdateListener = dbUpdateListener;
        }

        public String getTargetDirectory()
        {
            return targetDirectory;
        }

        public void setTargetDirectory(String targetDirectory)
        {
            this.targetDirectory = targetDirectory;
        }

        private Context mContext;
        private String mDbName;
        private int dbVersion;
        private boolean debug;
        private DbUpdateListener dbUpdateListener;
        private String targetDirectory;

        public DaoConfig()
        {
            mContext = null;
            mDbName = "afinal.db";
            dbVersion = 1;
            debug = true;
        }
    }

    public static interface DbUpdateListener
    {

        public abstract void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j);
    }

    class SqliteDbHelper extends SQLiteOpenHelper
    {

        public void onCreate(SQLiteDatabase sqlitedatabase)
        {
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            if(mDbUpdateListener != null)
                mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
            else
                dropDb();
        }

        private DbUpdateListener mDbUpdateListener;
        final FinalDb this$0;

        public SqliteDbHelper(Context context, String name, int version, DbUpdateListener dbUpdateListener)
        {
            this$0 = FinalDb.this;
            super(context, name, null, version);
            mDbUpdateListener = dbUpdateListener;
        }
    }


    private FinalDb(DaoConfig config)
    {
        if(config == null)
            throw new DbException("daoConfig is null");
        if(config.getContext() == null)
            throw new DbException("android context is null");
        if(config.getTargetDirectory() != null && config.getTargetDirectory().trim().length() > 0)
            db = createDbFileOnSDCard(config.getTargetDirectory(), config.getDbName());
        else
            db = (new SqliteDbHelper(config.getContext().getApplicationContext(), config.getDbName(), config.getDbVersion(), config.getDbUpdateListener())).getWritableDatabase();
        this.config = config;
    }

    private static synchronized FinalDb getInstance(DaoConfig daoConfig)
    {
        FinalDb dao = (FinalDb)daoMap.get(daoConfig.getDbName());
        if(dao == null)
        {
            dao = new FinalDb(daoConfig);
            daoMap.put(daoConfig.getDbName(), dao);
        }
        return dao;
    }

    public static FinalDb create(Context context)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        return create(config);
    }

    public static FinalDb create(Context context, boolean isDebug)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDebug(isDebug);
        return create(config);
    }

    public static FinalDb create(Context context, String dbName)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDbName(dbName);
        return create(config);
    }

    public static FinalDb create(Context context, String dbName, boolean isDebug)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDbName(dbName);
        config.setDebug(isDebug);
        return create(config);
    }

    public static FinalDb create(Context context, String targetDirectory, String dbName)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDbName(dbName);
        config.setTargetDirectory(targetDirectory);
        return create(config);
    }

    public static FinalDb create(Context context, String targetDirectory, String dbName, boolean isDebug)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setTargetDirectory(targetDirectory);
        config.setDbName(dbName);
        config.setDebug(isDebug);
        return create(config);
    }

    public static FinalDb create(Context context, String dbName, boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setDbName(dbName);
        config.setDebug(isDebug);
        config.setDbVersion(dbVersion);
        config.setDbUpdateListener(dbUpdateListener);
        return create(config);
    }

    public static FinalDb create(Context context, String targetDirectory, String dbName, boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener)
    {
        DaoConfig config = new DaoConfig();
        config.setContext(context);
        config.setTargetDirectory(targetDirectory);
        config.setDbName(dbName);
        config.setDebug(isDebug);
        config.setDbVersion(dbVersion);
        config.setDbUpdateListener(dbUpdateListener);
        return create(config);
    }

    public static FinalDb create(DaoConfig daoConfig)
    {
        return getInstance(daoConfig);
    }

    public void save(Object entity)
    {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.buildInsertSql(entity));
    }

    public boolean saveBindId(Object entity)
    {
        checkTableExist(entity.getClass());
        List entityKvList = SqlBuilder.getSaveKeyValueListByEntity(entity);
        if(entityKvList != null && entityKvList.size() > 0)
        {
            TableInfo tf = TableInfo.get(entity.getClass());
            ContentValues cv = new ContentValues();
            insertContentValues(entityKvList, cv);
            Long id = Long.valueOf(db.insert(tf.getTableName(), null, cv));
            if(id.longValue() == -1L)
            {
                return false;
            } else
            {
                tf.getId().setValue(entity, id);
                return true;
            }
        } else
        {
            return false;
        }
    }

    private void insertContentValues(List list, ContentValues cv)
    {
        if(list != null && cv != null)
        {
            KeyValue kv;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); cv.put(kv.getKey(), kv.getValue().toString()))
                kv = (KeyValue)iterator.next();

        } else
        {
            Log.w("FinalDb", "insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
        }
    }

    public void update(Object entity)
    {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
    }

    public void update(Object entity, String strWhere)
    {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
    }

    public void delete(Object entity)
    {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
    }

    public void deleteById(Class clazz, Object id)
    {
        checkTableExist(clazz);
        exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
    }

    public void deleteByWhere(Class clazz, String strWhere)
    {
        checkTableExist(clazz);
        String sql = SqlBuilder.buildDeleteSql(clazz, strWhere);
        debugSql(sql);
        db.execSQL(sql);
    }

    public void dropDb()
    {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table'", null);
        if(cursor != null)
            while(cursor.moveToNext()) 
                try
                {
                    db.execSQL((new StringBuilder("DROP TABLE ")).append(cursor.getString(0)).toString());
                }
                catch(SQLException e)
                {
                    Log.e("FinalDb", e.getMessage());
                }
        if(cursor != null)
        {
            cursor.close();
            cursor = null;
        }
    }

    private void exeSqlInfo(SqlInfo sqlInfo)
    {
        if(sqlInfo != null)
        {
            debugSql(sqlInfo.getSql());
            db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
        } else
        {
            Log.e("FinalDb", "sava error:sqlInfo is null");
        }
    }

    public Object findById(Object id, Class clazz)
    {
        Cursor cursor;
        checkTableExist(clazz);
        SqlInfo sqlInfo = SqlBuilder.getSelectSqlAsSqlInfo(clazz, id);
        if(sqlInfo == null)
            break MISSING_BLOCK_LABEL_105;
        debugSql(sqlInfo.getSql());
        cursor = db.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStringArray());
        Object obj;
        if(!cursor.moveToNext())
            break MISSING_BLOCK_LABEL_98;
        obj = CursorUtils.getEntity(cursor, clazz, this);
        cursor.close();
        return obj;
        Exception e;
        e;
        e.printStackTrace();
        cursor.close();
        break MISSING_BLOCK_LABEL_105;
        Exception exception;
        exception;
        cursor.close();
        throw exception;
        cursor.close();
        return null;
    }

    public Object findWithManyToOneById(Object id, Class clazz)
    {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if(dbModel != null)
        {
            Object entity = CursorUtils.dbModel2Entity(dbModel, clazz);
            return loadManyToOne(entity, clazz, new Class[0]);
        } else
        {
            return null;
        }
    }

    public transient Object findWithManyToOneById(Object id, Class clazz, Class findClass[])
    {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if(dbModel != null)
        {
            Object entity = CursorUtils.dbModel2Entity(dbModel, clazz);
            return loadManyToOne(entity, clazz, findClass);
        } else
        {
            return null;
        }
    }

    public transient Object loadManyToOne(Object entity, Class clazz, Class findClass[])
    {
        if(entity != null)
            try
            {
                Collection manys = TableInfo.get(clazz).manyToOneMap.values();
                for(Iterator iterator = manys.iterator(); iterator.hasNext();)
                {
                    ManyToOne many = (ManyToOne)iterator.next();
                    Object id = many.getValue(entity);
                    if(id != null)
                    {
                        boolean isFind = false;
                        if(findClass == null || findClass.length == 0)
                            isFind = true;
                        Class aclass[];
                        int j = (aclass = findClass).length;
                        for(int i = 0; i < j; i++)
                        {
                            Class mClass = aclass[i];
                            if(many.getManyClass() != mClass)
                                continue;
                            isFind = true;
                            break;
                        }

                        if(isFind)
                        {
                            Object manyEntity = findById(Integer.valueOf(id.toString()), many.getDataType());
                            if(manyEntity != null)
                                many.setValue(entity, manyEntity);
                        }
                    }
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        return entity;
    }

    public Object findWithOneToManyById(Object id, Class clazz)
    {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if(dbModel != null)
        {
            Object entity = CursorUtils.dbModel2Entity(dbModel, clazz);
            return loadOneToMany(entity, clazz, new Class[0]);
        } else
        {
            return null;
        }
    }

    public transient Object findWithOneToManyById(Object id, Class clazz, Class findClass[])
    {
        checkTableExist(clazz);
        String sql = SqlBuilder.getSelectSQL(clazz, id);
        debugSql(sql);
        DbModel dbModel = findDbModelBySQL(sql);
        if(dbModel != null)
        {
            Object entity = CursorUtils.dbModel2Entity(dbModel, clazz);
            return loadOneToMany(entity, clazz, findClass);
        } else
        {
            return null;
        }
    }

    public transient Object loadOneToMany(Object entity, Class clazz, Class findClass[])
    {
        if(entity != null)
            try
            {
                Collection ones = TableInfo.get(clazz).oneToManyMap.values();
                Object id = TableInfo.get(clazz).getId().getValue(entity);
                for(Iterator iterator = ones.iterator(); iterator.hasNext();)
                {
                    OneToMany one = (OneToMany)iterator.next();
                    boolean isFind = false;
                    if(findClass == null || findClass.length == 0)
                        isFind = true;
                    Class aclass[];
                    int j = (aclass = findClass).length;
                    for(int i = 0; i < j; i++)
                    {
                        Class mClass = aclass[i];
                        if(one.getOneClass() != mClass)
                            continue;
                        isFind = true;
                        break;
                    }

                    if(isFind)
                    {
                        List list = findAllByWhere(one.getOneClass(), (new StringBuilder(String.valueOf(one.getColumn()))).append("=").append(id).toString());
                        if(list != null)
                            if(one.getDataType() == net/tsz/afinal/db/sqlite/OneToManyLazyLoader)
                            {
                                OneToManyLazyLoader oneToManyLazyLoader = (OneToManyLazyLoader)one.getValue(entity);
                                oneToManyLazyLoader.setList(list);
                            } else
                            {
                                one.setValue(entity, list);
                            }
                    }
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        return entity;
    }

    public List findAll(Class clazz)
    {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz));
    }

    public List findAll(Class clazz, String orderBy)
    {
        checkTableExist(clazz);
        return findAllBySql(clazz, (new StringBuilder(String.valueOf(SqlBuilder.getSelectSQL(clazz)))).append(" ORDER BY ").append(orderBy).toString());
    }

    public List findAllByWhere(Class clazz, String strWhere)
    {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
    }

    public List findAllByWhere(Class clazz, String strWhere, String orderBy)
    {
        checkTableExist(clazz);
        return findAllBySql(clazz, (new StringBuilder(String.valueOf(SqlBuilder.getSelectSQLByWhere(clazz, strWhere)))).append(" ORDER BY ").append(orderBy).toString());
    }

    private List findAllBySql(Class clazz, String strSQL)
    {
        Cursor cursor;
        checkTableExist(clazz);
        debugSql(strSQL);
        cursor = db.rawQuery(strSQL, null);
        List list1;
        List list = new ArrayList();
        Object t;
        for(; cursor.moveToNext(); list.add(t))
            t = CursorUtils.getEntity(cursor, clazz, this);

        list1 = list;
        if(cursor != null)
            cursor.close();
        cursor = null;
        return list1;
        Exception e;
        e;
        e.printStackTrace();
        if(cursor != null)
            cursor.close();
        cursor = null;
        break MISSING_BLOCK_LABEL_117;
        Exception exception;
        exception;
        if(cursor != null)
            cursor.close();
        cursor = null;
        throw exception;
        return null;
    }

    public DbModel findDbModelBySQL(String strSQL)
    {
        Cursor cursor;
        debugSql(strSQL);
        cursor = db.rawQuery(strSQL, null);
        DbModel dbmodel;
        if(!cursor.moveToNext())
            break MISSING_BLOCK_LABEL_64;
        dbmodel = CursorUtils.getDbModel(cursor);
        cursor.close();
        return dbmodel;
        Exception e;
        e;
        e.printStackTrace();
        cursor.close();
        break MISSING_BLOCK_LABEL_70;
        Exception exception;
        exception;
        cursor.close();
        throw exception;
        cursor.close();
        return null;
    }

    public List findDbModelListBySQL(String strSQL)
    {
        Cursor cursor;
        List dbModelList;
        debugSql(strSQL);
        cursor = db.rawQuery(strSQL, null);
        dbModelList = new ArrayList();
        try
        {
            for(; cursor.moveToNext(); dbModelList.add(CursorUtils.getDbModel(cursor)));
            break MISSING_BLOCK_LABEL_76;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        cursor.close();
        break MISSING_BLOCK_LABEL_82;
        Exception exception;
        exception;
        cursor.close();
        throw exception;
        cursor.close();
        return dbModelList;
    }

    private void checkTableExist(Class clazz)
    {
        if(!tableIsExist(TableInfo.get(clazz)))
        {
            String sql = SqlBuilder.getCreatTableSQL(clazz);
            debugSql(sql);
            db.execSQL(sql);
        }
    }

    private boolean tableIsExist(TableInfo table)
    {
        Cursor cursor;
        if(table.isCheckDatabese())
            return true;
        cursor = null;
        String sql = (new StringBuilder("SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='")).append(table.getTableName()).append("' ").toString();
        debugSql(sql);
        cursor = db.rawQuery(sql, null);
        if(cursor == null || !cursor.moveToNext())
            break MISSING_BLOCK_LABEL_136;
        int count = cursor.getInt(0);
        if(count <= 0)
            break MISSING_BLOCK_LABEL_136;
        table.setCheckDatabese(true);
        if(cursor != null)
            cursor.close();
        cursor = null;
        return true;
        Exception e;
        e;
        e.printStackTrace();
        if(cursor != null)
            cursor.close();
        cursor = null;
        break MISSING_BLOCK_LABEL_148;
        Exception exception;
        exception;
        if(cursor != null)
            cursor.close();
        cursor = null;
        throw exception;
        if(cursor != null)
            cursor.close();
        cursor = null;
        return false;
    }

    private void debugSql(String sql)
    {
        if(config != null && config.isDebug())
            Log.d("Debug SQL", (new StringBuilder(">>>>>>  ")).append(sql).toString());
    }

    private SQLiteDatabase createDbFileOnSDCard(String sdcardPath, String dbfilename)
    {
        File dbf = new File(sdcardPath, dbfilename);
        if(!dbf.exists())
            try
            {
                if(dbf.createNewFile())
                    return SQLiteDatabase.openOrCreateDatabase(dbf, null);
            }
            catch(IOException ioex)
            {
                throw new DbException("\u6570\u636E\u5E93\u6587\u4EF6\u521B\u5EFA\u5931\u8D25", ioex);
            }
        else
            return SQLiteDatabase.openOrCreateDatabase(dbf, null);
        return null;
    }

    private static final String TAG = "FinalDb";
    private static HashMap daoMap = new HashMap();
    private SQLiteDatabase db;
    private DaoConfig config;

}
