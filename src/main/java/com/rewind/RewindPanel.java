package com.rewind;

import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/** Player-facing summary of the currently selected historical release. */
public class RewindPanel extends PluginPanel
{
    private final RewindPlugin plugin;
    private final JLabel dateValue = valueLabel();
    private final JLabel description = new JLabel();
    private final JLabel regionsValue = valueLabel();
    private final JLabel questsValue = valueLabel();
    private final JLabel skillsValue = valueLabel();
    private final JLabel prayersValue = valueLabel();
    private final JLabel spellsValue = valueLabel();
    private final JLabel completionValue = valueLabel();
    private final JLabel completionQuestsValue = valueLabel();
    private final JLabel completionSkillsValue = valueLabel();
    private final JLabel completionActivitiesValue = valueLabel();
    private final JPanel completionDetails = card();
    private final JPanel eraProgress = card();
    private final Map<Integer, JLabel> eraValues = new LinkedHashMap<>();
    private final JLabel geValue = valueLabel();
    private final JButton geToggle = new JButton();
    private final JLabel homeTeleportValue = valueLabel();
    private final JButton homeTeleportToggle = new JButton();
    private final JPanel timelineSelector = card();
    private final JComboBox<ReleaseDate> timelineDates = new JComboBox<>();

    public RewindPanel(RewindPlugin plugin)
    {
        this.plugin = plugin;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel title = new JLabel("Rewind");
        title.setFont(FontManager.getRunescapeBoldFont());
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);

        JLabel subtitle = new JLabel("<html>Experience Old School RuneScape<br>through its historical timeline.</html>");
        subtitle.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        subtitle.setBorder(new EmptyBorder(3, 0, 10, 0));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);

        content.add(sectionTitle("CURRENT DATE"));
        JPanel dateCard = card();
        dateValue.setFont(FontManager.getRunescapeBoldFont());
        dateValue.setForeground(Color.WHITE);
        dateValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateCard.add(dateValue);
        description.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        description.setBorder(new EmptyBorder(6, 0, 0, 0));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateCard.add(description);

        JButton changeTimeline = new JButton("Change Timeline");
        changeTimeline.setFocusable(false);
        changeTimeline.setAlignmentX(Component.LEFT_ALIGNMENT);
        changeTimeline.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        changeTimeline.setToolTipText("Choose a historical date");
        changeTimeline.addActionListener(e -> toggleTimelineSelector());
        dateCard.add(Box.createVerticalStrut(8));
        dateCard.add(changeTimeline);
        content.add(dateCard);

        buildTimelineSelector();
        timelineSelector.setVisible(false);
        content.add(Box.createVerticalStrut(6));
        content.add(timelineSelector);
        content.add(Box.createVerticalStrut(10));

        content.add(sectionTitle("AVAILABLE CONTENT"));
        JPanel unlockCard = card();
        unlockCard.add(row("Regions", regionsValue));
        unlockCard.add(row("Quests", questsValue));
        unlockCard.add(row("Skills", skillsValue));
        unlockCard.add(row("Prayers", prayersValue));
        unlockCard.add(row("Spells", spellsValue));
        content.add(unlockCard);
        content.add(Box.createVerticalStrut(10));

        content.add(sectionTitle("COMPLETION LOG"));
        JPanel completionCard = card();
        completionCard.add(row("Historical completion", completionValue));
        completionCard.add(row("Quests", completionQuestsValue));
        completionCard.add(row("Skill milestones", completionSkillsValue));
        completionCard.add(row("Activities", completionActivitiesValue));
        JButton viewCompletion = new JButton("View Completion Log");
        viewCompletion.setFocusable(false);
        viewCompletion.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewCompletion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        viewCompletion.addActionListener(e ->
        {
            toggleCompletionDetails();
            viewCompletion.setText(completionDetails.isVisible()
                ? "Hide Completion Log"
                : "View Completion Log");
        });
        completionCard.add(Box.createVerticalStrut(8));
        completionCard.add(viewCompletion);

        content.add(completionCard);
        content.add(Box.createVerticalStrut(6));
        completionDetails.setVisible(false);
        content.add(completionDetails);
        content.add(Box.createVerticalStrut(10));

        content.add(sectionTitle("ERA PROGRESS"));
        for (int year = 2001; year <= 2007; year++)
        {
            JLabel value = valueLabel();
            eraValues.put(year, value);
            eraProgress.add(row(Integer.toString(year), value));
        }
        content.add(eraProgress);
        content.add(Box.createVerticalStrut(10));

        content.add(sectionTitle("ACCESS"));
        JPanel accessCard = card();
        accessCard.add(row("Grand Exchange", geValue));
        geToggle.setFocusable(false);
        geToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        geToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        geToggle.setToolTipText("Toggle the Grand Exchange historical access override");
        geToggle.addActionListener(e -> toggleGrandExchange());
        accessCard.add(Box.createVerticalStrut(8));
        accessCard.add(geToggle);
        accessCard.add(Box.createVerticalStrut(10));
        accessCard.add(row("Home Teleport", homeTeleportValue));
        homeTeleportToggle.setFocusable(false);
        homeTeleportToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        homeTeleportToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        homeTeleportToggle.setToolTipText("Toggle the Home Teleport historical access override");
        homeTeleportToggle.addActionListener(e -> toggleHomeTeleport());
        accessCard.add(Box.createVerticalStrut(8));
        accessCard.add(homeTeleportToggle);
        content.add(accessCard);

        add(content, BorderLayout.NORTH);
        refresh();
    }

    public void refresh()
    {
        Release release = plugin.getCurrentRelease();
        if (release == null)
        {
            return;
        }

        dateValue.setText(release.getDate().getName());
        description.setText(html(release.getDescription()));
        regionsValue.setText(Integer.toString(Release.getRegions(release).size()));
        questsValue.setText(Integer.toString(Release.getQuests(release).size()));
        skillsValue.setText(Integer.toString(Release.getSkills(release).size()));
        prayersValue.setText(Integer.toString(Release.getPrayers(release).size()));
        spellsValue.setText(Integer.toString(Release.getSpells(release).size()));
        CompletionProgress completion = plugin.getCompletionProgress();
        completionValue.setText(completion.getAvailable() == 0 ? "—" : completion.getPercentage() + "%");
        completionValue.setForeground(completion.getAvailable() == 0
            ? ColorScheme.LIGHT_GRAY_COLOR
            : completion.getPercentage() == 100
                ? ColorScheme.PROGRESS_COMPLETE_COLOR
                : ColorScheme.BRAND_ORANGE);
        completionQuestsValue.setText(progress(completion.getCompletedQuests(), completion.getAvailableQuests()));
        completionSkillsValue.setText(progress(completion.getCompletedSkillMilestones(), completion.getAvailableSkillMilestones()));
        completionActivitiesValue.setText(progress(completion.getCompletedActivities(), completion.getAvailableActivities()));
        refreshEraProgress();

        // Prepare the hidden Completion Log as soon as the client state is readable.
        // Previously this was only rebuilt while already visible, so after a fresh
        // login the first click opened an empty panel until another timeline change
        // triggered a refresh.
        if (plugin.isClientStateReadable())
        {
            rebuildCompletionDetails();
        }

        updateGrandExchangeState(plugin.getConfig().unlockGrandExchange());
        updateHomeTeleportState(plugin.getConfig().unlockHomeTeleport());
    }

    private void refreshEraProgress()
    {
        Release release = plugin.getCurrentRelease();
        if (release == null || !plugin.isClientStateReadable())
        {
            for (JLabel value : eraValues.values())
            {
                value.setText("—");
            }
            return;
        }

        int selectedYear = release.getDate().getLocalDate().getYear();
        for (Map.Entry<Integer, JLabel> entry : eraValues.entrySet())
        {
            int year = entry.getKey();
            JLabel value = entry.getValue();
            if (year > selectedYear)
            {
                value.setText("Locked");
                value.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
                continue;
            }

            CompletionProgress era = plugin.getEraProgress(year);
            value.setText(era.getAvailable() == 0 ? "—" : era.getPercentage() + "%");
            value.setForeground(era.getAvailable() == 0
                ? ColorScheme.LIGHT_GRAY_COLOR
                : era.getPercentage() == 100
                    ? ColorScheme.PROGRESS_COMPLETE_COLOR
                    : ColorScheme.LIGHT_GRAY_COLOR);
        }
    }

    private void toggleCompletionDetails()
    {
        boolean show = !completionDetails.isVisible();
        completionDetails.setVisible(show);
        revalidate();
        repaint();
        SwingUtilities.invokeLater(() ->
        {
            completionDetails.revalidate();
            completionDetails.getParent().revalidate();
            RewindPanel.this.revalidate();
            RewindPanel.this.repaint();
            if (show)
            {
                completionDetails.scrollRectToVisible(new Rectangle(0, 0, 1, Math.min(40, completionDetails.getHeight())));
            }
        });
    }

    private void rebuildCompletionDetails()
    {
        completionDetails.removeAll();

        JPanel questRows = objectiveList();
        java.util.List<Quest> milestoneQuests = plugin.getAvailableCompletionQuests();
        if (milestoneQuests.isEmpty())
        {
            questRows.add(emptyStateRow("No quest objective at this date."));
        }
        else
        {
            for (Quest quest : milestoneQuests)
            {
                boolean done = plugin.isQuestComplete(quest);
                questRows.add(objectiveRow(done, quest.getName()));
            }
        }
        addExpandableSection(completionDetails, "QUESTS", questRows);

        completionDetails.add(Box.createVerticalStrut(6));

        JPanel skillRows = objectiveList();
        Map<Skill, Integer> levels = plugin.getAvailableCompletionSkills();
        for (Map.Entry<Skill, Integer> target : plugin.getHistoricalSkillTargets().entrySet())
        {
            int currentLevel = levels.getOrDefault(target.getKey(), 1);
            skillRows.add(objectiveRow(
                currentLevel >= target.getValue(),
                target.getKey().getName() + " " + target.getValue() + " (current " + currentLevel + ")"));
        }
        addExpandableSection(completionDetails, "SKILL MILESTONES", skillRows);

        completionDetails.add(Box.createVerticalStrut(6));

        JPanel activityRows = objectiveList();
        boolean hasActivity = false;

        if (plugin.isFishingTrawlerAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isFishingTrawlerComplete(),
                "Complete a Fishing Trawler trip",
                "Take part in the Fishing Trawler minigame and finish a trip. Rewind marks this complete when the Fishing Trawler reward container is populated at the end of the trip."));
            hasActivity = true;
        }

        if (plugin.isMageArenaCapeAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isMageArenaCapeComplete(),
                "Obtain a Mage Arena god cape",
                "Complete the original Mage Arena challenge and obtain any one of the three god capes: Saradomin, Zamorak, or Guthix. Rewind recognises the unimbued god cape when it appears in your inventory, equipment, or bank."));
            hasActivity = true;
        }

        if (plugin.isShadesMorttonAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isShadesMorttonComplete(),
                "Help rebuild the Flamtaer temple",
                "Participate in the Shades of Mort'ton activity by helping rebuild the Flamtaer temple. Rewind detects your contribution from the temple sanctity state while you are in Mort'ton."));
            hasActivity = true;
        }

        if (plugin.isCastleWarsAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isCastleWarsComplete(),
                "Buy a Castle Wars reward",
                "Play Castle Wars, earn tickets, then buy a decorative reward from Lanthus's Castle Wars reward shop. Rewind records the purchase when the reward enters your inventory at Castle Wars."));
            hasActivity = true;
        }

        if (plugin.isSlayerTowerAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isSlayerTowerComplete(),
                "Kill a monster in the Slayer Tower",
                "Fight and kill a monster inside the Slayer Tower. Rewind tracks a monster that your character is fighting and records the objective when that monster dies in a Slayer Tower region."));
            hasActivity = true;
        }

        if (plugin.isBarrowsAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isBarrowsComplete(),
                "Loot a Barrows reward chest",
                "Complete a Barrows run and open the reward chest. RuneScape keeps a persistent Barrows chest counter, which Rewind uses to recognise completion."));
            hasActivity = true;
        }

        if (plugin.isBlastFurnaceAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isBlastFurnaceComplete(),
                "Smelt bars at the Blast Furnace",
                "Use the Blast Furnace to produce bars. Rewind records the objective when the Blast Furnace bar-dispenser reward container contains bars."));
            hasActivity = true;
        }

        if (plugin.isFightCavesAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isFightCavesComplete(),
                "Complete the TzHaar Fight Cave",
                "Reach the end of the TzHaar Fight Cave and defeat TzTok-Jad. Rewind records the completion when Jad dies."));
            hasActivity = true;
        }

        if (plugin.isBonesToPeachesAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isBonesToPeachesComplete(),
                "Unlock Bones to Peaches",
                "Earn enough points in the Mage Training Arena to unlock Bones to Peaches. Rewind reads the game's persistent unlock state for the spell."));
            hasActivity = true;
        }

        if (plugin.isAgilityPyramidAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isAgilityPyramidComplete(),
                "Retrieve a pyramid top",
                "Complete the Agility Pyramid course and retrieve the pyramid top. Rewind records it when a pyramid top enters your inventory while you are at the Agility Pyramid."));
            hasActivity = true;
        }

        if (plugin.isTempleTrekkingAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isTempleTrekkingComplete(),
                "Complete a Temple Trek",
                "Finish a Temple Trek/Burgh de Rott Ramble and receive a reward token. Rewind records the objective when it sees the trek reward token in your player-owned items."));
            hasActivity = true;
        }

        if (plugin.isVoidSetAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isVoidSetComplete(),
                "Void top + robe + gloves + any helm",
                "Earn a basic Void Knight set from Pest Control: Void knight top, robe, gloves, and any one of the melee, ranged, or magic helms."));
            hasActivity = true;
        }

        if (plugin.isRuneDefenderAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isRuneDefenderComplete(),
                "Own a Rune defender",
                "Progress through the Warriors' Guild Cyclopes until you obtain a Rune defender. Rewind remembers the defender once it appears in your inventory, equipment, or bank."));
            hasActivity = true;
        }

        if (plugin.isTroubleBrewingAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isTroubleBrewingComplete(),
                "Earn Pieces of Eight",
                "Play Trouble Brewing on Mos Le'Harmless and earn Pieces of Eight, the minigame's reward currency. Rewind records the objective when Pieces of Eight appear in your player-owned items."));
            hasActivity = true;
        }

        if (plugin.isStrongholdSecurityAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isStrongholdSecurityComplete(),
                "Claim Stronghold boots",
                "Reach the end of the Stronghold of Security and claim either Fancy boots or Fighting boots from the Cradle of Life."));
            hasActivity = true;
        }

        if (plugin.isPyramidPlunderAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isPyramidPlunderComplete(),
                "Loot a Pyramid Plunder artefact",
                "Take part in Pyramid Plunder and loot one of its artefacts, such as an ivory comb, scarab, statuette, seal, or Pharaoh's sceptre. Rewind only credits tradeable artefacts when acquired inside Pyramid Plunder."));
            hasActivity = true;
        }

        if (plugin.isFighterTorsoAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isFighterTorsoComplete(),
                "Own a Fighter torso",
                "Play Barbarian Assault and earn enough role points to buy a Fighter torso. Rewind remembers the torso once it appears in your inventory, equipment, or bank."));
            hasActivity = true;
        }

        if (!hasActivity)
        {
            activityRows.add(emptyStateRow("No tracked activities at this date."));
        }
        addExpandableSection(completionDetails, "ACTIVITIES", activityRows);

        completionDetails.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        completionDetails.revalidate();
        completionDetails.repaint();
    }

    private static void addExpandableSection(JPanel parent, String title, JPanel rows)
    {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.DARK_GRAY_COLOR),
            new EmptyBorder(7, 8, 7, 8)));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel label = new JLabel(title);
        label.setForeground(Color.WHITE);
        header.add(label, BorderLayout.WEST);

        BasicArrowButton arrow = new BasicArrowButton(
            SwingConstants.EAST,
            ColorScheme.DARK_GRAY_COLOR,
            ColorScheme.LIGHT_GRAY_COLOR,
            Color.WHITE,
            ColorScheme.MEDIUM_GRAY_COLOR);
        arrow.setFocusable(false);
        arrow.setOpaque(true);
        arrow.setBorder(BorderFactory.createLineBorder(ColorScheme.MEDIUM_GRAY_COLOR));
        arrow.setPreferredSize(new Dimension(26, 26));
        arrow.setMinimumSize(new Dimension(26, 26));
        arrow.setMaximumSize(new Dimension(26, 26));
        header.add(arrow, BorderLayout.EAST);

        rows.setVisible(false);

        Runnable toggle = () ->
        {
            boolean show = !rows.isVisible();
            rows.setVisible(show);
            arrow.setDirection(show ? SwingConstants.SOUTH : SwingConstants.EAST);
            parent.revalidate();
            parent.repaint();
        };

        arrow.addActionListener(e -> toggle.run());
        MouseAdapter click = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                toggle.run();
            }
        };
        header.addMouseListener(click);
        label.addMouseListener(click);

        parent.add(header);
        parent.add(rows);
    }

    private static JPanel objectiveList()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(6, 8, 4, 8));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return panel;
    }

    private static JPanel emptyStateRow(String text)
    {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(2, 0, 2, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel label = new JLabel("<html><i>" + text + "</i></html>");
        label.setForeground(ColorScheme.MEDIUM_GRAY_COLOR);
        row.add(label, BorderLayout.WEST);
        return row;
    }

    private static JPanel objectiveRow(boolean complete, String objective)
    {
        return objectiveRow(complete, objective, null);
    }

    private static JPanel objectiveRow(boolean complete, String objective, String explanation)
    {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        row.setBorder(new EmptyBorder(2, 0, 2, 0));

        // HTML gives the objective a real wrapping width instead of allowing Swing
        // to clip long text at the right edge of the narrow RuneLite sidebar.
        JLabel label = new JLabel(
            "<html><div style='width:185px'>"
                + (complete ? "[x] " : "[ ] ")
                + escapeHtml(objective)
                + "</div></html>");
        label.setForeground(complete
            ? ColorScheme.PROGRESS_COMPLETE_COLOR
            : ColorScheme.LIGHT_GRAY_COLOR);

        if (explanation != null && !explanation.isEmpty())
        {
            row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            row.setToolTipText("Click for activity details");
            label.setToolTipText("Click for activity details");

            MouseAdapter detailsClick = new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    showObjectiveExplanation(row, objective, explanation);
                }
            };
            row.addMouseListener(detailsClick);
            label.addMouseListener(detailsClick);
        }

        row.add(label, BorderLayout.CENTER);
        return row;
    }

    private static void showObjectiveExplanation(Component parent, String objective, String explanation)
    {
        JLabel message = new JLabel(
            "<html><div style='width:280px'>"
                + escapeHtml(explanation)
                + "</div></html>");
        message.setForeground(ColorScheme.LIGHT_GRAY_COLOR);

        JOptionPane.showMessageDialog(
            parent,
            message,
            objective,
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static String escapeHtml(String value)
    {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

    void updateGrandExchangeState(boolean enabled)
    {
        geValue.setText(enabled ? "Enabled" : "Locked");
        geValue.setForeground(enabled ? ColorScheme.PROGRESS_COMPLETE_COLOR : ColorScheme.LIGHT_GRAY_COLOR);
        geToggle.setText(enabled ? "Disable Access" : "Enable Access");
    }

    void updateHomeTeleportState(boolean enabled)
    {
        homeTeleportValue.setText(enabled ? "Enabled" : "Locked");
        homeTeleportValue.setForeground(enabled ? ColorScheme.PROGRESS_COMPLETE_COLOR : ColorScheme.LIGHT_GRAY_COLOR);
        homeTeleportToggle.setText(enabled ? "Disable Access" : "Enable Access");
    }

    private void toggleGrandExchange()
    {
        boolean enable = !plugin.getConfig().unlockGrandExchange();
        plugin.getConfigManager().setConfiguration(
            RewindPlugin.CONFIG_GROUP_KEY,
            "unlockGrandExchange",
            enable);
        updateGrandExchangeState(enable);
    }

    private void toggleHomeTeleport()
    {
        boolean enable = !plugin.getConfig().unlockHomeTeleport();
        plugin.getConfigManager().setConfiguration(
            RewindPlugin.CONFIG_GROUP_KEY,
            "unlockHomeTeleport",
            enable);

        updateHomeTeleportState(enable);
    }

    private void buildTimelineSelector()
    {
        JLabel label = new JLabel("Choose a historical date");
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        timelineSelector.add(label);
        timelineSelector.add(Box.createVerticalStrut(6));

        DefaultComboBoxModel<ReleaseDate> model = new DefaultComboBoxModel<>();
        for (Release release : Release.getRELEASES())
        {
            if (isMeaningfulTimelineRelease(release))
            {
                model.addElement(release.getDate());
            }
        }
        timelineDates.setModel(model);
        timelineDates.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        timelineDates.setAlignmentX(Component.LEFT_ALIGNMENT);
        timelineSelector.add(timelineDates);
        timelineSelector.add(Box.createVerticalStrut(8));

        JPanel actions = new JPanel(new GridLayout(1, 2, 6, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton cancel = new JButton("Cancel");
        cancel.setFocusable(false);
        cancel.addActionListener(e -> timelineSelector.setVisible(false));
        actions.add(cancel);

        JButton apply = new JButton("Apply");
        apply.setFocusable(false);
        apply.addActionListener(e -> applyTimelineSelection());
        actions.add(apply);
        timelineSelector.add(actions);
    }

    /**
     * The player-facing timeline only contains dates where Rewind actually changes
     * the historical game state. Keep the final 10 August 2007 backup as the
     * explicit endpoint even though it does not itself unlock another tracked item.
     *
     * Historical no-op/news entries remain in the release database so their source
     * data is preserved and they can be surfaced again if Rewind later models the
     * activity or mechanic introduced on that date.
     */
    private static boolean isMeaningfulTimelineRelease(Release release)
    {
        if (release.getDate() == ReleaseDate._10_AUGUST_2007
            || release.getDate() == ReleaseDate._28_JULY_2003
            || release.getDate() == ReleaseDate._22_SEPTEMBER_2003
            || release.getDate() == ReleaseDate._18_OCTOBER_2004
            || release.getDate() == ReleaseDate._13_DECEMBER_2004
            || release.getDate() == ReleaseDate._26_JANUARY_2005
            || release.getDate() == ReleaseDate._23_AUGUST_2005
            || release.getDate() == ReleaseDate._16_JANUARY_2006
            || release.getDate() == ReleaseDate._28_MARCH_2006
            || release.getDate() == ReleaseDate._06_JUNE_2006
            || release.getDate() == ReleaseDate._13_JUNE_2006
            || release.getDate() == ReleaseDate._04_JULY_2006
            || release.getDate() == ReleaseDate._17_JULY_2006)
        {
            return true;
        }

        return hasEntries(release.getRegions())
            || hasEntries(release.getSkills())
            || hasEntries(release.getPrayers())
            || hasEntries(release.getQuests())
            || hasEntries(release.getSpells());
    }

    private static boolean hasEntries(java.util.Collection<?> values)
    {
        return values != null && !values.isEmpty();
    }

    private void toggleTimelineSelector()
    {
        boolean show = !timelineSelector.isVisible();
        if (show)
        {
            timelineDates.setSelectedItem(plugin.getConfig().release());
        }
        timelineSelector.setVisible(show);
        revalidate();
        repaint();
    }

    private void applyTimelineSelection()
    {
        ReleaseDate current = plugin.getConfig().release();
        ReleaseDate selected = (ReleaseDate) timelineDates.getSelectedItem();
        if (selected != null && selected != current)
        {
            plugin.getConfigManager().setConfiguration(
                RewindPlugin.CONFIG_GROUP_KEY,
                RewindPlugin.CONFIG_RELEASE_DATE_KEY,
                selected);
        }
        timelineSelector.setVisible(false);
        revalidate();
        repaint();
    }

    /** Kept for the existing config-change call site. */
    public void updateDescription(String ignored)
    {
        refresh();
    }

    private static JLabel sectionTitle(String text)
    {
        JLabel label = new JLabel(text);
        label.setFont(FontManager.getRunescapeSmallFont());
        label.setForeground(ColorScheme.BRAND_ORANGE);
        label.setBorder(new EmptyBorder(0, 0, 4, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static JPanel card()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        panel.setBorder(new EmptyBorder(7, 8, 7, 8));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        return panel;
    }

    private static JPanel row(String name, JLabel value)
    {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        JLabel key = new JLabel(name);
        key.setForeground(Color.WHITE);
        row.add(key, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);
        return row;
    }

    private static JLabel valueLabel()
    {
        JLabel label = new JLabel();
        label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        return label;
    }

    private static String progress(int current, int total)
    {
        return total == 0 ? "—" : current + " / " + total;
    }

    private static String html(String text)
    {
        if (text == null || text.trim().isEmpty())
        {
            return "<html><i>No release notes for this date.</i></html>";
        }
        return "<html>" + text + "</html>";
    }
}
