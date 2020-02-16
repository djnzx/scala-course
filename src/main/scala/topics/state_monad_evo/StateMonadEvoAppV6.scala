package topics.state_monad_evo

import cats.data.State

object StateMonadEvoAppV6 extends App {

  println(s"Prices: $prices")
  println(s"Portfolio: $portfolio")

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
  val buy: (String, Double) => State[Stocks, Double] =
    (stock_name: String, stock_amount: Double) => State { state => {
      buy0(stock_name, stock_amount, state)
    }}

  val sell: (String, Double) => State[Stocks, Double] =
    (stock_name: String, stock_amount: Double) => State { state => {
    sell0(stock_name, stock_amount, state)
  }}

  val combination: State[Stocks, (Double, Double)] = for {
    earned <- sell("APPLE", 5)
    spent  <- buy("GOOGLE", 6)
  } yield (earned, spent)
  val (portfolio4, (earned2, spent2)) = combination.run(portfolio).value
  println(s"portfolio4 = ${portfolio4}")
  println(s"earned2 = ${earned2}")
  println(s"spent2 = ${spent2}")

}
