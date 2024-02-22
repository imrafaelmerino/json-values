package jsonvalues.benchmark;


import java.util.List;

public class Person {

  private String firstName;
  private String lastName;
  private int age;
  private Double latitude;
  private Double longitude;
  private List<String> fruits;
  private List<Veggie> vegetables;
  private List<Integer> numbers;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(final int age) {
    this.age = age;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(final Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(final Double longitude) {
    this.longitude = longitude;
  }

  public List<String> getFruits() {
    return fruits;
  }

  public void setFruits(final List<String> fruits) {
    this.fruits = fruits;
  }

  public List<Veggie> getVegetables() {
    return vegetables;
  }

  public void setVegetables(final List<Veggie> vegetables) {
    this.vegetables = vegetables;
  }

  public List<Integer> getNumbers() {
    return numbers;
  }

  public void setNumbers(final List<Integer> numbers) {
    this.numbers = numbers;
  }

  @Override
  public String toString() {
    return "Person{" +
           "firstName='" + firstName + '\'' +
           ", lastName='" + lastName + '\'' +
           ", age=" + age +
           ", latitude=" + latitude +
           ", longitude=" + longitude +
           ", fruits=" + fruits +
           ", vegetables=" + vegetables +
           ", numbers=" + numbers +
           '}';
  }
}
