import java.time.LocalDate;// Import the LocalDate class for handling dates
import java.time.LocalTime; // Import the LocalTime class for handling time

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
   * @param date      The date when the heart rate was recorded.
   * @param time      The time when the heart rate was recorded.
   * @param heartRate The recorded heart rate value.
   */

  //Constructor for the HeartRate class
  public HeartRate(String tags, LocalDate date, LocalTime time, int heartRate){
    // Call the constructor of the superclass (LogEntry)
    super(tags, date, time);
    // Initialize the heartRate for this HeartRate object
    this.heartRate = heartRate;
  }

  /**
   * Validates the heart rate value to ensure it falls within a reasonable physiological range (30-220 bpm).
   * If the heart rate is outside this range, an {@link IllegalArgumentException} is thrown.
   *
   * @throws IllegalArgumentException If the provided heart rate value is less than or equal to 30 or greater than or equal to 221.
   */

  // Method to validate the heart rate value
  public void validateHeartRate() // method to check heart rate values
  {
    // Check if the heart rate is within a (normal) valid range (30 to 220 inclusive)
    if (heartRate > 30 && heartRate <221){
      // If the heart rate is valid, set the heartRate for this object
      this.heartRate = heartRate;
      return;
    }
    // If the heart rate is not within the valid range, throw an IllegalArgumentException
    throw new IllegalArgumentException("Invalid Heart Rate Value Entered.");
  }


  //Getter and Setter for Heart Rate

  /**
   * Returns the recorded heart rate value.
   *
   * @return The heart rate in beats per minute.
   */

  public int getHeartRate() {
    return heartRate;
  }

  /**
   * Sets the heart rate value for this log entry.
   *
   * @param heartRate The new heart rate value to set.
   */

  public void setHeartRate(int heartRate) {
    this.heartRate = heartRate;
  }

  /**
   * Returns a string representation of the {@code HeartRate} object.
   * The string includes the date, time, heart rate value, and associated tags (activity).
   * The date and time are formatted according to the patterns defined in the superclass.
   *
   * @return A formatted string representing the heart rate log entry.
   */

  //Override the toString method to provide a custom string representation of the HeartRate object
  @Override
  public String toString() {
    // Format the date and time using the formatters defined in the superclass (LogEntry)
    return "Date: " + super.getDate().format(Constants.dateFormatter) + ", Time: " + super.getTime().format(Constants.timeFormatter) + ", Heart Rate: "
        + this.getHeartRate() + ", Activity: " + super.getTags();

  }
}
