package com.chrono;

import lombok.Getter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Getter
public class EntityDefinition {
    private int id;
    private String name;
    private String releaseDate;
    private String lastUpdated;
    static Map<Integer, EntityDefinition> itemDefinitions;
    static Map<Integer, EntityDefinition> monsterDefinition;
    static Map<Integer, String> itemReleaseOverrides = Collections.emptyMap();

    // Unknown IDs and missing/invalid dates remain locked. IDs are never used as a date proxy.
    static boolean isUnlocked(EntityDefinition def, Date selected) {
        return isUnlockedDate(def == null ? null : def.releaseDate, selected);
    }

    private static boolean isUnlockedDate(String releaseDate, Date selected) {
        if (releaseDate == null || selected == null) return false;
        try {
            LocalDate introduced = LocalDate.parse(releaseDate);
            LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return HistoricalCutoff.isSupported(introduced) && !introduced.isAfter(date);
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static boolean isItemUnlocked(int id, Date selected) throws java.text.ParseException {
        EntityDefinition def = itemDefinitions == null ? null : itemDefinitions.get(id);
        if (def == null) return false;

        String release = def.releaseDate;
        if (release == null && itemReleaseOverrides != null) {
            release = itemReleaseOverrides.get(id);
        }
        return isUnlockedDate(release, selected);
    }

    public static boolean isMonsterUnlocked(int id, Date selected) throws java.text.ParseException {
        return isUnlocked(monsterDefinition == null ? null : monsterDefinition.get(id), selected);
    }
}
