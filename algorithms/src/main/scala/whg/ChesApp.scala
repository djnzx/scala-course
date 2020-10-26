package whg

object ChesApp extends App {
  
  val c = Chess.initial
  println(c.rep)
  println
  
  c.play(Seq(
    "e2e4",
    "e7e5",
    "f1c4",
    "b8c6",
    "d1f3",
    "d7d6",
    "f3f7",
  ))

}
