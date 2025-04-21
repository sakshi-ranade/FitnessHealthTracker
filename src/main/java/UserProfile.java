/**
 * @author Sakshi
 */

import java.time.LocalDate; // Import the LocalDate class for handling dates
import java.time.Period;
import java.time.Year;

/**
 * Represents a user profile with information like name, username, password, gender, date of birth,
 * and daily log file path.
 * Provides methods to access and modify these fields.
 * Calculates the user's age from date of birth.
 */

public class UserProfile {

  private String name;
  private String userName;
  private String password;
  private Gender gender;
  private LocalDate dateOfBirth;

  /**
   * Constructs a new UserProfile with the given attributes.
   *
   * @param name         name of the user
   * @param userName     username for login
   * @param password     password for login
   * @param gender       user's gender
   * @param dateOfBirth  user's date of birth. This will be validated to ensure it's not in the future
   * and that February 29 is only used in leap years.
   * @throws IllegalArgumentException if DOB provided is in the future
   * or February 29 provided is non-leap year.
   */

  //Constructor
  public UserProfile(String name, String userName, String password, Gender gender, LocalDate dateOfBirth){
    this.name = name;
    this.userName = userName;
    this.password = password;
    this.gender = gender;
    setDateOfBirth(dateOfBirth); //validation inside setter method
  }

  //Getter and Setters

  /**
   * Returns the name of the user.
   *
   * @return The name of the user.
   */

  public String getName() {
    return name;
  }

  /**
   * Sets the name of the user.
   *
   * @param name The new name of the user.
   */

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the username of the user.
   *
   * @return The username of the user.
   */

  public String getUserName() {
    return userName;
  }

  /**
   * Sets the username of the user and updates the daily log file path.
   *
   * @param userName The new username of the user.
   */

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Returns the password set by the user.
   *
   * @return The password set by the user.
   */

  public String getPassword() {
    return password;
  }

  /**
   * Sets the password of the user.
   *
   * @param password of the user.
   */

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the gender of the user.
   *
   * @return The gender of the user.
   */

  public Gender getGender() {
    return gender;
  }

  /**
   * Sets the gender of the user.
   *
   * @param gender of the user.
   */

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  /**
   * Returns the date of birth of the user.
   *
   * @return The date of birth of the user.
   */

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Sets the date of birth of the user.
   *
   * @param dateOfBirth The new date of birth.
   * @throws IllegalArgumentException if the provided date of birth is in the future
   * or if February 29 is provided for a non-leap year.
   */

  public void setDateOfBirth(LocalDate dateOfBirth) {
    // Checking if the DOB provided is in the future
    if (dateOfBirth.isAfter(LocalDate.now())){
      throw new IllegalArgumentException("Date of Birth cannot be in the future.");
    }
    //Check if the date is February 29 and the year is a leap year
    if (dateOfBirth.getMonthValue() == 2 && dateOfBirth.getDayOfMonth() == 29){
      if (!Year.of(dateOfBirth.getYear()).isLeap()){
        throw new IllegalArgumentException("Selected year is not a leap year.");
      }
    }
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Calculates and returns the age of the user in years and months.
   * Returns "Unknown Age" if the date of birth is not set.
   *
   * @return A string representing the user's age in the format "Years: [years] Months: [months]",
   * or "Unknown Age" if the date of birth is null.
   */

  public String getAge(){
    //Check if the DOB is null
    if (dateOfBirth == null){
      return "Unknown Age";
    }
    // Calculate the age based on DOB and current date
    Period age = Period.between(dateOfBirth, LocalDate.now());
    // Return the age in years and months
    if(age.getMonths() > 0){
      return age.getYears() + " years and " + age.getMonths() + " months";
    }
    return age.getYears() + " years";
  }

  /**
   * Returns a string representation of the {@code UserProfile} object.
   *
   * @return A string containing the user's name, username, gender, date of birth (or "N/A" if not set),
   * and daily log file path.
   */

  @Override
  public String toString(){
    //Returns the user details
    return "Name: " + this.name + "\nUserName: " + this.userName + "\nGender: " + this.gender +
        "\nDOB: " + (dateOfBirth != null ? dateOfBirth : "N/A");
  }
}