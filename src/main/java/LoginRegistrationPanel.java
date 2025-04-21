import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LoginRegistrationPanel extends JPanel implements ActionListener {

  private MainFrame mainFrame;

  // Registration Components
  private JTextField registerNameField;
  private JTextField registerUsernameField;
  private JPasswordField registerPasswordField;
  private JComboBox<Gender> registerGenderCombo;
  private JTextField registerDOBField;
  private JButton registerButton;

  // Login Components
  private JTextField loginUsernameField;
  private JPasswordField loginPasswordField;
  private JButton loginButton;


  public LoginRegistrationPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    setLayout(new BorderLayout());

    JPanel registrationPanel = createRegistrationPanel();
    JPanel loginPanel = createLoginPanel();

    JPanel mainPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;

    // Add a welcome label at the top
    JLabel welcomeLabel = new JLabel("Health Tracker - Login or Register", SwingConstants.CENTER);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
    gbc.gridwidth = 2;
    mainPanel.add(welcomeLabel, gbc);

    // Add registration and login panels
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    mainPanel.add(registrationPanel, gbc);

    gbc.gridx = 1;
    mainPanel.add(loginPanel, gbc);

    add(mainPanel, BorderLayout.CENTER);
  }

  private JPanel createRegistrationPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Register"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;

    // Name field
    panel.add(new JLabel("Name:"), gbc);
    gbc.gridx++;
    registerNameField = new JTextField(15);
    panel.add(registerNameField, gbc);

    // Username field
    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Username:"), gbc);
    gbc.gridx++;
    registerUsernameField = new JTextField(15);
    panel.add(registerUsernameField, gbc);

    // Password field
    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Password:"), gbc);
    gbc.gridx++;
    registerPasswordField = new JPasswordField(15);
    panel.add(registerPasswordField, gbc);

    // Gender dropdown
    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Gender:"), gbc);
    gbc.gridx++;
    registerGenderCombo = new JComboBox<>(Gender.values());
    panel.add(registerGenderCombo, gbc);

    // Date of birth field
    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
    gbc.gridx++;
    registerDOBField = new JTextField(15);
    panel.add(registerDOBField, gbc);

    // Register button
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    registerButton = new JButton("Register");
    registerButton.addActionListener(this);
    panel.add(registerButton, gbc);

    return panel;
  }

  private JPanel createLoginPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Login"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;

    // Username field
    panel.add(new JLabel("Username:"), gbc);
    gbc.gridx++;
    loginUsernameField = new JTextField(15);
    panel.add(loginUsernameField, gbc);

    // Password field
    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Password:"), gbc);
    gbc.gridx++;
    loginPasswordField = new JPasswordField(15);
    panel.add(loginPasswordField, gbc);

    // Login button
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    loginButton = new JButton("Login");
    loginButton.addActionListener(this);
    panel.add(loginButton, gbc);

    return panel;
  }

  public void resetFields() {
    // Clear all input fields
    registerNameField.setText("");
    registerUsernameField.setText("");
    registerPasswordField.setText("");
    registerDOBField.setText("");
    loginUsernameField.setText("");
    loginPasswordField.setText("");
  }

  private void clearRegistrationFields() {
    registerNameField.setText("");
    registerUsernameField.setText("");
    registerPasswordField.setText("");
    registerDOBField.setText("");
  }

  private void clearLoginFields() {
    loginUsernameField.setText("");
    loginPasswordField.setText("");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == registerButton) {
      String name = registerNameField.getText();
      String username = registerUsernameField.getText();
      String password = new String(registerPasswordField.getPassword());
      Gender gender = (Gender) registerGenderCombo.getSelectedItem();
      String dobString = registerDOBField.getText();

      // Validate all fields are filled
      if (name.isEmpty() || username.isEmpty() || password.isEmpty() || dobString.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required for registration.",
            "Registration Failed", JOptionPane.WARNING_MESSAGE);
        return;
      }

      try {
        LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ISO_DATE);
        UserProfile newUser = new UserProfile(name, username, password, gender, dob);
        if (UserDataStorage.saveUserProfile(newUser)) {
          JOptionPane.showMessageDialog(this, "Registration successful! Please log in.",
              "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
          clearRegistrationFields();
        } else {
          JOptionPane.showMessageDialog(this, "Username already exists.",
              "Existing Username", JOptionPane.WARNING_MESSAGE);
        }
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.",
            "Invalid Format", JOptionPane.WARNING_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error during registration: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Log the error
      }
    } else if (e.getSource() == loginButton) {
      String username = loginUsernameField.getText();
      String password = new String(loginPasswordField.getPassword());

      // Validate fields
      if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Username and password are required.",
            "Login Failed", JOptionPane.WARNING_MESSAGE);
        return;
      }

      try {
        if (UserDataStorage.authenticateUser(username, password)) {
          mainFrame.showAddMetricsScreen(username);
          clearLoginFields();
        } else {
          JOptionPane.showMessageDialog(this, "Invalid username or password.",
              "Login Failed", JOptionPane.WARNING_MESSAGE);
        }
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage(),
            "Login Failed", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Log the error
      }
    }
  }
}