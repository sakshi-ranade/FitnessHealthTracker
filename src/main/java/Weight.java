/**
 * Represents an entry specifically for weightInKg data.
 * Weight is stored in kilograms, even if user inputs pounds.
 * Supports both units: "kg" and "lb"
 */

public class Weight{
  private double weightInKg; //Designed to store weight values in Kilograms
  private String unit;


  /**
   * Constructs a new {@code Weight} object with the specified tags, date, and weightInKg.
   *
   * @param weight The weightInKg value in kilograms.
   * @param unit   The unit of entered weight, must be "kg" or "lb" (case-insensitive).
   * @throws IllegalArgumentException if weight is invalid or unit is unsupported.
   *
   */

  //Constructor for the weightInKg class
  public Weight(double weight, String unit){
    // Call the constructor of the superclass (LogEntry)
    //Validate weightInKg value can't be negative
    if (weight <= 0) {
      throw new IllegalArgumentException("Weight cannot be negative!");
    }
    unit = unit.toLowerCase();

    switch(unit){
      case "kg":
        this.weightInKg = weight;
        break;
      case "lb":
        this.weightInKg = convertPoundsToKg(weight);
        break;
    }

    this.unit = unit;
  }

  /**
   * Converts weight from pounds to kilograms to store internally
   *
   * @param pounds weight in pounds
   * @return weight equivalent in kg
   */

  public static double convertPoundsToKg(double pounds){
    //Converts lb to kg
    return pounds * 0.453592;
  }

  /**
   * Converts a weight from kilograms to pounds.
   *
   * @param kg Weight in kilograms.
   * @return Equivalent weight in pounds.
   */
  public static double convertKgToPounds(double kg) {
    return kg / 0.453592;
  }

  //Getter for weightInKg

  /**
   * Returns the stored weightInKg in kilograms.
   *
   * @return Weight in kilograms.
   */
  public double getWeight() {
    return weightInKg;
  }

  public double getWeightInUserUnits(){
    if (unit.equals("kg"))
      return weightInKg;
    else
      return convertKgToPounds(weightInKg);
  }

  /**
   * Returns the unit used when the weight was recorded.
   *
   * @return "kg" or "lb"
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Returns a formatted string representation of the weight entry.
   *
   * @return A string showing date, time, and weight in the originally selected unit.
   */

  @Override
  public String toString() {
    return "Weight: " + this.getWeight();
  }
}
