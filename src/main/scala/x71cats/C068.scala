package x71cats

import cats.Contravariant
import cats.Show
import cats.instances.string._
import cats.syntax.contravariant._ // for contramap

object C068 extends App {

//  trait Contravariant[F[_]] {
//    def contramap[A, B](fa: F[A])(f: B => A): F[B]
//  }
//
//  trait Invariant[F[_]] {
//    def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
//  }

  val showString = Show[String]
  val showSymbol = Contravariant[Show].contramap(showString)((sym: Symbol) => s"'${sym.name}")

  val s1: String = showSymbol.show(Symbol("dave"))
  println(s1)
  val s2: String = showString.contramap[Symbol](_.name).show(Symbol("dave"))
  println(s2)

}
