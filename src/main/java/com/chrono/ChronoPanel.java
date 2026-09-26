package com.chrono;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class ChronoPanel extends PluginPanel implements ActionListener {
    private ChronoPlugin plugin;

    private JButton myButton;
    private JLabel description;
    private JLabel title;

    public ChronoPanel(ChronoPlugin plugin) {
        this.plugin = plugin;

        // Set layout to GridBagLayout
        setLayout(new GridBagLayout());

        // Create GridBagConstraints object to set component constraints
        GridBagConstraints gbc = new GridBagConstraints();

        // Set title constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0); // add padding
        title = new JLabel("Chrono");
        title.setFont(FontManager.getRunescapeBoldFont());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, gbc);

        // Set label and button constraints for second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 5, 3); // add padding
        myButton = new JButton("Copy Unlocked Region IDs");
        myButton.addActionListener(this);
        add(myButton, gbc);

        // Set label and button constraints for second row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 5, 3); // add padding
        description = new JLabel();
        description.setText(plugin.getCurrentRelease().getDescription());
        add(description, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String regions = plugin.getCurrentRelease().getRegions().toString().replace("[", "").replace("]", "");
        StringSelection stringSelection = new StringSelection(regions);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public void updateDescription(String desc) {
        String text = "<html>"+desc+"</html>";
        description.setText(text);
    }
}