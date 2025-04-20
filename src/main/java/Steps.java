import java.time.LocalTime;

/**
 * Represents a daily step count entry for a user.
 * Steps are associated with a specific date and time and stored as an integer.
 * This class extends {@link LogEntry}.
 */
public class Steps extends LogEntry {
  private int steps;

  /**
   * Constructs a {@code Steps} entry with date, time, and step count.
   *
   * @param time  The time of the step count log.
   * @param steps The number of steps (must be between 1 and 50,000).
   * @throws IllegalArgumentException if step count is invalid.
   */
  public Steps(LocalTime time, int steps) {
    super(null, time);
    if (validateSteps(steps)) {
      this.steps = steps;
    }
  }

  /**
   * Returns the number of steps logged.
   *
   * @return Number of steps.
   */
  public int getSteps() {
    return steps;
  }

  /**
   * Sets the step count after validation.
   *
   * @param steps The number of steps to update.
   * @throws IllegalArgumentException if step count is invalid.
   */
  public void setSteps(int steps) {
    if (validateSteps(steps)) {
      this.steps = steps;
    }
  }

  /**
   * Validates the step count.
   * Steps must be between 1 and 50,000.
   *
   * @param steps Step count to validate.
   * @return true if valid, otherwise throws IllegalArgumentException.
   * @throws IllegalArgumentException if steps are invalid.
   */
  private boolean validateSteps(int steps) {
    if (steps <= 0) {
      throw new IllegalArgumentException("Steps cannot be negative!");
    }
    if (steps > 50000) {
      throw new IllegalArgumentException("Step count too high!");
    }
    return true;
  }

  /**
   * Converts steps into an approximate distance in kilometers.
   * Assumes 1 step = 0.0007 kilometers.
   *
   * @return Equivalent distance in kilometers.
   */
  public double getDistanceInKm() {
    return steps * 0.0007;
  }

  /**
   * Returns a string representation of the step entry.
   *
   * @return A formatted string showing the date, time, and number of steps.
   */
  @Override
  public String toString() {
    return "Time: " + getFormattedTime()
        + " Steps: " + steps;
  }
}
