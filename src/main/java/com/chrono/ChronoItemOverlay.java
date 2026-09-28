package com.chrono;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.ParseException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

/**
 * Greys historically unavailable items using RuneLite's item-widget overlay
 * collection so inventory, equipment, bank and shared-bank items are covered.
 */
@Slf4j
public class ChronoItemOverlay extends WidgetItemOverlay
{
    private final ChronoPlugin plugin;

    @Inject
    ChronoItemOverlay(ChronoPlugin plugin)
    {
        this.plugin = plugin;
        showOnInventory();
        showOnEquipment();
        showOnBank();
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
    {
        try
        {
            if (!plugin.isItemUnlocked(itemId))
            {
                paintLocked(graphics, widgetItem.getCanvasBounds());
            }
        }
        catch (ParseException ex)
        {
            log.debug("Unable to determine historical availability for item {}", itemId, ex);
        }
    }

    static void paintLocked(Graphics2D graphics, Rectangle bounds)
    {
        if (bounds == null || bounds.isEmpty())
        {
            return;
        }

        Color original = graphics.getColor();
        graphics.setColor(new Color(70, 70, 70, 210));
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        graphics.setColor(original);
    }
}
