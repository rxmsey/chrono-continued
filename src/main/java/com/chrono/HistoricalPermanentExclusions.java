package com.chrono;

import net.runelite.api.Skill;

public final class HistoricalPermanentExclusions
{
    private HistoricalPermanentExclusions() {}

    public static boolean isSkillPermanentlyLocked(Skill skill)
    {
        return skill == Skill.SAILING;
    }

    public static boolean isSailingWidget(int packedId)
    {
        int group = packedId >>> 16;
        return packedId == net.runelite.api.gameval.InterfaceID.Stats.SAILING
            || group == net.runelite.api.gameval.InterfaceID.SAILING_MENU
            || group == net.runelite.api.gameval.InterfaceID.SAILING_INTRO_HUD
            || (group >= net.runelite.api.gameval.InterfaceID.SAILING_BT_HUD
                && group <= net.runelite.api.gameval.InterfaceID.SAILING_CUSTOMISATION)
            || group == net.runelite.api.gameval.InterfaceID.SAILING_BOAT_CARGOHOLD
            || group == net.runelite.api.gameval.InterfaceID.SAILING_BOAT_CARGOHOLD_SIDE;
    }

    public static boolean isSailingMenuAction(String option, String target)
    {
        String o = option == null ? "" : option.toLowerCase(java.util.Locale.ROOT);
        String t = target == null ? "" : target.replaceAll("<[^>]*>", "").toLowerCase(java.util.Locale.ROOT);
        return o.contains("sailing") || t.contains("sailing") || t.contains("sailing boat");
    }
}
