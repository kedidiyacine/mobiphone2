package com.utils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

    // This map stores the mapping between property names and their corresponding
    // types
    private static final Map<String, Class<?>> propertyTypeMap = new HashMap<>();

    // Populate the propertyTypeMap with property names and types
    static {
        propertyTypeMap.put("id", Long.class);
        propertyTypeMap.put("type", String.class);
        propertyTypeMap.put("libelle", String.class);
        propertyTypeMap.put("prix_vente", Double.class);
        propertyTypeMap.put("qt_stock", int.class);
        propertyTypeMap.put("date_creation", LocalDateTime.class);
        propertyTypeMap.put("date_maj", LocalDateTime.class);
        propertyTypeMap.put("reference", String.class); // Additional property in TelephoneMobile
        propertyTypeMap.put("marque", String.class); // Additional property in TelephoneMobile
        propertyTypeMap.put("modele", String.class); // Additional property in TelephoneMobile
    }

    public static Class<?> getPropertyType(Class<?> clazz, String propertyName) {
        // Check if the property is present in the propertyTypeMap
        if (propertyTypeMap.containsKey(propertyName)) {
            return propertyTypeMap.get(propertyName);
        }

        // If not found, use reflection to get the type from the declared methods
        try {
            String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() +
                    propertyName.substring(1);
            Method getterMethod = clazz.getDeclaredMethod(getterMethodName);
            return getterMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }

    public static Object convertToCorrectType(String newValue, Class<?> targetType) {
        // Implement conversion logic based on the target type
        if (targetType == Long.class) {
            return Long.parseLong(newValue);
        } else if (targetType == Double.class) {
            return Double.parseDouble(newValue);
        } else if (targetType == int.class) {
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
