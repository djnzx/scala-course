package advanced

/** https://blog.adilakhter.com/2015/02/18/applying-scalas-type-lambda/
  * https://thenewcircle.com/s/post/665/a_more_readable_type_lambda_trick
  * https://gist.github.com/adilakhter/82f5bb73cf1d7b5672a9
  */
object TypeLambda2 extends App {

  trait HList {
    type Hd
  }

  class IntList extends HList {
    type Hd = Int
  }

  type IntMap[V] = Map[Int, V]

  implicitly[Int =:= IntList#Hd]

  val x: IntList#Hd = 10

  trait Functor[A, +M[_]] {
    def map[B](f: A => B): M[B]
  }

  case class SeqFunctor[A](seq: Seq[A]) extends Functor[A, Seq] {
    override def map[B](f: A => B): Seq[B] = seq.map(f)
  }

  case class MapFunctor[K, V](mapKV: Map[K, V]) extends Functor[V, ({ type L[a] = Map[K, a] })#L] {
    override def map[V2](f: V => V2): Map[K, V2] = mapKV map { case (k, v) =>
      (k, f(v))
    }
  }

  case class ReadableMapFunctor[K, V](mapKV: Map[K, V]) {
    def mapFunctor[V2] = {
      type `Map[K]`[V2] = Map[K, V2]

      new Functor[V, `Map[K]`] {
        override def map[V2](f: V => V2): `Map[K]`[V2] = mapKV map { case (k, v) =>
          (k, f(v))
        }
      }
    }
  }

}
