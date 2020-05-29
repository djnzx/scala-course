package pfps

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.{Contains, NonEmpty}
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

/**
 * https://github.com/fthomas/refined
 */
object A028 extends App {
  type Username = NonEmptyString Refined Contains['g']
  def lookup(username: Username) = ???

  // we can refine only once!
  type BrandX = String Refined NonEmpty
  @newtype case class Brand(value: BrandX)
  @newtype case class Category(value: NonEmptyString)

  val brand: Brand = Brand("foo")
  println(brand)
}
