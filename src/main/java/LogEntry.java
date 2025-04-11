import java.time.LocalDate; //import the LocalDate class
import java.time.LocalTime; //import the LocalTime class
import java.time.format.DateTimeFormatter;

public abstract class LogEntry {
  protected String tags; //to function like a comment section, example: Cycling can be a tag added after recoding BPM
  protected LocalDate date;
  protected LocalTime time;
  protected static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
  protected static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  //Constructor
  public LogEntry(String tags, LocalDate date, LocalTime time) {
    this.tags = tags;
    this.date = date;
    this.time = time;
  }

  //Getter and Setter for tags
  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  //Abstract method to display data
  public abstract String toString();

}
