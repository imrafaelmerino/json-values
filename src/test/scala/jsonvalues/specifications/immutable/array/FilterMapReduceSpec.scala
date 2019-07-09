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
               val arr = js.filterElems_(p=>p.elem.isStr)
               arr.stream_().filter(p=>p.elem.isNotJson).allMatch(p=>p.elem.isStr)
           }
           )
  }

  property("filterElems (removes every element but strings)")
  {
    check(
           forAll(jsGen.jsArrGen)
           {
             js =>
               val arr = js.filterElems(p=> p.elem.isStr)
               arr.stream_().filter(p=> p.elem.isNotJson && p.path.tail().isEmpty).allMatch(p=>p.elem.isStr)
           }
           )
  }


  property("filterElems_ (removes every element but integral numbers)")
  {
    check(
           forAll(jsGen.jsArrGen)
           {
             js =>
               val arr = js.filterElems_(p=>p.elem.isIntegral)
               arr.stream_().filter(p=>p.elem.isNotJson).allMatch(p=>p.elem.isIntegral)
           }
           )
  }


  property("filterElems (removes every element but integral numbers)")
  {
    check(
           forAll(jsGen.jsArrGen)
           {
             js =>
               val arr = js.filterElems_(p=>p.elem.isIntegral)
               arr.stream_().filter(p=>p.elem.isNotJson && p.path.tail().isEmpty).allMatch(p=>p.elem.isIntegral)
           }
           )
  }

  property("filterElems_ (removes null)")
  {
    check(
           forAll(jsGen.jsArrGen)
           {
             js =>
               val arr = js.filterElems_(p=>p.elem.isNotNull)
               arr.stream_().filter(p=>p.elem.isNotJson).allMatch(p=>p.elem.isNotNull)
           }
           )
  }

  property("filterElems (removes null)")
  {
    check(
           forAll(jsGen.jsArrGen)
           {
             js =>

               val arr = js.filterElems(p=>p.elem.isNotNull)
               arr.stream_().filter(p=>p.elem.isNotJson && p.path.tail().isEmpty).allMatch(p=>p.elem.isNotNull)
           }
           )
  }

  //  property("filterObjs_  (removes empty Json)")
  //  {
  //    check(
  //           forAll(jsGen.jsArrGen)
  //           {
  //             js =>
  //               val predicate: Predicate[JsPair] = p => !p.elem.asJson().isEmpty
  //               val arr = js.filterObjs_(predicate)
  //               arr.stream_().filter(p => p.elem.isJson).allMatch(p => !p.elem.asJson().isEmpty)
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
  //               val predicate: Predicate[JsPair] = p => !p.elem.asJson().isEmpty
  //               val arr = js.filterObjs(predicate)
  //               arr.stream_().filter(p => p.elem.isJson && p.path.tail().isEmpty).allMatch(p => !p.elem.asJson().isEmpty)
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
               val arr = js.filterKeys_(predicate)
               arr.stream_().allMatch(predicate)
           }
           )
  }
}
