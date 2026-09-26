package com.chrono.regionlocker;

import com.chrono.ChronoConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class HistoricalMinimapMaskOverlay extends Overlay
{
    private static final Color VOID = new Color(0, 0, 0, 255);
    private static final int RADIUS = 24;
    private final Client client;
    private final ChronoConfig config;

    @Inject
    HistoricalMinimapMaskOverlay(Client client, ChronoConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGHEST);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.maskLockedMinimap() || client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
        {
            return null;
        }

        WorldView worldView = client.getTopLevelWorldView();
        WorldPoint centre = client.getLocalPlayer().getWorldLocation();
        graphics.setColor(VOID);

        for (int dx = -RADIUS; dx <= RADIUS; dx++)
        {
            for (int dy = -RADIUS; dy <= RADIUS; dy++)
            {
                WorldPoint wp = new WorldPoint(centre.getX() + dx, centre.getY() + dy, centre.getPlane());
                if (!HistoricalRegionState.isTileUnlocked(wp))
                {
                    drawTile(graphics, worldView, wp);
                }
            }
        }

        return null;
    }

    private void drawTile(Graphics2D graphics, WorldView worldView, WorldPoint wp)
    {
        LocalPoint lp = LocalPoint.fromWorld(worldView, wp);
        if (lp == null)
        {
            return;
        }

        int x = lp.getX() & -Perspective.LOCAL_TILE_SIZE;
        int y = lp.getY() & -Perspective.LOCAL_TILE_SIZE;
        int id = worldView.getId();

        Point p1 = Perspective.localToMinimap(client, new LocalPoint(x, y, id));
        Point p2 = Perspective.localToMinimap(client, new LocalPoint(x, y + Perspective.LOCAL_TILE_SIZE, id));
        Point p3 = Perspective.localToMinimap(client, new LocalPoint(x + Perspective.LOCAL_TILE_SIZE, y + Perspective.LOCAL_TILE_SIZE, id));
        Point p4 = Perspective.localToMinimap(client, new LocalPoint(x + Perspective.LOCAL_TILE_SIZE, y, id));

        if (p1 == null || p2 == null || p3 == null || p4 == null)
        {
            return;
        }

        graphics.fillPolygon(new Polygon(
            new int[]{p1.getX(), p2.getX(), p3.getX(), p4.getX()},
            new int[]{p1.getY(), p2.getY(), p3.getY(), p4.getY()},
            4
        ));
    }
}
