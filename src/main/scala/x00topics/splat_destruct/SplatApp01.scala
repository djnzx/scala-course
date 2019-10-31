package x00topics.splat_destruct

// https://alvinalexander.com/bookmarks/scala/scalas-missing-splat-operator
// splat - mean destructuriation
object SplatApp01 extends App {

  def printItems(items: String*): Unit = {
    println("- start --------")
    items.foreach(println)
    println("------- finish --")
  }

  printItems()
  printItems("hello")
  printItems("hello", "world")

  val fruits = Seq("apple", "banana", "plum")
//  printItems(fruits)    // won't compile
  printItems(fruits: _*)  // works well

}
