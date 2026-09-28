package com.rewind;

import lombok.Getter;
import net.runelite.api.Prayer;
import net.runelite.api.gameval.InterfaceID;

@Getter
public enum RewindPrayer {
    THICK_SKIN(Prayer.THICK_SKIN, "Thick Skin", InterfaceID.Prayerbook.PRAYER1, 115, 135),
    BURST_OF_STRENGTH(Prayer.BURST_OF_STRENGTH, "Burst of Strength", InterfaceID.Prayerbook.PRAYER2,  116, 136),
    CLARITY_OF_THOUGH(Prayer.CLARITY_OF_THOUGHT, "Clarity of Thought", InterfaceID.Prayerbook.PRAYER3,  117, 137),
    SHARP_EYE(Prayer.SHARP_EYE, "Sharp Eye",  InterfaceID.Prayerbook.PRAYER19, 133, 153),
    MYSTIC_WILL(Prayer.MYSTIC_WILL, "Mystic Will", InterfaceID.Prayerbook.PRAYER20, 134, 154),
    ROCK_SKIN(Prayer.ROCK_SKIN,  "Rock Skin",InterfaceID.Prayerbook.PRAYER4, 118, 138),
    SUPERHUMAN_STRENGTH(Prayer.SUPERHUMAN_STRENGTH, "Superhuman Strength", InterfaceID.Prayerbook.PRAYER5,  119, 139),
    IMPROVED_REFLEXES(Prayer.IMPROVED_REFLEXES, "Improved Reflexes", InterfaceID.Prayerbook.PRAYER6,  120, 140),
    RAPID_HEAL(Prayer.RAPID_HEAL, "Rapid Heal", InterfaceID.Prayerbook.PRAYER7, 121, 141),
    RAPID_RESTORE(Prayer.RAPID_RESTORE, "Rapid Restore", InterfaceID.Prayerbook.PRAYER8, 122, 142),
    PROTECT_ITEM(Prayer.PROTECT_ITEM, "Protect Item", InterfaceID.Prayerbook.PRAYER9, 123, 143),
    STEEL_SKIN(Prayer.STEEL_SKIN, "Steel Skin", InterfaceID.Prayerbook.PRAYER10, 124, 144),
    ULTIMATE_STRENGTH(Prayer.ULTIMATE_STRENGTH, "Ultimate Strength", InterfaceID.Prayerbook.PRAYER11,  125, 145),
    INCREDIBLE_REFLEXES(Prayer.INCREDIBLE_REFLEXES, "Incredible Reflexes", InterfaceID.Prayerbook.PRAYER12,  126, 146),
    PROTECT_FROM_MAGIC(Prayer.PROTECT_FROM_MAGIC, "Protect from Magic", InterfaceID.Prayerbook.PRAYER13,  127, 147),
    PROTECT_FROM_MISSILES(Prayer.PROTECT_FROM_MISSILES, "Protect from Missiles", InterfaceID.Prayerbook.PRAYER14,  128, 148),
    PROTECT_FROM_MELEE(Prayer.PROTECT_FROM_MELEE, "Protect from Melee", InterfaceID.Prayerbook.PRAYER15,  129, 149),
    RETRIBUTION(Prayer.RETRIBUTION, "Retribution", InterfaceID.Prayerbook.PRAYER16,  131, 151),
    REDEMPTION(Prayer.REDEMPTION, "Redemption", InterfaceID.Prayerbook.PRAYER17,  130, 150),
    SMITE(Prayer.SMITE, "Smite", InterfaceID.Prayerbook.PRAYER18,  132, 152),
    HAWK_EYE(Prayer.HAWK_EYE, "Hawk Eye", InterfaceID.Prayerbook.PRAYER21,  502, 506),
    MYSTIC_LORE(Prayer.MYSTIC_LORE, "Mystic Lore", InterfaceID.Prayerbook.PRAYER22,  503, 507),
    EAGLE_EYE(Prayer.EAGLE_EYE, "Eagle Eye", InterfaceID.Prayerbook.PRAYER23,  504, 508),
    MYSTIC_MIGHT(Prayer.MYSTIC_MIGHT, "Mystic Might", InterfaceID.Prayerbook.PRAYER24,  505, 509),
    CHIVALRY(Prayer.CHIVALRY, "Chivalry", InterfaceID.Prayerbook.PRAYER25,  945, 949),
    PIETY(Prayer.PIETY, "Piety", InterfaceID.Prayerbook.PRAYER26,  946, 950),
    RIGOUR(Prayer.RIGOUR, "Rigour", InterfaceID.Prayerbook.PRAYER27,  1420, 1424),
    AUGURY(Prayer.AUGURY, "Augury", InterfaceID.Prayerbook.PRAYER28,  1421, 1425),
    PRESERVE(Prayer.PRESERVE, "Preserve", InterfaceID.Prayerbook.PRAYER29,  947, 951);

    private Prayer prayer;
    private String name;
    private int packedID;
    private int unlockedSpriteID;
    private int lockedSpriteID;

    RewindPrayer(Prayer prayer, String name, int packedID, int unlockedSpriteID, int lockedSpriteID) {
        this.prayer = prayer;
        this.name = name;
        this.packedID = packedID;
        this.unlockedSpriteID = unlockedSpriteID;
        this.lockedSpriteID = lockedSpriteID;
    }
}
