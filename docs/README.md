<img src="https://img.shields.io/badge/code%20quality-A-success"/>  [![Coverage](https://img.shields.io/badge/coverage-88-green)](https://imrafaelmerino.github.io/json-values/coverage-report/index.html) <img src="https://img.shields.io/badge/LOC-19797-blue"/>  <img src="https://img.shields.io/badge/HOC-67770-blue"/>

[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/json-values/0.1.2)](https://search.maven.org/artifact/com.github.imrafaelmerino/json-values/0.1.2/jar)
<img src="https://img.shields.io/github/last-commit/imrafaelmerino/json-values"/>
<img src="https://img.shields.io/github/release-date-pre/imrafaelmerino/json-values"/>

#### INTRODUCTION
Welcome to **json-values**, the first-ever Json library in _Java_ that uses _persistent data structures_ 
from _Scala_. _Java_ doesn't implement _persistent data structures_ natively; nevertheless, Scala does and 
runs on the _JVM_; therefore, you can go from Java to Scala smoothly and without any impact on the performance. 

I'm a big fan of [Clojure](https://clojure.org) among other functional languages, and with due respect to the
apparent differences, **json-values** follows its philosophy: 

* **Immutability over mutability**. If you still have doubts about why, you should take 
a look at one of my favorite talks ever, [_The value of values_](https://www.youtube.com/watch?v=-6BsiVyC1kM) 
from **Rich Hickey**. **json-values** is _functional_ because you can take advantage of immutable data structures to represent a Json.

* **Data over abstraction**. The API is declarative and data-centric. You only need values and functions to 
manipulate them and not fancy abstractions, long enterprise names, setters, or complex DSLs. [Narcissistic Design](https://www.youtube.com/watch?v=LEZv-kQUSi4) from **Stuart Halloway** is a 
great talk that elaborates on this point.

* _It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures_. —Alan Perlis. 
It's a one-package library with two main classes: JsObj and JsArray, that's all you need.
 
##### Why use json-values?
Let me enumerate four reasons:

* You need to deal with Jsons, and you want to program following a functional style, but you can't benefit from
all the advantages that immutability brings to your code because Java doesn't provide Persistent Data Structures.
The thing is that Java 8 brought functions, lambdas, lazy evaluation to some extent, streams... but, without immutability, 
something is still missing, and as Pat Helland said, [Immutability Changes Everything!](http://cidrdb.org/cidr2015/Papers/CIDR15_Paper16.pdf)
* You also need a straightforward and declarative API to manipulate Json, which takes advantages of all the
new features that were introduced in Java 8, like streams and collectors.
* You care about all the described above, but, you may still need a mutable object as you are programming in Java. 
With **json-values**, you can go from a mutable Json to an immutable one, back and forth, and the API to manipulate them is the same, being both implementations
hidden to the user.
* Simplicity matters and I'd argue that **json-values** is simple.
 
#### REQUIREMENTS
* Java 8 or greater.

#### INSTALLATION
Add the following dependency to your building tool:
```
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>json-values</artifactId>
  <version>0.1.2</version>
</dependency>
```
and that's all. It's a zero-dependency library, so you won't have to go through a kind of dependency hell to get it working. 
You can play around with the library using the java _REPL_ (>= Java 9) just typing:
```
jshell --class-path ${PATH_TO_JAR}/json-values-X.Y.Z.jar
```
Before getting started, remember the great quote from **Venkat Subramaniam**:

_By nature, we're wired to mistake familiar as simple and the unfamiliar as complex_.
#### 0.1 - JsPath
A JsPath represents a string syntax for identifying a specific value within a JSON. It's similar to the Json Pointer specification
defined in [rfc6901](https://tools.ietf.org/html/rfc6901), but I'd argue that **json-values** implementation it's more readable. 
There are two ways of creating paths:
* From a path-like string using the method _JsPath.of(...)_. A path is made up of keys and indexes 
separated by dots. Keys are URL-encoded to escape special characters; therefore, 
they could be part of an URL. When keys are numbers, they have to be single-quoted, 
to distinguish them from indexes.
* Using the  _fromKey_, _fromIndex_, _key_ and _index_ methods from the _JsPath_ class API. In this case, keys don't need
to be URL-encoded and no string is parsed.

```
{ 
"a": [ {"b": [1,2,3]} ],
" ": "z",
"c.d": 4,
"1": [false, true]
"'": null,
"": 1.2
}
JsPath.of("a.0.b.0") or JsPath.fromKey("a").index(0).key("b").index(0) -> 1
                             
JsPath.of("a.0.b.1") or JsPath.fromKey("a").index(0).key("b").index(1) -> 2

JsPath.of("a.0.b.2") or JsPath.fromKey("a").index(0).key("b").index(2) -> 3 
                               
// the index -1 points to the last element of the array 
JsPath.of("a.0.b.-1") or JsPath.fromKey("a").index(0).key("b").index(-1) -> 3

// whitespace is urlencoded to + or %20 (both are valid)
JsPath.of("+") or JsPath.of("%20") or JsPath.fromKey(" ") -> z  

// dot has to be escaped using of method
JsPath.of("c%2Ed") or JsPath.fromKey("c.d") -> z                           
    
// notice how the key is single-qouted in of method
JsPath.of("'1'.0") or JsPath.fromKey("1").index(0) -> false
JsPath.of("'1'.1") or JsPath.fromKey("1").index(1) -> true

// single quote is urlencoded to %27
JsPath.of("%27") or JsPath.fromKey("'") -> null                              
        
// empty string is a valid key!
JsPath.of("") or JsPath.fromKey("") -> 1.2
```
All the methods that accept a JsPath are overloaded so that a path-like string can be passed in instead.
#### 0.2 - JsElem
Every element in a Json is a _JsElem_. There is one for each json value described in [json.org](https://www.json.org):
* _JsStr_ represents immutable strings.
* The singletons _JsBool.TRUE_ and _JsBool.FALSE_ represent true and false.
* The singleton _NULL_ of type _JsNull_ represents null.
* _JsObj_ is a _Json_ that represents an object, which is an unordered set of name/value pairs.
* _JsArray_ is a _Json_ that represents an array, which is an ordered collection of values.
* _JsNumber_ represents immutable numbers. There are five different specializations: 
    * _JsInt_
    * _JsLong_
    * _JsDouble_
    * _JsBigInt_
    * _JsBigDec_
* The singleton _NOTHING_ of type _JsNothing_ represents nothing. It's not part of any specification. It's a convenient type
that makes certain functions that return a JsElem total on their arguments. For example, the function
 _JsElem get(JsPath)_ is total because returns a JsElem for every JsPath. If there is no element located at the specified
  path, it returns _NOTHING_. In other functions like _Json putIfPresent(Function<JsElem, JsElem>)_, this type comes in handy 
 as well because it's possible, just returning _NOTHING_, not to insert anything even if an element is present. 
#### 0.3 - JsPair
Unfortunately, there are no tuples in Java. _JsPair_ is a pair which represents an element of a _Json_ and the position 
where it's located at:

JsPair = (JsPath, JsElem)

There are different overloaded static factory methods to create pairs:
```
JsPair of(String path, int elem)
JsPair of(String path, double elem)
JsPair of(String path, long elem)
...
JsPair of(JsPath path, int elem)
JsPair of(JsPath path, double elem)
JsPair of(JsPath path, long elem)
...
JsPair of(JsPath path, JsElem elem)
```
Pairs are immutable. You can get the path or element of a pair by direct field access:
```
JsPair pair = JsPair.of("a.b.0", "a");
JsPath path = pair.path;
JsElem elem = pair.elem;
```
Pairs can be mapped using:
```
JsPair mapPath(UnaryOperator<JsPath> fn)
JsPair mapElem(UnaryOperator<JsElem> fn)
```

#### 1- How to create a Json?
**json-values** uses _static factory methods_ to create objects, just like the ones introduced by _Java 9_ to 
create small unmodifiable collections. There is a naming convention to emphasize what kind of object is created:
* **of** and **parse** methods return **immutable** jsons or values.
* **\_of\_** and **\_parse\_** methods return **mutable** jsons. 

You may be asking what's the point of using underscores to name methods. The reason is that symbols are 
great to convey information quickly and concisely, and distinguish methods that return mutable objects from 
the ones that return immutable ones, is something that has to be highlighted somehow. Not like in other 
languages like Scala, symbols are not allowed in Java to name variables and methods, that's why I use an underscore. 
I prefer to use an exclamation as Ruby does, but I can't.
#### 1.1- Creation of immutable json objects.
```
// from keys and associated elements
JsObj.of("a", JsInt.of(1), 
         "b", JsBool.TRUE, 
         "c", JsNull.NULL, 
         "d", JsStr.of("hi")
         );

// from varargs of json pairs
JsObj w = JsObj.of( JsPair.of("a.b.0", 1),
                    JsPair.of("a.b.1", 2)
                  );    
                           
//parsing a string, which returns a TryObj computation that may fail                       
JsObj z = JsObj.parse("{\"a\": {\"b\": [1,2]}}").orElseThrow(); 

Assert.assertEquals(w, z);   
 ```
#### 1.2- Creation of mutable json objects.
```
// from keys and associated elements
JsObj x = JsObj._of_("a", JsInt.of(1), 
                     "b", JsBool.TRUE, 
                     "c", JsNull.NULL, 
                     "d", JsStr.of("hi")
                    );  

// from vargs of json pairs
JsObj w = JsObj._of_( JsPair.of("a.b.0", 1),
                      JsPair.of("a.b.1", 2)
                    );    
                            
// parsing a string, which returns a TryObj computation that may fail                  
JsObj z = JsObj._parse_("{\"a\": {\"b\": [1,2]}}").orElseThrow(); 

Assert.assertEquals(w, z);   
```
#### 1.3- Creation of immutable json arrays.
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
JsArray d = JsArray.of(JsPair.of("0.a.b.0", 1),
                       JsPair.of("0.a.b.1", 2)
                       );
//parsing a string, which returns a TryArr computation that may fail                      
JsArray e =  JsArray.parse("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       

Assert.assertEquals(d, e);  
```               
#### 1.4- Creation of mutable json arrays.
```
// from varargs of ints
JsArray a = JsArray._of_(1,2,3);  

// from varargs of strings
JsArray b = JsArray._of_("a","b","c"); 

// from varargs of JsElem
JsArray c = JsArray._of_(JsBool.TRUE, 
                         JsStr.of("a"), 
                         JsNull.NULL, 
                         JsDouble.of(1.5d)
                         );

// from varargs of json pairs
JsArray d = JsArray._of_(JsPair.of("0.a.b.0", 1),
                         JsPair.of("0.a.b.1", 2)
                         );
// parsing a string, which returns a TryArr computation that may fail                        
JsArray e =  JsArray._parse_("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       

Assert.assertEquals(d, e);  
```
#### 2- Putting data in and getting data out. 
To be able to insert data in and pull data out in a simple way is a must for any Json API. That's why **json-values**
has several overloaded methods that allow the client to work directly with the primitive types, avoiding any conversion. 
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
##### 2-1 Obtaining primitive types.
All the _getXXX_ by path methods, return an Optional or one of its specializations for the particular primitive type. 
```
Assertions.assertEquals(OptionalInt.of(1), 
                        json.getInt("a.b.0.c")
                        );
Assertions.assertEquals(OptionalInt.of(2), 
                        json.getLong("a.b.0.d.-1")
                        );
Assertions.assertEquals(Optional.of("a"), 
                        json.getStr("a.b.0.e.0")
                        );
Assertions.assertEquals(OptionalDouble.of(1.2), 
                        json.getDouble("e.-1")
                        );
Assertions.assertEquals(OptionalInt.empty(), 
                        json.getInt("e.-1")
                        );
Assertions.assertEquals(OptionalInt.empty(), 
                        json.getInt("h")
                        );        
```
##### 2-1 Obtaining JsElems.
To obtains JsObj or JsArray wrapped into an optional:
```
Assertions.assertEquals(Optional.of(JsArray.of(1,2)), 
                        json.getArr("e.0")
                        );
Assertions.assertEquals(Optional.of(JsObj.of("c",JsInt.of(1),
                                             "d",JsArray.of(1,2),
                                             "e",JsArray.of("a","b"))
                                             )), 
                        json.getObj("a.b.0")
                        );
```
Working with JsElem may be necessary sometimes, for example, if it's unknown the type of the element located at a path.
The _get_ by path method returns a _JsElem_ and has the attractive property that is total, as it was mentioned above. Just as a reminder, it means that it returns a JsElem
for every possible path. Functional programmers strive for total functions. It's possible thanks to the _JsNothing_ type.
```
Assertions.assertEquals(JsNull.NULL, 
                        json.get("e.3")
                        );

Assertions.assertEquals(JsNothing.NOTHING, 
                        json.get("f")
                        ); 
```
##### 2-3 Putting data at any arbitrary path.

The _put_ method always inserts the specified element at the specified path:
```
Jsobj a = JsObj.empty().put("a.b.c", 1);
Assertions.assertEquals(JsInt.of(1), a.get("a.b.c"));
Assertions.assertEquals(1, a.getInt("a.b.c").getAsInt());

Jsobj b = a.put("a.b", true);
Assertions.assertEquals(JsBool.TRUE, a.get("a.b") );
Assertions.assertEquals(true, a.getBool("a.b").get() );

//a and b are immutable
Assertions.assertNotEquals(a,b);

JsArray c = JsArray.of(JsObj.of("a",JsArray.of(1,2)),
                       JsObj.of("b",JsArray.of("a","b"))
                       );
Assertions.assertEquals(JsInt.of(1), c.get("0.a.0") );
Assertions.assertEquals(1l, c.getLong("0.a.0").getAsLong() );
                       
JsArray d = c.put("0.a.0",true); 
Assertions.assertEquals(JsBool.TRUE, d.get("0.a.0") );
Assertions.assertEquals(true, d.getBool("0.a.0").get() );


```
The more natural way of adding data to arrays is with the methods _append_ and _prepend_, however, when inserting data in arrays at certain positions, filling with null may be necessary:
```
JsArray e = c.put("0.b.3","c");
Assertions.assertEquals(JsArray.of(JsStr.of("a"),
                                   JsStr.of("b"),
                                   JsNull.NULL,   
                                   JsStr.of("c")
                                   ), 
                        d.get("0.b") 
                        );
```
The point here is being honest. The string "c" has been inserted at the forth position of the array, and for that to happen, filling with null
the third position is necessary.  
##### 2-4 Being idiomatic: _tell, don't ask_ principle.
An attractive principle in OOP is known as ["tell, don't ask."](https://pragprog.com/articles/tell-dont-ask) It leads to more declarative
APIs. The _putIfAbsent_, _putIfPresent_, and _merge_ methods follow that principle. The point is, instead of checking if an element is present 
or not and, in consequence, to call or not the put method, you can do the same thing in just one call:
```
JsObj a = JsObj.empty().putIfPresent("a",1);
Assertions.assertEquals(JsObj.empty(), a);


JsObj b = JsObj.empty().putIfAbsent("a",1);
Assertions.assertEquals(JsInt.of(1), 
                        b.get("a")
                        );

JsObj c = b.putIfAbsent("a",2);
Assertions.assertEquals(b,c)


JsInt defaultElem = JsInt.of(1);
// in the function, d stands for the default element and e stands for the existing one
BiFunction<? super JsElem, ? super JsElem, ? extends JsElem> fn = (d, e)-> e.isInt() ?  e.asJsInt().plus(d.asJsInt()) : d;

// no element exists at "a" -> defaultElement is inserted
JsObj f = JsObj.empty().merge("a",
                              defaultElem,                                   
                              fn
                              ); 
Assertions.assertEquals(JsInt.of(1), f.get("a"));
// an element exists at "a" -> the function is invoked 
JsObj g = f.merge("a",
                  defaultElem,                                   
                  fn
                  ); 
Assertions.assertEquals(JsInt.of(2), 
                        f.get("a")
                        );                     
```
##### 2-5 Being lazy.
It's possible to be lazy and not produce any element if it's not going to be inserted, just passing a supplier:

JsObj d = b.putIfAbsent("a", ()-> computed value);

JsObj e = b.putIfPresent("a", ()-> computed value);

##### 2-6 Adding data to arrays.

To insert elements at the front of an array, it exists the methods _prepend_, _prependAll_, _prependIfPresent_, and _prependAllIfPresent_.
To insert elements at the back of an array, it exists the methods _append_, _appendAll_, _appendIfPresent_, and _appendAllIfPresent_.
The same considerations above apply for all of them.               
#### 3- Streams and Collectors.
After Oracle released Java 8, I can't imagine a data structure in Java without providing the stream and collector operations. They open the door
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
##### 3.1 Streams.
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
##### 3.2 Object collectors.
You can get the stream back into an immutable json object by the collector _JsObj.collector()_:
```
Assert.assertEquals(x,
                    x.stream_().collector(JsObj.collector())
                    );
```
or into a mutable json object by the collector _JsObj.\_collector\_()_:
```
Assert.assertEquals(x,
                    x.stream_().collector(JsObj._collector_())
                    );
```
##### 3.3 Array collectors.

You can get the stream back into an immutable json array by the collector _JsArray.collector()_:
```
Assert.assertEquals(y,
                    y.stream_().collector(JsArray.collector())
                    );

```
or into a mutable json array by the collector _JsArray.\_collector\_()_:
```
Assert.assertEquals(y,
                    y.stream_().collector(JsArray._collector_())
                    );

```

#### 4- Filter, map, and reduce.
What would an API be nowadays without filter, map, and reduce?. They are the crown jewel in functional programming and have been implemented
carefully in different ways taking into account the structure of a Json.

Functions which name ends with an underscore, are applied recursively to every element and not only to the first level of the json. It's
a naming convention in the API. As was mentioned before, names with symbols (except _ and $) are not valid in Java.
##### 4.1- Filter
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

##### 4.2- Map
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

The map functions have been designed in such a way that they don't change the structure of the json, which reminds of _functors_, a concept that it may be familiar if you know _Haskell_.
        
##### 4.3- Reduce
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
#### 5- Union and intersection.
Considering jsons Set of pairs, it seems reasonable to implement Set-Theory operations like union and intersection.
For certain operations, arrays can be considered Sets, MultiSets or Lists. In Sets, the order of data items does not 
matter (or is undefined) but duplicate data items are not permitted. In Lists, the order of data matters and duplicate data items are permitted. 
In MultiSets, the order of data items does not matter, but in this case, duplicate data items are permitted. 

##### 5.1- Union 
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
where a container of the same type exists in _c_ and _d_, the result is their union. This operations doesn't make
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
##### 5.2- Intersection 
Given two json objects _a_ and _b_:

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
a.intersection(b,LIST) == JsObj.empty()
a.intersection(b,SET) == JsObj.empty()
a.intersection(b,MULTISET) == JsObj.empty()

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

Given two json arrays _c_ and _d_:
* _c.intersection(d, SET)_ returns an array with the elements that exist in both _c_ and _d_
* _c.intersection(d, MULTISET)_ returns an array with the elements that exist in both _c_ and _d_, being duplicates allowed.
* _c.intersection(d, LIST)_ returns an array with the elements that exist in both _c_ and _d_ and are located at the same position.
* _c.intersection\_(d)_ behaves as _a.intersection(b, LIST)_, but for those elements that are containers of the same type and are
located at the same position, the result is their intersection.

Examples:

to be documented     


#### 6- Equality. 
The correctness of equals and hashcode methods are crucial for every Java application. As was mentioned before, **json-values**
is data-centric, which means basically that the number one is the number one, and forgive the repetition. No matter if it's placed in a int, 
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

#### 7- Exceptions and errors. 
Exceptions and errors are both treated as Exceptions in Java and most of the mainstream languages, but, conceptually, they are quite different. Errors mean that someone has to fix something; it could be an error of the user of the library or an error of the library itself. On the other hand, exceptions are expected in irregular situations at runtime, like accessing a non-existing file. No matter what you do, the
file could be deleted anytime by any other process, and the only thing you can do is to handle that possibility. 
**json-values** use the native unchecked exception _UnsupportedException_ when the client of the library makes an error,
for example, getting the head of an empty array, which means that the programmer needs to change something, 
for example, adding a guard condition. Another error could be to pass in null to a method, in which case it throws a  NullPointerException. No method in the library accepts null as a parameter.
The only exception in the API is the checked MalformedJson, which occurs when parsing a not well-formed string into a Json.

#### 8- Trampolines
**Json-values**, naturally, uses recursion all the time. To not blow up the stack, tail-recursive method 
calls are turned into iterative loops by Trampolines. The API exposes a well-known implementation of a 
Trampoline in case you want to do some _head and tail_ programming, and you should! Because, first, it's 
fun and second and more important, it makes the code more declarative, concise, and easy to reason about.
 My experience says that the more difficult the task is, the more benefit you'll get using this approach; 
 however, sometimes a simple loop is more straightforward and more transparent.
 Example: 
 
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
                      JsObj.empty()
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
                                             acc.put(headName,
                                                     JsObj.of("type",
                                                              JsStr.of("string")
                                                             )
                                                     )
                                             );
        if (headElem.isIntegral()) return schema(tail,
                                                 acc.put(headName,
                                                         JsObj.of("type",
                                                                  JsStr.of("integral")
                                                                 )
                                                        )
                                                );
        if (headElem.isDecimal()) return schema(tail,
                                                acc.put(headName,
                                                        JsObj.of("type",
                                                                  JsStr.of("decimal")
                                                                )
                                                        )
                                                );
        if (headElem.isBool()) return schema(tail,
                                             acc.put(headName,
                                                     JsObj.of("type",
                                                              JsStr.of("boolean")
                                                             )
                                                    )
                                            );
        if (headElem.isNull()) return schema(tail,
                                             acc.put(headName,
                                                     JsObj.of("type",
                                                              JsStr.of("null")
                                                             )
                                                    )
                                            );
        if (headElem.isJson()) throw new UnsupportedOperationException("Not implemented yet");

        throw new RuntimeException("Unexpected type of JsElem: " + headElem.getClass());
    }
```

However, it blows up the stack when the size of the json is 2033 or greater ( test executed in Java 8). 
Java compiler doesn't optimize tail-recursive calls as Scala or Clojure
compilers do. Nevertheless, we can still make use of Trampolines to turn recursion into an iteration. The following implementation does the trick:

```

    public JsObj schema()
    {

        return schema(this,
                      JsObj.empty()
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
        if (headElem.isStr()) return Trampoline.more(() -> schema(tail,
                                                                  acc.put(headName,
                                                                          JsObj.of("type",
                                                                                   JsStr.of("string")
                                                                                  )
                                                                         )
                                                                 ));
        if (headElem.isIntegral()) return Trampoline.more(() -> schema(tail,
                                                                       acc.put(headName,
                                                                               JsObj.of("type",
                                                                                        JsStr.of("integral")
                                                                                       )
                                                                              )
                                                                      ));
        if (headElem.isDecimal()) return Trampoline.more(() -> schema(tail,
                                                                      acc.put(headName,
                                                                              JsObj.of("type",
                                                                                       JsStr.of("decimal")
                                                                                      )
                                                                             )
                                                                     ));
        if (headElem.isBool()) return Trampoline.more(() -> schema(tail,
                                                                   acc.put(headName,
                                                                           JsObj.of("type",
                                                                                    JsStr.of("boolean")
                                                                                   )
                                                                          )
                                                                  ));
        if (headElem.isNull()) return Trampoline.more(() -> schema(tail,
                                                                   JsObj.of("type",
                                                                            JsStr.of("null")
                                                                           )
                                                                   ));


        if (headElem.isJson()) throw new UnsupportedOperationException("Not implemented yet");

        throw new RuntimeException("Unexpected type of JsElem: " + headElem.getClass());
    }
```
A Trampoline is a type that has two concrete implementations: _more_ and _done_. _more_ accepts as a parameter 
a supplier, which is lazy, so no operation is performed when it's returned and therefore, no call is piled-up on the stack. 
It's when _done_ is returned when the iteration is fired up, and then all the suppliers are computed in order.
#### 9- Performance
A benchmark using [jmh](https://openjdk.java.net/projects/code-tools/jmh/) has been carried out on my computer.
Find below the results parsing a string into a json of size 100,1000 and 10000,
using Jackson and json-values (both mutable an immutable implementations):
```
Benchmark          Mean        Mean error   Units
jackson_100       4003.461       95.410     ns/op
mutable_100       5778.173      277.587     ns/op
immutable_100     6895.163      182.102     ns/op

jackson_1000     121199.401    27685.712    ns/op
mutable_1000     119360.112     3655.862    ns/op
immutable_1000   200210.212     7026.397    ns/op

jackson_10000    1089908.830    23290.189   ns/op
mutable_10000    1309524.154   276923.222   ns/op
immutable_10000  2460901.909    66524.870   ns/op
```
As expected, the immutable implementation is slightly slower, but, it could make a difference in those scenarios when
defensive copies of objects are made.

#### 10- Tools. 
I've used different compiler plug-ins to find bugs at _compile time_:
* [The Checker Framework Compiler](https://checkerframework.org), which found some NullPointerException.
* [Google error-prone](https://errorprone.info), which found a bug related to [BigDecimalEquals](https://errorprone.info/bugpattern/BigDecimalEquals)

Part of the testing has been carried out using [Scala Check](https://www.scalacheck.org/) and Property-Based Testing. 
I developed a json generator for this purpose.

Any doubt, feedback or suggestion, please, drop out an email to imrafael.merino@gmail.com.







