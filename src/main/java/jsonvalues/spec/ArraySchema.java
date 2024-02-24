package jsonvalues.spec;

public final class ArraySchema {
  private int minItems;
  private int maxItems;
  private boolean uniqueItems;

  public ArraySchema setMinItems(int minItems) {
    if (minItems < 0) {
      throw new IllegalArgumentException("minItems must be >= 0");
    }
    this.minItems = minItems;
    return this;
  }

  public ArraySchema setMaxItems(int maxItems) {
    if (maxItems < 0) {
      throw new IllegalArgumentException("maxItems must be >= 0");
    }
    this.maxItems = maxItems;
    return this;
  }

  public ArraySchema setUniqueItems() {
    this.uniqueItems = true;
    return this;
  }


  ArraySchemaConstraints build() {
    return new ArraySchemaConstraints(minItems,
                                      maxItems,
                                      uniqueItems
    );
  }

}