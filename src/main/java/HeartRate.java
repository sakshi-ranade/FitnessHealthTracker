import java.time.LocalDate;
import java.time.LocalTime;

public class HeartRate extends LogEntry {
  private int heartRate;

  public HeartRate(String tags, LocalDate date, LocalTime time, int heartRate){
    super(tags, date, time);
    this.heartRate = heartRate;
  }

  public void validateHeartRate() // method to check heart rate values
  {
    if (heartRate > 30 && heartRate <221){
      this.heartRate = heartRate;
    }
    throw new IllegalArgumentException("Invalid Heart Rate Value Entered.");
  }

  //Getter and Setter for Heart Rate
  public int getHeartRate() {
    return heartRate;
  }

  public void setHeartRate(int heartRate) {
    this.heartRate = heartRate;
  }

  @Override
  public String toString() {
    return "Date: " + super.getDate().format(dateFormatter) + " Time: " + super.getTime().format(timeFormatter) + " Heart Rate: "
        + this.getHeartRate() + " Activity: " + super.getTags();

  }
}
