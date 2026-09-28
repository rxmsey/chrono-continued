package com.rewind.regionlocker;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.input.MouseAdapter;

/**
 * Prevents the minimap from bypassing Rewind's historical region boundary.
 *
 * RuneLite's ordinary scene walking is exposed as a WALK menu action, but the
 * minimap takes a separate input path. Rather than reimplement Jagex's minimap
 * rotation/zoom math, this listener projects nearby tiles using RuneLite's own
 * Perspective.localToMinimap method and consumes clicks whose nearest tile is
 * historically locked.
 */
public class HistoricalMinimapInputBlocker extends MouseAdapter
{
    private static final int MAX_SEARCH_RADIUS_TILES = 104;
    private static final int MAX_NEAREST_DISTANCE_SQ = 64; // 8 px

    private final Client client;
    private boolean blockClickSequence;
    // Published by the client thread; mouse callbacks must never read live widgets/varbits.
    private volatile Snapshot snapshot;

    static final class TilePoint
    {
        final int x, y;
        final boolean locked;

        TilePoint(int x, int y, boolean locked)
        {
            this.x = x;
            this.y = y;
            this.locked = locked;
        }
    }

    static final class Snapshot
    {
        final Rectangle bounds;
        final TilePoint[] tiles;

        Snapshot(Rectangle bounds, TilePoint[] tiles)
        {
            this.bounds = new Rectangle(bounds);
            this.tiles = tiles.clone();
        }

        boolean shouldBlock(int x, int y)
        {
            if (!bounds.contains(x, y) || !insideMinimapCircle(bounds, x, y)) return false;
            int bestDistanceSq = Integer.MAX_VALUE;
            boolean locked = false;
            for (TilePoint tile : tiles)
            {
                int dx = tile.x - x;
                int dy = tile.y - y;
                int distanceSq = dx * dx + dy * dy;
                if (distanceSq < bestDistanceSq)
                {
                    bestDistanceSq = distanceSq;
                    locked = tile.locked;
                }
            }
            return bestDistanceSq <= MAX_NEAREST_DISTANCE_SQ && locked;
        }
    }

    public void clear()
    {
        snapshot = null;
    }

    void publish(Snapshot next)
    {
        snapshot = next;
    }

    @Inject
    HistoricalMinimapInputBlocker(Client client)
    {
        this.client = client;
    }

    @Override
    public MouseEvent mousePressed(MouseEvent event)
    {
        blockClickSequence = shouldBlock(event);
        if (blockClickSequence)
        {
            event.consume();
        }
        return event;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent event)
    {
        if (blockClickSequence && SwingUtilities.isLeftMouseButton(event))
        {
            event.consume();
        }
        return event;
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent event)
    {
        if (blockClickSequence && SwingUtilities.isLeftMouseButton(event))
        {
            event.consume();
            blockClickSequence = false;
        }
        return event;
    }

    private boolean shouldBlock(MouseEvent event)
    {
        Snapshot current = snapshot;
        return !event.isConsumed() && SwingUtilities.isLeftMouseButton(event)
            && current != null && current.shouldBlock(event.getX(), event.getY());
    }

    /** Called on the client thread once per tick, never from an AWT callback. */
    public void refresh()
    {
        if (client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
        {
            clear();
            return;
        }

        Widget minimap = getMinimapWidget();
        Player player = client.getLocalPlayer();
        if (minimap == null || minimap.isHidden() || player == null)
        {
            clear();
            return;
        }

        Rectangle bounds = minimap.getBounds();
        WorldView worldView = player.getWorldView();
        LocalPoint playerLocal = player.getLocalLocation();
        if (worldView == null || playerLocal == null)
        {
            clear();
            return;
        }

        int centreSceneX = playerLocal.getSceneX();
        int centreSceneY = playerLocal.getSceneY();
        int searchRadius = Math.min(MAX_SEARCH_RADIUS_TILES, (int) Math.ceil(
            Math.max(bounds.width, bounds.height) / (2.0 * Math.max(0.5, client.getMinimapZoom()))) + 3);
        List<TilePoint> tiles = new ArrayList<>();

        int minX = Math.max(0, centreSceneX - searchRadius);
        int maxX = Math.min(worldView.getSizeX() - 1, centreSceneX + searchRadius);
        int minY = Math.max(0, centreSceneY - searchRadius);
        int maxY = Math.min(worldView.getSizeY() - 1, centreSceneY + searchRadius);

        for (int sceneX = minX; sceneX <= maxX; sceneX++)
        {
            for (int sceneY = minY; sceneY <= maxY; sceneY++)
            {
                LocalPoint candidate = LocalPoint.fromScene(sceneX, sceneY, worldView);
                Point projected = Perspective.localToMinimap(client, candidate, 30000);
                if (projected == null)
                {
                    continue;
                }

                WorldPoint worldPoint = WorldPoint.fromLocalInstance(
                    worldView.getScene(), candidate, worldView.getPlane());
                tiles.add(new TilePoint(projected.getX(), projected.getY(),
                    !HistoricalRegionState.isTileUnlocked(worldPoint)));
            }
        }

        publish(new Snapshot(bounds, tiles.toArray(new TilePoint[0])));
    }

    private Widget getMinimapWidget()
    {
        int widgetId = !client.isResized() ? InterfaceID.Toplevel.MINIMAP
            : client.getVarbitValue(VarbitID.RESIZABLE_STONE_ARRANGEMENT) == 1
                ? InterfaceID.ToplevelPreEoc.MINIMAP
                : InterfaceID.ToplevelOsrsStretch.MINIMAP;
        return client.getWidget(widgetId);
    }

    private static boolean insideMinimapCircle(Rectangle bounds, int x, int y)
    {
        double rx = Math.max(1.0, bounds.width / 2.0);
        double ry = Math.max(1.0, bounds.height / 2.0);
        double nx = (x - (bounds.x + rx)) / rx;
        double ny = (y - (bounds.y + ry)) / ry;
        return nx * nx + ny * ny <= 1.0;
    }
}
