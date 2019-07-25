package jsonvalues.specifications.mutable.array

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class AppendToArraySpec extends BasePropSpec
{

  property("appends one element")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>

            JsArray._empty_().append(path,
                                     elem
                                     ).getArray(path).get().size() == 1
          }
          )
  }

  property("appends three element")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen
                 )
          { (path,
             a,
             b,
             c
            ) =>

            val array = JsArray._empty_().append(path,
                                                 a,
                                                 b,
                                                 c
                                                 )
            array.getArray(path).get().size() == 3 &&
            array.get(path.index(0)).equals(a) &&
            array.get(path.index(1)).equals(b) &&
            array.get(path.index(2)).equals(c)
          }
          )
  }

  property("prepends three element")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen,
                 jsGen.jsElemGen
                 )
          { (path,
             a,
             b,
             c
            ) =>

            val array = JsArray._empty_().prepend(path,
                                                  a,
                                                  b,
                                                  c
                                                  )
            array.getArray(path).get().size() == 3 &&
            array.get(path.index(0)).equals(a) &&
            array.get(path.index(1)).equals(b) &&
            array.get(path.index(2)).equals(c)
          }
          )
  }

  property("appends a json array")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsArrGen
                 )
          { (path,
             a
            ) =>

            val array = JsArray._empty_().appendAll(path,
                                                    a
                                                    )
            array.getArray(path).get().size() == a.size() &&
            a.get("0").equals(array.get(path.index(0))) &&
            a.get("-1").equals(array.get(path.index(-1)))
          }
          )
  }

  property("appends a json array if exists the target")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsArrGen
                 )
          { (path,
             a
            ) =>

            val empty = JsArray._empty_().appendAllIfPresent(path,
                                                             () => a
                                                             )

            val notEmpty = JsArray._empty_().put(path,
                                                 JsArray._empty_()
                                                 ).appendAllIfPresent(path,
                                                                      () => a
                                                                      )
            !empty.getArray(path).isPresent && notEmpty.getArray(path).get().equals(a)
          }
          )
  }

  property("prepends a json array if exists the target")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsArrGen
                 )
          { (path,
             a
            ) =>

            val empty = JsArray._empty_().prependAllIfPresent(path,
                                                              () => a
                                                              )

            val notEmpty = JsArray._empty_().put(path,
                                                 JsArray._of_(1)
                                                 ).prependAllIfPresent(path,
                                                                       () => a
                                                                       )
            !empty.getArray(path).isPresent && notEmpty.getArray(path).get().equals(JsArray._of_(1).prependAll(a))
          }
          )
  }

  property("appendIfPresent elements doesn't append anything when the array doesn't exist")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            !JsArray._empty_().appendIfPresent(path,
                                               ScalaToJava.supplier(() => elem)
                                               ).getArray(path).isPresent
          }
          )
  }
  //
  property("appends elements to the front after creating a new array in an empty object")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            JsArray._empty_().prepend(path,
                                      elem,
                                      elem
                                      ).getArray(path).get().size() == 2
          }
          )
  }

  property("prependIfPresent elements doesn't append anything when the array doesn't exist")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            !JsArray._empty_().appendIfPresent(path,
                                               ScalaToJava.supplier(() => elem)
                                               ).getArray(path).isPresent
          }
          )
  }

  property("appendsFrontIfPresent appends elements to the front when the array containsPath")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            JsArray._empty_().prepend(path,
                                      elem
                                      ).prependIfPresent(path,
                                                         ScalaToJava.supplier(() => elem)
                                                         ).getArray(path).get().size() == 2


          }
          )
  }

  property("appendsBackIfPresent appends elements to the back when the array containsPath")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>
            JsArray._empty_().append(path,
                                     elem
                                     ).appendIfPresent(path,
                                                       ScalaToJava.supplier(() => elem)
                                                       ).getArray(path).get().size() == 2


          }
          )
  }
}
