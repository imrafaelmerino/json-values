/**
 json-values is a one-package and zero-dependency library to work with jsons in a declarative and functional way.
 There are different static factory methods to create objects:
 <ul>
 <li><code>of</code> methods to create immutable objects or values from primitive types.</li>
 <li><code>_of_</code> methods to create mutable objects from primitive types.</li>
 <li><code>parse</code> methods to parse strings into immutable objects or values.</li>
 <li><code>_parse_</code> methods to parse strings into mutable objects.</li>
 </ul>
 Only three exceptions are thrown by the library:
 <ul>
 <li>the unchecked UnsupportedOperationException, when the client makes an error calling certain methods.</li>
 <li>the checked MalformedJson, when a string can not be parsed into a json.</li>
 <li>the unchecked NullPointerException, when a method receives null as a parameter.</li>
 </ul>
 All the object methods that end with underscore traverse the whole json recursively, and not only
 the first level.
 */
package jsonvalues;