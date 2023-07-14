package kubukoz.cats2

import cats.Order
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._

final case class ProductId(id: String) extends AnyVal
object ProductId {

  implicit val ord: Order[ProductId] = new Order[ProductId] {
    override def compare(x: ProductId, y: ProductId): Int = Ordering.String.compare(x.id, y.id)
  }

}

final case class Product(id: ProductId, name: String)
final case class Inventory(productId: ProductId, amount: Int)

object MapApplicative extends IOApp {

  def putStrLn(line: String): IO[Unit] = IO(println(line))

  def getProducts =
    putStrLn("Loading products").as(
      List(
        Product(ProductId("P1"), "Milk"),
        Product(ProductId("P2"), "Bread"),
        Product(ProductId("P3"), "Apple"),
      ),
    )

  def getInventory =
    putStrLn("Loading inventory").as(
      List(
        Inventory(ProductId("P1"), 2),
        Inventory(ProductId("P2"), 3),
        Inventory(ProductId("P2"), 4),
        Inventory(ProductId("P4"), 5),
      ),
    )

  override def run(args: List[String]): IO[ExitCode] = {
    (for {
      pp <- getProducts.map(_.groupByNel(_.id).fmap(_.head)) // SortedMap[ProductId, Product]
      inv <- getInventory.map(_.groupByNel(_.productId).fmap(_.reduceMap(_.amount))) // SortedMap[ProductId, Int]
    } yield pp
      .map { case (pId, product) =>
        (pId, (product, inv.get(pId)))
      }
      .collect { case (k, (p, Some(i))) =>
        (k, (p, i))
      }).flatMap { _.toList.fmap(_.toString).traverse_(putStrLn) }

  }.as(ExitCode.Success)

}
