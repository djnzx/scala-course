package degoes.beescala2019

/**
  * https://www.youtube.com/watch?v=xpz4rf1RS8c
  */
object A001 extends App {
  // side effect
  println(1)
  println(2)
  // no way to compose
  // no way to test

  // ADT 2
  sealed trait Console[+A]
  final case class Return[A](value: A) extends Console[A]
  final case class Print[A](line: String, k: Console[A]) extends Console[A]
  final case class Read[A](k: String => Console[A]) extends Console[A]

  final def run[A](console: Console[A]): A = console match {
    case Return(v) => v
    case Print(line, k) => println(line); run(k)
    case Read(k) => run(k(scala.io.StdIn.readLine()))
  }

}
