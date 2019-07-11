package jsonvalues;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 *Computation that parses a string into a Json and may either result in a MalformedJson exception
 *or a successfully parsed json.
 */
public final class Try
{

    private @Nullable JsObj obj;

    private @Nullable JsArray arr;

    private @Nullable MalformedJson error;

    @EnsuresNonNull("#1")
    Try(final JsObj obj)

    {
        this.obj = obj;

    }

    @EnsuresNonNull("#1")
    Try(final JsArray arr)
    {
        this.arr = arr;

    }

    @EnsuresNonNull("#1")
    Try(MalformedJson error)
    {
        this.error = error;
    }

    /**
     Returns true if the parsed string is a malformed json.
     @return true if the parsed string is a malformed json
     */
    public boolean isFailure()
    {
        return error != null;
    }

    /**
     Returns true if the parsed string is a well-formed json.
     @return true if the parsed string is a well-formed json
     */
    public boolean isSuccess()
    {
        return arr != null;
    }

    /**
     Returns the computed JsObj if the parsed string is a well-formed json object, throwing a
     MalformedJson exception otherwise.
     @return a JsObj
     @throws MalformedJson if the parsed string is not a well-formed json object
     */
    public JsObj objOrElseThrow() throws MalformedJson
    {
        if (obj != null) return obj;
        if (arr != null) throw MalformedJson.expectedObj(arr.toString());
        if (error != null) throw error;
        throw new Error("Try.objOrElseThrow without an arr nor obj nor exception");
    }

    /**
     Returns the computed JsArray if the parsed string is a well-formed json array, throwing a
     MalformedJson exception otherwise.
     @return a JsArray
     @throws MalformedJson if the parsed string is not a well-formed json array
     */
    public JsArray arrOrElseThrow() throws MalformedJson
    {
        if (arr != null) return arr;
        if (obj != null) throw MalformedJson.expectedArray(obj.toString());
        if (error != null) throw error;
        throw new Error("Try.arrOrElseThrow without an arr nor obj nor exception");
    }

    /**
     Returns the computed Json if the parsed string is a well-formed json, throwing a MalformedJson
     exception otherwise.
     @return a Json
     @throws MalformedJson if the parsed string is not a well-formed json
     */
    public Json<?> orElseThrow() throws MalformedJson
    {
        if (arr != null) return arr;
        if (obj != null) return obj;
        if (error != null) throw error;
        throw new Error("Try.orElseThrow without an arr nor obj nor exception");
    }

    /**
     Returns the computed JsArray if the parsed string is a well-formed json array, returning the JsArray
     given by the supplier otherwise.
     @param  other the supplier
     @return a JsArray
     */
    public JsArray arrOrElse(Supplier<JsArray> other)
    {
        return arr != null ? arr : other.get();
    }

    /**
     Returns the computed JsObj if the parsed string is a well-formed json object, returning the JsObj
     given by the supplier otherwise.
     @param  other the supplier
     @return a JsArray
     */
    public JsObj objOrElse(Supplier<JsObj> other)
    {
        return obj != null ? obj : other.get();
    }

    /**
     Returns the computed Json wrapped in an optional if the parsed string is a well-formed json,
     returning an empty optional otherwise.
     @return  Optional.empty() if the parsed string is not a well-formed json, returning an
     Optional containing the computed Json otherwise.
     */
    public Optional<Json<?>> toOptional()
    {
        if (arr != null) return Optional.of(arr);
        if (obj != null) return Optional.of(obj);
        if (error != null) return Optional.empty();
        throw new Error("Try.toOptional without an arr nor obj nor exception");
    }

}

