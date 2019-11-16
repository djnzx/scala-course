package x060essential

object X204Set extends App {
  val people = Set(
    "Alice",
    "Bob",
    "Charlie",
    "Derek",
    "Edith",
    "Fred")
  val ages = Map(
    "Alice" -> 20,
    "Bob" -> 30,
    "Charlie" -> 50,
    "Derek" -> 40,
    "Edith" -> 10,
    "Fred" -> 60)
  val favoriteColors = Map(
    "Bob" -> "green",
    "Derek" -> "magenta",
    "Fred" -> "yellow")
  val favoriteLolcats = Map(
    "Alice" -> "Long Cat",
    "Charlie" -> "Ceiling Cat",
    "Edith" -> "Cloud Cat")

  def favoriteColor(name: String): Option[String] = favoriteColors.get(name)
  def favoriteColor2(name: String): String = favoriteColors.getOrElse(name, "beige")
  def printColors: Unit = for {
    person <- people
  } println(s"person: $person's color: ${favoriteColor2(person)}")


}
