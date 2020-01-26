package degoes.taglessfinal

/**
  * https://typelevel.org/blog/2017/12/27/optimizing-final-tagless.html
  */
object TFApp0 extends App {

  trait KVStore[F[_]] {
    def get(key: String): F[Option[String]]
    def put(key: String, a: String): F[Unit]
  }

  import cats._
  import cats.implicits._

  def program[M[_]: FlatMap, F[_]](a: String)(K: KVStore[M])(implicit P: Parallel[M]) =
    for {
      _ <- K.put("A", a)
      x <- (K.get("B"), K.get("C")).parMapN(_ |+| _)
      _ <- K.put("X", x.getOrElse("-"))
    } yield x

  def program[F[_]: Apply](F: KVStore[F]): F[List[String]] =
    (
      F.get("Cats"),
      F.get("Dogs"),
      F.put("Mice", "42"),
      F.get("Cats")
    ).mapN((f, s, _, t) => List(f, s, t).flatten)


}
