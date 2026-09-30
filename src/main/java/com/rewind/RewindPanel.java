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
        subtitle.setBorder(new EmptyBorder(3, 0, 12, 0));
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

        content.add(sectionTitle("TIMELINE PROGRESS"));
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
        Release finalRelease = Release.getReleaseByDate(ReleaseDate._10_AUGUST_2007);
        regionsValue.setText(progress(Release.getRegions(release).size(), Release.getRegions(finalRelease).size()));
        questsValue.setText(progress(Release.getQuests(release).size(), Release.getQuests(finalRelease).size()));
        skillsValue.setText(progress(Release.getSkills(release).size(), Release.getSkills(finalRelease).size()));
        prayersValue.setText(progress(Release.getPrayers(release).size(), Release.getPrayers(finalRelease).size()));
        spellsValue.setText(progress(Release.getSpells(release).size(), Release.getSpells(finalRelease).size()));
        CompletionProgress completion = plugin.getCompletionProgress();
        completionValue.setText(completion.getAvailable() == 0 ? "—" : completion.getPercentage() + "%");
        completionQuestsValue.setText(progress(completion.getCompletedQuests(), completion.getAvailableQuests()));
        completionSkillsValue.setText(progress(completion.getCompletedSkillMilestones(), completion.getAvailableSkillMilestones()));
        completionActivitiesValue.setText(progress(completion.getCompletedActivities(), completion.getAvailableActivities()));
        refreshEraProgress();
        if (completionDetails.isVisible() && plugin.isClientStateReadable()) rebuildCompletionDetails();
        boolean geUnlocked = plugin.getConfig().unlockGrandExchange();
        geValue.setText(geUnlocked ? "Enabled" : "Locked");
        geToggle.setText(geUnlocked ? "Disable Access" : "Enable Access");
        boolean homeTeleportUnlocked = plugin.getConfig().unlockHomeTeleport();
        homeTeleportValue.setText(homeTeleportUnlocked ? "Enabled" : "Locked");
        homeTeleportToggle.setText(homeTeleportUnlocked ? "Disable Access" : "Enable Access");
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
                continue;
            }

            CompletionProgress era = plugin.getEraProgress(year);
            value.setText(era.getAvailable() == 0 ? "—" : era.getPercentage() + "%");
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
        for (Quest quest : plugin.getAvailableCompletionQuests())
        {
            boolean done = plugin.isQuestComplete(quest);
            questRows.add(objectiveRow(done, quest.getName()));
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
        if (plugin.isBarrowsAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isBarrowsComplete(),
                "Loot the Barrows reward chest"));
            hasActivity = true;
        }

        if (plugin.isFightCavesAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isFightCavesComplete(),
                "Complete the TzHaar Fight Cave"));
            hasActivity = true;
        }

        if (plugin.isBonesToPeachesAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isBonesToPeachesComplete(),
                "Unlock Bones to Peaches"));
            hasActivity = true;
        }

        if (plugin.isVoidSetAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isVoidSetComplete(),
                "Own Void top, robe, gloves + any Void helm"));
            hasActivity = true;
        }

        if (plugin.isRuneDefenderAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isRuneDefenderComplete(),
                "Own a Rune defender"));
            hasActivity = true;
        }

        if (plugin.isFighterTorsoAvailable(plugin.getCurrentRelease()))
        {
            activityRows.add(objectiveRow(
                plugin.isFighterTorsoComplete(),
                "Own a Fighter torso"));
            hasActivity = true;
        }

        if (!hasActivity)
        {
            activityRows.add(objectiveRow(false, "No activities for this milestone"));
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

    private static JPanel objectiveRow(boolean complete, String objective)
    {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        // RuneLite's bundled RuneScape font does not reliably contain the Unicode
        // checkmark/circle glyphs; use ASCII markers to avoid green square glyphs.
        JLabel label = new JLabel((complete ? "[x] " : "[ ] ") + objective);
        label.setForeground(complete ? ColorScheme.PROGRESS_COMPLETE_COLOR : ColorScheme.LIGHT_GRAY_COLOR);
        row.add(label, BorderLayout.WEST);
        return row;
    }

    private void toggleGrandExchange()
    {
        boolean enable = !plugin.getConfig().unlockGrandExchange();
        plugin.getConfigManager().setConfiguration(
            RewindPlugin.CONFIG_GROUP_KEY,
            "unlockGrandExchange",
            enable);
    }

    private void toggleHomeTeleport()
    {
        boolean enable = !plugin.getConfig().unlockHomeTeleport();
        plugin.getConfigManager().setConfiguration(
            RewindPlugin.CONFIG_GROUP_KEY,
            "unlockHomeTeleport",
            enable);

        // Reflect the requested state immediately. ConfigChanged can fire before the
        // config proxy has refreshed, which previously made this row always appear
        // as Enabled / Disable Access even after turning it off.
        homeTeleportValue.setText(enable ? "Enabled" : "Locked");
        homeTeleportToggle.setText(enable ? "Disable Access" : "Enable Access");
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
        if (release.getDate() == ReleaseDate._10_AUGUST_2007)
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
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
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
        return current + " / " + total;
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
