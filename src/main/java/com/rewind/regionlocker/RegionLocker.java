/*
 * Copyright (c) 2019, Slay to Stay <https://github.com/slaytostay>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rewind.regionlocker;

import java.awt.Color;
import java.util.*;

import com.rewind.RewindConfig;
import com.rewind.RewindPlugin;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.util.Text;

public class RegionLocker
{
	private final Client client;
	private final RewindConfig config;
	private final ConfigManager configManager;

	@Getter
	private static Map<String, RegionTypes> regions = new HashMap<>();

	private RewindPlugin rewindPlugin;

	public static boolean renderLockedRegions;
	public static Color grayColor;
	public static int grayAmount;
	public static boolean hardBorder;
	public static boolean invertShader;

	public RegionLocker(Client client, RewindConfig config, ConfigManager configManager, RewindPlugin plugin)
	{
		this.client = client;
		this.config = config;
		this.configManager = configManager;
		this.rewindPlugin = plugin;
		readConfig();
	}

	private List<String> StringToList(String s)
	{
		List<String> regs;
		if (s.isEmpty())
			regs = new ArrayList<>();
		else
			regs = new ArrayList<>(Text.fromCSV(s));
		return regs;
	}

	public void readConfig()
	{
		renderLockedRegions = config.renderLockedRegions();
		grayColor = config.shaderGrayColor();
		grayAmount = config.shaderGrayAmount().getAlpha();
		hardBorder = config.hardBorder();
		regions.clear();
	}

	private void setConfig()
	{
		List<String> unlockedRegions = new ArrayList<>();
		List<String> unlockableRegions = new ArrayList<>();
		List<String> blacklistedRegions = new ArrayList<>();

		rewindPlugin.getCurrentRelease().getRegions().forEach(e -> unlockedRegions.add(e+""));

		String csv = Text.toCSV(unlockedRegions);
		configManager.setConfiguration(RewindPlugin.CONFIG_GROUP_KEY, "unlockedRegions", csv);
	}

	public void setRegions(List<Integer> regs, RegionTypes type)
	{
		for (int id : regs)
		{
			regions.put(id+"", type);
		}
	}

	public void addRegion(int regionId)
	{
		String id = Integer.toString(regionId);
		RegionTypes type = regions.get(id);
		if (type == null)
			regions.put(id, RegionTypes.UNLOCKABLE);
		else if (type == RegionTypes.UNLOCKABLE)
			regions.put(id, RegionTypes.UNLOCKED);
		else
			regions.remove(id);
		setConfig();
	}

	public static RegionTypes getType(int regionId)
	{
        return HistoricalRegionState.isRegionUnlocked(regionId) ? RegionTypes.UNLOCKED : null;
    }

	public static boolean hasRegion(int regionId)
	{
		return HistoricalRegionState.isRegionUnlocked(regionId);
	}

	public static boolean isUnlockable(int regionId)
	{
		RegionTypes type = getType(regionId);
		if (type == null) return false;
		return type == RegionTypes.UNLOCKABLE;
	}

	public static boolean isBlacklisted(int regionId)
	{
		RegionTypes type = getType(regionId);
		if (type == null) return false;
		return type == RegionTypes.BLACKLISTED;
	}


	public static int getX(int id)
	{
		return ((id >> 8) << 6);
	}

	public static int getY(int id)
	{
		return ((id & 255) << 6);
	}
}