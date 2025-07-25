package fmonad

import cats.implicits.catsSyntaxOptionId
import fmonad.FreeMonad.Delete
import fmonad.FreeMonad.Get
import fmonad.FreeMonad.KVStoreA
import fmonad.FreeMonad.Put
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** https://typelevel.org/cats/datatypes/freemonad.html
  * https://github.com/robinske
  */
object FreeMonad {

  /** 0. create ADT to represent */
  sealed trait KVStoreA[A]
  case class Put[A](key: String, value: A) extends KVStoreA[Unit]
  case class Get[A](key: String)           extends KVStoreA[Option[A]]
  case class Delete(key: String)           extends KVStoreA[Unit]

  /** 1. Create a Free type based on your ADT */
  import cats.free.Free
  type KVStore[A] = Free[KVStoreA, A]

  /** 2. Create smart constructors using liftF */
  import cats.free.Free.liftF

  def put[A](key: String, value: A): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put[A](key, value))

  def get[A](key: String): KVStore[Option[A]] =
    liftF[KVStoreA, Option[A]](Get[A](key))

  def delete(key: String): KVStore[Unit] =
    liftF(Delete(key))

  def update[A](key: String, f: A => A): KVStore[Unit] =
    for {
      vMaybe <- get[A](key)
      _      <- vMaybe.map(v => put[A](key, f(v))).getOrElse(Free.pure(()))
    } yield ()

  /** 3. Build a program */
  def program: KVStore[Option[Int]] =
    for {
      _ <- put("wild-cats", 2)
      _ <- update[Int]("wild-cats", x => x + 12)
      _ <- put("tame-cats", 5)
      n <- get[Int]("wild-cats")
      _ <- delete("tame-cats")
    } yield n

}

object ImpureCompiler {

  import cats.Id
  import cats.~>
  import scala.collection.mutable

  /** 4. Compiler, impure */
  def impureCompiler: KVStoreA ~> Id = new (KVStoreA ~> Id) {

    val kvs = mutable.Map.empty[String, Any]

    def apply[A](fa: KVStoreA[A]): Id[A] =
      fa match {

        case Put(key, value) =>
          println(s"put($key, $value)")
          kvs(key) = value
          ()

        case Get(key) =>
          println(s"get($key)")
          kvs.get(key)

        case Delete(key) =>
          println(s"delete($key)")
          kvs.remove(key)
          ()
      }
  }

}

object PureCompiler {

  import cats.data.State
  import cats.~>

  type KVStoreState[A] = State[Map[String, Any], A]

  /** 6. Compiler, pure */
  def pureCompiler: KVStoreA ~> KVStoreState = new (KVStoreA ~> KVStoreState) {

    def apply[A](fa: KVStoreA[A]): KVStoreState[A] =
      fa match {
        case Put(key, value) => State.modify(m => m.updated(key, value))
        case Get(key)        => State.inspect(m => m.get(key))
        case Delete(key)     => State.modify(m => m - key)
      }

  }

}

class FreeMonadTest extends AnyFunSuite with Matchers {
  import FreeMonad._

  test("impure") {
    import ImpureCompiler._

    /** 5. Run your program */
    val x: Option[Int] = program.foldMap(impureCompiler)
    pprint.log(x)
    x shouldBe 14.some
  }

  test("pure") {
    import PureCompiler._

    val resultFn: KVStoreState[Option[Int]] = program.foldMap(pureCompiler)
    val (s, a) = resultFn.run(Map.empty).value

    pprint.log(s)
    pprint.log(a)

    a shouldBe 14.some
    s shouldBe Map("wild-cats" -> 14)
  }

}
