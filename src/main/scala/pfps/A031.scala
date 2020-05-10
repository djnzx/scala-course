package pfps

import cats.data.State

object A031 extends App {
  val nextInt: State[Int, Int] = State(s => (s + 1, s * 2))

  def seq: State[Int, Int] = for {
    n1 <- nextInt // (2,2)
    n2 <- nextInt // (3,4)
    n3 <- nextInt // (4,6)
  } yield n1 + n2 + n3 // 2+4+6

  println(seq.run(1).value)
}
