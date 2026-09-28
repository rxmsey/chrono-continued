package com.rewind.regionlocker;

import com.rewind.RewindConfig;
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

public class HistoricalMinimapMaskOverlay extends Overlay
{
    private final Client client;
    private final RewindConfig config;

    @Inject
    HistoricalMinimapMaskOverlay(Client client, RewindConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(Overlay.PRIORITY_HIGHEST);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.maskLockedMinimap() || client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
        {
            return null;
        }

        int widgetId = !client.isResized() ? net.runelite.api.gameval.InterfaceID.Toplevel.MINIMAP
            : client.getVarbitValue(net.runelite.api.gameval.VarbitID.RESIZABLE_STONE_ARRANGEMENT) == 1
                ? net.runelite.api.gameval.InterfaceID.ToplevelPreEoc.MINIMAP
                : net.runelite.api.gameval.InterfaceID.ToplevelOsrsStretch.MINIMAP;
        net.runelite.api.widgets.Widget minimap = client.getWidget(widgetId);
        if (minimap == null || minimap.isHidden()) return null;
        java.awt.Rectangle bounds = minimap.getBounds();
        Graphics2D copy = (Graphics2D) graphics.create();
        copy.clip(new java.awt.geom.Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height));
        int radius = Math.min(104, (int) Math.ceil(Math.max(bounds.width, bounds.height)
            / (2.0 * Math.max(0.5, client.getMinimapZoom()))) + 2);
        try {
        WorldView worldView = client.getLocalPlayer().getWorldView();
        WorldPoint centre = client.getLocalPlayer().getWorldLocation();
        copy.setColor(config.shaderGrayColor());

        for (int dx = -radius; dx <= radius; dx++)
        {
            for (int dy = -radius; dy <= radius; dy++)
            {
                WorldPoint wp = new WorldPoint(centre.getX() + dx, centre.getY() + dy, centre.getPlane());
                LocalPoint local = LocalPoint.fromWorld(worldView, wp);
                if (local != null && !HistoricalRegionState.isTileUnlocked(
                    WorldPoint.fromLocalInstance(worldView.getScene(), local, worldView.getPlane())))
                {
                    drawTile(copy, worldView, wp);
                }
            }
        }

        } finally { copy.dispose(); }

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

        Point p1 = Perspective.localToMinimap(client, new LocalPoint(x, y, id), 30000);
        Point p2 = Perspective.localToMinimap(client, new LocalPoint(x, y + Perspective.LOCAL_TILE_SIZE, id), 30000);
        Point p3 = Perspective.localToMinimap(client, new LocalPoint(x + Perspective.LOCAL_TILE_SIZE, y + Perspective.LOCAL_TILE_SIZE, id), 30000);
        Point p4 = Perspective.localToMinimap(client, new LocalPoint(x + Perspective.LOCAL_TILE_SIZE, y, id), 30000);

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
