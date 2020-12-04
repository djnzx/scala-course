package a

import scala.util.matching.Regex

object Features213 {

  /**
    * pattern matching on interpolation
    * + toXOption
    */
  object feature12 {
    val data = "123-abc-true"

    val x: Option[(Int, String, Boolean)] = data match {
      case s"$a-$b-$c" =>
        for {
          a1 <- a.toIntOption
          c1 <- c.toBooleanOption
        } yield (a1, b, c1)
      case _ => None
    }
  }

  /**
    * chaining syntax
    */
  object feature3 {
    import scala.util.chaining._

    val r: Int = 
      5
        .pipe(_ * 5) // 25
        .tap(println)
        .pipe(_ + 1) // 26
  }

  /**
    * regexp matching
    */
  object regexpMatching {
    val BookExtractorRE:     Regex = """Book: title=([^,]+),\s+author=(.+)""".r
    val MagazineExtractorRE: Regex = """Magazine: title=([^,]+),\s+issue=(.+)""".r

    val catalog = Seq(
      "Book: title=Programming Scala Second Edition, author=Dean Wampler",
      "Magazine: title=The New Yorker, issue=January 2014",
      "Unknown: text=Who put this here??"
    )

    for (item <- catalog) {
      item match {
        case BookExtractorRE(title, author)    => println(s"""Book "$title", written by $author""")
        case MagazineExtractorRE(title, issue) => println(s"""Magazine "$title", issue $issue""")
        case entry                             => println(s"Unrecognized entry: $entry")
      }
    }
  }

}
