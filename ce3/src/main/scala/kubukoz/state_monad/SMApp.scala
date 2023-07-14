package kubukoz.state_monad

import cats.data.State
import pprint.pprintln

object SMApp extends App {

  type Counter = Map[String, Int]
  object Counter {
    def empty = Map.empty[String, Int]
  }

  def countWord(word: String, counter: Counter): Counter =
    counter.updatedWith(word) {
      case Some(n) => Some(n + 1)
      case None => Some(1)
    }

  def countWordState(word: String): State[Counter, Unit] =
    State.modify[Counter](counter => countWord(word, counter))

  val id = State.modify[Counter](identity)

  def counterAllWords(words: Seq[String]) =
    words.foldLeft(id) { (s, word) =>
      s.flatMap(_ => countWordState(word))
    }

  val m = counterAllWords("ABCBCDCDD".toList.map(_.toString)).run(Counter.empty).value._1
  pprintln(m)

}
