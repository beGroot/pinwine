// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Arrays.java

package net.tsz.afinal.core;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Arrays
{
    private static class ArrayList extends AbstractList
        implements List, Serializable, RandomAccess
    {

        public boolean contains(Object object)
        {
            if(object != null)
            {
                Object aobj[];
                int k = (aobj = a).length;
                for(int i = 0; i < k; i++)
                {
                    Object element = aobj[i];
                    if(object.equals(element))
                        return true;
                }

            } else
            {
                Object aobj1[];
                int l = (aobj1 = a).length;
                for(int j = 0; j < l; j++)
                {
                    Object element = aobj1[j];
                    if(element == null)
                        return true;
                }

            }
            return false;
        }

        public Object get(int location)
        {
            try
            {
                return a[location];
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                throw e;
            }
        }

        public int indexOf(Object object)
        {
            if(object != null)
            {
                for(int i = 0; i < a.length; i++)
                    if(object.equals(a[i]))
                        return i;

            } else
            {
                for(int i = 0; i < a.length; i++)
                    if(a[i] == null)
                        return i;

            }
            return -1;
        }

        public int lastIndexOf(Object object)
        {
            if(object != null)
            {
                for(int i = a.length - 1; i >= 0; i--)
                    if(object.equals(a[i]))
                        return i;

            } else
            {
                for(int i = a.length - 1; i >= 0; i--)
                    if(a[i] == null)
                        return i;

            }
            return -1;
        }

        public Object set(int location, Object object)
        {
            Object result = a[location];
            a[location] = object;
            return result;
        }

        public int size()
        {
            return a.length;
        }

        public Object[] toArray()
        {
            return (Object[])((Object []) (a)).clone();
        }

        public Object[] toArray(Object contents[])
        {
            int size = size();
            if(size > contents.length)
            {
                Class ct = ((Object) (contents)).getClass().getComponentType();
                contents = (Object[])Array.newInstance(ct, size);
            }
            System.arraycopy(((Object) (a)), 0, ((Object) (contents)), 0, size);
            if(size < contents.length)
                contents[size] = null;
            return contents;
        }

        private static final long serialVersionUID = 0xd9a43cbecd8806d2L;
        private final Object a[];

        ArrayList(Object storage[])
        {
            if(storage == null)
            {
                throw new NullPointerException();
            } else
            {
                a = storage;
                return;
            }
        }
    }


    private Arrays()
    {
    }

    public static transient List asList(Object array[])
    {
        return new ArrayList(array);
    }

    public static int binarySearch(byte array[], byte value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(byte array[], int startIndex, int endIndex, byte value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            byte midVal = array[mid];
            if(midVal < value)
                lo = mid + 1;
            else
            if(midVal > value)
                hi = mid - 1;
            else
                return mid;
        }

        return ~lo;
    }

    public static int binarySearch(char array[], char value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(char array[], int startIndex, int endIndex, char value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            char midVal = array[mid];
            if(midVal < value)
                lo = mid + 1;
            else
            if(midVal > value)
                hi = mid - 1;
            else
                return mid;
        }

        return ~lo;
    }

    public static int binarySearch(double array[], double value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(double array[], int startIndex, int endIndex, double value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            double midVal = array[mid];
            if(midVal < value)
                lo = mid + 1;
            else
            if(midVal > value)
            {
                hi = mid - 1;
            } else
            {
                if(midVal != 0.0D && midVal == value)
                    return mid;
                long midValBits = Double.doubleToLongBits(midVal);
                long valueBits = Double.doubleToLongBits(value);
                if(midValBits < valueBits)
                    lo = mid + 1;
                else
                if(midValBits > valueBits)
                    hi = mid - 1;
                else
                    return mid;
            }
        }

        return ~lo;
    }

    public static int binarySearch(float array[], float value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(float array[], int startIndex, int endIndex, float value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            float midVal = array[mid];
            if(midVal < value)
                lo = mid + 1;
            else
            if(midVal > value)
            {
                hi = mid - 1;
            } else
            {
                if(midVal != 0.0F && midVal == value)
                    return mid;
                int midValBits = Float.floatToIntBits(midVal);
                int valueBits = Float.floatToIntBits(value);
                if(midValBits < valueBits)
                    lo = mid + 1;
                else
                if(midValBits > valueBits)
                    hi = mid - 1;
                else
                    return mid;
            }
        }

        return ~lo;
    }

    public static int binarySearch(int array[], int value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(int array[], int startIndex, int endIndex, int value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            int midVal = array[mid];
            if(midVal < value)
                lo = mid + 1;
            else
            if(midVal > value)
                hi = mid - 1;
            else
                return mid;
        }

        return ~lo;
    }

    public static int binarySearch(long array[], long value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(long array[], int startIndex, int endIndex, long value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            long midVal = array[mid];
            if(midVal < value)
                lo = mid + 1;
            else
            if(midVal > value)
                hi = mid - 1;
            else
                return mid;
        }

        return ~lo;
    }

    public static int binarySearch(Object array[], Object value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(Object array[], int startIndex, int endIndex, Object value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            int midValCmp = ((Comparable)array[mid]).compareTo(value);
            if(midValCmp < 0)
                lo = mid + 1;
            else
            if(midValCmp > 0)
                hi = mid - 1;
            else
                return mid;
        }

        return ~lo;
    }

    public static int binarySearch(Object array[], Object value, Comparator comparator)
    {
        return binarySearch(array, 0, array.length, value, comparator);
    }

    public static int binarySearch(Object array[], int startIndex, int endIndex, Object value, Comparator comparator)
    {
        if(comparator == null)
            return binarySearch(array, startIndex, endIndex, value);
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            int midValCmp = comparator.compare(array[mid], value);
            if(midValCmp < 0)
                lo = mid + 1;
            else
            if(midValCmp > 0)
                hi = mid - 1;
            else
                return mid;
        }

        return ~lo;
    }

    public static int binarySearch(short array[], short value)
    {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(short array[], int startIndex, int endIndex, short value)
    {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        int lo = startIndex;
        for(int hi = endIndex - 1; lo <= hi;)
        {
            int mid = lo + hi >>> 1;
            short midVal = array[mid];
            if(midVal < value)
                lo = mid + 1;
            else
            if(midVal > value)
                hi = mid - 1;
            else
                return mid;
        }

        return ~lo;
    }

    private static void checkBinarySearchBounds(int startIndex, int endIndex, int length)
    {
        if(startIndex > endIndex)
            throw new IllegalArgumentException();
        if(startIndex < 0 || endIndex > length)
            throw new ArrayIndexOutOfBoundsException();
        else
            return;
    }

    public static void fill(byte array[], byte value)
    {
        for(int i = 0; i < array.length; i++)
            array[i] = value;

    }

    public static void fill(int array[], int value)
    {
        for(int i = 0; i < array.length; i++)
            array[i] = value;

    }

    public static void fill(boolean array[], boolean value)
    {
        for(int i = 0; i < array.length; i++)
            array[i] = value;

    }

    public static void fill(Object array[], Object value)
    {
        for(int i = 0; i < array.length; i++)
            array[i] = value;

    }

    public static int hashCode(boolean array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        boolean aflag[];
        int j = (aflag = array).length;
        for(int i = 0; i < j; i++)
        {
            boolean element = aflag[i];
            hashCode = 31 * hashCode + (element ? 1231 : '\u04D5');
        }

        return hashCode;
    }

    public static int hashCode(int array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        int ai[];
        int j = (ai = array).length;
        for(int i = 0; i < j; i++)
        {
            int element = ai[i];
            hashCode = 31 * hashCode + element;
        }

        return hashCode;
    }

    public static int hashCode(short array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        short aword0[];
        int j = (aword0 = array).length;
        for(int i = 0; i < j; i++)
        {
            short element = aword0[i];
            hashCode = 31 * hashCode + element;
        }

        return hashCode;
    }

    public static int hashCode(char array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        char ac[];
        int j = (ac = array).length;
        for(int i = 0; i < j; i++)
        {
            char element = ac[i];
            hashCode = 31 * hashCode + element;
        }

        return hashCode;
    }

    public static int hashCode(byte array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        byte abyte0[];
        int j = (abyte0 = array).length;
        for(int i = 0; i < j; i++)
        {
            byte element = abyte0[i];
            hashCode = 31 * hashCode + element;
        }

        return hashCode;
    }

    public static int hashCode(long array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        long al[];
        int j = (al = array).length;
        for(int i = 0; i < j; i++)
        {
            long elementValue = al[i];
            hashCode = 31 * hashCode + (int)(elementValue ^ elementValue >>> 32);
        }

        return hashCode;
    }

    public static int hashCode(float array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        float af[];
        int j = (af = array).length;
        for(int i = 0; i < j; i++)
        {
            float element = af[i];
            hashCode = 31 * hashCode + Float.floatToIntBits(element);
        }

        return hashCode;
    }

    public static int hashCode(double array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        double ad[];
        int j = (ad = array).length;
        for(int i = 0; i < j; i++)
        {
            double element = ad[i];
            long v = Double.doubleToLongBits(element);
            hashCode = 31 * hashCode + (int)(v ^ v >>> 32);
        }

        return hashCode;
    }

    public static int hashCode(Object array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        Object aobj[];
        int j = (aobj = array).length;
        for(int i = 0; i < j; i++)
        {
            Object element = aobj[i];
            int elementHashCode;
            if(element == null)
                elementHashCode = 0;
            else
                elementHashCode = element.hashCode();
            hashCode = 31 * hashCode + elementHashCode;
        }

        return hashCode;
    }

    public static int deepHashCode(Object array[])
    {
        if(array == null)
            return 0;
        int hashCode = 1;
        Object aobj[];
        int j = (aobj = array).length;
        for(int i = 0; i < j; i++)
        {
            Object element = aobj[i];
            int elementHashCode = deepHashCodeElement(element);
            hashCode = 31 * hashCode + elementHashCode;
        }

        return hashCode;
    }

    private static int deepHashCodeElement(Object element)
    {
        if(element == null)
            return 0;
        Class cl = element.getClass().getComponentType();
        if(cl == null)
            return element.hashCode();
        if(!cl.isPrimitive())
            return deepHashCode((Object[])element);
        if(cl.equals(Integer.TYPE))
            return hashCode((int[])element);
        if(cl.equals(Character.TYPE))
            return hashCode((char[])element);
        if(cl.equals(Boolean.TYPE))
            return hashCode((boolean[])element);
        if(cl.equals(Byte.TYPE))
            return hashCode((byte[])element);
        if(cl.equals(Long.TYPE))
            return hashCode((long[])element);
        if(cl.equals(Float.TYPE))
            return hashCode((float[])element);
        if(cl.equals(Double.TYPE))
            return hashCode((double[])element);
        else
            return hashCode((short[])element);
    }

    public static boolean equals(byte array1[], byte array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(array1[i] != array2[i])
                return false;

        return true;
    }

    public static boolean equals(short array1[], short array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(array1[i] != array2[i])
                return false;

        return true;
    }

    public static boolean equals(char array1[], char array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(array1[i] != array2[i])
                return false;

        return true;
    }

    public static boolean equals(int array1[], int array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(array1[i] != array2[i])
                return false;

        return true;
    }

    public static boolean equals(long array1[], long array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(array1[i] != array2[i])
                return false;

        return true;
    }

    public static boolean equals(float array1[], float array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(Float.floatToIntBits(array1[i]) != Float.floatToIntBits(array2[i]))
                return false;

        return true;
    }

    public static boolean equals(double array1[], double array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(Double.doubleToLongBits(array1[i]) != Double.doubleToLongBits(array2[i]))
                return false;

        return true;
    }

    public static boolean equals(boolean array1[], boolean array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
            if(array1[i] != array2[i])
                return false;

        return true;
    }

    public static boolean equals(Object array1[], Object array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
        {
            Object e1 = array1[i];
            Object e2 = array2[i];
            if(e1 != null ? !e1.equals(e2) : e2 != null)
                return false;
        }

        return true;
    }

    public static boolean deepEquals(Object array1[], Object array2[])
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length != array2.length)
            return false;
        for(int i = 0; i < array1.length; i++)
        {
            Object e1 = array1[i];
            Object e2 = array2[i];
            if(!deepEqualsElements(e1, e2))
                return false;
        }

        return true;
    }

    private static boolean deepEqualsElements(Object e1, Object e2)
    {
        if(e1 == e2)
            return true;
        if(e1 == null || e2 == null)
            return false;
        Class cl1 = e1.getClass().getComponentType();
        Class cl2 = e2.getClass().getComponentType();
        if(cl1 != cl2)
            return false;
        if(cl1 == null)
            return e1.equals(e2);
        if(!cl1.isPrimitive())
            return deepEquals((Object[])e1, (Object[])e2);
        if(cl1.equals(Integer.TYPE))
            return equals((int[])e1, (int[])e2);
        if(cl1.equals(Character.TYPE))
            return equals((char[])e1, (char[])e2);
        if(cl1.equals(Boolean.TYPE))
            return equals((boolean[])e1, (boolean[])e2);
        if(cl1.equals(Byte.TYPE))
            return equals((byte[])e1, (byte[])e2);
        if(cl1.equals(Long.TYPE))
            return equals((long[])e1, (long[])e2);
        if(cl1.equals(Float.TYPE))
            return equals((float[])e1, (float[])e2);
        if(cl1.equals(Double.TYPE))
            return equals((double[])e1, (double[])e2);
        else
            return equals((short[])e1, (short[])e2);
    }

    public static String toString(boolean array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 7);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(byte array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 6);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(char array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 3);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(double array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 7);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(float array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 7);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(int array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 6);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(long array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 6);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(short array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 6);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String toString(Object array[])
    {
        if(array == null)
            return "null";
        if(array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length * 7);
        sb.append('[');
        sb.append(array[0]);
        for(int i = 1; i < array.length; i++)
        {
            sb.append(", ");
            sb.append(array[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    public static String deepToString(Object array[])
    {
        if(array == null)
        {
            return "null";
        } else
        {
            StringBuilder buf = new StringBuilder(array.length * 9);
            deepToStringImpl(array, new Object[] {
                array
            }, buf);
            return buf.toString();
        }
    }

    private static void deepToStringImpl(Object array[], Object origArrays[], StringBuilder sb)
    {
        if(array == null)
        {
            sb.append("null");
            return;
        }
        sb.append('[');
        for(int i = 0; i < array.length; i++)
        {
            if(i != 0)
                sb.append(", ");
            Object elem = array[i];
            if(elem == null)
            {
                sb.append("null");
            } else
            {
                Class elemClass = elem.getClass();
                if(elemClass.isArray())
                {
                    Class elemElemClass = elemClass.getComponentType();
                    if(elemElemClass.isPrimitive())
                    {
                        if(Boolean.TYPE.equals(elemElemClass))
                            sb.append(toString((boolean[])elem));
                        else
                        if(Byte.TYPE.equals(elemElemClass))
                            sb.append(toString((byte[])elem));
                        else
                        if(Character.TYPE.equals(elemElemClass))
                            sb.append(toString((char[])elem));
                        else
                        if(Double.TYPE.equals(elemElemClass))
                            sb.append(toString((double[])elem));
                        else
                        if(Float.TYPE.equals(elemElemClass))
                            sb.append(toString((float[])elem));
                        else
                        if(Integer.TYPE.equals(elemElemClass))
                            sb.append(toString((int[])elem));
                        else
                        if(Long.TYPE.equals(elemElemClass))
                            sb.append(toString((long[])elem));
                        else
                        if(Short.TYPE.equals(elemElemClass))
                            sb.append(toString((short[])elem));
                        else
                            throw new AssertionError();
                    } else
                    {
                        if(!$assertionsDisabled && !(elem instanceof Object[]))
                            throw new AssertionError();
                        if(deepToStringImplContains(origArrays, elem))
                        {
                            sb.append("[...]");
                        } else
                        {
                            Object newArray[] = (Object[])elem;
                            Object newOrigArrays[] = new Object[origArrays.length + 1];
                            System.arraycopy(((Object) (origArrays)), 0, ((Object) (newOrigArrays)), 0, origArrays.length);
                            newOrigArrays[origArrays.length] = ((Object) (newArray));
                            deepToStringImpl(newArray, newOrigArrays, sb);
                        }
                    }
                } else
                {
                    sb.append(array[i]);
                }
            }
        }

        sb.append(']');
    }

    private static boolean deepToStringImplContains(Object origArrays[], Object array)
    {
        if(origArrays == null || origArrays.length == 0)
            return false;
        Object aobj[];
        int j = (aobj = origArrays).length;
        for(int i = 0; i < j; i++)
        {
            Object element = aobj[i];
            if(element == array)
                return true;
        }

        return false;
    }

    public static boolean[] copyOf(boolean original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static byte[] copyOf(byte original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static char[] copyOf(char original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static double[] copyOf(double original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static float[] copyOf(float original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static int[] copyOf(int original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static long[] copyOf(long original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static short[] copyOf(short original[], int newLength)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static Object[] copyOf(Object original[], int newLength)
    {
        if(original == null)
            throw new NullPointerException();
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength);
    }

    public static Object[] copyOf(Object original[], int newLength, Class newType)
    {
        if(newLength < 0)
            throw new NegativeArraySizeException();
        else
            return copyOfRange(original, 0, newLength, newType);
    }

    public static boolean[] copyOfRange(boolean original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            boolean result[] = new boolean[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static byte[] copyOfRange(byte original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            byte result[] = new byte[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static char[] copyOfRange(char original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            char result[] = new char[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static double[] copyOfRange(double original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            double result[] = new double[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static float[] copyOfRange(float original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            float result[] = new float[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static int[] copyOfRange(int original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            int result[] = new int[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static long[] copyOfRange(long original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            long result[] = new long[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static short[] copyOfRange(short original[], int start, int end)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            short result[] = new short[resultLength];
            System.arraycopy(original, start, result, 0, copyLength);
            return result;
        }
    }

    public static Object[] copyOfRange(Object original[], int start, int end)
    {
        int originalLength = original.length;
        if(start > end)
            throw new IllegalArgumentException();
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            Object result[] = (Object[])Array.newInstance(((Object) (original)).getClass().getComponentType(), resultLength);
            System.arraycopy(((Object) (original)), start, ((Object) (result)), 0, copyLength);
            return result;
        }
    }

    public static Object[] copyOfRange(Object original[], int start, int end, Class newType)
    {
        if(start > end)
            throw new IllegalArgumentException();
        int originalLength = original.length;
        if(start < 0 || start > originalLength)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            int resultLength = end - start;
            int copyLength = Math.min(resultLength, originalLength - start);
            Object result[] = (Object[])Array.newInstance(newType.getComponentType(), resultLength);
            System.arraycopy(((Object) (original)), start, ((Object) (result)), 0, copyLength);
            return result;
        }
    }

    static final boolean $assertionsDisabled = !net/tsz/afinal/core/Arrays.desiredAssertionStatus();

}
