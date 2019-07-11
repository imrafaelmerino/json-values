package jsonvalues.specifications.immutable.array

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class AppendToArraySpec extends BasePropSpec
{

  property("appends elements to the back after creating a new array in an empty object")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsElemGen
                 )
          { (path,
             elem
            ) =>

            JsArray.empty().append(path,
                                   elem
                                   ).getArray(path).get().size() == 1
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
            !JsArray.empty().appendIfPresent(path,
                                             elem
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
            JsArray.empty().prepend(path,
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
            !JsArray.empty().appendIfPresent(path,
                                             elem
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
            JsArray.empty().prepend(path,
                                    elem
                                    ).prependIfPresent(path,
                                                       elem
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
            JsArray.empty().append(path,
                                   elem
                                   ).appendIfPresent(path,
                                                     elem
                                                     ).getArray(path).get().size() == 2


          }
          )
  }
}
