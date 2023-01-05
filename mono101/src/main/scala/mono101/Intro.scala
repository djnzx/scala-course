package mono101

import monocle.AppliedLens
import monocle.syntax.all._

/** https://www.optics.dev/Monocle/docs/focus */
/** https://www.optics.dev/Monocle/docs/optics */
object Intro extends App {

  case class Address(n: Int, street: String)
  case class User(name: String, address: Address)

  val user = User("Anna", Address(12, "high street"))

  // lens applied to object
  val a: AppliedLens[User, String] = user.focus(_.name)
  // value extracted to Option[A]
  val b: Option[String] = user.focus(_.name).getOption
  // value extracted to A
  val c: String = user.focus(_.name).get

  val u1 = user.focus(_.name).replace("Bob")
  pprint.pprintln(u1)

  // formatter: off
  val u2 = user
    .focus(_.address.n).replace(33)
    .focus(_.address.n).modify(_ + 1)
    .focus(_.name).modify(_.toUpperCase)
  // formatter: on

  pprint.pprintln(u2)

}
