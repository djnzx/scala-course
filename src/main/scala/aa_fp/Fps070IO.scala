package aa_fp

object Fps070IO extends App {

  val app: XIO[Unit] = for {
    _ <- putLn("Hello, what's your name?")
    s <- getLn
    _ <- putLn(s"Hello, $s")
  } yield ()

  def runner[A](main: XIO[A]): A = {
    main.run
  }

  runner(app)

}
