package com.rewind;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Greys locked skills by resolving the live Stats-tab widgets from their
 * "View <skill> guide" action rather than relying on component positions.
 *
 * The native Stats tooltip is drawn in the same widget layer. While a skill
 * is hovered, temporarily suppress the grey masks so the XP tooltip remains
 * fully readable instead of being painted over by this overlay.
 */
public class RewindSkillOverlay extends Overlay
{
    private final Client client;
    private final RewindPlugin plugin;

    @Inject
    RewindSkillOverlay(Client client, RewindPlugin plugin)
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
        Point mouse = client.getMouseCanvasPosition();

        // RuneScape's skill XP tooltip is rendered beneath RuneLite overlays.
        // If the cursor is over a skill, don't draw the masks for this frame;
        // otherwise the masks obscure the tooltip text.
        if (isMouseOverSkill(stats.getStaticChildren(), mouse)
            || isMouseOverSkill(stats.getDynamicChildren(), mouse))
        {
            return null;
        }

        renderSkillWidgets(graphics, stats.getStaticChildren(), unlocked);
        renderSkillWidgets(graphics, stats.getDynamicChildren(), unlocked);
        return null;
    }

    private static boolean isMouseOverSkill(Widget[] widgets, Point mouse)
    {
        if (widgets == null || mouse == null)
        {
            return false;
        }

        for (Widget widget : widgets)
        {
            if (widget == null || widget.isHidden() || skillForWidget(widget) == null)
            {
                continue;
            }

            Rectangle bounds = widget.getBounds();
            if (bounds != null && bounds.contains(mouse.getX(), mouse.getY()))
            {
                return true;
            }
        }

        return false;
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
                RewindItemOverlay.paintLocked(graphics, widget.getBounds());
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
            for (RewindSkill rewindSkill : RewindSkill.values())
            {
                if (rewindSkill.getSkill().getName().equalsIgnoreCase(skillName))
                {
                    return rewindSkill.getSkill();
                }
            }
        }

        return null;
    }
}
