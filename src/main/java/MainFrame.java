import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {

  private CardLayout cardLayout;
  private JPanel mainPanel;
  private LoginRegistrationPanel loginRegisterPanel;
  private String currentUser; // To keep track of the logged-in user

  public MainFrame() throws IOException {
    setTitle("Health Tracker App");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600); // Increased size for better chart visibility
    setLocationRelativeTo(null); // Center the window

    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    // Create the login/register panel
    loginRegisterPanel = new LoginRegistrationPanel(this);
    mainPanel.add(loginRegisterPanel, "LOGIN_REGISTER");

    add(mainPanel);

    cardLayout.show(mainPanel, "LOGIN_REGISTER"); // Start with the login/register screen
  }

  public void showAddMetricsScreen(String username) throws IOException {
    this.currentUser = username;
    // Create a new instance of AddMetricsPanel each time
    AddMetricsPanel addMetricsPanel = new AddMetricsPanel(this, username);

    // Remove any existing "ADD_METRICS" panel
    Component[] components = mainPanel.getComponents();
    for (Component component : components) {
      if (component instanceof AddMetricsPanel) {
        mainPanel.remove(component);
        break;
      }
    }

    mainPanel.add(addMetricsPanel, "ADD_METRICS");
    cardLayout.show(mainPanel, "ADD_METRICS");
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  public void showViewMetricsScreen(String username) throws IOException {
    this.currentUser = username;
    // Create a new instance of ViewMetricsPanel each time
    ViewMetricsPanel viewMetricsPanel = new ViewMetricsPanel(this, username);

    // Remove any existing "VIEW_METRICS" panel
    Component[] components = mainPanel.getComponents();
    for (Component component : components) {
      if (component instanceof ViewMetricsPanel) {
        mainPanel.remove(component);
      }
    }

    mainPanel.add(viewMetricsPanel, "VIEW_METRICS");
    cardLayout.show(mainPanel, "VIEW_METRICS");
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  public void showLoginRegisterScreen() {
    // Clear any existing data in the login/register fields
    loginRegisterPanel.resetFields();

    // Switch to the login/register card
    cardLayout.show(mainPanel, "LOGIN_REGISTER");
    currentUser = null; // Reset current user on logout
  }

  public static void main(String[] args) {
    // Set system look and feel for better appearance
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
      try {
        new MainFrame().setVisible(true);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error initializing application: " + e.getMessage(),
            "Application Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
      }
    });
  }
}