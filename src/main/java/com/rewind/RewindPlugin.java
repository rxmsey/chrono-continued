package com.rewind;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import javax.inject.Inject;

import com.rewind.regionlocker.RegionBorderOverlay;
import com.rewind.regionlocker.RegionLocker;
import com.rewind.regionlocker.RegionLockerOverlay;
import com.rewind.regionlocker.HistoricalRegionState;
import com.rewind.regionlocker.HistoricalSceneMaskOverlay;
import com.rewind.regionlocker.HistoricalMinimapMaskOverlay;
import com.rewind.regionlocker.HistoricalMinimapInputBlocker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.DBTableID;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.widgets.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.Notifier;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Slf4j
@PluginDescriptor(
		name = "Rewind",
        configName = "chronoplugin", // Preserve RuneLite's enabled/disabled preference.
		description = "Experience Old School RuneScape through its historical timeline.",
		tags = {"time traveler", "by release"}
)
public class RewindPlugin extends Plugin {
    // Preserve existing release and region preferences across the Rewind rebrand.
	public static final String CONFIG_GROUP_KEY = "chrono";
	public static final String CONFIG_RELEASE_DATE_KEY = "releasedate";
	private static final int GRAND_EXCHANGE_REGION = 12598;

	private static final int SOUND_EFFECT_FAIL = 2277;
	private static final int SOUND_EFFECT_INACTIVE = 2673;
	private static final List<String> MENU_BLACKLIST = Arrays.asList("Use", "Take", "Wield","Empty", "Eat", "Wear", "Read", "Check", "Teleport", "Commune", "Drink", "Bury", "Scatter");

	@Inject
	private Client client;

	@Inject
	@Getter
	private RewindConfig config;

	@Inject
	private RegionLockerOverlay regionLockerOverlay;

	@Inject
	private RegionBorderOverlay regionBorderOverlay;

	@Inject
	private HistoricalSceneMaskOverlay historicalSceneMaskOverlay;

	@Inject
	private HistoricalMinimapMaskOverlay historicalMinimapMaskOverlay;

	@Inject
	private HistoricalMinimapInputBlocker historicalMinimapInputBlocker;

	@Inject
	private MouseManager mouseManager;

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
	private RewindItemOverlay itemOverlay;

	@Inject
	private RewindSkillOverlay skillOverlay;

	@Inject
	private RewindAbilityOverlay abilityOverlay;

	@Inject
	private Gson gson;

	@Inject
	private RenderCallbackManager renderCallbackManager;
    private volatile boolean maskScene;

	@Inject
	private ClientToolbar clientToolbar;

    @Inject
    private Notifier notifier;

	@Getter
	private Release currentRelease;

	@Getter
	@Setter
	private int hoveredRegion = -1;

	private RewindPanel panel;
	private NavigationButton navButton;

    private Set<Integer> sailingObjects = Collections.emptySet();
    private Set<String> unlockedQuestNames = Collections.emptySet();
    private final Set<String> completionSnapshot = new HashSet<>();
    private boolean completionSnapshotReady;

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
	RewindConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(RewindConfig.class);
	}

	@Override
	protected void startUp() {
		loadDefinitions();
		currentRelease = Release.getReleaseByDate(config.release());
        updateUnlockedQuestNames();
		HistoricalRegionState.setSelectedDate(config.release().getDate());
		HistoricalRegionState.replaceWith(Release.getRegions(currentRelease));
		overlayManager.add(itemOverlay);
		overlayManager.add(skillOverlay);
		overlayManager.add(abilityOverlay);
		overlayManager.add(regionLockerOverlay);
		overlayManager.add(historicalSceneMaskOverlay);
		overlayManager.add(historicalMinimapMaskOverlay);
        mouseManager.registerMouseListener(historicalMinimapInputBlocker);

		panel = new RewindPanel(this);
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "panel_icon.png");
		navButton = NavigationButton.builder()
				.tooltip("Rewind")
				.priority(5)
				.icon(icon)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);
        maskScene = config.maskLockedScene();
        updateAdditionalRegions();
        renderCallbackManager.register(drawListener);
        reloadScene();
        clientThread.invokeLater(this::refreshWidgets);
	}

	@Override
	protected void shutDown() {
		RegionLocker.renderLockedRegions = false;
		overlayManager.remove(itemOverlay);
		skillOverlay.restoreAllSkills();
		overlayManager.remove(skillOverlay);
		overlayManager.remove(abilityOverlay);
		overlayManager.remove(regionLockerOverlay);
		overlayManager.remove(regionBorderOverlay);
		overlayManager.remove(historicalSceneMaskOverlay);
		overlayManager.remove(historicalMinimapMaskOverlay);
        mouseManager.unregisterMouseListener(historicalMinimapInputBlocker);
        historicalMinimapInputBlocker.clear();
		clientToolbar.removeNavigation(navButton);
        renderCallbackManager.unregister(drawListener);
        reloadScene();
        // Rebuild the native spellbook after Rewind is disabled so its script callback
        // no longer filters the spell array.
        clientThread.invokeLater(this::redrawSpellbook);
        for (RewindPrayer prayer : RewindPrayer.values()) {
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
        EntityDefinition.indexMonsterDefinitions();
        Release[] base = loadDefinitionResource(Release[].class, "releases.json");
        Release[] continued = loadDefinitionResource(Release[].class, "releases-2005-2007.json");
        Release[] merged = Arrays.copyOf(base, base.length + continued.length);
        System.arraycopy(continued, 0, merged, base.length, continued.length);
        Release.setReleases(merged);
    }

    private <T> T loadDefinitionResource(Type type, String resource) {
        try (InputStream stream = RewindPlugin.class.getResourceAsStream(resource)) {
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
		if (e.getKey().equals("unlockGrandExchange")) {
			updateAdditionalRegions();
			reloadScene();
            if (panel != null) panel.refresh();
		}

		if (e.getKey().equals("unlockHomeTeleport")) {
            clientThread.invokeLater(this::updateSpells);
            if (panel != null) panel.refresh();
		}

		if(e.getKey().equals(CONFIG_RELEASE_DATE_KEY)) {
			currentRelease = Release.getReleaseByDate(config.release());
            updateUnlockedQuestNames();
			HistoricalRegionState.setSelectedDate(config.release().getDate());
			HistoricalRegionState.replaceWith(Release.getRegions(currentRelease));
            updateAdditionalRegions();
            clientThread.invokeLater(this::refreshWidgets);
            reloadScene();

			panel.updateDescription(currentRelease.getDescription());
		}
	}

    @Subscribe
    public void onClientTick(net.runelite.api.events.ClientTick event) {
        historicalMinimapInputBlocker.refresh();
    }

	@Subscribe
	public void onGameStateChanged(GameStateChanged e){
        historicalMinimapInputBlocker.clear();
        if (e.getGameState() == GameState.LOGGED_IN) {
            clientThread.invokeLater(() -> {
                refreshWidgets();
                seedCompletionSnapshot();
                if (panel != null) panel.refresh();
            });
        } else if (e.getGameState() == GameState.LOGIN_SCREEN || e.getGameState() == GameState.HOPPING) {
            completionSnapshotReady = false;
            completionSnapshot.clear();
        }
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
        // The scene mask is visual only. Enforce the same region boundary on direct
        // movement/object/ground-item actions so blacked-out regions are not still usable.
        if (isSceneLocationAction(e.getMenuAction())) {
            WorldPoint actionLocation = getMenuWorldPoint(e);
            if (actionLocation != null && !HistoricalRegionState.isTileUnlocked(actionLocation)) {
                deny(e); return;
            }
        }
        // Greyed pre-release skills should not still open their modern skill guides.
        if (widget != null && option.toLowerCase(Locale.ROOT).startsWith("view ")) {
            Skill skill = RewindSkillOverlay.skillForWidget(widget);
            if (skill != null && (HistoricalPermanentExclusions.isSkillPermanentlyLocked(skill)
                || !Release.getSkills(currentRelease).contains(skill))) {
                deny(e); return;
            }
        }
        // Quick prayers are a post-2007 feature and can activate locked prayers in one click.
        if ((option.toLowerCase(java.util.Locale.ROOT).contains("quick-prayer")
            || target.toLowerCase(java.util.Locale.ROOT).contains("quick-prayer"))
            && !option.toLowerCase(java.util.Locale.ROOT).contains("deactivate")) {
            deny(e); return;
        }
        if (option.equals("Activate") && (group == InterfaceID.PRAYERBOOK
            || Arrays.stream(RewindPrayer.values()).anyMatch(p -> p.getName().equalsIgnoreCase(target)))) {
            boolean allowed = Arrays.stream(RewindPrayer.values()).anyMatch(p ->
                p.getName().equalsIgnoreCase(target) && Release.getPrayers(currentRelease).contains(p.getPrayer()));
            if (!allowed) { deny(e); return; }
        }
        if (group == InterfaceID.PRAYERBOOK && isPrayerActivation(option)
            && !isPrayerWidgetUnlocked(widget, target)) {
            deny(e); return;
        }
        List<RewindSpell> spells = getUnlockedSpells();
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
            && !isNpcUnlocked(npc)) {
            deny(e); return;
        }
        // Match actual item operations plus the known item-use verbs. Shop stock gets an
        // explicit group check because RuneScape may expose Buy actions as generic widget ops.
        // Do not gate every widget with an item id: several historical interfaces reuse
        // pseudo-item ids purely as icons (for example Construction/build menus).
        boolean itemAction = e.isItemOp() || MENU_BLACKLIST.contains(option)
            || e.getMenuAction().name().startsWith("GROUND_ITEM_")
            || e.getMenuAction() == net.runelite.api.MenuAction.WIDGET_TARGET
            || group == InterfaceID.SHOPMAIN;
        boolean disposal = option.equals("Drop") || option.equals("Destroy") || option.equals("Examine")
            || option.startsWith("Deposit") || option.equals("Release") || option.equals("Remove");
        int itemId = e.getItemId();
        if (e.getMenuAction().name().startsWith("GROUND_ITEM_")) itemId = e.getId();
        if ((widget != null && widget.getId() == InterfaceID.GeOffers.SETUP_CONFIRM)
            || (group == InterfaceID.GE_OFFERS && option.equalsIgnoreCase("Confirm"))) {
            itemId = client.getVarpValue(VarPlayerID.TRADINGPOST_SEARCH);
        }
        if (config.unlockGrandExchange() && isGrandExchangeGroup(group)
            && itemId >= 0 && !isItemUnlocked(itemId)) {
            deny(e);
            addWarningMessage("This item was not available by " + config.release().getName() + ".", false);
            return;
        }
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
        updateSpells();
        redrawQuests();
    }

    /**
     * RuneLite's quest-list script calls this callback once for each quest row while it
     * builds the native list. Force historically unavailable quests to be filtered out,
     * but leave already-filtered rows alone so RuneLite's own search/status filters and
     * native Free/Members/release-date grouping continue to work normally.
     */
    @Subscribe(priority = -1) // run after RuneLite's native Quest List/Spellbook filtering
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        if (currentRelease == null) return;

        if ("questFilter".equals(event.getEventName())) {
            int[] intStack = client.getIntStack();
            int intStackSize = client.getIntStackSize();
            if (intStack == null || intStackSize < 2) return;

            int row = intStack[intStackSize - 1];
            Object[] displayName = client.getDBTableField(row, DBTableID.Quest.COL_DISPLAYNAME, 0);
            if (displayName == null || displayName.length == 0 || !(displayName[0] instanceof String)) return;

            if (!unlockedQuestNames.contains((String) displayName[0])) {
                intStack[intStackSize - 2] = 1;
            }
            return;
        }

        if ("spellbookSort".equals(event.getEventName())) {
            filterHistoricalSpells();
        }
    }

    /**
     * The game builds an array containing the spells which will be laid out in the
     * current spellbook. Filter that array instead of hiding/painting over widgets.
     * This is the same callback used by RuneLite's core Spellbook plugin, so the
     * remaining spells retain their native widgets, tooltips and menu behaviour.
     */
    private void filterHistoricalSpells() {
        int[] stack = client.getIntStack();
        int size = client.getIntStackSize();
        if (stack == null || size < 3) return;

        int spellbookEnumId = stack[size - 3];
        int spellArrayId = stack[size - 2];
        int numSpells = stack[size - 1];

        EnumComposition spellbook = client.getEnum(spellbookEnumId);
        int[] spells = client.getArray(spellArrayId);
        if (spellbook == null || spells == null || numSpells <= 0) return;

        Set<Integer> allowedWidgets = new HashSet<>();
        for (RewindSpell spell : getUnlockedSpells()) {
            allowedWidgets.add(spell.getPackedID());
        }

        int write = 0;
        for (int read = 0; read < numSpells; ++read) {
            int enumIndex = spells[read];
            ItemComposition spellDefinition = client.getItemDefinition(spellbook.getIntValue(enumIndex));
            int spellWidget = spellDefinition.getIntValue(ParamID.SPELL_BUTTON);
            if (allowedWidgets.contains(spellWidget)) {
                spells[write++] = enumIndex;
            } else {
                // The native redraw only lays out entries left in the spell array.
                // Explicitly hide excluded widgets during that same redraw pass so
                // their old/default bounds cannot remain stacked under Home Teleport
                // and produce a giant ghost context menu.
                Widget spell = client.getWidget(spellWidget);
                if (spell != null) spell.setHidden(true);
            }
        }

        stack[size - 1] = write;
    }

    private void updateUnlockedQuestNames() {
        if (currentRelease == null) {
            unlockedQuestNames = Collections.emptySet();
            return;
        }
        Set<String> names = new HashSet<>();
        for (Quest quest : Release.getQuests(currentRelease)) {
            names.add(quest.getName());
        }
        unlockedQuestNames = names;
    }

    private void redrawQuests() {
        if (client.getGameState() != GameState.LOGGED_IN) return;
        Widget container = client.getWidget(InterfaceID.Questlist.CONTAINER);
        if (container == null) return;
        Object[] onVarTransmitListener = container.getOnVarTransmitListener();
        if (onVarTransmitListener == null) return;
        clientThread.invokeLater(() -> client.runScript(onVarTransmitListener));
    }

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e) {
		if (e.getGroupId() == InterfaceID.TOPLEVEL_OSRS_STRETCH || e.getGroupId() == InterfaceID.TOPLEVEL) {
            this.updatePrayers();
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
				return isNpcUnlocked(npc);
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
        for (RewindPrayer prayer : RewindPrayer.values()) {
            Widget parent = client.getWidget(prayer.getPackedID());
            if (parent != null) parent.setOpacity(unlocked.contains(prayer.getPrayer()) ? 0 : 160);
        }
    }

    List<RewindSpell> getUnlockedSpells() {
        List<RewindSpell> unlocked = new ArrayList<>(Release.getSpells(currentRelease));
        if (config.unlockHomeTeleport()) {
            addSpell(unlocked, RewindSpell.LUMBRIDGE_HOME_TELEPORT);
            addSpell(unlocked, RewindSpell.EDGEVILLE_HOME_TELEPORT);
            addSpell(unlocked, RewindSpell.LUNAR_HOME_TELEPORT);
        }
        return unlocked;
    }

    private static void addSpell(List<RewindSpell> spells, RewindSpell spell) {
        if (!spells.contains(spell)) spells.add(spell);
    }

    private void updateSpells() {
        redrawSpellbook();
    }

    private void redrawSpellbook() {
        if (client.getGameState() != GameState.LOGGED_IN) return;
        Widget universe = client.getWidget(InterfaceID.MagicSpellbook.UNIVERSE);
        if (universe == null || universe.getOnInvTransmitListener() == null) return;
        client.createScriptEventBuilder(universe.getOnInvTransmitListener())
            .setSource(universe)
            .build()
            .run();
    }

    /**
     * Completion Log v1 deliberately uses only state RuneLite can read reliably:
     * historical quest completion and curated level milestones for skills which
     * existed by the selected date. Boss/activity objectives can be layered onto
     * this model once their persistent completion signals are verified.
     */
    @Subscribe
    public void onStatChanged(StatChanged event) {
        if (!completionSnapshotReady || currentRelease == null) return;
        Skill skill = event.getSkill();
        Integer target = getHistoricalSkillTargets().get(skill);
        if (target != null && event.getLevel() >= target) {
            String key = "skill:" + skill.name() + ":" + target;
            if (completionSnapshot.add(key)) {
                showCompletionPopup(skill.getName() + " " + target);
            }
        }
        checkQuestCompletions();
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        checkQuestCompletions();
    }

    private void checkQuestCompletions() {
        if (!completionSnapshotReady || currentRelease == null
            || client.getGameState() != GameState.LOGGED_IN) return;
        for (Quest quest : Release.getQuests(currentRelease)) {
            String key = "quest:" + quest.name();
            if (!completionSnapshot.contains(key) && isQuestComplete(quest)) {
                completionSnapshot.add(key);
                showCompletionPopup(quest.getName());
            }
        }
    }

    private void seedCompletionSnapshot() {
        if (client.getGameState() != GameState.LOGGED_IN || currentRelease == null) return;
        completionSnapshot.clear();
        for (Quest quest : Release.getQuests(currentRelease)) {
            if (isQuestComplete(quest)) completionSnapshot.add("quest:" + quest.name());
        }
        for (Map.Entry<Skill, Integer> entry : getHistoricalSkillTargets().entrySet()) {
            if (client.getRealSkillLevel(entry.getKey()) >= entry.getValue()) {
                completionSnapshot.add("skill:" + entry.getKey().name() + ":" + entry.getValue());
            }
        }
        completionSnapshotReady = true;
    }

    void testCompletionNotification() {
        String date = currentRelease == null ? "Historical timeline" : currentRelease.getDate().getName();
        notifier.notify("Rewind Completion — Cook's Assistant — TEST (" + date + ")");
        addWarningMessage("Historical milestone completed: Cook's Assistant. (Test)", false);
    }

    private void showCompletionPopup(String objective) {
        CompletionProgress progress = getCompletionProgress();
        String message = "Rewind Completion — " + objective + " — "
            + progress.getCompleted() + " / " + progress.getAvailable()
            + " (" + currentRelease.getDate().getName() + ")";
        notifier.notify(message);
        addWarningMessage("Historical milestone completed: " + objective + ".", false);
        if (panel != null) panel.refresh();
    }

    CompletionProgress getCompletionProgress() {
        if (currentRelease == null || client.getGameState() != GameState.LOGGED_IN) {
            return new CompletionProgress(0, 0, 0, 0);
        }

        List<Quest> availableQuests = Release.getQuests(currentRelease);
        int completedQuests = 0;
        for (Quest quest : availableQuests) {
            try {
                if (quest.getState(client) == QuestState.FINISHED) {
                    completedQuests++;
                }
            } catch (RuntimeException ex) {
                log.debug("Unable to read quest state for {}", quest.getName(), ex);
            }
        }

        int completedMilestones = 0;
        int availableMilestones = 0;
        final Map<Skill, Integer> targets = getHistoricalSkillTargets();
        for (Map.Entry<Skill, Integer> entry : targets.entrySet()) {
            availableMilestones++;
            if (client.getRealSkillLevel(entry.getKey()) >= entry.getValue()) {
                completedMilestones++;
            }
        }

        return new CompletionProgress(
            completedQuests, availableQuests.size(), completedMilestones, availableMilestones);
    }

    /**
     * Completion skill goals follow the content available in the selected timeline.
     * Known quest requirements establish a floor; Rewind then adds a small mastery
     * buffer and rounds up to a clean five-level target. Skills with no quest-driven
     * requirement yet receive a modest target based on how long they have existed.
     */
    Map<Skill, Integer> getHistoricalSkillTargets() {
        Map<Skill, Integer> requirements = new EnumMap<>(Skill.class);
        for (Quest quest : Release.getQuests(currentRelease)) {
            applyQuestRequirements(requirements, quest);
        }

        Map<Skill, Integer> targets = new LinkedHashMap<>();
        LocalDate selected = currentRelease.getDate().getLocalDate();
        for (Skill skill : Release.getSkills(currentRelease)) {
            if (HistoricalPermanentExclusions.isSkillPermanentlyLocked(skill)) continue;
            int requirement = requirements.getOrDefault(skill, 0);
            int target;
            if (requirement > 0) {
                target = roundUpFive(requirement + 5);
            } else {
                long ageMonths = java.time.temporal.ChronoUnit.MONTHS.between(
                    skillIntroductionDate(skill).withDayOfMonth(1), selected.withDayOfMonth(1));
                target = ageMonths < 6 ? 20 : ageMonths < 18 ? 30 : 40;
            }
            targets.put(skill, Math.min(target, historicalSkillCap(selected.getYear())));
        }
        return targets;
    }

    private static int historicalSkillCap(int year) {
        switch (year) {
            case 2001: return 40;
            case 2002: return 50;
            case 2003: return 60;
            case 2004: return 70;
            case 2005: return 75;
            case 2006: return 80;
            default: return 85;
        }
    }

    private static int roundUpFive(int level) {
        return Math.min(99, ((level + 4) / 5) * 5);
    }

    private static void require(Map<Skill, Integer> requirements, Skill skill, int level) {
        requirements.merge(skill, level, Math::max);
    }

    /**
     * Curated hard skill requirements for quests represented in Rewind. Entries are
     * intentionally added only when the requirement is reliable; unknown quests do
     * not invent a requirement and instead fall back to the skill-age target.
     */
    private static void applyQuestRequirements(Map<Skill, Integer> r, Quest q) {
        switch (q) {
            case DRAGON_SLAYER_I: require(r, Skill.MAGIC, 33); break;
            case HEROES_QUEST:
                require(r, Skill.COOKING, 53); require(r, Skill.FISHING, 53);
                require(r, Skill.MINING, 50); require(r, Skill.HERBLORE, 25); break;
            case LOST_CITY: require(r, Skill.CRAFTING, 31); require(r, Skill.WOODCUTTING, 36); break;
            case FAMILY_CREST:
                require(r, Skill.MINING, 40); require(r, Skill.SMITHING, 40);
                require(r, Skill.MAGIC, 59); require(r, Skill.CRAFTING, 40); break;
            case LEGENDS_QUEST:
                require(r, Skill.AGILITY, 50); require(r, Skill.CRAFTING, 50);
                require(r, Skill.HERBLORE, 45); require(r, Skill.MAGIC, 56);
                require(r, Skill.MINING, 52); require(r, Skill.PRAYER, 42);
                require(r, Skill.SMITHING, 50); require(r, Skill.STRENGTH, 50);
                require(r, Skill.THIEVING, 50); require(r, Skill.WOODCUTTING, 50); break;
            case DESERT_TREASURE_I:
                require(r, Skill.FIREMAKING, 50); require(r, Skill.MAGIC, 50);
                require(r, Skill.SLAYER, 10); require(r, Skill.THIEVING, 53); break;
            case MOURNINGS_END_PART_I:
                require(r, Skill.RANGED, 60); require(r, Skill.THIEVING, 50); break;
            case MOURNINGS_END_PART_II: require(r, Skill.AGILITY, 56); break;
            case RECIPE_FOR_DISASTER:
                require(r, Skill.COOKING, 70); break;
            case SWAN_SONG:
                require(r, Skill.MAGIC, 66); require(r, Skill.COOKING, 62);
                require(r, Skill.FISHING, 62); require(r, Skill.SMITHING, 45);
                require(r, Skill.FIREMAKING, 42); require(r, Skill.CRAFTING, 40); break;
            case LUNAR_DIPLOMACY:
                require(r, Skill.MAGIC, 65); require(r, Skill.DEFENCE, 40);
                require(r, Skill.WOODCUTTING, 55); require(r, Skill.MINING, 60);
                require(r, Skill.CRAFTING, 61); require(r, Skill.FIREMAKING, 49); break;
            case DREAM_MENTOR:
                // Combat level is a composite requirement, so individual combat-skill
                // targets continue to use their historical/content-driven goals.
                break;
            case KINGS_RANSOM:
                require(r, Skill.MAGIC, 45); require(r, Skill.DEFENCE, 65); break;
            default: break;
        }
    }

    private LocalDate skillIntroductionDate(Skill skill) {
        for (Release release : Release.getRELEASES()) {
            if (release.getSkills() != null && release.getSkills().contains(skill)) {
                return release.getDate().getLocalDate();
            }
        }
        return ReleaseDate._04_JANUARY_2001.getLocalDate();
    }

    List<Quest> getAvailableCompletionQuests() {
        return currentRelease == null ? Collections.emptyList() : new ArrayList<>(Release.getQuests(currentRelease));
    }

    boolean isQuestComplete(Quest quest) {
        if (quest == null || client.getGameState() != GameState.LOGGED_IN) return false;
        try {
            return quest.getState(client) == QuestState.FINISHED;
        } catch (RuntimeException ex) {
            log.debug("Unable to read quest state for {}", quest.getName(), ex);
            return false;
        }
    }

    Map<Skill, Integer> getAvailableCompletionSkills() {
        Map<Skill, Integer> levels = new LinkedHashMap<>();
        if (currentRelease == null || client.getGameState() != GameState.LOGGED_IN) return levels;
        for (Skill skill : Release.getSkills(currentRelease)) {
            if (!HistoricalPermanentExclusions.isSkillPermanentlyLocked(skill)) {
                levels.put(skill, client.getRealSkillLevel(skill));
            }
        }
        return levels;
    }

    private void updateAdditionalRegions() {
        HistoricalRegionState.setAdditionallyUnlocked(config.unlockGrandExchange()
            ? Collections.singleton(GRAND_EXCHANGE_REGION) : Collections.emptySet());
    }

    private boolean isNpcUnlocked(NPC npc) throws ParseException {
        WorldPoint location = npc.getWorldLocation();
        WorldView worldView = npc.getWorldView();
        LocalPoint localPoint = npc.getLocalLocation();
        if (worldView != null && localPoint != null) {
            location = WorldPoint.fromLocalInstance(worldView.getScene(), localPoint, worldView.getPlane());
        }
        if (config.unlockGrandExchange() && location.getRegionID() == GRAND_EXCHANGE_REGION) {
            return true;
        }
        if (!HistoricalRegionState.isTileUnlocked(location)) {
            return false;
        }
        return EntityDefinition.isMonsterUnlocked(npc.getId(), npc.getName(), config.release().getDate());
    }

    @VisibleForTesting
    static boolean isSceneLocationAction(net.runelite.api.MenuAction action) {
        if (action == null) return false;
        String name = action.name();
        return name.equals("WALK")
            || name.contains("GAME_OBJECT")
            || name.contains("GROUND_ITEM");
    }

    private WorldPoint getMenuWorldPoint(MenuOptionClicked event) {
        Player player = client.getLocalPlayer();
        if (player == null) return null;
        WorldView worldView = player.getWorldView();

        // WALK menu parameters are not a reliable way to identify the clicked tile
        // across every RuneLite input path. RuneLite itself exposes the selected scene
        // tile for ordinary scene walking, so prefer that exact tile.
        if (event.getMenuAction() == net.runelite.api.MenuAction.WALK) {
            Tile selectedTile = client.getSelectedSceneTile();
            if (selectedTile != null) {
                return WorldPoint.fromLocalInstance(
                    worldView.getScene(), selectedTile.getLocalLocation(), selectedTile.getPlane());
            }
        }

        LocalPoint localPoint = LocalPoint.fromScene(event.getParam0(), event.getParam1(), worldView);
        if (localPoint == null) return null;
        return WorldPoint.fromLocalInstance(worldView.getScene(), localPoint, worldView.getPlane());
    }

    @VisibleForTesting
    static boolean isGrandExchangeGroup(int group) {
        return group == InterfaceID.GE_OFFERS || group == InterfaceID.GE_OFFERS_SIDE
            || group == InterfaceID.GE_PRICELIST;
    }

    @VisibleForTesting
    static boolean isPrayerActivation(String option) {
        String value = option == null ? "" : option.toLowerCase(Locale.ROOT);
        return value.contains("activate") && !value.contains("deactivate");
    }

    private boolean isPrayerWidgetUnlocked(Widget widget, String target) {
        List<Prayer> unlocked = Release.getPrayers(currentRelease);
        int widgetId = widget == null ? -1 : widget.getId();
        for (RewindPrayer prayer : RewindPrayer.values()) {
            if (prayer.getPackedID() == widgetId || prayer.getName().equalsIgnoreCase(target)) {
                return unlocked.contains(prayer.getPrayer());
            }
        }
        // Any prayer absent from the historical catalogue is post-backup content.
        return false;
    }

	public static int getCenterX(Widget window, int width) {
		return (window.getWidth() / 2) - (width / 2);
	}

	public static int getCenterY(Widget window, int height) {
		return (window.getHeight() / 2) - (height / 2);
	}
}
