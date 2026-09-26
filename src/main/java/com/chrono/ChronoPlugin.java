package com.chrono;

import com.chrono.regionlocker.RegionTypes;
import com.chrono.ui.UIButton;
import com.chrono.ui.UILabel;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import javax.inject.Inject;

import com.chrono.regionlocker.RegionBorderOverlay;
import com.chrono.regionlocker.RegionLocker;
import com.chrono.regionlocker.RegionLockerOverlay;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.events.*;
import net.runelite.api.widgets.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
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
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
		name = "Chrono",
		description = "Travel back in time",
		tags = {"time traveler", "by release"}
)
public class ChronoPlugin extends Plugin {
	public static final String CONFIG_GROUP_KEY = "chrono";
	public static final String CONFIG_RELEASE_DATE_KEY = "releasedate";

	private static final int SOUND_EFFECT_FAIL = 2277;
	private static final int SOUND_EFFECT_INACTIVE = 2673;
	private static final List<String> MENU_BLACKLIST = Arrays.asList("Use", "Take", "Wield","Empty", "Eat", "Wear", "Read", "Check", "Teleport", "Commune", "Drink", "Bury", "Scatter");

	/* Widget IDs */
	private static final int PRAYER_TAB = 35454979;
	private static final int PRAYER_ORB = 10485777;
	private static final int QUICK_PRAYER = 10485779;

	private static final List<Integer> PRAYER_VARBITS = Arrays.asList(Varbits.PRAYER_PROTECT_FROM_MAGIC);

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
	private Gson gson;

	@Inject
	private Hooks hooks;

	@Inject
	private ClientToolbar clientToolbar;

	@Getter
	private Release currentRelease;

	@Getter
	@Setter
	private int hoveredRegion = -1;

	private ChronoPanel panel;
	private NavigationButton navButton;

	private Map<String, List<Widget>> skillOverlays;

	@Getter
	private boolean mapEnabled;

	/* Widgets */
	private UILabel prayerLocked;
	private UIButton quickPrayer;

	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	@Provides
	ChronoConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChronoConfig.class);
	}

	@Override
	protected void startUp() {
		loadDefinitions();
		currentRelease = Release.getReleaseByDate(config.release());
		overlayManager.add(itemOverlay);

		panel = new ChronoPanel(this);
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "panel_icon.png");
		navButton = NavigationButton.builder()
				.tooltip("Chrono")
				.priority(5)
				.icon(icon)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);
		hooks.registerRenderableDrawListener(drawListener);
	}

	@Override
	protected void shutDown() {
		RegionLocker.renderLockedRegions = false;
		overlayManager.remove(itemOverlay);
		overlayManager.remove(regionLockerOverlay);
		overlayManager.remove(regionBorderOverlay);
		clientToolbar.removeNavigation(navButton);
		hooks.unregisterRenderableDrawListener(drawListener);
	}

	private void loadDefinitions() {
		Type defMapType = new TypeToken<Map<Integer, EntityDefinition>>() {}.getType();
		EntityDefinition.itemDefinitions = loadDefinitionResource(defMapType, "items.json");
		EntityDefinition.monsterDefinition = loadDefinitionResource(defMapType, "monsters.json");

		Release.setReleases(loadDefinitionResource(Release[].class, "releases.json"));
	}


	private <T> T loadDefinitionResource(Type type, String resource) {
		// Load the resource as a stream and wrap it in a reader
		InputStream resourceStream = ChronoPlugin.class.getResourceAsStream(resource);
		assert resourceStream != null;
		InputStreamReader definitionReader = new InputStreamReader(resourceStream);

		// Load the objects from the JSON file
		return gson.fromJson(definitionReader, type);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e) {
		if(!e.getGroup().equals(CONFIG_GROUP_KEY)) return;

		if(e.getKey().equals(CONFIG_RELEASE_DATE_KEY)) {
			currentRelease = Release.getReleaseByDate(config.release());
			clientThread.invokeLater(() -> this.updatePrayers());
			clientThread.invokeLater(() -> this.updateQuests());
			clientThread.invokeLater(() -> this.updateSkillOverlays());

			clientThread.invokeLater(() -> {
				Widget w = client.getWidget(14286848);
				Object[] onLoadListener = w.getOnInvTransmitListener();

				if (onLoadListener == null)
				{
					return;
				}

				client.runScript(onLoadListener);
			});

			panel.updateDescription(currentRelease.getDescription());
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged e){
		if(e.getGameState().equals(GameState.LOGGED_IN)) {

		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked e) throws ParseException {
		if(e.getMenuOption().equals("Activate")) {
			List<ChronoPrayer> prayers = Arrays.stream(ChronoPrayer.values()).filter(p -> e.getMenuTarget().contains(p.getName())).collect(Collectors.toList());

			if(prayers.size() > 0) {
				List<Prayer> unlockedPrayers = Release.getPrayers(currentRelease);

				if(!unlockedPrayers.contains(prayers.get(0).getPrayer())) {
					e.consume();
					client.playSoundEffect(SOUND_EFFECT_INACTIVE);
				}
			}
		}

		if(e.getMenuOption().equals("Cast")) {
			List<ChronoSpell> unlockedSpells = Release.getSpells(currentRelease);
			List<ChronoSpell> validSpells = unlockedSpells.stream().filter(s -> e.getMenuTarget().contains(s.getName())).collect(Collectors.toList());

			if(validSpells.size() == 0) {
				e.consume();
				client.playSoundEffect(SOUND_EFFECT_INACTIVE);
			}
		}

		if(MENU_BLACKLIST.contains(e.getMenuOption())) {
			if(e.getItemId() < 0) return;

			// Catch a weird special case where it says the item ID is 0 when theres no item
			if(e.getItemId() == 0 && !e.getMenuTarget().equals("Dwarf Remains")) return;

			int id = e.getItemId();

			if(!isItemUnlocked(id)) {
				EntityDefinition def = EntityDefinition.itemDefinitions.get(id);
				if(def == null) return;

				e.consume();
				String was = def.getName().endsWith("s") ? "were" : "was";
				addWarningMessage(def.getName()+" "+was+" released after "+config.release().getName()+".", true);
			}
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e) {
		if(e.getGroupId() == WidgetInfo.SKILLS_CONTAINER.getGroupId()) {
			this.createLockedSkillOverlays();
		}
		else if (e.getGroupId() == WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB.getGroupId() || e.getGroupId() == WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB.getGroupId()) {
			this.createPrayerLockWidgets();
			this.updatePrayers();
		}
		else if(e.getGroupId() == WidgetInfo.QUESTLIST_BOX.getGroupId()) {
			this.updateQuests();
		}
		else if(e.getGroupId() == 218) {
			this.updateSpells();
		}
	}





	@Subscribe
	public void onVarbitChanged(VarbitChanged e) {
		if(PRAYER_VARBITS.contains(e.getVarbitId())) updatePrayers();
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
				e.printStackTrace();
				return true;
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

		client.playSoundEffect(SOUND_EFFECT_FAIL);
	}

	public boolean isItemUnlocked(int itemId) throws ParseException {
		return EntityDefinition.isItemUnlocked(itemId, config.release().getDate());
	}

	private void createLockedSkillOverlays() {
		List<ChronoSkill> skillWidgets = Arrays.asList(ChronoSkill.values());
		this.skillOverlays = new HashMap<>();

		skillWidgets.forEach(skill -> addSkillOverlay(skill.getSkill(), skill.getWidgetID()));
	}

	private void addSkillOverlay(Skill skill, int widgetID) {
		Widget skillWidget = client.getWidget(widgetID);
		if(skillWidget == null) return;

		boolean isUnlocked = Release.getSkills(currentRelease).contains(skill);
		List<Widget> widgets = new ArrayList<>();

		Widget icon = skillWidget.createChild(-1, WidgetType.GRAPHIC);
		Widget level = skillWidget.createChild(-1, WidgetType.GRAPHIC);
		level.setSpriteId(176);
		level.setSize(36, 36);
		level.setPos(28, -2);
		level.setOpacity(90);
		level.setHidden(isUnlocked);
		icon.setSpriteId(174);
		icon.setSize(36, 36);
		icon.setPos(-2, -2);
		icon.setOpacity(90);
		icon.setHidden(isUnlocked);

		widgets.add(icon);
		widgets.add(level);
		skillOverlays.put(skill.getName(), widgets);
	}

	private void createPrayerLockWidgets() {
		Widget prayerOrb = client.getWidget(PRAYER_ORB);
		Widget orbWidget = prayerOrb.createChild(-1, WidgetType.GRAPHIC);
		quickPrayer = new UIButton(orbWidget);
		quickPrayer.setSize(prayerOrb.getWidth(), prayerOrb.getHeight());
		quickPrayer.addAction("Disabled", () -> client.playSoundEffect(SOUND_EFFECT_FAIL));
		quickPrayer.setVisibility(false);

		Widget container = client.getWidget(35454976);
		Widget prayerLabel = container.createChild(-1, WidgetType.TEXT);
		prayerLocked = new UILabel(prayerLabel);
		prayerLocked.setText("Prayers have not been released yet.");
		prayerLocked.setColour(ColorScheme.BRAND_ORANGE.getRGB());
		prayerLocked.setSize(150, 75);
		prayerLocked.setPosition(getCenterX(container, 150), getCenterY(container, 75));
		prayerLocked.setVisibility(false);
	}

	private void updatePrayers() {
		if(prayerLocked == null) return;
		
		// Prayers were released in May 2001, despite the skill being available before
		if(currentRelease.getDate().getDate().before(ReleaseDate.MAY_2001.getDate())) {
			client.getWidget(QUICK_PRAYER).setHidden(true);
			client.getWidget(PRAYER_TAB).setHidden(true);
			prayerLocked.setVisibility(true);
			quickPrayer.setVisibility(true);
		}
		else {
			client.getWidget(QUICK_PRAYER).setHidden(false);
			client.getWidget(PRAYER_TAB).setHidden(false);
			prayerLocked.setVisibility(false);
			quickPrayer.setVisibility(false);

			List<Prayer> unlockedPrayers = Release.getPrayers(currentRelease);
			int offset = 4; // IDs change due to updates occasionally, but will always change by the same amount
			for(ChronoPrayer prayer : ChronoPrayer.values()) {
				Widget parent = client.getWidget(prayer.getPackedID() + offset);
				Widget original = parent.getChild(1);

				if(original == null) continue;
				if(unlockedPrayers.contains(prayer.getPrayer())) original.setSpriteId(prayer.getUnlockedSpriteID());
				else original.setSpriteId(prayer.getLockedSpriteID());
				original.revalidate();
			}
		}
	}

	private void updateSpells() {
		Widget parent = client.getWidget(14286851);

		if(parent == null) return;

		List<ChronoSpell> unlockedSpells = Release.getSpells(currentRelease);
		for(ChronoSpell spell : ChronoSpell.values()) {
			if(unlockedSpells.contains(spell)) continue;

			Widget spellWidget = client.getWidget(spell.getPackedID());

			if(spellWidget == null) continue;

			spellWidget.setSpriteId(spell.getLockedSpriteID());
		}
	}

	private void updateQuests() {
		Widget parent = client.getWidget(26148871);

		if(parent == null) return;

		Widget[] quests = parent.getChildren();
		List<Quest> unlockedQuests = Release.getQuests(currentRelease);

		for(Widget questWidget : quests) {
			List<Quest> validQuests = unlockedQuests.stream().filter(q -> q.getName().contains(questWidget.getText())).collect(Collectors.toList());

			// Quest is not unlocked and this is not a header
			if(validQuests.size() == 0 && questWidget.getFontId() == 494) {
				questWidget.setHasListener(false);
				questWidget.setTextColor(Color.GRAY.getRGB());
			}
			else if(questWidget.getFontId() == 494 && questWidget.getTextColor() == Color.GRAY.getRGB()) {
				questWidget.setHasListener(false);
				int color = validQuests.get(0).getState(client) == QuestState.FINISHED ? Integer.parseInt("dc10d", 16) : Integer.parseInt("ff0000", 16);
				questWidget.setTextColor(color);
			}
		}
	}

	private void updateSkillOverlays() {
		if(this.skillOverlays == null) return;

		List<Skill> unlocked = Release.getSkills(currentRelease);

		for(Skill skill : Skill.values()) {
			boolean hide = unlocked.contains(skill);
			List<Widget> overlay = this.skillOverlays.get(skill.getName());
			if(overlay != null && overlay.size() > 0) {
				overlay.forEach(widget -> widget.setHidden(hide));
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
