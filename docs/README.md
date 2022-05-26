<img src="./logo/package_twitter_if9bsyj4/color1/full/coverphoto/color1-white_logo_dark_background.png" alt="logo"/>

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_json-values&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_json-values)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/json-values/11.1.0)](https://search.maven.org/artifact/com.github.imrafaelmerino/json-values/11.1.0/jar)
[![codecov](https://codecov.io/gh/imrafaelmerino/json-values/branch/master/graph/badge.svg)](https://codecov.io/gh/imrafaelmerino/json-values)


“_Simplicity is a great virtue but it requires hard work to achieve it and education to appreciate it.
And to make matters worse: complexity sells better._”
**Edsger Wybe Dijkstra**


- [Introduction](#introduction)
- [What to use _json-values_ for and when to use it](#whatfor)
- [When not to use it](#notwhatfor)
- [How-To](#how-to)
    - [JsPath](#jspath)
    - [JsValue](#jsvalue)
    - [Creating Jsons](#creatingjson)
      - [Creating JsObj](#creatingjsonobj)
      - [Creating JsArray](#creatingjsonarray)
    - [Putting data in and getting data out](#inout)
    - [Filter, map and reduce](#filtermapreduce)  
    - [Putting data in and getting data out](#inout)
    - [Specs](#specs)
    - [Generators](#gen)
    - [Optics](#optics)
      - [Lenses](#lenses)
      - [Prism](#prism)
      - [Optionals](#opt)
- [Requirements](#requirements)
- [Installation](#installation)
- [Related projects](#rp)

## <a name="introduction"><a/> Introduction

Welcome to **json-values**, the first-ever JSON library in _Java_ implemented with
persistent data structures.

One of the most essential aspects of functional programming is immutable data structures,
better known in FP jargon as values.
It's a fact that, when possible, working with values leads to code with fewer bugs, is more
readable, and is easier to maintain. Item 17 of Effective Java states that we must minimize
mutability. Still, sometimes it's at the cost of losing performance because the 
[copy-on-write](https://en.wikipedia.org/wiki/Copy-on-write)
approach is very inefficient for significant data structures. Here is where persistent data
structures come into play.

Most functional languages, like Haskell, Clojure, and Scala, implement persistent data
structures natively. Java doesn't. 

The standard Java programmer finds it strange to work without objects and all the machinery
of frameworks and annotations. FP is all about functions and values; that's it. I will try
to cast some light on how we can manipulate JSON with json-values following a purely
functional approach.


## <a name="whatfor"><a/> What to use json-values for and when to use it

* You need to deal with Jsons, and you want to program following a functional style, **using just functions and values**,
  but you can't benefit from all the advantage that immutability brings to your code because **Java doesn't provide
  [Persistent Data Structures](https://en.wikipedia.org/wiki/Persistent_data_structure)**.
* For those architectures that work with JSON end-to-end, it's extremely safe and efficient to have a persistent Json. 
* Think of actors sending JSON messages one to each other for example.
* You manipulate JSON all the time, and you'd like to do it with less ceremony. **json-values** is declarative and
  takes advantage of a lot of concepts from functional programming to define a powerful API.
* Generating JSON to do Property-Based-Testing is child's play with json-values.
* Generating specifications to validate JSON and parse strings or bytes very efficiently is a piece of cake.
* Simplicity matters, and I'd argue that **json-values** is simple.
* As _**Pat Helland**_ said, [Immutability Changes Everything!](http://cidrdb.org/cidr2015/Papers/CIDR15_Paper16.pdf)

## <a name="how-to"><a/> How-To
As a developer, I'm convinced that code should win arguments, so let's get down to business and
do some coding. 

#### <a name="jspath"><a/>JsPath

A _JsPath_ represents a location of a specific value within a JSON. It's a sequence of _Position_, being a position
either a _Key_ or an _Index_.

```java   

//RFC 6901

JsPath path =  JsPath.path("/a/b/0");

Position head = path.head();

Assertions.assertEquals(head,
                        Key.of("a")
                        );

JsPath tail = path.tail();

Assertions.assertEquals(tail.head(),
                        Key.of("b")
                       );

Assertions.assertEquals(tail.last(),
                        Index.of(0)
                       );
                       
//alternative to RFC 6901 to create JsPath

JsPath path =  JsPath.fromKey("a").key("b").index(0);

                      
```


#### <a name="jsvalue"><a/>JsValue

Every element in a Json is a _JsValue_. There is a specific type for each value described
in [json.org](https://www.json.org): string, number, null, object and array.
There are five number specializations: int, long, double, decimal and biginteger. 

json-values adds support for instants and binary data. Instants are serialized into its 
string representation according to ISO-8601; and the binary type is serialized into a 
string encoded in base 64.

When it comes to the _equals_ method, json-values is data oriented, I mean, two JSON
are equals if they represent the same piece of information. Let's put some example:

```java  
JsObj json = JsObj.of("a", JsInt.of(1000),
                      "b", JsBigDec.of(BigDecimal.valueOf(100_000_000_000_000L)),
                      "c", JsInstant.of("2022-05-25T14:27:37.353Z"),
                      "d", JsStr.of("aGkh")
                     );

JsObj json1 = JsObj.of("b", JsBigInt.of(BigInteger.valueOf(100_000_000_000_000L)),
                       "a", JsLong.of(1000L),
                       "c", JsStr.of("2022-05-25T14:27:37.353Z"),
                       "d", JsBinary.of("hi!".getBytes(StandardCharsets.UTF_8))
                      );  

Assertions.assertEquals(json, json1);    
Assertions.assertEquals(json.hashcode(), json1.hashcode());                        
                    
```

Since both JSON represents the same information:

```json   

{
  "a": 1000,
  "b": 100000000000000,
  "c": "2022-05-25T14:27:37.353Z",
  "d": "aGkh"
}


```

it makes sense that both of them are equals, and therefore they have the same hashcode.

#### <a name="creatingjson"><a/>Creating JSON

There are several ways of creating JSON:
* Using the static factory methods _of_.
* Parsing an array of bytes, a string or an input stream. When possible, it's always better to work on byte level. If the schema of the Json is known, the fastest way is defining a parser from a spec.
* Creating an empty object and then using the API to insert values.

##### <a name="creatingjsonobj"><a/>Creating JsObj

Let's create the following JSON

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
            "tags": ["homeAddress"]
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

**Using the static factory methods provided by json-values:**

```java      
import jsonvalues.*;
import java.time.Instant;

JsObj person = 
    JsObj.of("name", JsStr.of("Rafael"),
             "surname", JsStr.of("Merino"),
             "phoneNumber", JsStr.of("6666666"),
             "registrationDate", JsInstant.of("2019-01-21T05:47:26.853Z"),
             "addresses", JsArray.of(JsObj.of("coordinates", JsArray.of(39.8581, -4.02263),
                                              "city", JsStr.of("Toledo"),
                                              "zipCode", JsStr.of("45920"),
                                              "tags", JsArray.of("workAddress")
                                             ),
                                     JsObj.of("coordinates", JsArray.of(40.4168, 3.7038),
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

You can use paths instead of keys and a nested structure, which turns out to be
really convenient as well:

```java   
import static jsonvalues.JsPath.path;
     
JsObj person = 
        JsObj.of(path("/name"), JsStr.of("Rafael"),
                 path("/surname"), JsStr.of("Merino"),
                 path("/phoneNumber"), JsStr.of("6666666"),
                 path("/registrationDate"), JsInstant.of("2019-01-21T05:47:26.853Z"),
                 path("/addresses/0/coordinates/0"), JsDouble.of(39.8581),
                 path("/addresses/0/coordinates/1"), JsDouble.of(-4.02263),
                 path("/addresses/0/city"), JsStr.of("Toledo"),
                 path("/addresses/0/tags"), JsArray.of("workAddress"),
                 path("/addresses/1/coordinates/0"),  JsDouble.of(40.4168),
                 path("/addresses/1/coordinates/1"), JsDouble.of(3.7038),
                 path("/addresses/1/city"), JsStr.of("Madrid"),
                 path("/addresses/1/tags"), JsArray.of("homeAddress")
                );     
```


**Parsing a string and the schema of the Json is unknown:**

```java   

JsObj a = JsObj.parse("{...}");

JsObj b = JsObj.parseYaml("{....}");

```

**Parsing a string and the schema of the object is known:**

In this case the best and fastest option is to use a spec to do the parsing. 
We'll talk about this option later.

**With the set method:**

Remember that a JSON is immutable, so the set method returns a brand new JSON.

```java   

JsObj person = 
        JsObj.empty().set("name", JsStr.of("Rafael"))
                     .set("surname", JsStr.of("Merino"))
                     .set("phoneNumber", JsStr.of("6666666"));

```


##### <a name="creatingjsonarray"><a/>Creating JsArray

**From primitives using the static factory method _of_ and varargs:**

```java   

JsArray a = JsArray.of("apple", "orange", "pear");

JsArray b = JsArray.of(1, 2, 3, 4);

```

**From JSON values using the static factory method _of_ and varargs:**

```java   

JsArray a = JsArray.of(JsStr.of("hi"), JsInt.of(1), JsBool.TRUE, JsNull.NULL);

```

**From an iterable of JsValue:**

```java    

List<JsValue> list = new ArrayList();
Set<JsValue> set = new HashSet();

JsArray.ofIterable(list);
JsArray.ofIterable(set);

  
```

**Parsing a string or array of bytes, and the schema of the Json is unknown:**

```java   

JsArray a = JsArray.parse("[...]");

JsArray b = JsArray.parseYaml("[....]");

```

**Parsing a string and the schema of the array is known:**

In this case, like parsing objects with a schema, the best and fastest option 
is to use a spec to do the parsing. We'll also talk about this option later.

**Creating and empty arran and adding new elements eith the methods _append_ and _prepend_:**

```java   

JsArray a = JsArray.empty().append(JsInt.of(1))
                           .prepend(JsInt.of(0));

JsArray b = JsArray.empty().append(JsInt.of(3))
                           .prepend(JsInt.of(2));

Assertions.equals(JsArray.of(0,1,2,3), a.appendAll(b));
Assertions.equals(JsArray.of(2,3,0,1), a.prependAll(b));

```

#### <a name="inout"><a/>Putting data in and getting data out
There are one function to put data in a JSON specifying a path and a value:

```java   

JsObj set(JsPath path, JsValue value, JsValue padWith);
JsObj set(JsPath path, JsValue value);

```

**The _set_ function always inserts the value at the specified path, creating
any needed container and padding arrays when necessary.**

TODO


#### <a name="filtermapreduce"><a/>Filter, map and reduce

Let's take a look at some very common transformations using the _map_ functions.
The map function doesn't change the structure of the JSON. This is a pattern
known in FP as a functor. Consider the following signatures:

```java   

JsObj mapAllValues( Function<JsPrimitive, JsValue> map);

JsObj mapAllKeys( Function<String, String> map);

JsObj mapAllObjs( Function<JsObj, JsValue> map);

JsArray mapAllValues( Function<JsPrimitive, JsValue> map);

//an array doesnt have any key but any JSON object contained does!
JsArray mapAllKeys( Function<String, String> map);

JsArray mapAllObjs( Function<JsObj, JsValue> map);
```

All of them traverse recursively the whole JSON.

The mapAllKeys function transform all the keys of JSON objects. The typical example
is when you want to pass from camel case format to snake case.

The _mapAllValues_ function operates on primitive types (not object or arrays)
and transform them into another value.

If the mapping depends not only on the value but also on its position in the JSON,
you can pass the full path in the map function using the following overloaded
methods:

```java  

JsObj mapAllKeys( BiFunction<JsPath, JsValue, String> map); 

JsObj mapAllValues( BiFunction<JsPath, JsPrimitive, JsValue> map);

JsObj mapAllObjs( BiFunction<JsPath, JsObj, JsValue> map)

```


filter and reduce: TODO



#### <a name="specs"><a/>Specs

But what about validating JSON? We can define the JSON schema following precisely
the same approach as defining JSON:

```java   
import static jsonvalues.spec.JsSpecs.*;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsErrorPair;

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
    
Set<JsErrorPair> errors = personSepc.test(person);   

Function<JsErrorPair, String> toStr = 
    pair -> pair.error.value + " @ "+ pair.path + " doesn't conform spec: " + pair.error.code;   

errors.forEach(pair -> System.out.println(toStr.apply(pair)));
    
    
```

I’d argue that it is very expressive, concise, and straightforward. I call it json-spec.
I named it after a Clojure library named [spec](https://clojure.org/guides/spec). Writing
specs feels like writing JSON. Strict specs don't allow keys that are not specified, whereas
lenient ones do. The real power is that you can create specs from predicates and compose them:

```java   
import static jsonvalues.spec.JsSpecs.*;    
import jsonvalues.spec.JsObjSpec;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.*;

BiFunction<Integer, Integer, Predicate<String>> lengthBetween =
       (min, max) -> string -> string.length() <= max && 
                               string.length() >= min;

BiFunction<Instant, Instant, Predicate<Instant>> instantBetween =
      (min, max) -> instant -> min.isBefore(instant) && 
                               max.isAfter(instant);

BiFunction<Long, Long, Predicate<BigDecimal>> decBetween =
      (min, max) -> n -> BigDecimal.valueOf(min).compareTo(n) < 0 && 
                         BigDecimal.valueOf(max).compareTo(n) > 0;
  
int MAX_NAME_LENGTH = 10;
int MAX_SURNAME_LENGTH = 10;
int MAX_PHONE_LENGTH = 10;
int MAX_CITY_LENGTH = 20;
int MAX_TAG_LENGTH = 20;
int MAX_ZIPCODE_LENGTH = 30;
int MIN_ADDRESSES_SIZE = 1;
int MAX_ADDRESSES_SIZE = 100;
int MAX_TAGS_SIZE = 10;
long LAT_MIN = -90;
long LAT_MAX = 90;
long LON_MIN = -180;
long LON_MAX = 180;    

       
Predicate<String> nameSpec = lengthBetween.apply(0, MAX_NAME_LENGTH);
       
Predicate<String> surnameSpec = lengthBetween.apply(0, MAX_SURNAME_LENGTH);
        
Predicate<String> phoneSpec = lengthBetween.apply(0, MAX_PHONE_LENGTH);
        
Predicate<Instant> registrationDateSpec = instantBetween.apply(Instant.EPOCH, Instant.MAX);

Predicate<BigDecimal> latitudeSpec = decBetween.apply(LAT_MIN, LAT_MAX);

Predicate<BigDecimal> longitudeSpec = decBetween.apply(LON_MIN, LON_MAX);

Predicate<String> citySpec = lengthBetween.apply(0, MAX_CITY_LENGTH);
        
Predicate<String> tagSpec = lengthBetween.apply(0, MAX_TAG_LENGTH);
       
Predicate<String> zipCodeSpec = lengthBetween.apply(0, MAX_ZIPCODE_LENGTH);
        

JsObjSpec personSpec =
    JsObjSpec.strict("name", str(nameSpec),
                     "surname", str(surnameSpec),
                     "phoneNumber", str(phoneSpec).nullable(),
                     "registrationDate", instant(registrationDateSpec),
                     "addresses", 
                     arrayOfObjSpec(JsObjSpec.lenient("coordinates",
                                                      tuple(decimal(latitudeSpec),
                                                            decimal(longitudeSpec)
                                                           ),
                                                      "city", str(citySpec),
                                                      "tags", arrayOfStr(tagSpec,
                                                                         0,
                                                                         MAX_TAGS_SIZE
                                                                        ),
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
the whole JSON and then validating it, we can verify the schema while parsing it and
stop the process as soon as an error happens. **After all, failing fast is important as well!**

```java      
import com.dslplatform.json.JsParserException;
import jsonvalues.spec.JsObjParser;    

JsObjParser personParser = new JsObjParser(personSpec);

String string = "...";
    
try{

    JsObj person = personParser.parse(string);
    
   }
catch(JsParserException e){
    
    System.out.println("Error parsing person JSON: " + e.getMessage())
    
}    

```

#### <a name="gen"><a/>Generators

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
              "addresses", 
              JsArrayGen.biased(JsObjGen.of("coordinates", 
                                            JsTupleGen.of(JsBigDecGen.biased(LAT_MIN, LAT_MAX),
                                                          JsBigDecGen.biased(LON_MIN, LON_MAX)
                                                         ),
                                            "city", JsStrGen.biased(0, MAX_CITY_LENGTH),
                                            "tags", JsArrayGen.biased(JsStrGen.biased(0, 
                                                                                      MAX_TAG_LENGTH
                                                                                     ),
                                                                      0,
                                                                      MAX_TAGS_SIZE
                                                                     ),
                                            "zipCode", JsStrGen.biased(0, MAX_ZIPCODE_LENGTH)
                                            )
                                         .setOptionals("tags", "zipCode", "city"),
                                MIN_ADDRESSES_SIZE, 
                                MAX_ADDRESSES_SIZE        
                                )
              )
           .setOptionals("surname", "phoneNumber", "addresses");

```


Most generators have two static factory methods: _biased_ and _arbitrary_. The latter returns
a uniform distribution of values, whereas the former generates, with a higher probability,
potential problematic values that tend to cause bugs in our code. For example:

* Integer generator

```java 
    
import fun.gen.Gen;    
    
Gen<JsStr> gen = JsIntGen.biased();

``` 
It produces with higher probability the values:

-  Integer.MAX_VALUE
-  Integer.MIN_VALUE
-  Short.MAX_VALUE
-  Short.MIN_VALUE
-  Byte.MAX_VALUE
-  Byte.MIN_VALUE
-  0

We can specify a bounded interval:

```java    

Gen<JsStr> gen = JsIntGen.biased(min, max)

``` 

that produces with higher probability the bounds of the interval *min* and *max*, and all the
previoues values from the unbounded generator that fall between the interval.

* Long generator

```java    
    
Gen<JsLong> unbounded = JsLongGen.biased();

Gen<JsLong> bounded = JsLongGen.biased(min, max);
    
``` 

Same values as the integer generators plus *Long.MAX_VALUE* and *Long.MIN_VALUE*

* String generator

```java    
    
Geb<JsStr> gen = JsStrGen.biased(min, max);
    
``` 
produces with higher probability the blank string of length *min* and *max*

If the predefined static factory methods doesn't suit your needs, you
can always create a new generator from the more general primitive types generators
and the function map or just using some combinator:

```java  
import fun.gen.Gen;
import fun.gen.Combinators;
import jsonvalues.gen.JsCons;

//notice it's a String generator
Gen<String> gen = seed -> () -> seed.nextInt() % 2 == 0 ? "even" : "odd";

Gen<JsStr> parity = gen.map(JsStr::of);

//using the oneOf combinator

Gen<JsStr> parity = Combinators.oneOf("even",
                                      "odd"
                                     )
                               .map(JsStr::of);
                                                          
```

You can combine any number of generators and set the probability of selecting each of them
for the next value generation:

```java 
// 20% alphaumeric strings and 80% digits
Gen<JsStr> gen = Combinators.freq(new Pair<>(2, JsStrGen.alphanumeric(0, 10)),
                                  new Pair<>(8, JsStrGen.digits(0,10)));
                                 
// 30% long  and 70% integers                                  
Gen<JsValue> gen = Combinators.freq(new Pair<>(3, JsLongGen.biased()),
                                    new Pair<>(7, JsIntGen.biased()));                                

```


Go to the javadoc to get more details about every generator. json-values
generators are built on top of the generators of java-fun.

#### <a name="optics"><a/>Optics
TODO
##### <a name="lenses"><a/>Lenses
TODO
##### <a name="prism"><a/>Prism
TODO
##### <a name="opt"><a/>Optionals
TODO





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
    <version>11.1.0</version>
</dependency>
```

## <a name="rp"><a/> Related projects

“Ideas are like rabbits. You get a couple and learn how to handle them, and pretty soon you have a dozen.” – John
Steinbeck

After the development of json-values, I published two more related projects:

* The Scala version: [json-scala-values](https://github.com/imrafaelmerino/json-scala-values)
* [vertx-effect](https://github.com/imrafaelmerino/vertx-effect)
* [mongo-values](https://github.com/imrafaelmerino/mongo-values) Set of codecs to use json-values with MongoDB
* [JIO](https://github.com/imrafaelmerino/JIO)


json-values uses the persistent data structures from [vavr](https://www.vavr.io/), 
[Jackson](https://github.com/FasterXML/jackson) to parse a string/bytes into
a stream of tokens and [dsl-sjon](https://github.com/ngs-doo/dsl-json) to parse a string/bytes given a spec.

