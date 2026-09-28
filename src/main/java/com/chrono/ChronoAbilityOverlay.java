package com.chrono;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Prayer;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/** Greys prayers and spells which did not exist at the selected historical date. */
public class ChronoAbilityOverlay extends Overlay
{
    private static final Color LOCKED_COLOR = new Color(65, 65, 65, 190);
    private final Client client;
    private final ChronoPlugin plugin;

    @Inject
    ChronoAbilityOverlay(Client client, ChronoPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(Overlay.PRIORITY_HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (client.getGameState() != GameState.LOGGED_IN || plugin.getCurrentRelease() == null)
        {
            return null;
        }

        List<Prayer> prayers = Release.getPrayers(plugin.getCurrentRelease());
        for (ChronoPrayer prayer : ChronoPrayer.values())
        {
            if (!prayers.contains(prayer.getPrayer()))
            {
                paintWidget(graphics, client.getWidget(prayer.getPackedID()));
            }
        }

        List<ChronoSpell> spells = Release.getSpells(plugin.getCurrentRelease());
        for (ChronoSpell spell : ChronoSpell.values())
        {
            if (!spells.contains(spell))
            {
                paintWidget(graphics, client.getWidget(spell.getPackedID()));
            }
        }

        // Fail closed for abilities added after this plugin's explicit catalogues.
        // Only widgets which advertise prayer/spell actions are painted, leaving
        // book filters, backgrounds, tooltips and layout controls untouched.
        paintUnknownAbilities(graphics, InterfaceID.PRAYERBOOK, true);
        paintUnknownAbilities(graphics, InterfaceID.MAGIC_SPELLBOOK, false);
        return null;
    }

    private void paintUnknownAbilities(Graphics2D graphics, int groupId, boolean prayer)
    {
        Widget root = client.getWidget(groupId, 0);
        if (root == null || root.isHidden())
        {
            return;
        }

        Widget[] widgets = root.getNestedChildren();
        if (widgets == null)
        {
            return;
        }

        for (Widget widget : widgets)
        {
            if (widget == null || widget.isHidden())
            {
                continue;
            }

            int packedId = widget.getId();
            if (prayer)
            {
                if (!isKnownPrayerWidget(packedId) && isPrayerAbilityWidget(widget))
                {
                    paintWidget(graphics, widget);
                }
            }
            else if (!isKnownSpellWidget(packedId) && isSpellAbilityWidget(widget))
            {
                paintWidget(graphics, widget);
            }
        }
    }

    private static boolean isKnownPrayerWidget(int packedId)
    {
        for (ChronoPrayer prayer : ChronoPrayer.values())
        {
            if (prayer.getPackedID() == packedId) return true;
        }
        return false;
    }

    private static boolean isKnownSpellWidget(int packedId)
    {
        for (ChronoSpell spell : ChronoSpell.values())
        {
            if (spell.getPackedID() == packedId) return true;
        }
        return false;
    }

    static boolean isPrayerAbilityWidget(Widget widget)
    {
        if (widget == null || widget.getId() >>> 16 != InterfaceID.PRAYERBOOK)
        {
            return false;
        }
        return hasAbilityVerb(widget, "activate") || hasAbilityVerb(widget, "deactivate");
    }

    static boolean isSpellAbilityWidget(Widget widget)
    {
        if (widget == null || widget.getId() >>> 16 != InterfaceID.MAGIC_SPELLBOOK)
        {
            return false;
        }
        return hasAbilityVerb(widget, "cast") || hasAbilityVerb(widget, "autocast");
    }

    private static boolean hasAbilityVerb(Widget widget, String verb)
    {
        String targetVerb = HistoricalSpellRestrictions.clean(widget.getTargetVerb()).toLowerCase(Locale.ROOT);
        if (targetVerb.equals(verb) || targetVerb.contains(verb))
        {
            return true;
        }

        String[] actions = widget.getActions();
        if (actions == null)
        {
            return false;
        }
        for (String action : actions)
        {
            String value = HistoricalSpellRestrictions.clean(action).toLowerCase(Locale.ROOT);
            if (value.equals(verb) || value.contains(verb))
            {
                return true;
            }
        }
        return false;
    }

    static void paintWidget(Graphics2D graphics, Widget widget)
    {
        if (widget == null || widget.isHidden()) return;
        Rectangle bounds = widget.getBounds();
        if (bounds == null || bounds.isEmpty()) return;
        Color original = graphics.getColor();
        graphics.setColor(LOCKED_COLOR);
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        graphics.setColor(original);
    }
}
