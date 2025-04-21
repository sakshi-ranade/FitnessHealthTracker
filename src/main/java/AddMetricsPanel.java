import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This panel is used to input and save different daily health metrics
 * like weight, steps, heart rate, and period logs (for female users).
 * It also includes navigation buttons to view saved metrics or logout.
 * This class uses GridBagLayout to organize components on the screen.
 */
public class AddMetricsPanel extends JPanel implements ActionListener {

  private MainFrame mainFrame;
  private String username;

  // Weight Components
  private JTextField weightField;
  private JComboBox<String> weightUnitCombo;
  private JButton saveWeightButton;

  // Steps Components
  private JTextField stepsField;
  private JButton saveStepsButton;

  // Heart Rate Components
  private JTextField heartRateField;
  private JTextField heartRateTimeField;
  private JTextField heartRateTagField;
  private JButton addHeartRateButton;

  // Period Log Components
  private JTextField periodStartDateField;
  private JTextField periodEndDateField;
  private JComboBox<String> flowLevelCombo;
  private JTextField periodTagsField;
  private JButton addPeriodLogButton;

  // Navigation Buttons
  private JButton viewMetricsButton;
  private JButton logoutButton;

  /**
   * Constructor for the AddMetricsPanel.
   * It sets up the layout and adds all input sections and navigation buttons.
   *
   * @param mainFrame Reference to the main application frame.
   * @param username The username of the logged-in user.
   * @throws IOException If there is an error reading user profile data.
   */
  public AddMetricsPanel(MainFrame mainFrame, String username) throws IOException {
    this.mainFrame = mainFrame;
    this.username = username; // Store the username
    // Use GridBagLayout to position components in a flexible grid.
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // spacing between components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.NORTH;
    // Show user profile information (name and age) at the top
    UserProfile userProfile = UserDataStorage.getUserProfile(username);
    if (userProfile != null) {
      JPanel profilePanel = createProfileHeading(userProfile);
      add(profilePanel, gbc);
    }
    gbc.gridy++;

    // Weight Section
    JPanel weightPanel = createWeightPanel();
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    add(weightPanel, gbc);
    gbc.gridwidth = 1;

    // Steps Section
    JPanel stepsPanel = createStepsPanel();
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    add(stepsPanel, gbc);
    gbc.gridwidth = 1;

    // Heart Rate Section
    JPanel heartRatePanel = createHeartRatePanel();
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    add(heartRatePanel, gbc);
    gbc.gridwidth = 1;

    // Period Log Sections
    if (userProfile != null && userProfile.getGender() == Gender.FEMALE) {
      JPanel periodLogPanel = createPeriodLogPanel();
      gbc.gridy++;
      gbc.gridx = 0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      add(periodLogPanel, gbc);
      gbc.gridwidth = 1;
    }

    // Navigation Buttons
    JPanel navigationPanel = createNavigationPanel();
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    add(navigationPanel, gbc);

  }

  /**
   * Creates a panel to input and save the user's weight.
   * Users can enter weight and choose the unit (kg or lbs).
   *
   * @return JPanel containing weight input components.
   */
  private JPanel createWeightPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Weight:"), gbc);
    // Dropdown to select weight unit
    gbc.gridx = 1;
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    weightField = new JTextField(10);
    inputPanel.add(weightField);
    weightUnitCombo = new JComboBox<>(new String[]{"kg", "lbs"});
    inputPanel.add(weightUnitCombo);
    panel.add(inputPanel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    saveWeightButton = new JButton("Save Weight");
    saveWeightButton.addActionListener(this);
    panel.add(saveWeightButton, gbc);

    return panel;
  }

  /**
   * Creates a panel to input and save the user's steps for today.
   * Users can enter the number of steps taken.
   *
   * @return JPanel containing steps input components.
   */
  private JPanel createStepsPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Steps:"), gbc);

    gbc.gridx = 1;
    stepsField = new JTextField(10);
    panel.add(stepsField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    saveStepsButton = new JButton("Save Steps");
    saveStepsButton.addActionListener(this);
    panel.add(saveStepsButton, gbc);

    return panel;
  }

  /**
   * Creates a panel to input and save heart rate data.
   * Users can enter heart rate value, time, and a tag describing the context.
   *
   * @return JPanel containing heart rate input components.
   */
  private JPanel createHeartRatePanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Heart Rate:"), gbc);

    gbc.gridx = 1;
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    heartRateField = new JTextField(5);
    inputPanel.add(heartRateField);
    heartRateTimeField = new JTextField(8);
    heartRateTimeField.setToolTipText("HH:MM (e.g., 10:30)");
    inputPanel.add(new JLabel("at"));
    inputPanel.add(heartRateTimeField);
    heartRateTagField = new JTextField(10);
    heartRateTagField.setToolTipText("e.g., resting, after exercise");
    inputPanel.add(new JLabel("Tag:"));
    inputPanel.add(heartRateTagField);
    panel.add(inputPanel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    addHeartRateButton = new JButton("Add Heart Rate");
    addHeartRateButton.addActionListener(this);
    panel.add(addHeartRateButton, gbc);

    return panel;
  }

  /**
   * Creates a panel for female users to log their period data.
   * Includes start and end dates, flow level, and optional tags.
   *
   * @return JPanel containing period log input components.
   */
  private JPanel createPeriodLogPanel() {
    // This panel is only shown for female users.
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Period Start Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    periodStartDateField = new JTextField(10);
    panel.add(periodStartDateField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(new JLabel("Period End Date (YYYY-MM-DD):"), gbc);
    gbc.gridx = 1;
    periodEndDateField = new JTextField(10);
    panel.add(periodEndDateField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(new JLabel("Flow Level:"), gbc);
    gbc.gridx = 1;
    flowLevelCombo = new JComboBox<>(new String[]{"Light", "Medium", "Heavy"});
    panel.add(flowLevelCombo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    panel.add(new JLabel("Period Tags:"), gbc);
    gbc.gridx = 1;
    periodTagsField = new JTextField(15);
    panel.add(periodTagsField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    addPeriodLogButton = new JButton("Add Period Log");
    addPeriodLogButton.addActionListener(this);
    panel.add(addPeriodLogButton, gbc);

    return panel;
  }

  /**
   * Creates a heading panel that shows the user's name, username and age.
   *
   * @param userProfile The profile of the currently logged-in user.
   * @return JPanel with name, username and age.
   */
  private JPanel createProfileHeading(UserProfile userProfile) {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 5, 2, 5);
    gbc.anchor = GridBagConstraints.CENTER;

    JLabel greetingLabel = new JLabel(String.format("Hi %s (%s)", userProfile.getName(), userProfile.getUserName()));
    greetingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    greetingLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel ageLabel = new JLabel("Age: " + userProfile.getAge());
    ageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    ageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    gbc.gridy = 0;
    panel.add(greetingLabel, gbc);

    gbc.gridy = 1;
    panel.add(ageLabel, gbc);

    return panel;
  }

  /**
   * Creates navigation buttons for viewing metrics or logging out.
   *
   * @return JPanel containing navigation buttons.
   */
  private JPanel createNavigationPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    viewMetricsButton = new JButton("View Metrics");
    viewMetricsButton.addActionListener(this);
    panel.add(viewMetricsButton);

    logoutButton = new JButton("Logout");
    logoutButton.addActionListener(this);
    panel.add(logoutButton);

    return panel;
  }

  /**
   * Handles actions when buttons are clicked.
   * Based on the source of the event, it saves the respective metric data.
   *
   * @param e The action event triggered by a button click.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == saveWeightButton) {
      if (username != null) {
        try {
          double weight = Double.parseDouble(weightField.getText());
          String unit = (String) weightUnitCombo.getSelectedItem();
          Weight weightObj = new Weight(weight, unit);
          DailyMetricsManager metricsManager = new DailyMetricsManager(username);
          if (metricsManager.saveWeight(LocalDate.now(), weightObj)) {
            JOptionPane.showMessageDialog(this, "Weight saved.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            weightField.setText("");
          } else {
            JOptionPane.showMessageDialog(this, "Error saving weight.",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(this, "Weight format is invalid.",
              "Error", JOptionPane.ERROR_MESSAGE);
        }  catch (IllegalArgumentException ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this, "User not logged in.",
            "Login Required", JOptionPane.WARNING_MESSAGE);
      }
    } else if (e.getSource() == saveStepsButton) {
      if (username != null) {
        try {
          int steps = Integer.parseInt(stepsField.getText());
          Steps stepsObj = new Steps(LocalDate.now(), steps);
          DailyMetricsManager metricsManager = new DailyMetricsManager(username);
          if (metricsManager.addSteps(stepsObj)) {
            JOptionPane.showMessageDialog(this, "Steps information updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            stepsField.setText("");
          } else {
            JOptionPane.showMessageDialog(this, "Error saving steps.",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(this, "Invalid steps format. ",
              "Error", JOptionPane.ERROR_MESSAGE);
        }  catch (IllegalArgumentException ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this, "User not logged in.",
            "Login Required", JOptionPane.WARNING_MESSAGE);
      }
    } else if (e.getSource() == addHeartRateButton) {
      if (username != null) {
        try {
          int heartRate = Integer.parseInt(heartRateField.getText());
          String timeStr = heartRateTimeField.getText();
          String tag = heartRateTagField.getText();
          LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
          HeartRate hrObj = new HeartRate(tag, LocalDate.now(), time, heartRate);
          DailyMetricsManager metricsManager = new DailyMetricsManager(username);
          if (metricsManager.addHeartRate(hrObj)) {
            JOptionPane.showMessageDialog(this, "Heart rate information saved successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            heartRateField.setText("");
            heartRateTimeField.setText("");
            heartRateTagField.setText("");
          } else {
            JOptionPane.showMessageDialog(this, "Error adding heart rate information.",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(this, "Invalid heart rate format.",
              "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
          JOptionPane.showMessageDialog(this, "Invalid time format (HH:MM).",
              "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this, "User not logged in.",
            "Login Required", JOptionPane.WARNING_MESSAGE);
      }
    } else if (e.getSource() == addPeriodLogButton) {
      if (username != null) {
        try {
          LocalDate startDate = LocalDate.parse(periodStartDateField.getText(), DateTimeFormatter.ISO_DATE);
          LocalDate endDate = LocalDate.parse(periodEndDateField.getText(), DateTimeFormatter.ISO_DATE);
          String flowLevel = (String) flowLevelCombo.getSelectedItem();
          String tags = periodTagsField.getText();
          PeriodLog periodLog = new PeriodLog(tags, startDate, endDate, flowLevel);
          DailyMetricsManager metricsManager = new DailyMetricsManager(username);
          if (metricsManager.addPeriodLog(periodLog)) {
            JOptionPane.showMessageDialog(this, "Period information saved successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            periodStartDateField.setText("");
            periodEndDateField.setText("");
            periodTagsField.setText("");
          } else {
            JOptionPane.showMessageDialog(this, "Error saving period log data.",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (DateTimeParseException ex) {
          JOptionPane.showMessageDialog(this, "Invalid date format (YYYY-MM-DD).",
              "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(this, "User not logged in.",
            "Login Required", JOptionPane.WARNING_MESSAGE);
      }
    } else if (e.getSource() == viewMetricsButton) {
      if (username != null) {
        try {
          mainFrame.showViewMetricsScreen(username);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } else {
        JOptionPane.showMessageDialog(this, "User not logged in.",
            "Login Required", JOptionPane.WARNING_MESSAGE);
      }
    } else if (e.getSource() == logoutButton) {
      mainFrame.showLoginRegisterScreen();
    }
  }
}
