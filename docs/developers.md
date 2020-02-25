
- [Why is json-values a one-package library?](#opl)
- [Why some methods have a well-documented Javadoc and others not?](#javadoc)
- [Has json-values been developed using TDD?](#tests)
- [How long has it taken to develop json-values?](#time)
- [Software Quality Award (2019)](#award)


## <a name="tests"><a/> Why is json-values a one-package library?
I don't want the user to get drowned in hundred of classes that they don't have to know about after hitting the 
autocomplete shortcut of their IDE. Before java 9, it's not possible to do that and having several packages at 
the same time, mainly when one package depends on another. From my point of view, the access modifier public 
in Java, it too public. json-values has been developed and compiled in Java 8. 

## <a name="javadoc"><a/> Why some methods have a well-documented Javadoc and others not?
Writing comments and Javadoc is a polarizing issue in our industry. 
The way I go about it in json-values is the following:
    
   - If a class, method, or field is public, it has to be well-documented. There is no excuse. 
   No matter how good is the code. From my point of view, good and bad code seems the same to 
   the users across the big pond using your library. They both are just bytecode interpreted 
   by the JIT compiler and running on a JVM. 

   - If a class, method, or field is not public, I don't usually write Javadoc unless something important has 
   to be pointed out. I try to write self-documented code following the philosophy that simplicity matters.
 
## <a name="opl"><a/> Has json-values been developed using TDD?
No, it doesn't. I don't like TDD. I know, shame on me! I care about testing, but I usually write tests after 
coding for a while. It helps me be in the user's shoes and focus on aspects like how good is an interface, 
how easy it is to use the API etc. Doing TDD my brain is focused mainly on just passing the test. In the end, 
having quality tests and proper test coverage is what matters, and not the means.

[JUnit 5](https://junit.org/junit5/) and Property Based Testing with [ScalaCheck](https://www.scalacheck.org/) has
been used to test json-values. PBT is excellent, and if you think about it, computers are way better than humans at 
generating random data, and we should take advantage of that. The test coverage figure that appears in [Codecov](https://codecov.io/gh/imrafaelmerino/json-values), corresponds to 
the tests implemented using only JUnit. I haven't found a way of adding up both test coverages figures from JUnit and 
ScalaCheck.

## <a name="time"><a/> How long has it taken to develop json-values?
I've been working on it since 2016. I did the first version of json-values two years ago, it was called [neatjson](https://github.com/imrafaelmerino/neatjson/). Back then, I was a typical enterprise object-oriented programmer; you know what I mean, I loved frameworks like Spring, Hibernate, and all that stuff. I reeducated myself reading a lot of books and watching a lot of videos on youtube about computer science:  functional programming, pure object-oriented programming, APIs, programming languages... I've put in json-values all the knowledge I've been acquiring since then.
Now, I don't like frameworks and tools, and I love programming languages. I consider myself an ultimately better programmer than I was, and it doesn't have anything to do with experience.  Albert Einstein said: "The definition of insanity is doing the same thing over and over again and expecting a different result."  
It has to do with education and practice, like everything in life. 

## <a name="award"><a/> Software Quality Award (2019)
This project participated in the [Software Quality Award](https://www.yegor256.com/2018/09/30/award-2019.html) in 2019, and it was 
qualified with 0 points, claiming that it didn't fit the minimum requirements. 
The reasons they gave me were the followings ([link to reviews](https://www.yegor256.com/txt/2019/award-2019.txt)):
* Only 2.7k lines of code.  
* A quote from the README: "json-values is functional", what does it have to do with OOP an EO? 

Regarding the first point, it's obvious they made a great mistake. I don't know how they
got that measure. Cloning the repo and executing the [command](https://www.npmjs.com/package/cloc)  _cloc src_ would have returned :

```

-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                           108           3443           3351          19414
Scala                           31            592             46           3195
XML                              1              0              0             13
-------------------------------------------------------------------------------
SUM:                           140           4035           3397          22622
-------------------------------------------------------------------------------
```

Furthermore, following the link to [Sonar Cloud](https://sonarcloud.io/code?id=imrafaelmerino_json-values) from the badge that appears in the readme of the repo reveals that 
my project without tests has around 13000 lines of code.

Regarding the second point, what kind of defect is to be functional? The library has been implemented in Java and 
following both FP and OOP paradigms. It's functional because a persistent Json has been implemented, that's all. 
Immutability is a great virtue in any paradigm, but without persistent data structures sometimes is not feasible because of
performance (copying big data structure every change you make is very inefficient).

I reported this immediately, and I was answered that it was a mistake and that they would double-check my poor review. 
I'm still waiting.

These are the facts, and I don't pretend anything else that explaining what happened, because this was quite disappointing.  

On the other hand, I must admit that my project improved a lot thanks to this award. 








    
