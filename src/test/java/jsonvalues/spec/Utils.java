package jsonvalues.spec;

import java.util.Objects;

class Utils {

    static boolean debugNonNull(Object object) {
        if (object != null) System.out.println(object);
        return object != null;
    }
}
