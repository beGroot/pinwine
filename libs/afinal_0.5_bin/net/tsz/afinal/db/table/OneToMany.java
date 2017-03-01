// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OneToMany.java

package net.tsz.afinal.db.table;


// Referenced classes of package net.tsz.afinal.db.table:
//            Property

public class OneToMany extends Property
{

    public OneToMany()
    {
    }

    public Class getOneClass()
    {
        return oneClass;
    }

    public void setOneClass(Class oneClass)
    {
        this.oneClass = oneClass;
    }

    private Class oneClass;
}
