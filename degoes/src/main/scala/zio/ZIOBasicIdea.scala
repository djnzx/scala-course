package degoes.zio

object ZIOBasicIdea extends App {

  // side effect
  println("Hello")

  // functional effect
  case class Task[A](run: () => A)
  object Task {
    def point[A](a: => A): Task[A] = new Task[A](() => a)
  }

  val repr: Task[Unit] = Task.point(println("Hello"))
  val next: () => Unit = repr.run
  next()

}
