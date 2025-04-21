import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link HeartRate} class.
 * Tests include validation of heart rate input, string formatting,
 * and getter/setter correctness for all fields.
 */

public class HeartRateTest {

  /**
   * Tests that a valid heart rate (within 30–220 bpm) passes validation
   * and is stored correctly.
   */

  @Test
  public void testValidHeartRate() {
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    HeartRate heartRateEntry1 = new HeartRate("Running", date, time, 200);
    assertEquals(200, heartRateEntry1.getHeartRate());
  }

  /**
   * Tests that a heart rate below the valid range (< 30) triggers
   * an IllegalArgumentException with the correct message.
   */

  @Test
  public void testInvalidLowHeartRate(){
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    // An IllegalArgumentException should be thrown
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new HeartRate("Resting", date, time, 20));
    // Verify the exception message matches the expected output
    assertEquals("Invalid Heart Rate Value Entered.", exception.getMessage());
  }

  /**
   * Tests that a heart rate above the valid range (> 220) triggers
   * an IllegalArgumentException with the correct message.
   */

  @Test
  public void testInvalidHighHeartRate(){
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    // An IllegalArgumentException should be thrown
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new HeartRate("Resting", date, time, 300));
    // Verify the exception message matches the expected output
    assertEquals("Invalid Heart Rate Value Entered.", exception.getMessage());
  }

  /**
   * Tests that the {@code toString()} method produces the correct formatted output
   * based on the provided date, time, heart rate, and activity tag.
   */

  @Test
  void testToString() {
    LocalDate date = LocalDate.of(2025,4,16);
    LocalTime time = LocalTime.of(16,10,30);
    HeartRate heartRateEntry4 = new HeartRate("Walking", date, time, 100);
    // Expected string based on LogEntry formatting rules
    String expectedOutput = "Date: 04-16-2025" + ", Time: 16:10:30"+ ", Heart Rate: 100" + ", Activity: Walking";
    // Assert that the formatted output matches the expected string
    assertEquals(expectedOutput, heartRateEntry4.toString());
  }
}