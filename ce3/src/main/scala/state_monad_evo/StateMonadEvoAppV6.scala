package state_monad_evo

//import cats.data.State

object StateMonadEvoAppV6 extends App {

  type Stocks = Map[String, Double]

  val prices: Stocks = Map(
    "AAPL"->1000,
    "GOGL"->1100,
    "FCBK"->1200,
    "AMZN"->1300
  )

  val portfolio: Stocks = Map(
    "AAPL"->10,
    "GOGL"->11,
    "FCBK"->12,
    "AMZN"->13
  )

  val sell = (stock_name: String, stock_amount: Double) => State { state: Stocks =>
    val own_amount = portfolio(stock_name)
    val price1 = prices(stock_name)
    val earned = stock_amount * price1
    (state + ( stock_name -> (own_amount - stock_amount)), earned)
  }

  val buy = (stock_name: String, stock_amount: Double) => State { state: Stocks =>
    val own_amount = portfolio(stock_name)
    val price1 = prices(stock_name)
    val spent = stock_amount * price1
    (state + ( stock_name -> (own_amount + stock_amount)), spent)
  }

  def peek[A](f: Stocks => A) = State { state: Stocks =>
    val r: A = f(state)
    (state, r)
  }

  val combine21: State[Stocks, (Double, Double)] = for {
    five    <- peek { st => println(s"BEF:$st"); 5 }
    _      <- peek { _ => println(s"FIVE:$five") }
    earned <- sell("AMZN", 3)
    spend  <- buy("AAPL", 10)
    _      <- peek { st => println(s"AFT:$st") }
  } yield (earned, spend)

  val combine22 =
    sell("AMZN", 3).flatMap(earned =>
      buy("AAPL", 10).map(spent =>
        (earned, spent)))


//  val (portfolio21, (earned, spent)) = combine21.run(portfolio).value
  val (portfolio21, (earned, spent)) = combine21.run(portfolio)
  println(s"portfolio = ${portfolio}")
  println(s"portfolio2 = ${portfolio21}")
  println(s"selling: AMZN, 3")
  println(s"earned = ${earned}")
  println(s"buying: AAPL, 10")
  println(s"spent = ${spent}")

}
