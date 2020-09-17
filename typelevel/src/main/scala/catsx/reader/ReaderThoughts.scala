package catsx.reader

import cats.Monad
import cats.data.Reader

object ReaderThoughts {

  def x2(x: Int) = x * 2
  def plus1(x: Int) = x + 1
  
  val a = 5
  
  val b: Int = x2(a)
  val c: Int = plus1(a)
  val d: (Int, Int) = (b, c)

  def composition1[A,B,C](f: A => B, g: A => C) = { a: A =>
    (f(a), g(a))
  }

  def composition2[A,B,C](f: A => B, g: B => C) = { a: A =>
    g(f(a))
  }
  
  def composition3[F[_]: Monad, A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = { a: A =>
    val fb: F[B] = f(a)
    val r: F[C] = F.flatMap(fb)(g)
    r
  } 
  
  val r1: Reader[Int, Int] = Reader(x2)
  val r2: Reader[Int, Int] = Reader(plus1)
  
  val r3: Reader[Int, (Int, Int)] = for {
    b1 <- r1
    c1 <- r2
  } yield (b1, c1)
  
  val d1: (Int, Int) = r3(5)
  
}
