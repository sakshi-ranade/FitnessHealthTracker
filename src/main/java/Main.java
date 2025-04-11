import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

  public static void main(String[] args) {
    try {
      UserProfile xyz = new UserProfile("XYZABC", "XYZ", 24, 34, UserProfile.Gender.MALE);
      xyz.ageCheck();
      HeartRate first = new HeartRate("walk", LocalDate.now(), LocalTime.now(),9);
      first.validateHeartRate();
    }
    catch (IllegalArgumentException e){
      System.out.println(e.getMessage());
    }


  }

}
