/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2018 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import static jsonvalues.JsParser.Event.*;

class JsParser implements Closeable
{
    private static final JsParser.BufferPool pool = new JsParser.BufferPool();

    /**
     * An event from {@code JsParser}.
     */
    enum Event
    {
        /**
         * Start of a JSON array. The position of the parser is after '['.
         */
        START_ARRAY,
        /**
         * Start of a JSON object. The position of the parser is after '{'.
         */
        START_OBJECT,
        /**
         * Name in a name/value pair of a JSON object. The position of the parser
         * is after the key name. The method {@link #getString} returns the key
         * name.
         */
        KEY_NAME,
        /**
         * String value in a JSON array or object. The position of the parser is
         * after the string value. The method {@link #getString}
         * returns the string value.
         */
        VALUE_STRING,
        /**
         * Number value in a JSON array or object. The position of the parser is
         * after the number value. {@code JsParser} provides the following
         * methods to access the number value: {@link #getInt},
         * {@link #getLong}, and {@link #getBigDecimal}.
         */
        VALUE_NUMBER,
        /**
         * {@code true} value in a JSON array or object. The position of the
         * parser is after the {@code true} value. 
         */
        VALUE_TRUE,
        /**
         * {@code false} value in a JSON array or object. The position of the
         * parser is after the {@code false} value.
         */
        VALUE_FALSE,
        /**
         * {@code null} value in a JSON array or object. The position of the
         * parser is after the {@code null} value.
         */
        VALUE_NULL,
        /**
         * End of a JSON object. The position of the parser is after '}'.
         */
        END_OBJECT,
        /**
         * End of a JSON array. The position of the parser is after ']'.
         */
        END_ARRAY
    }

    private Context currentContext = new NoneContext();
    @Nullable Event currentEvent;

    private final Stack stack = new Stack();
    private final Tokenizer tokenizer;

    JsParser(Reader reader)
    {
        tokenizer = new Tokenizer(Objects.requireNonNull(reader),
                                  pool
        );
    }

    String getString()
    {

        return tokenizer.getString();

    }

    private boolean isIntegralNumber()
    {
        return tokenizer.isIntegral();
    }

    private int getInt()
    {
        return tokenizer.getInt();
    }

    private boolean isDefinitelyInt()
    {
        return tokenizer.isDefinitelyInt();
    }

    private boolean isDefinitelyLong()
    {
        return tokenizer.isDefinitelyLong();
    }

    private long getLong()
    {
        return tokenizer.getLong();
    }

    BigDecimal getBigDecimal()
    {
        return tokenizer.getBigDecimal();
    }

    BigInteger getBigInteger()
    {
        return tokenizer.getBigInteger();
    }

    JsStr getJsString()
    {

        return tokenizer.getJsString();
    }

    JsNumber getJsNumber()
    {

        if (isDefinitelyInt())
        {
            return JsInt.of(getInt());
        } else if (isDefinitelyLong())
        {
            return JsLong.of(getLong());
        } else if (isIntegralNumber())
        {
            return JsBigInt.of(getBigInteger());
        }
        return JsBigDec.of(getBigDecimal());
    }

    Location getLocation()
    {
        return tokenizer.getLocation();
    }

    Location getLastCharLocation()
    {
        return tokenizer.getLastCharLocation();
    }

    @Nullable Event next() throws MalformedJson
    {
        return currentEvent = currentContext.getNextEvent();
    }

    @Override
    public void close()
    {
        try
        {
            tokenizer.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("I/O error while closing JSON tokenizer",
                                       e
            );
        }
    }

    private static final class Stack
    {
        private @Nullable Context head;

        private void push(Context context)
        {
            context.next = head;
            head = context;
        }

        private Context pop()
        {
            if (head == null) throw new NoSuchElementException();
            Context temp = head;
            head = head.next;
            return temp;
        }
    }

    private abstract static class Context
    {
        @Nullable Context next;

        abstract @Nullable Event getNextEvent() throws MalformedJson;

    }

    private final class NoneContext extends Context
    {

        @Override
        @Nullable Event getNextEvent() throws MalformedJson
        {
            // Handle 1. {   2. [   3. value
            Tokenizer.Token token = tokenizer.nextToken();
            if (token == Tokenizer.Token.CURLYOPEN)
            {
                stack.push(currentContext);
                currentContext = new ObjectContext();
                return Event.START_OBJECT;
            } else if (token == Tokenizer.Token.SQUAREOPEN)
            {
                stack.push(currentContext);
                currentContext = new ArrayContext();
                return Event.START_ARRAY;
            } else if (token.isValue())
            {
                return token.getEvent();
            }
            throw MalformedJson.invalidToken(token,
                                             getLastCharLocation(),
                                             "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]"
                                            );
        }


    }


    private final class ObjectContext extends Context
    {
        private boolean firstValue = true;

        /*
         * Some more things could be optimized. For example, instead
         * tokenizer.nextToken(), one could use tokenizer.matchColonToken() to
         * match ':'. That might optimize a bit, but will fragment nextToken().
         * I think the current one is more readable.
         *
         */
        @Override
        @Nullable Event getNextEvent() throws MalformedJson
        {
            // Handle 1. }   2. name:value   3. ,name:value
            Tokenizer.Token token;
            if (currentEvent == Event.KEY_NAME) token = tokenizer.matchColonToken();
            else if (currentEvent == Event.START_OBJECT) token = tokenizer.matchQuoteOrCloseObject();
            else token = tokenizer.nextToken();

            if (token == Tokenizer.Token.EOF)
            {
                if (currentEvent == null) return null;
                switch (currentEvent)
                {
                    case START_OBJECT:
                        throw MalformedJson.invalidToken(token,
                                                         getLastCharLocation(),
                                                         "[STRING, CURLYCLOSE]"
                                                        );
                    case KEY_NAME:
                        throw MalformedJson.invalidToken(token,
                                                         getLastCharLocation(),
                                                         "[COLON]"
                                                        );
                    default:
                        throw MalformedJson.invalidToken(token,
                                                         getLastCharLocation(),
                                                         "[COMMA, CURLYCLOSE]"
                                                        );
                }
            } else if (currentEvent == Event.KEY_NAME)
            {

                token = tokenizer.nextToken();
                if (token.isValue())
                {
                    return token.getEvent();
                } else if (token == Tokenizer.Token.CURLYOPEN)
                {
                    stack.push(currentContext);
                    currentContext = new ObjectContext();
                    return Event.START_OBJECT;
                } else if (token == Tokenizer.Token.SQUAREOPEN)
                {
                    stack.push(currentContext);
                    currentContext = new ArrayContext();
                    return Event.START_ARRAY;
                }
                throw MalformedJson.invalidToken(token,
                                                 getLastCharLocation(),
                                                 "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]"
                                                );
            } else
            {
                // Handle 1. }   2. name   3. ,name
                if (token == Tokenizer.Token.CURLYCLOSE)
                {
                    currentContext = stack.pop();
                    return Event.END_OBJECT;
                }
                if (firstValue)
                {
                    firstValue = false;
                } else
                {
                    if (token != Tokenizer.Token.COMMA)
                    {
                        throw MalformedJson.invalidToken(token,
                                                         getLastCharLocation(),
                                                         "[COMMA]"
                                                        );
                    }
                    token = tokenizer.nextToken();
                }
                if (token == Tokenizer.Token.STRING)
                {
                    return Event.KEY_NAME;
                }
                throw MalformedJson.invalidToken(token,
                                                 getLastCharLocation(),
                                                 "[STRING]"
                                                );
            }
        }


    }

    private final class ArrayContext extends Context
    {
        private boolean firstValue = true;

        // Handle 1. ]   2. value   3. ,value

        @Override
        @Nullable Event getNextEvent() throws MalformedJson
        {
            Tokenizer.Token token = tokenizer.nextToken();
            if (token == Tokenizer.Token.EOF)
            {
                if (currentEvent == null) return null;
                switch (currentEvent)
                {
                    case START_ARRAY:
                        throw MalformedJson.invalidToken(token,
                                                         getLastCharLocation(),
                                                         "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]"
                                                        );
                    default:
                        throw MalformedJson.invalidToken(token,
                                                         getLastCharLocation(),
                                                         "[COMMA, CURLYCLOSE]"
                                                        );
                }
            }
            if (token == Tokenizer.Token.SQUARECLOSE)
            {
                currentContext = stack.pop();
                return Event.END_ARRAY;
            }
            if (firstValue)
            {
                firstValue = false;
            } else
            {
                if (token != Tokenizer.Token.COMMA)
                {
                    throw MalformedJson.invalidToken(token,
                                                     getLastCharLocation(),
                                                     "[COMMA]"
                                                    );
                }
                token = tokenizer.nextToken();
            }
            if (token.isValue())
            {
                return token.getEvent();
            } else if (token == Tokenizer.Token.CURLYOPEN)
            {
                stack.push(currentContext);
                currentContext = new ObjectContext();
                return Event.START_OBJECT;
            } else if (token == Tokenizer.Token.SQUAREOPEN)
            {
                stack.push(currentContext);
                currentContext = new ArrayContext();
                return Event.START_ARRAY;
            }
            throw MalformedJson.invalidToken(token,
                                             getLastCharLocation(),
                                             "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]"
                                            );
        }

    }

    /**
     * @author Jitendra Kotamraju
     */
    static class Location
    {

        private final long columnNo;
        private final long lineNo;
        private final long offset;

        Location(long lineNo,
                 long columnNo,
                 long streamOffset
                )
        {
            this.lineNo = lineNo;
            this.columnNo = columnNo;
            this.offset = streamOffset;
        }

        @Override
        public String toString()
        {
            return "(line no=" + lineNo + ", column no=" + columnNo + ", offset=" + offset + ")";
        }

    }

    /**
     * char[] pool that pool instances of char[] which are expensive to create.
     *
     * @author Jitendra Kotamraju
     */
    static class BufferPool
    {

        // volatile since multiple threads may access queue reference
        private volatile @Nullable WeakReference<ConcurrentLinkedQueue<char[]>> queue;

        /**
         * Gets a new object from the pool.
         *
         * <p>
         * If no object is available in the pool, this method creates a new one.
         *
         * @return
         *      always non-null.
         */
        final char[] take()
        {
            char[] t = getQueue().poll();
            if (t == null)
                return new char[4096];
            return t;
        }

        private ConcurrentLinkedQueue<char[]> getQueue()
        {
            WeakReference<ConcurrentLinkedQueue<char[]>> q = queue;
            if (q != null)
            {
                ConcurrentLinkedQueue<char[]> d = q.get();
                if (d != null)
                    return d;
            }

            // overwrite the queue
            ConcurrentLinkedQueue<char[]> d = new ConcurrentLinkedQueue<>();
            queue = new WeakReference<>(d);

            return d;
        }

        /**
         * Returns an object back to the pool.
         */
        final void recycle(char[] t)
        {
            getQueue().offer(t);
        }

    }

    /**
     * JSON Tokenizer
     *
     * @author Jitendra Kotamraju
     */
    static final class Tokenizer implements Closeable
    {

        // Table to look up hex ch -> value (for e.g HEX['F'] = 15, HEX['5'] = 5)
        private static final int[] HEX = new int[128];

        static
        {
            Arrays.fill(HEX,
                        -1
                       );
            for (int i = '0'; i <= '9'; i++)
            {
                HEX[i] = i - '0';
            }
            for (int i = 'A'; i <= 'F'; i++)
            {
                HEX[i] = 10 + i - 'A';
            }
            for (int i = 'a'; i <= 'f'; i++)
            {
                HEX[i] = 10 + i - 'a';
            }
        }

        private static final int HEX_LENGTH = HEX.length;

        private final BufferPool bufferPool;

        private final Reader reader;

        // Internal buffer that is used for parsing. It is also used
        // for storing current string and number value token
        private char[] buf;

        // Indexes in buffer
        //
        // XXXssssssssssssXXXXXXXXXXXXXXXXXXXXXXrrrrrrrrrrrrrrXXXXXX
        //    ^           ^                     ^             ^
        //    |           |                     |             |
        //   storeBegin  storeEnd            readBegin      readEnd
        private int readBegin;
        private int readEnd;
        private int storeBegin;
        private int storeEnd;

        // line number of the current pointer of parsing char
        private long lineNo = 1;

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        // ^
        // |
        // bufferOffset
        //
        // offset of the last \r\n or \n. will be used to calculate column number
        // of a token or an error. This may be outside of the buffer.
        private long lastLineOffset = 0;
        // offset in the stream for the start of the buffer, will be used in
        // calculating Location's stream offset, column no.
        private long bufferOffset = 0;

        private boolean minus;
        private boolean fracOrExp;

        JsStr getJsString()
        {

            return JsStr.of(getString());

        }


        enum Token
        {
            CURLYOPEN(START_OBJECT,
                      false
            ),
            SQUAREOPEN(START_ARRAY,
                       false
            ),
            COLON(null,
                  false
            ),
            COMMA(null,
                  false
            ),
            STRING(VALUE_STRING,
                   true
            ),
            NUMBER(VALUE_NUMBER,
                   true
            ),
            TRUE(VALUE_TRUE,
                 true
            ),
            FALSE(VALUE_FALSE,
                  true
            ),
            NULL(VALUE_NULL,
                 true
            ),
            CURLYCLOSE(END_OBJECT,
                       false
            ),
            SQUARECLOSE(END_ARRAY,
                        false
            ),
            EOF(null,
                false
            );

            private final @Nullable Event event;
            private final boolean value;

            Token(@Nullable Event event,
                  boolean value
                 )
            {
                this.event = event;
                this.value = value;
            }

            @Nullable Event getEvent()
            {
                return event;
            }

            boolean isValue()
            {
                return value;
            }
        }

        Tokenizer(Reader reader,
                  BufferPool bufferPool
                 )
        {
            this.reader = reader;
            this.bufferPool = bufferPool;
            buf = bufferPool.take();
        }

        private void readString() throws MalformedJson
        {
            // when inPlace is true, no need to copy chars
            boolean inPlace = true;
            storeBegin = storeEnd = readBegin;

            do
            {
                // Write unescaped char block within the current buffer
                if (inPlace)
                {
                    int ch;
                    while (readBegin < readEnd && ((ch = buf[readBegin]) >= 0x20) && ch != '\\')
                    {
                        if (ch == '"')
                        {
                            storeEnd = readBegin++; // ++ to consume quote char
                            return;                 // Got the entire string
                        }
                        readBegin++;                // consume unescaped char
                    }
                    storeEnd = readBegin;
                }

                // string may be crossing buffer boundaries and may contain
                // escaped characters.
                int ch = read();
                if (ch >= 0x20 && ch != 0x22 && ch != 0x5c)
                {
                    if (!inPlace)
                    {
                        buf[storeEnd] = (char) ch;
                    }
                    storeEnd++;
                    continue;
                }
                switch (ch)
                {
                    case '\\':
                        inPlace = false;        // Now onwards need to copy chars
                        unescape();
                        break;
                    case '"':
                        return;
                    default:
                        throw MalformedJson.unexpectedChar(ch,
                                                           getLastCharLocation()
                                                          );
                }
            } while (true);
        }

        private void unescape() throws MalformedJson
        {
            int ch = read();
            switch (ch)
            {
                case 'b':
                    buf[storeEnd++] = '\b';
                    break;
                case 't':
                    buf[storeEnd++] = '\t';
                    break;
                case 'n':
                    buf[storeEnd++] = '\n';
                    break;
                case 'f':
                    buf[storeEnd++] = '\f';
                    break;
                case 'r':
                    buf[storeEnd++] = '\r';
                    break;
                case '"':
                case '\\':
                case '/':
                    buf[storeEnd++] = (char) ch;
                    break;
                case 'u':
                {
                    int unicode = 0;
                    for (int i = 0; i < 4; i++)
                    {
                        int ch3 = read();
                        int digit = (ch3 >= 0 && ch3 < HEX_LENGTH) ? HEX[ch3] : -1;
                        if (digit < 0)
                        {
                            throw MalformedJson.unexpectedChar(ch3,
                                                               getLastCharLocation()
                                                              );
                        }
                        unicode = (unicode << 4) | digit;
                    }
                    buf[storeEnd++] = (char) unicode;
                    break;
                }
                default:
                    throw MalformedJson.unexpectedChar(ch,
                                                       getLastCharLocation()
                                                      );
            }
        }

        // Reads a number char. If the char is within the buffer, directly
        // reads from the buffer. Otherwise, uses read() which takes care
        // of resizing, filling up the buf, adjusting the pointers
        private int readNumberChar()
        {
            if (readBegin < readEnd)
            {
                return buf[readBegin++];
            } else
            {
                storeEnd = readBegin;
                return read();
            }
        }

        private void readNumber(int ch) throws MalformedJson
        {
            storeBegin = storeEnd = readBegin - 1;
            // sign
            if (ch == '-')
            {
                this.minus = true;
                ch = readNumberChar();
                if (ch < '0' || ch > '9')
                {
                    throw MalformedJson.unexpectedChar(ch,
                                                       getLastCharLocation()
                                                      );
                }
            }

            // int
            if (ch == '0')
            {
                ch = readNumberChar();
            } else
            {
                do
                {
                    ch = readNumberChar();
                } while (ch >= '0' && ch <= '9');
            }

            // frac
            if (ch == '.')
            {
                this.fracOrExp = true;
                int count = 0;
                do
                {
                    ch = readNumberChar();
                    count++;
                } while (ch >= '0' && ch <= '9');
                if (count == 1)
                {
                    throw MalformedJson.unexpectedChar(ch,
                                                       getLastCharLocation()
                                                      );
                }
            }

            // exp
            if (ch == 'e' || ch == 'E')
            {
                this.fracOrExp = true;
                ch = readNumberChar();
                if (ch == '+' || ch == '-')
                {
                    ch = readNumberChar();
                }
                int count;
                for (count = 0; ch >= '0' && ch <= '9'; count++)
                {
                    ch = readNumberChar();
                }
                if (count == 0)
                {
                    throw MalformedJson.unexpectedChar(ch,
                                                       getLastCharLocation()
                                                      );
                }
            }
            if (ch != -1)
            {
                // Only reset readBegin if eof has not been reached
                readBegin--;
                storeEnd = readBegin;
            }
        }

        private void readTrue() throws MalformedJson
        {
            int ch1 = read();
            if (ch1 != 'r')
            {
                throw MalformedJson.unexpectedChar(ch1,
                                                   getLastCharLocation()
                                                  );
            }
            int ch2 = read();
            if (ch2 != 'u')
            {
                throw MalformedJson.unexpectedChar(ch2,
                                                   getLastCharLocation()
                                                  );
            }
            int ch3 = read();
            if (ch3 != 'e')
            {
                throw MalformedJson.unexpectedChar(ch3,
                                                   getLastCharLocation()
                                                  );
            }
        }

        private void readFalse() throws MalformedJson
        {
            int ch1 = read();
            if (ch1 != 'a')
            {
                throw MalformedJson.expectedChar(ch1,
                                                 getLastCharLocation(),
                                                 'a'
                                                );

            }
            int ch2 = read();
            if (ch2 != 'l')
            {
                throw MalformedJson.expectedChar(ch2,
                                                 getLastCharLocation(),
                                                 'l'
                                                );

            }
            int ch3 = read();
            if (ch3 != 's')
            {
                throw MalformedJson.expectedChar(ch3,
                                                 getLastCharLocation(),
                                                 's'
                                                );
            }
            int ch4 = read();
            if (ch4 != 'e')
            {
                throw MalformedJson.expectedChar(ch4,
                                                 getLastCharLocation(),
                                                 'e'
                                                );
            }
        }

        private void readNull() throws MalformedJson
        {
            int ch1 = read();
            if (ch1 != 'u')
            {
                throw MalformedJson.expectedChar(ch1,
                                                 getLastCharLocation(),
                                                 'u'
                                                );
            }
            int ch2 = read();
            if (ch2 != 'l')
            {
                throw MalformedJson.expectedChar(ch2,
                                                 getLastCharLocation(),
                                                 'l'
                                                );
            }
            int ch3 = read();
            if (ch3 != 'l')
            {
                throw MalformedJson.expectedChar(ch3,
                                                 getLastCharLocation(),
                                                 'l'
                                                );
            }
        }


        Token nextToken() throws MalformedJson
        {
            reset();
            int ch = read();

            ch = skipWhitespaces(ch);


            switch (ch)
            {
                case '"':
                    readString();
                    return Token.STRING;
                case '{':
                    return Token.CURLYOPEN;
                case '[':
                    return Token.SQUAREOPEN;
                case ',':
                    return Token.COMMA;
                case 't':
                    readTrue();
                    return Token.TRUE;
                case 'f':
                    readFalse();
                    return Token.FALSE;
                case 'n':
                    readNull();
                    return Token.NULL;
                case ']':
                    return Token.SQUARECLOSE;
                case '}':
                    return Token.CURLYCLOSE;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '-':
                    readNumber(ch);
                    return Token.NUMBER;
                case -1:
                    return Token.EOF;
                default:
                    throw MalformedJson.unexpectedChar(ch,
                                                       getLastCharLocation()
                                                      );

            }
        }


        /*
         * Could be optimized if the parser uses separate methods to match colon
         * etc (that would avoid the switch statement cost in certain cases)
         */
        Token matchColonToken() throws MalformedJson
        {
            reset();
            int ch = read();

            // whitespace
            ch = skipWhitespaces(ch);

            if (ch != ':') throw MalformedJson.expectedChar(ch,
                                                            getLastCharLocation(),
                                                            ':'
                                                           );

            return Token.COLON;


        }

        Token matchQuoteOrCloseObject() throws MalformedJson
        {
            reset();
            int ch = read();

            ch = skipWhitespaces(ch);

            if (ch == '"')
            {
                readString();
                return Token.STRING;
            }

            if (ch == '}') return Token.CURLYCLOSE;

            throw MalformedJson.expectedChar(ch,
                                             getLastCharLocation(),
                                             '"',
                                             '}'
                                            );
        }

        private int skipWhitespaces(int ch)
        {
            while (ch == 0x20 || ch == 0x09 || ch == 0x0a || ch == 0x0d)
            {
                if (ch == '\r')
                {
                    ++lineNo;
                    ch = read();
                    if (ch == '\n')
                    {
                        lastLineOffset = bufferOffset + readBegin;
                    } else
                    {
                        lastLineOffset = bufferOffset + readBegin - 1;
                        continue;
                    }
                } else if (ch == '\n')
                {
                    ++lineNo;
                    lastLineOffset = bufferOffset + readBegin;
                }
                ch = read();
            }
            return ch;
        }

        // Gives the location of the last char. Used for
        // ParsingException.getLocation
        Location getLastCharLocation()
        {
            // Already read the char, so subtracting -1
            return new Location(lineNo,
                                bufferOffset + readBegin - lastLineOffset,
                                bufferOffset + readBegin - 1
            );
        }

        // Gives the parser location. Used for JsParser.getLocation
        Location getLocation()
        {
            return new Location(lineNo,
                                bufferOffset + readBegin - lastLineOffset + 1,
                                bufferOffset + readBegin
            );
        }

        private int read()
        {
            try
            {
                if (readBegin == readEnd)
                {     // need to fill the buffer
                    int len = fillBuf();
                    if (len == -1)
                    {
                        return -1;
                    }
                    assert len != 0;
                    readBegin = storeEnd;
                    readEnd = readBegin + len;
                }
                return buf[readBegin++];
            }
            catch (IOException ioe)
            {
                throw new RuntimeException("I/O error while parsing JSON",
                                           ioe
                );
            }
        }

        private int fillBuf() throws IOException
        {
            if (storeEnd != 0)
            {
                int storeLen = storeEnd - storeBegin;
                if (storeLen > 0)
                {
                    // there is some store data
                    if (storeLen == buf.length)
                    {
                        // buffer is full, double the capacity
                        char[] doubleBuf = Arrays.copyOf(buf,
                                                         2 * buf.length
                                                        );
                        bufferPool.recycle(buf);
                        buf = doubleBuf;
                    } else
                    {
                        // Left shift all the stored data to make space
                        System.arraycopy(buf,
                                         storeBegin,
                                         buf,
                                         0,
                                         storeLen
                                        );
                        storeEnd = storeLen;
                        storeBegin = 0;
                        bufferOffset += readBegin - storeEnd;
                    }
                } else
                {
                    storeBegin = storeEnd = 0;
                    bufferOffset += readBegin;
                }
            } else
            {
                bufferOffset += readBegin;
            }
            // Fill the rest of the buf
            return reader.read(buf,
                               storeEnd,
                               buf.length - storeEnd
                              );
        }

        // state associated with the current token is no more valid
        private void reset()
        {
            if (storeEnd != 0)
            {
                storeBegin = 0;
                storeEnd = 0;
                minus = false;
                fracOrExp = false;
            }
        }

        String getString()
        {
            return new String(buf,
                              storeBegin,
                              storeEnd - storeBegin
            );
        }

        BigDecimal getBigDecimal()
        {

            return new BigDecimal(buf,
                                  storeBegin,
                                  storeEnd - storeBegin
            );

        }

        BigInteger getBigInteger()
        {
            return getBigDecimal().toBigIntegerExact();

        }

        int getInt()
        {
            // no need to create BigDecimal for common integer values (1-9 digits)
            int storeLen = storeEnd - storeBegin;

            int num = 0;
            int i = minus ? 1 : 0;
            for (; i < storeLen; i++)
            {
                final char c = buf[storeBegin + i];
                num = num * 10 + (c - '0');
            }
            return minus ? -num : num;

        }

        long getLong()
        {
            // no need to create BigDecimal for common integer values (1-18 digits)
            int storeLen = storeEnd - storeBegin;

            long num = 0;
            int i = minus ? 1 : 0;
            for (; i < storeLen; i++)
            {
                num = num * 10 + (buf[storeBegin + i] - '0');
            }
            return minus ? -num : num;

        }

        // returns true for common integer values (1-9 digits).
        // So there are cases it will return false even though the number is int
        boolean isDefinitelyInt()
        {
            int storeLen = storeEnd - storeBegin;
            return !fracOrExp && (storeLen <= 9 || (minus && storeLen <= 10));
        }

        // returns true for common long values (1-18 digits).
        // So there are cases it will return false even though the number is long
        boolean isDefinitelyLong()
        {
            int storeLen = storeEnd - storeBegin;
            return !fracOrExp && (storeLen <= 18 || (minus && storeLen <= 19));
        }

        boolean isIntegral()
        {
            return !fracOrExp || getBigDecimal().scale() == 0;
        }

        @Override
        public void close() throws IOException
        {
            reader.close();
            bufferPool.recycle(buf);
        }


    }

}