- [Persistent data structures](#pds)
 - [JsPath](#jspath)
 - [JsValue](#jselem)
 - [JsPair](#jspair)
 - [Creating Jsons](#json)
   - [Json objects](#json-obj)
   - [Json arrays](#json-arr)
 - [Creating Json specs](#json-spec)
   - [Json object specs](#json-obj-spec)
   - [Json array specs](#json-arr-spec)
 - [Creating Json generators](#json-gen)
   - [Json object generators](#json-obj-gen)
   - [Json array generators](#json-arr-gen)
 - [Creating Json futures](#json-futures)
   - [Json object futures](#json-obj-future)
   - [Json array futures](#json-arr-future)
 - [Creating Json suppliers](#json-supplier)
   - [Json object suppliers](#json-obj-supplier)
   - [Json array suppliers](#json-arr-supplier)
 - [Creating Json programs on the console](#json-console)
   - [Json object programs](#json-obj-console)
   - [Json array programs](#json-arr-console)
 - [Optics](#optics)
   - [Lenses](#lenses)
   - [Optionals](#optionals)
   - [Prisms](#prisms)
 - [Filter, map and reduce](#filter-map-reduce)
   - [Filter](#filter)
   - [Map](#map)
   - [Reduce](#reduce)
 - [Union and intersection](#union-and-intersection)
   - [Union](#union)
   - [Intersection](#intersection)
 - [Equality](#equality)
 - [Exceptions and errors](#exceptions-errors)
 - [Trampolines](#trampolines)
 - [Performance](#performance)
 - [Tools](#tools)

 ## <a name="pds"></a> Persistent data structures
 How do we make changes to immutable structures or values in a inexpensive way? Using persistent data structures. Copy-on-write is inefficient and, the performance goes down as you produce new values.
 Why don't we have a persistent Json? This is the question I asked myself when I got into functional programming. Since I found out no answer, I decided to implement a persistent Json.

 ## <a name="jspath"></a> JsPath
 A JsPath represents a location of a specific value within a JSON. It provides an implementation of the Json
 Pointer specification defined in [rfc6901](https://tools.ietf.org/html/rfc6901) through the static factory
 method _path(string)_, but there are two slightly differences:

   - According to the RFC, the path /0 could represent both a key named 0 or the first element of an array.
   That's perfectly fine to get data out of a Json; after all, the schema of the Json is supposed to be known
   by the user. However, imagine that the user wants to insert the name "Rafa" at /names/0 in an empty Json object:

 ```
 JsObj obj = JsObj.empty().put(path("/names/0"),"Rafa")
 ```

 What is the expected result? According to the rfc6901, both:

 ```
 {"names":["Rafa"]}
 ```

 and

 ```
 {"names":{"0":"Rafa"}}
 ```

 are valid results. The API has to provide the user with a way of distinguishing arrays from objects.
 The rfc6901 is to read data from a Json, and given the pointed out above, it cannot be used to insert
 data in a Json. The approach of json-values to make that distinction is to single-quote only the keys
 which names are numbers, i.e.:

 ```
 // {"names":{"0":"Rafa"}}
 JsObj obj = JsObj.empty().put(path("/names/'0'"),"Rafa")
 ```

 and

 ```
 // {"names":["Rafa"]}
 JsObj obj = JsObj.empty().put(path("/names/0"),"Rafa")
 ```

   - The index -1 represents the last element of an array.

 There are two ways of creating paths:

 * From a path-like string using the method _JsPath.path(string)_. See [rfc6901](https://tools.ietf.org/html/rfc6901) for further details

 * Using the JsPath API:

    - _fromKey(name)_ to create a JsPath from a key name.

    - _fromIndex(i)_ to create a JsPath from an index

    - _key(name)_ to append a key to a JsPath

    - _index(i)_ to append an index to a JsPath

 ```
 {
 "a": [ {"b": [1,2,3]} ],
 " ": "z",
 "c.d": 4,
 "1": [false, true]
 "'": null,
 "": 1.2
 }
 // represents the root
 path("") or JsPath.empty()

 // 1
 path("/a/0/b/0")
 fromKey("a").index(0).key("b").index(0)

 // 2
 path("/a/0/b/1")
 fromKey("a").index(0).key("b").index(1)

 // 3
 path("/a/0/b/2")
 fromKey("a").index(0).key("b").index(2)
 path("/a/0/b/-1")
 fromKey("a").index(0).key("b").index(-1)

 // z
 path("#/+")
 fromKey(" ")

 // 4
 path("#/c%2Ed")
 fromKey("c.d")

 // false
 path("/'1'/0")
 fromKey("1").index(0)

 // true
 path("/'1'/1")
 fromKey("1").index(1)

 // null
 path("#/%27")
 fromKey("'")

 // 1.2
 path("/")
 fromKey("")
 ```