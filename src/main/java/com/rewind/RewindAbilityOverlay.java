package com.rewind;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Prayer;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Keeps historically unavailable prayers hidden.
 *
 * Spell filtering is deliberately not performed here. Rewind filters the
 * spellbook's native spell array during the spellbookSort script callback so
 * spell widgets, tooltips and menus remain entirely native.
 */
public class RewindAbilityOverlay extends Overlay
{
    private final Client client;
    private final RewindPlugin plugin;

    @Inject
    RewindAbilityOverlay(Client client, RewindPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (client.getGameState() != GameState.LOGGED_IN || plugin.getCurrentRelease() == null)
        {
            return null;
        }

        if (plugin.isTutorialBypass())
        {
            restoreAllAbilities();
            return null;
        }

        List<Prayer> prayers = Release.getPrayers(plugin.getCurrentRelease());
        for (RewindPrayer prayer : RewindPrayer.values())
        {
            Widget widget = client.getWidget(prayer.getPackedID());
            if (widget != null)
            {
                boolean locked = prayer == RewindPrayer.RIGOUR
                    || !prayers.contains(prayer.getPrayer());
                widget.setHidden(locked);
            }
        }

        int prayerGroup = RewindPrayer.RIGOUR.getPackedID() >>> 16;
        hidePrayerWidgetByName(client.getWidget(prayerGroup, 0), "rigour");
        return null;
    }

    private void hidePrayerWidgetByName(Widget widget, String prayerName)
    {
        if (widget == null) return;
        String needle = prayerName.toLowerCase(java.util.Locale.ROOT);
        boolean match = containsText(widget.getName(), needle) || containsText(widget.getText(), needle);
        String[] actions = widget.getActions();
        if (!match && actions != null)
        {
            for (String action : actions)
            {
                if (containsText(action, needle))
                {
                    match = true;
                    break;
                }
            }
        }

        if (match)
        {
            widget.setHidden(true);
            return;
        }

        hidePrayerWidgetChildren(widget.getStaticChildren(), prayerName);
        hidePrayerWidgetChildren(widget.getDynamicChildren(), prayerName);
        hidePrayerWidgetChildren(widget.getNestedChildren(), prayerName);
    }

    private void hidePrayerWidgetChildren(Widget[] children, String prayerName)
    {
        if (children == null) return;
        for (Widget child : children)
        {
            hidePrayerWidgetByName(child, prayerName);
        }
    }

    private static boolean containsText(String value, String needle)
    {
        if (value == null) return false;
        return value.replaceAll("<[^>]*>", "")
            .toLowerCase(java.util.Locale.ROOT)
            .contains(needle);
    }

    void restoreAllAbilities()
    {
        for (RewindPrayer prayer : RewindPrayer.values())
        {
            Widget widget = client.getWidget(prayer.getPackedID());
            if (widget != null) widget.setHidden(false);
        }
    }
}
