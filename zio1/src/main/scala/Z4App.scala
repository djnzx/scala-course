import zio._

object F1Syntax {
  implicit class ProvideParameter[R, A](f: R => A) {
    def provide(r: R): Any => A = _ => f(r)
  }
}

object Z4App extends App {

  import F1Syntax._

  trait Google {
    def countPicturesOf(topic: String): Int
  }

  trait BusinessLogic {
    def isEven(topic: String): Boolean
  }

  object BusinessLogic {
    lazy val live: Google => BusinessLogic = google => make(google)

    def make(google: Google): BusinessLogic =
      new BusinessLogic {
        override def isEven(topic: String): Boolean = google.countPicturesOf(topic) % 2 == 0
      }
  }

  object GoogleImpl {
    lazy val live: Any => Google = _ => make

    lazy val make = new Google {
      override def countPicturesOf(topic: String): Int = if (topic == "cats") 42 else 43
    }
  }

  object DependencyGraph {
    lazy val live: Any => BusinessLogic = _ => {
      val google: Google = GoogleImpl.live.apply(())
      val logicMaker = BusinessLogic.live.provide(google)
      logicMaker(())
    }

    lazy val make = {
      val google = GoogleImpl.make
      val logic = BusinessLogic.make(google)
      logic
    }
  }

  lazy val logic = DependencyGraph.live.apply(())
//  lazy val logic = DependencyGraph.make

  def app = for {
    _ <- badConsole.putStrLn(logic.isEven("cats").toString)
    _ <- badConsole.putStrLn(logic.isEven("dogs").toString)
    _ <- badConsole.putStrLn("Hell")
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}

object TheMainIdea extends scala.App {

  import F1Syntax._

  /** here we have f: Int => Int */
  val inc: Int => Int = _ + 1
  /** here we have f: Any => Int, but with parameter provided */
  val provided: Any => Int = inc.provide(10)

  val result: Int = provided(())

  println(result)

}