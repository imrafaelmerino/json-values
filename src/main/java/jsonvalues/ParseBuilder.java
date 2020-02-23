package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 Builder with different transformations to customize the parsing of a string into a json.
 */
public final class ParseBuilder
{
    private Function<? super JsPair, ? extends JsValue> map = pair -> pair.value;
    private Predicate<? super JsPair> filter = pair -> true;
    private UnaryOperator<String> keyMap = k -> k;
    private Predicate<? super JsPath> keyFilter = k -> true;

    private ParseBuilder(){}
    /**
     static factory method to create a builder to customize the parsing of a string into a json.
     @return a new builder instance
     */
    public static ParseBuilder builder()
    {
        return new ParseBuilder();
    }

    Options create()
    {
        return new Options(map,
                           filter,
                           keyMap,
                           keyFilter
        );
    }

    /**
     adds a predicate to this builder to filter the elements of the json, removing those pairs that
     are evaluated to false on the predicate.
     @param filter the predicate to filter pair of elements.
     @return this ParseOptions builder
     */
    public ParseBuilder withElemFilter(final Predicate<? super JsPair> filter)
    {
        this.filter = filter;
        return this;
    }

    /**
     adds a function to this builder to filter the keys of the json.
     @param keyFilter the predicate to filter
     @return this ParseOptions builder
     */
    public ParseBuilder withKeyFilter(final Predicate<? super JsPath> keyFilter)
    {
        this.keyFilter = keyFilter;
        return this;
    }

    /**
     adds a function to this builder to map the keys of the json.
     @param keyMap the map function which takes as input the name of the key and returns the new key name
     @return this ParseOptions builder
     */
    public ParseBuilder withKeyMap(final UnaryOperator<String> keyMap)
    {
        this.keyMap = keyMap;
        return this;
    }

    /**
     adds a function to this builder to map the elements of the json.
     @param map the map function which takes as input a JsPair and returns the new JsElem
     @return this ParseOptions builder
     */
    public ParseBuilder withElemMap(final Function<? super JsPair, ? extends JsValue> map)
    {
        this.map = map;
        return this;
    }

    static class Options
    {

        final Function<? super JsPair, ? extends JsValue> elemMap;
        final Predicate<? super JsPair> elemFilter;
        final UnaryOperator<String> keyMap;
        final Predicate<? super JsPath> keyFilter;

        Options(final Function<? super JsPair, ? extends JsValue> map,
                final Predicate<? super JsPair> filter,
                final UnaryOperator<String> keyMap,
                final Predicate<? super JsPath> keyFilter
               )
        {
            this.elemMap = map;
            this.elemFilter = filter;
            this.keyMap = keyMap;
            this.keyFilter = keyFilter;
        }
    }
}