package fps

import scala.annotation.tailrec

object Fps011SimpleFpAppFlipCoin extends App {

  case class GameState(total: Int, right: Int) {
    def guessed: GameState = GameState(total + 1, right + 1)
    def wrong: GameState = GameState(total + 1, right)
  }

  def putStrLn(line: String): Unit = scala.Console.print(line)
  def getStrLn: String = scala.io.StdIn.readLine()
  def toss: String = scala.util.Random.nextInt(2) match {
    case 0 => "H"
    case _ => "T"
  }
  val finish = loop(GameState(0,0))

  @tailrec
  def loop(state: GameState): GameState = {
    putStrLn("Enter your choice:")
    val (state2, msg) = if (toss == getStrLn) (state.guessed, "You guessed right!")
                        else (state.wrong, "You guessed wrong:(")
    putStrLn(s"$msg\n")
    putStrLn("Do you want to continue (any key) or quit(q)?")
    getStrLn match {
      case "q" => state2
      case _ => loop(state2)
    }
  }

  println(s"Total: ${finish.total}, Right: ${finish.right}\n")
}
