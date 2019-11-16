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
//  println(total)
  val assoc1 = Map(
    "Noel" -> List("wrote", "chased", "slept on"),
    "The cat" -> List("meowed at", "chased", "slept on"),
    "The dog" -> List("barked at", "chased", "slept on")
  )
  val assoc2 = Map(
    "wrote" -> List("the book", "the letter", "the code"),
    "chased" -> List("the ball", "the dog", "the cat"),
    "slept on" -> List("the bed", "the mat", "the train"),
    "meowed at" -> List("Noel", "the door", "the food cupboard"),
    "barked at" -> List("the postman", "the car", "the cat"),
  )


}
