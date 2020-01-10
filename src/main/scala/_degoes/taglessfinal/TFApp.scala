package _degoes.taglessfinal

/**
  * https://typelevel.org/blog/2018/05/09/tagless-final-streaming.html
  */
object TFApp extends App {

  case class ItemName(value: String) extends AnyVal
  case class Item(name: ItemName, price: BigDecimal)

  trait ItemRepository[F[_]] {
    def findAll: F[List[Item]]
    def find(name: ItemName): F[Option[Item]]
    def save(item: Item): F[Unit]
    def remove(name: ItemName): F[Unit]
  }

  import doobie.implicits._
  import doobie.util.transactor.Transactor
  import cats.effect.Sync

  // Doobie implementation (not fully implemented, what matters here are the types).
  class PostgreSQLItemRepository[F[_]](xa: Transactor[F])(implicit F: Sync[F]) extends ItemRepository[F] {

    override def findAll: F[List[Item]] = sql"select name, price from items"
      .query[Item]
      .to[List](null)
      .transact(xa)

    override def find(name: ItemName): F[Option[Item]] = F.pure(None)
    override def save(item: Item): F[Unit] = F.unit
    override def remove(name: ItemName): F[Unit] = F.unit
  }


}
