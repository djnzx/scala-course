package freex

import cats.free.Free
import cats.free.Free.liftF
import cats.Id
import cats.~>

import scala.collection.mutable

object FreeApp extends App {
  val m = mutable.Map.empty[Int, String]

  m.put(1, "Jim")
  m.put(2, "Beam")

  sealed trait KVStoreA[A]
  case class Put[V](key: Int, value: V) extends KVStoreA[Unit]
  case class Get[V](key: Int) extends KVStoreA[Option[V]]
  case class Delete(key: Int) extends KVStoreA[Unit]

  type KVStore[A] = Free[KVStoreA, A]

  // Put returns nothing (i.e. Unit).
  def put[T](key: Int, value: T): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put[T](key, value))

  // Get returns a T value.
  def get[T](key: Int): KVStore[Option[T]] =
    liftF[KVStoreA, Option[T]](Get[T](key))

  // Delete returns nothing (i.e. Unit).
  def delete(key: Int): KVStore[Unit] =
    liftF(Delete(key))

  // Update composes get and set, and returns nothing.
  def update[T](key: Int, f: T => T): KVStore[Unit] =
    for {
      vMaybe <- get[T](key)
      _ <- vMaybe.map(v => put[T](key, f(v))).getOrElse(Free.pure(()))
    } yield ()

  def program: KVStore[Option[String]] =
    for {
      _ <- put(1, "Jim")
      _ <- update[String](1, _.toUpperCase)
      _ <- put(2, "Beam")
      n <- get[String](2)
      _ <- delete(2)
    } yield n

  def impureCompiler: KVStoreA ~> Id =
    new (KVStoreA ~> Id) {

      // a very simple (and imprecise) key-value store
      val kvs = mutable.Map.empty[Int, Any]

      def apply[A](fa: KVStoreA[A]): Id[A] = fa match {
        case Put(key, value) =>
          println(s"put($key, $value)")
          kvs(key) = value
          ()
        case Get(key) =>
          println(s"get($key)")
          kvs.get(key).map(_.asInstanceOf[A])
        case Delete(key) =>
          println(s"delete($key)")
          kvs.remove(key)
          ()
      }
    }
  pprint.pprintln(m.get(1))
  pprint.pprintln(m.get(3))
}
