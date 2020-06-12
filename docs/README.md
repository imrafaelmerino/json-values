[![Build Status](https://travis-ci.org/imrafaelmerino/json-values.svg?branch=master)](https://travis-ci.org/imrafaelmerino/json-values)
[![codecov](https://codecov.io/gh/imrafaelmerino/json-values/branch/master/graph/badge.svg)](https://codecov.io/gh/imrafaelmerino/json-values)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)

[![Javadocs](https://www.javadoc.io/badge/com.github.imrafaelmerino/json-values.svg)](https://www.javadoc.io/doc/com.github.imrafaelmerino/json-values)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/json-values/8.0.0)](https://search.maven.org/artifact/com.github.imrafaelmerino/json-values/8.0.0/jar)
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

One of the most essential aspects of functional programming is immutable data structures, better known as values. Updating these structures using the copy-on-write approach is inefficient, and this is why persistent
data structures were created. On the other hand, JSON is a lightweight, text-based, language-independent data interchange format. It's become so popular due to its simplicity.

There are many libraries out there to work with JSON in the JVM ecosystem; however, none of them use persistent data structures. In most cases, those libraries parse a string or array of bytes into an object. The thing is, why do that? JSON is a magnificent structure.
It's simple, easy to aggregate, ease to create, easy to reason about, so why create yet another abstraction over JSON? Moreover, many architectures work with JSON end-to-end. Going from JSON to objects or strings back and forth is not
efficient, especially when copy-on-write is the only option to avoid mutation.

## <a name="whatfor"><a/> What to use json-values for and when to use it
* You need to deal with Jsons, and you want to program following a functional style, **using functions and immutable types (or values)**,
but you can't benefit from all the advantage that immutability brings to your code because **Java doesn't provide Persistent Data Structures**.
The thing is that Java 8 brought functions, lambdas, lazy evaluation to some extent, and streams, but without immutability,
something is still missing, and as _**Pat Helland**_ said, [Immutability Changes Everything!](http://cidrdb.org/cidr2015/Papers/CIDR15_Paper16.pdf)
* You manipulate Jsons all the time, and you'd like to do it with less ceremony. **json-values** is declarative and takes advantages of all the new features that were introduced
in Java 8, like functions, suppliers, streams, and collectors, making json manipulation simple, fast, and efficient.
* Simplicity matters, and I 'd argue that **json-values** is simple.
* As a developer, I'm convinced that code should win arguments, so let me enumerate some examples, where I
leave the functions passed in as arguments with no implementation for brevity reasons (go to the [project page](https://imrafaelmerino.github.io/json-values/) for further
details)

```
json.mapKeys(toSneakeCase)

json.mapValues(trim, ifStr)

json.filterKeys(key.startsWith("_field"))

json.filterValues(isNotNull)

json.reduce(plus, ifInt)

//RFC 6901
json.set(path("/a/b"), value)

a.union(b, JsArray.TYPE.SET)
a.union(b, JsArray.TYPE.LIST)
a.union(b, JsArray.TYPE.MULTISET)

a.intersection(b)
```
I'd argue that it's straightforward, expressive, and concise. And that plus the fact that it's a persistent
data structure shows very well the essence of **json-values**.

That was just a little taste! Data generation and validation are significant in software.
If you struggle to generate data, it slows down the testing.
On the other hand, corrupt data can propagate throughout your system and cause a nightmare. Errors that blow up in your face
are way better! If you think about it, the definition, validation, and generation of a JSON value could be
implemented using the same data structure; after all, the three are just bindings with different
elements: values, generators, or specifications. Let's check out an example:

```
// defining a json object

var person = JsObj.of("name", JsStr.of("Rafael"),
                      "age", JsInt.of(37),
                      "languages", JsArray.of("Haskell", "Scala", "Java", "Clojure")
                      "github", JsStr.of("imrafaelmerino"),
                      "profession", JsStr.of("frustrated consultant"),
                      "address", JsObj.of("city", JsStr.of("Madrid"),
                                          "location", JsArray.of(40.566, 87.987),
                                          "country",JsStr.of("ES").optional().nullable()
                                          )
                        );

// defining a json spec. strict means: keys different than the specified are not allowed

var spec = JsObjSpec.strict("name", JsSpecs.str,
                            "age", JsSpecs.integer(n-> n>15 && n<100),
                            "languages", JsSpecs.arrayOfStr,
                            "github", JsSpecs.str.optional(),
                            "profession", JsSpecs.str.nullable(),
                            "address", JsObjSpec.lenient("city", JsSpecs.str,
                                                         "location", JsSpecs.tuple(JsSpecs.decimal,
                                                                                   JsSpecs.decimal
                                                                                   ),
                                                         "country",JsSpecs.str.optional().nullable()
                                                         )
                           );

// defining a json generator

var gen = JsObjGen.of("name", JsGens.alphabetic,
                      "age",  JsGens.choose(18,100),
                      "languages", JsGens.arrayOf(JsGens.str,10),
                      "github", JsGens.alphanumeric.optional(),
                      "profession", JsGens.oneOf(professions).nullable(),
                      "address", JsObjGen.of("city", JsGens.oneOf(cities),
                                             "location", JsGens.tuple(JsGens.decimal,
                                                                      JsGens.decimal
                                                                      ),
                                             "country",JsGens.oneOf(countries).optional().nullable()
                                             )
                      );

// if the object doesn't conform the spec, the errors and their locations are returned in a set

Set<JsErrorPair> errors = spec.test(person);

// you can use a spec to parse a string! as soon as an error is found, the parsing ends.

byte[] jsonBytes = ...;
String jsonStr = ...;

var parser = new JsObjParser(spec);

var a = parser.parse(jsonBytes);
var b = parser.parse(jsonStr);

```

We can create futures as well following the same philosophy:

```
CompletableFuture<JsValue> nameFut, ageFut, languagesFut, handleFut, professionFut, streetFut, lonFut, latFut, countryFut;

var future = JsObjFuture.of("name", () -> nameFut,
                            "age", () -> ageFut,
                            "languages", () -> languagesFut,
                            "github", () -> handleFut,
                            "profession", () -> professionFut,
                            "address", JsObjFuture.of("street", streetFut,
                                                      "location", tuple(() -> latFut,
                                                                        () -> lonFut
                                                                       ),
                                                      "country", () -> countryFut
                                                      )
                            );

CompletableFuture<JsObj> completableFuture = future.get();

```

We can even create suppliers:

```
//given some suppliers

Supplier<JsValue> name, age, languages, handle, profession, street, lon, lat, country;

var supplier = JsObjSupplier.of("name", name,
                                "age", age,
                                "languages", languages,
                                "github", handle,
                                "profession", profession,
                                "address", JsObjFuture.of("street", street,
                                                          "location", tuple(lat,
                                                                            lon
                                                                           ),
                                                          "country", country
                                                          )
                                );

JsObj obj = supplier.get();

```


We can use optics to put data in and get data out in a composable and concise way.Lenses, Optionals, and Prism have been
defined for every json type.

```
Lens<JsObj,String> nameLens = JsObj.lens.str("name");
Lens<JsObj,Integer> ageLens = JsObj.lens.intNum("age");
Lens<JsObj,JsArray>   languagesLens = JsObj.lens.array("languages");
Option<JsObj, String> githubOpt     = JsObj.optional.str("name");
Lens<JsObj,String>    cityLens      = JsObj.lens.str(path("/address/city"));
Lens<JsObj,Double> latitudeLens = JsObj.lens.doubleNum(path("/address/location/0"));
Lens<JsObj,Double> longitudeLens = JsObj.lens.doubleNum(path("/address/location/1"));
Lens<JsObj,JsValue> countryLens = JsObj.lens.value(path("/address/country"));

Function<Integer,Function<JsObj,JsObj>> incAge =
                    i -> ageLens.modify.apply(n -> n+i);
Function<String,Function<JsObj,JsObj>> addLanguage =
                    lan -> languagesLens.modify.apply(a -> a.append(JsStr.of(lan)));
Function<Function<Double,Double>,Function<JsObj,JsObj>> modifyLatitude = latitudeLens.modify::apply;
Function<Function<Double,Double>,Function<JsObj,JsObj>> modifyLongitude = longitudeLens.modify::apply;
Function<String,Function<JsObj,JsObj>> setCountry = c -> countryLens.set.apply(JsStr.of(c));
Function<String,Function<JsObj,JsObj>> setName = nameLens.set::apply;

Function<JsObj,JsObj> fn = setName.apply("Philip").andThen(incAge.apply(1))
                                  .andThen(addLanguage.apply("Lisp"))
                                  .andThen(setCountry.apply("ES"))
                                  .andThen(modifyLatitude.apply(l -> l + 0.5))
                                  .andThen(modifyLongitude.apply(l -> l + 0.8));

var newPerson =  fn.apply(person);

```

This is just a quick intro, but I'd like to highlight that generators, specs and optics are really powerful and composable.
Furthermore, any spec can be defined in terms of predicates, which allows you to define any imaginable validation.

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
  <version>8.0.0</version>
</dependency>
```

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

json-values uses the persistent data structures from [vavr](https://www.vavr.io/), [Jackson](https://github.com/FasterXML/jackson) to parse a string/bytes into
a stream of tokens and [dsl-sjon](https://github.com/ngs-doo/dsl-json) to parse a string/bytes given a spec.

I've written about json-values on my blog:
* [The value of json values - Recursive data structures](http://blog.imrafaelmerino.dev/2020/06/the-value-of-json-values-recursive-data.html)

