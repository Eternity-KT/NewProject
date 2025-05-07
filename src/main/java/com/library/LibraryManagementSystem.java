package com.library;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class LibraryManagementSystem {
    // Define common colors and fonts
    public static final Color PRIMARY_COLOR = new Color(51, 102, 153);  // Dark blue
    public static final Color SECONDARY_COLOR = new Color(240, 248, 255);  // Alice blue
    public static final Color ACCENT_COLOR = new Color(70, 130, 180);  // Steel blue
    public static final Color ERROR_COLOR = new Color(178, 34, 34);  // Firebrick
    public static final Color SUCCESS_COLOR = new Color(46, 139, 87);  // Sea green
    
    public static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 16);
    public static final Font TABLE_FONT = new Font("SansSerif", Font.PLAIN, 15);
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Set global UI properties
                UIManager.put("Label.font", LABEL_FONT);
                UIManager.put("TextField.font", FIELD_FONT);
                UIManager.put("TextArea.font", FIELD_FONT);
                UIManager.put("Button.font", BUTTON_FONT);
                UIManager.put("ComboBox.font", FIELD_FONT);
                UIManager.put("TabbedPane.font", HEADER_FONT);
                UIManager.put("Table.font", TABLE_FONT);
                UIManager.put("TableHeader.font", LABEL_FONT);
                
                // Set some color properties
                UIManager.put("TabbedPane.selected", SECONDARY_COLOR);
                UIManager.put("TabbedPane.background", PRIMARY_COLOR);
                UIManager.put("TabbedPane.foreground", Color.BLACK);
                UIManager.put("TabbedPane.selectedForeground", PRIMARY_COLOR);
                UIManager.put("Button.background", ACCENT_COLOR);
                UIManager.put("Button.foreground", Color.BLACK);
                
            } catch (Exception e) {
                e.printStackTrace();
            }

            new MainFrame();
        });
    }
}
