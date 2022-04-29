package jsonvalues;


import jsonvalues.spec.*;
import org.junit.jupiter.api.Assertions;

import java.util.Set;


class Fun {

    private Fun() {
    }

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
