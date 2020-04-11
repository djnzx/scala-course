package cats

import scala.concurrent.{Await, Future}

object C197UseCaseCH9Par extends App {

  import C189UseCaseCH9Par.foldMap

  val nCPU: Int = Runtime.getRuntime.availableProcessors // 8
  println(nCPU)
  val data : Vector[Int] = (1 to 200).toVector

  // data size calculation
  val dataSize = (data.length.toDouble / nCPU).ceil.toInt
  // partition our data
  val partitioned: Vector[Vector[Int]] = data.grouped(dataSize).toVector

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  import cats.instances.int._
  import cats.instances.vector._
  import cats.instances.future._
  import cats.syntax.traverse._

  // mapping and reducing in parallel
  val step1: Vector[Future[Int]] = partitioned.map(x => Future { foldMap(x)(identity) })
  val step2: Future[Vector[Int]] = step1.sequence
  // combining results
  val step3: Future[Int] = step2.map { vi => foldMap(vi)(identity) }
  // unpacking Future
  val step4 = Await.result(step3, 1.second)

  println(step4)
}
