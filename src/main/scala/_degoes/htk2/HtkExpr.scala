package _degoes.htk2

object HtkExpr extends App {

  trait Expr[F[_]] {
    def int(v: Int): F[Int]
    def str(v: String): F[String]
    def add(l: F[Int], r: F[Int]): F[Int]
    def concat(l: F[String], r: F[String]): F[String]
  }

  trait Dsl[A] {
    def apply[F[_]](implicit F: Expr[F]): F[A]
  }

  // this stuff gives us ability to create integer, to represent it
  def int(v: Int): Dsl[Int] = new Dsl[Int] {
    override def apply[F[_]](implicit F: Expr[F]): F[Int] = F.int(v)
  }

  def add(l: Dsl[Int], r: Dsl[Int]): Dsl[Int] = new Dsl[Int] {
    override def apply[F[_]](implicit F: Expr[F]): F[Int] = F.add(l.apply[F], r.apply[F])
  }

  type Id[A] = A

  def interpret: Expr[Id] = new Expr[Id] {
    override def int(v: Int): Id[Int] = v
    override def str(v: String): Id[String] = v
    override def add(l: Id[Int], r: Id[Int]): Id[Int] = l + r
    override def concat(l: Id[String], r: Id[String]): Id[String] = l ++ r
  }

  val r: Id[Int] = add(int(1), int(3)).apply(interpret)

  final case class Const[A, B](a: A)

//  def serialize: Expr[Const[Json, ?]] = ???
//  def deserialize[F[_]: Expr](json: Json): F[A] forSome { type A } = ???
}
