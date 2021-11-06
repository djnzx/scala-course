package degoes._problem

/**
  * http://degoes.net/articles/zio-environment
  */
object One extends App {

  // monad can be used in scala's for comprehension
  class IO[+A](val run: () => A) { me =>
//    def map   [B](f: A => B):     IO[B] = flatMap(f.andThen(IO.effect(_)))
    def map   [B](f: A => B):     IO[B] = new IO( () => f(me.run()))
    def flatMap[B](f: A => IO[B]): IO[B] = new IO( () => f(me.run()).run())
//    IO.effect(f(me.run()).run())
  }
//  object IO {
//    def effect[A](eff: => A) = new IO(() => eff)
//  }
//  class IO[+A](val run: () => A) { me =>
//    def map   [B](f: A => B):     IO[B] = flatMap(f.andThen(IO.effect(_)))
//    def flatMap[B](f: A => IO[B]): IO[B] = IO.effect(f(me.run()).run())
//  }
//  object IO {
//    def effect[A](eff: => A) = new IO(() => eff)
//  }

  def putStrLn(line: String): IO[Unit] = new IO(()=>println(line))
  val getStrLn: IO[String] = new IO(() => scala.io.StdIn.readLine())
//  def putStrLn(line: String): IO[Unit] = IO.effect(println(line))
//  val getStrLn: IO[String] = IO.effect(scala.io.StdIn.readLine())

  val program: IO[String] =
    for {
      _    <- putStrLn("Good morning, what's your name?")
      name <- getStrLn
      _    <- putStrLn(s"Great to meet you, $name")
    } yield name

  program.run()

}
