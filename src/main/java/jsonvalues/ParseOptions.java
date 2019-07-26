package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 Builder with different options to customize the parsing of a string into a json.
 */
public class ParseOptions
{
    private Function<? super JsPair,? extends JsElem> map = pair -> pair.elem;
    private Predicate<? super JsPair> filter = pair -> true;
    private UnaryOperator<String> keyMap = k -> k;
    private Predicate<JsPath> keyFilter = k -> true;

    /**
     static factory method to create a builder to customize the parsing of a string into a json.
     @return a new builder instance
     */
    public static ParseOptions builder()
    {
        return new ParseOptions();
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
     adds a predicate to this builder to filter the elements of the json to be parsed, removing those
     pairs that are evaluated to false on the predicate.
     @param filter the predicate to filter pair of elements.
     @return this ParseOptions builder
     */
    public ParseOptions withElemFilter(final Predicate<? super JsPair> filter)
    {
        this.filter = filter;
        return this;
    }

    /**
     adds a function to this builder to filter the keys of the json to be parsed.
     @param keyFilter the predicate to filter
     @return this ParseOptions builder
     */
    public ParseOptions withKeyFilter(final Predicate<JsPath> keyFilter)
    {
        this.keyFilter = keyFilter;
        return this;
    }

    /**
     adds a function to this builder to map the keys of the json to be parsed.
     @param keyMap the map function which takes as input the name of the key and returns the new key name
     @return this ParseOptions builder
     */
    public ParseOptions withKeyMap(final UnaryOperator<String> keyMap)
    {
        this.keyMap = keyMap;
        return this;
    }

    /**
     adds a function to this builder to map the elements of the json to be parsed
     @param map the map function which takes as input a JsPair and returns the new JsElem
     @return this ParseOptions builder
     */
    public ParseOptions withElemMap(final Function<JsPair, JsElem> map)
    {
        this.map = map;
        return this;
    }

    static class Options
    {

        final Function<? super JsPair, ? extends JsElem> elemMap;
        final Predicate<? super JsPair> elemFilter;
        final UnaryOperator<String> keyMap;
        final Predicate<? super JsPath> keyFilter;

        Options(final Function<? super JsPair, ? extends JsElem> map,
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