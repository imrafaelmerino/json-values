###### version 0.1.2 
- added changelog document
- javadoc fixes and improvements
- Added overloaded methods to work with primitive types directly instead of with JsElem: _putIfAbsent_, _putIfPresent_, _append_, _prepend_, _appendIfPresent_, _prependIfPresent_.
- Fixed signature of method _appendIfPresent_: Supplier<? super JsElement> instead of Supplier<JsElem>.
- Implementation change in _append_ methods: don't use appendAll method internally, which avoid to allocate an array when it's not necessary.
- Implementation change in _prepend_ methods: don't use appendAll method internally, which avoid to allocate an array when it's not necessary.
- Change signature of methods _appendAllIfPresent_ and _prependAllIfPresent_: supplier instead of function
- Added plus, minus and times methods in JsInt, JsLong and JsDouble
- Added fromKey and fromIndex static factory methods in JsPath
- bug fixed in intersection_ of arrays
###### version 0.1.3
- added isDecimal method in JsElem 