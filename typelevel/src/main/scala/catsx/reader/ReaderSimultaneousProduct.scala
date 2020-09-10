package catsx.reader

import cats.Id
import cats.data.{Kleisli, Reader}
import cats.implicits.catsSyntaxTuple2Semigroupal
import pprint.{pprintln => println}

import scala.util.Try

object ReaderSimultaneousProduct extends App {
  val r1: Reader[Int, Int]            = Reader { a: Int => a + 1 }
  val r2: Reader[Int, Int]            = Reader { a: Int => a * 2 }
  val r3: Reader[Int, String]         = Reader { a: Int => a.toString }
  val r4: Reader[String, Option[Int]] = Reader { s: String => Try(s.toInt).toOption }
  val r5: Reader[Any, Unit]           = Reader { x: Any => println(x) }
  
  /** chaining */
  val r123: Kleisli[Id, Int, String] = r1 andThen r2 andThen r3
  val r = r123(10) // (10+1)*2 => 22
  println(r)
  
  // .map(f: B => C) => A => F[C]
  val z1: Kleisli[Id, Int, Double] = r1.map(_.toDouble)
  // .flatMapF( B => F[C]) => A => F[C]
  // flatMapF === andThen
  val z2: Kleisli[Id, Int, Double] = r1.flatMapF(_.toDouble)
  // .flatMap( B => AA => F[C]) => AA => F[C]
  println(z1(100))
  println(z2(100))

  /**
    * Reader flatMap
    * simultaneously run all
    * and access to the result inside for
    * flatMap / for syntax
    */
  val rx: Kleisli[Id, Int, (Int, Int)] = for {
    r1x <- r1
    r2x <- r2
  } yield (r1x, r2x)
  println(rx(10))

  /**
    * Reader flatMap
    * simultaneously run all
    * and access to the result inside for
    * product / map2 syntax
    */
  val zz: Reader[Int, (Int, Int)] = (r1, r2).mapN(Tuple2.apply)
  println(zz(15))

}
