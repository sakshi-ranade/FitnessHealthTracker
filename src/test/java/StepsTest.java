import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Steps} class.
 */

public class StepsTest {

  /**
   * Test creating a valid Steps object.
   */

  @Test
  public void testValidSteps() {
    LocalDate date = LocalDate.of(2025, 4, 17);
    Steps entry1 = new Steps(date, 10000);
    assertEquals(10000, entry1.getSteps());
    assertEquals(date, entry1.getDate());
  }

  /**
   * Test conversion to kilometers.
   */
  @Test
  public void testDistanceConversion() {
    Steps entry2 = new Steps(LocalDate.now(), 12500);
    double expectedKmeEquivalency = Math.round(12500 * 0.0007*10.0)/10.0;
    assertEquals(expectedKmeEquivalency, entry2.getDistanceInKm(), 0.0001);
  }

  /**
   * Test negative step input throws exception.
   */
  @Test
  public void testNegativeStepsThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Steps(LocalDate.now(), -100);
    });
    assertEquals("Steps cannot be negative!", exception.getMessage());
  }

  /**
   * Test too high step input throws exception.
   */
  @Test
  public void testStepsTooHighThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Steps(LocalDate.now(), 100000);
    });
    assertEquals("Step count too high!", exception.getMessage());
  }

  /**
   * Test the toString output format.
   */
  @Test
  public void testToString() {
    LocalDate date = LocalDate.of(2025, 4, 17);
    Steps entry3 = new Steps(date, 3000);
    String expectedOutput = "Date: 04-17-2025 Steps: 3000 Distance: 2.1";
    assertEquals(expectedOutput, entry3.toString());
  }
}