import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.io.*;

public class ArtGalleryGUI extends JFrame {
    private JTextField visitorIDField, fullNameField, contactNumberField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<String> dayBox, monthBox, yearBox, ticketTypeBox;
    private JTextField ticketPriceField;

    private JTextField purchaseVisitorIDField, artworkNameField, artworkPriceField;

    private ArrayList<ArtGalleryVisitor> visitors; // List to store visitors
    private JTextArea outputArea; // To display results

    public ArtGalleryGUI() {
        setTitle("Art Gallery System");
        setSize(500, 600); // Smaller window
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        visitors = new ArrayList<>(); // Initialize visitor list

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Art Gallery System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Add Visitors Section
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Visitors"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        visitorIDField = new JTextField(12);
        fullNameField = new JTextField(12);
        contactNumberField = new JTextField(12);
        ticketPriceField = new JTextField(12);
        ticketPriceField.setEditable(false); // Ticket price is set based on ticket type

        addFormRow(formPanel, gbc, 0, "Visitor ID", visitorIDField);
        addFormRow(formPanel, gbc, 1, "Full Name", fullNameField);

        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        addFormRow(formPanel, gbc, 2, "Gender", genderPanel);

        String[] days = new String[31];
        for (int i = 0; i < 31; i++) days[i] = Integer.toString(i + 1);
        dayBox = new JComboBox<>(days);
        monthBox = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        yearBox = new JComboBox<>(new String[]{"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025"});

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(dayBox);
        datePanel.add(monthBox);
        datePanel.add(yearBox);
        addFormRow(formPanel, gbc, 3, "Registration Date", datePanel);

        addFormRow(formPanel, gbc, 4, "Contact Number", contactNumberField);

        ticketTypeBox = new JComboBox<>(new String[]{"Select", "Standard", "Elite"});
        ticketTypeBox.addActionListener(e -> updateTicketPrice());
        addFormRow(formPanel, gbc, 5, "Ticket Type", ticketTypeBox);
        addFormRow(formPanel, gbc, 6, "Ticket Price", ticketPriceField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addButton = new JButton("Add");
        JButton clearButton = new JButton("Clear");
        JButton displayButton = new JButton("Display");
        JButton readButton = new JButton("Read");
        JButton saveButton = new JButton("Save");

        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(readButton);
        buttonPanel.add(saveButton);

        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);

        // Purchase Section
        JPanel purchasePanel = new JPanel(new GridBagLayout());
        purchasePanel.setBorder(BorderFactory.createTitledBorder("Purchase Section"));

        purchaseVisitorIDField = new JTextField(12);
        artworkNameField = new JTextField(12);
        artworkPriceField = new JTextField(12);

        addFormRow(purchasePanel, gbc, 0, "Visitor ID", purchaseVisitorIDField);
        addFormRow(purchasePanel, gbc, 1, "Artwork Name", artworkNameField);
        addFormRow(purchasePanel, gbc, 2, "Artwork Price", artworkPriceField);

        JPanel purchaseButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
        JButton buyButton = new JButton("Buy");
        JButton discountButton = new JButton("Discount");
        JButton rewardButton = new JButton("Reward");
        JButton generateBillButton = new JButton("Generate Bill");
        JButton cancelButton = new JButton("Cancel");

        purchaseButtons.add(buyButton);
        purchaseButtons.add(discountButton);
        purchaseButtons.add(rewardButton);
        purchaseButtons.add(generateBillButton);
        purchaseButtons.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        purchasePanel.add(purchaseButtons, gbc);

        mainPanel.add(purchasePanel);

        // Manage Visitors Section
        JPanel managePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        managePanel.setBorder(BorderFactory.createTitledBorder("Manage Visitors"));
        JButton logVisitorButton = new JButton("Log Visitor");
        JButton assignAdvisorButton = new JButton("Assign Advisor");
        JButton checkDiscountButton = new JButton("Check Discount Eligibility");

        managePanel.add(logVisitorButton);
        managePanel.add(assignAdvisorButton);
        managePanel.add(checkDiscountButton);

        mainPanel.add(managePanel);

        // Output Area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        mainPanel.add(new JScrollPane(outputArea));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);

        // Action Listeners
        addButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(visitorIDField.getText().trim());
                        String fullName = fullNameField.getText().trim();
                        String gender = maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "";
                        String contactNumber = contactNumberField.getText().trim();
                        String registrationDate = dayBox.getSelectedItem() + "-" + monthBox.getSelectedItem() + "-" + yearBox.getSelectedItem();
                        String ticketType = (String) ticketTypeBox.getSelectedItem();

                        if (fullName.isEmpty() || gender.isEmpty() || contactNumber.isEmpty() || ticketType.equals("Select")) {
                            JOptionPane.showMessageDialog(this, "Please fill all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!contactNumber.matches("\\d{7,10}")){
                            JOptionPane.showMessageDialog(this, "Contact info has to be a 7-10 digit number", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        for (ArtGalleryVisitor visitor : visitors) {
                            if (visitor.getVisitorId() == visitorId) {
                                JOptionPane.showMessageDialog(this, "Visitor ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        ArtGalleryVisitor visitor = ticketType.equals("Elite") ? new EliteVisitor(visitorId, fullName, gender, contactNumber, registrationDate, ticketType) :
                            new StandardVisitor(visitorId, fullName, gender, contactNumber, registrationDate, ticketType);
                        visitors.add(visitor);

                        clearForm();
                        JOptionPane.showMessageDialog(this, "Visitor added successfully: " + fullName, "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });
        clearButton.addActionListener(e -> {
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Form cleared successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            });

        displayButton.addActionListener(e -> {
                    showVisitorsTable();
            });

        readButton.addActionListener(e -> {
                    showSavedVisitorsFromFile();
            });

        saveButton.addActionListener(e -> {
                    saveVisitorsToFile();
            });

        buyButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(purchaseVisitorIDField.getText().trim());
                        String artworkName = artworkNameField.getText().trim();
                        double artworkPrice = Double.parseDouble(artworkPriceField.getText().trim());
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null) {
                            String result = visitor.buyProduct(artworkName, artworkPrice);
                            visitor.calculateDiscount();
                            visitor.calculateRewardPoint();
                            JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for Visitor ID and Artwork Price.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        discountButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(purchaseVisitorIDField.getText().trim());
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null) {
                            double discount = visitor.calculateDiscount();
                            DecimalFormat df = new DecimalFormat("#.00");
                            JOptionPane.showMessageDialog(this, "Discount Amount: " + df.format(discount), "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });
        generateBillButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(purchaseVisitorIDField.getText().trim());
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null) {
                            if (visitor.getIsBought()) {
                                showBillPopup(visitor);
                                saveBillToFile(visitor, visitorId);
                                JOptionPane.showMessageDialog(this, "Bill generated and saved as Bill_" + visitorId + ".txt", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "No purchase made to generate bill.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        rewardButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(purchaseVisitorIDField.getText().trim());
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null) {
                            double reward = visitor.calculateRewardPoint();
                            JOptionPane.showMessageDialog(this, "Reward Points: " + reward, "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        cancelButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(purchaseVisitorIDField.getText().trim());
                        String artworkName = artworkNameField.getText().trim();
                        String cancellationReason = JOptionPane.showInputDialog(this, "Enter cancellation reason:");
                        if (cancellationReason == null) cancellationReason = "No reason provided";
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null) {
                            String result = visitor.cancelProduct(artworkName, cancellationReason);
                            JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        logVisitorButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(visitorIDField.getText().trim());
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null) {
                            visitor.logVisit();
                            JOptionPane.showMessageDialog(this, "Visit logged for Visitor ID: " + visitorId, "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        assignAdvisorButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(visitorIDField.getText().trim());
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null && visitor instanceof EliteVisitor) {
                            boolean assigned = ((EliteVisitor) visitor).assignPersonalArtAdvisor();
                            JOptionPane.showMessageDialog(this, "Personal Art Advisor Assigned: " + assigned, "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Only Elite Visitors can be assigned a Personal Art Advisor or Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        checkDiscountButton.addActionListener(e -> {
                    try {
                        int visitorId = Integer.parseInt(visitorIDField.getText().trim());
                        ArtGalleryVisitor visitor = findVisitor(visitorId);
                        if (visitor != null && visitor instanceof StandardVisitor) {
                            boolean eligible = ((StandardVisitor) visitor).checkDiscountUpgrade();
                            JOptionPane.showMessageDialog(this, "Eligible for Discount Upgrade: " + eligible, "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Only Standard Visitors have discount upgrade eligibility or Visitor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Visitor ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        setVisible(true);
    }

    // New method to show visitors in a tabular format popup
    private void showVisitorsTable() {
        if (visitors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No visitors to display.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"Visitor ID", "Full Name", "Gender", "Registration Date", 
                "Contact Number", "Ticket Type", "Ticket Cost", "Visit Count", 
                "Reward Points", "Is Active", "Special Info"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        DecimalFormat df = new DecimalFormat("#.00");

        for (ArtGalleryVisitor visitor : visitors) {
            String specialInfo = "";
            if (visitor instanceof StandardVisitor) {
                StandardVisitor sv = (StandardVisitor) visitor;
                specialInfo = "Discount: " + (sv.getDiscountPercent() * 100) + "%, Eligible: " + sv.getIsEligibleForDiscountUpgrade();
            } else if (visitor instanceof EliteVisitor) {
                EliteVisitor ev = (EliteVisitor) visitor;
                specialInfo = "Advisor: " + (ev.getAssignedPersonalArtAdvisor() ? "Yes" : "No") + 
                ", Events: " + (ev.getExclusiveEventAccess() ? "Yes" : "No");
            }

            Object[] row = {
                    visitor.getVisitorId(),
                    visitor.getFullName(),
                    visitor.getGender(),
                    visitor.getRegistrationDate(),
                    visitor.getContactNumber(),
                    visitor.getTicketType(),
                    df.format(visitor.getTicketCost()),
                    visitor.getVisitCount(),
                    df.format(visitor.getRewardPoints()),
                    visitor.getIsActive() ? "Yes" : "No",
                    specialInfo,

                };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Visitor Details", JOptionPane.PLAIN_MESSAGE);
    }

    // New method to show bill in tabular format popup
    private void showBillPopup(ArtGalleryVisitor visitor) {
        if (!visitor.getIsBought()) {
            JOptionPane.showMessageDialog(this, "No purchase made to generate bill.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"Description", "Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        DecimalFormat df = new DecimalFormat("#.00");

        model.addRow(new Object[]{"Visitor ID", visitor.getVisitorId()});
        model.addRow(new Object[]{"Visitor Name", visitor.getFullName()});
        model.addRow(new Object[]{"Ticket Type", visitor.getTicketType()});
        model.addRow(new Object[]{"Artwork Name", visitor.getArtworkName()});
        model.addRow(new Object[]{"Original Price", "Rs. " + df.format(visitor.getArtworkPrice())});
        model.addRow(new Object[]{"Discount Amount", "Rs. " + df.format(visitor.getDiscountAmount())});
        model.addRow(new Object[]{"Final Price", "Rs. " + df.format(visitor.getFinalPrice())});
        model.addRow(new Object[]{"Reward Points Earned", df.format(visitor.getRewardPoints())});

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setBackground(Color.CYAN);
        table.setRowHeight(25);

        // Make the table look more like an invoice
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JPanel billPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("INVOICE - Art Gallery System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        billPanel.add(titleLabel, BorderLayout.NORTH);
        billPanel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, billPanel, "Invoice", JOptionPane.PLAIN_MESSAGE);
    }

    // Method to show saved visitors from file in tabular format and populate visitors list
    private void showSavedVisitorsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("VisitorDetails.txt"))) {
            String line;
            boolean isDataSection = false;
            ArrayList<String[]> data = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("----------")) {
                    isDataSection = true;
                    continue;
                }
                if (isDataSection && !line.trim().isEmpty()) {
                    String[] parts = line.trim().split("\\s+", 10); // Split with limit to handle spaces in fullName
                    if (parts.length >= 7) {
                        data.add(parts);
                    }
                }
            }

            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No visitor data found in file.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Clear existing visitors to avoid duplicates
            visitors.clear();

            // Populate visitors ArrayList
            for (String[] parts : data) {
                try {
                    int visitorId = Integer.parseInt(parts[0]);
                    // Reconstruct fullName by combining parts until gender
                    StringBuilder fullNameBuilder = new StringBuilder();
                    int genderIndex = 1;
                    while (genderIndex < parts.length && !parts[genderIndex].equals("Male") && !parts[genderIndex].equals("Female")) {
                        fullNameBuilder.append(parts[genderIndex]).append(" ");
                        genderIndex++;
                    }
                    String fullName = fullNameBuilder.toString().trim();
                    String gender = parts[genderIndex];
                    String regDate = parts[genderIndex + 1];
                    String contact = parts[genderIndex + 2];
                    String ticketType = parts[genderIndex + 3];

                    // Create visitor object based on ticket type
                    ArtGalleryVisitor visitor = ticketType.equals("Elite") ? 
                            new EliteVisitor(visitorId, fullName, gender, contact, regDate, ticketType) :
                        new StandardVisitor(visitorId, fullName, gender, contact, regDate, ticketType);

                    // Update additional fields if available
                    if (parts.length > genderIndex + 6) {
                        int visitCount = Integer.parseInt(parts[genderIndex + 4]);
                        double rewardPoints = Double.parseDouble(parts[genderIndex + 5]);
                        boolean isActive = parts[genderIndex + 6].equals("Yes");

                        // Set visit count
                        for (int i = 0; i < visitCount; i++) {
                            visitor.logVisit();
                        }
                        // Set isActive
                        if (!isActive) {
                            visitor.isActive = false; // Directly set isActive to false if needed
                        }
                        // Set reward points
                        visitor.rewardPoints = rewardPoints;
                    }

                    // Check for duplicate visitor IDs before adding
                    boolean exists = false;
                    for (ArtGalleryVisitor v : visitors) {
                        if (v.getVisitorId() == visitorId) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        visitors.add(visitor);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    // Skip malformed lines
                    continue;
                }
            }

            String[] columnNames = {"Visitor ID", "Full Name", "Gender", "Reg Date", 
                    "Contact Number", "Ticket Type", "Ticket Price"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (String[] rowData : data) {
                model.addRow(rowData);
            }

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Saved Visitor Details", JOptionPane.PLAIN_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading from file or file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to save visitors to file in tabular format (overwritten every time with current state)
    private void saveVisitorsToFile() {
        File file = new File("VisitorDetails.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            // Always write title and header
            writer.write("ART GALLERY VISITOR DETAILS\n");
            writer.write("===========================\n\n");
            writer.write(String.format("%-12s %-20s %-10s %-15s %-15s %-15s %-15s %-12s %-12s %-10s%n",
                    "Visitor_ID", "Full_Name", "Gender", "Reg_Date", "Contact", "Ticket_Type", "Ticket_Price", "Visit_Count", "Reward_Pts", "Active"));
            writer.write(String.format("%-12s %-20s %-10s %-15s %-15s %-15s %-15s %-12s %-12s %-10s%n",
                    "----------", "--------------------", "----------", "---------------", "---------------", 
                    "---------------", "---------------", "----------", "----------", "--------"));

            // Write all current visitor data (including updates)
            DecimalFormat df = new DecimalFormat("#.00");
            for (ArtGalleryVisitor v : visitors) {
                writer.write(String.format("%-12d %-20s %-10s %-15s %-15s %-15s %-15s %-12d %-12s %-10s%n",
                        v.getVisitorId(), v.getFullName(), v.getGender(), v.getRegistrationDate(),
                        v.getContactNumber(), v.getTicketType(), df.format(v.getTicketCost()),
                        v.getVisitCount(), df.format(v.getRewardPoints()), 
                        v.getIsActive() ? "Yes" : "No"));
            }

            JOptionPane.showMessageDialog(this, "Data saved to VisitorDetails.txt ", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to save bill to file in tabular format
    private void saveBillToFile(ArtGalleryVisitor visitor, int visitorId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Bill_" + visitorId + ".txt"))) {
            DecimalFormat df = new DecimalFormat("#.00");

            writer.write("INVOICE - ART GALLERY SYSTEM\n");
            writer.write("============================\n\n");
            writer.write("Date: " + java.time.LocalDate.now() + "\n");
            writer.write("Time: " + java.time.LocalTime.now().toString().substring(0, 8) + "\n\n");

            writer.write(String.format("%-25s : %s%n", "Visitor ID", visitor.getVisitorId()));
            writer.write(String.format("%-25s : %s%n", "Visitor Name", visitor.getFullName()));
            writer.write(String.format("%-25s : %s%n", "Ticket Type", visitor.getTicketType()));
            writer.write("----------------------------------------\n");
            writer.write(String.format("%-25s : %s%n", "Artwork Name", visitor.getArtworkName()));
            writer.write(String.format("%-25s : Rs. %s%n", "Original Price", df.format(visitor.getArtworkPrice())));
            writer.write(String.format("%-25s : Rs. %s%n", "Discount Amount", df.format(visitor.getDiscountAmount())));
            writer.write("----------------------------------------\n");
            writer.write(String.format("%-25s : Rs. %s%n", "TOTAL AMOUNT", df.format(visitor.getFinalPrice())));
            writer.write(String.format("%-25s : %s%n", "Reward Points Earned", df.format(visitor.getRewardPoints())));
            writer.write("----------------------------------------\n");
            writer.write("\nThank you for visiting our Art Gallery!\n");

        } catch (IOException ex) {
            // Error handling will be done in the calling method
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int y, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void updateTicketPrice() {
        String ticketType = (String) ticketTypeBox.getSelectedItem();
        if (ticketType.equals("Standard")) {
            ticketPriceField.setText("1000.0");
        } else if (ticketType.equals("Elite")) {
            ticketPriceField.setText("2000.0");
        } else {
            ticketPriceField.setText("");
        }
    }

    private void clearForm() {
        visitorIDField.setText("");
        fullNameField.setText("");
        contactNumberField.setText("");
        ticketPriceField.setText("");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.clearSelection();
        ticketTypeBox.setSelectedIndex(0);
        dayBox.setSelectedIndex(0);
        monthBox.setSelectedIndex(0);
        yearBox.setSelectedIndex(0);
    }

    private ArtGalleryVisitor findVisitor(int visitorId) {
        for (ArtGalleryVisitor visitor : visitors) {
            if (visitor.getVisitorId() == visitorId) {
                return visitor;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArtGalleryGUI());
    }
}