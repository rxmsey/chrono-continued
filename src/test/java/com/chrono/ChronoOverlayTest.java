package com.chrono;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChronoOverlayTest {
    @BeforeClass public static void load() throws Exception { HistoricalDataTest.load(); }

    private static <T> T proxy(Class<T> type, Map<String, Object> values) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (p, m, a) -> {
            if (m.getName().equals("hashCode")) return System.identityHashCode(p);
            if (m.getName().equals("equals")) return p == a[0];
            if (values.containsKey(m.getName())) return values.get(m.getName());
            if (m.getReturnType() == boolean.class) return false;
            if (m.getReturnType() == int.class) return -1;
            return null;
        }));
    }

    private static class Selection extends ChronoPlugin {
        boolean unlocked;
        Release release = Release.getRELEASES().get(0);
        @Override public boolean isItemUnlocked(int id) { return unlocked; }
        @Override public Release getCurrentRelease() { return release; }
    }

    private static BufferedImage render(net.runelite.client.ui.overlay.Overlay overlay) {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try { overlay.render(graphics); } finally { graphics.dispose(); }
        return image;
    }

    @Test public void inventoryAndEquipmentRefreshWithoutWidgetEventsOrSpriteLoading() {
        for (int group : new int[]{InterfaceID.INVENTORY, InterfaceID.WORNITEMS}) {
            Map<String, Object> values = new HashMap<>();
            values.put("getId", group << 16 | 1);
            values.put("getItemId", 4151);
            values.put("getBounds", new Rectangle(10, 10, 36, 32));
            Widget item = proxy(Widget.class, values);
            // Cover nested and dynamic children used by the current inventory UI.
            Widget container = proxy(Widget.class, Map.of("getDynamicChildren", new Widget[]{item}));
            Widget root = proxy(Widget.class, Map.of("getNestedChildren", new Widget[]{container}));
            Client client = proxy(Client.class, Map.of("getGameState", GameState.LOGGED_IN,
                "getWidgetRoots", new Widget[]{root}));
            Selection selection = new Selection();
            ChronoItemOverlay overlay = new ChronoItemOverlay(client, selection);
            assertNotEquals(0, render(overlay).getRGB(20, 20));
            selection.unlocked = true;
            assertEquals(0, render(overlay).getRGB(20, 20));
            selection.unlocked = false;
            values.put("isHidden", true);
            assertEquals(0, render(overlay).getRGB(20, 20));
        }
    }

    @Test public void skillsRefreshAndSailingStaysLockedAcrossAllDates() {
        Map<Integer, Widget> widgets = new HashMap<>();
        widgets.put(InterfaceID.Stats.FARMING, proxy(Widget.class,
            Map.of("getBounds", new Rectangle(10, 10, 50, 30))));
        Map<String, Object> sailing = new HashMap<>();
        sailing.put("getBounds", new Rectangle(80, 10, 50, 30));
        widgets.put(InterfaceID.Stats.SAILING, proxy(Widget.class, sailing));
        Client client = (Client) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class<?>[]{Client.class},
            (p, m, a) -> m.getName().equals("getGameState") ? GameState.LOGGED_IN
                : m.getName().equals("getWidget") ? widgets.get(a[0]) : null);
        Selection selection = new Selection();
        ChronoSkillOverlay overlay = new ChronoSkillOverlay(client, selection);
        assertNotEquals(0, render(overlay).getRGB(20, 20));
        selection.release = Release.getRELEASES().get(Release.getRELEASES().size() - 1);
        assertEquals(0, render(overlay).getRGB(20, 20));
        for (Release release : Release.getRELEASES()) {
            selection.release = release;
            assertNotEquals(0, render(overlay).getRGB(90, 20));
        }
        sailing.put("isHidden", true);
        assertEquals(0, render(overlay).getRGB(90, 20));
        sailing.put("isHidden", false);
        sailing.put("getBounds", new Rectangle(80, 80, 50, 30));
        assertEquals(0, render(overlay).getRGB(90, 20));
        assertNotEquals(0, render(overlay).getRGB(90, 90));
    }

    @Test public void bankStorageIsNotMistakenForPlayerInventory() {
        assertFalse(ChronoItemOverlay.isItemInterface(InterfaceID.BANKMAIN));
        assertTrue(ChronoItemOverlay.isItemInterface(InterfaceID.BANKSIDE));
    }
}
