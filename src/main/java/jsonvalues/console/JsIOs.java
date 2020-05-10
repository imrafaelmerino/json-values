package jsonvalues.console;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.MyDslJson;
import jsonvalues.*;
import jsonvalues.future.JsFutures;
import jsonvalues.spec.JsSpec;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class JsIOs
{

  public static JsIO<JsValue> read(final JsSpec spec)
  {
    Objects.requireNonNull(spec);
    return path ->
    {
      final Supplier<CompletableFuture<JsValue>> retry = JsFutures.retry(() -> completedFuture(readLine())
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

                                                                                            System.out.println(toWhiteSpaces(indentationSize(path)) + "Uppsss: " + message);
                                                                                            JsIOs.printIndentedPath()
                                                                                                 .accept(path);
                                                                                            throw new RuntimeException(it);
                                                                                          }),
                                                                         1
                                                                        );
      return retry::get;
    };
  }


  private static String readLine()
  {
    Scanner in = new Scanner(System.in);

    return in.nextLine();
  }

  private static int indentationSize(final JsPath path){
    return (int) Math.pow(2, path.size());
  }

  private static String toWhiteSpaces(final int numberSpaces)
  {
    return IntStream.range(0, numberSpaces)
                    .mapToObj(i -> " ")
                    .collect(Collectors.joining());
  }



  static Consumer<JsPath> printIndentedPath()
  {
    return path -> System.out.print(toWhiteSpaces(indentationSize(path))+ path + " -> ");
  }

  static Consumer<JsPath> printlnIndentedPath()
  {
    return path -> System.out.println(toWhiteSpaces(indentationSize(path))+ path + " -> ");
  }
}
