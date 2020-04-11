package cats

import scala.concurrent.{Await, Future}

object C197UseCaseCH9Par extends App {

  def parFoldMap[A, B: Monoid](data: Vector[A])(f: A => B)(threads: Int): B = {
    // import single-threaded implementation
    import C189UseCaseCH9Par.foldMap
    // data size calculation
    val dataSize: Int = (data.length.toDouble / threads).ceil.toInt
    // partition our data
    val partitioned: Vector[Vector[A]] = data.grouped(dataSize).toVector

    import scala.concurrent.ExecutionContext.Implicits.global
    import cats.instances.vector.catsStdInstancesForVector
    import cats.instances.future._
    import cats.syntax.traverse._
    // mapping and reducing in parallel
    // sequence
//    val step1: Vector[Future[B]] = partitioned.map(x => Future { foldMap(x)(f) })
//    val step2: Future[Vector[B]] = step1.sequence
    val step2: Future[Vector[B]] = partitioned.traverse(sub => Future { foldMap(sub)(f) } )
    // combining results
    val step3: Future[B] = step2.map { vi: Vector[B] => foldMap(vi)(identity) }
    // unpack future
    import scala.concurrent.duration._
    val step4 = Await.result(step3, 1.second)
    // return result
    step4
  }

  val myData : Vector[Int] = (1 to 200).toVector

  import cats.instances.int._
  val foldRun: Int => Int = parFoldMap(myData)(identity)

  val nCPU: Int = Runtime.getRuntime.availableProcessors // 8
  println(nCPU)

  println(foldRun(nCPU))
}
