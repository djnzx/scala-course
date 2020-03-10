package cats.reader

object ReaderApp1 extends App {

  object RMonad {
    case class Reader[A, B](f: A => B) {
      def apply(input: A): B = f(input)
      def map[C]   (g: B => C)           : Reader[A, C] = Reader { a => g(f(a)) }
      def flatMap[C](g: B => Reader[A, C]): Reader[A, C] = Reader { a => g(f(a))(a) }
    }
    def pure[A, B](b: B)                 : Reader[A, B] = Reader { _ => b }
  }

  trait StockRepo {
    def findAll(): Map[String, Double]
    def sell(stock: String, quantity: Double): Double
    def buy(stock: String, amount: Double): Double
  }

  // plain API
  object Stocks1 {
    def findAll(repo: StockRepo): Map[String, Double] = repo.findAll()
    def sell(stock: String, quantity: Double, repo: StockRepo): Double = repo.sell(stock, quantity)
    def buy(stock: String, amount: Double, repo: StockRepo): Double = repo.buy(stock, amount)
  }

  // currying, and extracting Repo to lat parameter
  object Stocks2 {
    def findAll()(repo: StockRepo): Map[String, Double] = repo.findAll()
    def sell(stock: String, quantity: Double)(repo: StockRepo): Double = repo.sell(stock, quantity)
    def buy(stock: String, amount: Double)(repo: StockRepo): Double = repo.buy(stock, amount)
  }

  // high-order function, the same idea, but we don't have `repo: StockRepository` parameter
  object Stocks3 {
    def findAll(): StockRepo => Map[String, Double] = repo => repo.findAll()
    def sell(stock: String, quantity: Double): StockRepo => Double = repo => repo.sell(stock, quantity)
    def buy(stock: String, amount: Double): StockRepo => Double = repo => repo.buy(stock, amount)
  }

  // take amount, return function: Repo => Unit
  def investInStockWithMinValue(amount: Double): StockRepo => Unit =
    (repo: StockRepo) =>
      Stocks3.findAll()
        .andThen (s => s.minBy(_._2))
        .andThen { case (stock, _) => stock }
        .andThen (s => Stocks3.buy(s, amount)(repo))

  // you can use any of following deps
  import RMonad.{Reader => XReader}
  import cats.data.Reader
  import cats.implicits._


  // my syntax
  object XStocks {
    def findAll()                             : XReader[StockRepo, Map[String, Double]] = XReader { rp => rp.findAll() }
    def sell(stock: String, quantity: Double): XReader[StockRepo, Double] = XReader { rp => rp.sell(stock, quantity) }
    def buy(stock: String, amount: Double)   : XReader[StockRepo, Double] = XReader { rp => rp.buy(stock, amount) }
  }

  // cats syntax
  object Stocks {
    def findAll()                             : Reader[StockRepo, Map[String, Double]] = Reader { rp => rp.findAll() }
    def sell(stock: String, quantity: Double): Reader[StockRepo, Double] = Reader { rp => rp.sell(stock, quantity) }
    def buy(stock: String, amount: Double)   : Reader[StockRepo, Double] = Reader { rp => rp.buy(stock, amount) }
  }

  // We have our function, let’s say f: From => To
  // it can be represented: case class Reader[From, To](f: From => To) {...}
  // def apply(input: From): To = f(input)

  def investInCheapestStock(amount: Double): XReader[StockRepo, Double] =
    XStocks.findAll()
      .map(stocks => stocks.minBy(_._2))
      .map { case (stock, _) => stock }
      .flatMap(stock => XStocks.buy(stock, amount))

  def investInCheapestStockForCats(amount: Double): Reader[StockRepo, Unit] =
    for {
      stocks   <- Stocks.findAll()
      minStock <- Reader[StockRepo, String](_ => stocks.minBy(_._2)._1)
      _        <- Stocks.buy(minStock, amount)
    } yield ()



}

