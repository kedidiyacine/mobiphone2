package com.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils<T> {

    // This map stores the mapping between class names and property names/types
    private final Map<String, Map<String, Class<?>>> classPropertyTypeMap = new HashMap<>();

    // Populate the classPropertyTypeMap with class names, property names, and types
    public ReflectionUtils() {

        // Article
        Map<String, Class<?>> articleProperties = new HashMap<>();
        articleProperties.put("id", Long.class);
        articleProperties.put("type", String.class);
        articleProperties.put("libelle", String.class);
        articleProperties.put("prix_vente", Double.class);
        articleProperties.put("qt_stock", Integer.class);
        articleProperties.put("date_creation", LocalDateTime.class);
        articleProperties.put("date_maj", LocalDateTime.class);
        classPropertyTypeMap.put("BaseArticle", articleProperties);

        // TelephoneMobile
        Map<String, Class<?>> telephoneMobileProperties = new HashMap<>();
        telephoneMobileProperties.put("reference", String.class);
        telephoneMobileProperties.put("marque", String.class);
        telephoneMobileProperties.put("modele", String.class);
        classPropertyTypeMap.put("TelephoneMobile", telephoneMobileProperties);

        // LigneTelephonique
        Map<String, Class<?>> ligneTelephoniqueroperties = new HashMap<>();
        ligneTelephoniqueroperties.put("numero", String.class);
        ligneTelephoniqueroperties.put("operateur", String.class);
        ligneTelephoniqueroperties.put("montant_min_consommation", Double.class);
        classPropertyTypeMap.put("LigneTelephonique", ligneTelephoniqueroperties);

        // Client
        Map<String, Class<?>> clientProperties = new HashMap<>();
        clientProperties.put("cin", String.class);
        clientProperties.put("nom", String.class);
        clientProperties.put("prenom", String.class);
        clientProperties.put("adresse_de_livraison", String.class);
        clientProperties.put("email", String.class);
        classPropertyTypeMap.put("Client", clientProperties);
    }

    public Class<?> getPropertyType(Class<?> clazz, String propertyName) {
        return getPropertyTypeRecursively(clazz, propertyName);
    }

    @SuppressWarnings("unchecked")
    private Class<?> getPropertyTypeRecursively(Class<?> clazz, String propertyName) {
        String className = clazz.getSimpleName();

        // Check if the class is present in the classPropertyTypeMap
        if (classPropertyTypeMap.containsKey(className)) {
            Map<String, Class<?>> propertyTypeMap = classPropertyTypeMap.get(className);

            // Check if the property is present in the propertyTypeMap
            if (propertyTypeMap.containsKey(propertyName)) {
                return propertyTypeMap.get(propertyName);
            }
        }

        // still have errors !

        // If not found, check the superclass recursively
        Class<? super T> superClass = (Class<? super T>) clazz.getSuperclass();
        if (superClass != null) {
            return getPropertyTypeRecursively(superClass, propertyName);
        }

        // If no superclass and property not found, use reflection
        try {
            String getterMethodName = "get" + StringUtils.capitalizeWord(propertyName);
            Method getterMethod = clazz.getDeclaredMethod(getterMethodName);
            return getterMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }

    public Object invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] args) {
        try {
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
            return method.invoke(target, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }

    public void setPropertyValue(Object target, String propertyName, Object value) {
        try {
            Method method = target.getClass().getDeclaredMethod("set" +
                    StringUtils.capitalizeWord(propertyName),
                    value.getClass());
            method.invoke(target, value);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public Object convertToCorrectType(String newValue, Class<?> targetType) {
        // Implement conversion logic based on the target type
        if (targetType == Long.class) {
            return Long.parseLong(newValue);
        } else if (targetType == Double.class) {
            return Double.parseDouble(newValue);
        } else if (targetType == Integer.class) {
            return Integer.parseInt(newValue);
        } else if (targetType == LocalDateTime.class) {
            // Assuming LocalDateTime parsing logic
            return LocalDateTime.parse(newValue);
        } else {
            // Default case: return the original string
            return newValue;
        }
    }
}
