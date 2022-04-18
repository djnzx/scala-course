package rtj_cats

import cats._
import cats.data._
import cats.implicits._

object ReaderApp extends App {

  def toDouble(i: Int): Double = i.toDouble
  val r1: Kleisli[Id, Int, Double] = Reader(toDouble)

  def toString(d: Double): String = d.toString
  val r2: Kleisli[Id, Double, String] = Reader(toString)

  val r1a: Kleisli[Id, Int, String] = r1.map(toString) // here we treat it as: Double => String
  val r1b: Kleisli[Option, Int, Double] = r1.mapK(new (Id ~> Option) {
    override def apply[A](fa: Id[A]): Option[A] = Option(fa)
  })
  val r1c: Kleisli[Option, Int, String] = r1.mapF { d => Option(d.toString) }

  val r3a = r1.flatMap(d => r2)
  val r3b: Kleisli[Id, Int, String] = r1.flatMapF(toString) // here we treat it is Double => Id[String]

}
