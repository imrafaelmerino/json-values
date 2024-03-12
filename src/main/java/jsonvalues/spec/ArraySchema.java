package jsonvalues.spec;

/**
 * A class representing the schema for arrays in a JSON structure. It allows setting constraints such as minimum and
 * maximum number of items and uniqueness of items in the array.
 */
public final class ArraySchema {

  private ArraySchema() {
  }

  private int minItems;
  private int maxItems;
  private boolean uniqueItems;

  /**
   * Creates an ArraySchema with a minimum size constraint.
   *
   * @param minItems The minimum number of items allowed in the array (must be >= 0).
   * @return An ArraySchema instance with the specified minimum size constraint.
   * @throws IllegalArgumentException if minItems is less than 0.
   */
  public static ArraySchema withMinSize(int minItems) {
    if (minItems < 0) {
      throw new IllegalArgumentException("minItems must be >= 0");
    }
    var schema = new ArraySchema();
    schema.minItems = minItems;
    return schema;
  }

  /**
   * Creates an ArraySchema with a maximum size constraint.
   *
   * @param maxItems The maximum number of items allowed in the array (must be >= 0).
   * @return An ArraySchema instance with the specified maximum size constraint.
   * @throws IllegalArgumentException if maxItems is less than 0.
   */
  public static ArraySchema withMaxSize(int maxItems) {
    if (maxItems < 0) {
      throw new IllegalArgumentException("maxItems must be >= 0");
    }
    var schema = new ArraySchema();
    schema.maxItems = maxItems;
    return schema;
  }

  /**
   * Creates an ArraySchema with both minimum and maximum size constraints.
   *
   * @param minItems The minimum number of items allowed in the array (must be >= 0).
   * @param maxItems The maximum number of items allowed in the array (must be >= 0).
   * @return An ArraySchema instance with the specified minimum and maximum size constraints.
   * @throws IllegalArgumentException if minItems or maxItems is less than 0.
   */
  public static ArraySchema sizeBetween(int minItems,
                                        int maxItems) {
    if (minItems < 0) {
      throw new IllegalArgumentException("minItems must be >= 0");
    }
    if (maxItems < 0) {
      throw new IllegalArgumentException("maxItems must be >= 0");
    }
    var schema = new ArraySchema();
    schema.minItems = minItems;
    schema.maxItems = maxItems;
    return schema;
  }


  /**
   * Sets the uniqueItems constraint for the array.
   *
   * @return The current ArraySchema instance with the uniqueItems constraint set to true.
   */
  public ArraySchema setUniqueItems() {
    this.uniqueItems = true;
    return this;
  }


  ArraySchemaConstraints build() {
    return new ArraySchemaConstraints(minItems,
                                      maxItems,
                                      uniqueItems);
  }
}
