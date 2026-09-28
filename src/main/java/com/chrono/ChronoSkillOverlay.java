package com.chrono;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/** Uses current API component IDs and current bounds on every frame. */
public class ChronoSkillOverlay extends Overlay {
    private final Client client;
    private final ChronoPlugin plugin;

    @Inject
    ChronoSkillOverlay(Client client, ChronoPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (client.getGameState() != GameState.LOGGED_IN || plugin.getCurrentRelease() == null) return null;
        List<Skill> unlocked = Release.getSkills(plugin.getCurrentRelease());
        for (ChronoSkill skill : ChronoSkill.values()) {
            if (!HistoricalPermanentExclusions.isSkillPermanentlyLocked(skill.getSkill())
                && unlocked.contains(skill.getSkill())) continue;
            Widget widget = client.getWidget(skill.getWidgetID());
            if (widget != null && !widget.isHidden()) {
                ChronoItemOverlay.paintLocked(graphics, widget.getBounds());
            }
        }
        return null;
    }
}
