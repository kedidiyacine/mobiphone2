package com.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Object invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] args) {
        try {
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
            return method.invoke(target, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }

    public static void setPropertyValue(Object target, String propertyName, Object value) {

        try {
            Method method = target.getClass().getDeclaredMethod("set" +
                    StringUtils.capitalizeWord(propertyName),
                    value.getClass());
            method.invoke(target, value);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

}
