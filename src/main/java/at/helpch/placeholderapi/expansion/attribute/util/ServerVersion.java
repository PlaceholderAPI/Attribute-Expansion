package at.helpch.placeholderapi.expansion.attribute.util;

import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.attribute.AttributeInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Matt (<a href="https://github.com/ipsk">@ipsk</a>)
 */
public final class ServerVersion {

    private ServerVersion() throws InstantiationException {
        throw new InstantiationException("Class ServerVersion can not be instantiated");
    }

    private static final int CURRENT_VERSION = getCurrentVersion();

    /**
     * Whether the version has {@link org.bukkit.attribute.Attribute Attributes}
     * @since 1.9
     */
    public static final boolean HAS_ATTRIBUTES = CURRENT_VERSION >= 1_9_0;

    /**
     * Whether the version has {@link AttributeInstance#getDefaultValue()}
     * @since 1.11
     */
    public static final boolean HAS_ATTRIBUTE_DEFAULT_VALUE = CURRENT_VERSION >= 1_11_0;

    /**
     * Whether the version has {@link org.bukkit.Keyed Keys}
     * @see org.bukkit.Keyed
     * @see org.bukkit.NamespacedKey
     * @since 1.13
     */
    public static final boolean HAS_KEYS = CURRENT_VERSION >= 1_13_0;

    /**
     * Gets the current server version
     *
     * @return A protocol like number representing the version, for example 1.16.5 - 1165
     */
    private static int getCurrentVersion() {
        // No need to cache since will only run once
        final Matcher matcher = Pattern.compile("(?<version>\\d+\\.\\d+)(?<patch>\\.\\d+)?").matcher(Bukkit.getBukkitVersion());

        final StringBuilder stringBuilder = new StringBuilder();
        if (matcher.find()) {
            stringBuilder.append(matcher.group("version").replace(".", ""));
            final String patch = matcher.group("patch");
            if (patch == null) stringBuilder.append("0");
            else stringBuilder.append(patch.replace(".", ""));
        }

        //noinspection UnstableApiUsage
        final Integer version = Ints.tryParse(stringBuilder.toString());

        // Should never fail
        if (version == null) throw new IllegalArgumentException("Could not retrieve server version!");

        return version;
    }

}
