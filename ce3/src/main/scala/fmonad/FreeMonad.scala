package fmonad

/**
 * https://typelevel.org/cats/datatypes/freemonad.html
 * https://github.com/robinske
 */
object FreeMonad extends App {

  /** 0. create ADT to represent */
  sealed trait KVStoreA[A]
  case class Put[A](key: String, value: A) extends KVStoreA[Unit]
  case class Get[A](key: String) extends KVStoreA[Option[A]]
  case class Delete(key: String) extends KVStoreA[Unit]

  /** 1. Create a Free type based on your ADT */
  import cats.free.Free
  type KVStore[A] = Free[KVStoreA, A]

  /** 2. Create smart constructors using liftF */
  import cats.free.Free.liftF

  // Put returns nothing (i.e. Unit).
  def put[T](key: String, value: T): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put[T](key, value))

  // Get returns a T value.
  def get[T](key: String): KVStore[Option[T]] =
    liftF[KVStoreA, Option[T]](Get[T](key))

  // Delete returns nothing (i.e. Unit).
  def delete(key: String): KVStore[Unit] =
    liftF(Delete(key))

  // Update composes get and set, and returns nothing.
  def update[T](key: String, f: T => T): KVStore[Unit] =
    for {
      vMaybe <- get[T](key)
      _      <- vMaybe.map(v => put[T](key, f(v))).getOrElse(Free.pure(()))
    } yield ()

  /** 3. Build a program */
  def program: KVStore[Option[Int]] =
    for {
      _ <- put("wild-cats", 2)
      _ <- update[Int]("wild-cats", (_ + 12))
      _ <- put("tame-cats", 5)
      n <- get[Int]("wild-cats")
      _ <- delete("tame-cats")
    } yield n

  /** 4. Write a compiler for your program */
  import cats.Id
  import cats.arrow.FunctionK
  import cats.~>
  import scala.collection.mutable

  // the program will crash if a type is incorrectly specified.
  def impureCompiler: KVStoreA ~> Id =
    new (KVStoreA ~> Id) {

      // a very simple (and imprecise) key-value store
      val kvs = mutable.Map.empty[String, Any]

      def apply[A](fa: KVStoreA[A]): Id[A] =
        fa match {
          case Put(key, value) =>
            println(s"put($key, $value)")
            kvs(key) = value
            ()
          case Get(key)        =>
            println(s"get($key)")
            kvs.get(key).asInstanceOf[A]
          case Delete(key)     =>
            println(s"delete($key)")
            kvs.remove(key)
            ()
        }
    }

  /** 5. Run your program */
  val result1: Option[Int] = program.foldMap(impureCompiler)

  /** 6. Use a pure compiler (optional) */

  import cats.data.State

  type KVStoreState[A] = State[Map[String, Any], A]
  val pureCompiler: KVStoreA ~> KVStoreState = new (KVStoreA ~> KVStoreState) {
    def apply[A](fa: KVStoreA[A]): KVStoreState[A] =
      fa match {
        case Put(key, value) => State.modify(_.updated(key, value))
        case Get(key)        =>
          State.inspect(_.get(key).asInstanceOf[A])
        case Delete(key)     => State.modify(_ - key)
      }
  }
  val resultFn: KVStoreState[Option[Int]] = program.foldMap(pureCompiler)
  val (s, a) = resultFn.run(Map.empty).value
}
