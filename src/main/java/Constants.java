import java.time.format.DateTimeFormatter;

public class Constants {
   /**
    * The file path where user data is stored.
    */
   public static final String USER_DATA_FILE = "src/main/resources/users.txt";

   /**
    * The directory where individual user-related files will be stored.
    */
   public static final String USER_DIRECTORY = "src/main/resources/users/";
   /**
    * A simple key used for XOR encryption/decryption.
    */
   public static final String XOR_KEY = "secret";
   /**
    * A static final DateTimeFormatter for formatting the date in "MM-dd-yyyy" pattern.
    */
   public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
   /**
    * A static final DateTimeFormatter for formatting the time in "HH:mm:ss" pattern.
    */
   public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

   /**
    * Ref: <a href="https://www.baeldung.com/java-private-constructors">Reference</a>
    * Private constructor to prevent class instantiation
    */
   private Constants(){

   }
}
