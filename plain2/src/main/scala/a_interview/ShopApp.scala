package a_interview

trait Tool {
  val a = 1
}

trait Hammer {
  val b = 2
}

trait Shop { _: Tool with Hammer =>
  val c = a + b
}

/** what will be printed:
  * 0, 1, 2, 3 ?
  */
object ShopApp extends App {
  val shop =
    new Tool with Shop with Hammer
//    new Tool with Hammer with Shop
  println(shop.c)
}
