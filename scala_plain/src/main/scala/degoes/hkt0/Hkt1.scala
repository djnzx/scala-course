package degoes.hkt0

object Hkt1 extends App {

  val x1: List[Int] = List(1,2,3)
  val x2: List[_] = List(1,2,3)

  println(x1)
  println(x2)

  trait WithMap[F[_]] {
    def map[A,B](fa: F[A])(f: A => B): F[B]
  }

//  new WithMap[List[Int]] {
//    override def map[A, B](fa: List[A])(f: A => B): List[B] = ???
//  }



}
