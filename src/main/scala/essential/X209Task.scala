package essential

object X209Task extends App {
  val subjects = List("Noel", "The cat", "The dog")
  val verbs = List("wrote", "chased", "slept on")
  val objects = List("the book","the ball","the bed")
  val total= for {
    subj <- subjects;
    verb <- verbs;
    obj  <- objects } yield s"$subj $verb $obj"
  println(total)
}
