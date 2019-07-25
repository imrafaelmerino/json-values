#### INTRODUCTION
Welcome to **json-values**, the first-ever Json library in _Java_ that uses _persistent data structures_ 
from _Scala_. _Java_ doesn't implement _persistent data structures_ natively, nevertheless, Scala does and 
runs on the _JVM_; therefore, you can go from Java to Scala smoothly and without any impact on the performance. 

I'm a big fan of [Clojure](https://clojure.org) among other functional languages, and with due respect to the
apparent differences, **json-values** follows its philosophy: 

* **immutability over mutability**. If you still have doubts about why, you should take 
a look at one of my favorite talks ever, [_The value of values_](https://www.youtube.com/watch?v=-6BsiVyC1kM) 
from **Rich Hickey**. **json-values** is _functional_ because you can take advantage of immutable data structures to represent a Json.

* **Data over abstraction**. The API is declarative and data-centric. You only need values and functions to 
manipulate them and not fancy abstractions, long enterprise names, setters or complex DSLs.[Narcissistic Design](https://www.youtube.com/watch?v=LEZv-kQUSi4) from **Stuart Halloway** is a 
great talk that elaborates on this point.

* _It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures_. —Alan Perlis. 
It's a one-package library with two main classes: JsObj and JsArray, that's all you need. I'd argue that it makes **json-values** 
a simple to use library and simplicity matters!
 
**Json-values**, naturally, uses recursion all the time. To not blow up the stack, tail-recursive method 
calls are turned into iterative loops by Trampolines. The API exposes a well-known implementation of a 
Trampoline in case you want to do some _head and tail_ programming, and you should! Because, first, it's 
fun and second and more important, it makes the code more declarative, concise, and easy to reason about.
 My experience says that the more difficult the task is, the more benefit you'll get using this approach; 
 however, sometimes a simple loop is simpler and more transparent.
 
#### REQUIREMENTS
* Java 8 or greater.

#### INSTALATION
Add the following dependency to your building tool:
```
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>json-values</artifactId>
  <version>0.1.1</version>
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
defined in [rfc6901](https://tools.ietf.org/html/rfc6901), but I'd argue it's more readable. 
There are two ways of creating paths:
* From a path-like string using the method _JsPath.of(...)_. A path is made up of keys and indexes 
separated by dots. Keys are URL-encoded to escape special characters; therefore, 
they could be part of an URL. When keys are numbers, they have to be single-quoted, 
to distinguish them from indexes.
* Using the _key_ and _index_ methods from the _JsPath_ class API, in which case, the keys aren't URL-encoded. This way is less readable and less concise. On the other hand,  it's more efficient because no string is parsed and sometimes more convenient because keys don't need to to be URL-encoded. I recommend using the path-like string approach at first and only
to change to the API way during optimization and if it's justified.
```
{ 
"a": [ {"b": [1,2,3]} ],
" ": "z",
".": "dot",
"1": [false, true]
"'": null,
"": 1.2
}

// 1
JsPath.of("a.0.b.0")                                
JsPath.empty().key("a").index(0).key("b").index(0)
// 2
JsPath.of("a.0.b.1")                                
JsPath.empty().key("a").index(0).key("b").index(1) 
// 3
JsPath.of("a.0.b.2")                                
JsPath.empty().key("a").index(0).key("b").index(2)  
// the index -1 points to the last element of the array (3 in this case)
JsPath.of("a.0.b.-1")                               
JsPath.empty().key("a").index(0).key("b").index(-1) 
// "z"
JsPath.of("+")                                       
JsPath.empty().key(" ")                            
JsPath.of("%20")                                    
// false
JsPath.of("'1'.0")                                  
JsPath.empty().key("1").index(0)                    
// true
JsPath.of("'1'.1")                                 
JsPath.empty().key("1").index(1)
// null
JsPath.of("%27")                                  
JsPath.empty().key("'")                             
// 1.2, empty string is a valid key!
JsPath.of("")                                      
JsPath.empty().key("")
```
#### 0.2 - JsElem
Every element in a Json is a _JsElem_. There is one for each json value described in [json.org](https://www.json.org):
* _JsStr_ represents immutable strings.
* The singletons _JsBool.TRUE_ and _JsBool.FALSE_ represent true and false.
* The singleton _JsNull.NULL_ represents null.
* _JsObj_ represents a json object, which is an unordered set of name/value pairs.
* _JsArray_ represents a json array, which is an ordered collection of values.
* _JsNumber_ represents immutable numbers. There are five different specializations: 
    * _JsInt_
    * _JsLong_
    * _JsDouble_
    * _JsBigInt_
    * _JsBigDec_
* The singleton _JsNothing.NOTHING_ represents nothing. It's not part on any specification. It's a convenient type
that make certain functions that return a JsElem total on their arguments. For example the function _JsElem get(JsPath)_  is total
because returns a JsElem for every JsPath. If there is no element located at a path, it returns _NOTHING_.
In other functions like _Json putIfPresent(Function<JsElem,JsElem>)_, this type comes in handy as well because allows the function
to insert nothing even if an element is present just returning _JsNothing.NOTHING_
#### 0.3 - JsPair
Unfortunately, there are no tuples in Java. _JsPair_ is a pair which represents an 
element of a _Json_ and the position where it's located at:

JsPair = (JsPath, JsElem)

A set of _JsPairs_ can model a Json, which makes obvious how to implement streams on _Jsons_ (see section 2).
#### 1- How to create a Json?
**json-values** uses _static factory methods_ to create objects, just like the ones introduced by Java 9 to 
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
JsObj x = JsObj.of("a", JsInt.of(1), 
                   "b", JsBool.TRUE, 
                   "c", JsNull.NULL, 
                   "d", JsStr.of("hi")
                   );
// empty immutable instance is a singleton
JsObj empty = JsObj.empty();                    
JsObj y = empty.put("a", 1)
               .put("b", true)
               .put("c", null)
               .put("d", "hi");
Assert.assertEquals(x, y);   
// empty will never change
Assert.assertNotEquals(empty, y);  

// from varargs of json pairs
JsObj w = JsObj.of( JsPair.of("a.b.0", 1 ),
                    JsPair.of("a.b.1", 2 )
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
JsObj empty = JsObj._empty_();               
JsObj y = empty.put("a",1)
               .put("b",true)
               .put("c",null)
               .put("d","hi");         
Assert.assertEquals(x, y);   
// something called _empty_ it's not empty anymore! 
Assert.assertEquals(empty, y);  
  
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
JsArray d = JsArray.of(JsPair.of("0.a.b.0", "a"),
                       JsPair.of("0.a.b.1", "b")
                       );
//parsing a string, which returns a TryArr computation that may fail                      
JsArray e =  JsArray.parse("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       
Assert.assertEquals(d, e);  

JsArray empty = JsArray.empty();
JsArray f = empty.append(1)  
                 .append(2)           
                 .prepend(0);   
// empty will never change  
Assert.assertNotEquals(empty, f);     
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
JsArray d = JsArray._of_(JsPair.of("0.a.b.0", "a"),
                         JsPair.of("0.a.b.1", "b")
                         );
// parsing a string, which returns a TryArr computation that may fail                        
JsArray e =  JsArray._parse_("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       
Assert.assertEquals(d, e);  

JsArray empty = JsArray._empty_();
JsArray f = empty.append(1)  
                 .append(2)           
                 .prepend(0);   
// empty is not empty!, it's [0,1,2] 
Assert.assertEquals(empty, f);   
```               
#### 2- Streams and Collectors.
Stream methods returns sequences of JsPairs:
```
JsObj x = JsObj.of("a", JsArray.of(1,2,3),
                   "b", JsObj.of("c",JsInt.of(4),
                                 "d",JsStr.of("hi")
                                )
                   )
x.stream().forEach(System.out::println)
```
prints out the following sequence of two pairs:
```text
("a", [1,2,3])
("b", {"c":4, "d": "hi"})
```
By convention, every method that ends with an underscore is applied recursively to every element that is a Json.
Taking that into account:
```
x.stream_().forEach(System.out::println)
```
prints out the following sequence of five pairs:
```text
("a.0", 1) 
("a.1", 2)
("a.2", 3) 
("b.c", 4)
("b.d", "hi")
```
You can get the stream back into a json object by a collector:
```
// the collector JsObj.collector() returns an immutable json object
JsObj a = x.stream_().collector(JsObj.collector());
Assert.assertEquals(x,a);

// the collector JsObj._collector_() returns a mutable json object
JsObj b = x.stream_().collector(JsObj._collector_());
Assert.assertEquals(x,b);

```

For arrays, it's just the same:
```
JsArray x = JsArray.of(JsArray.of(1,2), 
                       JsStr.of("red"), 
                       JsObj.of("c","blue", 
                                "d", "pink"
                               )
                       )
x.stream().forEach(System.out::println)
```
prints out the following sequence of three pairs:
```text
("0", [1,2])
("1", "red")
("2", {"c":"blue", "d":"pink"})
```
and
```
x.stream_().forEach(System.out::println)
```
prints out the following sequence of five pairs:
```text
("0.0", 1)
("0.1", 2)
("1", "red")
("2.c", "blue")
("2.d", "pink")
```
You can get the stream back into a json array by a collector:
```
// the collector JsArray.collector() returns an immutable json array
JsObj a = x.stream_().collector(JsArray.collector());
Assert.assertEquals(x,a);

// the collector JsArray._collector_() returns a mutable json array
JsObj b = x.stream_().collector(JsArray._collector_());
Assert.assertEquals(x,b);

```
#### 3- Filter, map and reduce.
Every operation can be applied only to the first level of a json 
```
Json filterKeys(final Predicate<JsPair> predicate);
Json filterElems(final Predicate<JsPair> predicate);
Json filterObjs(final BiPredicate<JsPath, JsObj> predicate); 

Json mapElems(final Function<JsPair, ? extends JsElem> fn,
              final Predicate<JsPair> predicate
             );
Json mapKeys(final Function<JsPair, String> fn,
             final Predicate<JsPair> predicate
            );
Json mapObjs(final BiFunction<JsPath, JsObj, JsObj> fn,
             final BiPredicate<JsPath, JsObj> predicate
            );
            
<R> Optional<R> reduce(BinaryOperator<R> op,
                       Function<? super JsPair, R> map,
                       Predicate<? super JsPair> predicate
                       );          
```
or to the whole structure recursively, just adding an underscore to the name of the method:

```
Json filterElems_(final Predicate<JsPair> predicate);
Json filterKeys_(final Predicate<JsPair> predicate);
Json filterObjs_(final BiPredicate<JsPath, JsObj> predicate); 

Json mapKeys_(final Function<JsPair, String> fn,
              final Predicate<JsPair> predicate
          );
Json mapElems_(final Function<JsPair, ? extends JsElem> fn,
               final Predicate<JsPair> predicate
           );
Json mapObjs_(final BiFunction<JsPath, JsObj, JsObj> fn,
              final BiPredicate<JsPath, JsObj> predicate
          );
           
<R> Optional<R> reduce_(BinaryOperator<R> op,
                        Function<? super JsPair, R> map,
                        Predicate<? super JsPair> predicate
                       );               
```

_filterKeys_ methods remove keys from JsObj based on the full path of the key and its associated element.

_filterElems_ methods remove keys from JsObj based on the full path of the key and its associated element.

_filterObjs_ is a specialization of filterElems to remove json objects. 

The same considerations apply for the map functions, except that they map the elements that satisfy 
the specified predicate, instead of removing them.

_reduce_ functions are a classic map-reduce over the elements (not containers) that satisfies the specified predicate

Considering a Json a tree, we can conclude that the presented map and filter functions don't change the structure 
of the tree, they transform their leaves (map) or cut them down (filter). They remind me of _functors_. If you know 
_Haskell_, it must be familiar.


#### 3.1- Examples.
to be documented.

#### 4- Union and intersection. 
Both operations can be applied to the first level of the jsons
```
JsObj union(final JsObj that);
JsObj intersection(final JsObj that,
                   final TYPE ARRAY_AS
                  );
JsArray union(final JsArray that,
              final TYPE ARRAY_AS
             );    
```
or to the whole structure recursively
```              
JsObj union_(final JsObj that,
             final TYPE ARRAY_AS
            );
JsObj intersection_(final JsObj that,
                    final TYPE ARRAY_AS
                   );               
JsArray union_(final JsArray that);
JsArray intersection_(final JsArray that);
JsArray intersection_(final JsArray that);
```             
Arrays can be considered lists (ordered and with duplicates), sets(not ordered and without duplicated)
alternatively, multisets (not ordered and with duplicates) using the parameter _ARRAY_AS_.

#### 4.1- Examples.
to be documented.

#### 5- Putting data in and getting data out. 
To be able to insert data in and pull data out in a simple way is a must for any API. That's why **json-values**
has several overloaded methods that allows the client to work directly with the primitive types, without any kind
of conversion.
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

// 1
OptionalInt a = json.getInt("a.b.0.c");
// 2   
OptionalLong b = json.getLong("a.b.0.d.-1");  
// "a" 
Optional<String> c = json.getStr("a.b.0.e.0"); 
// [1,2]
Optional<JsArray> d = json.getArr("e.0");     
// {"c":1, "d": [1,2], "e": ["a", "b"]}
Optional<JsObj> e = json.getObj("a.b.0"); 
// 1.2   
OptionalDouble f = json.getDouble("e.-1");  
// OptionalInt.empty() because the element doesn't exist
OptionalInt f = json.getInt("h");  
// OptionalInt.empty() because the element is not an integer
OptionalInt f = json.getInt("e.-1");  

//the get method returns a JsElem (the singleton JsNull.NULL in this case)
JsElem g = json.get("e.3")                    
```
The following methods always create the specified element at the specified position, replacing any existing element.  
```
// put "a" at d
json.put("d", "a")   
// prepend 2 to the front of the array
json.append("e.0",1)    
json.prepend("e.0", 2)   /
           
// {"a":[null,null,1]}, put always creates an element at the specifed position,  filling with null if necessary  
JsObj.empty().put("a.2",1)           
                                     
```
There are declarative alternatives which require certain conditions to insert the element:
```                           
json.putIfAbsent("e.0.2", 2) 
json.putIfPresent("a.b.0.d", "a")
json.appendIfPresent("e.0", "a")
json.prependIfPresent("e.0", "a")
json.merge("a.b.0.c",
           JsInt.of(1),                                   // no element: puts default value
           (d,e)-> e.asJsInt().map(i-> i + d.asJsInt().x) // elem exists: puts existing + default 
          );                
```
#### 6- Equality. 
The following objects
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

Both objects represent the same json, so they are equals, and therefore, they have the same hashcode.
It doesn't matter that different primitive types and wrappers have been used to create them. That's a detail
of the Java language and  **json-values** is data-centric.

There is a method to test if two objects are equals considering arrays sets or multisets.
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

a.equals(b, TYPE.SET)      //true
a.equals(b, TYPE.MULTISET) //false
b.equals(c, TYPE.SET)      //true
b.equals(c, TYPE.MULTISET) //false
```

#### 7- Exceptions and errors. 
Exceptions and errors are both treated as Exceptions in Java and most of the mainstream languages, but, conceptually, they are quite different. Errors mean that someone has to fix something; it could be an error of the user of the library or an error of the library itself. On the other hand, exceptions are expected in irregular situations at runtime, like accessing a non-existing file. No matter what you do, the
file could be deleted anytime by any other process, and the only thing you can do is to handle that possibility. 
**json-values** use the native unchecked exception _UnsupportedException_ when the client of the library makes an error,
for example, getting the head of an empty array, which means that the programmer needs to change something, 
for example, adding a guard condition. Another error could be to pass in null to a method, in which case it throws a  NullPointerException. No method in the library accepts null as a parameter.
The only exception in the API is the checked MalformedJson, which occurs when a not well-formed string is parsed into a Json.

#### 8- Trampolines
to be documented

#### 9- Performance
A benchmark using [jmh](https://openjdk.java.net/projects/code-tools/jmh/) has been carried out on my computer.
The following results have been obtained parsing a string into a json of size 100,1000 and 10000,
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
As expectd, the immutable implementation is slightly slower, but, it could make a difference in those scenarios when
defensive copies of objects are performed.

#### 10- Tools. 
Different compiler plug-ins to find bugs at _compile time_ have been used:
* [The Checker Framework Compiler](https://checkerframework.org), which found some NullPointerException.
* [Google error-prone](https://errorprone.info), which found a bug related to [BigDecimalEquals](https://errorprone.info/bugpattern/BigDecimalEquals)

Part of the testing has been carried out using [Scala Check](https://www.scalacheck.org/) and Property Based Testing. 
I developed a json generator for this purpose.

#### 11- Release plan
to be documented

Any doubt, feedback or suggestion, please,  drop out an email to imrafael.merino@gmail.com

 






