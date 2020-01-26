package jsonvalues;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 Encapsulates a RFC 6902 implementation. Json patch operations can be applied to Jsons using the method
 {@link Json#patch(JsArray)}.
 */
public final class Patch<T extends Json<T>>
{

    private static final String FROM_FIELD = "from";
    private static final String OP_FIELD = "op";
    private static final String PATH_FIELD = "path";
    private static final String VALUE_FIELD = "value";

    /**
     List of supported patch-operations
     */
    private enum OP
    {ADD, REMOVE, MOVE, COPY, REPLACE, TEST}


    final List<OpPatch<T>> ops;

    Patch(JsArray array) throws PatchMalformed
    {
        ops = new ArrayList<>();
        for (JsElem elem : array)
        {
            if (!elem.isObj()) throw PatchMalformed.operationIsNotAnObj(elem);
            final JsObj opObj = elem.asJsObj();
            final Optional<String> op = opObj.getStr(JsPath.fromKey(OP_FIELD));
            if (!op.isPresent()) throw PatchMalformed.operationRequired(opObj);
            try
            {
                switch (OP.valueOf(op.get()
                                     .toUpperCase()))
                {
                    case ADD:
                        ops.add(new OpPatchAdd<>(opObj));
                        break;
                    case REMOVE:
                        ops.add(new OpPatchRemove<>(opObj));
                        break;
                    case MOVE:
                        ops.add(new OpPatchMove<>(opObj));
                        break;
                    case COPY:
                        ops.add(new OpPatchCopy<>(opObj));
                        break;
                    case REPLACE:
                        ops.add(new OpPatchReplace<>(opObj));
                        break;
                    case TEST:
                        ops.add(new OpPatchTest<>(opObj));
                        break;
                    default:
                        throw InternalError.patchOperationNotSupported(op.get());
                }
            }
            catch (IllegalArgumentException e)
            {
                throw PatchMalformed.operationNotSupported(opObj);
            }
        }
    }

    /**
     return a new patch-operation builder
     @return a new patch-operation builder
     */
    public static Builder create()
    {
        return new Builder();
    }

    /**
     represents a builder to create json-patch operations according to the RFC 6902 specification.
     */
    public static final class Builder
    {
        private Builder()
        {
        }

        private JsArray ops = Jsons.immutable.array.empty();

        /**
         ADD operation.
         @param path target location of the operation
         @param value element to be added
         @return this builder with an ADD operation appended
         */
        public Builder add(final String path,
                           final JsElem value
                          )
        {
            ops.append(Jsons.immutable.object.of(PATH_FIELD,
                                                 JsStr.of(requireNonNull(path)),
                                                 OP_FIELD,
                                                 JsStr.of(OP.ADD.name()),
                                                 VALUE_FIELD,
                                                 requireNonNull(value)
                                                ));
            return this;
        }

        /**
         REPLACE operation.
         @param path target location of the operation
         @param value element to be replaced with
         @return this builder with a REPLACE operation appended
         */
        public Builder replace(final String path,
                               final JsElem value
                              )
        {
            ops.append(Jsons.immutable.object.of(PATH_FIELD,
                                                 JsStr.of(requireNonNull(path)),
                                                 VALUE_FIELD,
                                                 requireNonNull(value),
                                                 OP_FIELD,
                                                 JsStr.of(OP.REPLACE.name())
                                                ));
            return this;

        }

        /**
         REMOVE operation.
         @param path target location of the operation
         @return this builder with a REMOVE operation appended
         */
        public Builder remove(final String path)
        {
            ops.append(Jsons.immutable.object.of(PATH_FIELD,
                                                 JsStr.of(requireNonNull(path)),
                                                 OP_FIELD,
                                                 JsStr.of(OP.REMOVE.name())
                                                ));
            return this;
        }

        /**
         TEST operation.
         @param path target location of the operation
         @param value element to be tested
         @return this builder with a TEST operation appended
         */
        public Builder test(final String path,
                            final JsElem value
                           )
        {
            ops.append(Jsons.immutable.object.of(PATH_FIELD,
                                                 JsStr.of(requireNonNull(path)),
                                                 VALUE_FIELD,
                                                 requireNonNull(value),
                                                 OP_FIELD,
                                                 JsStr.of(OP.TEST.name())
                                                ));
            return this;

        }

        /**
         MOVE operation.
         @param from target location of the operation
         @param to source location of the operation
         @return this builder with a MOVE operation appended
         */
        public Builder move(final String from,
                            final String to
                           )
        {
            ops.append(Jsons.immutable.object.of(PATH_FIELD,
                                                 JsStr.of(requireNonNull(to)),
                                                 FROM_FIELD,
                                                 JsStr.of(requireNonNull(from)),
                                                 OP_FIELD,
                                                 JsStr.of(OP.MOVE.name())
                                                ));
            return this;
        }

        /**
         COPY operation.
         @param from target location of the operation
         @param to source location of the operation
         @return this builder with a COPY operation appended
         */
        public Builder copy(final String from,
                            final String to
                           )
        {
            ops.append(Jsons.immutable.object.of(PATH_FIELD,
                                                 JsStr.of(requireNonNull(to)),
                                                 FROM_FIELD,
                                                 JsStr.of(requireNonNull(from)),
                                                 OP_FIELD,
                                                 JsStr.of(OP.COPY.name())
                                                )
                      );
            return this;
        }

        /**
         returns the array of operations
         @return a JsArray
         */
        public JsArray toArray()
        {
            return ops;
        }

        /**
         returns the array of operations as a string
         @return a String
         */
        public String toString()
        {
            return ops.toString();
        }
    }
}
