package jsonvalues.specifications

object Functions
{

  def isLowercase(s: String): Boolean = s.chars().allMatch(c => Character.isLowerCase(c))
  def isUppercase(s: String): Boolean = s.chars().allMatch(c => Character.isUpperCase(c))

}
