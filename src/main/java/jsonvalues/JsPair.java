package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 Immutable pair which represents a JsElem of a json and its JsPath location: (path, element).
 */
public final class JsPair
{

    /**
     the json element.
     */
    public final JsElem elem;


    /**
     the path of the element.
     */
    public final JsPath path;


    private JsPair(final JsPath path,
                   final JsElem elem
                  )
    {
        this.path = path;
        this.elem = elem;
    }


    /**

     Returns a json pair from the path-like string and the json element.
     @param path the path-like string
     @param elem the JsElem
     @return a JsPair
     */
    public static JsPair of(String path,
                            JsElem elem
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          Objects.requireNonNull(elem)
        );
    }

    /**
     Returns a json pair from the path and the json element.
     @param path the JsPath object
     @param elem the JsElem
     @return a JsPair
     */
    public static JsPair of(JsPath path,
                            JsElem elem
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          Objects.requireNonNull(elem)
        );
    }


    /**

     @return string representation of this pair: (path, elem)
     */
    @Override
    public String toString()
    {
        return String.format("(%s, %s)",
                             path,
                             elem
                            );
    }

    /**
     Returns true if that is a pair and both represents the same element at the same path.
     @param that the reference object with which to compare.
     @return true if this.element.equals(that.element) and this.path.equals(that.path)
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final JsPair thatPair = (JsPair) that;
        return Objects.equals(elem,
                              thatPair.elem
                             ) &&
        Objects.equals(path,
                       thatPair.path
                      );
    }

    /**
     Returns the hashcode of this pair.
     @return the hashcode of this pair
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elem,
                            path
                           );
    }


    /**
     Returns a new pair with the same path and a new element result of applying the mapping function
     @param map the mapping function which maps the JsElem
     @return a new JsPair
     */
    public JsPair mapElem(UnaryOperator<JsElem> map)
    {
        return JsPair.of(this.path,
                         map.apply(this.elem)
                        );
    }

    /**
     Returns a new pair with the same element and a new path result of applying the mapping function
     @param map the mapping function which maps the JsPath
     @return a new JsPair
     */
    public JsPair mapPath(UnaryOperator<JsPath> map)
    {
        return JsPair.of(map.apply(this.path),
                         this.elem
                        );
    }


}
