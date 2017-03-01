// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Deque.java

package net.tsz.afinal.core;

import java.util.Iterator;

// Referenced classes of package net.tsz.afinal.core:
//            Queue

public interface Deque
    extends Queue
{

    public abstract void addFirst(Object obj);

    public abstract void addLast(Object obj);

    public abstract boolean offerFirst(Object obj);

    public abstract boolean offerLast(Object obj);

    public abstract Object removeFirst();

    public abstract Object removeLast();

    public abstract Object pollFirst();

    public abstract Object pollLast();

    public abstract Object getFirst();

    public abstract Object getLast();

    public abstract Object peekFirst();

    public abstract Object peekLast();

    public abstract boolean removeFirstOccurrence(Object obj);

    public abstract boolean removeLastOccurrence(Object obj);

    public abstract boolean add(Object obj);

    public abstract boolean offer(Object obj);

    public abstract Object remove();

    public abstract Object poll();

    public abstract Object element();

    public abstract Object peek();

    public abstract void push(Object obj);

    public abstract Object pop();

    public abstract boolean remove(Object obj);

    public abstract boolean contains(Object obj);

    public abstract int size();

    public abstract Iterator iterator();

    public abstract Iterator descendingIterator();
}
