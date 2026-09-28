package com.chrono;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.ParseException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/** Reads the live widget tree instead of relying on the item draw-hook collection. */
@Slf4j
public class ChronoItemOverlay extends Overlay {
    private final Client client;
    private final ChronoPlugin plugin;

    @Inject
    ChronoItemOverlay(Client client, ChronoPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (client.getGameState() != GameState.LOGGED_IN) return null;
        Set<Widget> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        renderWidgets(graphics, client.getWidgetRoots(), visited);
        return null;
    }

    private void renderWidgets(Graphics2D graphics, Widget[] widgets, Set<Widget> visited) {
        if (widgets == null) return;
        for (Widget widget : widgets) {
            if (widget == null || !visited.add(widget) || widget.isHidden()) continue;
            int itemId = widget.getItemId();
            if (itemId >= 0 && isItemInterface(widget.getId() >>> 16)) {
                try {
                    if (!plugin.isItemUnlocked(itemId)) {
                        Graphics2D mask = (Graphics2D) graphics.create();
                        try {
                            // Respect scrolling/parent clipping without changing game widgets.
                            for (Widget parent = widget.getParent(); parent != null; parent = parent.getParent()) {
                                mask.clip(parent.getBounds());
                            }
                            paintLocked(mask, widget.getBounds());
                        } finally {
                            mask.dispose();
                        }
                    }
                } catch (ParseException ex) {
                    log.debug("Unable to determine historical availability for item {}", itemId, ex);
                }
            }
            renderWidgets(graphics, widget.getDynamicChildren(), visited);
            renderWidgets(graphics, widget.getStaticChildren(), visited);
            renderWidgets(graphics, widget.getNestedChildren(), visited);
        }
    }

    static boolean isItemInterface(int group) {
        switch (group) {
            case InterfaceID.INVENTORY:
            case InterfaceID.WORNITEMS:
            case InterfaceID.BANK_DEPOSITBOX:
            case InterfaceID.BANKSIDE:
            case InterfaceID.SHOPSIDE:
            case InterfaceID.GE_OFFERS_SIDE:
            case InterfaceID.GE_PRICECHECKER_SIDE:
            case InterfaceID.EQUIPMENT_SIDE:
            case InterfaceID.SEED_VAULT_DEPOSIT:
            case InterfaceID.TRADEMAIN:
            case InterfaceID.TRADESIDE:
            case InterfaceID.POH_COSTUMES_SIDE:
                return true;
            default:
                return false;
        }
    }

    static void paintLocked(Graphics2D graphics, Rectangle bounds) {
        if (bounds == null || bounds.isEmpty()) return;
        Color original = graphics.getColor();
        // A visible neutral veil works even before item sprites have loaded.
        graphics.setColor(new Color(70, 70, 70, 210));
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        graphics.setColor(original);
    }
}
