package com.rewind.regionlocker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.runelite.api.coords.WorldPoint;

public final class HistoricalRegionState
{
    private static final Set<Integer> unlocked = new HashSet<>();
    private static final Set<Integer> additionallyUnlocked = new HashSet<>();
    private static final Map<Integer, LocalDate> timedRegions = new HashMap<>();
    private static final Set<Integer> legacyAuxiliaryRegions = new HashSet<>(java.util.Arrays.asList(
        12437 // Wizards' Tower basement; present for The Restless Ghost in the original 2001 world.
    ));
    private static LocalDate selected = LocalDate.of(2005, 1, 31);
    private static boolean bypassRestrictions;

    private HistoricalRegionState() {}

    public static synchronized void setSelectedDate(Date date)
    {
        selected = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static synchronized void replaceWith(Collection<Integer> regions)
    {
        unlocked.clear();
        if (regions != null)
        {
            unlocked.addAll(regions);
        }
    }

    public static synchronized void setAdditionallyUnlocked(Collection<Integer> regions)
    {
        additionallyUnlocked.clear();
        if (regions != null)
        {
            additionallyUnlocked.addAll(regions);
        }
    }

    public static synchronized void clearRegionGates()
    {
        timedRegions.clear();
    }

    public static synchronized void gateRegion(int regionId, LocalDate release)
    {
        timedRegions.merge(regionId, release, (existing, incoming) ->
            existing.isBefore(incoming) ? existing : incoming);
    }

    public static synchronized void setBypassRestrictions(boolean bypass)
    {
        bypassRestrictions = bypass;
    }

    public static synchronized boolean isTileUnlocked(WorldPoint point)
    {
        return isRegionUnlocked(point.getRegionID());
    }

    public static synchronized boolean isRegionUnlocked(int regionId)
    {
        if (bypassRestrictions)
        {
            return true;
        }

        // User-forced exceptions (currently the Grand Exchange) are true overrides:
        // they must win over both release-list absence and any future timed gate.
        if (additionallyUnlocked.contains(regionId))
        {
            return true;
        }

        LocalDate gate = timedRegions.get(regionId);
        if (gate != null)
        {
            return !selected.isBefore(gate);
        }

        if (isAuxiliaryUndergroundRegion(regionId))
        {
            return true;
        }

        return unlocked.contains(regionId);
    }

    static boolean isAuxiliaryUndergroundRegion(int regionId)
    {
        return legacyAuxiliaryRegions.contains(regionId);
    }
}
