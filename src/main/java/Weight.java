/**
 * Represents an entry specifically for weightInKg data.
 * Weight is stored in kilograms, even if user inputs pounds.
 * Supports both units: "kg" and "lb"
 */

public class Weight{
  private static final double POUND_KG_CONVERSION = 0.453592;
  private static final double MAX_WEIGHT = 500d;
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
  public Weight(double weight, String unit){
    // Call the constructor of the superclass (LogEntry)
    //Validate weightInKg value can't be negative
    if (weight <= 0) {
      throw new IllegalArgumentException("Weight cannot be negative!");
    }
    unit = unit.toLowerCase();
    double inputWeight = 0;
    switch(unit){
      case "kg":
        inputWeight = weight;
        break;
      case "lb":
        inputWeight = convertPoundsToKg(weight);
        break;
    }
    if(inputWeight > MAX_WEIGHT) {
      throw new IllegalArgumentException("Unable to save weight metric more than " + MAX_WEIGHT + " kgs.");
    }
    this.weightInKg = inputWeight;
    this.unit = unit;
  }

  /**
   * Converts weight from pounds to kilograms to store internally
   *
   * @param weightInPounds weight in pounds
   * @return weight equivalent in kg
   */

  public static double convertPoundsToKg(double weightInPounds){
    //Converts lb to kg
    return Math.round(weightInPounds * POUND_KG_CONVERSION * 10.0) / 10.0;
  }

  /**
   * Converts a weight from kilograms to pounds.
   *
   * @param weightInKg Weight in kilograms.
   * @return Equivalent weight in pounds.
   */
  public static double convertKgToPounds(double weightInKg) {
    return Math.round( (weightInKg / POUND_KG_CONVERSION) * 10.0) / 10.0;
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
