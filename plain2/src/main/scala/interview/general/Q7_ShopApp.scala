package interview.general

trait Tool {
  println("tool: init")
  val a = 1
  println(s"tool: a=$a") // 0 if overridden in main
  println("tool: done")
}

trait Hammer {
  println("hammer: init")
  val b = 2
  println(s"hammer: b=$b") // 0 if overridden in main
  println("hammer: done")
}

trait Shop { _: Tool with Hammer =>
  println("shop: init")
  val c = a + b
  println(s"shop: a=$a")
  println(s"shop: b=$b")
  println(s"shop: c=$c")
  println("shop: done")
}

/** what will be printed? */
object Q7_ShopApp extends App {
  //             1         2         3
  val shop = new Tool with Shop with Hammer {
    println(s"a=$a")
    println(s"b=$b")
//    override val a = 10
//    override val b = 20
//    println(s"a=$a")
//    println(s"b=$b")
  }
  println(s"finally: a=${shop.a}")
  println(s"finally: b=${shop.b}")
  println(s"finally: c=${shop.c}")
}
