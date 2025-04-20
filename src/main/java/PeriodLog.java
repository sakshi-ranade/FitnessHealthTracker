import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a period log entry, including the start and end date
 * and the level of menstrual flow.
 * Extends {@link LogEntry}.
 */

public class PeriodLog extends LogEntry{
  private LocalDate startDate;
  private LocalDate endDate;
  private String flowLevel;

  /**
   * Constructs a new PeriodLog with specified tags, start date, end date, and flow level.
   *
   * @param tags      Optional tag or label for the log.
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @param flowLevel The intensity of menstrual flow (e.g., "Light", "Moderate", "Heavy").
   */

  public PeriodLog(String tags, LocalDate startDate, LocalDate endDate, String flowLevel){
    super(tags, null); //time not needed
    if(endDate.isBefore(startDate)){
      throw new IllegalArgumentException("End date cannot be before start date.");
    }
    this.flowLevel = flowLevel;
    this.startDate = startDate;
    this.endDate = endDate;
  }
  //Getter and Setter for statDate
  /**
   * Returns the start date of the menstrual period.
   *
   * @return The start date.
   */
  public LocalDate getStartDate(){
    return startDate;
  }

  /**
   * Sets the start date of the menstrual period.
   *
   * @param startDate The end date to set.
   */

  public void setStartDate(LocalDate startDate){
    this.startDate = startDate;
  }

  //Getter and Setter for endDate

  /**
   * Returns the ending date of the menstrual period.
   *
   * @return The end date.
   */
  public LocalDate getEndDate(){
    return endDate;
  }

  /**
   * Sets the ending date of the menstrual period.
   *
   * @param endDate The end date to set.
   */

  public void setEndDate(LocalDate endDate){
    this.endDate = endDate;
  }

  //Getter and Setter for flowLevel

  /**
   * Returns the flow level of the menstrual period.
   *
   * @return The flow level (e.g., "light", "medium", "heavy").
   */

  public String getFlowLevel() {
    return flowLevel;
  }

  /**
   * Sets the flow level of the menstrual period.
   *
   * @param flowLevel The flow level to set (e.g., "light", "medium", "heavy").
   */

  public void setFlowLevel(String flowLevel) {
    this.flowLevel = flowLevel;
  }

  /**
   * Calculates the duration of the menstrual period in days.
   * Includes both the start and end dates in the duration.
   *
   * @return A string representation of period log
   */

  //Method to calculate periods duration
  public String periodDuration(){
    long daysDifference = ChronoUnit.DAYS.between(startDate, endDate) + 1;
    return "Periods lasted for: "+ daysDifference + " days.";
  }

  /**
   * Returns a string representation of the period log entry.
   * Includes the start date, end date, and the calculated duration.
   *
   * @return A formatted string describing the period log.
   */

  @Override
  public String toString() {
    return "Period Log" +
        "\nStarted on: " + getStartDate().format(dateFormatter) +
        "\nEnded on: " + getEndDate().format(dateFormatter) +
        "\n Duration: " + periodDuration();
  }
}

