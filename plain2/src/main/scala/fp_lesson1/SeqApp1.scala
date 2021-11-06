package fp_lesson1

object SeqApp1 {

  def map2[A,B,C](oa: Option[A], ob: Option[B])(f: (A, B) => C): Option[C] = for {
    a <- oa
    b <- ob
  } yield f(a, b)
  
  def sequence[A](list: List[Option[A]]): Option[List[A]] = list match {
    case Nil  => Some(List.empty[A]) 
    case h::t => map2(h, sequence(t))(_ :: _)
  }
  
  val data: List[Option[Int]] = List(Option(1), Option(11), Option(111))
  val result: Option[List[Int]] = sequence(data)
  
}
