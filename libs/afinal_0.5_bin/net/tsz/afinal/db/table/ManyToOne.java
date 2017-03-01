// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ManyToOne.java

package net.tsz.afinal.db.table;


// Referenced classes of package net.tsz.afinal.db.table:
//            Property

public class ManyToOne extends Property
{

    public ManyToOne()
    {
    }

    public Class getManyClass()
    {
        return manyClass;
    }

    public void setManyClass(Class manyClass)
    {
        this.manyClass = manyClass;
    }

    private Class manyClass;
}
