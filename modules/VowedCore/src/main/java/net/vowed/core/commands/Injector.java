package net.vowed.core.commands;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by JPaul on 8/20/2016.
 */
public interface Injector
{
    public Object getInstance(Class<?> clazz) throws InvocationTargetException, IllegalAccessException, InstantiationException;
}
