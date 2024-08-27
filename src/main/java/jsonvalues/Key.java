package jsonvalues;


import static java.util.Objects.requireNonNull;

/**
 * Represents the key of a value in a JSON object.
 */
public final class Key implements Position {

  /**
   * The name of the key.
   */
  public final String name;

  private Key(final String name) {
    this.name = name;
  }

  /**
   * Returns a new instance representing a key with the given name in a JSON object. Any string, except null, can
   * represent a key in a JSON object, even the empty string.
   *
   * @param key the given name of the key
   * @return a new Key object
   */
  public static Key of(String key) {
    return new Key(requireNonNull(key));
  }


  /**
   * Compares this key with another given position. If the given position is a key, both are compared lexicographically.
   * If it's an index, both are compared numerically.
   *
   * @param o the given position
   * @return 0 if they are equal, +1 if this is greater, -1 otherwise
   */
  @Override
  public int compareTo(final Position o) {
    if (requireNonNull(o)
        .isKey()) {
      return name.compareTo(o.asKey().name);
    }
    return name.compareTo(o.asIndex()
                           .toString());

  }

  /**
   * Returns this key.
   *
   * @return this object
   */
  @Override
  public Key asKey() {
    return this;
  }

  /**
   * Throws a UserError exception.
   *
   * @throws UserError a Key can't be cast into an Index
   */
  @Override
  public Index asIndex() {
    throw UserError.asIndexOfKey();
  }


  /**
   * Returns false.
   *
   * @return false
   */
  @Override
  public boolean isIndex() {
    return false;
  }

  /**
   * Returns true.
   *
   * @return true
   */
  @Override
  public boolean isKey() {
    return true;
  }

  /**
   * Returns the hashcode of this key.
   *
   * @return the hashcode of the name.
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }

  /**
   * Returns true if that is a key and both have the same name.
   *
   * @param that the other object
   * @return true if both objects are Keys with the same name
   */
  @Override
  public boolean equals(final Object that) {
    if (that == null || getClass() != that.getClass()) {
      return false;
    }
    if (this == that) {
      return true;
    }
    Key thatObj = (Key) that;
    return name.equals(thatObj.name);
  }

  /**
   * Returns the name of the key.
   *
   * @return the name of the key
   */
  @Override
  public String toString() {
    return name;
  }
}
