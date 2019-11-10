package x060essential

object X148 extends App {
  sealed trait Maybe[A] {
    def map[B](fn: A => B): Maybe[B] = this match {
      case Full(v) => Full(fn(v))
      case Empty() => Empty[B]()
    }
    def flatMap[B](fn: A => Maybe[B]): Maybe[B] = this match {
      case Full(v) => fn(v)
      case Empty() => Empty[B]()
    }
  }
  final case class Full[A](value: A) extends Maybe[A]
  final case class Empty[A]() extends Maybe[A]

  def mightFail1: Maybe[Int] = Full(1)
  def mightFail2: Maybe[Int] = Full(2)
  def mightFail3: Maybe[Int] = Empty()

  val combined: Maybe[Int] =
    mightFail1.flatMap(x =>
      mightFail2.flatMap(y =>
        mightFail3.flatMap(z =>
          Full(x + y + z)
        )))
  println(combined)
  val combined2: Maybe[Int] = for {
    x <- mightFail1
    y <- mightFail2
    z <- mightFail3
  } yield x+y+z
  println(combined2)

  val list: List[Maybe[Int]] = List(Full(3), Full(2), Full(1))
  val list2: Seq[Maybe[Int]] = list.map(maybe => maybe.flatMap[Int]( x => if (x % 2 == 0) Full(x) else Empty() ))
  val list3: Seq[Maybe[Int]] = list.map(maybe => maybe.flatMap[Int]{ x => if (x % 2 == 0) Full(x) else Empty() })
  println(list2)
  println(list3)
}
