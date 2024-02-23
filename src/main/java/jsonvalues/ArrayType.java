package jsonvalues;

/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2021 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Helper to replace reflective array access.
 *
 * @author Pap Lőrinc
 */
interface ArrayType<T> {

  @SuppressWarnings("unchecked")
  static <T> ArrayType<T> obj() {
    return (ArrayType<T>) ObjectArrayType.INSTANCE;
  }

  int lengthOf(Object array);

  T getAt(Object array,
          int index);

  Object empty();

  void setAt(Object array,
             int index,
             T value) throws ClassCastException;

  Object copy(Object array,
              int arraySize,
              int sourceFrom,
              int destinationFrom,
              int size);


  default Object newInstance(int length) {
    return copy(empty(),
                length);
  }

  /**
   * System.arrayCopy with same source and destination
   */
  default Object copyRange(Object array,
                           int from,
                           int to) {
    int length = to - from;
    return copy(array,
                length,
                from,
                0,
                length);
  }

  /**
   * Repeatedly group an array into equal sized subtrees
   */
  default Object grouped(Object array,
                         int groupSize) {
    final int arrayLength = lengthOf(array);
    final Object results = obj().newInstance(1 + ((arrayLength - 1) / groupSize));
    obj().setAt(results,
                0,
                copyRange(array,
                          0,
                          groupSize));

    for (int start = groupSize, i = 1; start < arrayLength; i++) {
      final int nextLength = Math.min(groupSize,
                                      arrayLength - (i * groupSize));
      obj().setAt(results,
                  i,
                  copyRange(array,
                            start,
                            start + nextLength));
      start += nextLength;
    }

    return results;
  }

  /**
   * clone the source and set the value at the given position
   */
  default Object copyUpdate(Object array,
                            int index,
                            T element) {
    final Object copy = copy(array,
                             index + 1);
    setAt(copy,
          index,
          element);
    return copy;
  }

  default Object copy(Object array,
                      int minLength) {
    final int arrayLength = lengthOf(array);
    final int length = Math.max(arrayLength,
                                minLength);
    return copy(array,
                length,
                0,
                0,
                arrayLength);
  }

  /**
   * clone the source and keep everything after the index (pre-padding the values with null)
   */
  default Object copyDrop(Object array,
                          int index) {
    final int length = lengthOf(array);
    return copy(array,
                length,
                index,
                index,
                length - index);
  }

  /**
   * clone the source and keep everything before and including the index
   */
  default Object copyTake(Object array,
                          int lastIndex) {
    return copyRange(array,
                     0,
                     lastIndex + 1);
  }

  /**
   * Create a single element array
   */
  default Object asArray(T element) {
    final Object result = newInstance(1);
    setAt(result,
          0,
          element);
    return result;
  }


  final class ObjectArrayType implements ArrayType<Object> {

    static final ObjectArrayType INSTANCE = new ObjectArrayType();
    static final Object[] EMPTY = new Object[0];

    private static Object[] cast(Object array) {
      return (Object[]) array;
    }

    private static Object copyNonEmpty(Object array,
                                       int arraySize,
                                       int sourceFrom,
                                       int destinationFrom,
                                       int size
                                      ) {
      final Object[] result = new Object[arraySize];
      System.arraycopy(array,
                       sourceFrom,
                       result,
                       destinationFrom,
                       size); /* has to be near the object allocation to avoid zeroing out the array */
      return result;
    }

    @Override
    public Object[] empty() {
      return EMPTY;
    }

    @Override
    public int lengthOf(Object array) {
      return (array != null) ? cast(array).length : 0;
    }

    @Override
    public Object getAt(Object array,
                        int index) {
      return cast(array)[index];
    }

    @Override
    public void setAt(Object array,
                      int index,
                      Object value) {
      cast(array)[index] = value;
    }

    @Override
    public Object copy(Object array,
                       int arraySize,
                       int sourceFrom,
                       int destinationFrom,
                       int size
                      ) {
      return (size > 0)
             ? copyNonEmpty(array,
                            arraySize,
                            sourceFrom,
                            destinationFrom,
                            size)
             : new Object[arraySize];
    }
  }
}