package at.helpch.placeholderapi.expansion.attribute.util;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class AttributeUtil {

    private static Method valuesMethod = null;
    private static Method keyMethod = null;
    private static Method nameMethod = null;

    static {
        try {
            valuesMethod = Class.forName("org.bukkit.attribute.Attribute").getMethod("values");
            keyMethod = Class.forName("org.bukkit.attribute.Attribute").getMethod("getKey");
            nameMethod = Class.forName("org.bukkit.attribute.Attribute").getMethod("name");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private AttributeUtil() {}

    /*
    Use reflection as since 1.21, Attribute class has changed to an Interface causing IncompatibleClassChangeError
    This allows backwards compatibility
     */
    public static Attribute[] getAttributes() {
        if (valuesMethod == null) {
            return new Attribute[]{};
        }
        try {
            return (Attribute[]) valuesMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Attribute.values();
    }

    public static NamespacedKey getKey(final Attribute attribute) {
        try {
            return (NamespacedKey) keyMethod.invoke(attribute);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    public static String getName(final Attribute attribute) {
        try {
            return (String) nameMethod.invoke(attribute);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
