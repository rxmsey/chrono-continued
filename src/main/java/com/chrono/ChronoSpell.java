package com.chrono;

import lombok.Getter;

@Getter
public enum ChronoSpell {
    LUMBRIDGE_HOME_TELEPORT("Lumbridge Home Teleport",  14286854, 356, 406),
    WIND_STRIKE("Wind Strike",                          14286855, 15,65),
    CONFUSE("Confuse",                                  14286856, 16, 66),
    ENCHANT_CROSSBOW_BOLT("Enchant Crossbow Bolt",      14286857, 358, 408),
    WATER_STRIKE("Water Strike",                        14286858, 17,67),
    LVL_1_ENCHANT("Lvl-1 Enchant",                      14286859, 18, 68),
    EARTH_STRIKE("Earth Strike",                        14286860, 19, 69),
    WEAKEN("Weaken",                                    14286861, 20, 70),
    FIRE_STRIKE("Fire Strike",                          14286862, 21, 71),
    BONES_TO_BANANAS("Bones to Bananas", 14286863, 22, 72),
    WIND_BOLT("Wind Bolt", 14286864, 23, 73),
    CURSE("Curse", 14286865, 24, 74),
    BIND("Bind", 14286866, 319, 369),
    LOW_LEVEL_ALCHEMY("Low Level Alchemy", 14286867, 25, 75),
    WATER_BOLT("Water Bolt", 14286868, 26, 76),
    VARROCK_TELEPORT("Varrock Teleport", 14286869, 27, 77),
    LVL_2_ENCHANT("Lvl-2 Enchant", 14286870, 28, 78),
    EARTH_BOLT("Earth Bolt", 14286871, 29, 79),
    LUMBRIDGE_TELEPORT("Lumbridge Teleport", 14286872, 30, 80),
    TELEKINETIC_GRAB("Telekinetic Grab", 14286873, 31, 81),
    FIRE_BOLT("Fire Bolt", 14286874, 32, 82),
    FALADOR_TELEPORT("Falador Teleport", 14286875, 33, 83),
    CRUMBLE_UNDEAD("Crumble Undead", 14286876, 34, 84),
    TELEPORT_TO_HOUSE("Teleport to House", 14286877, 355, 405),
    WIND_BLAST("Wind Blast", 14286878, 35, 85),
    SUPERHEAT_ITEM("Superheat Item", 14286879, 36, 86),
    CAMELOT_TELEPORT("Camelot Teleport", 14286880, 37, 87),
    WATER_BLAST("Water Blast", 14286881, 38, 88),
    LVL_3_ENCHANT("Lvl-3 Enchant", 14286882, 39, 89),
    IBAN_BLAST("Iban Blast", 14286883, 53, 103),
    SNARE("Snare", 14286884, 320, 370),
    MAGIC_DART("Magic Dart", 14286885, 324, 374),
    ARDOUGNE_TELEPORT("Ardougne Teleport", 14286886, 54, 104),
    EARTH_BLAST("Earth Blast", 14286887, 40, 90),
    HIGH_LEVEL_ALCHEMY("High Level Alchemy", 14286888, 41, 91),
    CHARGE_WATER_ORB("Charge Water Orb", 14286889, 42, 92),
    LVL_4_ENCHANT("Lvl-4 Enchant", 14286890, 43, 93),
    WATCHTOWER_TELEPORT("Watchtower Teleport", 14286891, 55, 105),
    FIRE_BLAST("Fire Blast", 14286892, 44, 94),
    CHARGE_EARTH_ORB("Charge Earth Orb", 14286893, 45, 95),
    BONES_TO_PEACHES("Bones to Peaches", 14286894, 354, 404),
    SARADOMIN_STRIKE("Saradomin Strike", 14286895, 61, 111),
    FLAMES_OF_ZAMORAK("Flames of Zamorak", 14286896, 60, 110),
    CLAWS_OF_GUTHIX("Claws of Guthix", 14286897, 59, 109),
    TROLLHEIM_TELEPORT("Trollheim Teleport", 14286898, 323, 373),
    WIND_WAVE("Wind Wave", 14286899, 46, 96),
    CHARGE_FIRE_ORB("Charge Fire Orb", 14286900, 47, 97),
    APE_ATOLL_TELEPORT("Ape Atoll Teleport", 14286901, 357, 407),
    WATER_WAVE("Water Wave", 14286902, 48, 98),
    CHARGE_AIR_ORB("Charge Air Orb", 14286903, 49, 99),
    VULNERABILITY("Vulnerability", 14286904, 56, 106),
    LVL_5_ENCHANT("Lvl-5 Enchant", 14286905, 50, 100),
    KOUREND_CASTLE_TELEPORT("Kourend Castle Teleport", 14286906, 360, 410),
    EARTH_WAVE("Earth Wave", 14286907, 51, 101),
    ENFEEBLE("Enfeeble", 14286908, 57, 107),
    TELEOTHER_LUMBRIDGE("Teleother Lumbridge", 14286909, 349, 399),
    FIRE_WAVE("Fire Wave", 14286910, 52, 102),
    ENTANGLE("Entangle", 14286911, 321, 371),
    STUN("Stun", 14286912, 58, 108),
    CHARGE("Charge", 14286913, 322, 372),
    WIND_SURGE("Wind Surge", 14286914, 362, 412),
    TELEOTHER_FALADOR("Teleother Falador", 14286915, 350, 400),
    WATER_SURGE("Water Surge", 14286916, 363, 413),
    TELE_BLOCK("Tele Block", 14286917, 352, 402),
    TELEPORT_TO_TARGET("Teleport to Target", 14286918, 359, 409),
    LVL_6_ENCHANT("Lvl-6 Enchant", 14286919, 353, 403),
    TELEOTHER_CAMELOT("Teleother Camelot", 14286920, 351, 401),
    EARTH_SURGE("Earth Surge", 14286921, 364, 414),
    LVL_7_ENCHANT("Lvl-7 Enchant", 14286922, 361, 411),
    FIRE_SURGE("Fire Surge", 14286923, 365, 415);


    private String name;
    private int packedID;
    private int unlockedSpriteID;
    private int lockedSpriteID;

    ChronoSpell(String name, int packedID, int unlockedSpriteID, int lockedSpriteID) {
        this.name = name;
        this.packedID = packedID;
        this.lockedSpriteID = lockedSpriteID;
        this.unlockedSpriteID = unlockedSpriteID;
    }
}
