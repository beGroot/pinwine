// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractCollection.java

package net.tsz.afinal.core;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractCollection
    implements Collection
{

    protected AbstractCollection()
    {
    }

    public boolean add(Object object)
    {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection collection)
    {
        boolean result = false;
        for(Iterator it = collection.iterator(); it.hasNext();)
            if(add(it.next()))
                result = true;

        return result;
    }

    public void clear()
    {
        for(Iterator it = iterator(); it.hasNext(); it.remove())
            it.next();

    }

    public boolean contains(Object object)
    {
        Iterator it = iterator();
        if(object != null)
            while(it.hasNext()) 
                if(object.equals(it.next()))
                    return true;
        else
            while(it.hasNext()) 
                if(it.next() == null)
                    return true;
        return false;
    }

    public boolean containsAll(Collection collection)
    {
        for(Iterator it = collection.iterator(); it.hasNext();)
            if(!contains(it.next()))
                return false;

        return true;
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public abstract Iterator iterator();

    public boolean remove(Object object)
    {
        Iterator it = iterator();
        if(object != null)
            while(it.hasNext()) 
                if(object.equals(it.next()))
                {
                    it.remove();
                    return true;
                }
        else
            while(it.hasNext()) 
                if(it.next() == null)
                {
                    it.remove();
                    return true;
                }
        return false;
    }

    public boolean removeAll(Collection collection)
    {
        boolean result = false;
        for(Iterator it = iterator(); it.hasNext();)
            if(collection.contains(it.next()))
            {
                it.remove();
                result = true;
            }

        return result;
    }

    public boolean retainAll(Collection collection)
    {
        boolean result = false;
        for(Iterator it = iterator(); it.hasNext();)
            if(!collection.contains(it.next()))
            {
                it.remove();
                result = true;
            }

        return result;
    }

    public abstract int size();

    public Object[] toArray()
    {
        int size = size();
        int index = 0;
        Iterator it = iterator();
        Object array[] = new Object[size];
        while(index < size) 
            array[index++] = it.next();
        return array;
    }

    public Object[] toArray(Object contents[])
    {
        int size = size();
        int index = 0;
        if(size > contents.length)
        {
            Class ct = ((Object) (contents)).getClass().getComponentType();
            contents = (Object[])Array.newInstance(ct, size);
        }
        for(Iterator iterator1 = iterator(); iterator1.hasNext();)
        {
            Object entry = (Object)iterator1.next();
            contents[index++] = entry;
        }

        if(index < contents.length)
            contents[index] = null;
        return contents;
    }

    public String toString()
    {
        if(isEmpty())
            return "[]";
        StringBuilder buffer = new StringBuilder(size() * 16);
        buffer.append('[');
        for(Iterator it = iterator(); it.hasNext();)
        {
            Object next = it.next();
            if(next != this)
                buffer.append(next);
            else
                buffer.append("(this Collection)");
            if(it.hasNext())
                buffer.append(", ");
        }

        buffer.append(']');
        return buffer.toString();
    }
}
