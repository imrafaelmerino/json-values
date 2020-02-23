package jsonvalues.specifications.immutable.array

import java.util.function.Predicate

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll


class FilterMapReduceSpec extends BasePropSpec
{


  property("filterElems_ (removes every element but strings)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isStr)
          arr.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isStr)
      }
      )
  }

  property("filterElems (removes every element but strings)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterValues(p => p.value.isStr)
          arr.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isStr)
      }
      )
  }


  property("filterElems_ (removes every element but integral numbers)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isIntegral)
          arr.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isIntegral)
      }
      )
  }


  property("filterElems (removes every element but integral numbers)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isIntegral)
          arr.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isIntegral)
      }
      )
  }

  property("filterElems_ (removes null)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val arr = js.filterAllValues(p => p.value.isNotNull)
          arr.streamAll().filter(p => p.value.isNotJson).allMatch(p => p.value.isNotNull)
      }
      )
  }

  property("filterElems (removes null)")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>

          val arr = js.filterValues(p => p.value.isNotNull)
          arr.streamAll().filter(p => p.value.isNotJson && p.path.tail().isEmpty).allMatch(p => p.value.isNotNull)
      }
      )
  }

  //  property("filterObjs_  (removes empty Json)")
  //  {
  //    check(
  //           forAll(jsGen.jsArrGen)
  //           {
  //             js =>
  //               val predicate: Predicate[JsPair] = p => !p.value.asJson().isEmpty
  //               val arr = js.filterObjs_(predicate)
  //               arr.stream_().filter(p => p.value.isJson).allMatch(p => !p.value.asJson().isEmpty)
  //           }
  //         )
  //  }
  //
  //  property("filterObjs (removes empty Json)")
  //  {
  //    check(
  //           forAll(jsGen.jsArrGen)
  //           {
  //             js =>
  //               val predicate: Predicate[JsPair] = p => !p.value.asJson().isEmpty
  //               val arr = js.filterObjs(predicate)
  //               arr.stream_().filter(p => p.value.isJson && p.path.tail().isEmpty).allMatch(p => !p.value.asJson().isEmpty)
  //           }
  //         )
  //  }

  property("filterKeys returns the same array")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val predicate: Predicate[JsPair] = _ => false
          val arr = js.filterKeys(predicate)
          arr == js
      }
      )
  }


  property("filterKeys_ (removes keys named 'a')")
  {
    check(
      forAll(jsGen.jsArrGen)
      {
        js =>
          val predicate: Predicate[JsPair] = p => p.path.stream().filter(it => it.isKey).allMatch(it => it.asKey().name != "a")
          val arr = js.filterAllKeys(predicate)
          arr.streamAll().allMatch(predicate)
      }
      )
  }
}
