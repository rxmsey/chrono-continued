package com.rewind;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;

/**
 * Short-lived in-game banner for Rewind objectives. It deliberately renders in
 * the game viewport rather than using desktop notifications or chat.
 */
public class RewindCompletionOverlay extends Overlay
{
    private static final long DISPLAY_MS = 5000L;
    private static final int WIDTH = 330;
    private static final int HEIGHT = 86;

    private volatile String objective;
    private volatile String timeline;
    private volatile String progress;
    private volatile long visibleUntil;

    @Inject
    RewindCompletionOverlay()
    {
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPriority(OverlayPriority.HIGHEST);
    }

    void show(String objective, String timeline, int completed, int available)
    {
        this.objective = objective;
        this.timeline = timeline;
        this.progress = available > 0 ? completed + " / " + available + " historical objectives" : "";
        this.visibleUntil = System.currentTimeMillis() + DISPLAY_MS;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (objective == null || System.currentTimeMillis() >= visibleUntil)
        {
            return null;
        }

        Rectangle clip = graphics.getClipBounds();
        int canvasWidth = clip == null ? 765 : clip.x + clip.width;
        int x = Math.max(0, (canvasWidth - WIDTH) / 2);
        int y = 55;

        // OSRS-style dark framed notification with warm gold trim.
        graphics.setColor(new Color(20, 16, 12, 238));
        graphics.fillRect(x, y, WIDTH, HEIGHT);
        graphics.setColor(new Color(111, 86, 51));
        graphics.drawRect(x, y, WIDTH - 1, HEIGHT - 1);
        graphics.setColor(new Color(201, 165, 92));
        graphics.drawRect(x + 2, y + 2, WIDTH - 5, HEIGHT - 5);

        drawCentered(graphics, x, "REWIND COMPLETION", y + 20,
            FontManager.getRunescapeBoldFont(), new Color(255, 152, 31));
        drawCentered(graphics, x, objective, y + 43,
            FontManager.getRunescapeBoldFont(), Color.WHITE);
        drawCentered(graphics, x, "Historical objective completed", y + 60,
            FontManager.getRunescapeSmallFont(), new Color(255, 205, 110));

        String footer = timeline + (progress.isEmpty() ? "" : "  -  " + progress);
        drawCentered(graphics, x, footer, y + 76,
            FontManager.getRunescapeSmallFont(), new Color(190, 190, 190));

        return new Dimension(WIDTH, HEIGHT);
    }

    private static void drawCentered(Graphics2D graphics, int x, String text, int y, Font font, Color color)
    {
        graphics.setFont(font);
        graphics.setColor(color);
        FontMetrics metrics = graphics.getFontMetrics();
        graphics.drawString(text, x + (WIDTH - metrics.stringWidth(text)) / 2, y);
    }
}
