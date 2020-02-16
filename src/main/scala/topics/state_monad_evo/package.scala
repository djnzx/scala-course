package topics

/**
  * http://rcardin.github.io/design/programming/fp/monad/2018/11/22/and-monads-for-all-state-monad.html
  */
package object state_monad_evo {

  // type
  type Stocks = Map[String, Double]

  // prices of
  val prices: Stocks = Map(
    "APPLE" -> 1000,
    "GOOGLE" -> 1100,
    "FACEBOOK" -> 1200,
    "AMAZON" -> 1300
  )

  // real storage
  val portfolio: Stocks = Map(
    "APPLE" -> 10,
    "GOOGLE" -> 11,
    "FACEBOOK" -> 12,
    "AMAZON" -> 13
  )


}
