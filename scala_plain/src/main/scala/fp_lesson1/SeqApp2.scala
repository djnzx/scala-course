package fp_lesson1

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SeqApp2 {

  trait HasMap2[F[_]] {
    def map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
    def pure[A](a: A): F[A]
  }
  
  implicit val map2opt: HasMap2[Option] = new HasMap2[Option] {
    override def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = for {
      a <- fa
      b <- fb
    } yield f(a, b)

    override def pure[A](a: A): Option[A] = Option(a)
  }
  
  implicit val map2fut: HasMap2[Future] = new HasMap2[Future] {
    override def map2[A, B, C](fa: Future[A], fb: Future[B])(f: (A, B) => C): Future[C] = 
    fa.flatMap(a => fb.map(b => f(a, b)))

    override def pure[A](a: A): Future[A] = Future.successful(a)
  }
  
  def sequence[F[_], A](list: List[F[A]])(implicit F: HasMap2[F]): F[List[A]] = list match {
    case Nil  => F.pure(List.empty[A]) 
    case h::t => F.map2(h, sequence(t))(_ :: _)
  }
  
  val data1: List[Option[Int]] = List(Option(1), Option(11), Option(111))
  val result1: Option[List[Int]] = sequence(data1)

  val data2: List[Future[Int]] = List(Future(1), Future(11), Future(111))
  val result2: Future[List[Int]] = sequence(data2)
  
}
