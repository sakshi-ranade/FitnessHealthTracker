import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PeriodLog extends LogEntry{
  private LocalDate endDate;
  private String flowLevel;

  public PeriodLog(String tags, LocalDate startDate, LocalDate endDate, String flowLevel){
    super(tags, startDate, null);
    this.flowLevel = flowLevel;
  }

  //Getter and Setter for endDate
  public LocalDate getEndDate(){
    return endDate;
  }

  public void setEndDate(LocalDate endDate){
    this.endDate = endDate;
  }

  //Getter and Setter for flowLevel
  public String getFlowLevel() {
    return flowLevel;
  }

  public void setFlowLevel(String flowLevel) {
    this.flowLevel = flowLevel;
  }

  //Method to calculate periods duration
  public String periodDuration(){
    long daysDifference = ChronoUnit.DAYS.between(getDate(), endDate) + 1;
    return "Periods lasted for: "+ daysDifference + " days.";
  }

  @Override
  public String toString() {
    return "Period Log" + "\nStarted on: " + getDate().format(dateFormatter) +
        "\nEnded on: " + getEndDate().format(dateFormatter) + "\n Duration: " + periodDuration();
  }
}
