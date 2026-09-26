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
        RELEASES = Arrays.stream(releases)
            .sorted(Comparator.comparing(r -> r.date.getLocalDate()))
            .collect(java.util.stream.Collectors.toList());
        for (Release r : RELEASES) {
            if (r.date == null || !HistoricalCutoff.isSupported(r.date.getLocalDate()))
                throw new IllegalArgumentException("Unsupported historical release");
            for (List<?> values : Arrays.asList(r.skills, r.prayers, r.quests, r.spells))
                if (values != null && values.contains(null))
                    throw new IllegalArgumentException("Unknown enum in release " + r.date);
        }
    }

    public static Release getReleaseByDate(ReleaseDate date) {
        return RELEASES.stream().filter(r -> r.date.getLocalDate().equals(date.getLocalDate()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Missing release: " + date));
    }

    public static List<Integer> getRegions(Release release) {
        LinkedHashSet<Integer> regions = new LinkedHashSet<>();

        for (Release r : RELEASES) {
            if (r.date.getLocalDate().isAfter(release.date.getLocalDate())) break;
            if (r.regions != null) regions.addAll(r.getRegions());

        }

        return new ArrayList<>(regions);
    }

    public static List<Skill> getSkills(Release release) {
        List<Skill> skills = new ArrayList<>();

        for(Release r : RELEASES) {
            if (r.date.getLocalDate().isAfter(release.date.getLocalDate())) break;
            if(r.skills != null) skills.addAll(r.getSkills());

        }

        skills.removeIf(HistoricalPermanentExclusions::isSkillPermanentlyLocked);
        return skills;
    }

    public static List<Prayer> getPrayers(Release release) {
        List<Prayer> prayers = new ArrayList<>();

        for(Release r : RELEASES) {
            if (r.date.getLocalDate().isAfter(release.date.getLocalDate())) break;
            if(r.prayers != null) prayers.addAll(r.getPrayers());

        }

        return prayers;
    }

    public static List<Quest> getQuests(Release release) {
        List<Quest> quests = new ArrayList<>();

        for(Release r : RELEASES) {
            if (r.date.getLocalDate().isAfter(release.date.getLocalDate())) break;
            if(r.quests != null) quests.addAll(r.getQuests());

        }

        return quests;
    }

    public static List<ChronoSpell> getSpells(Release release) {
        List<ChronoSpell> spells = new ArrayList<>();

        for(Release r : RELEASES) {
            if (r.date.getLocalDate().isAfter(release.date.getLocalDate())) break;
            if(r.spells != null) spells.addAll(r.getSpells());

        }

        return spells;
    }
}
