// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Select.java

package net.tsz.afinal.annotation.view;

import java.lang.annotation.Annotation;

public interface Select
    extends Annotation
{

    public abstract String selected();

    public abstract String noSelected();
}
