<img src="./logo/package_twitter_if9bsyj4/color1/full/coverphoto/color1-white_logo_dark_background.png" alt="logo"/>

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/json-values/10.0.0)](https://search.maven.org/artifact/com.github.imrafaelmerino/json-values/10.0.0/jar)
[![codecov](https://codecov.io/gh/imrafaelmerino/json-values/branch/master/graph/badge.svg)](https://codecov.io/gh/imrafaelmerino/json-values)


*“Simplicity is a great virtue but it requires hard work to achieve it and education to appreciate it.
And to make matters worse: complexity sells better.”*
Edsger Wybe Dijkstra

- [Introduction](#introduction)
- [What to use _json-values_ for and when to use it](#whatfor)
- [When not to use it](#notwhatfor)
- [Requirements](#requirements)
- [Installation](#installation)
- [Related projects](#rp)
- [Release process](#release)



## <a name="introduction"><a/> Introduction

Welcome to **json-values**, the first-ever Json library in _Java_ implemented with 
persistent data structures.

One of the most essential aspects of functional programming is immutable data structures, 
better known in FP jargon as values.
It's a fact that, when possible, working with values leads to code with fewer bugs, is more 
readable, and is easier to maintain. Item 17 of Effective Java states that we must minimize 
mutability. Still, sometimes it's at the cost of losing performance because the copy-on-write 
approach is very inefficient for significant data structures. Here is where persistent data 
structures come into play.

Most functional languages, like Haskell, Clojure, and Scala, implement persistent data 
structures natively. Java doesn't. The best alternative I've found in the JVM ecosystem 
is the persistent collections provided by the library vavr. It provides a well-designed 
API and has a good performance.

The standard Java programmer finds it strange to work without objects and all the machinery 
of frameworks and annotations. FP is all about functions and values; that's it. I will try 
to cast some light on how we can manipulate JSON with json-values following a purely 
functional approach. First things first, let's create a JSON object:

## <a name="whatfor"><a/> What to use json-values for and when to use it

* You need to deal with Jsons, and you want to program following a functional style, **using just functions and values**,
  but you can't benefit from all the advantage that immutability brings to your code because **Java doesn't provide
  Persistent Data Structures**.
  The thing is that Java 8 brought functions, lambdas, lazy evaluation to some extent, and streams, but without
  immutability,
  something is still missing, and as _**Pat Helland**_
  said, [Immutability Changes Everything!](http://cidrdb.org/cidr2015/Papers/CIDR15_Paper16.pdf)
* You manipulate Jsons all the time, and you'd like to do it with less ceremony. **json-values** is declarative and
  takes advantage of a lot of concepts from functional programming to define a powerful API.
* Generating Json to do Property-Based-Testing is child's play with json-values.
* Generation specifications to validate Json and parse strings or bytes is a piece of cake.
* Simplicity matters, and I'd argue that **json-values** is simple.
* As a developer, I'm convinced that code should win arguments, so let me enumerate some examples.

First things first. Let's define a Json

```json
{
    "name": "Rafael",
    "surname": "Merino",
    "phoneNumber": 6666666,
    "registrationDate": "2019-01-21T05:47:26.853Z",
    "addresses": [
         {
            "coordinates": [39.8581, -4.02263],
            "city": "Toledo",
            "zipCode": "45920", 
            "tags": ["homeAddress"] ,
             
        },
        {
            "coordinates": [39.8581, -4.02263],
            "city": "Madrid",
            "zipCode": "28029",
            "tags": ["workAddress"]
        }
    ]
}

```

and coding it using the factory methods provided by json-values

```java      
import jsonvalues.*;  


JsObj person = 
    JsObj.of("name", JsStr.of("Rafael"),
             "surname", JsStr.of("Merino"),
             "phoneNumber", JsStr.of("6666666"),
             "registrationDate", JsInstant.of(Instant.parse("2019-01-21T05:47:26.853Z")),
             "addresses", JsArray.of(JsObj.of("coordinates", JsArray.of(39.8581, -4.02263),
                                              "city", JsStr.of("Toledo"),
                                              "zipCode", JsStr.of("45920"),
                                              "tags", JsArray.of("workAddress")
                                             ),
                                     JsObj.of("coordinates", JsArray.of(39.8581, -4.02263),
                                              "city", JsStr.of("Madrid"),
                                              "zipCode", JsStr.of("28029"),
                                              "tags", JsArray.of("homeAddress", "amazon")
                                             )
                                    )
            );

```

As you can see, its definition is like raw JSON. It’s a recursive data structure. 
You can nest as many JSON objects as you want. Think of any imaginable JSON, and 
you can write it in no time.

But what about validating JSON? We can define the JSON schema following precisely 
the same approach:


```java   
import jsonvalues.spec.JsObjSpec;
import static jsonvalues.spec.JsSpecs.*;

JsObjSpec personSpec =
    JsObjSpec.strict("name", str(),
                     "surname", str(),
                     "phoneNumber", str(),
                     "registrationDate", instant(),
                     "addresses", arrayOfObjSpec(JsObjSpec.strict("coordinates", 
                                                                  tuple(decimal(),
                                                                        decimal()
                                                                        ),
                                                                  "city", str(),
                                                                  "tags", arrayOfStr(),
                                                                  "zipCode", str()
                                                                  )
                                                )
                    );

```

I’d argue that it is very expressive, concise, and straightforward. I call it json-spec. 
I named it after a Clojure library named spec. Writing specs feels like writing JSON. 
Strict specs don't allow keys that are not specified, whereas lenient ones do. The real 
power is that you can create specs from predicates and compose them:

```java    

BiFunction<Integer, Integer, Predicate<String>> lengthBetween =
       (min, max) -> string -> string.length() <= max && 
                               string.length() >= min;


BiFunction<Instant, Instant, Predicate<Instant>> instantBetween =
      (min, max) -> instant -> min.isBefore(instant) && 
                               max.isAfter(instant);

BiFunction<Long, Long, Predicate<BigDecimal>> decBetween =
      (min, max) -> n -> BigDecimal.valueOf(min).compareTo(n) < 0 && 
                         BigDecimal.valueOf(max).compareTo(n) > 0;
  
```

```java    

int MAX_NAME_LENGTH = 10;
int MAX_SURNAME_LENGTH = 10;
int MAX_PHONE_LENGTH = 10;
int MAX_CITY_LENGTH = 20;
int MAX_TAG_LENGTH = 20;
int MAX_ZIPCODE_LENGTH = 30;
int LAT_MIN = -90;
int LAT_MAX = 90;
int LON_MIN = -180;
int LON_MAX = 180;
int MIN_ADDRESSES_SIZE = 1;
int MAX_ADDRESSES_SIZE = 100;

        
Predicate<String> nameSpec = lengthBetween.apply(0, MAX_NAME_LENGTH);
       
Predicate<String> surnameSpec = lengthBetween.apply(0, MAX_SURNAME_LENGTH);
        
Predicate<String> phoneSpec = lengthBetween.apply(0, MAX_PHONE_LENGTH);
        
Predicate<Instant> registrationDateSpec = instantBetween.apply(Instant.EPOCH, Instant.MAX);

Predicate<BigDecimal> latitudeSpec = decBetween.apply(LAT_MIN, LAT_MAX);

Predicate<BigDecimal> longitudeSpec = decBetween.apply(LON_MIN, LON_MAX);

Predicate<String> citySpec = lengthBetween.apply(0, MAX_CITY_LENGTH);
        
Predicate<String> tagSpec = lengthBetween.apply(0, MAX_TAG_LENGTH);
       
Predicate<String> zipCodeSpec = lengthBetween.apply(0, MAX_ZIPCODE_LENGTH);
        

```

```java    

JsObjSpec personSpec =
    JsObjSpec.strict("name", str(nameSpec),
                     "surname", str(surnameSpec),
                     "phoneNumber", str(phoneSpec).nullable(),
                     "registrationDate", instant(registrationDateSpec),
                     "addresses", arrayOfObjSpec(JsObjSpec.lenient("coordinates",
                                                                   tuple(decimal(latitudeSpec),
                                                                         decimal(longitudeSpec)
                                                                         ),
                                                                   "city", str(citySpec),
                                                                   "tags", arrayOfStr(tagSpec),
                                                                   "zipCode", str(zipCodeSpec)
                                                                  )
                                                          .setOptionals("tags", "zipCode", "city"),
                                                 MIN_ADDRESSES_SIZE,
                                                 MAX_ADDRESSES_SIZE                
                                                 )
                    )
             .setOptionals("surname", "phoneNumber", "addresses");      

```

As you can see, the spec's structure remains the same, and it’s child’s play to define 
optional and nullable fields.

Another exciting thing we can do with specs is parsing strings or bytes. Instead of parsing 
the whole JSON and then validating it, we can verify the JSON schema while parsing it and 
stop the process as soon as an error happens. After all, failing fast is important as well!

```java      
JsObjParser personParser = new JsObjParser(personSpec);

String string = "...";


JsObj person = personParser.parse(string);

```

Another critical aspect of software development is data generation. It’s an essential aspect 
of property-based testing, a technique for the random testing of program properties very well 
known in FP. Computers are way better than humans at generating random data. You'll catch more
bugs testing your code against a lot of inputs instead of just one. Writing generators, like
specs, is as simple as writing JSON:

```java      

JsObjGen personGen =
    JsObjGen.of("name", JsStrGen.biased(0, MAX_NAME_LENGTH),
                "surname", JsStrGen.biased(0, MAX_NAME_LENGTH),
                "phoneNumber", JsStrGen.biased(0,MAX_PHONE_LENGTH),
                "registrationDate", JsInstantGen.biased(0, Instant.MAX.getEpochSecond()),
                "addresses", JsArrayGen.biased(0,1)
                                       .apply(JsObjGen.of("coordinates", JsTupleGen.of(JsBigDecGen.biased(LAT_MIN, LAT_MAX),
                                                                                       JsBigDecGen.biased(LON_MIN, LON_MAX)
                                                                                      ),
                                                          "city", JsStrGen.biased(0, MAX_CITY_LENGTH),
                                                          "tags", JsArrayGen.biased(0,MAX_ADDRESSES_SIZE)
                                                                            .apply(JsStrGen.biased(0, MAX_TAG_LENGTH)),
                                                          "zipCode", JsStrGen.biased(0, MAX_ZIPCODE_LENGTH)
                                                          )
                                                       .setOptionals("tags", "zipCode", "city"),
                                              MIN_ADDRESSES_SIZE                       
                                              MAX_ADDRESSES_SIZE         
                                              )
                )
            .setOptionals("surname", "phoneNumber", "addresses");

```

Most generators have two static factory methods: _biased_ and _arbitrary_. The former returns
a uniform distribution, whereas the latest generates with a high probability potential 
problematic values that tend to cause bugs in our code. Find below some distributions:

```java    





``` 



```java 
//FILTERING
todo


JsObj json = JsObj.parse(string)

// first level         
json.mapKeys(toSneakeCase)

// traverses first level and  nested json       
json.mapAllKeys(toSneakeCase)

json.mapAllValues(trim, ifStr)

json.filterAllKeys(key.startsWith("_field"))

json.filterAllValues(isNotNull)

json.reduceAll(plus, ifInt)

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
If you think about it, the definition, validation, and generation of a JSON could be
implemented using the same data structure; after all, the three of them are just bindings with different
elements: values, generators, or specifications. Let's check out some examples.

Defining a json object:

```java
var person=JsObj.of("name",JsStr.of("Rafael"),
        "age",JsInt.of(37),
        "languages",JsArray.of("Haskell","Scala","Java","Clojure")
        "github",JsStr.of("imrafaelmerino"),
        "profession",JsStr.of("frustrated consultant"),
        "address",JsObj.of("city",JsStr.of("Madrid"),
        "location",JsArray.of(40.566,87.987),
        "country",JsStr.of("ES")
        )
        );

```

Defining a json spec; strict means: keys different from the specified are not allowed:

```java
import static jsonvalues.spec.JsSpecs;

var addressSpec=JsObjSpec.lenient("city",str,
        "country",str.optional().nullable()
        "location",tuple(decimal,
        decimal
        )
        );

        var spec=JsObjSpec.strict("name",str,
        "age",integer(n->n>15&&n<100),
        "languages",arrayOfStr,
        "github",str.optional(),
        "profession",str.nullable(),
        "address",addressSpec
        );

// if the object doesn't conform the spec, the errors and their locations are returned in a set

        Set<JsErrorPair> errors=spec.test(person);

```

We can use a spec to parse a string! As soon as an error is found, the parsing ends.

```java

byte[]jsonBytes=...;
        String jsonStr=...;

        var parser=new JsObjParser(spec);

        var a=parser.parse(jsonBytes);
        var b=parser.parse(jsonStr);

```

It supports the standard Json types: string, number, null, object, array; There are five number specializations:
int, long, double, decimal and biginteger. json-values adds support for instants and binary data. Instants
are serialized into its string representation according to ISO-8601; and the binary type is serialized into a
string encoded in base 64.

## <a name="notwhatfor"><a/> When not to use it

**json-values** fits well in _pure_ OOP and incredibly well in FP, but NOT in _EOOP_, which stands for
Enterprise Object-Oriented Programming. Don't create yet another fancy abstraction with getters and setters
or a complex DSL over json-values. [Narcissistic Design](https://www.youtube.com/watch?v=LEZv-kQUSi4) from **Stuart
Halloway** is a
great talk that elaborates ironically on this point.

## <a name="requirements"><a/> Requirements

Java 8 or greater.

## <a name="installation"><a/> Installation

Add the following dependency to your building tool:

```xml

<dependency>
    <groupId>com.github.imrafaelmerino</groupId>
    <artifactId>json-values</artifactId>
    <version>10.0.0</version>
</dependency>
```

## <a name="rp"><a/> Related projects

“Ideas are like rabbits. You get a couple and learn how to handle them, and pretty soon you have a dozen.” – John
Steinbeck

After the development of json-values, I published two more related projects:

* The Scala version: [json-scala-values](https://github.com/imrafaelmerino/json-scala-values)
* [vertx-effect](https://github.com/imrafaelmerino/vertx-effect)
* [mongo-values](https://github.com/imrafaelmerino/mongo-values) Set of codecs to use json-values with MongoDB
* [JIO](https://github.com/imrafaelmerino/JIO) Using JIO you can define Json generators to do Property Based Testing:

```java
import static jio.gen.json.JsObjGen;

var addressGen=JsObjGen.of("city",oneOf(cities),
        "country",oneOf(countries).nullable(),
        "location",tuple(decimal,
        decimal
        )
        );
        var gen=JsObjGen.of("name",alphabetic,
        "age",arbitrary(18,100),
        "languages",arrayOf(str,10),
        "github",alphanumeric,
        "profession",oneOf(professions).nullable(),
        "address",addressGen
        );
```

json-values uses the persistent data structures from [vavr](https://www.vavr.io/)
, [Jackson](https://github.com/FasterXML/jackson) to parse a string/bytes into
a stream of tokens and [dsl-sjon](https://github.com/ngs-doo/dsl-json) to parse a string/bytes given a spec.

## <a name="release"><a/> Release process

Every time a tagged commit is pushed into master, a Travis CI build will be triggered automatically and start the
release process,
deploying to Maven repositories and GitHub Releases. See the Travis conf file .travis.yml for
further details. On the other hand, the master branch is read-only, and all the commits should be pushed to
master through pull requests. 
