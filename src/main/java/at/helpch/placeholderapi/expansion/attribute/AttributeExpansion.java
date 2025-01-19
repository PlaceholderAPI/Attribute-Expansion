package at.helpch.placeholderapi.expansion.attribute;

import at.helpch.placeholderapi.expansion.attribute.util.AttributeUtil;
import at.helpch.placeholderapi.expansion.attribute.util.ServerVersion;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Version;
import me.clip.placeholderapi.expansion.VersionSpecific;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class AttributeExpansion extends PlaceholderExpansion implements VersionSpecific {

    private final Map<String, Attribute> attributes = new HashMap<>();
    private final List<String> placeholders = new ArrayList<>();

    public AttributeExpansion() {
        for (final Attribute attribute : AttributeUtil.getAttributes()) {
            if (ServerVersion.HAS_KEYS) {
                attributes.put(AttributeUtil.getKey(attribute).getKey(), attribute);
            }

            attributes.put(AttributeUtil.getName(attribute), attribute);

        }

        placeholders.add("%attribute_player_has_<attribute>%");
        placeholders.add("%attribute_player_baseValue_<attribute>%");
        placeholders.add("%attribute_player_value_<attribute>%");

        if (ServerVersion.HAS_ATTRIBUTE_DEFAULT_VALUE) {
            placeholders.add("%attribute_player_defaultValue_<attribute>%");
        }
    }

    private String bool(boolean bool) {
        return bool ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "attribute";
    }

    @Override
    public @NotNull String getAuthor() {
        return "HelpChat";
    }

    @Override
    public @NotNull String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return placeholders;
    }

    @Override
    public boolean isCompatibleWith(Version version) {
        if (ServerVersion.HAS_ATTRIBUTES) {
            log(Level.INFO, "Available attributes: " + String.join(", ", attributes.keySet()));
        }

        return ServerVersion.HAS_ATTRIBUTES;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(@Nullable OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) {
            return null;
        }

        final Player player = offlinePlayer.getPlayer();

        if (player == null) {
            return null;
        }

        final String[] args = params.split("_", 3);

        if (args.length != 3) {
            return null;
        }

        final String type = args[0].toLowerCase();

        // TODO: 2022-02-13, Sun, 0:21 add support for items as well
        if (!type.equalsIgnoreCase("player")) {
            return "Unknown type " + type + ", available types: player";
        }

        final String method = args[1];
        final Attribute attribute = this.attributes.get(args[2]);

        if (attribute == null) {
            return "Unknown attribute " + args[2];
        }

        final AttributeInstance instance = player.getAttribute(attribute);

        if (method.equals("has")) {
            return bool(instance != null);
        }

        if (instance == null) {
            return null;
        }

        switch (method) {
            default: {
                return "Unknown method " + method;
            }

            // Available in all versions
            case "baseValue": {
                return Double.toString(instance.getBaseValue());
            }

            case "value": {
                return Double.toString(instance.getValue());
            }

            // 1.11+
            case "defaultValue": {
                if (ServerVersion.HAS_ATTRIBUTE_DEFAULT_VALUE) {
                    return Double.toString(instance.getDefaultValue());
                } else {
                    return "Not available in " + PlaceholderAPIPlugin.getServerVersion().getVersion();
                }
            }
        }
    }

}
