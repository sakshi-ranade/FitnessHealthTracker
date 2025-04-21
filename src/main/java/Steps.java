import java.time.LocalDate;

/**
 * Represents a daily step count entry for a user.
 * Steps are associated with a specific date and stored as an integer.
 * This class extends {@link LogEntry}.
 */

public class Steps extends LogEntry{
  private static final int MAX_VALID_STEPS = 50000;
  private static final double STEP_TO_KM_CONVERSION = 0.0007;
  private int steps;

  /**
   * Constructs a {@code Steps} entry for a given date.
   *
   * @param date  The date of the step count log.
   * @param steps The number of steps (must be between 1 and 50,000).
   * @throws IllegalArgumentException if step count is invalid.
   */

  //Constructor (tags and time are not tracked)
  public Steps(LocalDate date, int steps){
    super(null,date,null);
    validateSteps(steps);
    this.steps = steps;
  }

  //Getter and Setter for Steps

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
    validateSteps(steps);
    this.steps = steps;
  }

  /**
   * Validates the step count.
   * Steps must be between 1 and 50,000.
   *
   * @param steps Step count to validate.
   * @throws IllegalArgumentException if steps are negative or > 50000 (too high).
   */

  public void validateSteps(int steps) // method to check step count
  {
    if (steps > 0 && steps <= MAX_VALID_STEPS) {
      this.steps = steps;
    } else if (steps < 0) {
      throw new IllegalArgumentException("Steps cannot be negative!");
    } else if (steps > MAX_VALID_STEPS) {
      throw new IllegalArgumentException("Step count too high!");
    }
  }

  /**
   * Converts steps into an approximate distance in kilometers.
   * Assumes 1 step = 0.0008 kilometers.
   *
   * @return Equivalent distance in kilometers.
   */

  public double getDistanceInKm() {
    return Math.round(steps * STEP_TO_KM_CONVERSION * 10.0) / 10.0; //approximate avg value for women and men source: https://www.omnicalculator.com/sports/steps-to-km
  }

  /**
   * Returns a string representation of the step entry.
   *
   * @return A formatted string showing the date and number of steps.
   */

  @Override
  public String toString() {
    return "Date: " + super.getDate().format(Constants.dateFormatter) +" Steps: "
        + this.getSteps() + " Distance: " + this.getDistanceInKm();
  }
}
