package catsx.reader

import cats.data.Reader

object ReaderChainingAndThan extends App {

  val r1 = Reader { a: Int => Thread.sleep(2000); a + 1 }
  val r2 = Reader { a: Int => Thread.sleep(4000); a * 2 }
  /**
    * Reader chaining
    * (A => B, B => C) => A => C
    */
  val r12 = r1 andThen r2

  val was1 = System.currentTimeMillis()
  val r = r12(1000) // (1000 + 1) * 2 => 2002
  val delta1 = System.currentTimeMillis() - was1
  println(delta1) // 6011
  println(r)
  
  

}
