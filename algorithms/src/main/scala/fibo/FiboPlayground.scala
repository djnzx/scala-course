package fibo

import scala.collection.mutable

object FiboPlayground extends App {

  // O(N^N)
  object ClassicRecursion {
    def fibo(n: Int): Int = n match {
      case 0 | 1 => n
      case n     => fibo(n - 1) + fibo(n - 2)
    }
  }

  // O(N), mutable cache is used
  object ClassicRecursionCached {
    private val cache = mutable.Map.empty[Int, Int]

    def fibo(n: Int): Int = n match {
      case 0 | 1 => n
      case n     => cache.getOrElse(
          n, {
            val nTh = fibo(n - 1) + fibo(n - 2)
            cache.put(n, nTh)
            nTh
          }
        )
    }
  }

  // O(N), mutable state is used
  object JavaIshMutable {

    def fibo(n: Int): Int = {
      var n1: Int = 0
      var n2: Int = 1

      for {
        _ <- 1 to n
      } yield {
        val next = n1 + n2
        n1 = n2
        n2 = next
      }

      n1
    }

  }

  // O(N), recursion without necessity to unwind
  object TailRecursion {
    def fibo(n: Int): Int = {

      def fiboTR(n1: Int, n2: Int, cnt: Int): Int = cnt match {
        case 0   => n1
        case cnt => fiboTR(n2, n1 + n2, cnt - 1)
      }

      fiboTR(0, 1, n)
    }
  }

  // O(N), declarative way
  object DeclarativeImplementation {
    def fibo(n: Int): Int = (1 to n).foldLeft((0, 1)) { case ((n1, n2), _) => (n2, n1 + n2) }._1
  }

  // 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765
  val range = 0 to 20
  val r0 = range.map(x => ClassicRecursion.fibo(x))
  val r1 = range.map(x => ClassicRecursionCached.fibo(x))
  val r2 = range.map(x => JavaIshMutable.fibo(x))
  val r3 = range.map(x => TailRecursion.fibo(x))
  val r4 = range.map(x => DeclarativeImplementation.fibo(x))
  println(r0)
  println(r1)
  println(r2)
  println(r3)
  println(r4)

}
