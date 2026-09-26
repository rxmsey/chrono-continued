package com.chrono;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Prayer;
import net.runelite.api.Quest;
import net.runelite.api.Skill;

import java.util.*;

@Slf4j
public class Release {
    @Getter
    private static List<Release> RELEASES = new ArrayList<>();

    @Getter
    private ReleaseDate date;
    @Getter
    private List<Integer> regions;
    @Getter
    private List<Skill> skills;
    @Getter
    private List<Prayer> prayers;
    @Getter
    private List<Quest> quests;
    @Getter
    private List<ChronoSpell> spells;
    @Getter
    private String description;

    public Release(ReleaseDate date, List<Integer> regions, List<Skill> skills, List<Prayer> prayers, List<Quest> quests, List<ChronoSpell> spells, String description) {
        this.date = date;
        this.skills = skills;
        this.regions = regions;
        this.prayers = prayers;
        this.quests = quests;
        this.spells = spells;
        this.description = description;
    }

    public static void setReleases(Release[] releases) {
        RELEASES = Arrays.asList(releases);
    }

    public static Release getReleaseByDate(ReleaseDate date) {
        for(Release r : RELEASES) {
            if(r.getDate().equals(date)) return r;
        }

        return null;
    }

    public static List<Skill> getSkills(Release release) {
        List<Skill> skills = new ArrayList<>();

        for(Release r : RELEASES) {
            if(r.skills != null) skills.addAll(r.getSkills());
            if(r.equals(release)) break;
        }

        return skills;
    }

    public static List<Prayer> getPrayers(Release release) {
        List<Prayer> prayers = new ArrayList<>();

        for(Release r : RELEASES) {
            if(r.prayers != null) prayers.addAll(r.getPrayers());
            if(r.equals(release)) break;
        }

        return prayers;
    }

    public static List<Quest> getQuests(Release release) {
        List<Quest> quests = new ArrayList<>();

        for(Release r : RELEASES) {
            if(r.quests != null) quests.addAll(r.getQuests());
            if(r.equals(release)) break;
        }

        return quests;
    }

    public static List<ChronoSpell> getSpells(Release release) {
        List<ChronoSpell> spells = new ArrayList<>();

        for(Release r : RELEASES) {
            if(r.spells != null) spells.addAll(r.getSpells());
            if(r.equals(release)) break;
        }

        return spells;
    }
}
