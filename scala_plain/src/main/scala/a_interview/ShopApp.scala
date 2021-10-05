package a_interview

trait Tool {
  val a = 13
}

trait Shop { _: Tool =>
  val b = a
}

object ShopApp extends App {
  val shop = new Tool with Shop
  println(shop.b)
}
