package com.chrono;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Greys locked skills by resolving the live Stats-tab widgets from their
 * "View <skill> guide" action rather than relying on component positions.
 */
public class ChronoSkillOverlay extends Overlay
{
    private final Client client;
    private final ChronoPlugin plugin;

    @Inject
    ChronoSkillOverlay(Client client, ChronoPlugin plugin)
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

        Widget stats = client.getWidget(InterfaceID.Stats.UNIVERSE);
        if (stats == null || stats.isHidden())
        {
            return null;
        }

        List<Skill> unlocked = Release.getSkills(plugin.getCurrentRelease());
        renderSkillWidgets(graphics, stats.getStaticChildren(), unlocked);
        renderSkillWidgets(graphics, stats.getDynamicChildren(), unlocked);
        return null;
    }

    private static void renderSkillWidgets(Graphics2D graphics, Widget[] widgets, List<Skill> unlocked)
    {
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

            Skill skill = skillForWidget(widget);
            if (skill == null)
            {
                continue;
            }

            boolean locked = HistoricalPermanentExclusions.isSkillPermanentlyLocked(skill)
                || !unlocked.contains(skill);
            if (locked)
            {
                ChronoItemOverlay.paintLocked(graphics, widget.getBounds());
            }
        }
    }

    static Skill skillForWidget(Widget widget)
    {
        String[] actions = widget.getActions();
        if (actions == null)
        {
            return null;
        }

        for (String action : actions)
        {
            if (action == null)
            {
                continue;
            }

            String value = action.replaceAll("<[^>]*>", "").trim();
            if (!value.regionMatches(true, 0, "View ", 0, 5)
                || !value.toLowerCase(java.util.Locale.ROOT).endsWith(" guide"))
            {
                continue;
            }

            String skillName = value.substring(5, value.length() - 6).trim();
            for (ChronoSkill chronoSkill : ChronoSkill.values())
            {
                if (chronoSkill.getSkill().getName().equalsIgnoreCase(skillName))
                {
                    return chronoSkill.getSkill();
                }
            }
        }

        return null;
    }
}
