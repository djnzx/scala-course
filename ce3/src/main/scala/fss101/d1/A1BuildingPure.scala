package fss.d1

import fs2._

object A1BuildingPure extends App {

  /** empty */
  val s0: Stream[Pure, INothing] = Stream.empty

  /** one element */
  val s1: Stream[Pure, Int] = Stream.emit(11)

  /** many elements, sequence */
  val s2a: Stream[Pure, Int] = Stream.emits(List(11, 12, 13))

  /** many elements, variadic */
  val s2b: Stream[Pure, Int] = Stream(21, 22, 23)

  /** pure streams (doesn't require computation) can be collected immediately */
  val r1: Vector[Int] = s2b.toVector
  val r2: List[Int] = s2a.toList

  /** combination */
  val s3: Stream[Pure, Int] = Stream(1, 2, 3) ++ Stream(4, 5)
  s3.map(_ + 1)
  s3.flatMap(x => Stream(x, x * 10)) // 1, 10, 2, 20, 3, 30, 4, 40, 5, 50
  s3.filter(_ > 0)
  Stream(1, 2, 3).fold(0)(_ + _) // Stream of ONE element

  /** collect by using partial function */
  Stream(None, Some(2), Some(3)).collect { case Some(i) => i }

  /** insert something between elements */
  Stream.range(0, 5).intersperse(42).toList // 0, 42, 1, 42, 2, 42, 3, 42, 4

  /** repeat */
  Stream(1, 2, 3).repeat.drop(1).take(5) // 2 3 1 2 3
  Stream(1, 2, 3).repeatN(4) // 1 2 3 1 2 3 1 2 3

}
