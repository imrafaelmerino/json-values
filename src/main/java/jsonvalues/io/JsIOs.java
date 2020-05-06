package jsonvalues.io;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyDslJson;
import jsonvalues.*;
import jsonvalues.spec.JsSpec;

import java.io.IOException;
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

                 }
                )
      .exceptionally(it ->
                     {
                       final String message = it.getCause() != null ? it.getCause()
                                                                        .getMessage() : it.getMessage();
                       System.out.println(message);
                       return JsNothing.NOTHING;
                     });
  }


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
