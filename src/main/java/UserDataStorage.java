import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class UserDataStorage {


  /**
   * Saves a new user's profile to the main users file.
   * Only saves if the username doesn't already exist.
   *
   * @param profile The UserProfile to save.
   * @return true if user was saved, false if user already exists
   * @throws IOException If there's an issue writing to the file.
   */
  public static boolean saveUserProfile(UserProfile profile) throws IOException {
    // Check if user already exists
    if (isUserExists(profile.getUserName())) {
      System.out.println("User already exists.");
      return false;
    }

    // Encrypt the password using XOR
    String encryptedPassword = xorEncrypt(profile.getPassword(), Constants.XOR_KEY);

    // Format the user data as a CSV line
    String line = String.format("%s|%s|%s|%s|%s",
        profile.getName(),
        profile.getUserName(),
        profile.getGender(),
        encryptedPassword,
        profile.getDateOfBirth());

    // Write to the users file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.USER_DATA_FILE, true))) {
      writer.write(line);
      writer.newLine();
    }

    return true;
  }

  /**
   * Checks if a user with the given username already exists.
   *
   * @param username The username to check.
   * @return true if user exists, false otherwise.
   * @throws IOException If reading the file fails.
   */
  public static boolean isUserExists(String username) throws IOException {
    File file = new File(Constants.USER_DATA_FILE);
    if (!file.exists()) {
      return false;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(Constants.USER_DATA_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length >= 2 && parts[1].equals(username)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Authenticates a user based on username and password.
   *
   * @param username The username to check.
   * @param password The password to verify.
   * @return true if authentication successful, false otherwise.
   * @throws IOException If reading the file fails.
   */
  public static boolean authenticateUser(String username, String password) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(Constants.USER_DATA_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length >= 4 && parts[1].equals(username)) {
          String decryptedPassword = xorEncrypt(parts[3], Constants.XOR_KEY);
          return decryptedPassword.equals(password);
        }
      }
    }
    return false;
  }

  /**
   * Retrieves a UserProfile from the file based on the username.
   *
   * @param username The username of the user to retrieve.
   * @return The UserProfile object, or null if not found.
   * @throws IOException If reading the file fails.
   */
  public static UserProfile getUserProfile(String username) throws IOException {
    File file = new File(Constants.USER_DATA_FILE);
    if (!file.exists()) {
      return null;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(Constants.USER_DATA_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length >= 4 && parts[1].equals(username)) {
          String decryptedPassword = xorEncrypt(parts[3], Constants.XOR_KEY);
          LocalDate dob = LocalDate.parse(parts[4]);
          return new UserProfile(parts[0], parts[1], decryptedPassword, Gender.valueOf(parts[2]), dob);
        }
      }
    }
    return null;
  }

  /**
   * Encrypts or decrypts the input using XOR cipher with the given key.
   * This is a simple encryption which works both ways (encrypt/decrypt).
   *
   * @param input The input string.
   * @param key   The encryption key.
   * @return The result string.
   */
  private static String xorEncrypt(String input, String key) {
    char[] inputChars = input.toCharArray();
    char[] keyChars = key.toCharArray();
    char[] result = new char[inputChars.length];

    for (int i = 0; i < inputChars.length; i++) {
      result[i] = (char) (inputChars[i] ^ keyChars[i % keyChars.length]);
    }

    return new String(result);
  }

  /**
   * Determines if a user is female based on their profile.
   *
   * @param username The username to check.
   * @return true if user is female, false otherwise or if user not found.
   * @throws IOException If reading the file fails.
   */
  public static boolean isUserFemale(String username) throws IOException {
    UserProfile profile = getUserProfile(username);
    if (profile != null) {
      return profile.getGender() == Gender.FEMALE;
    }
    return false;
  }
}