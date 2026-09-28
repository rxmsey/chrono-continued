package com.rewind;

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
 * Hides historically unavailable skills from the Stats tab entirely.
 * A skill reappears normally once it is available at the selected release.
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
        updateSkillWidgets(stats.getStaticChildren(), unlocked);
        updateSkillWidgets(stats.getDynamicChildren(), unlocked);
        return null;
    }

    void restoreAllSkills()
    {
        Widget stats = client.getWidget(InterfaceID.Stats.UNIVERSE);
        if (stats == null)
        {
            return;
        }

        restoreSkillWidgets(stats.getStaticChildren());
        restoreSkillWidgets(stats.getDynamicChildren());
    }

    private static void updateSkillWidgets(Widget[] widgets, List<Skill> unlocked)
    {
        if (widgets == null)
        {
            return;
        }

        for (Widget widget : widgets)
        {
            if (widget == null)
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
            widget.setHidden(locked);
        }
    }

    private static void restoreSkillWidgets(Widget[] widgets)
    {
        if (widgets == null)
        {
            return;
        }

        for (Widget widget : widgets)
        {
            if (widget != null && skillForWidget(widget) != null)
            {
                widget.setHidden(false);
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
