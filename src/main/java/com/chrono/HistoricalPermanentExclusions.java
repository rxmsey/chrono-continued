package com.chrono;

import net.runelite.api.Skill;

public final class HistoricalPermanentExclusions
{
    private HistoricalPermanentExclusions() {}

    public static boolean isSkillPermanentlyLocked(Skill skill)
    {
        return skill == Skill.SAILING;
    }

    public static boolean isSailingMenuAction(String option, String target)
    {
        String o = option == null ? "" : option.toLowerCase();
        String t = target == null ? "" : target.replaceAll("<[^>]*>", "").toLowerCase();
        return o.contains("sailing") || t.contains("sailing") || t.contains("sailing boat");
    }
}
