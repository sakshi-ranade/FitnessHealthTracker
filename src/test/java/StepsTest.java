import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;


public class StepsTest {

  @Test
  public void testValidSteps() {
    LocalTime time = LocalTime.of(14, 30);
    Steps entry = new Steps(time, 10000);
    assertEquals(10000, entry.getSteps());
    assertEquals(time, entry.getTime());
  }

  @Test
  public void testDistanceConversion() {
    Steps entry = new Steps(LocalTime.now(), 12500);
    double expectedKm = 12500 * 0.0007;
    assertEquals(expectedKm, entry.getDistanceInKm(), 0.0001);
  }

  @Test
  public void testNegativeStepsThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new Steps(LocalTime.now(), -100));
    assertEquals("Steps cannot be negative!", exception.getMessage());
  }

  @Test
  public void testStepsTooHighThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new Steps(LocalTime.now(), 100000));
    assertEquals("Step count too high!", exception.getMessage());
  }

  @Test
  public void testToString() {
    LocalTime time = LocalTime.of(9, 15);
    Steps entry = new Steps(time, 3000);
    String expected = "Time: " + time.format(Constants.timeFormatter) + " Steps: 3000";
    assertEquals(expected, entry.toString());
  }
}
