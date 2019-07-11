package jsonvalues;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 *  Computation that parses a string into a JsObj and may either result in a MalformedJson exception
 *  or a successfully parsed object.
 */
public final class TryObj
{
    private @Nullable JsObj obj;
    private @Nullable MalformedJson error;

    @EnsuresNonNull("#1")
    TryObj(final JsObj obj)
    {
        this.obj = obj;
    }

    @EnsuresNonNull("#1")
    TryObj(final MalformedJson error)
    {
        this.error = error;
    }

    /**
     Returns true if the parsed string is a malformed json object.
     @return true if the parsed string is a malformed json object
     */
    public boolean isFailure()
    {
        return error != null;
    }

    /**
     Returns true if the parsed string is a well-formed json object.
     @return true if the parsed string is a well-formed json object
     */
    public boolean isSuccess()
    {
        return error == null;
    }

    /**
     Returns the computed json object wrapped in an optional if the parsed string is a well-formed
     json object, returning an empty optional otherwise.
     @return  an Optional containing a JsObj or Optional.empty() if the given string is not a
     well-formed json object.
     */
    public Optional<JsObj> toOptional()
    {
        if (obj != null) return Optional.of(obj);
        return Optional.empty();
    }

    /**
     Returns the computed JsObj if the parsed string is a well-formed json object, throwing an exception
     otherwise.
     @return a JsObj
     @throws MalformedJson if this try is a failure
     */
    public JsObj orElseThrow() throws MalformedJson
    {
        if (obj != null) return obj;
        if (error != null) throw error;
        throw new Error("TryObj.orElseThrow without obj nor exception");
    }

    /**
     Returns the computed JsObj if the parsed string is a well-formed json object, returning the JsObj
     given by the supplier otherwise.
     @param other supplier to be executed if the parsed string is not a well-formed json object
     @return a JsObj
     */
    public JsObj orElse(Supplier<JsObj> other)
    {
        return obj != null ? obj : other.get();
    }

}
