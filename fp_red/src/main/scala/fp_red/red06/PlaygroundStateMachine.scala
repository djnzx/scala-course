package fp_red.red06

import fp_red.red06.State.{get, modify, sequence}

sealed trait Input
case object Coin extends Input
case object Turn extends Input

/**
  * this is our state 
  */
case class Machine(locked: Boolean, candies: Int, coins: Int)

object Candy {
  def update = (i: Input) => (m: Machine) =>
    (i, m) match {
      case (_,    Machine(_,     0,     _))    => m
      case (Coin, Machine(false, _,     _))    => m
      case (Turn, Machine(true,  _,     _))    => m
      case (Coin, Machine(true,  candy, coin)) => Machine(false, candy, coin + 1)
      case (Turn, Machine(false, candy, coin)) => Machine(true, candy - 1, coin)
    }

  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = for {
    _ <- sequence(inputs map (modify[Machine] _ compose update))
    s <- get
  } yield (s.coins, s.candies)
}

