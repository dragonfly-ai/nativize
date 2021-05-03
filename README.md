# nativize

This Scala.JS library provides a rudimentary framework for exporting scala.js classes and companion objects to native JavaScript programming environments in ways that feel canonical from JavaScript contexts.

For example, using the ```@JSExport``` family of annotation macros, scala companion object factories take different forms depending on whether accessed from Scala.js or JavaScript contexts.

Consider a simple case class:
```scala
@JSExportTopLevel("Foo") @JSExportALL
case class Foo(x:Int)
```
From Scala and Scala.js programming contexts, we can access Foo's factory method without explicitly referring to Foo.apply(1).
```scala
val foo = Foo(1)
```
However, from the JavaScript perspective, we can only access the Factory with an explicit call to apply:
```javascript
var foo = Foo.apply(1);
```

The nativize library allows us to export custom function objects to the global scope in order to retain factory patterns for simple classes.
For example:
```scala
  /**
   * Main Package Scope.
   */
  @JSExportTopLevel("Foo")
  val scope:js.Dynamic = factoryJS(
    Foo.apply _
  )
```
By creating this facade, JavaScript code can directly access the Foo.apply factory method in a way that feels more native to JavaScript developers.
```javascript
var foo = Foo(1);
```

More Examples:
```scala
@JSExportAll
object Vec3 {
  def add(v0:Vec3, v1:Vec3):Vec3 = Vec3( v0.x + v1.x, v0.y + v1.y, v0.z + v1.z )
  def normalize(v:Vec3):Vec3 = { val m = v.magnitude; Vec3( v.x / m, v.y / m, v.z / m ) }
}

@JSExportAll
case class Vec3(x:Double, y:Double, z:Double) {
  def magnitude:Double = Math.sqrt(x*x + y*y +z*z)
}

object NativeFacadeJS {
  @JSExportTopLevel("Vec3")
  val scope:js.Dynamic = factoryJS(
    Vec3.apply _,
    "add" ->  Vec3.add _,
    "normalize" -> Vec3.normalize _
  )
}
```

This allows access to companion object methods and fields from plain JavaScript:
```javascript
var v0 = Vec3(1, 2, 3)
var v1 = Vec3(4, 5, 6)
var sum = Vec3.add(v0, v1)
console.log(sum.toString())
var normalizedSum = Vec3.normalize(sum)
```