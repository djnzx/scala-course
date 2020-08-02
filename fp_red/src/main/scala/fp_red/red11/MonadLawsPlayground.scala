package fp_red.red11

import fp_red.red08.Gen

object MonadLawsPlayground {
  
  case class Order(item: Item, quantity: Int)
  case class Item(name: String, price: Double)

  val genOrder: Gen[Order] = for {
    name     <- Gen.stringN(3)
    price    <- Gen.uniform.map(_ * 10)
    quantity <- Gen.choose(1,100)
  } yield Order(Item(name, price), quantity)

  // or:

  val genItem: Gen[Item] = for {
    name  <- Gen.stringN(3)
    price <- Gen.uniform.map(_ * 10)
  } yield Item(name, price)

  val genOrder2: Gen[Order] = for {
    item     <- genItem
    quantity <- Gen.choose(1,100)
  } yield Order(item, quantity)

}
