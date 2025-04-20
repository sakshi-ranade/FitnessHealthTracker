import java.time.format.DateTimeFormatter;

public class Constants {
   static final String USER_DATA_FILE = "src/main/resources/users.txt";
   static final String USER_DIRECTORY = "src/main/resources/users/";
   static final String XOR_KEY = "FITNESS"; // Simple encryption key
   /**
    * A static final DateTimeFormatter for formatting the date in "MM-dd-yyyy" pattern.
    */
   protected static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
   /**
    * A static final DateTimeFormatter for formatting the time in "HH:mm:ss" pattern.
    */
   protected static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

}
