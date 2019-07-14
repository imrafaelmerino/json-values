#### INTRODUCTION
Welcome to **json-values**, the first-ever Json library in _Java_ that uses _persistent data structures_ from _Scala_.
_Java_ doesn't implement _persistent data structures_ natively, nevertheless, Scala does and runs on the _JVM_, so you can go from Java to Scala easily and 
without any impact on the performance. 

I'm a big fan of [Clojure](https://clojure.org) among other functional languages, and with due respect to the obvious differences,
**json-values** follows its philosophy: 

* **immutability over mutability**. If you still have doubts about why, you should take 
a look at one of my favorite talks ever, [_The value of values_](https://www.youtube.com/watch?v=-6BsiVyC1kM) from **Rich Hickey**. 
So **json-values** is _functional_ because you can take advantage of immutable data structures to represent a Json.

* **Data over abstraction**. The API is really declarative and data-centric,
which makes it really simple to use. No fancy abstractions, long enterprise names, setters or complex DSLs, just values
and functions to manipulate them in a _declarative_ way. [Narcissistic Design](https://www.youtube.com/watch?v=LEZv-kQUSi4) from **Stuart Halloway** is a great talk that elaborates on this point.

* _It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures._ —**Alan Perlis**. 
It's a one-package library with two main classes: JsObj and JsArray, that's all you need. 
I'd argue that it makes **json-values** a really simple library and simplicity matters! 

Json-values, naturally, uses recursion all the time. To not blow up the stack, tail-recursive method calls are turned into iterative loops 
by Trampolines. A well-known implementation of a Trampoline is exposed by the API in case you wanna do
some _head and tail_ programming and you should! because, first, it's fun, and second and more important, it makes the code more declarative, concise and easy to reason about. 
My experience in Java says that the more difficult the task is, the more benefit you'll get using this approach. Sometimes a
simple loop is enough and more clear.


#### REQUIREMENTS
* Java 8 or greater.

#### INSTALATION
Add the following dependency to your building tool:
```
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>json-values</artifactId>
  <version>X.Y.Z</version>
</dependency>
```
where X.Y.Z is the latest stable version. And that's all. It's a zero-dependency library, so you won't 
have to go through a kind of dependency hell to get it working. As a matter of fact, you can play around
with the library using the java _REPL_ (>= JAVA 9) just typing
```
jshell --class-path ${PATH_TO_JAR}/json-values-X.Y.Z.jar
```

Before getting started, remember the great quote from **Venkat Subramaniam**:

_By nature we're wired to mistake familiar as simple and the unfamiliar as complex_.
#### 0 - Basic concepts

##### 0.1 - JsPath
A JsPath represents a location in a Json. There are two ways of creating paths:
* From a string using the method _JsPath.of(...)_. A path is made up of keys and indexes separated by dots. Keys
are urlencoded to escape special characters, so they could be part of an url. When keys are numbers, they have to be single-quoted, 
to distinguish them from indexes.
* Using the _key_ and _index_ methods from JsPath, in which case, keys dont have to be url-encoded to escape special characters.
```
{
"a": [ {"b": [1,2,3]} ],
" ": "white-space",
".": "dot",
"1": [false, true]
"'": null,
"": ""
}

// 1
JsPath.of("a.0.b.0")
JsPath.empty().key("a").index(0).key("b").index(0)

// 2
JsPath.of("a.0.b.1")
JsPath.empty().key("a").index(0).key("b").index(1) 

// 3, the index -1 means the last element of the array
JsPath.of("a.0.b.2")
JsPath.empty().key("a").index(0).key("b").index(2)
JsPath.of("a.0.b.-1")
JsPath.empty().key("a").index(0).key("b").index(-1)

// "white-space"
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

// empty string is a valid key!
// ""
JsPath.of("")
JsPath.empty().key("")

+ = "white space"  // JsPath.of("%20") and JsPath.of(" ") would return the same result
'1'.0 = false      // when the key is a number, it has to be single-quoted 
'1'.1 = true 
%27 = 10 
JsPath.of("") = 1  // the empty string is a valid key!
```
##### 0.2 - JsElem
Every element in a Json is a JsElem. There is one for each json value described in [json.org](https://www.json.org) :
* JsStr represents immutable strings.
* The singletons JsBool.TRUE and JsBool.FALSE represents true and false.
* The singleton JsNull.NULL represents null.
* JsObj represents objects. 
* JsArray represents arrays.
* JsNumber represents immutable numbers. There are five different specializations: 
    * JsInt
    * JsLong
    * JsDouble
    * JsBigInt
    * JsBigDec
 
##### 0.3 - JsPair
Unfortunately, there is no tuples in Java. JsPair is a pair which represents an element of a Json and the position where
it's located at:

JsPair = (JsPath, JsElem)

A Json can be modeled as a set of JsPairs, which makes obvious how to implement streams on Jsons (see section 2) 

#### 1- How to create a Json?
json-values uses static factory methods to create objects, just like the ones introduced by Java 9 to create small unmodifiable collections. 
There is a naming convention to emphasize what kind of object is created.
* **of** and **parse** methods return immutable jsons or values.
* **\_of\_** and **\_parse\_** methods return  mutable jsons. 

You may be asking what's the point of using underscores to name methods. The reason is that symbols are great to convey information quickly and concisely,
and distinguish methods that return mutable objects from the ones that return immutable ones, is something that has to be highlighted somehow.
Not like in other languages like Scala, symbols are not allowed in java to name variables and methods. 

##### 1.1- Creation of immutable json objects.
```
import jsonvalues.*;
JsObj empty = JsObj.empty();  // empty immutable instance is a singleton

JsObj x = JsObj.of("a", JsInt.of(1), 
                   "b", JsBool.TRUE, 
                   "c", JsNull.NULL, 
                   "d", JsStr.of("hi")
                   );

JsObj y = empty.put("a", 1)
               .put("b", true)
               .put("c", null)
               .put("d", "hi");

Assert.assertEquals(x, y);   
Assert.assertNotEquals(empty, y);  // empty will never change

// from varargs of json pairs
JsObj w = JsObj.of( JsPair.of("a.b.0", 1 ),
                    JsPair.of("a.b.1", 2 )
                  );
                  
//parsing a string, which returns a jsonvalues.TryObj computation that may fail                       
JsObj z = JsObj.parse("{\"a\": {\"b\": [1,2]}}").orElseThrow(); 

Assert.assertEquals(w, z);   
 ```
##### 1.2- Creation of mutable json objects.
```
import jsonvalues.*;
JsObj _empty_ = JsObj._empty_();  
   
JsObj _x_ = JsObj._of_("a", JsInt.of(1), 
                       "b", JsBool.TRUE, 
                       "c", JsNull.NULL, 
                       "d", JsStr.of("hi")
                       );
                       
JsObj _y_ = _empty_.put("a",1)
                   .put("b",true)
                   .put("c",null)
                   .put("d","hi");
                   
Assert.assertEquals(_x_, _y_);   
Assert.assertEquals(_empty_, _y_); // something called _empty_ it's not empty anymore!  
  
// from vargs of pairs
JsObj _w_ = JsObj._of_( JsPair.of("a.b.0", 1),
                        JsPair.of("a.b.1", 2)
                      );
// parsing a string, which returns a jsonvalues.TryObj computation that may fail                  
JsObj _z_ = JsObj._parse_("{\"a\": {\"b\": [1,2]}}").orElseThrow(); 

Assert.assertEquals(_w_, _z_);       
```

##### 1.3- Creation of immutable json arrays.
```
import jsonvalues.*;
JsArray a = JsArray.of(1,2,3);  // from varargs of int

JsArray b = JsArray.of("a","b","c"); // from varargs of string

JsArray c = JsArray.of(JsBool.TRUE, 
                       JsStr.of("a"), 
                       JsNull.NULL, 
                       JsDouble.of(1.5d)
                       ) // from varargs of JsElem

//from varargs of json pairs
JsArray d = JsArray.of(JsPair.of("0.a.b.0", "a"),
                       JsPair.of("0.a.b.1", "b")
                       );

//parsing a string, which returns a jsonvalues.TryArray computation that may fail                      
JsArray e =  JsArray.parse("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       

JsArray empty = JsArray.empty();

JsArray f = empty.append(JsInt.of(1))  
                 .append(JsInt.of(2))           
                 .prepend(JsInt.of(0));   
                 
Assert.assertNotEquals(empty, f); // empty  will never change      
```               
##### 1.4- Creation of mutable json arrays.
```
import jsonvalues.*;

JsArray _a_ = JsArray._of_(1,2,3);  // from varargs of int

JsArray _b_ = JsArray._of_("a","b","c"); // from varargs of string

// from varargs of JsElem
JsArray _c_ = JsArray._of_(JsBool.TRUE, 
                           JsStr.of("a"), 
                           JsNull.NULL, 
                           JsDouble.of(1.5d)
                           ) 

//from varargs of json pairs
JsArray d = JsArray._of_(JsPair.of("0.a.b.0", "a"),
                         JsPair.of("0.a.b.1", "b")
                         );

//parsing a string, which returns a jsonvalues.TryArray computation                        
JsArray e =  JsArray._parse_("[{\"a\":{\"b\":[1,2]}}]").orElseThrow();       

JsArray _empty_ = JsArray._empty_();

JsArray _f_ = _empty_.append(JsInt.of(1))  
                     .append(JsInt.of(2))           
                     .prepend(JsInt.of(0));   
                     
Assert.assertEquals(_empty_, _f_); //  _empty_ is not empty! is [0,1,2]   
```            
##### 1.5- Going from immutable to mutable back and forth.


   
##### 2- Streams and Collectors.
Stream methods returns sequences of JsPai:
```
JsObj x = JsObj.of("a", JsArray.of(1,2,3),
                   "b", JsObj.of("c",JsInt.of(4),
                                 "d",JsStr.of("hi")
                                )
                   )
x.stream().forEach(System.out::println)
```
prints out the following sequence of two pairs:

("a", [1,2,3])
("b", {"c":4, "d": "hi"})

By convention, **every method that ends with an underscore is applied recursively to every element** that is a Json.
Taking that into account:
```
x.stream_().forEach(System.out::println)
```
prints out the following sequence of five pairs:

("a.0", 1) 
("a.1", 2)
("a.2", 3) 
("b.c", 4)
("b.d", "hi")

Let's multiply by 10 every number
```
Function<JsPair,JsPair> times10ifInt = p -> p.elem.isInt()) ? p.elem.asJsInt().map(i->i*10) : p.elem;
x.stream_().map(times10ifNumber).collector(JsObj.collector());
```
What if you want to get the stream back into a mutable json. Well, taking into account the convention pointed out in _1_, it only requires two underscores:
```
x.stream_().map(times10ifNumber).collector(JsObj._collector_());
```

So, as it was expected, JsObj.\_collector\_() returns a mutable object whereas JsObj.collector() returns an immutable one.

For arrays it's just the same:
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

("0", [1,2])
("1", "red")
("2", {"c":"blue", "d":"pink"})
and
```
x.stream_().forEach(System.out::println)
```
prints out the following sequence of five pairs:

("0.0", 1)
("0.1", 2)
("1", "red")
("2.c", "blue")
("2.d", "pink")

Let's convert every string to uppercase:
```
Function<JsPair,JsPair> toUpperCase = p -> p.elem.isStr() ? p.elem.asJsStr().map(String::toUpperCase) : p.elem;
x.stream_().map(toUpperCase).collector(JsArray.collector())   // stream into immutable array
x.stream_().map(toUpperCase).collector(JsArray._collector_()) // stream into mutable array
```
By the way, the implemented collectors support parallel streams.

##### 3- Filter, map and reduce.

Every operation can be applied only to the first level of the json 
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

or to the whole json recursively, just adding an underscore to the name of the method:

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

filterKeys methods removes keys from json objects based on the full path of the key and it's element.
filterElements methods removes elements which are not containers from jsons based on the full path of the
element and the element itself.
filterObjs is a specialization of filterElements to remove json objects. 

the same considerations applies for map functions, except that it maps elements instead of removing them

reduce functions is a classic map-reduce over the elements (not containers) that satisfies the specified predicate

##### 4- Union and intersection. 
```
JsObj union(final JsObj that);
JsObj intersection(final JsObj that,
                   final TYPE ARRAY_AS
                  );
JsArray union(final JsArray that,
                  final TYPE ARRAY_AS
                 );    
```
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
           
##### 5- Getting data in and pulling data out. 
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

OptionalInt a = json.getInt("a.b.0.c")        // 1
OptionalLong b = json.getLong("a.b.0.d.-1")   // 2
Optional<String> c = json.getStr("a.b.0.e.0") // "a"
Optional<JsArray> d = json.getArr("e.0")      // [1,2]
Optional<JsObj> e = json.getObj("a.b.0")      // {"c":1, "d": [1,2], "e": ["a", "b"]}
OptionalDouble f = json.getDouble("e.-1")     // 1.2
//the get method returns a JsElem
JsElem g = json.get("e.3")                    // null, 
```
The following methods always create the specified element at the specified position, replacing any existing one.  
```
json.append("e.0", JsStr.of("a"))    // append 1 to the back of the array  
json.prepend("e.0", JsStr.of("a"))   // prepend 1 to the front of the array
json.put("d", "a")                   // put "a" at d
JsObj.empty().put("a.2",1)           // {"a":[null,null,1]}, put always creates an element at the specifed position,
                                     // filling with null if necessary
```
There are declarative alternative which requires certain conditions to insert new elements:
```                           
json.putIfAbsent("e.0.2", ()-> JsInt.of(2)) 
json.putIfPresent("a.b.0.d", e -> JsStr.of("a"))
json.appendIfPresent("e.0", ()-> JsStr.of("a"))
json.prependIfPresent("e.0", ()-> JsStr.of("a"))

// if there's no element, the default value is put. If it exists a value, the function is executed, being the parameters
// the default value and the existing one
json.merge("a.b.0.c",
           JsInt.of(1),                                   // no element: put default value
           (d,e)-> e.asJsInt().map(i-> i + d.asJsInt().x) // elem exists: put existing + default 
          );                
```
##### 6- Equality. 
JsObj x = JsObj.of("a", JsInt.of(1),
                   "b", JsLong.of(100)
                   "c", JsDouble.of(1),
                   "d", JsDouble.of(10d)
                  )

JsObj y = JsObj.of("a", JsBigInt.of(BigInteger.ONE),
                   "b", JsInt.of(100)
                   "c", JsBigDec.of(BigDecimal.ONE),
                   "d", JsInt.of(10)
                  )

x.equals(y) => x.hashCode == y.hashCode()

Both objects represent exactly the same Json, so they are equals and therefore, they have the same hashcode.
It doesnt matter that different primitive types and objects have been used to create them. That's a detail
of the Java language and  **json-values** is data-centric.


 boolean equals(final JsElem elem,
                final TYPE ARRAY_AS
               );

##### 7- Exceptions and errors. 
Even though both of them are treated as Exceptions in Java and most of the languages, conceptually 
they are quite different. Errors means that someone has to fix something, it could be an error of 
the user of the library or an error of the library itself. On the other hand, exceptions are expected 
but irregular situations at runtime, like accessing to a non existing file. No matter what you do, the
file could be deleted anytime by any other process and the only thing you can do is to handle that possibility. 
json-values use the native unchecked exception UnsupportedException when the client of the library makes an error,
for example getting the head of an empty array, which means that the programmer need to change something, 
for example, adding a guard condition. Another error could be to pass in null to a method, in which case a 
NullPointerException is thrown. There's is no method in the library that accepts null as a parameter.
The only exception in the API is the checked MalformedJson, which occurs when a not well-formed string is parsed 
into a Json. It's wrapped in a functional Try computation.
##### 8- Trampolines

##### 9- Performance


##### 10- Tools. 


During the development, different compiler plug-ins to find bugs at _compile time_ have been used:
* [The Checker Framework Compiler](https://checkerframework.org), which found some NullPointerException.
* [Google error-prone](https://errorprone.info), which found a bug related to [BigDecimalEquals](https://errorprone.info/bugpattern/BigDecimalEquals)

Scala check




#### ACKNOWLEDGMENTS

 






