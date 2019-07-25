package jsonvalues.specifications.mutable.jsobject

import java.util.stream

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class ParseStringIntoJsObjSpec extends BasePropSpec
{

  property("parse string into mutable object mapping keys to uppercase")
  {
    check(forAll(jsGen.jsObjGen)
          { obj =>
             val parsed = Json._parse_(obj.toString,
                                      ParseOptions.builder().withKeyMap(it => it.toUpperCase)
                                      ).objOrElseThrow()
            val s: stream.Stream[Position] = parsed.stream_().flatMap(it => it.path.stream())


            s.filter(s => s.isKey()).allMatch(k => specifications.Functions.isUppercase(k.asKey().name))


          }
          )
  }
}
