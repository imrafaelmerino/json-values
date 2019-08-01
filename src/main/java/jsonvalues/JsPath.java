package jsonvalues;


import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.regex.qual.Regex;
import scala.collection.JavaConverters;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.Vector;
import scala.collection.mutable.Builder;
import scala.runtime.AbstractFunction1;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**<pre>
 Represents the full path location of an element in a json. It's a list of {@link Position}. Exists
 two different ways of creating a JsPath:

 - By a path-like string using the static factory method {@link JsPath#of(String)}, where the path follows
 the grammar:

 path: position, position.path
 position: key, index
 key: string, 'string', 'number'
 index: number
 string: non-numeric characters <b>url-encoded</b>  (see https://www.json.org for more details)
 number: \d+

 When the name of a key is numeric, it has to be single-quoted, to distinguish indexes from
 keys. When the key is not numeric, single quotes are completely optional.

 - By an API, using the methods {@link #key(String)} and {@link #index(int)}. In this case, keys don't have to
 be url-encoded.

 For example, given the following Json object:

 {
 "a": { "x":[ {"c": [1,2, { "": { "1" : true, " ": false, "'": 4} }]} ]}}
 "1": null,
 "": ""
 }

 "" = ""                      //empty string is the empty key, which is a valid name for a key
 "'1'" = null                 //numeric keys have to be single-quoted
 "a.x.0.c.0" = 1
 "a.x.0.c.1" = 2
 "a.x.0.c.2..'1'" = true      // single quotes are only mandatory when the key is a number
 "a.x.0.c.2..+" = false       // + is url-decoded to the white-space
 "a.x.0.c.2..%27" = 4         // %27 is url-decoded to '

 and using the API:

 {@code
 JsPath empty = JsPath.empty();  // doesn't represent any path
 empty.key("") = ""
 empty.key("1") = null
 empty.key("a").key("x").index(0).key("c").index(0) = 1
 empty.key("a").key("x").index(0).key("c").index(1) = 2
 empty.key("a").key("x").index(0).key("c").index(2).key("").key("1") = true
 empty.key("a").key("x").index(0).key("c").index(2).key("").key(" ") = false  //and not key("+")
 empty.key("a").key("x").index(0).key("c").index(2).key("").key("'") = 4      //and not key("%27")
 }
 </pre>
 */
public final class JsPath implements Comparable<JsPath>

{
    private static final Key KEY_EMPTY = Key.of("");
    private static final Key KEY_SINGLE_QUOTE = Key.of("'");
    private static final String MINUS_ONE = "-1";
    private static final String UTF8 = "utf-8";
    private static final CanBuildFrom<Vector<Position>, Position, Vector<Position>> bf = new CanBuildFrom<Vector<Position>, Position, Vector<Position>>()
    {
        @Override
        public Builder<Position, Vector<Position>> apply()
        {
            return Vector.<Position>canBuildFrom().apply();
        }

        @Override
        public Builder<Position, Vector<Position>> apply(final Vector<Position> v)
        {
            return Vector.<Position>canBuildFrom().apply();
        }
    };


    private static final Vector<Position> EMPTY_VECTOR = new Vector<>(0,
                                                                      0,
                                                                      0
    );
    private static final JsPath EMPTY = new JsPath(EMPTY_VECTOR);
    private static final @Regex String REGEX_SEPARATOR = "\\.";

    /**
     Returns the singleton empty path.
     @return the singleton empty path
     */
    public static JsPath empty()
    {
        return EMPTY;
    }

    private final Vector<Position> positions;

    JsPath()
    {
        positions = EMPTY_VECTOR;
    }

    JsPath(final Vector<Position> positions)
    {
        this.positions = positions;
    }

    JsPath(final Position position)
    {
        this(EMPTY_VECTOR.appendBack(position));
    }


    /**
     Compares this path with another given as a parameter by comparing in order each of their positions,
     one by one, until a result different than zero is returned or all the positions of any path are
     consumed.
     @param that the given path
     @return 1 if this is greater than that, -1 if this is lower than that, 0 otherwise
     @see Index#compareTo(Position) index.compareTo(position)
     @see Key#compareTo(Position) key.compareTo(position)
     */
    @Override
    public int compareTo(final JsPath that)
    {

        if (this.isEmpty() && requireNonNull(that).isEmpty()) return 0;
        if (that.isEmpty()) return 1;
        if (this.isEmpty()) return -1;

        int i = this.head()
                    .compareTo(that.head());

        return (i != 0) ? i : this.tail()
                                  .compareTo(that.tail());


    }

    /**
     Returns the head of this path if it's not empty, throwing an exception otherwise.
     @return the head of the path witch is an object of type Position representing and Index or a Key
     @throws UnsupportedOperationException if the path is empty
     */
    public Position head()
    {
        return positions.head();

    }

    /**
     Provides a declarative way parse implementing an if-else statement based on the condition of if
     this path is empty or not. The value returned by each branch is computed by a supplier.
     @param emptySupplier the supplier that will compute the result only if the path is empty
     @param noneEmptySupplier the supplier that will compute the result only if the path is not empty
     @param <T> the type of the result
     @return an object of type T
     */
    public <T> T ifEmptyElse(Supplier<T> emptySupplier,
                             Supplier<T> noneEmptySupplier
                            )
    {

        return isEmpty() ? requireNonNull(emptySupplier).get() : requireNonNull(noneEmptySupplier).get();
    }

    /**
     Returns a sequential {@code Stream} of Positions with this path as its source.
     @return stream of Positions of this path
     */
    public Stream<Position> stream()
    {
        return JavaConverters.asJavaCollection(positions.iterator()
                                                        .toIterable())
                             .stream();
    }

    /**
     Provides a declarative way of implementing an if-else statement based on the condition given by
     the predicate. The value returned by each branch is computed by a supplier.
     @param predicate the given predicate
     @param ifTrueFn the supplier that will compute the result only if the path tested on the predicate is true
     @param ifFalseFn the supplier that will compute the result only if the path tested on the predicate is false
     @param <T> the type of the result
     @return an object of type T
     */
    public <T> T ifPredicateElse(Predicate<? super JsPath> predicate,
                                 Supplier<T> ifTrueFn,
                                 Supplier<T> ifFalseFn
                                )
    {

        return requireNonNull(predicate).test(this) ? requireNonNull(ifTrueFn).get() : requireNonNull(ifFalseFn).get();

    }

    /**
     Returns a new path incrementing the last index by one, throwing an UnsupportedOperationException
     if the last Position is not an index
     @return a new JsPath with the last index incremented by one
     @throws UnsupportedOperationException if the last position is not an Index
     */
    public JsPath inc()
    {

        return last().match(key ->
                            {
                                throw new UnsupportedOperationException("inc parse Key. Index was expected.");
                            },
                            i -> this.init()
                                     .index(i + 1)
                           );


    }

    /**
     Returns a new path decrementing the last index by one, throwing an UnsupportedOperationException
     if the last Position is not an index
     @return a new JsPath with the last index decremented by one
     @throws UnsupportedOperationException if the last position is not an Index
     */
    public JsPath dec()
    {

        return last().match(key ->
                            {
                                throw new UnsupportedOperationException("dec parse Key. Index was expected.");
                            },
                            i -> this.init()
                                     .index(i - 1)
                           );


    }

    /**
     Returns a new path appending an index with the given value to the back of this path.
     @param i the value of the index to be appended
     @return a new JsPath with the Index appended to the back
     */
    public JsPath index(int i)
    {
        return new JsPath(positions.appendBack(Index.of(i)));

    }

    /**
     Returns a new path without the last Position of this path.
     @return a new JsPath without the last Position of this JsPath
     @throws UnsupportedOperationException if the JsPath is empty
     */
    public JsPath init()
    {
        return positions.isEmpty() ? EMPTY : new JsPath(positions.init());

    }

    /**
     Returns true if the path is empty. An empty path represents the empty key
     @return true if this path is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return positions.isEmpty();
    }

    /**

     @return false if this path is not empty
     */
    public boolean isNotEmpty()
    {
        return !positions.isEmpty();
    }

    /**
     creates a new JsPath appending the key to <code>this</code> JsPath.
     @param key the key name to be appended in raw, without encoding nor single-quoting like in {@link JsPath#of(String)} )}
     @return a JsPath with the key appended to the back
     */
    public JsPath key(String key)
    {
        return new JsPath(positions.appendBack(Key.of(requireNonNull(key))));


    }


    /**
     returns the last parse <code>this</code> JsPath if it's not empty or a exception otherwise.
     @return the last parse the JsPath witch is an object parse type Position representing and Index or a Key

     @throws UnsupportedOperationException if the JsPath is empty
     */
    public Position last()
    {
        return positions.last();
    }

    /**
     @return the number parse Position (keys and indexes) parse <code>this</code> JsPath
     */
    public int size()
    {
        return positions.length();
    }

    /**
     returns a path from a key
     @param key the name of the key
     @return a new JsPath
     */
    public static  JsPath fromKey(final String key){
        return EMPTY.key(requireNonNull(key));
    }

    /**
     returns a path from an index
     @param i the index
     @return a new JsPath
     */
    public static  JsPath fromIndex(final int i){
        return EMPTY.index(i);
    }

    public static JsPath of(final String path)
    {
        if (requireNonNull(path).equals("")) return EMPTY.key("");

        //errorProne warning https://errorprone.info/bugpattern/StringSplitter;
        String[] tokens = requireNonNull(path).split(REGEX_SEPARATOR,
                                                     -1
                                                    );
        Vector<Position> vector = EMPTY_VECTOR;
        for (String token : tokens)
            vector = vector.appendBack(mapToField(token));
        return new JsPath(vector);

    }


    /**

     @return a JsPath without the head parse <code>this</code> JsPath

     @throws UnsupportedOperationException if the JsPath is empty
     */
    public JsPath tail()
    {
        return new JsPath(positions.tail());

    }

    /**
     Returns a string representation of this path where key names are single quoted when they are numbers,
     and encoded in application/x-www-form-urlencoded format when they are strings, and indexes are left
     as they are, being each position separated from each other with a dot. White-space is encoded as +,
     see {@link URLEncoder#encode(String, String)} for more details.
     Examples:
     @return a string representation of this JsPath following the pattern urlEncode(string).number.'number'...
     */
    @Override
    public String toString()
    {
        if (positions.isEmpty()) return "";


        return positions.iterator()
                        .map(new AbstractFunction1<Position, String>()
                        {
                            @Override
                            public String apply(final Position pos)
                            {
                                return pos.match(key ->
                                                 {
                                                     try
                                                     {
                                                         if (key.equals("")) return key;
                                                         return isNumeric(key) ? String.format("'%s'",
                                                                                               key
                                                                                              ) : String.format("%s",
                                                                                                                URLEncoder.encode(key,
                                                                                                                                  UTF8
                                                                                                                                 )
                                                                                                               );
                                                     }
                                                     catch (UnsupportedEncodingException e)
                                                     {

                                                         throw new IllegalArgumentException(e);

                                                     }
                                                 },
                                                 Integer::toString
                                                );


                            }
                        })
                        .mkString("",
                                  ".",
                                  ""
                                 );

    }

    private static Position mapToField(final String token)
    {
        assert token != null;
        try
        {
            if (token.equals("")) return KEY_EMPTY;
            if (token.equals("'")) return KEY_SINGLE_QUOTE;

            boolean isNumeric = isNumeric(token);
            if (isNumeric)
                return Index.of(Integer.valueOf(token));
            //token="'" case is covered before
            if (token.startsWith("'") && token.endsWith("'"))
                return Key.of(URLDecoder.decode(token.substring(1,
                                                                token.length() - 1
                                                               ),
                                                UTF8
                                               ));

            return Key.of(URLDecoder.decode(token,
                                            UTF8
                                           ));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    /**

     @param token not empty string
     @return true if is a valid numeric position in a path
     */
    private static boolean isNumeric(final String token)
    {
        return MINUS_ONE.equals(token) || token.chars()
                                               .allMatch(Character::isDigit);
    }

    private static final BiFunction<UnaryOperator<String>, Position, Position> mapKeyFn = (map, it) ->
    {

        if (it.isKey()) return Key.of(map.apply(it.asKey().name));
        else return it;
    };


    /**
     Creates a new JsPath applying the given map function to every key of this path.
     @param map the given map function
     @return a new JsPath with all its Keys mapped with the given function
     */
    public JsPath mapKeys(UnaryOperator<String> map)
    {

        requireNonNull(map);
        if (this.isEmpty()) return EMPTY;
        final Position head = this.head();
        final JsPath tail = this.tail();

        final JsPath headPath = new JsPath(mapKeyFn.apply(map,
                                                          head
                                                         ));
        if (tail.isEmpty()) return headPath;


        return headPath.append(tail.mapKeys(map));


    }


    /**
     Creates a new JsPath appending the given path to this path.
     @param path the given JsPath to be appended
     @return a new JsPath with the given JsPath appended to <code>this</code> JsPath
     */
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public JsPath append(final JsPath path)
    {
        return new JsPath(this.positions.$plus$plus(requireNonNull(path).positions,
                                                    bf
                                                   ));
    }

    /**
     Creates a new JsPath prepending the given path to this path.
     @param path the given path to be prepended
     @return a new JsPath with the given JsPath prepended to <code>this</code> JsPath
     */
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public JsPath prepend(final JsPath path)
    {
        return new JsPath(requireNonNull(path).positions.$plus$plus(this.positions,
                                                                    bf
                                                                   ));
    }

    /**
     Indicates whether some other object is "equal to" this path
     @param that the reference object with which to compare.
     @return true if that is a JsPath which represents the same location as this JsPath
     */
    @Override
    public boolean equals(@Nullable Object that)
    {
        if (that == null || getClass() != that.getClass()) return false;
        if (this == that) return true;
        final JsPath thatObj = (JsPath) that;
        if (isEmpty() && thatObj.isEmpty()) return true;
        if (isEmpty()) return false;
        if (thatObj.isEmpty()) return false;

        return this.head()
                   .equals(thatObj.head()) && this.tail()
                                                  .equals(thatObj.tail());


    }

    /**
     Returns the hashcode of this path
     @return hashcode of this JsPath
     */
    @Override
    public int hashCode()
    {
        return positions.hashCode();
    }


}
