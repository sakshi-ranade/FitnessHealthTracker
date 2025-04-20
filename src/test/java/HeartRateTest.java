import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link HeartRate} class.
 *
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
    LocalTime time = LocalTime.now();
    HeartRate heartRateEntry = new HeartRate("Running", time, 200);
    assertEquals(200, heartRateEntry.getHeartRate());
  }

  /**
   * Tests that a heart rate below the valid range (< 30) triggers
   * an IllegalArgumentException with the correct message.
   */
  @Test
  public void testInvalidLowHeartRate() {
    LocalTime time = LocalTime.now();
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new HeartRate("Resting", time, 20));
    assertEquals("Invalid Heart Rate Value Entered.", exception.getMessage());
  }

  /**
   * Tests that a heart rate above the valid range (> 220) triggers
   * an IllegalArgumentException with the correct message.
   */
  @Test
  public void testInvalidHighHeartRate() {
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new HeartRate("Resting", time, 250));
    assertEquals("Invalid Heart Rate Value Entered.", exception.getMessage());
  }

  /**
   * Tests that the {@code setHeartRate} method correctly updates the heart rate
   * when a valid value within the acceptable range (30–220 bpm) is provided.
   * Verifies that the new value is stored and retrieved successfully.
   */
  @Test
  public void testSetHeartRateValid() {
    LocalTime time = LocalTime.now();
    HeartRate entry = new HeartRate("Walking", time, 100);
    entry.setHeartRate(150);
    assertEquals(150, entry.getHeartRate());
  }

  /**
   * Tests that the {@code setHeartRate} method throws an {@link IllegalArgumentException}
   * when an invalid heart rate (below 30 bpm) is provided.
   * Verifies that the exception message matches the expected output.
   */
  @Test
  public void testSetHeartRateInvalid() {
    LocalTime time = LocalTime.now();
    HeartRate entry = new HeartRate("Walking", time, 100);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> entry.setHeartRate(10));
    assertEquals("Invalid Heart Rate Value Entered.", exception.getMessage());
  }

  /**
   * Tests that the {@code toString()} method produces the correct formatted output
   * based on the provided date, time, heart rate, and activity tag.
   */
  @Test
  public void testToString() {
    LocalTime time = LocalTime.of(16, 10, 30);
    HeartRate entry = new HeartRate("Walking", time, 100);
    String expectedOutput = "Time: 16:10:30\nHeart Rate: 100\nActivity: Walking";
    assertEquals(expectedOutput, entry.toString());
  }
}