package com.chrono.regionlocker;

import com.chrono.ChronoConfig;
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

public class HistoricalSceneMaskOverlay extends Overlay
{
    private final Client client;
    private final ChronoConfig config;

    @Inject
    HistoricalSceneMaskOverlay(Client client, ChronoConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(Overlay.PRIORITY_HIGHEST);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.maskLockedScene() || client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
        {
            return null;
        }

        WorldView worldView = client.getLocalPlayer().getWorldView();
        Graphics2D copy = (Graphics2D) graphics.create();
        copy.clip(new java.awt.Rectangle(client.getViewportXOffset(), client.getViewportYOffset(),
            client.getViewportWidth(), client.getViewportHeight()));
        copy.setColor(config.shaderGrayColor());
        try {

        for (int dx = 0; dx < worldView.getSizeX(); dx++)
        {
            for (int dy = 0; dy < worldView.getSizeY(); dy++)
            {
                LocalPoint lp = LocalPoint.fromScene(dx, dy, worldView);
                if (lp == null)
                {
                    continue;
                }

                WorldPoint template = WorldPoint.fromLocalInstance(worldView.getScene(), lp, worldView.getPlane());
                if (HistoricalRegionState.isTileUnlocked(template)) continue;
                Polygon poly = Perspective.getCanvasTilePoly(client, lp);
                if (poly != null)
                {
                    copy.fillPolygon(poly);
                }
            }
        }

        } finally { copy.dispose(); }
        return null;
    }
}
