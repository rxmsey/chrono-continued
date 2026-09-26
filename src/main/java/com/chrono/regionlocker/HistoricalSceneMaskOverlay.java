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
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class HistoricalSceneMaskOverlay extends Overlay
{
    private static final Color VOID = new Color(0, 0, 0, 255);
    private static final int RADIUS = 52;
    private final Client client;
    private final ChronoConfig config;

    @Inject
    HistoricalSceneMaskOverlay(Client client, ChronoConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.HIGHEST);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.maskLockedScene() || client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
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
                if (HistoricalRegionState.isTileUnlocked(wp))
                {
                    continue;
                }

                LocalPoint lp = LocalPoint.fromWorld(worldView, wp);
                if (lp == null)
                {
                    continue;
                }

                Polygon poly = Perspective.getCanvasTilePoly(client, lp);
                if (poly != null)
                {
                    graphics.fillPolygon(poly);
                }
            }
        }

        return null;
    }
}
