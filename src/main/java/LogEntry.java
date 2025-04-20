import java.time.LocalTime; // Import the LocalTime class for handling times
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class for formatting dates and times

/**
 * Abstract class representing a generic log entry.
 * It stores common information such as tags, date, and time of the log.
 * Subclasses will extend this class to represent specific types of log entries.
 */
public abstract class LogEntry {
  /**
   * Tags associated with the log entry, functioning like a comment section.
   * Example: "Cycling" can be a tag added after recording BPM.
   */
  protected String tags;
  /**
   * The time of the log entry.
   */
  protected LocalTime time;
  /**
   * A static final DateTimeFormatter for formatting the date in "MM-dd-yyyy" pattern.
   */
  protected static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
  /**
   * A static final DateTimeFormatter for formatting the time in "HH:mm:ss" pattern.
   */
  protected static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  /**
   * Constructs a new {@code LogEntry} with the specified tags, date, and time.
   *
   * @param tags The tags associated with this log entry.
   * @param time The time of this log entry.
   */
  public LogEntry(String tags, LocalTime time) {
    this.tags = tags;
    this.time = time;
  }

  /**
   * Returns the tags associated with this log entry.
   *
   * @return The tags of this log entry.
   */
  public String getTags() {
    return tags;
  }

  /**
   * Sets the tags for this log entry.
   *
   * @param tags The new tags to associate with this log entry.
   */
  public void setTags(String tags) {
    this.tags = tags;
  }

  /**
   * Returns the time of this log entry.
   *
   * @return The time of this log entry.
   */
  public LocalTime getTime() {
    return time;
  }

  /**
   * Returns the time of this log entry formatted as "HH:mm:ss".
   *
   * @return The formatted time string.
   */
  public String getFormattedTime() {
    return time.format(timeFormatter);
  }

  /**
   * Sets the time for this log entry.
   *
   * @param time The new time for this log entry.
   */
  public void setTime(LocalTime time) {
    this.time = time;
  }

  /**
   * Abstract method to display the specific data of the log entry.
   * Subclasses must implement this method to provide their own representation.
   *
   * @return A string representation of the log entry's data.
   */
  @Override
  public abstract String toString();

}