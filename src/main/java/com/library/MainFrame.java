package com.library;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
class MainFrame extends JFrame {
    private LibraryManager libraryManager;
    private JTabbedPane tabbedPane;
    
    // References to combo boxes for updates
    private JComboBox<String> memberComboBox;
    private JComboBox<String> bookComboBox;
    private JTable borrowedBooksTable;

    public MainFrame() {
        libraryManager = new LibraryManager();

        // Add some sample data
        addSampleData();

        // Apply custom styling
        applyCustomStyling();
        
        // Setup the UI
        setupUI();
    
        // Configure the frame
        setTitle("Library Management System");
        setSize(1024, 768);  // Larger initial size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void applyCustomStyling() {
        // Set custom background color for the frame
        this.getContentPane().setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        
        // Create a custom JTabbedPane UI to improve tab appearance
        UIManager.put("TabbedPane.contentAreaColor", LibraryManagementSystem.SECONDARY_COLOR);
        UIManager.put("TabbedPane.selected", LibraryManagementSystem.SECONDARY_COLOR);
    }

    private void addSampleData() {
        // No need to add sample data anymore as it's loaded from files
        // This method is kept for future use if needed
        
        // Check if data already exists
        if (libraryManager.getAllBooks().isEmpty()) {
            System.out.println("No books found in file, sample data will be loaded automatically");
        }
        
        if (libraryManager.getAllMembers().isEmpty()) {
            System.out.println("No members found in file, sample data will be loaded automatically");
        }
    }

    private void setupUI() {
        // Create a tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LibraryManagementSystem.HEADER_FONT);
        tabbedPane.setBackground(LibraryManagementSystem.PRIMARY_COLOR);
        tabbedPane.setForeground(Color.BLACK);
        
        // Add padding around the tabbed pane
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
    
        // Add tabs
        tabbedPane.addTab("Books Management", createBooksPanel());
        tabbedPane.addTab("Members Management", createMembersPanel());
        tabbedPane.addTab("Borrowing Management", createBorrowingPanel());
        tabbedPane.addTab("Search", createSearchPanel());
        tabbedPane.addTab("QR Generator", createQRGeneratorPanel());
        
        // Add icon indicators to make tabs more visible
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setIconAt(i, createColorIcon(10, 10, LibraryManagementSystem.ACCENT_COLOR));
        }
    
        // Add tab change listener to update components when switching tabs
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            // If Borrowing Management tab is selected
            if (selectedIndex == 2) {
                refreshBorrowingComponents();
            }
        });
        
        // Add the tabbed pane to the frame with padding
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
            }
            
            // Method to refresh borrowing components with updated data
            // Create a custom renderer for combo boxes to better display text
            private class ComboBoxRenderer extends DefaultListCellRenderer {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, 
                        int index, boolean isSelected, boolean cellHasFocus) {
                    
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (c instanceof JLabel && value != null) {
                        JLabel label = (JLabel) c;
                        label.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
                        
                        if (isSelected) {
                            label.setBackground(LibraryManagementSystem.PRIMARY_COLOR);
                            label.setForeground(Color.WHITE);
                        } else {
                            label.setBackground(Color.WHITE);
                            label.setForeground(Color.BLACK);
                        }
                    }
                    return c;
                }
            }
            
            private void refreshBorrowingComponents() {
                if (memberComboBox != null && bookComboBox != null && borrowedBooksTable != null) {
                    // Apply custom renderer for better display
                    memberComboBox.setRenderer(new ComboBoxRenderer());
                    bookComboBox.setRenderer(new ComboBoxRenderer());
                    
            updateMemberComboBox(memberComboBox);
            updateBookComboBox(bookComboBox);
            updateBorrowedBooksTable(borrowedBooksTable);
        }
    }
    
    private Icon createColorIcon(int width, int height, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillRect(x, y, width, height);
            }
            
            @Override
            public int getIconWidth() {
                return width;
            }
            
            @Override
            public int getIconHeight() {
                return height;
            }
        };
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
        // Panel header
        JLabel panelHeader = new JLabel("Book Management", JLabel.CENTER);
        panelHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelHeader.setForeground(LibraryManagementSystem.PRIMARY_COLOR);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(panelHeader, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        
        // Book details panel
        JPanel bookDetailsPanel = new JPanel(new GridLayout(5, 2, 20, 20)); // Increased spacing between elements
        bookDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR, 1),
                "Book Details",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 16)), // Larger title font
            BorderFactory.createEmptyBorder(15, 15, 15, 15))); // More padding inside the panel
        bookDetailsPanel.setBackground(new Color(245, 245, 250));  // Slightly different background from white for contrast
        bookDetailsPanel.setOpaque(true);  // Ensure the panel is opaque
    
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField quantityField = new JTextField();
        
        // Style text fields
        styleTextField(idField);
        styleTextField(titleField);
        styleTextField(authorField);
        styleTextField(categoryField);
        styleTextField(quantityField);
    
        JLabel idLabel = new JLabel("Book ID:");
        JLabel titleLabel = new JLabel("Title:");
        JLabel authorLabel = new JLabel("Author:");
        JLabel categoryLabel = new JLabel("Category:");
        JLabel quantityLabel = new JLabel("Quantity:");
        
        // Style labels
        styleLabel(idLabel);
        styleLabel(titleLabel);
        styleLabel(authorLabel);
        styleLabel(categoryLabel);
        styleLabel(quantityLabel);
    
        bookDetailsPanel.add(idLabel);
        bookDetailsPanel.add(idField);
        bookDetailsPanel.add(titleLabel);
        bookDetailsPanel.add(titleField);
        bookDetailsPanel.add(authorLabel);
        bookDetailsPanel.add(authorField);
        bookDetailsPanel.add(categoryLabel);
        bookDetailsPanel.add(categoryField);
        bookDetailsPanel.add(quantityLabel);
        bookDetailsPanel.add(quantityField);
    
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)),  // Top and bottom border only
            BorderFactory.createEmptyBorder(15, 0, 15, 0)  // Increased padding
        ));
        buttonPanel.setOpaque(true);  // Ensure the panel is opaque
        
        JButton addButton = new JButton("Add Book");
        JButton updateButton = new JButton("Update Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton clearButton = new JButton("Clear Fields");
        
        // Style buttons
        styleButton(addButton, LibraryManagementSystem.SUCCESS_COLOR);
        styleButton(updateButton, LibraryManagementSystem.ACCENT_COLOR);
        styleButton(deleteButton, LibraryManagementSystem.ERROR_COLOR);
        styleButton(clearButton, new Color(100, 100, 100));  // Gray for clear button

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Books table with styled header and alternating row colors
        String[] columns = {"ID", "Title", "Author", "Category", "Available Quantity"};
        Object[][] data = {};
        JTable booksTable = new JTable(data, columns) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                comp.setFont(LibraryManagementSystem.TABLE_FONT);
                
                // Alternating row colors
                if (row % 2 == 0) {
                    comp.setBackground(Color.WHITE);
                } else {
                    comp.setBackground(new Color(240, 240, 250));
                }
                
                if (isCellSelected(row, column)) {
                    comp.setBackground(new Color(185, 209, 234));
                }
                
                return comp;
            }
        };
        
        // Style table
        booksTable.setRowHeight(30);
        booksTable.setIntercellSpacing(new Dimension(10, 5));
        booksTable.setShowGrid(true);
        booksTable.setGridColor(new Color(220, 220, 220));
        booksTable.getTableHeader().setBackground(LibraryManagementSystem.PRIMARY_COLOR);
        booksTable.getTableHeader().setForeground(Color.BLACK);
        booksTable.getTableHeader().setFont(LibraryManagementSystem.LABEL_FONT);
        booksTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        JScrollPane tableScrollPane = new JScrollPane(booksTable);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 0, 0),
            BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR)
        ));
        
        // Use a more structured layout to prevent overlapping
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        topSection.add(bookDetailsPanel, BorderLayout.CENTER);
        topSection.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create a separate panel for the table with fixed height
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Add more space above table
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Use a split panel approach with fixed sizes
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSection, tablePanel);
        splitPane.setDividerLocation(380); // Further increased height for the larger text fields
        splitPane.setEnabled(false); // Disable resizing
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        
        // Add the split pane to content panel
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
        // Add content panel to main panel
        panel.add(contentPanel, BorderLayout.CENTER);

        // Load books data
        updateBooksTable(booksTable);

        // Add action listeners
        addButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String category = categoryField.getText();
                int quantity = Integer.parseInt(quantityField.getText());

                // Validate inputs
                if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Book book = new Book(id, title, author, category, quantity);
                boolean added = libraryManager.addBook(book);

                if (added) {
                    JOptionPane.showMessageDialog(this, "Book added successfully!");
                    clearBookFields(idField, titleField, authorField, categoryField, quantityField);
                    updateBooksTable(booksTable);
                    refreshBorrowingComponents(); // Refresh borrowing components
                } else {
                    JOptionPane.showMessageDialog(this, "Book with this ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String category = categoryField.getText();
                int quantity = Integer.parseInt(quantityField.getText());

                // Validate inputs
                if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Book book = new Book(id, title, author, category, quantity);
                boolean updated = libraryManager.updateBook(book);

                if (updated) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully!");
                    clearBookFields(idField, titleField, authorField, categoryField, quantityField);
                    updateBooksTable(booksTable);
                    refreshBorrowingComponents(); // Refresh borrowing components
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String id = idField.getText();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a book ID to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean deleted = libraryManager.removeBook(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                clearBookFields(idField, titleField, authorField, categoryField, quantityField);
                updateBooksTable(booksTable);
                refreshBorrowingComponents(); // Refresh borrowing components
            } else {
                JOptionPane.showMessageDialog(this, "Book not found or is currently borrowed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> {
            clearBookFields(idField, titleField, authorField, categoryField, quantityField);
        });

        // Table selection listener
        booksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && booksTable.getSelectedRow() != -1) {
                int row = booksTable.getSelectedRow();
                idField.setText(booksTable.getValueAt(row, 0).toString());
                titleField.setText(booksTable.getValueAt(row, 1).toString());
                authorField.setText(booksTable.getValueAt(row, 2).toString());
                categoryField.setText(booksTable.getValueAt(row, 3).toString());
                quantityField.setText(booksTable.getValueAt(row, 4).toString());
            }
        });

        return panel;
    }

    private void clearBookFields(JTextField idField, JTextField titleField, JTextField authorField, JTextField categoryField, JTextField quantityField) {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        quantityField.setText("");
    }

    private void updateBooksTable(JTable table) {
        ArrayList<Book> books = libraryManager.getAllBooks();
        Object[][] data = new Object[books.size()][5];

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getId();
            data[i][1] = book.getTitle();
            data[i][2] = book.getAuthor();
            data[i][3] = book.getCategory();
            data[i][4] = book.getAvailableQuantity();
        }

        table.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[] {"ID", "Title", "Author", "Category", "Available Quantity"}
        ));
    }

    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Member details panel
        JPanel memberDetailsPanel = new JPanel(new GridLayout(4, 2, 20, 20)); // Increased spacing
        memberDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR, 1),
                "Member Details",
                TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 16)), // Larger title font
            BorderFactory.createEmptyBorder(15, 15, 15, 15))); // More padding
        memberDetailsPanel.setBackground(new Color(245, 245, 250));
        memberDetailsPanel.setOpaque(true);
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        
        // Style text fields
        styleTextField(idField);
        styleTextField(nameField);
        styleTextField(phoneField);
        styleTextField(emailField);
        
        JLabel idLabel = new JLabel("Member ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel emailLabel = new JLabel("Email:");
        
        // Style labels
        styleLabel(idLabel);
        styleLabel(nameLabel);
        styleLabel(phoneLabel);
        styleLabel(emailLabel);
        
        memberDetailsPanel.add(idLabel);
        memberDetailsPanel.add(idField);
        memberDetailsPanel.add(nameLabel);
        memberDetailsPanel.add(nameField);
        memberDetailsPanel.add(phoneLabel);
        memberDetailsPanel.add(phoneField);
        memberDetailsPanel.add(emailLabel);
        memberDetailsPanel.add(emailField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Member");
        JButton updateButton = new JButton("Update Member");
        JButton deleteButton = new JButton("Delete Member");
        JButton clearButton = new JButton("Clear Fields");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Members table
        String[] columns = {"ID", "Name", "Phone", "Email", "Books Borrowed"};
        Object[][] data = {};
        JTable membersTable = new JTable(data, columns);
        JScrollPane tableScrollPane = new JScrollPane(membersTable);

        // Use the improved layout structure to prevent overlapping
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        topSection.add(memberDetailsPanel, BorderLayout.CENTER);
        topSection.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create a separate panel for the table with fixed height
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Add more space above table
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Use a split panel approach with fixed sizes
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSection, tablePanel);
        splitPane.setDividerLocation(330); // Increased height for larger text fields
        splitPane.setEnabled(false); // Disable resizing
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        
        // Add the split pane to panel
        panel.add(splitPane, BorderLayout.CENTER);

        // Load members data
        updateMembersTable(membersTable);

        // Add action listeners
        addButton.addActionListener(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            // Validate inputs
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Member member = new Member(id, name, phone, email);
            boolean added = libraryManager.addMember(member);

            if (added) {
                JOptionPane.showMessageDialog(this, "Member added successfully!");
                clearMemberFields(idField, nameField, phoneField, emailField);
                updateMembersTable(membersTable);
                refreshBorrowingComponents(); // Refresh borrowing components
            } else {
                JOptionPane.showMessageDialog(this, "Member with this ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            // Validate inputs
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Member member = new Member(id, name, phone, email);
            boolean updated = libraryManager.updateMember(member);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Member updated successfully!");
                clearMemberFields(idField, nameField, phoneField, emailField);
                updateMembersTable(membersTable);
                refreshBorrowingComponents(); // Refresh borrowing components
            } else {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String id = idField.getText();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a member ID to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean deleted = libraryManager.removeMember(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                clearMemberFields(idField, nameField, phoneField, emailField);
                updateMembersTable(membersTable);
                refreshBorrowingComponents(); // Refresh borrowing components
            } else {
                JOptionPane.showMessageDialog(this, "Member not found or has borrowed books!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> {
            clearMemberFields(idField, nameField, phoneField, emailField);
        });

        // Table selection listener
        membersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && membersTable.getSelectedRow() != -1) {
                int row = membersTable.getSelectedRow();
                idField.setText(membersTable.getValueAt(row, 0).toString());
                nameField.setText(membersTable.getValueAt(row, 1).toString());
                phoneField.setText(membersTable.getValueAt(row, 2).toString());
                emailField.setText(membersTable.getValueAt(row, 3).toString());
            }
        });

        return panel;
    }

    private void clearMemberFields(JTextField idField, JTextField nameField, JTextField phoneField, JTextField emailField) {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    private void updateMembersTable(JTable table) {
        ArrayList<Member> members = libraryManager.getAllMembers();
        Object[][] data = new Object[members.size()][5];

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            data[i][0] = member.getId();
            data[i][1] = member.getName();
            data[i][2] = member.getPhone();
            data[i][3] = member.getEmail();
            data[i][4] = member.getBorrowedBooks().size();
        }

        table.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[] {"ID", "Name", "Phone", "Email", "Books Borrowed"}
        ));
    }

    private JPanel createBorrowingPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel header - smaller
        JLabel panelHeader = new JLabel("Borrowing Management", JLabel.CENTER);
        panelHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelHeader.setForeground(LibraryManagementSystem.PRIMARY_COLOR);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(panelHeader, BorderLayout.NORTH);
    
        // Instead of GridLayout, use a more flexible layout for the borrowing details panel
        JPanel borrowingDetailsPanel = new JPanel();
        borrowingDetailsPanel.setLayout(new BoxLayout(borrowingDetailsPanel, BoxLayout.Y_AXIS));
        borrowingDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR, 1),
                "Borrowing Details",
                TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 14)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        borrowingDetailsPanel.setBackground(new Color(245, 245, 250));
        borrowingDetailsPanel.setOpaque(true);
    
        // Create and store references as class members for later updates
        memberComboBox = new JComboBox<>();
        bookComboBox = new JComboBox<>();
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 30, 1));
        
        // Create separate panels for each row to get more control over layout
        JPanel memberPanel = new JPanel(new BorderLayout(10, 5));
        JPanel bookPanel = new JPanel(new BorderLayout(10, 5));
        JPanel daysPanel = new JPanel(new BorderLayout(10, 5));
        
        memberPanel.setBackground(new Color(245, 245, 250));
        bookPanel.setBackground(new Color(245, 245, 250));
        daysPanel.setBackground(new Color(245, 245, 250));
        
        // Style combo boxes with increased height
        memberComboBox.setFont(LibraryManagementSystem.FIELD_FONT);
        bookComboBox.setFont(LibraryManagementSystem.FIELD_FONT);
        daysSpinner.setFont(LibraryManagementSystem.FIELD_FONT);
        
        // Set preferred size for comboboxes - smaller than before
        memberComboBox.setPreferredSize(new Dimension(400, 35));
        bookComboBox.setPreferredSize(new Dimension(400, 35));
        daysSpinner.setPreferredSize(new Dimension(400, 35));
    
        JLabel memberLabel = new JLabel("Member:");
        JLabel bookLabel = new JLabel("Book:");
        JLabel daysLabel = new JLabel("Borrow Days:");
        
        // Style labels
        styleLabel(memberLabel);
        styleLabel(bookLabel);
        styleLabel(daysLabel);
        
        // Add components to their respective panels
        memberPanel.add(memberLabel, BorderLayout.WEST);
        memberPanel.add(memberComboBox, BorderLayout.CENTER);
        
        bookPanel.add(bookLabel, BorderLayout.WEST);
        bookPanel.add(bookComboBox, BorderLayout.CENTER);
        
        daysPanel.add(daysLabel, BorderLayout.WEST);
        daysPanel.add(daysSpinner, BorderLayout.CENTER);
        
        // Reduced spacing between rows
        borrowingDetailsPanel.add(memberPanel);
        borrowingDetailsPanel.add(Box.createVerticalStrut(10));
        borrowingDetailsPanel.add(bookPanel);
        borrowingDetailsPanel.add(Box.createVerticalStrut(10));
        borrowingDetailsPanel.add(daysPanel);
    
        // Update combo boxes
        updateMemberComboBox(memberComboBox);
        updateBookComboBox(bookComboBox);
    
        // Button panel - more compact
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 0, 8, 0)
        ));
        
        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");
        
        // Style buttons
        styleButton(borrowButton, LibraryManagementSystem.SUCCESS_COLOR);
        styleButton(returnButton, LibraryManagementSystem.ACCENT_COLOR);

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);

        // Borrowed books table
        String[] columns = {"Member ID", "Member Name", "Book ID", "Book Title", "Borrow Date", "Due Date"};
        Object[][] data = {};
        borrowedBooksTable = new JTable(data, columns); // Store reference as class variable
        // Style table
        borrowedBooksTable.setRowHeight(30);
        borrowedBooksTable.setIntercellSpacing(new Dimension(10, 5));
        borrowedBooksTable.setShowGrid(true);
        borrowedBooksTable.setGridColor(new Color(220, 220, 220));
        borrowedBooksTable.getTableHeader().setBackground(LibraryManagementSystem.PRIMARY_COLOR);
        borrowedBooksTable.getTableHeader().setForeground(Color.BLACK);
        borrowedBooksTable.getTableHeader().setFont(LibraryManagementSystem.LABEL_FONT);
        borrowedBooksTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        JScrollPane tableScrollPane = new JScrollPane(borrowedBooksTable);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 0, 0),
            BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR)
        ));

        // Use the improved layout structure to prevent overlapping
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        topSection.add(borrowingDetailsPanel, BorderLayout.CENTER);
        topSection.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create a separate panel for the table with fixed height
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Add more space above table
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Use a split panel approach with fixed sizes
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSection, tablePanel);
        splitPane.setDividerLocation(220); // Reduced height for a more compact view
        splitPane.setEnabled(false); // Disable resizing
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        
        // Add the split pane to panel
        panel.add(splitPane, BorderLayout.CENTER);

        // Load borrowed books data
        updateBorrowedBooksTable(borrowedBooksTable);

        // Add action listeners
        borrowButton.addActionListener(e -> {
            if (memberComboBox.getSelectedItem() == null || bookComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select both member and book!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String memberId = ((String) memberComboBox.getSelectedItem()).split(" - ")[0];
            String bookId = ((String) bookComboBox.getSelectedItem()).split(" - ")[0];
            int days = (int) daysSpinner.getValue();

            boolean borrowed = libraryManager.borrowBook(memberId, bookId, days);

            if (borrowed) {
                JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
                // Refresh all components in the borrowing panel
                refreshBorrowingComponents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to borrow book. Check if the book is available.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> {
            int selectedRow = borrowedBooksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a borrowed book to return!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String memberId = borrowedBooksTable.getValueAt(selectedRow, 0).toString();
            String bookId = borrowedBooksTable.getValueAt(selectedRow, 2).toString();

            boolean returned = libraryManager.returnBook(memberId, bookId);

            if (returned) {
                JOptionPane.showMessageDialog(this, "Book returned successfully!");
                // Refresh all components in the borrowing panel
                refreshBorrowingComponents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void updateMemberComboBox(JComboBox<String> comboBox) {
        if (comboBox == null) return;
        
        // Remember selected item if there was one
        String selectedItem = comboBox.getSelectedItem() != null ? 
                              comboBox.getSelectedItem().toString() : null;
                              
        comboBox.removeAllItems();
    
        for (Member member : libraryManager.getAllMembers()) {
            // Limit the length of the display name to prevent overflow
            String name = member.getName();
            if (name.length() > 30) {
                name = name.substring(0, 27) + "...";
            }
            String item = member.getId() + " - " + name;
            comboBox.addItem(item);
            
            // Restore selection if possible
            if (selectedItem != null && item.startsWith(member.getId())) {
                comboBox.setSelectedItem(item);
            }
        }
        
        // Force UI refresh
        comboBox.revalidate();
        comboBox.repaint();
        
        // Set prototype display value to help with sizing
        comboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    }
    
    private void updateBookComboBox(JComboBox<String> comboBox) {
        if (comboBox == null) return;
        
        // Remember selected item if there was one
        String selectedItem = comboBox.getSelectedItem() != null ? 
                              comboBox.getSelectedItem().toString() : null;
                              
        comboBox.removeAllItems();
    
        for (Book book : libraryManager.getAvailableBooks()) {
            // Limit the length of the title to prevent overflow
            String title = book.getTitle();
            if (title.length() > 30) {
                title = title.substring(0, 27) + "...";
            }
            String item = book.getId() + " - " + title;
            comboBox.addItem(item);
            
            // Restore selection if possible
            if (selectedItem != null && item.startsWith(book.getId())) {
                comboBox.setSelectedItem(item);
            }
        }
        
        // Force UI refresh
        comboBox.revalidate();
        comboBox.repaint();
        
        // Set prototype display value to help with sizing
        comboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    }

    private void updateBorrowedBooksTable(JTable table) {
        ArrayList<BorrowRecord> records = libraryManager.getAllBorrowRecords();
        Object[][] data = new Object[records.size()][6];

        for (int i = 0; i < records.size(); i++) {
            BorrowRecord record = records.get(i);
            Member member = libraryManager.getMember(record.getMemberId());
            Book book = libraryManager.getBook(record.getBookId());

            data[i][0] = record.getMemberId();
            data[i][1] = member != null ? member.getName() : "Unknown";
            data[i][2] = record.getBookId();
            data[i][3] = book != null ? book.getTitle() : "Unknown";
            data[i][4] = new SimpleDateFormat("yyyy-MM-dd").format(record.getBorrowDate());
            data[i][5] = new SimpleDateFormat("yyyy-MM-dd").format(record.getDueDate());
        }

        table.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[] {"Member ID", "Member Name", "Book ID", "Book Title", "Borrow Date", "Due Date"}
        ));
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
        // Panel header
        JLabel panelHeader = new JLabel("Search Books and Members", JLabel.CENTER);
        panelHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelHeader.setForeground(LibraryManagementSystem.PRIMARY_COLOR);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(panelHeader, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(LibraryManagementSystem.SECONDARY_COLOR);
    
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
            
        JTextField searchField = new JTextField(30);
        styleTextField(searchField);
        
        JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[] {"Book", "Member"});
        searchTypeComboBox.setFont(LibraryManagementSystem.FIELD_FONT);
        searchTypeComboBox.setPreferredSize(new Dimension(150, 30));
        
        JButton searchButton = new JButton("Search");
        styleButton(searchButton, LibraryManagementSystem.ACCENT_COLOR);
    
        JLabel searchLabel = new JLabel("Search:");
        styleLabel(searchLabel);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchButton);
    
        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(LibraryManagementSystem.FIELD_FONT);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultsArea.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Add components to the content panel
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(resultsPanel, BorderLayout.CENTER);
        
        // Add content panel to main panel
        panel.add(contentPanel, BorderLayout.CENTER);

        // Add action listener for search button
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            String searchType = (String) searchTypeComboBox.getSelectedItem();

            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            resultsArea.setText("");

            if (searchType.equals("Book")) {
                ArrayList<Book> results = libraryManager.searchBooks(searchTerm);

                if (results.isEmpty()) {
                    resultsArea.append("No books found matching the search term.\n");
                } else {
                    resultsArea.append("Found " + results.size() + " book(s):\n\n");

                    for (Book book : results) {
                        resultsArea.append("ID: " + book.getId() + "\n");
                        resultsArea.append("Title: " + book.getTitle() + "\n");
                        resultsArea.append("Author: " + book.getAuthor() + "\n");
                        resultsArea.append("Category: " + book.getCategory() + "\n");
                        resultsArea.append("Available Quantity: " + book.getAvailableQuantity() + "\n");
                        resultsArea.append("------------------------\n");
                    }
                }
            } else if (searchType.equals("Member")) {
                ArrayList<Member> results = libraryManager.searchMembers(searchTerm);

                if (results.isEmpty()) {
                    resultsArea.append("No members found matching the search term.\n");
                } else {
                    resultsArea.append("Found " + results.size() + " member(s):\n\n");

                    for (Member member : results) {
                        resultsArea.append("ID: " + member.getId() + "\n");
                        resultsArea.append("Name: " + member.getName() + "\n");
                        resultsArea.append("Phone: " + member.getPhone() + "\n");
                        resultsArea.append("Email: " + member.getEmail() + "\n");
                        resultsArea.append("Books Borrowed: " + member.getBorrowedBooks().size() + "\n");
                        resultsArea.append("------------------------\n");
                    }
                }
            }
        });

        return panel;
    }

    private JPanel createQRGeneratorPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // QR Generator panel
        JPanel qrGeneratorPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        qrGeneratorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> itemTypeComboBox = new JComboBox<>(new String[] {"Book", "Member"});
        JComboBox<String> itemComboBox = new JComboBox<>();
        JButton generateButton = new JButton("Generate QR Code");

        qrGeneratorPanel.add(new JLabel("Item Type:"));
        qrGeneratorPanel.add(itemTypeComboBox);
        qrGeneratorPanel.add(new JLabel("Select Item:"));
        qrGeneratorPanel.add(itemComboBox);
        qrGeneratorPanel.add(new JLabel(""));
        qrGeneratorPanel.add(generateButton);

        // QR display panel
        JPanel qrDisplayPanel = new JPanel(new BorderLayout());
        JLabel qrImageLabel = new JLabel("QR code will be displayed here", SwingConstants.CENTER);
        qrDisplayPanel.add(qrImageLabel, BorderLayout.CENTER);

        // Add components to the panel
        panel.add(qrGeneratorPanel, BorderLayout.NORTH);
        panel.add(qrDisplayPanel, BorderLayout.CENTER);

        // Update item combo box based on selected type
        updateItemComboBox(itemComboBox, (String) itemTypeComboBox.getSelectedItem());

        // Add action listeners
        itemTypeComboBox.addActionListener(e -> {
            updateItemComboBox(itemComboBox, (String) itemTypeComboBox.getSelectedItem());
        });

        generateButton.addActionListener(e -> {
            if (itemComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(panel, "Please select an item!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String itemType = (String) itemTypeComboBox.getSelectedItem();
            String itemId = ((String) itemComboBox.getSelectedItem()).split(" - ")[0];

            // In a real application, this would generate a QR code
            // For this demo, we'll just display a text representation
            qrImageLabel.setText("QR Code for " + itemType + " ID: " + itemId);
            qrImageLabel.setIcon(createDummyQRCode());

            JOptionPane.showMessageDialog(panel, "QR Code generated successfully!\n" +
                            "In a real application, this would generate an actual QR code image.",
                    "QR Generated", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    private void updateItemComboBox(JComboBox<String> comboBox, String itemType) {
        comboBox.removeAllItems();

        if (itemType.equals("Book")) {
            for (Book book : libraryManager.getAllBooks()) {
                comboBox.addItem(book.getId() + " - " + book.getTitle());
            }
        } else if (itemType.equals("Member")) {
            for (Member member : libraryManager.getAllMembers()) {
                comboBox.addItem(member.getId() + " - " + member.getName());
            }
        }
    }

    private ImageIcon createDummyQRCode() {
        // Create a simple representation of a QR code
        int size = 200;  // Larger QR code for better visibility
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
                size, size, java.awt.image.BufferedImage.TYPE_INT_RGB);
    
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);
        g.setColor(Color.BLACK);
    
        // Draw a simple pattern
        int blockSize = 20;  // Larger blocks for better visibility
        for (int i = 0; i < size / blockSize; i++) {
            for (int j = 0; j < size / blockSize; j++) {
                if ((i + j) % 2 == 0 || i == 0 || j == 0 || i == size / blockSize - 1 || j == size / blockSize - 1) {
                    g.fillRect(i * blockSize, j * blockSize, blockSize, blockSize);
                }
            }
        }
    
        g.dispose();
        return new ImageIcon(image);
    }
    
    // Helper methods for consistent UI styling
    
    private void styleTextField(JTextField textField) {
        textField.setFont(LibraryManagementSystem.FIELD_FONT);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 45)); // Increased height
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10) // More internal padding
        ));
        // Ensure text field has appropriate colors for visibility and editing
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setCaretColor(Color.BLACK);
        textField.setEditable(true);
        textField.setEnabled(true);
        // Set the row height to accommodate larger text
        textField.setMargin(new Insets(5, 10, 5, 10));
        
        // Add focus highlighting for better user experience
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(LibraryManagementSystem.PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });
    }
    
    private void styleLabel(JLabel label) {
        label.setFont(LibraryManagementSystem.LABEL_FONT);
        label.setForeground(LibraryManagementSystem.PRIMARY_COLOR);
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(LibraryManagementSystem.BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(lightenColor(bgColor, 0.2f));
            }
    
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private Color lightenColor(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + 255 * factor));
        int g = Math.min(255, (int)(color.getGreen() + 255 * factor));
        int b = Math.min(255, (int)(color.getBlue() + 255 * factor));
        return new Color(r, g, b);
    }
}