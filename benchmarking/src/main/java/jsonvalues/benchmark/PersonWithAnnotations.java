package jsonvalues.benchmark;


import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersonWithAnnotations {
    @NotNull
    @Size(min = 1,max = 255)
    private String firstName;

    @NotNull
    @Size(min = 1,max = 255)
    private String lastName;

    @NotNull
    @Min(0)
    @Max(110)
    private Integer age;

    @NotNull
    @Min(-90)
    @Max(90)
    private BigDecimal latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private BigDecimal longitude;

    @NotNull
    @Size(min=1,max = 100)
    private List<String> fruits;
    @NotNull
    @Size(max = 100)
    private List<VeggieWithAnnotations> vegetables = new ArrayList<>();

    @NotNull
    @Size(min=1,max = 100)
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

    public Integer getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(final BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(final BigDecimal longitude) {
        this.longitude = longitude;
    }

    public List<String> getFruits() {
        return fruits;
    }

    public void setFruits(final List<String> fruits) {
        this.fruits = fruits;
    }

    public List<VeggieWithAnnotations> getVegetables() {
        return vegetables;
    }

    public void setVegetables(final List<VeggieWithAnnotations> vegetables) {
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
