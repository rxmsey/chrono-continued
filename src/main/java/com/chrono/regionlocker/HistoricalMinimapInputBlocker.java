package com.chrono.regionlocker;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
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
 * Prevents the minimap from bypassing Chrono's historical region boundary.
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
        if (event.isConsumed() || !SwingUtilities.isLeftMouseButton(event)
            || client.getGameState() != GameState.LOGGED_IN)
        {
            return false;
        }

        Widget minimap = getMinimapWidget();
        Player player = client.getLocalPlayer();
        if (minimap == null || minimap.isHidden() || player == null)
        {
            return false;
        }

        Rectangle bounds = minimap.getBounds();
        int mouseX = event.getX();
        int mouseY = event.getY();
        if (!bounds.contains(mouseX, mouseY) || !insideMinimapCircle(bounds, mouseX, mouseY))
        {
            return false;
        }

        WorldView worldView = player.getWorldView();
        LocalPoint playerLocal = player.getLocalLocation();
        if (worldView == null || playerLocal == null)
        {
            return false;
        }

        int centreSceneX = playerLocal.getSceneX();
        int centreSceneY = playerLocal.getSceneY();
        int searchRadius = Math.min(MAX_SEARCH_RADIUS_TILES, (int) Math.ceil(
            Math.max(bounds.width, bounds.height) / (2.0 * Math.max(0.5, client.getMinimapZoom()))) + 3);
        int bestDistanceSq = Integer.MAX_VALUE;
        WorldPoint bestWorldPoint = null;

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

                int dx = projected.getX() - mouseX;
                int dy = projected.getY() - mouseY;
                int distanceSq = dx * dx + dy * dy;
                if (distanceSq < bestDistanceSq)
                {
                    bestDistanceSq = distanceSq;
                    bestWorldPoint = WorldPoint.fromLocalInstance(
                        worldView.getScene(), candidate, worldView.getPlane());
                }
            }
        }

        return bestWorldPoint != null && bestDistanceSq <= MAX_NEAREST_DISTANCE_SQ
            && !HistoricalRegionState.isTileUnlocked(bestWorldPoint);
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
