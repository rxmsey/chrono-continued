package com.rewind;

import net.runelite.client.config.ConfigGroup;
import net.runelite.client.plugins.PluginDescriptor;
import org.junit.Test;
import static org.junit.Assert.*;

public class RebrandCompatibilityTest
{
    @Test public void publicNameAndSavedKeysAreIndependent()
    {
        PluginDescriptor descriptor = RewindPlugin.class.getAnnotation(PluginDescriptor.class);
        assertEquals("Rewind", descriptor.name());
        assertEquals("Experience Old School RuneScape through its historical timeline.", descriptor.description());
        assertEquals("chronoplugin", descriptor.configName());
        assertEquals("chrono", RewindConfig.class.getAnnotation(ConfigGroup.class).value());
        assertEquals("releasedate", RewindPlugin.CONFIG_RELEASE_DATE_KEY);
    }

    @Test public void renamedPackageStillResolvesBundledResources()
    {
        for (String resource : new String[]{"panel_icon.png", "releases.json", "releases-2005-2007.json",
            "items.json", "monsters.json", "item-release-overrides.json", "sailing-objects.json"})
        {
            assertNotNull(resource, RewindPlugin.class.getResource(resource));
        }
    }
}
