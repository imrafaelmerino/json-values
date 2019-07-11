Welcome to **json-values**, the first-ever Json library in _Java_ that uses _persistent data structures_ from _Scala_.
_Java_ doesn't implement _persistent data structures_ natively, nevertheless, Scala does and runs on the _JVM_, so you can go from Java to Scala easily and 
without any impact on the performance. 

I'm a big fan of [Clojure](https://clojure.org) among other functional languages, and with due respect to the obvious differences,
**json-values** follows its philosophy: 

* **immutability over mutability**. If you still have doubts about why, you should take 
a look at one of my favorite talks ever, [_The value of values_](https://www.youtube.com/watch?v=-6BsiVyC1kM) from **Rich Hickey**. 
So **json-values** is _functional_ because you can take advantage of immutable data structures to represent a Json.

* Data over abstraction. The API is really declarative and data-centric,
which makes it really simple to use. No fancy abstractions, long enterprise names, setters or complex DSLs, just values
and functions to manipulate them in a _declarative_ way. [Narcissistic Design](https://www.youtube.com/watch?v=LEZv-kQUSi4) from **Stuart Halloway** is a great talk that elaborates on this point.

* _It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures._ —**Alan Perlis**. It's
a one-package library with two main classes: JsObj and JsArray, that's all you need. 
I'd argue that it makes **json-values** a really simple library and simplicity matters! 

It's a zero-dependency library. You won't have to go through a kind of dependency hell to get it
working. It makes also really easy to interact with the library through the JSHELL, the Java REPL released in Java9.

During the development, different compiler plug-ins to find bugs at _compile time_ have been used:
* [The Checker Framework Compiler](https://checkerframework.org), which found some NullPointerException.
* [Google error-prone](https://errorprone.info), which found a bug related to [BigDecimalEquals](https://errorprone.info/bugpattern/BigDecimalEquals)

Json-values, naturally, uses recursion all the time. To not blow up the stack, tail-recursive method calls are turned into iterative loops 
by Trampolines. A well-known implementation of a Trampoline is exposed by the API in case you wanna do
some _head and tail_ programming and you should! because, first, it's fun, and second and more important, it makes the code more declarative, concise and easy to reason about. 
My experience in Java says that the more difficult the task is, the more benefit you'll get using this approach. Sometimes a
simple loop is enough and more clear.

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
  




 






