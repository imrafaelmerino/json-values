
- [Why json-values is a one-package library?](#opl)
- [Why some methods have a well-documented javadoc and others not?](#javadoc)
- [Has json-values been developed using TDD?](#tests)

## <a name="tests"><a/> Why json-values is a one-package library?
I don't want the user to get drowned in hundred of classes that they don't have to know about after hitting the 
autocomplete shortcut of their IDE. 
Before java 9, it's not possible to do that and having several packages at the same time, mainly when one package 
depends on another. From my point of view, the access modifier public in Java, it too public. 

At this moment, json-values is compiled in Java 8 and for now (10-28-2019) I have no intention of implementing a 
new version in Java 9, at least not before I implement the library in Scala and the Json Schema specification. 

## <a name="javadoc"><a/> Why some methods have a well-documented Javadoc and others not?
Writing comments and Javadoc is a polarizing issue in our industry. 
The way I go about it  in json-values is the following:
    
   - If a class, method, or field is public, it has to be well-documented. There is no excuse. No matter how
    good is the code. Remember, good 
and bad code seems the same to the users across the big pond using your library. They both are just bytecode
interpreted by the JIT compiler and running on a JVM. 

   - If a class, method, or field is not public, I don't usually write Javadoc unless something important has to 
 be pointed out. And now, not as before, the user of your code is the guy sitting in front of you, and
   they will appreciate good implementations and quality code. Developers don't pay as much attention to 
   the Javadoc as to the source code they are developing.
 
## <a name="opl"><a/> Has json-values been developed using TDD?
No, it doesn't. I don't like TDD. I know, shame on me! I care about testing, but I usually write tests
after coding for a while (even after a week sometimes!). It helps me be in the user's shoes and focus on aspects
like how good is an interface, how easy it is to use the API etc. It's while I'm writing documentation and 
tests when I refactor the most because I realize something could be more readable, usable, or concise. It doesn't
happen to be doing TDD because my brain is focused on other low-level aspects. In the end, having quality tests and proper 
test coverage is what matters, and not the means.

Some tests that requires a specific input have been implemented using [JUnit 5](https://junit.org/junit5/).
On the other hand, where the goal is to test some method using a large number of random inputs, I've used 
Property Based Testing with [ScalaCheck](https://www.scalacheck.org/). 
PBT is excellent, and if you think about it, computers are way better than humans at cranking out data, and 
we should take advantage of that. The test coverage that is in [Codecov](https://codecov.io/gh/imrafaelmerino/json-values), corresponds to the test implemented
using Junit. I haven't found a way of adding up bot test coverages from Junit and ScalaCheck.
