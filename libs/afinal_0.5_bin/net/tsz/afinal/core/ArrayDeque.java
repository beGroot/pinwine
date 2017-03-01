// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ArrayDeque.java

package net.tsz.afinal.core;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

// Referenced classes of package net.tsz.afinal.core:
//            AbstractCollection, Deque, Arrays

public class ArrayDeque extends AbstractCollection
    implements Deque, Cloneable, Serializable
{
    private class DeqIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            return cursor != fence;
        }

        public Object next()
        {
            if(cursor == fence)
                throw new NoSuchElementException();
            Object result = elements[cursor];
            if(tail != fence || result == null)
            {
                throw new ConcurrentModificationException();
            } else
            {
                lastRet = cursor;
                cursor = cursor + 1 & elements.length - 1;
                return result;
            }
        }

        public void remove()
        {
            if(lastRet < 0)
                throw new IllegalStateException();
            if(delete(lastRet))
            {
                cursor = cursor - 1 & elements.length - 1;
                fence = tail;
            }
            lastRet = -1;
        }

        private int cursor;
        private int fence;
        private int lastRet;
        final ArrayDeque this$0;

        private DeqIterator()
        {
            this$0 = ArrayDeque.this;
            super();
            cursor = head;
            fence = tail;
            lastRet = -1;
        }

        DeqIterator(DeqIterator deqiterator)
        {
            this();
        }
    }

    private class DescendingIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            return cursor != fence;
        }

        public Object next()
        {
            if(cursor == fence)
                throw new NoSuchElementException();
            cursor = cursor - 1 & elements.length - 1;
            Object result = elements[cursor];
            if(head != fence || result == null)
            {
                throw new ConcurrentModificationException();
            } else
            {
                lastRet = cursor;
                return result;
            }
        }

        public void remove()
        {
            if(lastRet < 0)
                throw new IllegalStateException();
            if(!delete(lastRet))
            {
                cursor = cursor + 1 & elements.length - 1;
                fence = head;
            }
            lastRet = -1;
        }

        private int cursor;
        private int fence;
        private int lastRet;
        final ArrayDeque this$0;

        private DescendingIterator()
        {
            this$0 = ArrayDeque.this;
            super();
            cursor = tail;
            fence = head;
            lastRet = -1;
        }

        DescendingIterator(DescendingIterator descendingiterator)
        {
            this();
        }
    }


    private void allocateElements(int numElements)
    {
        int initialCapacity = 8;
        if(numElements >= initialCapacity)
        {
            initialCapacity = numElements;
            initialCapacity |= initialCapacity >>> 1;
            initialCapacity |= initialCapacity >>> 2;
            initialCapacity |= initialCapacity >>> 4;
            initialCapacity |= initialCapacity >>> 8;
            initialCapacity |= initialCapacity >>> 16;
            if(++initialCapacity < 0)
                initialCapacity >>>= 1;
        }
        elements = new Object[initialCapacity];
    }

    private void doubleCapacity()
    {
        if(!$assertionsDisabled && head != tail)
            throw new AssertionError();
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCapacity = n << 1;
        if(newCapacity < 0)
        {
            throw new IllegalStateException("Sorry, deque too big");
        } else
        {
            Object a[] = new Object[newCapacity];
            System.arraycopy(((Object) (elements)), p, ((Object) (a)), 0, r);
            System.arraycopy(((Object) (elements)), 0, ((Object) (a)), r, p);
            elements = a;
            head = 0;
            tail = n;
            return;
        }
    }

    private Object[] copyElements(Object a[])
    {
        if(head < tail)
            System.arraycopy(((Object) (elements)), head, ((Object) (a)), 0, size());
        else
        if(head > tail)
        {
            int headPortionLen = elements.length - head;
            System.arraycopy(((Object) (elements)), head, ((Object) (a)), 0, headPortionLen);
            System.arraycopy(((Object) (elements)), 0, ((Object) (a)), headPortionLen, tail);
        }
        return a;
    }

    public ArrayDeque()
    {
        elements = new Object[16];
    }

    public ArrayDeque(int numElements)
    {
        allocateElements(numElements);
    }

    public ArrayDeque(Collection c)
    {
        allocateElements(c.size());
        addAll(c);
    }

    public void addFirst(Object e)
    {
        if(e == null)
            throw new NullPointerException();
        elements[head = head - 1 & elements.length - 1] = e;
        if(head == tail)
            doubleCapacity();
    }

    public void addLast(Object e)
    {
        if(e == null)
            throw new NullPointerException();
        elements[tail] = e;
        if((tail = tail + 1 & elements.length - 1) == head)
            doubleCapacity();
    }

    public boolean offerFirst(Object e)
    {
        addFirst(e);
        return true;
    }

    public boolean offerLast(Object e)
    {
        addLast(e);
        return true;
    }

    public Object removeFirst()
    {
        Object x = pollFirst();
        if(x == null)
            throw new NoSuchElementException();
        else
            return x;
    }

    public Object removeLast()
    {
        Object x = pollLast();
        if(x == null)
            throw new NoSuchElementException();
        else
            return x;
    }

    public Object pollFirst()
    {
        int h = head;
        Object result = elements[h];
        if(result == null)
        {
            return null;
        } else
        {
            elements[h] = null;
            head = h + 1 & elements.length - 1;
            return result;
        }
    }

    public Object pollLast()
    {
        int t = tail - 1 & elements.length - 1;
        Object result = elements[t];
        if(result == null)
        {
            return null;
        } else
        {
            elements[t] = null;
            tail = t;
            return result;
        }
    }

    public Object getFirst()
    {
        Object x = elements[head];
        if(x == null)
            throw new NoSuchElementException();
        else
            return x;
    }

    public Object getLast()
    {
        Object x = elements[tail - 1 & elements.length - 1];
        if(x == null)
            throw new NoSuchElementException();
        else
            return x;
    }

    public Object peekFirst()
    {
        return elements[head];
    }

    public Object peekLast()
    {
        return elements[tail - 1 & elements.length - 1];
    }

    public boolean removeFirstOccurrence(Object o)
    {
        if(o == null)
            return false;
        int mask = elements.length - 1;
        Object x;
        for(int i = head; (x = elements[i]) != null; i = i + 1 & mask)
            if(o.equals(x))
            {
                delete(i);
                return true;
            }

        return false;
    }

    public boolean removeLastOccurrence(Object o)
    {
        if(o == null)
            return false;
        int mask = elements.length - 1;
        Object x;
        for(int i = tail - 1 & mask; (x = elements[i]) != null; i = i - 1 & mask)
            if(o.equals(x))
            {
                delete(i);
                return true;
            }

        return false;
    }

    public boolean add(Object e)
    {
        addLast(e);
        return true;
    }

    public boolean offer(Object e)
    {
        return offerLast(e);
    }

    public Object remove()
    {
        return removeFirst();
    }

    public Object poll()
    {
        return pollFirst();
    }

    public Object element()
    {
        return getFirst();
    }

    public Object peek()
    {
        return peekFirst();
    }

    public void push(Object e)
    {
        addFirst(e);
    }

    public Object pop()
    {
        return removeFirst();
    }

    private void checkInvariants()
    {
        if(!$assertionsDisabled && elements[tail] != null)
            throw new AssertionError();
        if(!$assertionsDisabled && (head != tail ? elements[head] == null || elements[tail - 1 & elements.length - 1] == null : elements[head] != null))
            throw new AssertionError();
        if(!$assertionsDisabled && elements[head - 1 & elements.length - 1] != null)
            throw new AssertionError();
        else
            return;
    }

    private boolean delete(int i)
    {
        checkInvariants();
        Object elements[] = this.elements;
        int mask = elements.length - 1;
        int h = head;
        int t = tail;
        int front = i - h & mask;
        int back = t - i & mask;
        if(front >= (t - h & mask))
            throw new ConcurrentModificationException();
        if(front < back)
        {
            if(h <= i)
            {
                System.arraycopy(((Object) (elements)), h, ((Object) (elements)), h + 1, front);
            } else
            {
                System.arraycopy(((Object) (elements)), 0, ((Object) (elements)), 1, i);
                elements[0] = elements[mask];
                System.arraycopy(((Object) (elements)), h, ((Object) (elements)), h + 1, mask - h);
            }
            elements[h] = null;
            head = h + 1 & mask;
            return false;
        }
        if(i < t)
        {
            System.arraycopy(((Object) (elements)), i + 1, ((Object) (elements)), i, back);
            tail = t - 1;
        } else
        {
            System.arraycopy(((Object) (elements)), i + 1, ((Object) (elements)), i, mask - i);
            elements[mask] = elements[0];
            System.arraycopy(((Object) (elements)), 1, ((Object) (elements)), 0, t);
            tail = t - 1 & mask;
        }
        return true;
    }

    public int size()
    {
        return tail - head & elements.length - 1;
    }

    public boolean isEmpty()
    {
        return head == tail;
    }

    public Iterator iterator()
    {
        return new DeqIterator(null);
    }

    public Iterator descendingIterator()
    {
        return new DescendingIterator(null);
    }

    public boolean contains(Object o)
    {
        if(o == null)
            return false;
        int mask = elements.length - 1;
        Object x;
        for(int i = head; (x = elements[i]) != null; i = i + 1 & mask)
            if(o.equals(x))
                return true;

        return false;
    }

    public boolean remove(Object o)
    {
        return removeFirstOccurrence(o);
    }

    public void clear()
    {
        int h = head;
        int t = tail;
        if(h != t)
        {
            head = tail = 0;
            int i = h;
            int mask = elements.length - 1;
            do
            {
                elements[i] = null;
                i = i + 1 & mask;
            } while(i != t);
        }
    }

    public Object[] toArray()
    {
        return copyElements(new Object[size()]);
    }

    public Object[] toArray(Object a[])
    {
        int size = size();
        if(a.length < size)
            a = (Object[])Array.newInstance(((Object) (a)).getClass().getComponentType(), size);
        copyElements(a);
        if(a.length > size)
            a[size] = null;
        return a;
    }

    public ArrayDeque clone()
    {
        try
        {
            ArrayDeque result = (ArrayDeque)super.clone();
            result.elements = Arrays.copyOf(elements, elements.length);
            return result;
        }
        catch(CloneNotSupportedException e)
        {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream s)
        throws IOException
    {
        s.defaultWriteObject();
        s.writeInt(size());
        int mask = elements.length - 1;
        for(int i = head; i != tail; i = i + 1 & mask)
            s.writeObject(elements[i]);

    }

    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
        int size = s.readInt();
        allocateElements(size);
        head = 0;
        tail = size;
        for(int i = 0; i < size; i++)
            elements[i] = s.readObject();

    }

    public volatile Object clone()
        throws CloneNotSupportedException
    {
        return clone();
    }

    private transient Object elements[];
    private transient int head;
    private transient int tail;
    private static final int MIN_INITIAL_CAPACITY = 8;
    private static final long serialVersionUID = 0x207cda2e240da08bL;
    static final boolean $assertionsDisabled = !net/tsz/afinal/core/ArrayDeque.desiredAssertionStatus();





}
