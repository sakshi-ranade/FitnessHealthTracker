import java.time.LocalDate;
import java.time.LocalTime;

public class Weight extends LogEntry{
  private double weight;

  public Weight(String tags, LocalDate date, LocalTime time, double weight){
    super(tags,date,time);
    this.weight = weight;
  }

  //Getter and Setter for weight

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public void validateWeight(){
    if (weight <= 0){
      throw new IllegalArgumentException("Weight cannot be negative!");
    }
  }

  @Override
  public String toString() {
    return "Date: " + super.getDate().format(dateFormatter) + " Weighed at: " + super.getTime().format(timeFormatter) + " Weight: "
        + this.getWeight();
  }
}
