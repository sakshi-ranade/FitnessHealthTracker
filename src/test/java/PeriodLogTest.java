import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class PeriodLogTest {

  /**
   * Tests that an exception is thrown when endDate is before startDate.
   */

  @Test
  public void testEndDateBeforeStartDate(){
    LocalDate start = LocalDate.of(2025,4,17);
    LocalDate end = LocalDate.of(2025, 4, 16);
    Exception exception = assertThrows(IllegalArgumentException.class,() ->
        new PeriodLog("Test", start, end, "Test"));
    assertEquals("End date cannot be before start date.", exception.getMessage());
  }

  /**
   * Tests the correct duration calculation of the period.
   */

  @Test
  public void testPeriodDuration(){
    LocalDate start = LocalDate.of(2025,2,26);
    LocalDate end = LocalDate.of(2025,3,2);
    PeriodLog period = new PeriodLog("Test", start, end, "medium");
    assertEquals("Periods lasted for: 5 days.", period.periodDuration());

  }

  /**
   * Tests the string representation of the period log.
   */
  @Test
  public void testToStringFormat() {
    LocalDate start = LocalDate.of(2025, 4, 1);
    LocalDate end = LocalDate.of(2025, 4, 3);
    PeriodLog period = new PeriodLog("Flow", start, end, "Light");
    //String representation of expected output
    String expectedOutput = "Period Log" +
        "\nStarted on: 04-01-2025" +
        "\nEnded on: 04-03-2025" +
        "\n Duration: Periods lasted for: 3 days.";
    assertEquals(expectedOutput, period.toString());
  }
}