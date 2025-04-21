import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Weight} class.
 * Tests include validation of input, unit conversion, and string representation.
 */

public class WeightTest {

  /**
   * Tests that a valid weight in kilograms is stored correctly.
   */

  @Test
  public void testValidWeightInKg(){
    double weight = 60.5; //weight in kg
    String unit = "kg";
    Weight entry1 = new Weight(weight,unit);
    //Since it is in kg, it should store the weight without conversion
    assertEquals(60.5, entry1.getWeight(),0.001);
    assertEquals("kg",entry1.getUnit());
  }

  /**
   * Tests that weight in pounds is converted correctly to kilograms.
   */
  @Test
  public void testConvertPoundsToKg(){
    double pounds = 150.1;
    String unit = "lb";
    Weight entry2 = new Weight(pounds, unit);
    //Should convert pounds to kg
    double expectedWeightInKg = Math.round(pounds * 0.453592 * 10.0) / 10.0;
    assertEquals(expectedWeightInKg, entry2.getWeight(), 0.001);
    assertEquals("lb", entry2.getUnit());
    //Testing whether original pounds value is returned
    assertEquals(pounds, entry2.getWeightInUserUnits(), 0.001);

  }

  /**
   * Tests that a negative weight throws an IllegalArgumentException.
   */
  @Test
  public void testNegativeWeightThrowsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new Weight(-45.5, "kg"));
    assertEquals("Weight cannot be negative!", exception.getMessage());
  }

  /**
   * Tests the toString method for correct formatting.
   */
  @Test
  public void testToStringOutput() {
    double weight = 60.0;
    String unit = "kg";
    Weight entry3 = new Weight(weight, unit);
    String result = entry3.toString();
    // Check for expected content in output
    String expectedOutput = "Weight: 60.0";
    assertEquals(result, expectedOutput);

  }



}