
public class UserProfile {

  public enum Gender{
    FEMALE, MALE, I_DO_NOT_WANT_TO_SPECIFY
  }

  String name;
  String userName;
  int ageInYears;
  int ageInMonths;
  private Gender gender;


  public UserProfile(String name, String userName, int ageInYears, int ageInMonths, Gender gender){
    this.name = name;
    this.userName = userName;
    this.ageInYears = ageInYears;
    this.ageInMonths = ageInMonths;
    this.gender = gender;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getAgeInMonths() {
    return ageInMonths;
  }

  public void setAgeInMonths(int ageInMonths) {
    this.ageInMonths = ageInMonths;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public void ageCheck(){
    if (ageInYears < 0 || ageInMonths < 0){
      throw new IllegalArgumentException("Age cannot be negative");
    }
  }

  public void correctAge(){
    if (ageInMonths >= 12) {
      int quotient = ageInMonths / 12;
      int remainder = ageInMonths % 12;
      setAgeInYears(this.ageInYears + quotient);
      this.ageInMonths = remainder;
    }
  }

  public int getAgeInYears() {
    return ageInYears;
  }

  public void setAgeInYears(int ageInYears) {
    this.ageInYears = ageInYears;
  }

  @Override
  public String toString(){
    return "Name: " + this.name + " UserName: " + this.userName + " Age(years): " + this.ageInYears + " Age(Months): " + this.ageInMonths + " Gender: " + this.gender;
  }

  public static void main(String[] args) {
    UserProfile abz = new UserProfile("xyz", "xyz", 12, 23, Gender.MALE);
    System.out.println(abz.toString());
    abz.correctAge();
    System.out.println("After: " + abz.toString());


  }

}
