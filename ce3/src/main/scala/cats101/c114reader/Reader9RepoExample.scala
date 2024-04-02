package cats101.c114reader

object Reader9RepoExample extends App {

  /** here is an classical API example */
  trait StockRepo {
    def findAll(): Map[String, Double]
    def sell(stock: String, quantity: Double): Double
    def buy(stock: String, amount: Double): Double
  }

  /**
    * and to use it, we add the repo as the last parameter of our functions
    * but the problem is: we need to have [[StockRepo]] to create the chains of operation
    * WE DON'T have it at that moment
    */
  object StocksV1 {
    def findAll(repo: StockRepo): Map[String, Double] = repo.findAll()
    def sell(stock: String, quantity: Double, repo: StockRepo): Double = repo.sell(stock, quantity)
    def buy(stock: String, amount: Double, repo: StockRepo): Double = repo.buy(stock, amount)
  }

  /**
    * we can do currying, put [[StockRepo]] as a last parameter and deal with it
    */
  object StocksV2 {
    def findAll()(repo: StockRepo): Map[String, Double] = repo.findAll()
    def sell(stock: String, quantity: Double)(repo: StockRepo): Double = repo.sell(stock, quantity)
    def buy(stock: String, amount: Double)(repo: StockRepo): Double = repo.buy(stock, amount)
  }

  /**
    * what we have is:
    * calling function w.o [[StockRepo]] we get back
    * a function StockRepo => A 
    */
  val fa: StockRepo => Map[String, Double] = StocksV2.findAll()
  val se: StockRepo => Double              = StocksV2.sell("APPLE", 10)
  val bu: StockRepo => Double              = StocksV2.buy("GOOGLE", 5)

  /**
    * we can eliminate extra step and represent calls
    * via high-order function:
    */
  object StocksV3 {
    def findAll():                              StockRepo => Map[String, Double] = repo => repo.findAll()
    def sell(stock: String, quantity: Double): StockRepo => Double              = repo => repo.sell(stock, quantity)
    def buy(stock: String, amount: Double):    StockRepo => Double              = repo => repo.buy(stock, amount)
  }
  
  /**
    * and eliminate repo parameter [[StockRepo]]
    */
  object StocksV4 {
    def findAll():                              StockRepo => Map[String, Double] = _.findAll()
    def sell(stock: String, quantity: Double): StockRepo => Double              = _.sell(stock, quantity)
    def buy(stock: String, amount: Double):    StockRepo => Double              = _.buy(stock, amount)
  }

  /**
    * plain implementation
    * we pass [[StockRepo]] several times
    * UGLY...
    */
  def investInStockWithMinValueV1(amount: Double): StockRepo => Double = {
    (rp: StockRepo) =>
      // find all
      val sts: Map[String, Double] = StocksV4.findAll()(rp)
      // find the cheapest stock
      val (stock, _): (String, Double) = sts.minBy(_._2)
      // buy it
      val bought = StocksV4.buy(stock, amount)(rp)
      
      bought
  }
  
  /**
    * compositional implementation
    * we pass [[StockRepo]] several times
    * EVEN UGLIER... but functional
    */
  def investInStockWithMinValueV2(amount: Double): StockRepo => Double =
    (rp: StockRepo) =>
      // find all
      StocksV4.findAll()
        // find the cheapest stock
        .andThen { sts: Map[String, Double] => sts.minBy(_._2) }
        // get the name of that stock
        .andThen { case (stock, _) => stock }
        // buy it
        .andThen { sname: String => StocksV4.buy(sname, amount)(rp) } (rp)

  /**
    * Reader monad to rescue !
    */
  case class AReader[A, B](f: A => B) {
    def apply(input: A): B = f(input)
    def map[C]   (g: B => C)            : AReader[A, C] = AReader { a => g(f(a)) }
    def flatMap[C](g: B => AReader[A, C]): AReader[A, C] = AReader { a => g(f(a))(a) }
  }
  object AReader {
    def pure[A, B](b: B)                : AReader[A, B] = AReader { _ => b }
  }

  /**
    * we represent our [[StockRepo]] as a set of readers
    */
  object StocksV5 {
    def findAll()                             : AReader[StockRepo, Map[String, Double]] = AReader { rp => rp.findAll() }
    def sell(stock: String, quantity: Double): AReader[StockRepo, Double] = AReader { rp => rp.sell(stock, quantity) }
    def buy(stock: String, amount: Double)   : AReader[StockRepo, Double] = AReader { rp => rp.buy(stock, amount) }
  }

  /**
    * and we can use flatMap syntax
    */
  def investInCheapestStockV5a(amount: Double): AReader[StockRepo, Double] =
    StocksV5.findAll()
      .map(stocks => stocks.minBy(_._2))
      .map { case (stock, _) => stock }
      .flatMap(stock => StocksV5.buy(stock, amount))

  /**
    * and we can use for-comprehension syntax
    */
  def investInCheapestStockV5b(amount: Double): AReader[StockRepo, Double] =
    for {
      stocks   <- StocksV5.findAll()
      minStock <- AReader.pure(stocks.minBy(_._2)._1)
      spent    <- StocksV5.buy(minStock, amount)
    } yield spent

  /**************************************
    * CATS has this monad out of the box.
    *************************************/
  import cats.data.Reader
  import cats.Id

  object StocksV6 {
    def findAll()                             : Reader[StockRepo, Map[String, Double]] = Reader { rp => rp.findAll() }
    def sell(stock: String, quantity: Double): Reader[StockRepo, Double] = Reader { rp => rp.sell(stock, quantity) }
    def buy(stock: String, amount: Double)   : Reader[StockRepo, Double] = Reader { rp => rp.buy(stock, amount) }
  }

  /**
    * flatMap syntax - unreadable
    */
  def investInCheapestStockV6a(amount: Double): Reader[StockRepo, Double] =
    StocksV6.findAll().flatMap { stocksMap: Map[String, Double] =>
      Reader[StockRepo, String](_ => stocksMap.minBy(_._2)._1).flatMap { sname: String =>
        StocksV6.buy(sname, amount)
      }
    }

  /**
    * for syntax - way more readable
    * 
    * What we do - we pass automatically parameter to ALL readers
    */
  def investInCheapestStockV6b(amount: Double): Reader[StockRepo, Double] =
    for {
      stocks   <- StocksV6.findAll()
      stocksT: Map[String, Double] = stocks
      name     <- Reader[StockRepo, String](_ => stocks.minBy(_._2)._1) // just a value lifted to context
      nameT: String = name
      spent    <- StocksV6.buy(name, amount)
      spentT: Double = spent
    } yield spent

  val repo: StockRepo = ???
  
  val spent1: Id[Double] = investInCheapestStockV6b(10).apply(repo)
  /** or */
  val spent2:     Double = investInCheapestStockV6b(10)(repo)
}

