package com.rewind;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
 * Hides only the prayer/spell widgets explicitly catalogued by Rewind.
 * This deliberately avoids traversing the whole spellbook interface because
 * RuneScape keeps widgets for other spellbooks/filter states in that tree.
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

        List<RewindSpell> spells = plugin.getUnlockedSpells();

        RewindSpell hoveredSpell = null;
        net.runelite.api.Point mouse = client.getMouseCanvasPosition();
        if (mouse != null)
        {
            for (RewindSpell spell : RewindSpell.values())
            {
                Widget widget = client.getWidget(spell.getPackedID());
                Rectangle bounds = widget == null ? null : widget.getBounds();
                if (widget != null && !widget.isHidden() && bounds != null && bounds.contains(mouse.getX(), mouse.getY()))
                {
                    hoveredSpell = spell;
                    break;
                }
            }
        }

        int hoveredLevel = magicLevel(hoveredSpell);
        graphics.setColor(new Color(62, 53, 41));

        for (RewindSpell spell : RewindSpell.values())
        {
            if (spells.contains(spell))
            {
                continue;
            }

            Widget widget = client.getWidget(spell.getPackedID());
            if (widget == null || widget.isHidden())
            {
                continue;
            }

            Rectangle bounds = widget.getBounds();
            if (bounds != null && bounds.width > 0 && bounds.height > 0)
            {
                Rectangle spellbook = spellbookBounds();
                boolean suppressCover = false;
                if (spellbook != null && hoveredLevel > 0)
                {
                    int splitY = spellbook.y + spellbook.height / 2;
                    suppressCover = hoveredLevel <= 53 ? bounds.y >= splitY : bounds.y < splitY;
                }

                if (!suppressCover)
                {
                    graphics.fillRect(bounds.x + 1, bounds.y + 1,
                        Math.max(1, bounds.width - 2), Math.max(1, bounds.height - 2));
                }
            }
        }

        int magicGroup = RewindSpell.LUMBRIDGE_HOME_TELEPORT.getPackedID() >>> 16;
        Widget magicRoot = client.getWidget(magicGroup, 0);
        shadeMinigameTeleport(graphics, magicRoot, hoveredLevel);
        shadeAlwaysHiddenSpell(graphics, magicRoot, "civitas illa fortis");
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
                if (containsText(action, needle)) { match = true; break; }
            }
        }
        if (match) { widget.setHidden(true); return; }
        hidePrayerWidgetChildren(widget.getStaticChildren(), prayerName);
        hidePrayerWidgetChildren(widget.getDynamicChildren(), prayerName);
        hidePrayerWidgetChildren(widget.getNestedChildren(), prayerName);
    }

    private void hidePrayerWidgetChildren(Widget[] children, String prayerName)
    {
        if (children == null) return;
        for (Widget child : children) hidePrayerWidgetByName(child, prayerName);
    }

    private void shadeAlwaysHiddenSpell(Graphics2D graphics, Widget widget, String spellName)
    {
        if (widget == null || widget.isHidden()) return;
        String needle = spellName.toLowerCase(java.util.Locale.ROOT);
        boolean match = containsText(widget.getName(), needle) || containsText(widget.getText(), needle);
        String[] actions = widget.getActions();
        if (!match && actions != null)
        {
            for (String action : actions)
            {
                if (containsText(action, needle)) { match = true; break; }
            }
        }
        if (match)
        {
            Rectangle bounds = widget.getBounds();
            if (bounds != null && bounds.width > 0 && bounds.height > 0)
            {
                graphics.fillRect(bounds.x + 1, bounds.y + 1,
                    Math.max(1, bounds.width - 2), Math.max(1, bounds.height - 2));
            }
        }
        shadeAlwaysHiddenChildren(graphics, widget.getStaticChildren(), spellName);
        shadeAlwaysHiddenChildren(graphics, widget.getDynamicChildren(), spellName);
        shadeAlwaysHiddenChildren(graphics, widget.getNestedChildren(), spellName);
    }

    private void shadeAlwaysHiddenChildren(Graphics2D graphics, Widget[] children, String spellName)
    {
        if (children == null) return;
        for (Widget child : children) shadeAlwaysHiddenSpell(graphics, child, spellName);
    }

    private static boolean containsText(String value, String needle)
    {
        if (value == null) return false;
        return value.replaceAll("<[^>]*>", "").toLowerCase(java.util.Locale.ROOT).contains(needle);
    }

    private Rectangle spellbookBounds()
    {
        int group = RewindSpell.LUMBRIDGE_HOME_TELEPORT.getPackedID() >>> 16;
        Widget root = client.getWidget(group, 0);
        return root == null ? null : root.getBounds();
    }

    private static int magicLevel(RewindSpell spell)
    {
        if (spell == null) return -1;
        switch (spell)
        {
            case LUMBRIDGE_HOME_TELEPORT: return 0;
            case WIND_STRIKE: return 1;
            case CONFUSE: return 3;
            case ENCHANT_CROSSBOW_BOLT: return 4;
            case WATER_STRIKE: return 5;
            case LVL_1_ENCHANT: return 7;
            case EARTH_STRIKE: return 9;
            case WEAKEN: return 11;
            case FIRE_STRIKE: return 13;
            case BONES_TO_BANANAS: return 15;
            case WIND_BOLT: return 17;
            case CURSE: return 19;
            case BIND: return 20;
            case LOW_LEVEL_ALCHEMY: return 21;
            case WATER_BOLT: return 23;
            case VARROCK_TELEPORT: return 25;
            case LVL_2_ENCHANT: return 27;
            case EARTH_BOLT: return 29;
            case LUMBRIDGE_TELEPORT: return 31;
            case TELEKINETIC_GRAB: return 33;
            case FIRE_BOLT: return 35;
            case FALADOR_TELEPORT: return 37;
            case CRUMBLE_UNDEAD: return 39;
            case TELEPORT_TO_HOUSE: return 40;
            case WIND_BLAST: return 41;
            case SUPERHEAT_ITEM: return 43;
            case CAMELOT_TELEPORT: return 45;
            case WATER_BLAST: return 47;
            case LVL_3_ENCHANT: return 49;
            case IBAN_BLAST: return 50;
            case SNARE: return 50;
            case MAGIC_DART: return 50;
            case ARDOUGNE_TELEPORT: return 51;
            case EARTH_BLAST: return 53;
            case HIGH_LEVEL_ALCHEMY: return 55;
            case CHARGE_WATER_ORB: return 56;
            case LVL_4_ENCHANT: return 57;
            case WATCHTOWER_TELEPORT: return 58;
            case FIRE_BLAST: return 59;
            case CHARGE_EARTH_ORB: return 60;
            case BONES_TO_PEACHES: return 60;
            case SARADOMIN_STRIKE: return 60;
            case CLAWS_OF_GUTHIX: return 60;
            case FLAMES_OF_ZAMORAK: return 60;
            case TROLLHEIM_TELEPORT: return 61;
            case WIND_WAVE: return 62;
            case CHARGE_FIRE_ORB: return 63;
            case APE_ATOLL_TELEPORT: return 64;
            case WATER_WAVE: return 65;
            case CHARGE_AIR_ORB: return 66;
            case VULNERABILITY: return 66;
            case LVL_5_ENCHANT: return 68;
            case EARTH_WAVE: return 70;
            case ENFEEBLE: return 73;
            case TELEOTHER_LUMBRIDGE: return 74;
            case FIRE_WAVE: return 75;
            case ENTANGLE: return 79;
            case STUN: return 80;
            case CHARGE: return 80;
            case WIND_SURGE: return 81;
            case TELEOTHER_FALADOR: return 82;
            case TELE_BLOCK: return 85;
            case WATER_SURGE: return 85;
            case LVL_6_ENCHANT: return 87;
            case TELEOTHER_CAMELOT: return 90;
            case EARTH_SURGE: return 90;
            case LVL_7_ENCHANT: return 93;
            case FIRE_SURGE: return 95;
            default: return 54;
        }
    }

    private void shadeMinigameTeleport(Graphics2D graphics, Widget widget, int hoveredLevel)
    {
        if (widget == null || widget.isHidden()) return;
        String name = widget.getName();
        String text = widget.getText();
        String[] actions = widget.getActions();
        boolean minigame = containsMinigameTeleport(name) || containsMinigameTeleport(text);
        if (!minigame && actions != null)
        {
            for (String action : actions)
            {
                if (containsMinigameTeleport(action)) { minigame = true; break; }
            }
        }
        if (minigame)
        {
            Rectangle bounds = widget.getBounds();
            if (bounds != null && bounds.width > 0 && bounds.height > 0)
            {
                Rectangle spellbook = spellbookBounds();
                boolean suppressCover = false;
                if (spellbook != null && hoveredLevel > 0)
                {
                    int splitY = spellbook.y + spellbook.height / 2;
                    suppressCover = hoveredLevel <= 53 ? bounds.y >= splitY : bounds.y < splitY;
                }
                if (!suppressCover)
                {
                    graphics.fillRect(bounds.x + 1, bounds.y + 1,
                        Math.max(1, bounds.width - 2), Math.max(1, bounds.height - 2));
                }
            }
        }
        shadeMinigameChildren(graphics, widget.getStaticChildren(), hoveredLevel);
        shadeMinigameChildren(graphics, widget.getDynamicChildren(), hoveredLevel);
        shadeMinigameChildren(graphics, widget.getNestedChildren(), hoveredLevel);
    }

    private void shadeMinigameChildren(Graphics2D graphics, Widget[] children, int hoveredLevel)
    {
        if (children == null) return;
        for (Widget child : children) shadeMinigameTeleport(graphics, child, hoveredLevel);
    }

    private static boolean containsMinigameTeleport(String value)
    {
        if (value == null) return false;
        String plain = value.replaceAll("<[^>]*>", "").toLowerCase(java.util.Locale.ROOT);
        return plain.contains("minigame teleport") || plain.contains("cast minigame teleport");
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
