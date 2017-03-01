// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FinalActivity.java

package net.tsz.afinal;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import java.lang.reflect.Field;
import net.tsz.afinal.annotation.view.EventListener;
import net.tsz.afinal.annotation.view.Select;
import net.tsz.afinal.annotation.view.ViewInject;

public class FinalActivity extends Activity
{

    public FinalActivity()
    {
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        initInjectedView(this);
    }

    public void setContentView(View view, android.view.ViewGroup.LayoutParams params)
    {
        super.setContentView(view, params);
        initInjectedView(this);
    }

    public void setContentView(View view)
    {
        super.setContentView(view);
        initInjectedView(this);
    }

    public static void initInjectedView(Activity sourceActivity)
    {
        initInjectedView(sourceActivity, sourceActivity.getWindow().getDecorView());
    }

    public static void initInjectedView(Object injectedSource, View sourceView)
    {
        Field fields[] = injectedSource.getClass().getDeclaredFields();
        if(fields != null && fields.length > 0)
        {
            Field afield[];
            int j = (afield = fields).length;
            for(int i = 0; i < j; i++)
            {
                Field field = afield[i];
                ViewInject viewInject = (ViewInject)field.getAnnotation(net/tsz/afinal/annotation/view/ViewInject);
                if(viewInject != null)
                {
                    int viewId = viewInject.id();
                    try
                    {
                        field.setAccessible(true);
                        field.set(injectedSource, sourceView.findViewById(viewId));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    String clickMethod = viewInject.click();
                    if(!TextUtils.isEmpty(clickMethod))
                        setViewClickListener(sourceView, field, clickMethod);
                    String longClickMethod = viewInject.longClick();
                    if(!TextUtils.isEmpty(longClickMethod))
                        setViewLongClickListener(sourceView, field, longClickMethod);
                    String itemClickMethod = viewInject.itemClick();
                    if(!TextUtils.isEmpty(itemClickMethod))
                        setItemClickListener(sourceView, field, itemClickMethod);
                    String itemLongClickMethod = viewInject.itemLongClick();
                    if(!TextUtils.isEmpty(itemLongClickMethod))
                        setItemLongClickListener(sourceView, field, itemLongClickMethod);
                    Select select = viewInject.select();
                    if(!TextUtils.isEmpty(select.selected()))
                        setViewSelectListener(sourceView, field, select.selected(), select.noSelected());
                }
            }

        }
    }

    private static void setViewClickListener(Object injectedSource, Field field, String clickMethod)
    {
        try
        {
            Object obj = field.get(injectedSource);
            if(obj instanceof View)
                ((View)obj).setOnClickListener((new EventListener(injectedSource)).click(clickMethod));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void setViewLongClickListener(Object injectedSource, Field field, String clickMethod)
    {
        try
        {
            Object obj = field.get(injectedSource);
            if(obj instanceof View)
                ((View)obj).setOnLongClickListener((new EventListener(injectedSource)).longClick(clickMethod));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void setItemClickListener(Object injectedSource, Field field, String itemClickMethod)
    {
        try
        {
            Object obj = field.get(injectedSource);
            if(obj instanceof AbsListView)
                ((AbsListView)obj).setOnItemClickListener((new EventListener(injectedSource)).itemClick(itemClickMethod));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void setItemLongClickListener(Object injectedSource, Field field, String itemClickMethod)
    {
        try
        {
            Object obj = field.get(injectedSource);
            if(obj instanceof AbsListView)
                ((AbsListView)obj).setOnItemLongClickListener((new EventListener(injectedSource)).itemLongClick(itemClickMethod));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void setViewSelectListener(Object injectedSource, Field field, String select, String noSelect)
    {
        try
        {
            Object obj = field.get(injectedSource);
            if(obj instanceof View)
                ((AbsListView)obj).setOnItemSelectedListener((new EventListener(injectedSource)).select(select).noSelect(noSelect));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
