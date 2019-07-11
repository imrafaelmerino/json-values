package jsonvalues;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Computation that parses a string into a JsArray and may either result in a MalformedJson exception
 * or a successfully parsed array.
 */
public final class TryArr
{
    private @Nullable JsArray arr;
    private @Nullable MalformedJson error;

    @EnsuresNonNull("#1")
    TryArr(final JsArray arr)
    {
        this.arr = arr;
    }

    @EnsuresNonNull("#1")
    TryArr(final MalformedJson error)
    {
        this.error = error;
    }

    /**
     Returns true if the parsed string is not a well-formed json array.
     @return true if the parsed string is not a well-formed json array
     */
    public boolean isFailure()
    {
        return error != null;
    }

    /**
     Returns true if the parsed string is a well-formed json array.
     @return true if the parsed string is a well-formed json array
     */
    public boolean isSuccess()
    {
        return arr != null;
    }

    /**
     Returns the computed JsArray wrapped in an optional if the parsed string is a well-formed json
     array, returning an empty optional otherwise.
     @return  Optional.empty() if the parsed string is not a well-formed json array, or an
     Optional containing the computed JsArray otherwise.
     */
    public Optional<JsArray> toOptional()
    {
        if (arr != null) return Optional.of(arr);
        return Optional.empty();
    }

    /**
     Returns the computed JsArray if the parsed string is a well-formed json array, returning the
     JsArray given by the supplier otherwise.
     @param other supplier to be executed if the parsed string is not a well-formed json array
     @return a JsArray
     */
    public JsArray orElse(Supplier<JsArray> other)
    {
        return arr != null ? arr : other.get();
    }

    /**
     Returns the computed JsArray if the parsed string is a well-formed json array, throwing a MalformedJson
     otherwise.
     @return a JsArray
     @throws MalformedJson if the parsed string is not a well-formed json array
     */
    public JsArray orElseThrow() throws MalformedJson
    {
        if (arr != null) return arr;
        if (error != null) throw error;
        throw new Error("TryArr.orElseThrow without arr nor exception");
    }
}
