package jsonvalues.benchmark;

import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static jsonvalues.spec.JsSpecs.*;
import static jsonvalues.spec.JsSpecs.bool;

public class Conf
{
    public static final String PERSON_JSON;
    public static final String PERSON_jSON_SCHEMA;

    public final static JsObjSpec PERSON_SPEC = JsObjSpec.strict("firstName",
                                                           str,
                                                           "lastName",
                                                           str,
                                                           "age",
                                                           integer(greaterOrEqualThan(0)),
                                                           "latitude",
                                                           decimal(interval(new BigDecimal(-90),
                                                                            new BigDecimal(90)
                                                                           )
                                                                  ),
                                                           "longitude",
                                                           decimal(interval(new BigDecimal(-180),
                                                                            new BigDecimal(180)
                                                                           )
                                                                  ),
                                                           "fruits",
                                                           arrayOfStr,
                                                           "numbers",
                                                           arrayOfInt,
                                                           "vegetables",
                                                           arrayOf(JsObjSpec.strict("veggieName",
                                                                                    str,
                                                                                    "veggieLike",
                                                                                    bool
                                                                                   )
                                                                  )
                                                          );

    static {
        PERSON_jSON_SCHEMA =  fileContent("personSchema.json");
        PERSON_JSON =  fileContent("person.json");

    }

    private static String fileContent(String name) {
        final InputStream stream = Conf.class.getClassLoader()
                                             .getResourceAsStream(name);
        try {
            return fromStream(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String fromStream(InputStream in) throws IOException {
        BufferedReader reader  = new BufferedReader(new InputStreamReader(in));
        StringBuilder  out     = new StringBuilder();
        String         newLine = System.getProperty("line.separator");
        String         line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    private static IntPredicate greaterOrEqualThan(int value) {
        return i -> i >= value;
    }

    private static Predicate<BigDecimal> interval(BigDecimal min,
                                                  BigDecimal max
                                                 ) {
        return v -> v.doubleValue() <= max.doubleValue() && v.doubleValue() >= min.doubleValue();
    }
}
