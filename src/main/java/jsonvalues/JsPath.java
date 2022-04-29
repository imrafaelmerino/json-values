package jsonvalues;

import io.vavr.collection.Vector;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * <pre>
 * Represents the full path location of an element in a json. It's a list of {@link Position}. Exists
 * two different ways to create a JsPath:
 * - From a path-like string using the static factory method {@link JsPath#path(String)}, where the path
 * follows the Json Pointer specification <a href="http://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 * In order to be able to use paths to put data in a Json, keys which name are numbers have to
 * to be single-quoted:
 * {@code
 * a={"0": true}
 * b=[false]
 * }
 * According to the rfc 6901:
 * the pointer /0 points to true in a, and to false in b.
 * In json-values it's slightly different:
 * /0 points to false in b, and /'0' points to true in a.
 * It's necessary to make that distinction because otherwise, there are scenarios when there is no way
 * to know if the user wants to insert an array or an object:
 * {@code <script>}
 * JsObj obj = empty.put("/a/0/0",true)
 * obj = {"a":[[true]]}       //valid result according to the rfc and json-values
 * obj = {"a":{"0":{"0":true} //valid result according to the rfc
 * {@code <script>}
 * - By the API, using the methods {@link JsPath#fromKey(String)} and {@link JsPath#fromIndex(int)} to create
 * a JsPath an then the methods {@link JsPath#index(int)} and {@link JsPath#key(String)} to append keys or indexes:
 * {@code <script>}
 * JsPath a = JsPath.fromKey("a").index(0).key("b") =  /a/0/b
 * JsPath b = JsPath.fromIndex(0).key("a").index(0) =  /0/a/0
 * {@code <script>}
 * For example, given the following Json object:
 * {
 * "a": { "x":[ {"c": [1,2, { "": { "1" : true, " ": false, "'": 4} }]} ]}}
 * "1": null,
 * "": ""
 * }
 * / = ""                      //an empty string is a valid name for a key
 * /'1' = null                 //numeric keys have to be single-quoted
 * /a/x/0/c/0 = 1
 * /a/x/0/c/1 = 2
 * /a/x/0/c/2//'1' = true      // single quotes are only mandatory when the key is a number
 * according to the rfc, # at the beginning indicates that the path is a fragment of an url and
 * therefore the keys have to be url-encoded:
 * #/a/x/0/c/2//+" = false     // + is url-decoded to the white-space
 * #/a/x/0/c/2//%27" = 4       // %27 is url-decoded to '
 * and using the API:
 * {@code <script>}
 * fromKey("") = ""
 * fromKey("1") = null
 * fromKey("a").key("x").index(0).key("c").index(0) = 1
 * fromKey("a").key("x").index(0).key("c").index(1) = 2
 * fromKey("a").key("x").index(0).key("c").index(2).key("").key("1") = true
 * fromKey("a").key("x").index(0).key("c").index(2).key("").key(" ") = false
 * fromKey("a").key("x").index(0).key("c").index(2).key("").key("'") = 4
 * {@code <script>}
 * </pre>
 */
@SuppressWarnings("UnnecessaryLambda")
public final class JsPath implements Comparable<JsPath> {
    private static final Key KEY_EMPTY = Key.of("");
    private static final Key KEY_SINGLE_QUOTE = Key.of("'");
    private static final String MINUS_ONE = "-1";
    private static final Vector<Position> EMPTY_VECTOR = Vector.empty();
    private static final JsPath EMPTY = new JsPath(EMPTY_VECTOR);
    private static final BiFunction<UnaryOperator<String>, Position, Position> mapKeyFn = (map, it) ->
    {

        if (it.isKey()) return Key.of(map.apply(it.asKey().name));
        else return it;
    };
    private static final UnaryOperator<String> escape =
            token -> token.replace("~1",
                                   "/"
                          )
                          .replace("~0",
                                   "~");
    private static final UnaryOperator<String> decode = token ->
    {
        try {
            return URLDecoder.decode(token,
                                     "UTF-8"
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    };
    private final Vector<Position> positions;


    JsPath(final Position position) {
        this(EMPTY_VECTOR.append(position));
    }

    JsPath(final Vector<Position> positions) {
        this.positions = positions;
    }

    /**
     * Returns the singleton empty path.
     *
     * @return the singleton empty path
     */
    public static JsPath empty() {
        return EMPTY;
    }

    /**
     * returns a path from an index
     *
     * @param i the index
     * @return a new JsPath
     */
    public static JsPath fromIndex(final int i) {
        return EMPTY.index(i);
    }

    /**
     * returns a JsPath from a set of keys
     *
     * @param key    the first key of the path
     * @param others the rest of the keys
     * @return a new JsPath
     */
    public static JsPath path(final String key,
                              final String... others) {

        return new JsPath(Vector.<Position>empty().append(Key.of(Objects.requireNonNull(key))).appendAll(
                Arrays.stream(requireNonNull(others)).map(Key::of).collect(Collectors.toList())));
    }


    public static JsPath path(final String path) {
        if (requireNonNull(path).equals("")) return EMPTY;
        if (path.equals("#")) return EMPTY;
        if (path.equals("#/")) return fromKey("");
        if (path.equals("/")) return fromKey("");
        if (!path.startsWith("#/") && !path.startsWith("/")) throw UserError.pathMalformed(path);
        if (path.startsWith("#")) return parse(mapTokenToPosition(t -> escape.andThen(decode)
                                                                             .apply(t))
        ).apply(path.substring(2));
        return parse(mapTokenToPosition(escape)
        ).apply(path.substring(1));
    }

    /**
     * returns a path from a key
     *
     * @param key the name of the key
     * @return a new JsPath
     */
    public static JsPath fromKey(final String key) {
        return EMPTY.key(requireNonNull(key));
    }

    private static Function<String, JsPath> parse(final Function<String, Position> mapFn) {
        return path ->
        {
            String[] tokens = requireNonNull(path).split("/",
                                                         -1
            );
            Vector<Position> vector = EMPTY_VECTOR;
            for (String token : tokens) vector = vector.append(mapFn.apply(token));
            return new JsPath(vector);
        };
    }

    private static Function<String, Position> mapTokenToPosition(final UnaryOperator<String> mapKeyFn) {
        return token ->
        {
            if (token.equals("")) return KEY_EMPTY;
            if (token.equals("'")) return KEY_SINGLE_QUOTE;
            boolean isNumeric = isNumeric(token);
            if (isNumeric) {
                if (token.length() > 1 && token.startsWith("0")) throw UserError.indexWithLeadingZeros(token);
                return Index.of(Integer.parseInt(token));
            }
            //token="'" case is covered before
            if (token.startsWith("'") && token.endsWith("'"))
                return Key.of(mapKeyFn.apply(token.substring(1,
                                                             token.length() - 1
                              ))
                );
            return Key.of(mapKeyFn.apply(token));
        };
    }

    /**
     * creates a new JsPath appending the key to <code>this</code> JsPath.
     *
     * @param key the key name to be appended in raw, without encoding nor single-quoting like in {@link JsPath#path(String)} )}
     * @return a JsPath with the key appended to the back
     */
    public JsPath key(final String key) {
        return new JsPath(positions.append(Key.of(requireNonNull(key))));
    }

    /**
     * @param token not empty string
     * @return true if is a valid numeric position in a path
     */
    private static boolean isNumeric(final String token) {
        return MINUS_ONE.equals(token) || token.chars()
                                               .allMatch(Character::isDigit);
    }

    /**
     * Compares this path with another given as a parameter by comparing in order each of their positions,
     * one by one, until a result different from zero is returned or all the positions of any path are
     * consumed.
     *
     * @param that the given path
     * @return 1 if this is greater than that, -1 if this is lower than that, 0 otherwise
     * @see Index#compareTo(Position) index.compareTo(position)
     * @see Key#compareTo(Position) key.compareTo(position)
     */
    @Override
    public int compareTo(final JsPath that) {

        if (this.isEmpty() && requireNonNull(that).isEmpty()) return 0;
        if (that.isEmpty()) return 1;
        if (this.isEmpty()) return -1;

        int i = this.head()
                    .compareTo(that.head());

        return (i != 0) ?
               i :
               this.tail()
                   .compareTo(that.tail());

    }

    /**
     * Returns the head of this path if it's not empty, throwing an exception otherwise.
     *
     * @return the head of the path witch is an object of type Position representing and Index or a Key
     * @throws UserError if the path is empty
     */
    public Position head() {
        if (isEmpty()) throw UserError.headOfEmptyPath();
        return positions.head();

    }

    /**
     * Returns a sequential {@code Stream} of Positions with this path as its source.
     *
     * @return stream of Positions of this path
     */
    public Stream<Position> stream() {
        return positions.toJavaStream();
    }

    /**
     * Returns a new path incrementing the last index by one, throwing an UserError
     * if the last Position is not an index
     *
     * @return a new JsPath with the last index incremented by one
     * @throws UserError if the last position is not an Index
     */
    public JsPath inc() {

        return last().match(key ->
                            {
                                throw UserError.incOfKey();
                            },
                            i -> this.init()
                                     .index(i + 1)
        );


    }

    /**
     * returns the last position <code>this</code> JsPath if it's not empty or a exception otherwise.
     *
     * @return the last position the JsPath witch is an object of type Position representing and Index or a Key
     * @throws UserError if the JsPath is empty
     */
    public Position last() {
        if (isEmpty()) throw UserError.lastOfEmptyPath();
        return positions.last();
    }

    /**
     * Returns a new path appending an index with the given value to the back of this path.
     *
     * @param i the value of the index to be appended
     * @return a new JsPath with the Index appended to the back
     */
    public JsPath index(final int i) {
        return new JsPath(positions.append(Index.of(i)));

    }

    /**
     * Returns a new path without the last Position of this path.
     *
     * @return a new JsPath without the last Position of this JsPath
     * @throws UserError if the JsPath is empty
     */
    public JsPath init() {
        if (isEmpty()) throw UserError.initOfEmptyPath();
        return new JsPath(positions.init());

    }

    /**
     * Returns true if the path is empty. An empty path represents the empty key
     *
     * @return true if this path is empty, false otherwise
     */
    public boolean isEmpty() {
        return positions.isEmpty();
    }

    /**
     * Returns a new path decrementing the last index by one, throwing an UserError
     * if the last Position is not an index
     *
     * @return a new JsPath with the last index decremented by one
     * @throws UserError if the last position is not an Index
     */
    public JsPath dec() {

        return last().match(key ->
                            {
                                throw UserError.decOfKey();
                            },
                            i -> this.init()
                                     .index(i - 1)
        );


    }

    /**
     * Returns the number of Position (keys and indexes) of <code>this</code> JsPath
     *
     * @return the number of Position (keys and indexes) of <code>this</code> JsPath
     */
    public int size() {
        return positions.length();
    }

    /**
     * Returns a JsPath without the head of this JsPath
     *
     * @return a JsPath without the head of this JsPath
     * @throws UserError if the JsPath is empty
     */
    public JsPath tail() {
        if (isEmpty()) throw UserError.tailOfEmptyPath();
        return new JsPath(positions.tail());
    }

    /**
     * Creates a new JsPath applying the given map function to every key of this path.
     *
     * @param map the given map function
     * @return a new JsPath with all its Keys mapped with the given function
     */
    public JsPath mapKeys(final UnaryOperator<String> map) {
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
     * Creates a new JsPath appending the given path to this path.
     *
     * @param path the given JsPath to be appended
     * @return a new JsPath with the given JsPath appended to <code>this</code> JsPath
     */
    public JsPath append(final JsPath path) {
        return new JsPath(this.positions.appendAll(requireNonNull(path).positions));
    }

    /**
     * Creates a new JsPath prepending the given path to this path.
     *
     * @param path the given path to be prepended
     * @return a new JsPath with the given JsPath prepended to <code>this</code> JsPath
     */
    public JsPath prepend(final JsPath path) {
        return new JsPath(requireNonNull(path).positions.appendAll(this.positions));
    }

    /**
     * returns true if this path starts with the given path. If the given path is JsPath.empty(), it
     * always returns true
     *
     * @param path the given path
     * @return true if this JsPath starts with the given JsPath
     */
    public boolean startsWith(final JsPath path) {
        if (requireNonNull(path).isEmpty()) return true;
        if (this.isEmpty()) return false;

        return this.head()
                   .equals(path.head()) && this.tail()
                                               .startsWith(path.tail());
    }

    /**
     * returns true if this path ends with the given path. If the given path is JsPath.empty(), it
     * always returns true
     *
     * @param path the given path
     * @return true if this JsPath ends with the given JsPath
     */
    public boolean endsWith(final JsPath path) {
        if (requireNonNull(path).isEmpty()) return true;
        if (this.isEmpty()) return false;

        return this.last()
                   .equals(path.last()) && this.init()
                                               .endsWith(path.init());
    }

    /**
     * Returns the hashcode of this path
     *
     * @return hashcode of this JsPath
     */
    @Override
    public int hashCode() {
        return positions.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this path
     *
     * @param that the reference object with which to compare.
     * @return true if that is a JsPath which represents the same location as this JsPath
     */
    @Override
    public boolean equals(final Object that) {
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
     * Returns a string representation of this path following the format defined in the RFC 6901 with
     * the exception that keys which names are numbers are single-quoted.
     * Example: /a/b/0/'1'/
     *
     * @return a string representation of this JsPath following the RFC 6901
     */
    @Override
    public String toString() {
        if (positions.isEmpty()) return "";
        return positions.iterator()
                        .map(pos -> pos.match(key ->
                                              {
                                                  if (key.equals("")) return key;
                                                  return isNumeric(key) ?
                                                         String.format("'%s'",
                                                                       key
                                                         ) :
                                                         key;
                                              },
                                              Integer::toString
                             )

                        )
                        .mkString("/",
                                  "/",
                                  ""
                        );
    }


}
