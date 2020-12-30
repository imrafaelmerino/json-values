# JSON-VALUES
## v9.0.0-RC1  ( Tue Aug 18 2020 22:00:48 GMT+0200 (Central European Summer Time) )

## Features 
  - New methods: JsArray.get(int), JsArray.set(int,jsvalue), JsArray.getXXX(int)
  - New methods: JsObj.get(string)
  - New type JsPrimitive
  - Filter,map and reduce new methods: sometimes you don't need the full JsPath of each element 
  - Upgrading jackson and dsl-json dependencies
  
## Breaking changes 
  - Futures moved into JIO project
  - Console programs moved into JIO project 
  - Filter, map and reduce use BiFunction instead of a function that takes a JsPair
  - Remove Json supplier (it was a just an experiment and nobody uses it)
  - Remove Trampoline. Sadly Java doesnt implement tail recursion and create trampolines puts a lot of pressure into de garbage collector.
  - filter, map and reduce refactored 
  - remove isNotJson method: isPrimitive method has been added.

## Performance
  - Remove trampolines and recursion in favour of iteration 
  - filter, map and reduce don't create JsPair

## Bug
  - JsArray.get(int) doesnt return last element when index is -1
  - JsArray.set(path,value,pad) doesn't remove element when value is JsNothing 
  - JsObj.set(path,value,pad) doesn't remove element when value is JsNothing 



