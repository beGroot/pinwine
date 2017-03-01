// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EventListener.java

package net.tsz.afinal.annotation.view;

import android.view.View;
import android.widget.AdapterView;
import java.lang.reflect.Method;
import net.tsz.afinal.exception.ViewException;

public class EventListener
    implements android.view.View.OnClickListener, android.view.View.OnLongClickListener, android.widget.AdapterView.OnItemClickListener, android.widget.AdapterView.OnItemSelectedListener, android.widget.AdapterView.OnItemLongClickListener
{

    public EventListener(Object handler)
    {
        this.handler = handler;
    }

    public EventListener click(String method)
    {
        clickMethod = method;
        return this;
    }

    public EventListener longClick(String method)
    {
        longClickMethod = method;
        return this;
    }

    public EventListener itemLongClick(String method)
    {
        itemLongClickMehtod = method;
        return this;
    }

    public EventListener itemClick(String method)
    {
        itemClickMethod = method;
        return this;
    }

    public EventListener select(String method)
    {
        itemSelectMethod = method;
        return this;
    }

    public EventListener noSelect(String method)
    {
        nothingSelectedMethod = method;
        return this;
    }

    public boolean onLongClick(View v)
    {
        return invokeLongClickMethod(handler, longClickMethod, new Object[] {
            v
        });
    }

    public boolean onItemLongClick(AdapterView arg0, View arg1, int arg2, long arg3)
    {
        return invokeItemLongClickMethod(handler, itemLongClickMehtod, new Object[] {
            arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3)
        });
    }

    public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3)
    {
        invokeItemSelectMethod(handler, itemSelectMethod, new Object[] {
            arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3)
        });
    }

    public void onNothingSelected(AdapterView arg0)
    {
        invokeNoSelectMethod(handler, nothingSelectedMethod, new Object[] {
            arg0
        });
    }

    public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3)
    {
        invokeItemClickMethod(handler, itemClickMethod, new Object[] {
            arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3)
        });
    }

    public void onClick(View v)
    {
        invokeClickMethod(handler, clickMethod, new Object[] {
            v
        });
    }

    private static transient Object invokeClickMethod(Object handler, String methodName, Object params[])
    {
        if(handler == null)
            return null;
        Method method = null;
        Method method = handler.getClass().getDeclaredMethod(methodName, new Class[] {
            android/view/View
        });
        if(method != null)
            return method.invoke(handler, params);
        try
        {
            throw new ViewException((new StringBuilder("no such method:")).append(methodName).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static transient boolean invokeLongClickMethod(Object handler, String methodName, Object params[])
    {
        if(handler == null)
            return false;
        Method method = null;
        Method method = handler.getClass().getDeclaredMethod(methodName, new Class[] {
            android/view/View
        });
        if(method != null)
        {
            Object obj = method.invoke(handler, params);
            return obj != null ? Boolean.valueOf(obj.toString()).booleanValue() : false;
        }
        try
        {
            throw new ViewException((new StringBuilder("no such method:")).append(methodName).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private static transient Object invokeItemClickMethod(Object handler, String methodName, Object params[])
    {
        if(handler == null)
            return null;
        Method method = null;
        Method method = handler.getClass().getDeclaredMethod(methodName, new Class[] {
            android/widget/AdapterView, android/view/View, Integer.TYPE, Long.TYPE
        });
        if(method != null)
            return method.invoke(handler, params);
        try
        {
            throw new ViewException((new StringBuilder("no such method:")).append(methodName).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static transient boolean invokeItemLongClickMethod(Object handler, String methodName, Object params[])
    {
        if(handler == null)
            throw new ViewException("invokeItemLongClickMethod: handler is null :");
        Method method = null;
        Method method = handler.getClass().getDeclaredMethod(methodName, new Class[] {
            android/widget/AdapterView, android/view/View, Integer.TYPE, Long.TYPE
        });
        if(method != null)
        {
            Object obj = method.invoke(handler, params);
            return Boolean.valueOf(obj != null ? Boolean.valueOf(obj.toString()).booleanValue() : false).booleanValue();
        }
        try
        {
            throw new ViewException((new StringBuilder("no such method:")).append(methodName).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private static transient Object invokeItemSelectMethod(Object handler, String methodName, Object params[])
    {
        if(handler == null)
            return null;
        Method method = null;
        Method method = handler.getClass().getDeclaredMethod(methodName, new Class[] {
            android/widget/AdapterView, android/view/View, Integer.TYPE, Long.TYPE
        });
        if(method != null)
            return method.invoke(handler, params);
        try
        {
            throw new ViewException((new StringBuilder("no such method:")).append(methodName).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static transient Object invokeNoSelectMethod(Object handler, String methodName, Object params[])
    {
        if(handler == null)
            return null;
        Method method = null;
        Method method = handler.getClass().getDeclaredMethod(methodName, new Class[] {
            android/widget/AdapterView
        });
        if(method != null)
            return method.invoke(handler, params);
        try
        {
            throw new ViewException((new StringBuilder("no such method:")).append(methodName).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Object handler;
    private String clickMethod;
    private String longClickMethod;
    private String itemClickMethod;
    private String itemSelectMethod;
    private String nothingSelectedMethod;
    private String itemLongClickMehtod;
}
