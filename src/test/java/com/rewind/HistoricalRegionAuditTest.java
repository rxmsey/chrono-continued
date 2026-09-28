package com.rewind;

import com.rewind.regionlocker.HistoricalRegionState;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HistoricalRegionAuditTest {
    private static class Area {
        String name;
        String date;
        List<Integer> regions;
    }

    @BeforeClass public static void load() throws Exception { HistoricalDataTest.load(); }

    @Test public void reviewedAreasUnlockTogetherAtTheirDocumentedBoundary() throws Exception {
        Area[] areas;
        try (InputStreamReader reader = new InputStreamReader(
            getClass().getResourceAsStream("region-boundaries.json"), StandardCharsets.UTF_8)) {
            areas = new Gson().fromJson(reader, Area[].class);
        }
        for (Release release : Release.getRELEASES()) {
            HistoricalRegionState.setSelectedDate(release.getDate().getDate());
            HistoricalRegionState.replaceWith(Release.getRegions(release));
            for (Area area : areas) {
                boolean expected = !release.getDate().getLocalDate().isBefore(LocalDate.parse(area.date));
                for (int region : area.regions) {
                    assertEquals(area.name + " / " + region + " at " + release.getDate(),
                        expected, HistoricalRegionState.isRegionUnlocked(region));
                }
            }
        }
    }

    @Test public void regionAssignmentsAreUniqueAndWithinThePackedIdRange() {
        Set<Integer> seen = new HashSet<>();
        for (Release release : Release.getRELEASES()) {
            for (int region : release.getRegions()) {
                assertTrue("Invalid region " + region, region >= 0 && region <= 65535);
                assertTrue("Duplicate region " + region, seen.add(region));
            }
        }
    }

    @Test public void fixingHistoricalMinigamesDoesNotOpenModernDedicatedAreas() {
        Release last = Release.getRELEASES().get(Release.getRELEASES().size() - 1);
        HistoricalRegionState.setSelectedDate(last.getDate().getDate());
        HistoricalRegionState.replaceWith(Release.getRegions(last));
        for (int region : new int[]{9043, 10063, 10064, 9807, 13151, 12895, 14642,
            13136, 12889, 12611, 14160, 9033, 11675, 12119}) {
            assertFalse("Post-backup/dedicated modern region " + region,
                HistoricalRegionState.isRegionUnlocked(region));
        }
    }
}
