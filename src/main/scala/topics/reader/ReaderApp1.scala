package topics.reader

object ReaderApp1 extends App {

  object RMonad {
    case class Reader[A, B](f: A => B) {
      def apply(input: A): B = f(input)
      def map[C]   (g: B => C)           : Reader[A, C] = Reader { a => g(f(a)) }
      def flatMap[C](g: B => Reader[A, C]): Reader[A, C] = Reader { a => g(f(a))(a) }
    }
    def pure[A, B](b: B): Reader[A, B] = Reader((_: A) => b)
  }

  trait StockRepository {
    def findAll(): Map[String, Double]
    def sell(stock: String, quantity: Double): Double
    def buy(stock: String, amount: Double): Double
  }

  object Stocks0 {
    def findAll(repo: StockRepository): Map[String, Double] = repo.findAll()
    def sell(stock: String, quantity: Double, repo: StockRepository): Double = repo.sell(stock, quantity)
    def buy(stock: String, amount: Double, repo: StockRepository): Double = repo.buy(stock, amount)
  }

  // currying
  object Stocks2 {
    def findAll()(repo: StockRepository): Map[String, Double] = repo.findAll()
    def sell(stock: String, quantity: Double)(repo: StockRepository): Double = repo.sell(stock, quantity)
    def buy(stock: String, amount: Double)(repo: StockRepository): Double = repo.buy(stock, amount)
  }

  // high-order function, the same idea, but we don't have `repo: StockRepository` parameter
  object Stocks3 {
    def findAll(): StockRepository => Map[String, Double] = repo => repo.findAll()
    def sell(stock: String, quantity: Double): StockRepository => Double = repo => repo.sell(stock, quantity)
    def buy(stock: String, amount: Double): StockRepository => Double = repo => repo.buy(stock, amount)
  }

  def investInStockWithMinValue(amount: Double): StockRepository => Unit =
    (repo: StockRepository) => {
      val investment = Stocks3.findAll()
        .andThen (s => s.minBy(_._2))
        .andThen { case (stock, _) => stock }
        .andThen (s => Stocks3.buy(s, amount)(repo))
    }

  import RMonad._

  object Stocks {
    def findAll(): Reader[StockRepository, Map[String, Double]] = Reader {
      repo => repo.findAll()
    }
    def sell(stock: String, quantity: Double): Reader[StockRepository, Double] = Reader {
      repo => repo.sell(stock, quantity)
    }
    def buy(stock: String, amount: Double): Reader[StockRepository, Double] = Reader {
      repo => repo.buy(stock, amount)
    }
  }

  // We have our function, let’s say f: From => To
  // it can be represented: case class Reader[From, To](f: From => To) {...}
  // def apply(input: From): To = f(input)

  def investInStockWithMinValue(amount: Double): Reader[StockRepository, Double] =
    Stocks.findAll()
      .map(stocks => stocks.minBy(_._2))
      .map { case (stock, _) => stock }
      .flatMap(stock => Stocks.buy(stock, amount))

  def investInStockWithMinValueUsingForComprehension(amount: Double): Reader[StockRepository, Unit] =
    for {
      stocks <- Stocks.findAll()
      minStock <- RMonad.pure(stocks.minBy(_._2)._1)
      _ <- Stocks.buy(minStock, amount)
    } yield ()

}

