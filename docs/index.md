 - [JsPath](#jspath)
 - [JsElem](#jselem)
 - [JsPair](#jspair)
 - [Creating Jsons](#json-creation)
   - [Immutable Json objects](#json-immutable-obj-creation)
   - [Mutable Json objects](#json-mutable-obj-creation)
   - [Immutable Json arrays](#json-immutable-arr-creation)
   - [Mutable Json arrays](#json-mutable-arr-creation)
 - [Putting data in and getting data out](#data-in-out)
   - [Obtaining primitive types](#obtaining-primitive-types)
   - [Obtaining Json elements](#obtaining-jselements)
   - [Putting data at any location](#putting-data-by-path)
   - [Being idiomatic: _tell, don't ask_ principle](#tell-dont-ask)
   - [Being lazy](#lazy)
   - [Manipulating arrays](#manipulating-arrays)
 - [Stream and collectors](#stream-collectors)
    - [Streams](#streams)
    - [Object collectors](#object-collectors)
    - [Array collectors](#array-collectors)
 - [Filter, map and reduce](#filter-map-reduce)
   - [Filter](#filter)
   - [Map](#map)
   - [Reduce](#reduce)
 - [RFC 6902: Json Patch](#json-patch)  
 - [Union and intersection](#union-and-intersection)
   - [Union](#union)
   - [Intersection](#intersection)
 - [Equality](#equality)
 - [Exceptions and errors](#exceptions-errors)
 - [Trampolines](#trampolines)
 - [Performance](#performance)
 - [Tools](#tools)
 
## <a name="jspath"></a> JsPath
A JsPath represents a location of a specific value within a JSON. It provides an implementation of the Json 
Pointer specification defined in [rfc6901](https://tools.ietf.org/html/rfc6901) through the static factory
method _path(string)_, but there are two slightly differences:
 
  - According to the RFC, the path /0 could represent both a key named 0 or the first element of an array. 
  That's perfectly fine to get data out of a Json; after all, the schema of the Json is supposed to be known 
  by the user. However, imagine that the user wants to insert the name "Rafa" at /names/0 in an empty Json object:

```
JsObj obj = Jsons.immutable.obj.empty().put(path("/names/0"),"Rafa")
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
The rfc6901 is to read data from a Json, and given the pointed out above, it can not be used to insert 
data in a Json. The approach of json-values to make that distinction is to single-quote only the keys 
which names are numbers, i.e.:

```
// {"names":{"0":"Rafa"}}
JsObj obj = Jsons.immutable.obj.empty().put(path("/names/'0'"),"Rafa")
```

and

```
// {"names":["Rafa"]}
JsObj obj = Jsons.immutable.obj.empty().put(path("/names/0"),"Rafa")
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

## <a name="jselem"></a> JsElem
Every element in a Json is a _JsElem_. There is one for each json value described in [json.org](https://www.json.org):
* _JsStr_ represents immutable strings.

* The singletons _JsBool.TRUE_ and _JsBool.FALSE_ represent true and false.

* The singleton _JsNull.NULL_ represents null.

* _JsObj_ is a _Json_ that represents an object, which is an unordered set of name/value pairs.

* _JsArray_ is a _Json_ that represents an array, which is an ordered collection of values.

* _JsNumber_ represents immutable numbers. There are five different specializations: 
    
    * _JsInt_
    
    * _JsLong_
    
    * _JsDouble_
    
    * _JsBigInt_
    
    * _JsBigDec_

* The singleton _JsNothing.NOTHING_ represents nothing. It's not part of any specification. It's a convenient type
that makes certain functions that return a JsElem **total** on their arguments. For example, the function
 _JsElem get(JsPath)_ is total because it returns a JsElem for every JsPath. If there is no element located at the specified path, it returns _NOTHING_. In other functions like _Json putIfPresent(Function<JsElem, JsElem>)_, this type comes in handy as well because it's possible, just returning _NOTHING_, not to insert anything even if an element is present. 
 
## <a name="jspair"></a> JsPair
There are different overloaded static factory methods to create pairs:
```
JsPair of(JsPath path, int elem)
JsPair of(JsPath path, double elem)
JsPair of(JsPath path, long elem)
JsPair of(JsPath path, boolean elem)
JsPair of(JsPath path, String elem)
JsPair of(JsPath path, JsElem elem)
```

Pairs are immutable. You can get the path and element of a pair by direct field access:

```
JsPair pair = JsPair.of(path("/a/b/0"), "a");
JsPath path = pair.path;
JsElem elem = pair.elem;
```

Pairs can be mapped using:

```
JsPair mapPath(UnaryOperator<JsPath> fn)
JsPair mapElem(UnaryOperator<JsElem> fn)
```

## <a name="json-creation"></a> Creating Jsons

**json-values** uses factories to create objects. There are two factories:

_Jsons.immutable_, that uses persistent data structures from Scala: [Scala HashMap](https://scala-lang.org/files/archive/api/2.12.0/scala/collection/seq/HashMap.html) 
and [Scala Vector](https://www.scala-lang.org/api/2.12.0/scala/collection/immutable/Vector.html)

_Jsons.mutable_, that uses the conventional Java collections HashMap and ArrayList.

They are singletons; therefore, they can not be modified. 
```
                                      
### <a name="json-immutable-obj-creation"></a> Creation of immutable Json objects

```
// from keys and associated elements
JsObj.of("a", JsInt.of(1), 
                          "b", JsBool.TRUE, 
                          "c", JsNull.NULL, 
                          "d", JsStr.of("hi")
                         );

// from varargs of json pairs
JsObj w = JsObj.of( JsPair.of(path("/a/b/0"), 1),
                                     JsPair.of(path("/a/b/1"), 2)
                                   );    
                           
//parsing a string, which returns a TryObj computation that may fail                       
JsObj z = JsObj.parse("{\"a\": {\"b\": [1,2]}}").orElseThrow(); 

Assertions.assertEquals(w, z);   
 ```

### <a name="json-mutable-obj-creation"></a> Creation of mutable Json objects

```
// from keys and associated elements
JsObj x = Jsons.mutable.object.of("a", JsInt.of(1), 
                                  "b", JsBool.TRUE, 
                                  "c", JsNull.NULL, 
                                  "d", JsStr.of("hi")
                                 );  

// from vargs of json pairs
JsObj w = Jsons.mutable.object.of( JsPair.of(path("/a/b/0"), 1),
                                     JsPair.of(path("/a/b/1"), 2)
                                   );    
                            
// parsing a string, which returns a TryObj computation that may fail                  
JsObj z = Jsons.mutable.object.parse("{\"a\": {\"b\": [1,2]}}").orElseThrow(); 

Assertions.assertEquals(w, z);   
```

### <a name="json-immutable-arr-creation"></a> Creation of immutable Json arrays

```
// from varargs of ints
JsArray a = JsArray.of(1,2,3);  

// from varargs of strings
JsArray b = JsArray.of("a","b","c");

// from varargs of JsElem
JsArray c = JsArray.of(JsBool.TRUE, 
                                     JsStr.of("a"), 
                                     JsNull.NULL, 
                                     JsDouble.of(1.5d)
                                    );
                       
//from varargs of json pairs
JsArray d = JsArray.of(JsPair.of(path("/0/a/b/0"), 1),
                                     JsPair.of(path("/0/a/b/1"), 2)
                                    );

//parsing a string, which returns a TryArr computation that may fail                      
JsArray e =  JsArray.parse("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       

Assertions.assertEquals(d, e);  
```        
       
### <a name="json-mutable-arr-creation"></a> Creation of mutable Json arrays

```
// from varargs of ints
JsArray a = Jsons.mutable.array.of(1,2,3);  

// from varargs of strings
JsArray b = Jsons.mutable.array.of("a","b","c"); 

// from varargs of JsElem
JsArray c = Jsons.mutable.array.of(JsBool.TRUE, 
                                   JsStr.of("a"), 
                                   JsNull.NULL, 
                                   JsDouble.of(1.5d)
                                  );

// from varargs of json pairs
JsArray d = JsArray.of(JsPair.of(path("/0/a/b/0"), 1),
                                     JsPair.of(path("/0/a/b/1"), 2)
                                    );

// parsing a string, which returns a TryArr computation that may fail                        
JsArray e =  JsArray.parse("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       

Assertions.assertEquals(d, e);  
```

## <a name="data-in-out"></a> Putting data in and getting data out
To be able to insert data in and pull data out in a simple way is a must for any Json API. That's why **json-values**
has several overloaded methods that allow the client to work directly with primitive types, avoiding any conversion. 

```
{
"a": { "b": [ { "c": 1,
                "d": [1,2],
                "e": ["a","b"]
              }  
            ] 
     },
 "e": [[1,2], {}, [], null, 1.2]     
}
```

### <a name="obtaining-primitive-types"></a> Obtaining primitive types
All the _getXXX_ by path methods, return an Optional or one of its specializations for the particular primitive type. 

```
Assertions.assertEquals(OptionalInt.of(1), 
                        json.getInt(path("/a/b/0/c"))
                        );
Assertions.assertEquals(OptionalLong.of(2), 
                        json.getLong(path("/a/b/0/d/-1"))
                        );
Assertions.assertEquals(Optional.of("a"), 
                        json.getStr(path("/a/b/0/e/0"))
                        );
Assertions.assertEquals(OptionalDouble.of(1.2), 
                        json.getDouble(path("/e/-1"))
                        );
Assertions.assertEquals(OptionalInt.empty(), 
                        json.getInt(path("/e/-1"))
                        );
Assertions.assertEquals(OptionalInt.empty(), 
                        json.getInt(fromKey("h"))
                        );        
```

### <a name="obtaining-jselements"></a> Obtaining Json elements
To obtains JsObj or JsArray wrapped into an optional:

```
Assertions.assertEquals(Optional.of(JsArray.of(1,2)), 
                        json.getArr(path("/e/0"))
                        );
Assertions.assertEquals(Optional.of(JsObj.of("c",JsInt.of(1),
                                                              "d",JsArray.of(1,2),
                                                              "e",JsArray.of("a","b"))
                                                              )
                                    ), 
                        json.getObj(path("/a/b/0"))
                        );
```

Working with JsElem may be necessary sometimes, for example, if it's unknown the type of the element located at a path.
The _get_ by path method returns a _JsElem_ and has the attractive property that is total, as it was mentioned above. 
Just as a reminder, it means that it returns a JsElem for every possible path. Functional programmers strive for total functions. 
It's possible thanks to the _JsNothing_ type.

```
Assertions.assertEquals(JsNull.NULL, 
                        json.get(path("/e/3"))
                        );

Assertions.assertEquals(JsNothing.NOTHING, 
                        json.get(fromKey("f"))
                        ); 
```

### <a name="putting-data-by-path"></a> Putting data at any location
   - The _put_ method always inserts the specified element at the specified path:
   
```
Jsobj a = Jsons.immutable.obj.empty().put(path("/a/b/c"), 1);
Assertions.assertEquals(JsInt.of(1), a.get(path("/a/b/c")));
Assertions.assertEquals(1, a.getInt(path("/a/b/c")).getAsInt());

Jsobj b = a.put(path("a.b"), true);
Assertions.assertEquals(JsBool.TRUE, a.get(path("/a/b")));
Assertions.assertEquals(true, a.getBool(path("/a/b")).get());

//a and b are immutable
Assertions.assertNotEquals(a,b);

JsArray c = JsArray.of(JsObj.of("a",JsArray.of(1,2)),
                                     JsObj.of("b",JsArray.of("a","b"))
                                    );
Assertions.assertEquals(JsInt.of(1), c.get(path("/0/a/0")) );
Assertions.assertEquals(1l, c.getLong(path("/0/a/0")).getAsLong() );
                       
JsArray d = c.put(path("0.a.0"),true); 
Assertions.assertEquals(JsBool.TRUE, d.get(path("/0/a/0")) );
Assertions.assertEquals(true, d.getBool(path("/0/a/0")).get() );
```

When inserting data in arrays at specific positions, filling with null may be necessary:

```
JsArray e = c.put(path("/0/b/3"),"c");
Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                                 JsStr.of("b"),
                                                 JsNull.NULL,   
                                                 JsStr.of("c")
                                                 ), 
                        d.get(path("/0/b")) 
                        );
```

The point here is being honest. The string _c_ has been inserted at the forth position of the array, and for that to happen, filling with null the third position is necessary.  

  - The _add_ method, unlike the _put_, never creates a new container, but it adds an element to an existing one.
  If the parent container doesn't exist, an UserError is thrown. If the parent is an array, the elements at or above 
  the specified index are shifted one position to the right.
  
```
JsObj obj = JsObj.of("a",JsArray.of(1,2,3),
                                      "b",JsObj.of("c",JsStr.of("hi"))
                                     );

Assertions.assertEquals(JsArray.of(1,5,2,3),
                        obj.add(path("/a/1"), 5).get(path("a"))
                       );

Assertions.assertEquals(JsStr.of("bye"),
                        obj.add(path("/a/b/d"), "bye").get(path("/a/b/d"))
                       );
```
  
### <a name="tell-dont-ask"></a> Being idiomatic: tell don't ask principle
An attractive principle in OOP is known as ["tell, don't ask."](https://pragprog.com/articles/tell-dont-ask) It 
leads to more declarative APIs. The _putIfAbsent_, _putIfPresent_, and _merge_ methods follow that principle. The 
point is, instead of checking if an element is present or not and, in consequence, to call or not the put method, 
you can do the same thing in just one call:

```
JsObj a = Jsons.immutable.obj.empty().putIfPresent(fromKey("a"),1);
Assertions.assertEquals(Jsons.immutable.obj.empty(), a);


JsObj b = Jsons.immutable.obj.empty().putIfAbsent(fromKey("a"),1);
Assertions.assertEquals(JsInt.of(1), 
                        b.get(fromKey("a"))
                        );

JsObj c = b.putIfAbsent(fromKey("a"),2);
Assertions.assertEquals(b,c)


JsInt defaultElem = JsInt.of(1);
// in the function, d stands for the default element and e stands for the existing one
BiFunction<? super JsElem, ? super JsElem, ? extends JsElem> fn = (d, e)-> e.isInt() ?  e.asJsInt().plus(d.asJsInt()) : d;

// no element exists at "a" -> defaultElement is inserted
JsObj f = Jsons.immutable.obj.empty().merge(fromKey("a"),
                              defaultElem,                                   
                              fn
                              ); 
Assertions.assertEquals(JsInt.of(1), f.get(fromKey("a")));
// an element exists at "a" -> the function is invoked 
JsObj g = f.merge(fromKey("a"),
                  defaultElem,                                   
                  fn
                  ); 
Assertions.assertEquals(JsInt.of(2), 
                        f.get(fromKey("a"))
                        );                     
```

### <a name="lazy"></a> Being lazy
It's possible to be lazy and not produce any element if it's not going to be inserted, just passing a supplier:

````
JsObj d = b.putIfAbsent(fromKey("a"), ()-> computed value);

JsObj e = b.putIfPresent(fromKey("a"), ()-> computed value);
````

### <a name="manipulating-arrays"></a> Manipulating arrays
To insert elements at the front of an array, it exists the methods _prepend_, _prependAll_, _prependIfPresent_, and _prependAllIfPresent_.
To insert elements at the back of an array, it exists the methods _append_, _appendAll_, _appendIfPresent_, and _appendAllIfPresent_.
The same considerations above apply for all of them.  

## <a name="stream-collectors"></a> Stream and collectors
After Java 8 was released, I can't imagine a data structure in Java without providing the stream and collector operations. They open the door
to manipulate data in a very functional way.

A Set of _JsPairs_ can model a Json, which makes obvious how to implement streams on _Jsons_. For example, the following Set:

````
{(a, 1), (b, 2), (c.d, "a"), (c.e.0, 1), (c.e.1, 2), (_, JsNothing)}  where _ means any other path
````

represents the json

```text
{
    "a":1,
    "b":2,
    "c":{
        "d": "a"
        "e": [1,2]
        }
}
```

On the other hand, to implement a collector in Java, a mutable data structure to accumulate the pairs in, is required. It's the real reason that
there is a mutable implementation of a Json in **json-values**. At first, I had no intention of providing one, but sometimes you have to give in and be coherent with the
language you are programming in. 

### <a name="streams"></a> Streams
Stream methods return sequences of JsPairs on-demand:

```
JsObj x = JsObj.of("a", JsArray.of(1,2,3),
                                    "b", JsObj.of("c",JsInt.of(4),
                                                                   "d",JsStr.of("hi")
                                                                   )
                                   );
x.stream().forEach(System.out::println);
```

prints out the following sequence of two pairs:

```text
("a", [1,2,3])
("b", {"c":4, "d": "hi"})
```

By convention, every method that ends with an underscore is applied recursively to every element that is a Json.
Taking that into account:

```
x.stream_().forEach(System.out::println);
```

prints out the following sequence of five pairs:

```text
("a.0", 1) 
("a.1", 2)
("a.2", 3) 
("b.c", 4)
("b.d", "hi")
```

For arrays, it's just the same:

```
JsArray y = JsArray.of(JsArray.of(1,2), 
                                     JsStr.of("red"), 
                                     JsObj.of("c","blue", 
                                                               "d", "pink"
                                                              )
                                    );
y.stream().forEach(System.out::println);
```

prints out the following sequence of three pairs:

```text
("0", [1,2])
("1", "red")
("2", {"c":"blue", "d":"pink"})
```

and

```
y.stream_().forEach(System.out::println)
```

prints out the following sequence of five pairs:

```text
("0.0", 1)
("0.1", 2)
("1", "red")
("2.c", "blue")
("2.d", "pink")
```

### <a name="object-collectors"></a> Object collectors
You can get the stream back into a mutable json object by the collector _Jsons.mutable.object.collector()_:

```
Assert.assertTrue(x.same(x.stream_()
                          .collector(Jsons.mutable.object.collector())
                        )
                 );
```

Take into account that a collector requires a mutable object in order to accumulate all the elements of the stream.

### <a name="array-collectors"></a> Array collectors
You can get the stream back into a mutable json array by the collector _Jsons.mutable.array.collector()_:

```
Assert.assertTrue(y.same(y.stream_()
                          .collector(Jsons.mutable.array.collector())
                        )
                 );

```

## <a name="filter-map-reduce"></a> Filter, map and reduce
What would an API be nowadays without filter, map, and reduce?. They are the crown jewel in functional programming and have been implemented
carefully in different ways taking into account the structure of a Json.

Functions which name ends with an underscore, are applied recursively to every element and not only to the first level of the json. It's
a naming convention in the API. As was mentioned before, names with symbols (except _ and $) are not valid in Java.

### <a name="filter"></a> Filter
_filterKeys_ methods remove the keys from a JsObj which pairs satisfy a predicate: 

```
Json filterKeys(Predicate<? super JsPair> predicate);
Json filterKeys_(Predicate<? super JsPair> predicate);
```

_filterElems_ methods remove the elements from a Json **which are not containers** and which pairs satisfy a predicate:

```
Json filterElems(Predicate<? super JsPair> predicate);
Json filterElems_(Predicate<? super JsPair> predicate);
```

_filterObjs_ methods remove the json objects from a Json which pairs satisfy a predicate:

```
Json filterObjs(BiPredicate<? super JsPath, ? super JsObj> predicate); 
Json filterObjs_(BiPredicate<? super JsPath, ? super JsObj> predicate); 
```

### <a name="map"></a> 
_mapKeys_ methods map the keys from a JsObj which pairs satisfy a predicate. The map function takes as a parameter a pair and returns 
the new key.

```          
Json mapKeys(Function<? super JsPair, String> fn,
             Predicate<? super JsPair> predicate
            );
Json mapKeys_(Function<? super JsPair, String> fn,
              Predicate<? super JsPair> predicate
             );  
```

_mapElems_ methods map the elements from a Json **which are not containers** and which pairs satisfy a predicate. The map function takes
as a parameter a pair and returns the new json element.

```
Json mapElems(Function<? super JsPair, ? extends JsElem> fn,
              Predicate<? super JsPair> predicate
             );
Json mapElems_(Function<? super JsPair, ? extends JsElem> fn,
               Predicate<? super JsPair> predicate
              );   
```

_mapObjs_ methods map the json objects from a Json which pairs satisfy a predicate. The map function takes
as a parameter a json object, its path location and returns the new json object:

```          
Json mapObjs(BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
             BiPredicate<? super JsPath, ? super JsObj> predicate
            );
Json mapObjs_(BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
              BiPredicate<? super JsPath, ? super JsObj> predicate
             );            
``` 

The map functions have been designed in such a way that they don't change the structure of the json, which reminds of _functors_, 
a concept that it may be familiar if you know _Haskell_.

### <a name="reduce"></a> Reduce
Reduce methods are a classic map-reduce over the elements **which are not containers** and which pairs satisfy a predicate.
 The map function takes as a parameter a pair and returns an element that is reduced by an operator.
 
```
<R> Optional<R> reduce(BinaryOperator<R> op,
                       Function<? super JsPair, R> map,
                       Predicate<? super JsPair> predicate
                       );       
                                  
<R> Optional<R> reduce_(BinaryOperator<R> op,
                        Function<? super JsPair, R> map,
                        Predicate<? super JsPair> predicate
                       );        
```

## <a name="json-patch"></a> [RFC 6902](https://tools.ietf.org/html/rfc6902): Json Patch specification

```
TryPatch<JsObj> try = json.patch(Patch.ops()
                                      .add("/a/b",JsStr.of("a"))
                                      .move("/a/c","/b/c")
                                      .copy("/a/E","/b/h/0")
                                      .remove("/f/e")
                                      .test("/d/0",JsBool.TRUE)
                                      .toArray()
                                   );

if(try.isSuccess()) return try.orElseThrow();
```

## <a name="union-and-intersection"></a> Union and intersection
Considering jsons Set of pairs, it seems reasonable to implement Set-Theory operations like union and intersection.
For certain operations, arrays can be considered Sets, MultiSets or Lists. In Sets, the order of data items does not matter (or is undefined) but duplicate data items are not permitted. In Lists, the order of data matters and duplicate data items are permitted. 
In MultiSets, the order of data items does not matter, but in this case, duplicate data items are permitted. 

### <a name="union"></a> Union
Given two json objects _a_ and _b_:
 
*  _a.union(b)_ returns _a_ plus those pairs from _b_  which keys don't exist in _a_.
Taking that into account, it's not a commutative operation unless the elements associated with the keys that exist in both
objects are equals.
* a.union_(b, ARRAY_AS) behaves like the union above but, for those keys that exit in _a_ and _b_ which associated elements
are **containers of the same type**, the result is their union. In this case, we can specify if arrays are considered Sets, Lists, or MultiSets.

Examples:

```
a = { "a": 1, "c": json1}  
b = { "b": 2, "c": json2}
// json1 and json2 are either objecs or arrays
a.union_(b) = { "a": 1, "b":2, "c": json1.union_(json2) }
b.union_(a) = { "a": 1, "b":2, "c": json2.union_(json1) }
// notice de difference
a.union(b) = { "a": 1,  "b":2, "c": json1} 
b.union(a) = { "a": 1,  "b":2, "c": json2} 
```

```
a= {"a":1, "c": [{ "d":1 }] }
b= {"b":2, "c": [{ "e":2 }] }
c= {"a":1, "b":2, c": [{ "d":1 }, { "e":2 }] }
d= {"a":1, "b":2, c": [{ "d":1, "e":2 }] }
e= {"a":1, "b":2, c": [{ "d":1 }] }

c = a.union_(b, SET)
d = a.union_(b, LIST)
e = a.union(b)

f= {"a": [1, 2, {"b": {"b":1} } ] }  
g= {"a": [3, [4,5], 6, 7], "b": [1, 2] }  
h= {"a": [1, 2, {"b": {"b":1} }, 3, [4,5], 6, 7], "b":[1,2]}  
i = {"a": [1, 2, {"b": {"b":1} }, 7], "b":[1,2]} 
j = {"a": [1, 2, {"b": {"b":1} }], "b":[1,2]} 

h = f.union_(g,SET)
h = f.union_(g,MULTISET)
i = f.union_(g,LIST)
j = f.union(g)
```

Given two arrays _c_ and _d_:
* _c.union(d, SET)_ returns _c_ plus those elements from _d_ that don't exist in a.

* _c.union(d, MULTISET)_ returns _c_ plus all the elements from _d_ appended to the back.

* _c.union(d, LIST)_ returns _c_ plus those elements from d which position >= c.size().

* _c.union\_(d)_ returns _c_ plus those elements from _d_ which position >= c.size(), and, at the positions
where a container of the same type exists in _c_ and _d_, the result is their union. This operation doesn't make
any sense if arrays are not considered Lists.

Notice that _c.union(d, SET)_ and _c.union(d, MULTISET)_ are commutative.

Examples:

```
c= [ 1, json1, 2]
d= [ 1, json2, 3, 2]

c.union(d,SET) = [1, json1, 2, json2, 3]
c.union(d,MULTISET) = [1, json1, 2, 1, json2, 3, 2]
c.union(d,LIST) = [1, json1, 2, 2]
c.union_(d) = [1, json1.union_(json2), 2, 2]
```
```
a= [1, 2]
b= [3, [4, 5] , 6]
c= [1, 2, 3, [4, 5], 6]
d= [1, 2, 6]
e= [1, 2, 3, [4, 5], 6, 1, 2, 6]

d= a.union(b,LIST)
c= a.union(b,SET)
c= c.union(d,SET)
e= c.union(d,MULTISET)
```

### <a name="intersection"></a> Intersection
Given two json objects, _a_ and _b_:

* _a.intersection(b, SET)_ returns an object with the keys that exist in both _a_ and _b_ which associated elements are equal,
considering arrays Set of elements. 

* _a.intersection(b, MULTISET)_ returns an object with the keys that exist in both _a_ and _b_ which associated elements are equal,
considering arrays MultiSet of elements.

* _a.intersection(b, LIST)_ returns an object with the keys that exist in both _a_ and _b_ which associated elements are equal,
considering arrays List of elements.

* _a.intersection\_(b, SET)_ behaves as _a.intersection(b, SET)_, but for those keys that exist in both _a_ and _b_ 
which associated elements are json objects, the result is their intersection.

* _a.intersection\_(b, LIST)_ behaves as _a.intersection(b, LIST)_, but for those keys that exist in both _a_ and _b_
which associated elements are json of the same type (object or arrays), the result is their intersection.

* _a.intersection\_(b, MULTISET)_ behaves as _a.intersection(b, MULTISET)_, but for those keys that exist in both _a_ and _b_
which associated elements are json objects, the result is their intersection.

``` 
a = { "b": {"a":1, "b":2, "c": [{"a":1, "b":[1,2]}, {"b":2}, {"c":3}] } }
b = { "b": {"a":1, "b":2, "c": [{"a":1, "b":[1]  }, {"b":2}] } }
c = { "b": {"a":1, "b":2, "c": [{"b":2}] } }

//the json object associated with the key "b", are different
a.intersection(b,LIST) == Jsons.immutable.obj.empty()
a.intersection(b,SET) == Jsons.immutable.obj.empty()
a.intersection(b,MULTISET) == Jsons.immutable.obj.empty()

//the intersection is applied recursively between the json objects associated with the key "b"
a.intersection_(b,LIST) == b
a.intersection_(b,SET) == c
a.intersection_(b,MULTISET) == c

d = { "b": [1, 2, {"a":1       }, true,  null, true ] }
e = { "b": [1, 2, {"a":1, "c":2}, false, true, null, 1 ] }
f = { "b": [1, 2, true, null] }
g = { "b": [1, 2, true, null, true] }
h = { "b": [1, 2]}
h = { "b": [1, 2, {"a":1}] }

f = d.intersection(e,SET)
g = d.intersection(e,MULTISET)
h = d.intersection(e,LIST)
i = d.intersection_(e,LIST)
``` 

Given two json arrays, _c_ and _d_:

* _c.intersection(d, SET)_ returns an array with the elements that exist in both _c_ and _d_

* _c.intersection(d, MULTISET)_ returns an array with the elements that exist in both _c_ and _d_, being duplicates allowed.

* _c.intersection(d, LIST)_ returns an array with the elements that exist in both _c_ and _d_ and are located at the same position.

* _c.intersection\_(d)_ behaves as _a.intersection(b, LIST)_, but, the result for those elements that are containers of the same type and are also located at the same position, is their intersection.

Examples:

to be documented     

## <a name="equality"></a> Equality
The correctness of equals and hashcode methods are crucial for every Java application. As was mentioned before, **json-values**
is data-centric, which means basically that a number is just a number. No matter if it's wrapped in a int, 
a long or even a BigDecimal. According to that, the following objects:

```
JsObj x = JsObj.of("a", JsInt.of(1),
                                    "b", JsLong.of(100)
                                    "c", JsDouble.of(1),
                                    "d", JsDouble.of(10d)
                                   );

JsObj y = JsObj.of("a", JsBigInt.of(BigInteger.ONE),
                                    "b", JsInt.of(100)
                                    "c", JsBigDec.of(BigDecimal.ONE),
                                    "d", JsInt.of(10)
                                   );
```

satisfy the property _x.equals(y) => x.hashCode == y.hashCode()_

Both objects represent the same piece of information, so they are equals, and therefore, they have the same hashcode.
It doesn't matter that different primitive types and wrappers have been used to create both jsons. That's a detail
of the Java language.

There is a method to test if two objects are equals considering arrays Sets or MultiSets:

```
boolean equals(final JsElem elem,
               final TYPE ARRAY_AS
               );
```               

For example:

```
JsArray a = JsArray.of(1,2,3)
JsArray b = JsArray.of(1,2,3,2,3)
JsArray c = JsArray.of(1,2,3,3,2)

Assertions.assertTrue(a.equals(b, TYPE.SET));     
Assertions.assertFalse(a.equals(b, TYPE.MULTISET));
Assertions.assertTrue(b.equals(c, TYPE.SET));
Assertions.assertFalse(b.equals(c, TYPE.MULTISET));
```

Given the same Json created with two different factories:

```
String str = "{...}"

Json x = JsObj.parse(str)

Json y = Jsons.mutable.object.parse(str)

x.equals(y) == y.equals(x)   // false => objects from different factories are never equals 

x.same(y) == y.same(x)       // true
```

## <a name="exceptions-errors"></a> Exceptions and errors
Exceptions and errors are both treated as Exceptions in Java and most of the mainstream languages, but, conceptually, 
they are quite different. Errors mean that someone has to fix something; it could be an error of the user of the library or an error of the library itself. On the other hand, exceptions are expected in irregular situations at runtime, like accessing a non-existing file. No matter what you do, the file could be deleted anytime by any other process, and the only thing you can do is to handle that possibility. 
 
**json-values** uses the custom unchecked exception _UserError_ when the client of the library makes an error,
for example, getting the head of an empty array, which means that the programmer needs to change something to fix the bug. Another error could be to pass in null to a method, in which case it throws a NullPointerException. No method in the library but _equals_ accepts null as a parameter. _InternalError_ is another custom unchecked exception that is thrown when an
error made by the developers is detected.
The only exceptions in the API are the custom checked:

   - MalformedJson, which occurs when parsing a not well-formed string into a Json.
   
   - PatchMalformed, which occurs when a patch can not be applied because it has an invalid schema.
   
   - PatchOpError, which occurs when a patch is applied and returns an error.

## <a name="trampolines"></a> Trampolines
**Json-values**, naturally, uses recursion all the time. To not blow up the stack, tail-recursive method calls are turned into iterative loops by Trampolines. The API exposes a well-known implementation of a 
Trampoline in case you want to do some _head and tail_ programming, and you should! Because, first, it's fun and second and more important, it makes the code more declarative, concise, and easy to reason about.
 My experience says that the more difficult the task is, the more benefit you'll get using this approach; 
 however, sometimes a simple loop is more straightforward and more transparent.
 
 Let's create a function named _schema_ that, given a Json, it returns a new Json describing the types of its elements. Let's 
 only consider strings, numbers, booleans, and null. Find below an example:

```
JsObj obj = JsObj.of("a",JsInt.of(1),
                                      "b",JsStr.of("a"),
                                      "c",TRUE,
                                      "d",NULL,
                                      "e",JsDouble.of(1.3)
                                     );
System.out.println( a.schema() );

{
  "e": { "type": "decimal"},
  "a": { "type": "integral"},
  "b": { "type": "string"},
  "c": { "type": "boolean"},
  "d": { "type": "null"}
}
```

A possible recursive implementation is:

```
    public JsObj schema()
    {

        return schema(this,
                      Jsons.immutable.obj.empty()
                     );
    }

    private JsObj schema(JsObj obj,
                         JsObj acc
                        )
    {
        if (obj.isEmpty()) return acc;
        Map.Entry<String, JsElem> head = obj.head();
        String headName = head.getKey();
        JsElem headElem = head.getValue();
        JsObj tail = obj.tail(headName); 
        if (headElem.isStr()) return schema(tail,
                                            acc.put(fromKey(headName),
                                                    JsObj.of("type",
                                                                              JsStr.of("string")
                                                                              )
                                                    )
                                             );
        if (headElem.isIntegral()) return schema(tail,
                                                 acc.put(fromKey(headName),
                                                         JsObj.of("type",
                                                                                   JsStr.of("integral")
                                                                                  )
                                                         )
                                                );
        if (headElem.isDecimal()) return schema(tail,
                                                acc.put(fromKey(headName),
                                                        JsObj.of("type",
                                                                                  JsStr.of("decimal")
                                                                                 )
                                                        )
                                                );
        if (headElem.isBool()) return schema(tail,
                                             acc.put(fromKey(headName),
                                                     JsObj.of("type",
                                                                               JsStr.of("boolean")
                                                                              )
                                                    )
                                            );
        if (headElem.isNull()) return schema(tail,
                                             acc.put(fromKey(headName),
                                                     JsObj.of("type",
                                                                               JsStr.of("null")
                                                                              )
                                                    )
                                            );
        if (headElem.isJson()) throw new UnsupportedOperationException("Not implemented yet");

        throw new RuntimeException("Unexpected type of JsElem: " + headElem.getClass());
    }
```

However, it blows up the stack when the size of the json is 2033 or higher ( test executed in Java 8). 
Java compiler doesn't optimize tail-recursive calls as Scala or Clojure
compilers do. Nevertheless, we can still make use of Trampolines to turn recursion into an iteration. The following implementation does the trick:

```
    public JsObj schema()
    {

        return schema(this,
                      Jsons.immutable.obj.empty()
                     ).get();
    }

    private Trampoline<JsObj> schema(JsObj obj,
                                     JsObj acc
                                    )
    {
        if (obj.isEmpty()) return Trampoline.done(acc);
        Map.Entry<String, JsElem> head = obj.head();
        String headName = head.getKey();
        JsElem headElem = head.getValue();
        JsObj tail = obj.tail(headName);
        if (headElem.isStr()) 
               return Trampoline.more(() -> schema(tail,
                                                   acc.put(fromKey(headName),
                                                           JsObj.of("type",
                                                                                     JsStr.of("string")
                                                                                     )
                                                           )
                                                   ));
        if (headElem.isIntegral()) 
               return Trampoline.more(() -> schema(tail,
                                                   acc.put(fromKey(headName),
                                                           JsObj.of("type",
                                                                                     JsStr.of("integral")
                                                                                    )
                                                          )
                                                   ));
        if (headElem.isDecimal())
               return Trampoline.more(() -> schema(tail,
                                                   acc.put(fromKey(headName),
                                                           JsObj.of("type",
                                                                                     JsStr.of("decimal")
                                                                                     )
                                                           )
                                                   ));
        if (headElem.isBool()) 
               return Trampoline.more(() -> schema(tail,
                                                   acc.put(fromKey(headName),
                                                           JsObj.of("type",
                                                                                     JsStr.of("boolean")
                                                                                    )
                                                           )
                                                  ));
        if (headElem.isNull()) 
               return Trampoline.more(() -> schema(tail,
                                                   acc.put(fromKey(headName),
                                                           JsObj.of("type",
                                                                                     JsStr.of("null")
                                                                                     )
                                                           )
                                                  ));

        if (headElem.isJson()) throw new UnsupportedOperationException("Not implemented yet...");

        throw new RuntimeException("Unexpected type of JsElem: " + headElem.getClass());
    }
```

A Trampoline is a type that has two concrete implementations: _more_ and _done_. _more_ accepts as a parameter a supplier, which is lazy, so no operation is performed when it's returned and therefore, no call is piled-up on the stack. 
It's when _done_ is returned when the iteration is fired up, and then all the suppliers are computed in order.

## <a name="performance"></a> Performance
A benchmark using [jmh](https://openjdk.java.net/projects/code-tools/jmh/) has been carried out on my computer.
Find below the results parsing a string into a json of size 100,1000 and 10000,
using Jackson and json-values (both mutable an immutable implementations):

```
Benchmark                                                           Mode   Samples    Mean          Mean error    Units
parse_string_jackson_obj_10000                                      avgt     10    1356624.227      442088.003    ns/op
parse_string_jsonvalues_mutable_eclipse_collections_obj_10000       avgt     10    2212575.403     1160516.788    ns/op
parse_string_jsonvalues_mutable_obj_10000                           avgt     10    1295379.624       67240.388    ns/op
parse_string_jsonvalues_immutable_obj_10000                         avgt     10    4047304.291      936095.812    ns/

parse_string_jackson_obj_100000                                     avgt     10   35630406.739    23015218.199    ns/op
parse_string_jsonvalues_mutable_eclipse_collections_obj_100000      avgt     10   10999830.101      544504.730    ns/op
parse_string_json_values_mutable_obj_100000                         avgt     10   15452070.507     1523005.400    ns/op
parse_string_jsonvalues_immutable_obj_100000                        avgt     10   45034250.352    11901240.411    ns/op


parse_string_jackson_obj_1000000                                    avgt     10   319011320.738    176763458.970   ns/op
parse_string_jsonvalues_mutable_eclipse_collections_obj_1000000     avgt     10   166773500.095    28255130.625    ns/op
parse_string_jsonvalues_mutable_obj_1000000                         avgt     10   414601294.875    216932693.384   ns/op
parse_string_jsonvalues_immutable_obj_1000000                       avgt     10   1320324870.750   734454321.103   ns/op
```

As expected, the immutable implementation is slightly slower, but, it could make a difference in those scenarios when
defensive copies of objects are made. On the other hand, the Json that uses the map from Eclipse Collections get the best result
in all the cases.

## <a name="tools"></a> Tools
I've used different compiler plug-ins to find bugs at _compile time_:
* [The Checker Framework Compiler](https://checkerframework.org), which found some NullPointerException.
* [Google error-prone](https://errorprone.info), which found a bug related to [BigDecimalEquals](https://errorprone.info/bugpattern/BigDecimalEquals)

Part of the testing has been carried out using [Scala Check](https://www.scalacheck.org/) and Property-Based Testing. 
I developed a json generator for this purpose.

Any question, feedback, or suggestion, please, drop out an email to imrafael.merino@gmail.com.
