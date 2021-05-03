package ai.dragonfly.nativize

import scala.scalajs.js
import js.Dynamic.global.{selectDynamic => getGlobal}
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

@JSExportAll
object Vec3 {
  def add(v0:Vec3, v1:Vec3):Vec3 = Vec3( v0.x + v1.x, v0.y + v1.y, v0.z + v1.z )
  def normalize(v:Vec3):Vec3 = { val m = v.magnitude; Vec3( v.x / m, v.y / m, v.z / m ) }
}

@JSExportAll
case class Vec3(x:Double, y:Double, z:Double) {
  def magnitude:Double = Math.sqrt(x*x + y*y +z*z)
}

object Demo extends App {
  /**
   * Main Package Scope.
   */
  @JSExportTopLevel("demo")
  val scope:js.Dynamic = js.Dynamic.literal(
    "Vec3" -> factoryJS(
      Vec3.apply _,
      "add" ->  Vec3.add _,
      "normalize" -> Vec3.normalize _
    )
  )
}
