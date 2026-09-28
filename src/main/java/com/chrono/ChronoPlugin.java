package com.chrono;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import javax.inject.Inject;

import com.chrono.regionlocker.RegionBorderOverlay;
import com.chrono.regionlocker.RegionLocker;
import com.chrono.regionlocker.RegionLockerOverlay;
import com.chrono.regionlocker.HistoricalRegionState;
import com.chrono.regionlocker.HistoricalSceneMaskOverlay;
import com.chrono.regionlocker.HistoricalMinimapMaskOverlay;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;
import java.util.List;

@Slf4j
@PluginDescriptor(
		name = "Chrono Continued",
		description = "Travel back in time",
		tags = {"time traveler", "by release"}
)
public class ChronoPlugin extends Plugin {
	public static final String CONFIG_GROUP_KEY = "chrono";
	public static final String CONFIG_RELEASE_DATE_KEY = "releasedate";

	private static final int SOUND_EFFECT_FAIL = 2277;
	private static final int SOUND_EFFECT_INACTIVE = 2673;
	private static final List<String> MENU_BLACKLIST = Arrays.asList("Use", "Take", "Wield","Empty", "Eat", "Wear", "Read", "Check", "Teleport", "Commune", "Drink", "Bury", "Scatter");

	@Inject
	private Client client;

	@Inject
	@Getter
	private ChronoConfig config;

	@Inject
	private RegionLockerOverlay regionLockerOverlay;

	@Inject
	private RegionBorderOverlay regionBorderOverlay;

	@Inject
	private HistoricalSceneMaskOverlay historicalSceneMaskOverlay;

	@Inject
	private HistoricalMinimapMaskOverlay historicalMinimapMaskOverlay;

	@Inject
	@Getter
	private ConfigManager configManager;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ChronoItemOverlay itemOverlay;

	@Inject
	private ChronoSkillOverlay skillOverlay;

	@Inject
	private Gson gson;

	@Inject
	private RenderCallbackManager renderCallbackManager;
    private volatile boolean maskScene;

	@Inject
	private ClientToolbar clientToolbar;

	@Getter
	private Release currentRelease;

	@Getter
	@Setter
	private int hoveredRegion = -1;

	private ChronoPanel panel;
	private NavigationButton navButton;

    private Set<Integer> sailingObjects = Collections.emptySet();

	@Getter
	private boolean mapEnabled;

	/* Widgets */

	private final RenderCallback drawListener = new RenderCallback() {
        @Override public boolean addEntity(Renderable entity, boolean ui) { return shouldDraw(entity, ui); }
        @Override public boolean drawTile(Scene scene, Tile tile) {
            return !maskScene || HistoricalRegionState.isTileUnlocked(
                WorldPoint.fromLocalInstance(scene, tile.getLocalLocation(), tile.getPlane()));
        }
        @Override public boolean drawObject(Scene scene, TileObject object) {
            return !maskScene || HistoricalRegionState.isTileUnlocked(
                WorldPoint.fromLocalInstance(scene, object.getLocalLocation(), object.getPlane()));
        }
    };

	@Provides
	ChronoConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChronoConfig.class);
	}

	@Override
	protected void startUp() {
		loadDefinitions();
		currentRelease = Release.getReleaseByDate(config.release());
		HistoricalRegionState.setSelectedDate(config.release().getDate());
		HistoricalRegionState.replaceWith(Release.getRegions(currentRelease));
		overlayManager.add(itemOverlay);
		overlayManager.add(skillOverlay);
		overlayManager.add(regionLockerOverlay);
		overlayManager.add(historicalSceneMaskOverlay);
		overlayManager.add(historicalMinimapMaskOverlay);

		panel = new ChronoPanel(this);
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "panel_icon.png");
		navButton = NavigationButton.builder()
				.tooltip("Chrono Continued")
				.priority(5)
				.icon(icon)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);
        maskScene = config.maskLockedScene();
        renderCallbackManager.register(drawListener);
        reloadScene();
        clientThread.invokeLater(this::refreshWidgets);
	}

	@Override
	protected void shutDown() {
		RegionLocker.renderLockedRegions = false;
		overlayManager.remove(itemOverlay);
		overlayManager.remove(skillOverlay);
		overlayManager.remove(regionLockerOverlay);
		overlayManager.remove(regionBorderOverlay);
		overlayManager.remove(historicalSceneMaskOverlay);
		overlayManager.remove(historicalMinimapMaskOverlay);
		clientToolbar.removeNavigation(navButton);
        renderCallbackManager.unregister(drawListener);
        reloadScene();
        if (client.getGameState() == GameState.LOGGED_IN) updateQuests(false);
        for (ChronoSpell spell : ChronoSpell.values()) {
            Widget widget = client.getWidget(spell.getPackedID());
            if (widget != null) widget.setOpacity(0);
        }
        for (ChronoPrayer prayer : ChronoPrayer.values()) {
            Widget widget = client.getWidget(prayer.getPackedID());
            if (widget != null) widget.setOpacity(0);
        }
	}

	private void loadDefinitions() {
        Integer[] objects = loadDefinitionResource(Integer[].class, "sailing-objects.json");
        sailingObjects = new HashSet<>(Arrays.asList(objects));
        Type defMapType = new TypeToken<Map<Integer, EntityDefinition>>() {}.getType();
        EntityDefinition.itemDefinitions = loadDefinitionResource(defMapType, "items.json");
        Type overrideMapType = new TypeToken<Map<Integer, String>>() {}.getType();
        EntityDefinition.itemReleaseOverrides = loadDefinitionResource(overrideMapType, "item-release-overrides.json");
        EntityDefinition.monsterDefinition = loadDefinitionResource(defMapType, "monsters.json");
        Release[] base = loadDefinitionResource(Release[].class, "releases.json");
        Release[] continued = loadDefinitionResource(Release[].class, "releases-2005-2007.json");
        Release[] merged = Arrays.copyOf(base, base.length + continued.length);
        System.arraycopy(continued, 0, merged, base.length, continued.length);
        Release.setReleases(merged);
    }

    private <T> T loadDefinitionResource(Type type, String resource) {
        try (InputStream stream = ChronoPlugin.class.getResourceAsStream(resource)) {
            if (stream == null) throw new IllegalStateException("Missing resource: " + resource);
            try (InputStreamReader reader = new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8)) {
                return gson.fromJson(reader, type);
            }
        } catch (java.io.IOException ex) {
            throw new IllegalStateException("Cannot read " + resource, ex);
        }
    }

	@Subscribe
	public void onConfigChanged(ConfigChanged e) {
        if(!e.getGroup().equals(CONFIG_GROUP_KEY)) return;
        maskScene = config.maskLockedScene();
        if (e.getKey().equals("maskLockedScene")) reloadScene();

		if(e.getKey().equals(CONFIG_RELEASE_DATE_KEY)) {
			currentRelease = Release.getReleaseByDate(config.release());
			HistoricalRegionState.setSelectedDate(config.release().getDate());
			HistoricalRegionState.replaceWith(Release.getRegions(currentRelease));
            clientThread.invokeLater(this::refreshWidgets);
            reloadScene();

			panel.updateDescription(currentRelease.getDescription());
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged e){
        if (e.getGameState() == GameState.LOGGED_IN) clientThread.invokeLater(this::refreshWidgets);
	}

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked e) throws ParseException {
        String option = HistoricalSpellRestrictions.clean(e.getMenuOption());
        String target = HistoricalSpellRestrictions.clean(e.getMenuTarget());
        Widget widget = e.getWidget();
        int group = widget == null ? -1 : widget.getId() >>> 16;
        if (HistoricalPermanentExclusions.isSailingMenuAction(option, target)
            || (widget != null && HistoricalPermanentExclusions.isSailingWidget(widget.getId()))) {
            deny(e); return;
        }
        // Quick prayers are a post-2007 feature and can activate locked prayers in one click.
        if ((option.toLowerCase(java.util.Locale.ROOT).contains("quick-prayer")
            || target.toLowerCase(java.util.Locale.ROOT).contains("quick-prayer"))
            && !option.toLowerCase(java.util.Locale.ROOT).contains("deactivate")) {
            deny(e); return;
        }
        if (option.equals("Activate") && (group == InterfaceID.PRAYERBOOK
            || Arrays.stream(ChronoPrayer.values()).anyMatch(p -> p.getName().equalsIgnoreCase(target)))) {
            boolean allowed = Arrays.stream(ChronoPrayer.values()).anyMatch(p ->
                p.getName().equalsIgnoreCase(target) && Release.getPrayers(currentRelease).contains(p.getPrayer()));
            if (!allowed) { deny(e); return; }
        }
        List<ChronoSpell> spells = Release.getSpells(currentRelease);
        if (option.equals("Cast") || option.toLowerCase(java.util.Locale.ROOT).contains("autocast")) {
            if (!HistoricalSpellRestrictions.allowed(target, spells)) { deny(e); return; }
        }
        if (group == InterfaceID.MAGIC_SPELLBOOK && e.getMenuAction() == net.runelite.api.MenuAction.WIDGET_TARGET
            && !HistoricalSpellRestrictions.allowedWidget(widget.getId(), spells)) {
            deny(e); return;
        }
        // Re-check a previously selected spell/item after changing the historical date.
        if (e.getMenuAction().name().startsWith("WIDGET_TARGET_ON_")) {
            Widget selected = client.getSelectedWidget();
            if (selected != null && selected.getId() >>> 16 == InterfaceID.MAGIC_SPELLBOOK
                && !HistoricalSpellRestrictions.allowedWidget(selected.getId(), spells)) {
                deny(e); return;
            }
            if (selected != null && selected.getItemId() >= 0 && !isItemUnlocked(selected.getItemId())) {
                deny(e); return;
            }
        }
        if ((e.getMenuAction().name().startsWith("GAME_OBJECT_")
            || e.getMenuAction() == net.runelite.api.MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
            && sailingObjects.contains(e.getId())) {
            deny(e); return;
        }
        NPC npc = e.getMenuEntry().getNpc();
        if (npc != null && !option.equals("Examine")
            && !EntityDefinition.isMonsterUnlocked(npc.getId(), config.release().getDate())) {
            deny(e); return;
        }
        // Match action types as well as labels so new/custom item actions cannot bypass the gate.
        boolean itemAction = e.isItemOp() || MENU_BLACKLIST.contains(option)
            || e.getMenuAction().name().startsWith("GROUND_ITEM_")
            || e.getMenuAction() == net.runelite.api.MenuAction.WIDGET_TARGET;
        boolean disposal = option.equals("Drop") || option.equals("Destroy") || option.equals("Examine")
            || option.startsWith("Deposit") || option.equals("Release") || option.equals("Remove");
        int itemId = e.getItemId();
        if (e.getMenuAction().name().startsWith("GROUND_ITEM_")) itemId = e.getId();
        if (itemAction && !disposal && itemId >= 0 && !isItemUnlocked(itemId)) {
            deny(e);
            addWarningMessage("This item is unavailable or has no verified release date for "
                + config.release().getName() + ".", false);
        }
    }

    private void deny(MenuOptionClicked event) {
        event.consume();
        client.playSoundEffect(SOUND_EFFECT_INACTIVE);
    }

    private void reloadScene() {
        clientThread.invokeLater(() -> {
            if (client.getGameState() == GameState.LOGGED_IN) client.setGameState(GameState.LOADING);
        });
    }

    private void refreshWidgets() {
        if (client.getGameState() != GameState.LOGGED_IN) return;
        updatePrayers();
        updateQuests();
        updateSpells();
    }

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e) {
		if (e.getGroupId() == InterfaceID.TOPLEVEL_OSRS_STRETCH || e.getGroupId() == InterfaceID.TOPLEVEL) {
            this.updatePrayers();
		}
		else if(e.getGroupId() == InterfaceID.QUESTLIST) {
			this.updateQuests();
		}
		else if(e.getGroupId() == InterfaceID.MAGIC_SPELLBOOK) {
			this.updateSpells();
		}
	}





	@VisibleForTesting
	boolean shouldDraw(Renderable renderable, boolean drawingUI) {
		if (renderable instanceof NPC)
		{
			NPC npc = (NPC) renderable;

			if(npc.getInteracting() == client.getLocalPlayer()) {
				return true;
			}

			try {
				return EntityDefinition.isMonsterUnlocked(npc.getId(), config.release().getDate());
            } catch(ParseException e) {
                return false;
			}
		}

		return true;
	}

	private void addWarningMessage(String message, boolean playSound) {
		final ChatMessageBuilder chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(message)
				.append(ChatColorType.NORMAL);

		chatMessageManager.queue(QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage(chatMessage.build())
				.build());

        if (playSound) client.playSoundEffect(SOUND_EFFECT_FAIL);
	}

	public boolean isItemUnlocked(int itemId) throws ParseException {
        ItemComposition item = client.getItemDefinition(itemId);
        // A bank note is the same historical item, not an independent modern item.
        if (item.getNote() != -1) itemId = item.getLinkedNoteId();
        return EntityDefinition.isItemUnlocked(itemId, config.release().getDate());
	}

    private void updatePrayers() {
        List<Prayer> unlocked = Release.getPrayers(currentRelease);
        for (ChronoPrayer prayer : ChronoPrayer.values()) {
            Widget parent = client.getWidget(prayer.getPackedID());
            if (parent != null) parent.setOpacity(unlocked.contains(prayer.getPrayer()) ? 0 : 160);
        }
    }

    private void updateSpells() {
        List<ChronoSpell> unlocked = Release.getSpells(currentRelease);
        for (ChronoSpell spell : ChronoSpell.values()) {
            Widget widget = client.getWidget(spell.getPackedID());
            if (widget != null) widget.setOpacity(unlocked.contains(spell) ? 0 : 160);
        }
    }

    private void updateQuests() { updateQuests(true); }

    private void updateQuests(boolean restrict) {
        Widget parent = client.getWidget(InterfaceID.Questlist.LIST);
        if (parent == null || parent.getChildren() == null) return;
        List<Quest> unlocked = Release.getQuests(currentRelease);
        for (Widget widget : parent.getChildren()) {
            if (widget == null) continue;
            String name = HistoricalSpellRestrictions.clean(widget.getText());
            Quest quest = Arrays.stream(Quest.values()).filter(q -> q.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
            if (quest == null) continue;
            if (restrict && !unlocked.contains(quest)) widget.setTextColor(Color.GRAY.getRGB());
            else {
                QuestState state = quest.getState(client);
                widget.setTextColor(state == QuestState.FINISHED ? 0x00ff00
                    : state == QuestState.IN_PROGRESS ? 0xffff00 : 0xff0000);
            }
        }
    }

	public static int getCenterX(Widget window, int width) {
		return (window.getWidth() / 2) - (width / 2);
	}

	public static int getCenterY(Widget window, int height) {
		return (window.getHeight() / 2) - (height / 2);
	}
}
