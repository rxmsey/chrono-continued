package com.rewind;

import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
    private final JPanel completionDetails = card();
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
        JButton viewCompletion = new JButton("View Completion Log");
        viewCompletion.setFocusable(false);
        viewCompletion.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewCompletion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        viewCompletion.addActionListener(e -> toggleCompletionDetails());
        completionCard.add(Box.createVerticalStrut(8));
        completionCard.add(viewCompletion);
        content.add(completionCard);
        content.add(Box.createVerticalStrut(6));
        completionDetails.setVisible(false);
        content.add(completionDetails);
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
        if (completionDetails.isVisible()) rebuildCompletionDetails();
        boolean geUnlocked = plugin.getConfig().unlockGrandExchange();
        geValue.setText(geUnlocked ? "Enabled" : "Locked");
        geToggle.setText(geUnlocked ? "Disable Access" : "Enable Access");
        boolean homeTeleportUnlocked = plugin.getConfig().unlockHomeTeleport();
        homeTeleportValue.setText(homeTeleportUnlocked ? "Enabled" : "Locked");
        homeTeleportToggle.setText(homeTeleportUnlocked ? "Disable Access" : "Enable Access");
    }

    private void toggleCompletionDetails()
    {
        boolean show = !completionDetails.isVisible();
        if (show) rebuildCompletionDetails();
        completionDetails.setVisible(show);
        revalidate();
        repaint();
    }

    private void rebuildCompletionDetails()
    {
        completionDetails.removeAll();

        JLabel questHeader = sectionTitle("QUESTS");
        completionDetails.add(questHeader);
        for (Quest quest : plugin.getAvailableCompletionQuests())
        {
            boolean done = plugin.isQuestComplete(quest);
            completionDetails.add(objectiveRow(done, quest.getName()));
        }

        completionDetails.add(Box.createVerticalStrut(8));
        completionDetails.add(sectionTitle("SKILL MILESTONES"));
        final int[] milestones = RewindPlugin.completionMilestones();
        for (Map.Entry<Skill, Integer> entry : plugin.getAvailableCompletionSkills().entrySet())
        {
            for (int milestone : milestones)
            {
                completionDetails.add(objectiveRow(
                    entry.getValue() >= milestone,
                    entry.getKey().getName() + " " + milestone));
            }
        }

        completionDetails.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        completionDetails.revalidate();
        completionDetails.repaint();
    }

    private static JPanel objectiveRow(boolean complete, String objective)
    {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel label = new JLabel((complete ? "\u2713 " : "\u25CB ") + objective);
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
