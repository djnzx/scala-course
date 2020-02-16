package topics.state_monad_evo

object StateMonadEvoAppV2 extends App {

  def buy(stock_name: String, money: Double)(portfolio: Stocks): (Double, Stocks) = {
    // calculate how many stock I can buy for my money
    val purchased = money / prices(stock_name)
    val owned = portfolio(stock_name)
    // return AMOUNT OF STOCKS PURCHASED + updated portfolio
    (purchased, portfolio + (stock_name -> (owned + purchased)))
  }

  def sell(stock_name: String, quantity: Double)(portfolio: Stocks): (Double, Stocks) = {
    val revenue = quantity * prices(stock_name)
    val owned = portfolio(stock_name)
    // return AMOUNT OF MONEY EARNED + updated structure
    (revenue, portfolio + (stock_name -> (owned - quantity)))
  }

  def get(name: String)(portfolio: Stocks): Double = portfolio(name)

  /**
    * so far, so good, but error prone
    */
  def move(from: String, to: String, portfolio0: Stocks): ((Double, Double), Stocks) = {
    val originallyOwned = get(from)(portfolio0)
    val (revenue, portfolio1) = sell(from, originallyOwned)(portfolio0)
    val (purchased, portfolio2) = buy(to, revenue)(portfolio1)
    ((originallyOwned, purchased), portfolio2)
  }

  val buyPartial: (String, Double) => Stocks => (Double, Stocks) = (name: String, amount: Double) => buy(name, amount)
  //                                  ==========================
  val sellPartial: (String, Double) => Stocks => (Double, Stocks) = (name: String, amount: Double) => sell(name, amount)
  //                                   ==========================
}
