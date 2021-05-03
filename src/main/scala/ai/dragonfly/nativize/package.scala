package ai.dragonfly

import org.scalajs.dom.experimental.URL

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.JavaScriptException

package object nativize {

//  val apply: String => (js.Any => Unit) = js.Dynamic.global.updateDynamic
//  (path:String)(immigrant:js.Dynamic):Unit = {
//    val pathTokens = path.split(".")
//    if (js.Dynamic.global.selectDynamic(pathTokens(0)))
//    for (pt <- pathTokens.tail) {
//      if () {
//
//      }
//    }
//
//    js.Dynamic.global.updateDynamic(path)(immigrant)
//  }

  /**
   * Convenience Method for handling default values in native JavaScript factories and overloaded methods.
   * @param parameter a parameter that may have an undefined value.
   * @param default a default value to use if parameter is undefined.
   * @tparam T the type of the default value
   * @return the specified parameter or the default value.
   */
  def orDefault[T](parameter:T, default:T):T = if(js.isUndefined(parameter)) default else parameter

  /**
   * Convenience Method for handling default values in native JavaScript factories and overloaded methods.
   * @param parameter a numeric parameter that may have a value of 0 or undefined.
   * @param default a default value to use if parameter is 0 or undefined.
   * @return the specified parameter or the default value.
   */
  def orDefaultNumber(parameter:Double, default:Double):Double = if(parameter == 0) default else parameter

  /**
   * Implicitly converts between js.BigInt and Long types
   *
   * @param bi a native JavaScript BigInt
   * @return Long
   */
  implicit def jsBigInt2Long(bi:js.BigInt):Long = java.lang.Long.parseLong(bi.toString())

  /**
   * Implicitly converts between Long and js.BigInt
   *
   * @param l a scala.Long.
   * @return js.BigInt
   */
  implicit def Long2jsBigInt(l:Long):js.BigInt = js.BigInt(l.toString)

  /**
   * Implicitly converts between native JavaScript URL and java.net.URI
   * @param url a native JavaScript URL.
   * @return java.net.URI
   */
  implicit def URL2URI(url:URL):java.net.URI = new java.net.URI(url.toString)

  /**
   * Wraps internal Exception types in instances of scala.scalajs.js.JavaScriptException.
   * @param f a factory method that returns an Exception from a single parameter of type:String.
   * @return
   */
  def exceptionJS(f:String => Throwable):String => JavaScriptException = (s:String) => JavaScriptException(f(s))

  /**
   * Generates a facade for scala or scala.js factory methods in the form of a native JavaScript Function Object with
   * member fields.
   *
   * factoryJS generates native JavaScript objects with static methods and fields scoped analogously to scala syntax for
   * case classes and classes with companion objects.
   *
   * For example, instead of having to call: some.path.ClassName.apply() to access a case class's factory method,
   * native JavaScript developers can call: some.path.ClassName() just as Scala and Scala.js developers can.
   *
   * When using this on factories with default parameters or overloaded factory methods, take care to handle undefined
   * parameters in a single apply method.
   *
   * @param f a function that wraps a scala object's factory apply method.
   * @param members other data members that appear in the scala object that should get exposed in JavaScript.
   * @return
   */
  def factoryJS(f:js.Function, members:(String, js.Any)*):js.Dynamic = {
    val jsObj:js.Dynamic = js.Dynamic.literal("t" -> f).selectDynamic("t")
    for ((name, value) <- members) jsObj.updateDynamic(name)(value)
    jsObj
  }

  /**
   * exit the program if running in Node.JS
   * @param i exit code.
   */
  def exit(i: Int):Unit = try {
    js.Dynamic.global.process.exit(i)
  } catch {
    case _:Throwable => println("Can't exit from the browser.")
  }

}
