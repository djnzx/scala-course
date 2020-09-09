package catsx.reader

import cats.Id
import cats.data.{Kleisli, Reader}
import cats.implicits.catsSyntaxTuple2Semigroupal
import pprint.{pprintln => println}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FutureEx extends App {

  val r1: Reader[Any, Future[Int]] = Reader { _: Any => Future { Thread.sleep(2000); 1 }}
  val r2: Reader[Any, Future[Int]] = Reader { _: Any => Future { Thread.sleep(4000); 2 }}

  val was1 = System.currentTimeMillis()
  val rc1: Kleisli[Id, Any, Future[Int]] = for {
    r1f <- r1
    r2f <- r2
  } yield r1f.flatMap(a => r2f.map(b => a+b))
  println(Await.result(rc1(()), 5.seconds))
  val delta1 = System.currentTimeMillis() - was1
  println(delta1) // 4381L ???

  val was2 = System.currentTimeMillis()
  val rc2: Reader[Any, Future[Int]] = (r1, r2).mapN((f1, f2) => f1.flatMap(a => f2.map(b => a+b)))
  println(Await.result(rc2(()), 5.seconds))
  val delta2 = System.currentTimeMillis() - was2
  println(delta2) // 4381L ???

  /**
    * actually
    * - flatMap is a chaining
    * - mapN is a parallel
    * 
    * but because there is no sense to chain => they started at the same time ???   
    */
}
