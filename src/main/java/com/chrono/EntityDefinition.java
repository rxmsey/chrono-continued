package com.chrono;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Slf4j
public class EntityDefinition {
    private int id;
    private String name;
    private String releaseDate;
    private String lastUpdated;

    static Map<Integer, EntityDefinition> itemDefinitions;
    static Map<Integer, EntityDefinition> monsterDefinition;

    public static boolean isItemUnlocked(int itemId, Date release) throws ParseException {
        EntityDefinition def = itemDefinitions.get(itemId);

        if(def == null) return false;
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(def.getReleaseDate());
        return releaseDate.before(release);
    }

    public static boolean isMonsterUnlocked(int npcId, Date release) throws ParseException {
        EntityDefinition def = monsterDefinition.get(npcId);

        if(def == null) return true;

        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(def.getReleaseDate());
        return releaseDate.before(release);
    }
}
