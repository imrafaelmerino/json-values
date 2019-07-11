/**
 json-values is a one-package and zero-dependency library to work with jsons in a declarative and functional way.
 There are different static factory methods to create objects:
 <ul>
 <li><code>of</code> methods to create immutable objects or values from primitive types.</li>
 <li><code>parse</code> methods to parse strings into immutable objects or values.</li>
 <li><code>_of_</code> methods to create mutable objects from primitive types.</li>
 <li><code>_parse_</code> methods to parse strings into mutable objects.</li>
 </ul>
 Only three exceptions are thrown by the library:
 <ul>
 <li>the unchecked UnsupportedOperationException, when the client makes a programming error.</li>
 <li>the checked MalformedJson, when a string can not be parsed into a json.</li>
 <li>the unchecked NullPointerException, when a method receives a null parameter.</li>
 </ul>
 All the methods which name ends with underscore are applied to the whole json recursively, and not only
 to the first level. For example:
 <pre>
 {@code
 x={"a":1, "b":[{"c":1, "d":true}]}
 x.size() = 2  // a and b
 x.size_() = 3 // a, b.0.c and b.0.1
 x.mapKeys(toUppercase)  =  {"A":1, "B":[{"c":1, "d":true}]}
 x.mapKeys_(toUppercase) =  {"A":1, "B":[{"C":1, "D":true}]}
 }
 </pre>
 */
package jsonvalues;