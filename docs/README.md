[![Build Status](https://travis-ci.org/imrafaelmerino/json-values.svg?branch=master)](https://travis-ci.org/imrafaelmerino/json-values)
[![CircleCI](https://circleci.com/gh/imrafaelmerino/json-values/tree/master.svg)](https://circleci.com/gh/imrafaelmerino/json-values/tree/master)
[![codecov](https://codecov.io/gh/imrafaelmerino/json-values/branch/master/graph/badge.svg)](https://codecov.io/gh/imrafaelmerino/json-values)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)

[![Javadocs](https://www.javadoc.io/badge/com.github.imrafaelmerino/json-values.svg)](https://www.javadoc.io/doc/com.github.imrafaelmerino/json-values/2.0.0)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/json-values/2.0.0)](https://search.maven.org/artifact/com.github.imrafaelmerino/json-values/2.0.0/jar)

[![Gitter](https://badges.gitter.im/json-values/community.svg)](https://gitter.im/json-values/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

- [Introduction](#introduction)
- [What to use _json-values_ for and when to use it](#whatfor)
- [When not to use it](#notwhatfor)
- [Requirements](#requirements)
- [Installation](#installation)
- [Documentation](https://imrafaelmerino.github.io/json-values/)
- [Want to help](#wth)
- [Develop](#develop)

## <a name="introduction"><a/> Introduction
Welcome to **json-values**, the first-ever Json library in _Java_ that uses _persistent data structures_ 
from _Scala_. _Java_ doesn't implement _persistent data structures_ natively; nevertheless, _Scala_ does and 
runs on the _JVM_; therefore, you can go from Java to Scala smoothly and without any impact on the performance. 

I'm a big fan of [Clojure](https://clojure.org) among other functional languages, and with due respect to the
apparent differences, **json-values** follows its philosophy: 

* **Immutability over mutability**. If you still have doubts about why, you should take 
a look at one of my favorite talks ever, [_The value of values_](https://www.youtube.com/watch?v=-6BsiVyC1kM) 
from **Rich Hickey**. **json-values** is _functional_ because you can take advantage of immutable data structures 
to represent a Json.
* **Data over abstraction**. The API is declarative and data-centric. You only need values (also known as immutable objects) and functions to 
manipulate them.
* _It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures_. —Alan Perlis. 

**json-values** has taken much inspiration from Scala as well, as a matter of fact, it's been implemented following both the object-oriented
and functional paradigms, as Scala promotes.
## <a name="whatfor"><a/> What to use json-values for and when to use it
* You need to deal with Jsons, and you want to program following a functional style, **using functions and immutable types (or values)**,
but you can't benefit from all the advantages that immutability brings to your code because **Java doesn't provide Persistent Data Structures**.
The thing is that Java 8 brought functions, lambdas, lazy evaluation to some extent, streams... but, without immutability, 
something is still missing, and as _**Pat Helland**_ said, [Immutability Changes Everything!](http://cidrdb.org/cidr2015/Papers/CIDR15_Paper16.pdf)
* You manipulate Jsons all the time, and you'd like to do it with less ceremony. **json-values** is declarative and takes advantages of all the new features that were introduced 
in Java 8, like functions, suppliers, streams, and collectors, making json manipulation simple, fast, and efficient. 
* You may be thinking, ok, great, I agree with all the described above, but, there are some scenarios where I still need a mutable Json as, after all, I'm programming in Java. 
With **json-values**, you can go from a mutable Json to an immutable one, back and forth, and the API to manipulate 
them is the same, being both implementations hidden to the user. 
* Simplicity matters, and I 'd argue that **json-values** is simple.
* As a developer, I'm convinced that code should win arguments, so let me enumerate some examples, where I
leave the functions passed in as arguments with no implementation for brevity reasons (go to the [project page](https://imrafaelmerino.github.io/json-values/) for further
details)
```
json.mapKeys(toSneakeCase)

obj.stream().parallel().map(toSneakCase).collect(JsObj.collector())

json.mapElems(trim, ifStr)

json.filterKeys(key.startsWith("_field"))

json.filterElems(isNotNull)

json.reduce(plus, ifInt)

//RFC 6901
json.putIfAbsent(path("/a/b"), ()-> getElem)

json.appendIfPresent(path("/c/d"), ()-> getElem)

json.prependAll(path("/a/b"), list)

a.union(b, JsArray.TYPE.SET)
a.union(b, JsArray.TYPE.LIST)
a.union(b, JsArray.TYPE.MULTISET)

a.intersection(b)

//RFC 6902
json.patch(Patch.ops().add("/a/b",
                           JsInt.of(1)
                          )
                      .remove("/c/0")
                      .toArray()
          )

```
I'd argue that it's very simple, expressive and concise. And that plus the fact that it's a persistent
data structure shows very well the essence of **json-values**.
## <a name="notwhatfor"><a/> When not to use it
**json-values** fits well in _pure_ OOP and incredibly well in FP, but NOT in _EOOP_, which stands for 
Enterprise Object-Oriented Programming. Don't create yet another fancy abstraction with getters and setters 
or a complex DSL over json-values. [Narcissistic Design](https://www.youtube.com/watch?v=LEZv-kQUSi4) from **Stuart Halloway** is a 
great talk that elaborates ironically on this point.
## <a name="requirements"><a/> Requirements
Java 8 or greater.
## <a name="installation"><a/> Installation
Add the following dependency to your building tool:
```
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>json-values</artifactId>
  <version>2.0.0</version>
</dependency>
```
and that's all. It's a **zero-dependency** library, so you won't have to go through a kind of dependency hell to get it working. 
You can play around with the library using the java _REPL_ (>= Java 9) just typing:
```
jshell --class-path ${PATH_TO_JAR}/json-values-X.Y.Z.jar
```
## <a ><a/> Documentation   
Go to https://imrafaelmerino.github.io/json-values/
## <a name="wth"><a/> Want to help
I've set up a separate document for [contributors](./CONTRIBUTING.md).
## <a name="develop"><a/> Develop
I've set up a separate document for [developers](./developers.md). Things like why json-values is a one-package library, if it was developed using TDD or anything related to the
development of the library can be found there. I'll be adding little by little more and more
information.    
