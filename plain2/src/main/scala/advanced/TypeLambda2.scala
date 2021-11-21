package advanced

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/** https://blog.adilakhter.com/2015/02/18/applying-scalas-type-lambda/ */
object TypeLambda2 extends App {

  trait HList {
    type Hd
  }

  class IntList extends HList {
    type Hd = Int
  }

  type IntMap[V] = Map[Int, V]

  /** we can check whether these types are the same */
  implicitly[Int =:= IntList#Hd]

  /** will not compile */
//  implicitly[Int =:= Double]

  val x1: IntList#Hd = 10

  trait Functor[A, +M[_]] {
    def map[B](f: A => B): M[B]
  }

  class SeqFunctor[A](seq: Seq[A]) extends Functor[A, Seq] {
    override def map[B](f: A => B): Seq[B] = seq.map(f)
  }

  class MapFunctor1[K, V](mapKV: Map[K, V]) extends Functor[V, ({ type L[a] = Map[K, a] })#L] {
    override def map[V2](f: V => V2): Map[K, V2] = mapKV map { case (k, v) => (k, f(v)) }
  }

  class MapFunctor2[K, V](mapKV: Map[K, V]) extends Functor[V, Map[K, *]] {
    override def map[V2](f: V => V2): Map[K, V2] = mapKV map { case (k, v) => (k, f(v)) }
  }

  class MapFunctor3[K, V](mapKV: Map[K, V]) {

    type MapK[V2] = Map[K, V2]

    def mkFunctor: Functor[V, MapK] = new Functor[V, MapK] {
      override def map[V2](f: V => V2): MapK[V2] = mapKV map { case (k, v) => (k, f(v)) }
    }

  }

  class MapFunctor4[K, V](mapKV: Map[K, V]) {

    def mkFunctor = {

      type MapK[V2] = Map[K, V2]

      new Functor[V, MapK] {
        override def map[V2](f: V => V2): MapK[V2] = mapKV map { case (k, v) => (k, f(v)) }
      }
    }

  }

}

class TypeLambda2Spec extends AnyFunSpec with Matchers {

  import TypeLambda2._

  it("3") {
    val f: Functor[Int, MapFunctor3[String, Int]#MapK] = new MapFunctor3(Map("a" -> 1)).mkFunctor
    f.map(_ * 10) shouldEqual Map("a" -> 10)
  }

  it("4") {

    /** in this case we can't write signature */
    val f = new MapFunctor4(Map("a" -> 1)).mkFunctor
    f.map(_ * 10) shouldEqual Map("a" -> 10)
  }

}
