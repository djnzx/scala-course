package x0lessons.for_comprehensions

// https://alvinalexander.com/scala/fp-book/how-to-make-class-work-generator-in-for-expression
object My4FlatMapAbleApp extends App {

  // my general intention
  val strings = Sequence("Java", "Scala", "Haskell")
  val numbers = Sequence(75, 42, 33)
  val persons = Sequence(Person("Alex"), Person("Dima"))
  val persons2 = Sequence(Person("Masha"), Person("Marina"))

  // to run this code, we need to implement 'foreach'
  for {
    item <- strings
  } println(item)

  // to run this code, we need to implement 'map'
  val outcome1 = for {
    item <- strings
  } yield item
  println(outcome1)

  // to run this code, we need to implement 'withFilter'
  val outcome2 = for {
    item <- strings
    if item.startsWith("S")
  } yield item
  println(outcome2)

  // to run this code, we need to implement 'flatMap'
  val outcome3: Sequence[(Person, Person)] = for {
    item1 <- persons  // flatMap req
    item2 <- persons2 // map req
    if item1 != item2
  } yield item1 -> item2 // this is always a List because of a lot of results
  println(outcome3)
}
