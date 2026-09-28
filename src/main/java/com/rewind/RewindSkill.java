package com.rewind;

import lombok.Getter;
import net.runelite.api.Skill;
import net.runelite.api.gameval.InterfaceID;

public enum RewindSkill {
    ATTACK(Skill.ATTACK, InterfaceID.Stats.ATTACK),
    STRENGTH(Skill.STRENGTH, InterfaceID.Stats.STRENGTH),
    DEFENCE(Skill.DEFENCE, InterfaceID.Stats.DEFENCE),
    RANGED(Skill.RANGED, InterfaceID.Stats.RANGED),
    PRAYER(Skill.PRAYER, InterfaceID.Stats.PRAYER),
    MAGIC(Skill.MAGIC, InterfaceID.Stats.MAGIC),
    RUNECRAFT(Skill.RUNECRAFT, InterfaceID.Stats.RUNECRAFT),
    CONSTRUCTION(Skill.CONSTRUCTION, InterfaceID.Stats.CONSTRUCTION),
    HITPOINTS(Skill.HITPOINTS, InterfaceID.Stats.HITPOINTS),
    AGILITY(Skill.AGILITY, InterfaceID.Stats.AGILITY),
    HERBLORE(Skill.HERBLORE, InterfaceID.Stats.HERBLORE),
    THIEVING(Skill.THIEVING, InterfaceID.Stats.THIEVING),
    CRAFTING(Skill.CRAFTING, InterfaceID.Stats.CRAFTING),
    FLETCHING(Skill.FLETCHING, InterfaceID.Stats.FLETCHING),
    SLAYER(Skill.SLAYER, InterfaceID.Stats.SLAYER),
    HUNTER(Skill.HUNTER, InterfaceID.Stats.HUNTER),
    MINING(Skill.MINING, InterfaceID.Stats.MINING),
    SMITHING(Skill.SMITHING, InterfaceID.Stats.SMITHING),
    FISHING(Skill.FISHING, InterfaceID.Stats.FISHING),
    COOKING(Skill.COOKING, InterfaceID.Stats.COOKING),
    FIREMAKING(Skill.FIREMAKING, InterfaceID.Stats.FIREMAKING),
    WOODCUTTING(Skill.WOODCUTTING, InterfaceID.Stats.WOODCUTTING),
    FARMING(Skill.FARMING, InterfaceID.Stats.FARMING),
    SAILING(Skill.SAILING, InterfaceID.Stats.SAILING);

    @Getter
    private Skill skill;
    @Getter
    private int widgetID;

    RewindSkill(Skill skill, int id) {
        this.skill = skill;
        this.widgetID = id;
    }
}
