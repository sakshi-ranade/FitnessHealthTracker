import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Manages health tracking data files for a user.
 * Creates and maintains separate files for daily metrics, steps, heart rate, and period logs.
 */
public class DailyMetricsManager {
  private static final String DAILY_METRICS_FILE = "daily_metrics.txt";
  private static final String STEPS_FILE = "steps.txt";
  private static final String HEART_RATE_FILE = "heart_rate.txt";
  private static final String PERIOD_LOG_FILE = "period_log.txt";
  private final String userFolder;


  /**
   * Constructs a new HealthDataManager for the specified user.
   * Creates the user's folder if it doesn't exist.
   *
   * @param username The username of the user.
   */
  public DailyMetricsManager(String username) {
    this.userFolder = Constants.USER_DIRECTORY + username + "/";
    createUserFolder();
  }

  /**
   * Creates the user's folder and necessary files if they don't exist.
   */
  private void createUserFolder() {
    try {
      Files.createDirectories(Paths.get(userFolder));

      // Create files if they don't exist
      createFileIfNotExists(DAILY_METRICS_FILE);
      createFileIfNotExists(STEPS_FILE);
      createFileIfNotExists(HEART_RATE_FILE);
      createFileIfNotExists(PERIOD_LOG_FILE);
    } catch (IOException e) {
      System.err.println("Error creating user folder: " + e.getMessage());
    }
  }

  /**
   * Creates a file within the user's folder if it doesn't exist.
   *
   * @param fileName Name of the file to create.
   * @throws IOException If an I/O error occurs.
   */
  private void createFileIfNotExists(String fileName) throws IOException {
    Path filePath = Paths.get(userFolder + fileName);
    if (!Files.exists(filePath)) {
      Files.createFile(filePath);

      // Add headers to the files
      BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
      switch (fileName) {
        case DAILY_METRICS_FILE:
          writer.write("date,weight,unit");
          break;
        case STEPS_FILE:
          writer.write("date,time,steps");
          break;
        case HEART_RATE_FILE:
          writer.write("date,time,tag,heartRate");
          break;
        case PERIOD_LOG_FILE:
          writer.write("startDate,endDate,flowLevel,tags");
          break;
      }
      writer.newLine();
      writer.close();
    }
  }

  /**
   * Saves or updates weight in the daily metrics file.
   * Replaces the existing weight entry for the day if one exists.
   *
   * @param date  The date of the weight entry.
   * @param weight The Weight object to save.
   * @return true if successful, false otherwise.
   */
  public boolean saveWeight(LocalDate date, Weight weight) {
    try {
      // Read existing data
      Path metricsFilePath = Paths.get(userFolder + DAILY_METRICS_FILE);
      List<String> lines = Files.readAllLines(metricsFilePath);
      boolean updated = false;

      // Check for existing entry on this date
      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 1 && parts[0].equals(date.format(Constants.dateFormatter))) {
          // Update existing entry
          lines.set(i, String.format("%s,%.2f,%s",
              date.format(Constants.dateFormatter),
              weight.getWeightInUserUnits(),
              weight.getUnit()));
          updated = true;
          break;
        }
      }

      // Add new entry if not updated
      if (!updated) {
        lines.add(String.format("%s,%.2f,%s",
            date.format(Constants.dateFormatter),
            weight.getWeightInUserUnits(),
            weight.getUnit()));
      }

      // Write back to file
      Files.write(metricsFilePath, lines);
      return true;
    } catch (IOException e) {
      System.err.println("Error saving weight: " + e.getMessage());
      return false;
    }
  }

  /**
   * Gets weight data for a specific date.
   *
   * @param date The date to get weight data for.
   * @return Optional containing Weight object if found, empty otherwise.
   */
  public Optional<Weight> getWeight(LocalDate date) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(userFolder + DAILY_METRICS_FILE));

      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 3 && parts[0].equals(date.format(Constants.dateFormatter))) {
          double weightValue = Double.parseDouble(parts[1]);
          String unit = parts[2];
          return Optional.of(new Weight(weightValue, unit));
        }
      }

      return Optional.empty();
    } catch (IOException e) {
      System.err.println("Error getting weight: " + e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Adds a new steps entry to the steps file.
   *
   * @param date The date of the steps entry.
   * @param steps The Steps object to save.
   * @return true if successful, false otherwise.
   */
  public boolean addSteps(LocalDate date, Steps steps) {
    try {
      // Append new entry
      String entry = String.format("%s,%s,%d",
          date.format(Constants.dateFormatter),
          steps.getTime().format(Constants.timeFormatter),
          steps.getSteps());

      Files.write(Paths.get(userFolder + STEPS_FILE),
          Collections.singletonList(entry),
          StandardOpenOption.APPEND);
      return true;
    } catch (IOException e) {
      System.err.println("Error adding steps: " + e.getMessage());
      return false;
    }
  }

  /**
   * Gets all steps entries for a specific date.
   *
   * @param date The date to get steps for.
   * @return List of Steps objects for the date.
   */
  public List<Steps> getSteps(LocalDate date) {
    List<Steps> stepsList = new ArrayList<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(userFolder + STEPS_FILE));

      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 3 && parts[0].equals(date.format(Constants.dateFormatter))) {
          LocalTime time = LocalTime.parse(parts[1], Constants.timeFormatter);
          int stepsCount = Integer.parseInt(parts[2]);
          stepsList.add(new Steps(time, stepsCount));
        }
      }
    } catch (IOException e) {
      System.err.println("Error getting steps: " + e.getMessage());
    }

    return stepsList;
  }

  /**
   * Adds a new heart rate entry to the heart rate file.
   *
   * @param date The date of the heart rate entry.
   * @param heartRate The HeartRate object to save.
   * @return true if successful, false otherwise.
   */
  public boolean addHeartRate(LocalDate date, HeartRate heartRate) {
    try {
      // Append new entry
      String entry = String.format("%s,%s,%s,%d",
          date.format(Constants.dateFormatter),
          heartRate.getTime().format(Constants.timeFormatter),
          heartRate.getTags(),
          heartRate.getHeartRate());

      Files.write(Paths.get(userFolder + HEART_RATE_FILE),
          Collections.singletonList(entry),
          StandardOpenOption.APPEND);
      return true;
    } catch (IOException e) {
      System.err.println("Error adding heart rate: " + e.getMessage());
      return false;
    }
  }

  /**
   * Gets all heart rate entries for a specific date.
   *
   * @param date The date to get heart rate data for.
   * @return List of HeartRate objects for the date.
   */
  public List<HeartRate> getHeartRates(LocalDate date) {
    List<HeartRate> heartRates = new ArrayList<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(userFolder + HEART_RATE_FILE));

      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 4 && parts[0].equals(date.format(Constants.dateFormatter))) {
          LocalTime time = LocalTime.parse(parts[1], Constants.timeFormatter);
          String tags = parts[2];
          int heartRateValue = Integer.parseInt(parts[3]);
          heartRates.add(new HeartRate(tags, time, heartRateValue));
        }
      }
    } catch (IOException e) {
      System.err.println("Error getting heart rates: " + e.getMessage());
    }

    return heartRates;
  }

  /**
   * Adds a new period log entry to the period log file.
   *
   * @param periodLog The PeriodLog object to save.
   * @return true if successful, false otherwise.
   */
  public boolean addPeriodLog(PeriodLog periodLog) {
    try {
      // Append new entry
      String entry = String.format("%s,%s,%s,%s",
          periodLog.getStartDate().format(Constants.dateFormatter),
          periodLog.getEndDate().format(Constants.dateFormatter),
          periodLog.getFlowLevel(),
          periodLog.getTags());

      Files.write(Paths.get(userFolder + PERIOD_LOG_FILE),
          Collections.singletonList(entry),
          StandardOpenOption.APPEND);
      return true;
    } catch (IOException e) {
      System.err.println("Error adding period log: " + e.getMessage());
      return false;
    }
  }

  /**
   * Gets all period log entries.
   *
   * @return List of PeriodLog objects.
   */
  public List<PeriodLog> getPeriodLogs() {
    List<PeriodLog> periodLogs = new ArrayList<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(userFolder + PERIOD_LOG_FILE));

      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 4) {
          LocalDate startDate = LocalDate.parse(parts[0], Constants.dateFormatter);
          LocalDate endDate = LocalDate.parse(parts[1], Constants.dateFormatter);
          String flowLevel = parts[2];
          String tags = parts[3];

          periodLogs.add(new PeriodLog(tags, startDate, endDate, flowLevel));
        }
      }
    } catch (IOException e) {
      System.err.println("Error getting period logs: " + e.getMessage());
    }

    return periodLogs;
  }

  /**
   * Gets period logs for a specific month.
   *
   * @param year The year.
   * @param month The month (1-12).
   * @return List of PeriodLog objects for the specified month.
   */
  public List<PeriodLog> getPeriodLogsByMonth(int year, int month) {
    List<PeriodLog> allLogs = getPeriodLogs();
    List<PeriodLog> filteredLogs = new ArrayList<>();

    for (PeriodLog log : allLogs) {
      LocalDate startDate = log.getStartDate();
      if (startDate.getYear() == year && startDate.getMonthValue() == month) {
        filteredLogs.add(log);
      }
    }

    return filteredLogs;
  }

  /**
   * Gets all weight entries.
   *
   * @return Map with LocalDate keys and Weight values.
   */
  public Map<LocalDate, Weight> getAllWeights() {
    Map<LocalDate, Weight> weights = new HashMap<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(userFolder + DAILY_METRICS_FILE));

      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 3) {
          LocalDate date = LocalDate.parse(parts[0], Constants.dateFormatter);
          double weightValue = Double.parseDouble(parts[1]);
          String unit = parts[2];

          weights.put(date, new Weight(weightValue, unit));
        }
      }
    } catch (IOException e) {
      System.err.println("Error getting all weights: " + e.getMessage());
    }

    return weights;
  }

  /**
   * Gets all steps entries.
   *
   * @return Map with LocalDate keys and Lists of Steps values.
   */
  public Map<LocalDate, List<Steps>> getAllSteps() {
    Map<LocalDate, List<Steps>> allSteps = new HashMap<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(userFolder + STEPS_FILE));

      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 3) {
          LocalDate date = LocalDate.parse(parts[0], Constants.dateFormatter);
          LocalTime time = LocalTime.parse(parts[1], Constants.timeFormatter);
          int stepsCount = Integer.parseInt(parts[2]);

          Steps step = new Steps(time, stepsCount);

          if (!allSteps.containsKey(date)) {
            allSteps.put(date, new ArrayList<>());
          }
          allSteps.get(date).add(step);
        }
      }
    } catch (IOException e) {
      System.err.println("Error getting all steps: " + e.getMessage());
    }

    return allSteps;
  }

  /**
   * Gets all heart rate entries.
   *
   * @return Map with LocalDate keys and Lists of HeartRate values.
   */
  public Map<LocalDate, List<HeartRate>> getAllHeartRates() {
    Map<LocalDate, List<HeartRate>> allHeartRates = new HashMap<>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(userFolder + HEART_RATE_FILE));

      for (int i = 1; i < lines.size(); i++) { // Skip header
        String line = lines.get(i);
        String[] parts = line.split(",");

        if (parts.length >= 4) {
          LocalDate date = LocalDate.parse(parts[0], Constants.dateFormatter);
          LocalTime time = LocalTime.parse(parts[1], Constants.timeFormatter);
          String tags = parts[2];
          int heartRateValue = Integer.parseInt(parts[3]);

          HeartRate heartRate = new HeartRate(tags, time, heartRateValue);

          if (!allHeartRates.containsKey(date)) {
            allHeartRates.put(date, new ArrayList<>());
          }
          allHeartRates.get(date).add(heartRate);
        }
      }
    } catch (IOException e) {
      System.err.println("Error getting all heart rates: " + e.getMessage());
    }

    return allHeartRates;
  }
}