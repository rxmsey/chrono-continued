package com.rewind;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.ParseException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

/**
 * Greys historically unavailable items using RuneLite's item-widget overlay
 * collection so inventory, equipment, bank and shared-bank items are covered.
 */
@Slf4j
public class RewindItemOverlay extends WidgetItemOverlay
{
    private final RewindPlugin plugin;

    @Inject
    RewindItemOverlay(RewindPlugin plugin)
    {
        this.plugin = plugin;
        showOnInventory();
        showOnEquipment();
        showOnBank();
        // Include the shop stock pane itself, not only the player's shop-side inventory.
        showOnInterfaces(InterfaceID.SHOPMAIN, InterfaceID.GE_OFFERS, InterfaceID.GE_PRICELIST);
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
    {
        if (plugin.isTutorialBypass())
        {
            return;
        }

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
