import java.time.LocalTime;

/**
 * Represents a log entry specifically for heart rate data.
 * Extends the {@link LogEntry} class and includes the heart rate value.
 */
public class HeartRate extends LogEntry {
  private int heartRate;

  /**
   * Constructs a new {@code HeartRate} object with the specified tags, date, time, and heart rate.
   *
   * @param tags      The tags associated with this heart rate log entry (e.g., activity performed).
   * @param time      The time when the heart rate was recorded.
   * @param heartRate The recorded heart rate value.
   */
  public HeartRate(String tags, LocalTime time, int heartRate) {
    super(tags, time);
    if (validateHeartRate(heartRate)) {
      this.heartRate = heartRate;
    }
  }

  /**
   * Validates the heart rate value to ensure it falls within a reasonable physiological range (30-220 bpm).
   *
   * @param heartRate The heart rate value to validate.
   * @return true if valid, otherwise throws an exception.
   * @throws IllegalArgumentException If the heart rate is not within 30-220 bpm.
   */
  public static boolean validateHeartRate(int heartRate) {
    if (heartRate >= 30 && heartRate <= 220) {
      return true;
    }
    throw new IllegalArgumentException("Invalid Heart Rate Value Entered.");
  }

  public int getHeartRate() {
    return heartRate;
  }

  public void setHeartRate(int heartRate) {
    if (validateHeartRate(heartRate)) {
      this.heartRate = heartRate;
    }
  }

  @Override
  public String toString() {
    return "Time: " + getTime().format(Constants.timeFormatter) + "\n"
        + "Heart Rate: " + heartRate + "\n"
        + "Activity: " + getTags();
  }
}
