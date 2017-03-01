// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ManyToOne.java

package net.tsz.afinal.annotation.sqlite;

import java.lang.annotation.Annotation;

public interface ManyToOne
    extends Annotation
{

    public abstract String column();
}
