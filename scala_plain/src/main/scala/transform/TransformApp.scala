package transform

object TransformApp extends App {

  trait Transform[F[_], G[_]] {
    def apply[A](fa: F[A]): G[A]
  }
  
  type ~>[F[_], G[_]] = Transform[F, G]
  
  implicit val oToL: Option ~> List = new (Option ~> List) {
    override def apply[A](fa: Option[A]): List[A] = fa.map(a => List(a)).getOrElse(List.empty)
  }
  
  val oi: Option[Int] = Option(1)
  
  def method[F[_], A](fa: F[A])(implicit fToL: F ~> List): A = {
    val ga: List[A] = fToL.apply(fa)
    ga.head
  }

  pprint.pprintln(oi)
  pprint.pprintln(oToL(oi))
  pprint.pprintln(method(oi))
  
}
