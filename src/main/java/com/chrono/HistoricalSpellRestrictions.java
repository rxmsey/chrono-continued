package com.chrono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public final class HistoricalSpellRestrictions
{
    private static final LocalDate ANCIENTS = LocalDate.of(2005, 4, 18);
    private static final LocalDate DREAM_MENTOR = LocalDate.of(2007, 5, 15);

    private static final Set<String> ANCIENT = new HashSet<>(Arrays.asList(
        "Smoke Rush", "Shadow Rush", "Blood Rush", "Ice Rush",
        "Smoke Burst", "Shadow Burst", "Blood Burst", "Ice Burst",
        "Smoke Blitz", "Shadow Blitz", "Blood Blitz", "Ice Blitz",
        "Smoke Barrage", "Shadow Barrage", "Blood Barrage", "Ice Barrage",
        "Paddewwa Teleport", "Senntisten Teleport", "Kharyrll Teleport", "Lassar Teleport",
        "Dareeyak Teleport", "Carrallangar Teleport", "Annakarl Teleport", "Ghorrock Teleport"
    ));

    private static final Set<String> DREAM = new HashSet<>(Arrays.asList(
        "Monster Examine", "Humidify", "Hunter Kit", "Stat Spy", "Dream", "Plank Make", "Spellbook Swap"
    ));

    private HistoricalSpellRestrictions() {}

    public static boolean allowed(String menuTarget, Date selectedDate)
    {
        LocalDate selected = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String target = menuTarget == null ? "" : menuTarget.replaceAll("<[^>]*>", "");

        for (String spell : ANCIENT)
        {
            if (target.contains(spell))
            {
                return !selected.isBefore(ANCIENTS);
            }
        }

        for (String spell : DREAM)
        {
            if (target.contains(spell))
            {
                return !selected.isBefore(DREAM_MENTOR);
            }
        }

        return true;
    }
}
