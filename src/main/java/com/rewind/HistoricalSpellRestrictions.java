package com.rewind;

import java.util.List;
import java.util.Locale;

/** Exact spell identities: substrings such as Vengeance must not unlock Vengeance Other. */
public final class HistoricalSpellRestrictions {
    private HistoricalSpellRestrictions() {}

    public static String clean(String text) {
        if (text == null) return "";
        return text.replaceAll("<[^>]*>", "").replace('\u00a0', ' ').trim();
    }

    public static boolean allowed(String target, List<RewindSpell> unlocked) {
        String name = clean(target).split(" -> ", 2)[0].trim();
        // Client spelling variants refer to the same historical spell.
        if (name.equals("Teleport Block")) name = "Tele Block";
        if (name.equals("Enchant Crossbow Bolts")) name = "Enchant Crossbow Bolt";
        for (RewindSpell spell : unlocked) if (spell.getName().equalsIgnoreCase(name)) return true;
        return false;
    }

    public static boolean allowedWidget(int packedId, List<RewindSpell> unlocked) {
        return unlocked.stream().anyMatch(spell -> spell.getPackedID() == packedId);
    }
}
