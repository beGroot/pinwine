// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Queue.java

package net.tsz.afinal.core;

import java.util.Collection;

public interface Queue
    extends Collection
{

    public abstract boolean add(Object obj);

    public abstract boolean offer(Object obj);

    public abstract Object remove();

    public abstract Object poll();

    public abstract Object element();

    public abstract Object peek();
}
