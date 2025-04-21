import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ExpertHelpPanel extends JPanel {
  private JTextArea questionArea;
  private JTextArea responseArea;
  private JButton submitButton;
  private JComboBox<String> topicSelector;
  private String apiKey= "";
  private final String username;
  private final DailyMetricsManager metricsManager;

  private static final String CONFIG_FILE = "config.properties";
  private static final String[] TOPICS = {
      "General Health Advice",
      "Period Health (Female)"
  };

  public ExpertHelpPanel(String username) {
    this.username = username;
    this.metricsManager = new DailyMetricsManager(username);

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Create components
    JPanel topPanel = new JPanel(new BorderLayout(5, 5));

    // Topic selector
    JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    selectorPanel.add(new JLabel("Select topic: "));
    topicSelector = new JComboBox<>(TOPICS);
    selectorPanel.add(topicSelector);
    topPanel.add(selectorPanel, BorderLayout.NORTH);

    // Question area
    questionArea = new JTextArea(3, 40);
    questionArea.setLineWrap(true);
    questionArea.setWrapStyleWord(true);
    JScrollPane questionScroll = new JScrollPane(questionArea);
    questionScroll.setBorder(BorderFactory.createTitledBorder("Ask your health question:"));
    topPanel.add(questionScroll, BorderLayout.CENTER);

    // Submit button
    submitButton = new JButton("Get Expert Advice");
    submitButton.addActionListener(e -> submitQuestion());
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(submitButton);
    topPanel.add(buttonPanel, BorderLayout.SOUTH);

    add(topPanel, BorderLayout.NORTH);

    // Response area
    responseArea = new JTextArea(15, 40);
    responseArea.setLineWrap(true);
    responseArea.setWrapStyleWord(true);
    responseArea.setEditable(false);
    JScrollPane responseScroll = new JScrollPane(responseArea);
    responseScroll.setBorder(BorderFactory.createTitledBorder("Expert Response:"));
    add(responseScroll, BorderLayout.CENTER);
  }


  private void submitQuestion() {

    String question = questionArea.getText().trim();
    if (question.isEmpty()) {
      JOptionPane.showMessageDialog(this,
          "Please enter a question.",
          "Input Required",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // Disable UI while processing
    submitButton.setEnabled(false);
    questionArea.setEnabled(false);
    responseArea.setText("Analyzing your health data and preparing a response...");

    // Use a background thread for API calls
    SwingWorker<String, Void> worker = new SwingWorker<>() {
      @Override
      protected String doInBackground() throws Exception {
        return getExpertResponse(question);
      }

      @Override
      protected void done() {
        try {
          String response = get();
          responseArea.setText(response);
        } catch (Exception e) {
          responseArea.setText("Error: " + e.getMessage());
        } finally {
          // Re-enable UI
          submitButton.setEnabled(true);
          questionArea.setEnabled(true);
        }
      }
    };

    worker.execute();
  }

  private String getExpertResponse(String question) {
    String topic = (String) topicSelector.getSelectedItem();
    String prompt = buildPrompt(question, topic);
    String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

    try {
      // Build JSON request
      JsonObject textPart = new JsonObject();
      textPart.addProperty("text", prompt);

      JsonArray partsArray = new JsonArray();
      partsArray.add(textPart);

      JsonObject content = new JsonObject();
      content.add("parts", partsArray);

      JsonArray contentsArray = new JsonArray();
      contentsArray.add(content);

      JsonObject requestBody = new JsonObject();
      requestBody.add("contents", contentsArray);

      // Send POST request
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(endpoint))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
          .build();

      HttpClient client = HttpClient.newHttpClient();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // Parse response
      String responseBody = response.body();
      try {
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

        // Extract text from response
        String responseText = jsonResponse
            .getAsJsonArray("candidates")
            .get(0).getAsJsonObject()
            .getAsJsonObject("content")
            .getAsJsonArray("parts")
            .get(0).getAsJsonObject()
            .get("text").getAsString();

        return responseText;
      } catch (Exception e) {
        return "Error parsing response: " + responseBody;
      }
    } catch (Exception e) {
      return "Error communicating with AI service: " + e.getMessage();
    }
  }

  private String buildPrompt(String question, String topic) {
    StringBuilder prompt = new StringBuilder();
    prompt.append("You are a health expert providing advice based on the following user health data and question.\n\n");

    // Add relevant health data based on topic
    if (topic.equals("Period Health (Female)")) {
      appendPeriodData(prompt);
    } else {// For general health, include summary of all data
      appendBasicHealthSummary(prompt);
    }

    prompt.append("\nUser Question: ").append(question).append("\n\n");
    prompt.append("Provide helpful, evidence-based health advice. Include specific recommendations based on the data provided. Format your response in an easy-to-read way with headings and bullet points where appropriate.");

    return prompt.toString();
  }

  private void appendHeartRateData(StringBuilder prompt) {
    prompt.append("HEART RATE DATA:\n");
    var heartRates = metricsManager.getAllHeartRates();
    if (heartRates.isEmpty()) {
      prompt.append("No heart rate data available.\n");
      return;
    }

    int count = 0;
    for (var entry : heartRates.entrySet()) {
      for (HeartRate hr : entry.getValue()) {
        prompt.append(hr.toString()).append("\n");
        count++;
        if (count >= 10) break; // Limit to 10 entries to keep prompt size reasonable
      }
      if (count >= 10) break;
    }
  }

  private void appendWeightData(StringBuilder prompt) {
    prompt.append("WEIGHT DATA:\n");
    var weights = metricsManager.getAllWeights();
    if (weights.isEmpty()) {
      prompt.append("No weight data available.\n");
      return;
    }

    for (var entry : weights.entrySet()) {
      prompt.append("Date: ").append(entry.getKey())
          .append(", Weight: ").append(entry.getValue().getWeightInUserUnits())
          .append(" ").append(entry.getValue().getUnit())
          .append("\n");
    }
  }

  private void appendStepsData(StringBuilder prompt) {
    prompt.append("STEPS DATA:\n");
    var stepsData = metricsManager.getAllSteps();
    if (stepsData.isEmpty()) {
      prompt.append("No steps data available.\n");
      return;
    }

    int count = 0;
    for (var entry : stepsData.entrySet()) {
      for (Steps steps : entry.getValue()) {
        prompt.append(steps.toString()).append("\n");
        count++;
        if (count >= 10) break;
      }
      if (count >= 10) break;
    }
  }

  private void appendPeriodData(StringBuilder prompt) {
    prompt.append("PERIOD LOG DATA:\n");
    var periodLogs = metricsManager.getPeriodLogs();
    if (periodLogs.isEmpty()) {
      prompt.append("No period log data available.\n");
      return;
    }

    for (PeriodLog log : periodLogs) {
      prompt.append(log.toString()).append("\n");
    }
  }

  private void appendBasicHealthSummary(StringBuilder prompt) {
    // Add basic user data
    try {
      UserProfile profile = UserDataStorage.getUserProfile(username);
      if (profile != null) {
        prompt.append("USER PROFILE:\n");
        prompt.append("Gender: ").append(profile.getGender()).append("\n");
        prompt.append("Age: ").append(profile.getAge()).append("\n");
      }
    } catch (Exception e) {
      // Ignore profile errors
    }

    // Add summary counts
    prompt.append("DATA SUMMARY:\n");
    appendWeightData(prompt);
    appendStepsData(prompt);
    appendHeartRateData(prompt);
    // Include most recent data points if available
    var weights = metricsManager.getAllWeights();
    if (!weights.isEmpty()) {
      var lastWeight = weights.entrySet().stream().reduce((a, b) -> a.getKey().isAfter(b.getKey()) ? a : b).orElse(null);
      if (lastWeight != null) {
        prompt.append("Latest weight: ").append(lastWeight.getValue().getWeightInUserUnits())
            .append(" ").append(lastWeight.getValue().getUnit())
            .append(" on ").append(lastWeight.getKey()).append("\n");
      }
    }
  }
}