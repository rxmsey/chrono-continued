package com.chrono;

import lombok.Getter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
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

    // Unknown IDs and missing/invalid dates remain locked. IDs are never used as a date proxy.
    static boolean isUnlocked(EntityDefinition def, Date selected) {
        if (def == null || def.releaseDate == null || selected == null) return false;
        try {
            LocalDate introduced = LocalDate.parse(def.releaseDate);
            LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return HistoricalCutoff.isSupported(introduced) && !introduced.isAfter(date);
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static boolean isItemUnlocked(int id, Date selected) throws java.text.ParseException {
        return isUnlocked(itemDefinitions == null ? null : itemDefinitions.get(id), selected);
    }
    public static boolean isMonsterUnlocked(int id, Date selected) throws java.text.ParseException {
        return isUnlocked(monsterDefinition == null ? null : monsterDefinition.get(id), selected);
    }
}
