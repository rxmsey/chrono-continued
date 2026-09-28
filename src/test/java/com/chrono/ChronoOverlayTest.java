package com.chrono;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChronoOverlayTest
{
    @BeforeClass
    public static void load() throws Exception
    {
        HistoricalDataTest.load();
    }

    private static <T> T proxy(Class<T> type, Map<String, Object> values)
    {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (p, m, a) -> {
            if (m.getName().equals("hashCode")) return System.identityHashCode(p);
            if (m.getName().equals("equals")) return p == a[0];
            if (values.containsKey(m.getName())) return values.get(m.getName());
            if (m.getReturnType() == boolean.class) return false;
            if (m.getReturnType() == int.class) return -1;
            return null;
        }));
    }

    @Test
    public void itemOverlayUsesRuneLiteWidgetItemOverlay()
    {
        assertTrue(WidgetItemOverlay.class.isAssignableFrom(ChronoItemOverlay.class));
    }

    @Test
    public void lockedMaskPaintsTheRequestedBounds()
    {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try
        {
            ChronoItemOverlay.paintLocked(graphics, new Rectangle(10, 10, 36, 32));
        }
        finally
        {
            graphics.dispose();
        }

        assertNotEquals(0, image.getRGB(20, 20));
        assertEquals(0, image.getRGB(2, 2));
    }

    @Test
    public void skillWidgetsResolveByLiveGuideAction()
    {
        for (Skill skill : new Skill[]{Skill.FARMING, Skill.CONSTRUCTION, Skill.HUNTER, Skill.SAILING})
        {
            Map<String, Object> values = new HashMap<>();
            values.put("getActions", new String[]{"View " + skill.getName() + " guide"});
            Widget widget = proxy(Widget.class, values);
            assertEquals(skill, ChronoSkillOverlay.skillForWidget(widget));
        }

        Widget unrelated = proxy(Widget.class, Map.of("getActions", new String[]{"Configure"}));
        assertNull(ChronoSkillOverlay.skillForWidget(unrelated));
    }

    @Test
    public void sailingIsAlwaysPermanentlyLocked()
    {
        assertTrue(HistoricalPermanentExclusions.isSkillPermanentlyLocked(Skill.SAILING));
        assertFalse(HistoricalPermanentExclusions.isSkillPermanentlyLocked(Skill.FARMING));
        assertFalse(HistoricalPermanentExclusions.isSkillPermanentlyLocked(Skill.CONSTRUCTION));
        assertFalse(HistoricalPermanentExclusions.isSkillPermanentlyLocked(Skill.HUNTER));
    }

    @Test
    public void finalHistoricalReleaseContainsFarmingConstructionAndHunterButNotSailing()
    {
        Release latest = Release.getRELEASES().get(Release.getRELEASES().size() - 1);
        assertTrue(Release.getSkills(latest).contains(Skill.FARMING));
        assertTrue(Release.getSkills(latest).contains(Skill.CONSTRUCTION));
        assertTrue(Release.getSkills(latest).contains(Skill.HUNTER));
        assertFalse(Release.getSkills(latest).contains(Skill.SAILING));
    }
}
