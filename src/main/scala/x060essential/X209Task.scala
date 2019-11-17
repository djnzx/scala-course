package x060essential

object X209Task extends App {
  val subjects = List("Noel", "The cat", "The dog")
  val verbs = List("wrote", "chased", "slept on")
  val objects = List("the book","the ball","the bed")
  val total: Seq[String] = for {
    subject <- subjects
    verb <- verbs
    object_ <- objects
  } yield s"$subject $verb $object_"
  println(total)
}
