package _degoes.hangman

object Hangman {
//  trait Console[F[_]] {
//    def printLine(line: String): F[Unit]
//
//    def readLine: F[String]
//  }
//  object Console {
//    def apply[F[_]](implicit F: Console[F]): Console[F] = F
//  }
//
//  //
//  // EXERCISE 5
//  //
//  // Implement helper methods called `printLine` and `readLine` that work with
//  // any `F[_]` that supports the `Console` effect.
//  //
//  def printLine[F[_]: Console](line: String): F[Unit] =
//    Console[F].printLine(line)
//  def readLine[F[_]: Console]: F[String] =
//    Console[F].readLine
//
//  //
//  // EXERCISE 6
//  //
//  // Create an instance of the `Console` type class for `IO[E, ?]` for any `E`
//  // by using `IO.sync` and the Scala functions `println` and
//  // `scala.io.StdIn.readLine`.
//  //
//  implicit def ConsoleIO[E]: Console[IO[E, ?]] =
//    new Console[IO[E, ?]] {
//      def printLine(line: String): IO[E, Unit] =
//        IO.sync(println(line))
//      def readLine: IO[E, String] =
//        IO.sync(scala.io.StdIn.readLine())
//    }
//
//  //
//  // EXERCISE 7
//  //
//  // Create an instance of the `Random` type class for `IO[E, ?]` for any `E`
//  // by using `IO.sync` and `scala.util.Random.nextInt`.
//  //
//  trait Random[F[_]] {
//    def nextInt(max: Int): F[Int]
//  }
//  object Random {
//    def apply[F[_]](implicit F: Random[F]): Random[F] = F
//  }
//  def nextInt[F[_]: Random](max: Int): F[Int] = Random[F].nextInt(max)
//  implicit def RandomIO[E]: Random[IO[E, ?]] =
//    new Random[IO[E, ?]] {
//      def nextInt(max: Int): IO[E, Int] =
//        IO.sync(scala.util.Random.nextInt(max))
//    }
//
//  //
//  // EXERCISE 8
//  //
//  // Create a hangman game that is polymorphic in the effect type `F[_]`,
//  // requiring only the capability to perform `Console` and `Random` effects.
//  //
//  def myGame[F[_]: Console: Random: Monad]: F[Unit] =
//    for {
//      _     <- printLine[F]("Welcome to Purely Functional Hangman! (TM)")
//      name  <- getName[F]
//      word  <- chooseWord[F]
//      state <- State(name, Set(), word).point[F]
//      _     <- renderState[F](state)
//      _     <- gameLoop[F](state)
//    } yield ()
//
//  case class State(name: String, guesses: Set[Char], word: String) {
//    def failures: Int = (guesses -- word.toSet).size
//
//    def playerLost: Boolean = guesses.size > (word.length * 2)
//
//    def playerWon: Boolean = (word.toSet -- guesses).size == 0
//  }
//
//  def gameLoop[F[_]: Console: Monad](state: State): F[State] =
//    for {
//      char  <- getChoice[F]
//      state <- state.copy(guesses = state.guesses + char).point[F]
//      _     <- renderState[F](state)
//      _     <- if (state.playerLost) printLine[F]("Sorry, you lost, " + state.name + "!")
//      else if (state.playerWon) printLine[F]("Congratulations, you won, " + state.name + "!")
//      else (if (state.word.toSet.contains(char)) printLine[F]("You guessed right, " + state.name + "! Keep going!")
//      else printLine[F]("You guessed wrong, but keep trying, " + state.name + "!")) *>
//        gameLoop[F](state)
//
//    } yield state
//
//  def renderState[F[_]: Console](state: State): F[Unit] = {
//    //
//    //  f     n  c  t  o
//    //  -  -  -  -  -  -  -
//    //
//    //  Guesses: a, z, y, x
//    //
//    val word =
//    state.word.toList.map(c =>
//      if (state.guesses.contains(c)) s" $c " else "   ").mkString("")
//
//    val line = List.fill(state.word.length)(" - ").mkString("")
//
//    val guesses = " Guesses: " + state.guesses.mkString(", ")
//
//    val text = word + "\n" + line + "\n\n" + guesses + "\n"
//
//    printLine[F](text)
//  }
//
//  def getChoice[F[_]: Console: Monad]: F[Char] =
//    printLine[F]("Please guess a letter: ") *>
//      readLine[F].flatMap(line =>
//        line.trim.headOption match {
//          case Some(c) if (c.isLetter) => c.point[F]
//          case _ => printLine[F]("Sorry, that is not a letter! Please try again.") *>
//            getChoice[F]
//        }
//      )
//
//  def getName[F[_]: Console: Apply]: F[String] =
//    printLine[F]("What is your name?") *>
//      readLine[F]
//
//  def chooseWord[F[_]: Random: Functor]: F[String] =
//    nextInt[F](Dictionary.length).map(Dictionary(_))
}
