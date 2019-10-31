package x00topics.strict_lazy

object StrictApp extends App {

  val outcome = data
    .filter(lessThan30)
    .filter(moreThan20)

  for (
    item <- outcome
  ) println(s"(strict): $item")

}
