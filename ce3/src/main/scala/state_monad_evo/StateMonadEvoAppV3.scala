package state_monad_evo

object StateMonadEvoAppV3 extends App {

  type Transaction[+A] = Stocks => (A, Stocks)

  def buy(name: String, amount: Double): Transaction[Double] = portfolio => {
    val purchased = amount / prices(name)
    val owned = portfolio(name)
    (purchased, portfolio + (name -> (owned + purchased)))
  }

  def sell(name: String, quantity: Double): Transaction[Double] = portfolio => {
    val revenue = quantity * prices(name)
    val owned = portfolio(name)
    (revenue, portfolio + (name -> (owned - quantity)))
  }

  def get(name: String): Transaction[Double] = portfolio => {
    (portfolio(name), portfolio)
  }

  def map[A,B](tr: Transaction[A])(f: A => B): Transaction[B] = portfolio => {
    // we apply HAVING TRANSACTION (FUNCTION: Stocks => (A, Stocks))
    // to portfolio as a parameter
    val (result_a: A, portfolio1: Stocks) = tr(portfolio) // Stocks => (A, Stocks)
    // we apply GIVEN f: A => B and produce the result of type B
    val result_b: B = f(result_a)
    // returning the pair (result_b)
    (result_b, portfolio1)
  }

  def flatMap[A,B](tr: Transaction[A])(f: A => Transaction[B]): Transaction[B] = portfolio => {
    // we apply HAVING TRANSACTION (FUNCTION: Stocks => (A, Stocks))
    val (result_a: A, portfolio1: Stocks) = tr(portfolio)
    // we apply GIVEN FUNCTION (A => Transaction[B])
    val result_b: Transaction[B] = f(result_a)
    // we apply result_b to portfolio1 taken on the first step
    val r: (B, Stocks) = result_b(portfolio1)
    r
  }

  def move(from: String, to: String): Transaction[(Double, Double)] =
    flatMap(get(from))(amount_stock =>
      flatMap(sell(from, amount_stock))(money_got =>
        map(buy(to, money_got))(purchased =>
          (amount_stock, purchased)
        )
      )
    )
}
