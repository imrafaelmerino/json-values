package jsonvalues.specifications.mutable.array

import java.util.stream

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class ParseStringIntoJsArraySpec extends BasePropSpec
{

  property("parse string into mutable array mapping keys to uppercase")
  {
    check(forAll(jsGen.jsArrGen)
          { arr =>
            val parsed = Json._parse_(arr.toString,
                                      ParseOptions.builder().withKeyMap(it => it.toUpperCase)
                                      ).arrOrElseThrow()
            val s: stream.Stream[Position] = parsed.stream_().flatMap(it => it.path.stream())


            s.filter(s => s.isKey()).allMatch(k => specifications.Functions.isUppercase(k.asKey().name))


          }
          )
  }
}
