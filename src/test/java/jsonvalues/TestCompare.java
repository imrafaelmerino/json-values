package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCompare {


    @Test
    public void testPosition(){

        Assertions.assertEquals(1,Index.of(1).compareTo(Index.of(0)));
        Assertions.assertEquals(-1,Index.of(1).compareTo(Index.of(2)));
        Assertions.assertEquals(0,Index.of(1).compareTo(Index.of(1)));

        Assertions.assertTrue(Index.of(1).compareTo(Key.of("a"))<0);
        Assertions.assertEquals(0, Index.of(1).compareTo(Key.of("1")));
        Assertions.assertTrue(Index.of(1).compareTo(Key.of("0"))>0);


    }
}
