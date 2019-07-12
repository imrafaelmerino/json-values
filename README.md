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

* _It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures._ —**Alan Perlis**. It's
a one-package library with two main classes: JsObj and JsArray, that's all you need. 
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
where X.Y.Z is the latest estable version. And that's all. It's a zero-dependency library, so you won't 
have to go through a kind of dependency hell to get it working. As a matter of fact, you can play around
with the library using the java _REPL_ (>= JAVA 9) just typing
```
jshell --class-path ${PATH_TO_JAR}/json-values-X.Y.Z.jar
```

Before getting started, remember the great quote from **Venkat Subramaniam**:
_By nature we're wired to mistake familiar as simple and the unfamiliar as complex_.
#### 0- Basic concepts


#### 1- How to create a Json?
json-values uses static factory methods to create objects, just like the ones introduced by Java 9 to create small unmodifiable collections. 
There is a naming convention to emphasize what kind of object is created.
* **of** and **parse** methods return immutable jsons or values.
* \_of\_ and \_parse\_ return  mutable jsons. 

You may be asking what's the point of using underscores to name methods. The reason is that I like symbols to convey information quickly and concisely, but, as you you know, they are not allowed in java to name variables and methods.

##### 1.1- Creation of immutable json objects.
```
import jsonvalues.*;
// the empty immutable singleton, same instance is always returned 
JsObj empty = JsObj.empty();  
JsObj x = JsObj.of("a", JsInt.of(1), "b", JsBool.TRUE, "c", JsNull.NULL, "d", JsStr.of("hi"));
JsObj y = empty.put("a", 1)
               .put("b", true)
               .put("c", null)
               .put("d", "hi");
Assert.assertEquals(x, y);   
Assert.assertNotEquals(empty, y);  // empty is immutable and will never change

// from json pairs
JsObj w = JsObj.of( JsPair.of("a.b.0", JsInt.of(1) ),
                    JsPair.of("a.b.1", JsInt.of(2) )
                  );
// parsing a string                  
JsObj z = JsObj.parse("{\"a\": {\"b\": [1,2]}}"); 
Assert.assertEquals(w, z);   
 ```
##### 1.2- Creation of mutable json objects.
```
import jsonvalues.*;
JsObj _empty_ = JsObj._empty_();    
JsObj _x_ = JsObj._of_("a", JsInt.of(1), "b", JsBool.TRUE, "c", JsNull.NULL, "d", JsStr.of("hi"));
JsObj _y_ = _empty_.put("a",1)
                   .put("b",true)
                   .put("c",null)
                   .put("d","hi");
Assert.assertEquals(_x_, _y_);   
Assert.assertEquals(_empty_, _y_); // something that I called _empty_ it's not empty anymore!  
  
// from vargs of pairs
JsObj _w_ = JsObj._of_( JsPair.of("a.b.0", JsInt.of(1) ),
                        JsPair.of("a.b.1", JsInt.of(2) )
                      );
// parsing a string                  
JsObj _z_ = JsObj._parse_("{\"a\": {\"b\": [1,2]}}"); 
Assert.assertEquals(w, z);       
```

##### 1.3- Creation of immutable json arrays.
```
import jsonvalues.*;
JsArray a = JsArray.of(1,2,3);  // from varargs of int
JsArray b = JsArray.of("a","b","c"); // from varargs of string
JsArray c = JsArray.of(JsBool.TRUE, JsStr.of("a"), JsNull.NULL, JsDouble.of(1.5d)) // from varargs of JsElem
//from json pairs
JsArray d = JsArray.of(JsPair.of("0.a.b.0", JsStr.of("a"")),
                       JsPair.of("0.a.b.1", JsStr.of("b"")));
//from varargs of json pairs                        
JsArray e =  JsArray.parse("[{\"a\":{\"b\":[1,2]}}]");       

JsArray empty = JsArray.empty();

JsArray f = empty.append(JsInt.of(1))  
                 .append(JsInt.of(2))           
                 .prepend(JsInt.of(0));   
                 
Assert.assertNotEquals(empty, f); // empty is immutable and will never change      
```               
##### 1.4- Creation of mutable json arrays.
```
import jsonvalues.*;
JsArray _a_ = JsArray._of_(1,2,3);  // from varargs of int
JsArray _b_ = JsArray._of_("a","b","c"); // from varargs of string
JsArray _c_ = JsArray._of_(JsBool.TRUE, JsStr.of("a"), JsNull.NULL, JsDouble.of(1.5d)) // from varargs of JsElem
//from json pairs
JsArray d = JsArray._of_(JsPair.of("0.a.b.0", JsStr.of("a"")),
                         JsPair.of("0.a.b.1", JsStr.of("b"")));
//from varargs of json pairs                        
JsArray e =  JsArray._parse_("[{\"a\":{\"b\":[1,2]}}]");       

JsArray _empty_ = JsArray._empty_();

JsArray _f_ = _empty_.append(JsInt.of(1))  
                     .append(JsInt.of(2))           
                     .prepend(JsInt.of(0));   
                     
Assert.assertEquals(_empty_, _f_); //  _empty_ is not empty, is [0,1,2]   
```            
##### 1.5- Going from immutable to mutable back and forth.


   
##### 2- Streams and Collectors.
Stream methods returns sequences of JsPair, where a JsPair is just a pair consisting of an element and the path where it's located at:
JsPair = (JsPath, JsElem)
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
Function<JsPair,JsPair> times10ifInt = pair -> pair.elem.isInt()) ? pair.elem.asJsInt().map(i->i*10) : pair.elem ;
JsObj y = x.stream_().map(times10ifNumber).collector(JsObj.collector());
```
What if you want to get the stream back into a mutable json. Well, taking into account the convention pointed out in _1_, it only requires two underscores:
```
JsObj _y_ = x.stream_().map(times10ifNumber).collector(JsObj._collector_());
```

So, as it was expected JsObj.collector() returns an immutable object whereas JsObj.\_collector\_() returns an mutable one.

For arrays is just the same:
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
Function<JsPair,JsPair> toUpperCase = pair -> pair.elem.isStr() ? pair.elem.asJsStr().map(String::toUpperCase) : pair.elem;
JsArray y = x.stream_().map(toUpperCase).collector(JsArray.collector())     // stream into immutable array
JsArray _y_ = x.stream_().map(toUpperCase).collector(JsArray._collector_()) // stream into mutable array
```

By the way, the implemented collectors support parallel streams.

##### 3- Filter, map and reduce. 


##### 4- Union and intersection. 


##### 5- Getting data in and pulling data out. 


##### 6- Equality. 


##### 6- Tools. 


During the development, different compiler plug-ins to find bugs at _compile time_ have been used:
* [The Checker Framework Compiler](https://checkerframework.org), which found some NullPointerException.
* [Google error-prone](https://errorprone.info), which found a bug related to [BigDecimalEquals](https://errorprone.info/bugpattern/BigDecimalEquals)

Scala check


As a big fan of **Joshua Bloch** and _Effective Java_, I try to follow all his guidelines. The json-values
library takes special attention to the following items from the Third Edition of the book:

* Careful implementation of equals and hashcode (Item 10 and 11):
```
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
It doesn't matter that different primitive types and objects have been used to create them. That's a detail
of the Java language and  **json-values** is data-centric.                          

```
* Careful implementation of a custom serialized form: Item 87.
``


``
* Item 49: Every method parameter is checked for validity.
```
Every method parameter is null-checked, throwing an UsupportedOperationException when it's null. 
```
 *Item 56: Every exposed API element is documented. Javadoc or this document is enough to use 
* Use of static factory methods to be expressive instantiating objects, 
hide implementation classes and when possible returns always the same immutable instance: Item 1
* Prefer primitive to boxed primitives: Item 61. There are different method specializations to put data in and pull data out which use 
primitive types, like the following:
```
Json put(path,int)
Json put(path,double)
Json put(path,long)

OptionalInt getInt(path)          // instead of Optional<Integer>
OptionalDouble getDouble(path)    // instead of Optional<Double>
OptionalLong getLong(path)        // instead of Optional<Long>
```
*  Use of checked exceptions for recoverable conditions and runtime exceptions for programming errors: Item 70. The only checked exception in the API is MalformedJson,
which occurs when a not well-formed string is parsed into a Json. When there is a programming error, like getting the head of
an empty array, an UnsupportedOperationException is thrown, which means the programmer need to change
something, for example, adding a guard condition. Whenever a null is passed in as a method parameter, a NullPointerException is thrown.
* Some method names have been chosen consistently with well-known Java APIs, especially the Collections framework: Item 51
 * Every unchecked warning has been eliminated or explained the type-safety of the code by a @SuppressWarnings
 annotation. Item: 27
 * Item 15: Minimize the accessibility of classes and members. If a class or method is public it means it makes sense to be used or called. It's a must not to burden the user of the API with a lot of classes and methods which are only used internally and makes no sense to be exposed to the  client of the API. That's why it's a one-package library and everything is not public by default.
  


#### ACKNOWLEDGMENTS

 






