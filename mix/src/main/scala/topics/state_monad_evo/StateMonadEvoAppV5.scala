package topics.state_monad_evo

/**
  * everything is based on:
  *      S => (S, A)
  */
object StateMonadEvoAppV5 extends App {

  case class STM[S, A](run: S => (S, A)) {
    def map[B](f: A => B): STM[S,B] = STM { state =>
      // evaluate having
      val (s1, a): (S, A) = run(state)
      // evaluate given
      (s1, f(a))
    }
    def flatMap[B](f: A => STM[S,B]): STM[S, B] = STM { state =>
      // evaluate having
      val (s1, a): (S, A) = run(state)
      // evaluate given
      f(a).run(s1)
    }
  }
  println(s"Prices: $prices")
  println(s"Portfolio: $portfolio")

  // plain implementations

  val get_price0: String => Double = (stock_name: String) => prices(stock_name)

  val sell0: (String, Double, Stocks) => (Stocks, Double) =
    (stock_name: String, stock_amount: Double, pf: Stocks) => {
      val earned: Double = stock_amount * get_price0(stock_name)
      val now: Double = pf(stock_name) - stock_amount
      (pf + (stock_name -> now), earned)
    }

  val buy0 = (stock_name: String, stock_amount: Double, pf: Stocks) => {
    val spent: Double = stock_amount * get_price0(stock_name)
    val now = pf(stock_name) + stock_amount
    (pf + (stock_name -> now), spent)
  }

  // plain usage
  val (portfolio2, earned) = sell0("APPLE", 5, portfolio)
  println(earned)
  println(portfolio2)
  val (portfolio3, spent) = buy0("GOOGLE", 10, portfolio2)
  println(spent)
  println(portfolio3)

  // State Monad wrappers
  val buy: (String, Double) => STM[Stocks, Double] =
    (stock_name: String, stock_amount: Double) => STM { state => {
      buy0(stock_name, stock_amount, state)
    }}

  val sell: (String, Double) => STM[Stocks, Double] =
    (stock_name: String, stock_amount: Double) => STM { state => {
    sell0(stock_name, stock_amount, state)
  }}

  val combination: STM[Stocks, (Double, Double)] = for {
    earned <- sell("APPLE", 5)
    spent  <- buy("GOOGLE", 6)
  } yield (earned, spent)
  val (portfolio4, (earned2, spent2)) = combination.run(portfolio)
  println(s"portfolio4 = ${portfolio4}")
  println(s"earned2 = ${earned2}")
  println(s"spent2 = ${spent2}")

}
