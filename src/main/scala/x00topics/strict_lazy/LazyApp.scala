package x00topics.strict_lazy

object LazyApp extends App {

  val outcome = data
    .withFilter(lessThan30)
    .withFilter(moreThan20)

  for (
    item <- outcome
  ) println(s"(lazy): $item")

}
