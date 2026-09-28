package com.rewind;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(RewindPlugin.CONFIG_GROUP_KEY)
public interface RewindConfig extends Config
{
	@ConfigSection(
			name = "Set date to travel back to",
			description = "General settings for the plugin",
			position = 0
	)
	String generalSettings = "generalSettings";

	@ConfigSection(
			name = "Environment Looks",
			description = "Settings relating to locked regions look",
			position = 1
	)
	String environmentSettings = "environmentSettings";

	@ConfigSection(
			name = "Map Settings",
			description = "Settings relating to the map overlay",
			position = 2
	)
	String mapSettings = "mapSettings";

	// General Settings

	@ConfigItem(
			keyName = RewindPlugin.CONFIG_RELEASE_DATE_KEY,
			name = "Date",
			description = "Release you are restricted to",
			position = 1,
			section = generalSettings
	)
	default ReleaseDate release()
	{
		return ReleaseDate.JANUARY_2001;
	}

	@ConfigItem(
			keyName = "unlockGrandExchange",
			name = "Unlock Grand Exchange",
			description = "Allow access to the Grand Exchange while keeping items newer than the selected date blocked",
			position = 2,
			section = generalSettings
	)
	default boolean unlockGrandExchange()
	{
		return false;
	}

	// Environment Looks

	@ConfigItem(
			keyName = "maskLockedScene",
			name = "Mask locked world",
			description = "Cover historically unavailable world tiles using Locked region color",
			position = 19,
			section = environmentSettings
	)
	default boolean maskLockedScene()
	{
		return true;
	}

	@ConfigItem(
			keyName = "maskLockedMinimap",
			name = "Mask locked minimap",
			description = "Cover historically unavailable minimap tiles using Locked region color",
			position = 20,
			section = environmentSettings
	)
	default boolean maskLockedMinimap()
	{
		return true;
	}


	@ConfigItem(
			keyName = "renderLockedRegions",
			name = "Locked chunk shader",
			hidden = true,
			description = "Adds graphical change to all chunk that are locked",
			position = 21,
			section = environmentSettings
	)
	default boolean renderLockedRegions()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "shaderGrayColor",
			name = "Locked region color",
			description = "Color and opacity of locked regions in the world and minimap; changes apply immediately",
			position = 22,
			section = environmentSettings
	)
	default Color shaderGrayColor()
	{
		return new Color(0, 31, 77, 204);
	}

	@Alpha
	@ConfigItem(
			keyName = "shaderGrayAmount",
			name = "Chunk shader opacity",
			hidden = true,
			description = "The amount of gray scale that is applied to a locked chunk in the shader (alpha only)",
			position = 23,
			section = environmentSettings
	)
	default Color shaderGrayAmount()
	{
		return new Color(0, 0, 0, 204);
	}

	@ConfigItem(
			keyName = "hardBorder",
			name = "Hard chunk border",
			hidden = true,
			description = "True = hard border cutoff, False = chunk border gradient",
			position = 24,
			section = environmentSettings
	)
	default boolean hardBorder()
	{
		return true;
	}

	@ConfigItem(
			keyName = "renderRegionBorders",
			name = "Draw chunk border lines",
			description = "Draw the chunk borders in the environment marked by lines",
			position = 25,
			section = environmentSettings
	)
	default boolean renderRegionBorders()
	{
		return false;
	}

	@ConfigItem(
			keyName = "regionBorderWidth",
			name = "Chunk border width",
			description = "How wide the region border will be",
			position = 26,
			section = environmentSettings
	)
	default int regionBorderWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
			keyName = "regionBorderColor",
			name = "Chunk border color",
			description = "The color of the chunk borders",
			position = 27,
			section = environmentSettings
	)
	default Color regionBorderColor()
	{
		return new Color(0, 200, 83, 200);
	}

	// Map Settings

	@ConfigItem(
			keyName = "drawMapOverlay",
			name = "Draw chunks on map",
			description = "Draw a color overlay for each locked/unlocked chunk",
			position = 28,
			section = mapSettings
	)
	default boolean drawMapOverlay()
	{
		return true;
	}

	@ConfigItem(
			keyName = "invertMapOverlay",
			name = "Invert map overlay",
			description = "Switches which chunks the map will draw the color overlay for (true = locked, false = unlocked)",
			position = 29,
			section = mapSettings
	)
	default boolean invertMapOverlay()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "mapOverlayColor",
			name = "Map overlay color",
			description = "The color the map overlay will draw the chunks in",
			position = 30,
			section = mapSettings
	)
	default Color mapOverlayColor()
	{
		return new Color(0, 0, 0, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "unlockableOverlayColor",
			name = "Unlockable overlay color",
			description = "The color the map overlay will draw the unlockable chunks in",
			position = 31,
			section = mapSettings
	)
	default Color unlockableOverlayColor()
	{
		return new Color(60, 200, 160, 100);
	}

	@Alpha
	@ConfigItem(
			keyName = "blacklistedOverlayColor",
			name = "Blacklisted overlay color",
			description = "The color the map overlay will draw the blacklisted chunks in",
			position = 32,
			section = mapSettings
	)
	default Color blacklistedOverlayColor()
	{
		return new Color(0, 0, 0, 255);
	}

	@ConfigItem(
			keyName = "drawMapGrid",
			name = "Draw map grid",
			description = "Draw the grid of chunks on the map",
			position = 33,
			section = mapSettings
	)
	default boolean drawMapGrid()
	{
		return false;
	}

	@ConfigItem(
			keyName = "drawRegionId",
			name = "Draw region IDs",
			description = "Draw the chunk ID for each chunk on the map",
			position = 34,
			section = mapSettings
	)
	default boolean drawRegionId()
	{
		return true;
	}

	@ConfigItem(
			keyName = "tierFilter",
			name = "Tier Filter",
			description = "Filter by tier in the plugin panel",
			hidden = true
	)
	default String tierFilter() { return "1,2,3,4,5"; }

	@ConfigItem(
			keyName = "regionFilter",
			name = "Region Filter",
			description = "Filter by region in the plugin panel",
			hidden = true
	)
	default String regionFilter() { return "Misthalin,Desert,Kandarin,Asgarnia,Fremennik,Tirannwn,Karamja,Morytania,Wilderness,Lower Zeah,Upper Zeah,Kebos"; }

	@ConfigItem(
			keyName = "showCompletedTasks",
			name = "Show Completed Tasks",
			description = "Show completed tasks in the plugin panel",
			hidden = true
	)
	default boolean showCompletedTasks() { return true; }
}
