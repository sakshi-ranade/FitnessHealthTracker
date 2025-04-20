import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainTestClass {
  public static void main(String[] args) {
    // 1. Create a new UserProfile
    UserProfile newUser = new UserProfile(
        "evej",
        "evej",
        "securePass",
        Gender.FEMALE,
        LocalDate.of(1993, 11, 20)
    );

    // 2. Save the new UserProfile
    try {
      if (UserDataStorage.saveUserProfile(newUser)) {
        System.out.println("User profile created successfully for: " + newUser.getUserName());

        // 3. Create a DailyMetricsManager for the new user
        DailyMetricsManager metricsManager = new DailyMetricsManager(newUser.getUserName());

        // --- Adding Period Log Entries ---
        System.out.println("\n--- Adding Period Log Entries ---");
        PeriodLog periodLog1 = new PeriodLog("initial flow", LocalDate.of(2025, 4, 10), LocalDate.of(2025, 4, 15), "light");
        if (metricsManager.addPeriodLog(periodLog1)) {
          System.out.println("Period log entry added: " + periodLog1.getStartDate() + " to " + periodLog1.getEndDate());
        }
        PeriodLog periodLog2 = new PeriodLog("heavier flow", LocalDate.of(2025, 5, 8), LocalDate.of(2025, 5, 13), "medium");
        if (metricsManager.addPeriodLog(periodLog2)) {
          System.out.println("Period log entry added: " + periodLog2.getStartDate() + " to " + periodLog2.getEndDate());
        }

        // --- Adding Weight Entries for 5 Days ---
        System.out.println("\n--- Adding Weight Entries ---");
        for (int i = 0; i < 5; i++) {
          LocalDate date = LocalDate.now().minusDays(i);
          Weight weight = new Weight(70.5 - (0.2 * i), "kg");
          if (metricsManager.saveWeight(date, weight)) {
            System.out.println("Weight added for " + date + ": " + weight.getWeightInUserUnits() + " " + weight.getUnit());
          }
        }

        // --- Adding Multiple Step Entries for Each of 3 Days ---
        System.out.println("\n--- Adding Step Entries ---");
        for (int i = 0; i < 3; i++) {
          LocalDate date = LocalDate.now().minusDays(i);
          for (int j = 0; j < 3; j++) {
            LocalTime time = LocalTime.of(8 + j * 4, 15 * j, 0);
            int steps = 500 + i * 100 + j * 200;
            Steps step = new Steps(time, steps);
            if (metricsManager.addSteps(date, step)) {
              System.out.println("Steps added for " + date + " at " + time + ": " + steps);
            }
          }
        }

        // --- Adding Multiple Heart Rate Entries for Each of 2 Days ---
        System.out.println("\n--- Adding Heart Rate Entries ---");
        for (int i = 0; i < 2; i++) {
          LocalDate date = LocalDate.now().minusDays(i);
          for (int j = 0; j < 4; j++) {
            LocalTime time = LocalTime.of(9 + j * 3, 0 + j * 15, 0);
            String tag = (j % 2 == 0) ? "resting" : "active";
            int heartRate = 65 + i * 5 + j * 10;
            HeartRate hr = new HeartRate(tag, time, heartRate);
            if (metricsManager.addHeartRate(date, hr)) {
              System.out.println("Heart rate added for " + date + " at " + time + " (" + tag + "): " + heartRate + " bpm");
            }
          }
        }

        // --- Fetching and Printing Data ---
        System.out.println("\n--- Fetching and Printing Data ---");

        // Fetch Period Logs
        List<PeriodLog> periodLogs = metricsManager.getPeriodLogs();
        System.out.println("\n--- Period Logs ---");
        periodLogs.forEach(log -> System.out.println("Start: " + log.getStartDate() + ", End: " + log.getEndDate() + ", Flow: " + log.getFlowLevel() + ", Tags: " + log.getTags()));

        // Fetch Weight for Today
        LocalDate today = LocalDate.now();
        Optional<Weight> weightToday = metricsManager.getWeight(today);
        System.out.println("\n--- Weight for " + today + " ---");
        weightToday.ifPresentOrElse(
            weight -> System.out.println("Weight: " + weight.getWeightInUserUnits() + " " + weight.getUnit()),
            () -> System.out.println("No weight recorded for " + today)
        );

        // Fetch All Weights
        Map<LocalDate, Weight> allWeights = metricsManager.getAllWeights();
        System.out.println("\n--- All Recorded Weights ---");
        allWeights.forEach((date, weight) -> System.out.println(date + ": " + weight.getWeightInUserUnits() + " " + weight.getUnit()));

        // Fetch Steps for Yesterday
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Steps> stepsYesterday = metricsManager.getSteps(yesterday);
        System.out.println("\n--- Steps for " + yesterday + " ---");
        stepsYesterday.forEach(step -> System.out.println("Time: " + step.getTime() + ", Steps: " + step.getSteps()));

        // Fetch All Steps
        Map<LocalDate, List<Steps>> allSteps = metricsManager.getAllSteps();
        System.out.println("\n--- All Recorded Steps ---");
        allSteps.forEach((date, stepsList) -> {
          System.out.println(date + ":");
          stepsList.forEach(step -> System.out.println("  Time: " + step.getTime() + ", Steps: " + step.getSteps()));
        });

        // Fetch Heart Rates for Today
        List<HeartRate> heartRatesToday = metricsManager.getHeartRates(today);
        System.out.println("\n--- Heart Rates for " + today + " ---");
        heartRatesToday.forEach(hr -> System.out.println("Time: " + hr.getTime() + ", Tag: " + hr.getTags() + ", Rate: " + hr.getHeartRate() + " bpm"));

        // Fetch All Heart Rates
        Map<LocalDate, List<HeartRate>> allHeartRates = metricsManager.getAllHeartRates();
        System.out.println("\n--- All Recorded Heart Rates ---");
        allHeartRates.forEach((date, hrList) -> {
          System.out.println(date + ":");
          hrList.forEach(hr -> System.out.println("  Time: " + hr.getTime() + ", Tag: " + hr.getTags() + ", Rate: " + hr.getHeartRate() + " bpm"));
        });

      } else {
        System.out.println("Failed to create user profile for: " + newUser.getUserName());
      }
    } catch (IOException e) {
      System.err.println("Error during user profile creation or saving: " + e.getMessage());
    }
  }
}