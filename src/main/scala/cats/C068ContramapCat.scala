package cats

import cats.Contravariant
import cats.Show
import cats.instances.string._
import cats.syntax.contravariant._ // for contramap

object C068ContramapCat extends App {

//  trait Contravariant[F[_]] {
//    def contramap[A, B](fa: F[A])(f: B => A): F[B]
//  }
//
//  trait Invariant[F[_]] {
//    def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
//  }

  // 1. I know how to show String
  val showString: Show[String] = Show[String]
  // 2. I know how to map my symbol to string
  val mapper: Symbol => String = (sm: Symbol) => sm.name
  // 3.1. I want to reuse them in order not to duplicate the code:
  val showSymbol1: Show[Symbol] = Contravariant[Show].contramap(showString)(mapper)
  // 3.2. or:
  val showSymbol2: Show[Symbol] = showString.contramap[Symbol](mapper)

  // 4. I have the symbol
  val sym: Symbol = Symbol("dave") //
  // or, deprecated since 2.13
//  val sym2: Symbol = 'dave
  println(sym)
//  println(sym2)

  // 5.1. I can use my function 3.1 to show it
  val s1: String = showSymbol1.show(sym)
  println(s1)

  // 5.2. I can use my function 3.2 to show it
  val s2: String = showSymbol2.show(sym)
  println(s2)

}
