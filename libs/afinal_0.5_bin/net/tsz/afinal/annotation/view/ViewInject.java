// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ViewInject.java

package net.tsz.afinal.annotation.view;

import java.lang.annotation.Annotation;

// Referenced classes of package net.tsz.afinal.annotation.view:
//            Select

public interface ViewInject
    extends Annotation
{

    public abstract int id();

    public abstract String click();

    public abstract String longClick();

    public abstract String itemClick();

    public abstract String itemLongClick();

    public abstract Select select();
}
