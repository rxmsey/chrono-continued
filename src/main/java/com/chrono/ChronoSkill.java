package com.chrono;

import lombok.Getter;
import net.runelite.api.Skill;

public enum ChronoSkill {
    ATTACK(Skill.ATTACK, 20971521),
    STRENGTH(Skill.STRENGTH, 20971522),
    DEFENCE(Skill.DEFENCE, 20971523),
    RANGED(Skill.RANGED, 20971524),
    PRAYER(Skill.PRAYER, 20971525),
    MAGIC(Skill.MAGIC, 20971526),
    RUNECRAFT(Skill.RUNECRAFT, 20971527),
    CONSTRUCTION(Skill.CONSTRUCTION, 20971528),
    HITPOINTS(Skill.HITPOINTS, 20971529),
    AGILITY(Skill.AGILITY, 20971530),
    HERBLORE(Skill.HERBLORE, 20971531),
    THIEVING(Skill.THIEVING, 20971532),
    CRAFTING(Skill.CRAFTING, 20971533),
    FLETCHING(Skill.FLETCHING, 20971534),
    SLAYER(Skill.SLAYER, 20971535),
    HUNTER(Skill.HUNTER, 20971536),
    MINING(Skill.MINING, 20971537),
    SMITHING(Skill.SMITHING, 20971538),
    FISHING(Skill.FISHING, 20971539),
    COOKING(Skill.COOKING, 20971540),
    FIREMAKING(Skill.FIREMAKING, 20971541),
    WOODCUTTING(Skill.WOODCUTTING, 20971542),
    FARMING(Skill.FARMING, 20971543);

    @Getter
    private Skill skill;
    @Getter
    private int widgetID;

    ChronoSkill(Skill skill, int id) {
        this.skill = skill;
        this.widgetID = id;
    }
}
