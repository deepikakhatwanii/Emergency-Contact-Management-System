import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EmergencyContactManagement extends Frame
        implements ActionListener {

    Label title, totalLabel, importantLabel;
    Label nameLabel, phoneLabel, relationLabel, categoryLabel;
    Label searchLabel;

    TextField nameField, phoneField, searchField;

    Choice relationChoice, categoryChoice;

    Checkbox importantCheck;

    Button addButton, updateButton, searchButton;
    Button deleteButton, displayButton, clearButton, exitButton;

    TextArea contactArea;

    EmergencyContactManagement() {
        setTitle("Emergency Contact Management System");
        setSize(650, 700);
        setLayout(null);
        setBackground(Color.WHITE);

        title = new Label("Emergency Contact Manager");
        title.setBounds(150, 40, 350, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);

        totalLabel = new Label("Total Contacts: 0");
        totalLabel.setBounds(100, 85, 180, 25);
        add(totalLabel);

        importantLabel = new Label("Important Contacts: 0");
        importantLabel.setBounds(330, 85, 200, 25);
        add(importantLabel);

        nameLabel = new Label("Name:");
        nameLabel.setBounds(70, 135, 100, 25);
        add(nameLabel);

        nameField = new TextField();
        nameField.setBounds(190, 135, 300, 25);
        add(nameField);

        phoneLabel = new Label("Phone Number:");
        phoneLabel.setBounds(70, 175, 110, 25);
        add(phoneLabel);

        phoneField = new TextField();
        phoneField.setBounds(190, 175, 300, 25);
        add(phoneField);

        relationLabel = new Label("Relation:");
        relationLabel.setBounds(70, 215, 100, 25);
        add(relationLabel);

        relationChoice = new Choice();
        relationChoice.add("Family");
        relationChoice.add("Friend");
        relationChoice.add("Doctor");
        relationChoice.add("Police");
        relationChoice.add("Other");
        relationChoice.setBounds(190, 215, 300, 25);
        add(relationChoice);

        categoryLabel = new Label("Category:");
        categoryLabel.setBounds(70, 255, 100, 25);
        add(categoryLabel);

        categoryChoice = new Choice();
        categoryChoice.add("Personal");
        categoryChoice.add("Medical");
        categoryChoice.add("Emergency");
        categoryChoice.add("Other");
        categoryChoice.setBounds(190, 255, 300, 25);
        add(categoryChoice);

        importantCheck = new Checkbox("Mark as Important");
        importantCheck.setBounds(190, 290, 180, 25);
        add(importantCheck);

        addButton = new Button("Add Contact");
        addButton.setBounds(70, 330, 120, 30);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new Button("Update");
        updateButton.setBounds(210, 330, 100, 30);
        updateButton.addActionListener(this);
        add(updateButton);

        searchLabel = new Label("Search by Name/Phone:");
        searchLabel.setBounds(70, 380, 150, 25);
        add(searchLabel);

        searchField = new TextField();
        searchField.setBounds(230, 380, 260, 25);
        add(searchField);

        searchButton = new Button("Search");
        searchButton.setBounds(70, 420, 100, 30);
        searchButton.addActionListener(this);
        add(searchButton);

        deleteButton = new Button("Delete");
        deleteButton.setBounds(185, 420, 100, 30);
        deleteButton.addActionListener(this);
        add(deleteButton);

        displayButton = new Button("Display All");
        displayButton.setBounds(300, 420, 100, 30);
        displayButton.addActionListener(this);
        add(displayButton);

        clearButton = new Button("Clear");
        clearButton.setBounds(415, 420, 75, 30);
        clearButton.addActionListener(this);
        add(clearButton);

        exitButton = new Button("Exit");
        exitButton.setBounds(505, 420, 75, 30);
        exitButton.addActionListener(this);
        add(exitButton);

        Label listLabel = new Label("Saved Contacts / Messages:");
        listLabel.setBounds(50, 470, 250, 25);
        add(listLabel);

        contactArea = new TextArea();
        contactArea.setBounds(50, 500, 550, 140);
        contactArea.setEditable(false);
        add(contactArea);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        updateCounters();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addButton) {
            addContact();

        } else if (e.getSource() == updateButton) {
            updateContact();

        } else if (e.getSource() == searchButton) {
            searchContact();

        } else if (e.getSource() == deleteButton) {
            deleteContact();

        } else if (e.getSource() == displayButton) {
            displayContacts();

        } else if (e.getSource() == clearButton) {
            clearFields();

        } else if (e.getSource() == exitButton) {
            dispose();
        }
    }

    // Add contact to MySQL
    void addContact() {

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String relation = relationChoice.getSelectedItem();
        String category = categoryChoice.getSelectedItem();

        String priority = importantCheck.getState()
                ? "High" : "Normal";

        if (name.isEmpty() || phone.isEmpty()) {
            contactArea.setText(
                    "Please enter both name and phone number.");
            return;
        }

        if (!phone.matches("[0-9]{10}")) {
            contactArea.setText(
                    "Enter a valid 10-digit phone number.");
            return;
        }

        String sql = "INSERT INTO contacts "
                + "(name, phone, relationship, category, priority) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                contactArea.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, relation);
                ps.setString(4, category);
                ps.setString(5, priority);

                ps.executeUpdate();

                contactArea.setText("Contact added successfully!");

                clearInputFields();
                updateCounters();

            }

        } catch (SQLException ex) {

            if (ex.getErrorCode() == 1062) {
                contactArea.setText(
                        "This phone number already exists.");
            } else {
                contactArea.setText(
                        "Error saving contact: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // Search contact by name or phone
    void searchContact() {

        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            contactArea.setText("Enter a name or phone number.");
            return;
        }

        String sql = "SELECT * FROM contacts "
                + "WHERE name = ? OR phone = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                contactArea.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, query);
                ps.setString(2, query);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        String foundName = rs.getString("name");
                        String foundPhone = rs.getString("phone");
                        String relation = rs.getString("relationship");
                        String category = rs.getString("category");
                        String priority = rs.getString("priority");

                        nameField.setText(foundName);
                        phoneField.setText(foundPhone);
                        searchField.setText(foundPhone);

                        relationChoice.select(relation);
                        categoryChoice.select(category);

                        importantCheck.setState(
                                "High".equalsIgnoreCase(priority));

                        contactArea.setText(
                                "Name: " + foundName
                                + "\nPhone: " + foundPhone
                                + "\nRelation: " + relation
                                + "\nCategory: " + category
                                + "\nPriority: " + priority);

                    } else {
                        contactArea.setText("Contact not found.");
                    }
                }
            }

        } catch (SQLException ex) {
            contactArea.setText(
                    "Search failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Update existing contact
    void updateContact() {

        String oldPhone = searchField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String name = nameField.getText().trim();

        if (oldPhone.isEmpty()) {
            contactArea.setText(
                    "Search for the contact before updating.");
            return;
        }

        if (name.isEmpty() || newPhone.isEmpty()) {
            contactArea.setText(
                    "Name and phone number cannot be empty.");
            return;
        }

        if (!newPhone.matches("[0-9]{10}")) {
            contactArea.setText(
                    "Enter a valid 10-digit phone number.");
            return;
        }

        String sql = "UPDATE contacts SET name = ?, phone = ?, "
                + "relationship = ?, category = ?, priority = ? "
                + "WHERE phone = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                contactArea.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, newPhone);
                ps.setString(3, relationChoice.getSelectedItem());
                ps.setString(4, categoryChoice.getSelectedItem());

                ps.setString(5,
                        importantCheck.getState() ? "High" : "Normal");

                ps.setString(6, oldPhone);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    searchField.setText(newPhone);
                    contactArea.setText(
                            "Contact updated successfully!");
                    updateCounters();
                } else {
                    contactArea.setText(
                            "Contact not found. Search again.");
                }
            }

        } catch (SQLException ex) {

            if (ex.getErrorCode() == 1062) {
                contactArea.setText(
                        "This phone number already exists.");
            } else {
                contactArea.setText(
                        "Update failed: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // Delete contact after confirmation
    void deleteContact() {

        String phone = searchField.getText().trim();

        if (phone.isEmpty()) {
            contactArea.setText(
                    "Search for a contact before deleting.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this contact?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            contactArea.setText("Deletion cancelled.");
            return;
        }

        String sql = "DELETE FROM contacts WHERE phone = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                contactArea.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, phone);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    contactArea.setText(
                            "Contact deleted successfully!");

                    clearInputFields();
                    searchField.setText("");
                    updateCounters();

                } else {
                    contactArea.setText(
                            "Contact not found. Search again.");
                }
            }

        } catch (SQLException ex) {
            contactArea.setText(
                    "Delete failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Display all contacts
    void displayContacts() {

        String sql = "SELECT * FROM contacts ORDER BY id";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                contactArea.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                StringBuilder output = new StringBuilder();
                int count = 0;

                while (rs.next()) {

                    count++;

                    output.append(count)
                            .append(". ")
                            .append(rs.getString("name"))
                            .append(" | Phone: ")
                            .append(rs.getString("phone"))
                            .append(" | Relation: ")
                            .append(rs.getString("relationship"))
                            .append(" | Category: ")
                            .append(rs.getString("category"))
                            .append(" | Priority: ")
                            .append(rs.getString("priority"))
                            .append("\n");
                }

                if (count == 0) {
                    contactArea.setText("No contacts available.");
                } else {
                    contactArea.setText(output.toString());
                }
            }

        } catch (SQLException ex) {
            contactArea.setText(
                    "Display failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Update dashboard counters
    void updateCounters() {

        String sql = "SELECT COUNT(*) AS total, "
                + "COALESCE(SUM(CASE WHEN priority = 'High' "
                + "THEN 1 ELSE 0 END), 0) AS important "
                + "FROM contacts";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                totalLabel.setText("Total Contacts: 0");
                importantLabel.setText("Important Contacts: 0");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int total = rs.getInt("total");
                    int importantCount = rs.getInt("important");

                    totalLabel.setText("Total Contacts: " + total);

                    importantLabel.setText(
                            "Important Contacts: " + importantCount);
                }
            }

        } catch (SQLException ex) {
            contactArea.setText(
                    "Dashboard counter error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Clear input fields
    void clearInputFields() {

        nameField.setText("");
        phoneField.setText("");
        relationChoice.select(0);
        categoryChoice.select(0);
        importantCheck.setState(false);
    }

    // Clear complete form
    void clearFields() {

        clearInputFields();
        searchField.setText("");
        contactArea.setText("");
    }

    public static void main(String[] args) {
        new EmergencyContactManagement();
    }
}