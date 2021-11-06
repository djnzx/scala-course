package fp_lesson1

object SeqApp0 {

  def sequence[A](list: List[Option[A]]): Option[List[A]] = ???
  
  val data:   List[Option[Int]] = List(Option(1), Option(11), Option(111))
  val result: Option[List[Int]] = sequence(data)
  
}
