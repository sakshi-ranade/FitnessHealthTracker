import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link UserProfile} class.
 * This class contains unit tests to validate the behavior of
 * date of birth handling, leap year logic, future date validation,
 * daily log file path generation, and string representation of the user profile.
 */

public class UserProfileTest {

  /**
   * Test that a valid date of birth correctly initializes the UserProfile.
   * Ensures the date is stored and age string contains "Years".
   */

  @Test
  public void testValidDOB(){
    LocalDate dob = LocalDate.of(1990, 10, 25);
    UserProfile user = new UserProfile("Alice", "alice123", "ABCxyz123", Gender.FEMALE, dob);
    // Check that DOB is set correctly
    assertEquals(dob, user.getDateOfBirth());
    // Check that the age string includes "Years"
    assertTrue(user.getAge().contains("Years"));
  }

  /**
   * Test that a leap year date (Feb 29, 1992) is handled correctly.
   * Validates that UserProfile accepts a valid leap day.
   */

  @Test
  public void testLeapYear(){
    LocalDate leapDOB = LocalDate.of(1992, 2,29);
    UserProfile user = new UserProfile("Bob", "bob123", "XYZabc123", Gender.MALE, leapDOB);
    // Ensure the leap year date is accepted
    assertEquals(leapDOB, user.getDateOfBirth());
  }

  /**
   * Test that an invalid date (Feb 29 on a non-leap year) throws an exception.
   * This test verifies that Java's LocalDate class prevents invalid dates.
   */

  @Test
  public void testNonLeapYear() {
    assertThrows(DateTimeException.class, () -> { // This line itself will throw the exception
      LocalDate nonLeapDate = LocalDate.of(2023, 2, 29);

      // Won't even reach this point
      new UserProfile("Test", "user", "pass", Gender.MALE, nonLeapDate);
    });
  }

  /**
   * Test that setting a future date of birth throws an IllegalArgumentException.
   * The UserProfile constructor is expected to reject future DOBs.
   */

  @Test
  public void testFutureDOB(){
    LocalDate futureDOB = LocalDate.now().plusDays(5);
    // Expect an exception when trying to set a future DOB
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new UserProfile("Mon", "mon123", "abcXYZ1234", Gender.MALE, futureDOB));
    // Ensure the exception message is correct
    assertEquals("Date of Birth cannot be in the future.", exception.getMessage());
  }


  /**
   * Test the string representation of the UserProfile object.
   * Validates that the toString method returns the expected format.
   */

  @Test
  public void testToString(){
    LocalDate dob = LocalDate.of(1998, 6, 16);
    UserProfile user = new UserProfile("Adam", "adam123", "abcxyz1234", Gender.MALE, dob);
    String output = user.toString();
    //Expected output format
    String expectedOutput = "Name: Adam\n" +
        "UserName: adam123\n" +
        "Gender: MALE\n" +
        "DOB: 1998-06-16";
    // Compare actual toString() output with expected
    assertEquals(expectedOutput, user.toString());
  }
}