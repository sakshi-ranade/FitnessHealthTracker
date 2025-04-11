import java.time.LocalDate;
import java.time.LocalTime;

public class Steps extends LogEntry{
  private int steps;

  public Steps(String tags, LocalDate date, LocalTime time, int steps){
    super(tags,date,time);
    this.steps = steps;
  }

  //Getter and Setter for Steps
  public int getSteps() {
    return steps;
  }

  public void setSteps(int steps) {
    this.steps = steps;
  }

  public void validateSteps(int steps) // method to check step count
  {
    if (steps > 0 || steps <= 50000)
    {
      this.steps = steps;
    }
    else if (steps < 0)
    {
      throw new IllegalArgumentException("Steps cannot be negative!.");
    }
    else if (steps > 50000)
    {
      throw new IllegalArgumentException("Step count too high!.");
    }
  }

  @Override
  public String toString() {
    return "Date: " + super.getDate().format(dateFormatter) + " Time: " + super.getTime().format(timeFormatter) + " Steps: "
        + this.getSteps();
  }
}
