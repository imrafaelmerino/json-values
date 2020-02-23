[![Build Status](https://travis-ci.org/imrafaelmerino/json-values.svg?branch=master)](https://travis-ci.org/imrafaelmerino/json-values)
[![CircleCI](https://circleci.com/gh/imrafaelmerino/json-values/tree/master.svg)](https://circleci.com/gh/imrafaelmerino/json-values/tree/master)
[![codecov](https://codecov.io/gh/imrafaelmerino/json-values/branch/master/graph/badge.svg)](https://codecov.io/gh/imrafaelmerino/json-values)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)

[![Javadocs](https://www.javadoc.io/badge/com.github.imrafaelmerino/json-values.svg)](https://www.javadoc.io/doc/com.github.imrafaelmerino/json-values)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/json-values/4.1.0)](https://search.maven.org/artifact/com.github.imrafaelmerino/json-values/4.1.0/jar)
[![](https://jitpack.io/v/imrafaelmerino/json-values.svg)](https://jitpack.io/#imrafaelmerino/json-values)
[![Gitter](https://badges.gitter.im/json-values/community.svg)](https://gitter.im/json-values/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

- [Introduction](#introduction)
- [What to use _json-values_ for and when to use it](#whatfor)
- [When not to use it](#notwhatfor)
- [Requirements](#requirements)
- [Installation](#installation)
- [Documentation](https://imrafaelmerino.github.io/json-values/)
- [Want to help](#wth)
- [Develop](#develop)
- [Related projects](#rp)

## <a name="introduction"><a/> Introduction
Welcome to **json-values**, the first-ever Json library in _Java_ implemented with persistent data structures. 

## <a name="whatfor"><a/> What to use json-values for and when to use it
* You need to deal with Jsons, and you want to program following a functional style, **using functions and immutable types (or values)**,
but you can't benefit from all the advantages that immutability brings to your code because **Java doesn't provide Persistent Data Structures**.
The thing is that Java 8 brought functions, lambdas, lazy evaluation to some extent, streams... but, without immutability, 
something is still missing, and as _**Pat Helland**_ said, [Immutability Changes Everything!](http://cidrdb.org/cidr2015/Papers/CIDR15_Paper16.pdf)
* You manipulate Jsons all the time, and you'd like to do it with less ceremony. **json-values** is declarative and takes advantages of all the new features that were introduced 
in Java 8, like functions, suppliers, streams, and collectors, making json manipulation simple, fast, and efficient. 
* Simplicity matters, and I 'd argue that **json-values** is simple.
* As a developer, I'm convinced that code should win arguments, so let me enumerate some examples, where I
leave the functions passed in as arguments with no implementation for brevity reasons (go to the [project page](https://imrafaelmerino.github.io/json-values/) for further
details)
```
json.mapKeys(toSneakeCase)

obj.stream().parallel().map(toSneakCase).collect(Jsons.mutable.object.collector())

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

// creation of Jsons from primitive types

JsObj.of("a",JsInt.of(13),
                          "b",JsStr.of("hi!")
                         )

Jsons.mutable.array.of(1,2,3)

// creation of Jsons parsing strings

JsObj.parse("{...}")

Jsons.mutable.array.parse("[...]")
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
  <version>4.1.0</version>
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
## <a name="rp"><a/> Related projects
“Ideas are like rabbits. You get a couple and learn how to handle them, and pretty soon you have a dozen.” – John Steinbeck

After the development of json-values, I published two more related projects:
* The Scala version: [json-scala-values](https://github.com/imrafaelmerino/json-scala-values)
* Json generators in Scala to do property-based testing: [json-scala-values-generator](https://github.com/imrafaelmerino/json-scala-values-generator) . This is a 
project I'm especially proud of. I think there is no Json generator more declarative, concise, and why not, beautiful in the
whole wide world! If I'm wrong, please let me know!


