package com.rewind.regionlocker;

import java.awt.Canvas;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.lang.reflect.Proxy;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import org.junit.Test;
import static org.junit.Assert.*;

public class HistoricalMinimapInputBlockerTest
{
    private final Canvas canvas = new Canvas();

    private MouseEvent event(int id, int x, int y, int button)
    {
        return new MouseEvent(canvas, id, 0, 0, x, y, 1, false, button);
    }

    private HistoricalMinimapInputBlocker blocker()
    {
        Client client = (Client) Proxy.newProxyInstance(Client.class.getClassLoader(),
            new Class<?>[]{Client.class}, (p, m, a) -> {
                throw new AssertionError("Mouse callback accessed live client: " + m.getName());
            });
        HistoricalMinimapInputBlocker blocker = new HistoricalMinimapInputBlocker(client);
        blocker.publish(new HistoricalMinimapInputBlocker.Snapshot(new Rectangle(600, 0, 150, 150),
            new HistoricalMinimapInputBlocker.TilePoint[]{
                new HistoricalMinimapInputBlocker.TilePoint(675, 75, true),
                new HistoricalMinimapInputBlocker.TilePoint(690, 75, false)}));
        return blocker;
    }

    private void sequence(HistoricalMinimapInputBlocker blocker, int x, int y, int button, boolean consumed)
    {
        MouseEvent press = event(MouseEvent.MOUSE_PRESSED, x, y, button);
        MouseEvent release = event(MouseEvent.MOUSE_RELEASED, x, y, button);
        MouseEvent click = event(MouseEvent.MOUSE_CLICKED, x, y, button);
        assertSame(press, blocker.mousePressed(press));
        assertSame(release, blocker.mouseReleased(release));
        assertSame(click, blocker.mouseClicked(click));
        assertEquals(consumed, press.isConsumed());
        assertEquals(consumed, release.isConsumed());
        assertEquals(consumed, click.isConsumed());
    }

    @Test public void normalGameAndUiClicksNeverReadClientFromAwt() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            HistoricalMinimapInputBlocker b = blocker();
            sequence(b, 200, 200, MouseEvent.BUTTON1, false);
            sequence(b, 700, 400, MouseEvent.BUTTON1, false);
            sequence(b, 601, 1, MouseEvent.BUTTON1, false); // outside circular minimap
            sequence(b, 690, 75, MouseEvent.BUTTON1, false); // unlocked tile
            sequence(b, 675, 75, MouseEvent.BUTTON3, false);
            sequence(b, 675, 75, MouseEvent.BUTTON2, false);
        });
    }

    @Test public void lockedLeftSequenceIsConsumedAndNextOrdinaryClickPasses() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            HistoricalMinimapInputBlocker b = blocker();
            sequence(b, 675, 75, MouseEvent.BUTTON1, true);
            sequence(b, 200, 200, MouseEvent.BUTTON1, false);
        });
    }

    @Test public void unmatchedMinimapClickPasses()
    {
        sequence(blocker(), 675, 110, MouseEvent.BUTTON1, false);
    }

    @Test public void missingSnapshotPasses()
    {
        HistoricalMinimapInputBlocker b = blocker();
        b.clear();
        sequence(b, 675, 75, MouseEvent.BUTTON1, false);
    }

    @Test public void consumedPressDoesNotStartBlockedSequence()
    {
        HistoricalMinimapInputBlocker b = blocker();
        MouseEvent press = event(MouseEvent.MOUSE_PRESSED, 675, 75, MouseEvent.BUTTON1);
        press.consume();
        assertSame(press, b.mousePressed(press));
        MouseEvent release = event(MouseEvent.MOUSE_RELEASED, 675, 75, MouseEvent.BUTTON1);
        assertFalse(b.mouseReleased(release).isConsumed());
    }

    @Test public void dragWithoutClickCannotPoisonNextPress()
    {
        HistoricalMinimapInputBlocker b = blocker();
        assertTrue(b.mousePressed(event(MouseEvent.MOUSE_PRESSED, 675, 75, MouseEvent.BUTTON1)).isConsumed());
        assertTrue(b.mouseReleased(event(MouseEvent.MOUSE_RELEASED, 200, 200, MouseEvent.BUTTON1)).isConsumed());
        sequence(b, 200, 200, MouseEvent.BUTTON1, false);
    }

    @Test public void motionAndBoundaryEventsPassThrough()
    {
        HistoricalMinimapInputBlocker b = blocker();
        MouseEvent motion = event(MouseEvent.MOUSE_MOVED, 675, 75, MouseEvent.NOBUTTON);
        assertSame(motion, b.mouseMoved(motion));
        assertSame(motion, b.mouseDragged(motion));
        assertSame(motion, b.mouseEntered(motion));
        assertSame(motion, b.mouseExited(motion));
        assertFalse(motion.isConsumed());
    }

    @Test public void logoutAndContextMenuClearCachedBlocking()
    {
        for (boolean menu : new boolean[]{false, true})
        {
            Client client = (Client) Proxy.newProxyInstance(Client.class.getClassLoader(),
                new Class<?>[]{Client.class}, (p, m, a) -> {
                    if (m.getName().equals("getGameState")) return menu ? GameState.LOGGED_IN : GameState.LOGIN_SCREEN;
                    if (m.getName().equals("isMenuOpen")) return menu;
                    throw new AssertionError("Unexpected read: " + m.getName());
                });
            HistoricalMinimapInputBlocker b = new HistoricalMinimapInputBlocker(client);
            b.publish(new HistoricalMinimapInputBlocker.Snapshot(new Rectangle(600, 0, 150, 150),
                new HistoricalMinimapInputBlocker.TilePoint[]{new HistoricalMinimapInputBlocker.TilePoint(675, 75, true)}));
            b.refresh();
            sequence(b, 675, 75, MouseEvent.BUTTON1, false);
        }
    }

    private static <T> T proxy(Class<T> type, java.util.Map<String, Object> values)
    {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (p, m, a) -> {
            if (SwingUtilities.isEventDispatchThread()) throw new AssertionError("Live game access on AWT");
            if (values.containsKey(m.getName())) return values.get(m.getName());
            throw new AssertionError("Unexpected game method: " + m.getName());
        }));
    }

    @Test public void projectedLockedTilesAndGeOverrideWorkInAllLayouts() throws Exception
    {
        for (int layout = 0; layout < 3; layout++)
        {
            net.runelite.api.coords.LocalPoint local = new net.runelite.api.coords.LocalPoint(64, 64, net.runelite.api.WorldView.TOPLEVEL);
            net.runelite.api.Scene scene = proxy(net.runelite.api.Scene.class, java.util.Map.of(
                "isInstance", false, "getBaseX", 3136, "getBaseY", 3456)); // GE region 12598
            net.runelite.api.WorldView view = proxy(net.runelite.api.WorldView.class, java.util.Map.of(
                "getId", net.runelite.api.WorldView.TOPLEVEL, "getSizeX", 1, "getSizeY", 1, "getPlane", 0, "getScene", scene));
            net.runelite.api.Player player = proxy(net.runelite.api.Player.class, java.util.Map.of(
                "getWorldView", view, "getLocalLocation", local, "getCameraFocus", local));
            java.util.Map<String, Object> widgetValues = new java.util.HashMap<>();
            widgetValues.put("isHidden", false);
            widgetValues.put("getBounds", new Rectangle(600, 0, 150, 150));
            widgetValues.put("getCanvasLocation", new net.runelite.api.Point(600, 0));
            widgetValues.put("getWidth", 150);
            widgetValues.put("getHeight", 150);
            net.runelite.api.widgets.Widget widget = proxy(net.runelite.api.widgets.Widget.class, widgetValues);
            java.util.Map<String, Object> values = new java.util.HashMap<>();
            values.put("getGameState", GameState.LOGGED_IN);
            values.put("isMenuOpen", false);
            values.put("isResized", layout != 0);
            values.put("getVarbitValue", layout == 1 ? 1 : 0);
            values.put("getWidget", widget);
            values.put("getLocalPlayer", player);
            values.put("getCameraFocusEntity", player);
            values.put("getMinimapZoom", 4.0);
            values.put("getCameraYawTarget", 0);
            HistoricalMinimapInputBlocker b = new HistoricalMinimapInputBlocker(proxy(Client.class, values));
            HistoricalRegionState.replaceWith(java.util.Collections.emptyList());
            HistoricalRegionState.setAdditionallyUnlocked(java.util.Collections.emptyList());
            b.refresh();
            SwingUtilities.invokeAndWait(() -> sequence(b, 675, 75, MouseEvent.BUTTON1, true));
            HistoricalRegionState.setAdditionallyUnlocked(java.util.Collections.singleton(12598));
            b.refresh();
            SwingUtilities.invokeAndWait(() -> sequence(b, 675, 75, MouseEvent.BUTTON1, false));
            HistoricalRegionState.setAdditionallyUnlocked(java.util.Collections.emptyList());
            widgetValues.put("isHidden", true);
            b.refresh();
            sequence(b, 675, 75, MouseEvent.BUTTON1, false);
        }
    }
}
