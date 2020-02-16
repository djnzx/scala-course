package topics.state_monad_evo

object StateMonadEvoAppV4 extends App {

  case class Transaction[A](eval: Stocks => (A, Stocks)) {

    def map[B](f: A => B): Transaction[B] = Transaction { portfolio: Stocks =>
      // we apply HAVING TRANSACTION (FUNCTION: Stocks => (A, Stocks))
      // to portfolio as a parameter
      val (result_a: A, portfolio1: Stocks) = eval(portfolio)
      // we apply GIVEN f: A => B and produce the result of type B
      val result_b: B = f(result_a)
      // returning the pair (result_b)
      (result_b, portfolio1)
    }

    def flatMap[B](f: A => Transaction[B]): Transaction[B] = Transaction { portfolio: Stocks =>
      // we apply HAVING TRANSACTION (FUNCTION: Stocks => (A, Stocks))
      val (result_a: A, portfolio1: Stocks) = eval(portfolio)
      // we apply GIVEN FUNCTION (A => Transaction[B])
      val result_b: Transaction[B] = f(result_a)
      // we apply result_b to portfolio1 taken on the first step
      val r: (B, Stocks) = result_b.eval(portfolio1)
      r
    }
  }

  def buy(name: String, amount: Double): Transaction[Double] = Transaction { portfolio =>
    StateMonadEvoAppV1.buy1(name, amount, portfolio)
  }

  def sell(name: String, quantity: Double): Transaction[Double] = Transaction { portfolio =>
    StateMonadEvoAppV1.sell1(name, quantity, portfolio)
  }

  def get(name: String): Transaction[Double] = Transaction { portfolio =>
    (StateMonadEvoAppV1.get1(name, portfolio), portfolio)
  }

  def move_plain(from: String, to: String): Transaction[(Double, Double)] =
    get(from).flatMap(own =>
      sell(from, own).flatMap(money =>
        buy(to, money).map(bought =>
          (own, bought))))

  def move_sugared(from: String, to: String): Transaction[(Double, Double)] =
    for {
      was        <- get(from)
      revenue    <- sell(from, was)
      purchased  <- buy(to, revenue)
    } yield {
      (was, purchased)
    }

  println(portfolio)
  val representation: Transaction[(Double, Double)] = move_sugared("APPLE", "FACEBOOK")
  val (s, portfolio2) = representation.eval(portfolio)
  println(portfolio2)
  println(s)

}
