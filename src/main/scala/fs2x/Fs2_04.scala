package fs2x

import cats.effect.IO
import fs2.Stream

object Fs2_04 extends App {
  Stream(1,0).repeat.take(6).toList
  // List[Int] = List(1, 0, 1, 0, 1, 0)
  Stream(1,2,3).drain.toList
  // List[Nothing] = List()
  Stream.eval_(IO(println("!!"))).compile.toVector.unsafeRunSync()
  // Vector[Nothing] = Vector()
  (Stream(1,2) ++ Stream(3).map(_ => throw new Exception("nooo!!!"))).attempt.toList
  // List[Either[Throwable, Int]] = List(Right(1), Right(2), Left(Left(java.lang.Exception: nooo!!!)))
}
