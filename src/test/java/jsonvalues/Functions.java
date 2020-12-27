package jsonvalues;


import jsonvalues.spec.ERROR_CODE;
import jsonvalues.spec.JsErrorPair;
import org.junit.jupiter.api.Assertions;

import java.util.Set;


public class Functions {

    private Functions() { }

    public static void assertErrorIs(final Set<JsErrorPair> error,
                                     final ERROR_CODE code,
                                     final JsValue value,
                                     final JsPath path
                                    ) {
        final JsErrorPair pair = error.stream()
                                      .findFirst()
                                      .get();

        Assertions.assertEquals(pair.error.code,
                                code
                               );

        Assertions.assertEquals(pair.error.value,
                                value
                               );

        Assertions.assertEquals(pair.path,
                                path
                               );
    }

}
