package context

object TransformingContext extends App {

  /** normal definition */
  trait Transform[F[_], G[_]] {
    def apply[A](fa: F[A]): G[A]
  }

  /** type alias */
  type ~>[F[_], G[_]] = Transform[F, G]

  /** implicit instances */
  implicit val optionToList: Option ~> List = new (Option ~> List) {
    override def apply[A](fa: Option[A]): List[A] = fa.map(a => List(a)).getOrElse(List.empty)
  }
  implicit val listToOption: List ~> Option = new (List ~> Option) {
    override def apply[A](fa: List[A]): Option[A] = fa.headOption
  }

  /** we have no information about context F[_], but we can convert it to G[_] */
  def method1[F[_], A](fa: F[A])(implicit fToList: F ~> List) = {
    val la: List[A] = fToList.apply(fa)
    ???
  }
  def method2[F[_], G[_], A](fa: F[A])(implicit fToG: F ~> G): A = {
    val ga: G[A] = fToG(fa)
    ???
  }

}
