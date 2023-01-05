package mono101

import cats.implicits.catsSyntaxOptionId
import monocle.Lens

object UniversityExample extends App {

  case class Lecturer(firstName: String, lastName: String, salary: Int)
  case class Department(budget: Int, lecturers: List[Lecturer])
  case class University(name: String, departments: Map[String, Department])

  val uni = University(
    "oxford",
    Map(
      "Computer Science" -> Department(
        45,
        List(
          Lecturer("john", "doe", 10),
          Lecturer("robert", "johnson", 16)
        )
      ),
      "History"          -> Department(
        30,
        List(
          Lecturer("arnold", "stones", 20)
        )
      )
    )
  )

  val physics = Department(36, List(
    Lecturer("daniel", "jones", 12),
    Lecturer("roger", "smith", 14)
  ))

  import monocle.Focus
  import monocle.syntax.all._

  // navigation
  val departments: Lens[University, Map[String, Department]] = Focus[University](_.departments)
  val replacerFn: University => University = departments.at("History").replace(None)

  val u2 = replacerFn(uni)
  pprint.pprintln(u2)

  val u3 = departments.at("Physics").replace(Some(physics))(uni)
  pprint.pprintln(u3)

  val l2 = Lecturer("Arnold", "Schwarzenegger", 33)
  val h2 = Department(30, List(l2))

  val u4 = departments.at("History").modify{
    case None => Some(h2) // History1 - None - adding
    case Some(x) => x.focus(_.lecturers).modify(l2 :: _).some
  }(uni)
  pprint.pprintln(u4)
}
