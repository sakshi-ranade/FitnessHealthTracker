import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// JFreeChart imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class ViewMetricsPanel extends JPanel {

  private MainFrame mainFrame;
  private JTabbedPane tabbedPane;
  private JPanel weightPanel;
  private JTextArea weightTextArea;
  private JScrollPane weightScrollPane;
  private JPanel stepsPanel;
  private JTextArea stepsTextArea;
  private JScrollPane stepsScrollPane;
  private JPanel stepsChartPanel;
  private JPanel heartRatePanel;
  private JTextArea heartRateTextArea;
  private JScrollPane heartRateScrollPane;
  private JPanel heartRateChartPanel; // Panel for heart rate chart
  private JPanel periodLogPanel;
  private JTextArea periodLogTextArea;
  private JScrollPane periodLogScrollPane;
  private ExpertHelpPanel expertHelpPanel; // New panel for expert help
  private JButton backButton;
  private String username;

  public ViewMetricsPanel(MainFrame mainFrame, String username) throws IOException {
    this.mainFrame = mainFrame;
    this.username = username;
    setLayout(new BorderLayout());

    tabbedPane = new JTabbedPane();

    // Weight Tab
    weightPanel = new JPanel(new BorderLayout());
    weightTextArea = new JTextArea();
    weightTextArea.setEditable(false);
    weightScrollPane = new JScrollPane(weightTextArea);
    weightPanel.add(weightScrollPane, BorderLayout.CENTER);
    tabbedPane.addTab("Weight", weightPanel);

    // Steps Tab
    // Steps Tab - with chart and text data
    stepsPanel = new JPanel(new BorderLayout());

    JSplitPane stepsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    stepsSplitPane.setResizeWeight(0.7); // Give more space to chart

    stepsChartPanel = new JPanel(new BorderLayout()); // New panel for steps chart
    stepsTextArea = new JTextArea();
    stepsTextArea.setEditable(false);
    stepsScrollPane = new JScrollPane(stepsTextArea);

    stepsSplitPane.setTopComponent(stepsChartPanel);
    stepsSplitPane.setBottomComponent(stepsScrollPane);

    stepsPanel.add(stepsSplitPane, BorderLayout.CENTER);
    tabbedPane.addTab("Steps", stepsPanel);

    // Heart Rate Tab - with chart and text data
    heartRatePanel = new JPanel(new BorderLayout());

    // Create a split pane for text and chart
    JSplitPane heartRateSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    heartRateSplitPane.setResizeWeight(0.7); // Give more space to chart

    // Chart panel for heart rate visualization
    heartRateChartPanel = new JPanel(new BorderLayout());

    // Text area for heart rate data
    heartRateTextArea = new JTextArea();
    heartRateTextArea.setEditable(false);
    heartRateScrollPane = new JScrollPane(heartRateTextArea);

    heartRateSplitPane.setTopComponent(heartRateChartPanel);
    heartRateSplitPane.setBottomComponent(heartRateScrollPane);

    heartRatePanel.add(heartRateSplitPane, BorderLayout.CENTER);
    tabbedPane.addTab("Heart Rate", heartRatePanel);

    // Period Log Tab - only for female users
    UserProfile userProfile = UserDataStorage.getUserProfile(username);
    if (userProfile != null && userProfile.getGender() == Gender.FEMALE) {
      periodLogPanel = new JPanel(new BorderLayout());
      periodLogTextArea = new JTextArea();
      periodLogTextArea.setEditable(false);
      periodLogScrollPane = new JScrollPane(periodLogTextArea);
      periodLogPanel.add(periodLogScrollPane, BorderLayout.CENTER);
      tabbedPane.addTab("Period Log", periodLogPanel);
    }

    // Expert Help Tab - new tab for AI assistance
    expertHelpPanel = new ExpertHelpPanel(username);
    tabbedPane.addTab("Expert Help", expertHelpPanel);

    add(tabbedPane, BorderLayout.CENTER);

    // Navigation panel with Back and Logout buttons
    JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    backButton = new JButton("Back to Add Metrics");
    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          mainFrame.showAddMetricsScreen(username);
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    });
    navigationPanel.add(backButton);

    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.showLoginRegisterScreen();
      }
    });
    navigationPanel.add(logoutButton);

    add(navigationPanel, BorderLayout.SOUTH);

    // Load metrics data for the current user
    loadMetrics(username);
  }

  public void loadMetrics(String username) {
    if (username == null || username.isEmpty()) {
      JOptionPane.showMessageDialog(this, "No user logged in.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    this.username = username;
    DailyMetricsManager metricsManager = new DailyMetricsManager(username);

    // Load Weight Data
    weightTextArea.setText("");
    Map<LocalDate, Weight> weights = metricsManager.getAllWeights();
    StringBuilder weightBuilder = new StringBuilder("--- Weight Log ---\n");
    for (Map.Entry<LocalDate, Weight> entry : weights.entrySet()) {
      weightBuilder.append("Date: ").append(entry.getKey().format(DateTimeFormatter.ISO_DATE))
          .append(", Weight: ").append(entry.getValue().getWeightInUserUnits())
          .append(" ").append(entry.getValue().getUnit()).append("\n");
    }
    weightTextArea.setText(weightBuilder.toString());

    // Load Steps Data
    stepsTextArea.setText("");
    Map<LocalDate, List<Steps>> allSteps = metricsManager.getAllSteps();
    StringBuilder stepsBuilder = new StringBuilder("--- Steps Log ---\n");
    for (Map.Entry<LocalDate, List<Steps>> entry : allSteps.entrySet()) {
      for (Steps step : entry.getValue()) {
        stepsBuilder.append(step.toString()).append("\n");
      }
    }
    createStepsChart(allSteps);
    stepsTextArea.setText(stepsBuilder.toString());

    // Load Heart Rate Data
    heartRateTextArea.setText("");
    Map<LocalDate, List<HeartRate>> allHeartRates = metricsManager.getAllHeartRates();
    StringBuilder heartRateBuilder = new StringBuilder("--- Heart Rate Log ---\n");

    // Create a TreeMap to ensure dates are sorted
    TreeMap<LocalDate, List<HeartRate>> sortedHeartRates = new TreeMap<>(allHeartRates);

    for (Map.Entry<LocalDate, List<HeartRate>> entry : sortedHeartRates.entrySet()) {
      for (HeartRate hr : entry.getValue()) {
        heartRateBuilder.append(hr.toString()).append("\n");
      }
    }
    heartRateTextArea.setText(heartRateBuilder.toString());

    // Create and set heart rate chart with enhanced visualization
    createEnhancedHeartRateChart(sortedHeartRates);

    // Load Period Log Data (if applicable)
    if (periodLogTextArea != null) {
      periodLogTextArea.setText("");
      List<PeriodLog> periodLogs = metricsManager.getPeriodLogs();
      StringBuilder periodLogBuilder = new StringBuilder("--- Period Log ---\n");
      for (PeriodLog log : periodLogs) {
        periodLogBuilder.append(log.toString()).append("\n");
      }
      periodLogTextArea.setText(periodLogBuilder.toString());
    }
  }

  private void createEnhancedHeartRateChart(Map<LocalDate, List<HeartRate>> heartRateData) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    LocalTime startTime = LocalTime.of(8, 0);
    for (Map.Entry<LocalDate, List<HeartRate>> entry : heartRateData.entrySet()) {
      for (HeartRate hr : entry.getValue()) {
        dataset.addValue(hr.getHeartRate(), hr.getTags(), hr.getDate());
      }
    }
    JFreeChart chart = ChartFactory.createBarChart(
        "Heart Rate per Tag per Day",  // chart title
        "Date",                        // domain axis label
        "BPM",                         // range axis label
        dataset
    );
    // Create chart panel with enhanced features
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new Dimension(600, 400));
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setDomainZoomable(true);
    chartPanel.setRangeZoomable(true);

    // Add chart to panel
    heartRateChartPanel.removeAll();
    heartRateChartPanel.add(chartPanel, BorderLayout.CENTER);
    heartRateChartPanel.revalidate();
  }

  private void createStepsChart(Map<LocalDate, List<Steps>> stepsData) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for (Map.Entry<LocalDate, List<Steps>> entry : stepsData.entrySet()) {
      int stepCount = 0;
      for (Steps step : entry.getValue()) {
        stepCount += step.getSteps();
      }
      dataset.addValue(stepCount, "Steps", entry.getKey());
    }

    JFreeChart chart = ChartFactory.createBarChart(
        "Steps per Tag per Day",  // chart title
        "Date",                   // x-axis label
        "Steps",                  // y-axis label
        dataset
    );

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new Dimension(600, 400));
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setDomainZoomable(true);
    chartPanel.setRangeZoomable(true);

    stepsChartPanel.removeAll();
    stepsChartPanel.add(chartPanel, BorderLayout.CENTER);
    stepsChartPanel.revalidate();
  }

}