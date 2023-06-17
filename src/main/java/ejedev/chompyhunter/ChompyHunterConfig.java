package ejedev.chompyhunter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chompyhunter")
public interface ChompyHunterConfig extends Config
{
    @ConfigItem(
            keyName = "notifyChompy",
            name = "Notify on spawn",
            description = "Sends a notification when a chompy spawns",
            position = 1
    )
    default boolean notifyChompySpawn()
    {
        return false;
    }
    @ConfigItem(
        keyName = "autoHide",
        name = "Autohide",
        description = "Hide plugin when not in Castle Wars or Feldip Hiills regions",
        position = 2
    )
default boolean autoHide()
{
    return true;
}
@ConfigItem(
        keyName = "autoHideTimeout",
        name = "Autohide Timeout",
        description = "Timeout in seconds when leaving region",
        position = 3
)
default int autoHideTimeout()
{
    return 60;
}
}