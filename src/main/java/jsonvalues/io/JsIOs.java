package jsonvalues.io;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyDslJson;
import jsonvalues.*;
import jsonvalues.spec.JsSpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class JsIOs
{

  public static JsIO<JsValue> read(JsSpec spec)
  {
    return path -> () -> completedFuture(readLine())
      .thenApply(s ->
                 {
                   final JsonReader<?> reader = MyDslJson.INSTANCE.getReader(s.getBytes());
                   try
                   {
                     reader.getNextToken();
                     return spec.parser()
                                .parse(reader);
                   }
                   catch (IOException e)
                   {
                     throw new RuntimeException(e);
                   }

                 });
  }

  public static JsIO<JsValue> readStr = path -> () -> completedFuture(readLine())
    .thenApply(JsStr::of);

  public static JsIO<JsValue> readInt = path -> () -> completedFuture(readLine())
    .thenApply(s -> JsInt.of(Integer.parseInt(s)));

  public static JsIO<JsValue> readLong = path -> () -> completedFuture(readLine())
    .thenApply(s -> JsLong.of(Long.parseLong(s)));

  public static JsIO<JsValue> readBool = path -> () -> completedFuture(readLine())
    .thenApply(s -> JsBool.of(Boolean.parseBoolean(s)));

  public static JsIO<JsValue> readDouble = path -> () -> completedFuture(readLine())
    .thenApply(s -> JsDouble.of(Double.parseDouble(s)));


  public static JsIO<JsValue> readDecimal = path -> () -> completedFuture(readLine())
    .thenApply(s -> JsBigDec.of(new BigDecimal(s)));


  private static String readLine()
  {
    Scanner in = new Scanner(System.in);

    return in.nextLine();
  }

  static String indent(JsPath path)
  {
    return IntStream.range(0,
                           (int) Math.pow(2,
                                          path.size()
                                         )
                          )
                    .mapToObj(i -> " ")
                    .collect(Collectors.joining());
  }
}
