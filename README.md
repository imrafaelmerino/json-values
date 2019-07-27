#### INTRODUCTION
<div style="text-align: justify">
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
It's a one-package library with two main classes: JsObj and JsArray, that's all you need. I'd argue that it makes **json-values** 
a simple to use library and simplicity matters!
 
**Json-values**, naturally, uses recursion all the time. To not blow up the stack, tail-recursive method 
calls are turned into iterative loops by Trampolines. The API exposes a well-known implementation of a 
Trampoline in case you want to do some _head and tail_ programming, and you should! Because, first, it's 
fun and second and more important, it makes the code more declarative, concise, and easy to reason about.
 My experience says that the more difficult the task is, the more benefit you'll get using this approach; 
 however, sometimes a simple loop is more straightforward and more transparent.
 
#### REQUIREMENTS
* Java 8 or greater.

#### INSTALLATION
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
defined in [rfc6901](https://tools.ietf.org/html/rfc6901), but I'd argue that **json-values** implementation it's more readable. 
There are two ways of creating paths:
* From a path-like string using the method _JsPath.of(...)_. A path is made up of keys and indexes 
separated by dots. Keys are URL-encoded to escape special characters; therefore, 
they could be part of an URL. When keys are numbers, they have to be single-quoted, 
to distinguish them from indexes.
* Using the _key_ and _index_ methods from the _JsPath_ class API. This way is less readable and less concise. On the other hand,  it's more efficient because no string is parsed and sometimes more convenient because keys don't need to to be URL-encoded. I recommend using the path-like string approach at first and only
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
All the methods that accept a JsPath are overloaded so that a path-like string can be passed in instead.
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
* The singleton _JsNothing.NOTHING_ represents nothing. It's not part of any specification. It's a convenient type
that makes certain functions that return a JsElem total on their arguments. For example, the function _JsElem get(JsPath)_ is total
because returns a JsElem for every JsPath. If there is no element located at a path, it returns _NOTHING_.
In other functions like _Json putIfPresent(Function<JsElem, JsElem>)_, this type comes in handy as well because it's
possible, just returning _JsNothing.NOTHING_, not to insert anything even if an element is present. 
#### 0.3 - JsPair
Unfortunately, there are no tuples in Java. _JsPair_ is a pair which represents an 
element of a _Json_ and the position where it's located at:

JsPair = (JsPath, JsElem)

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
Assertions.assertEquals(Optional.of(JsArray.of(1,2)), 
                        json.getArr("e.0")
                        );
Assertions.assertEquals(Optional.of(JsObj.of("c",JsInt.of(1),
                                             "d",JsArray.of(1,2),
                                             "e",JsArray.of("a","b"))
                                             )), 
                        json.getObj("a.b.0")
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
##### 2-1 Obtaining json elements.
Working with JsElem may be necessary sometimes, for example, if it's unknown the type of the element.
The _get_ by path method returns a _JsElem_ and has the attractive property that is total. What does it mean? Well, it means that it returns a JsElem
for every possible path passed in. Functional programmers strive for total functions. As I mentioned above, it's possible thanks to the _JsNothing_ type.
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
The more natural way of adding data to arrays is with the methods append and prepend, however, when inserting data in arrays at certain positions, filling with null may be necessary:
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
##### 2-4 Being idiomatic: tell, don't ask.
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
Function<? super JsElem, ? extends JsElem> fn = (d,e)-> if(e.isInt()) e.asJsInt().plus(d.asJsInt()) else d;

// no element exists at "a" -> defaultElement is inserted
JsObj f = JsObj.empty().merge("a",
                              defaultElem,                                   
                              fn
                              ); 
Assertions.assertEquals(JsInt.of(1), f.get("a"));
// an element exists at "a" -> the function is invoked and the default element is added to existing one: 1 + 1
JsObj g = f.merge("a",
                  defaultElem,                                   
                  fn
                  ); 
Assertions.assertEquals(JsInt.of(2), 
                        f.get("a")
                        );                     
```
##### 2-5 Being lazy.

// it's possible to be lazy and not produce the element if it's not going to be inserted, just using a supplier:
JsObj d = b.putIfAbsent("a", ()-> computed value);
JsObj e = b.putIfPresent("a", ()-> computed value);

##### 2-5 Adding data to arrays.

To insert elements at the front of an array exist the methods _prepend_, _prependAll_, _prependIfPresent_, and _prependAllIfPresent_.
To insert elements at the back of an array exist the methods _append_, _appendAll_, _appendIfPresent_, and _appendAllIfPresent_.
The same considerations above apply for all of them.               
#### 3- Streams and Collectors.
After Oracle released Java 8, I can't imagine a data structure in Java without providing a stream and collector operations. They open the door
to manipulate data in a very functional way.
A set of _JsPairs_ can model a Json, which makes obvious how to implement streams on _Jsons_. For example, the following set:
````
{(a, 1), (b, 2), (c.d, "a"), (c.e.0, 1), (c.e.1, 2), (_, JsNothing)}  where _ means any other path
````
represents
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
On the other hand, to implement a collector in Java, a mutable data structure to accumulate the data in, is required. It's the real reason that
there is a mutable implementation in **json-values**. At first, I had no intention of providing one, but sometimes you have to give in and be coherent with the
language you are programming in. 
##### 3.1 Streams.
Stream methods return sequences of JsPairs on-demand:
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
For arrays, it's just the same:
```
JsArray y = JsArray.of(JsArray.of(1,2), 
                       JsStr.of("red"), 
                       JsObj.of("c","blue", 
                                "d", "pink"
                               )
                       )
y.stream().forEach(System.out::println)
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
As was mentioned before, methods which name ends with an underscore, are applied recursively and not only to the first level of the json.
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

#### 5.1- Examples.
to be documented.

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
of the Java language, and **json-values** is data-centric.

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

Part of the testing has been carried out using [Scala Check](https://www.scalacheck.org/) and Property-Based Testing. 
I developed a json generator for this purpose.

#### 11- Release plan
to be documented

Any doubt, feedback or suggestion, please,  drop out an email to imrafael.merino@gmail.com.

 </div>






