package mono101

import cats.implicits.catsSyntaxOptionId
import monocle.Lens
import monocle.PTraversal

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

  val physics = Department(
    36,
    List(
      Lecturer("daniel", "jones", 12),
      Lecturer("roger", "smith", 14)
    )
  )

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

  val u4 = departments
    .at("History") // option - no guarantee "History" exists
    .modify {
      case None    => Some(h2) // History1 - None - adding
      case Some(x) => x.focus(_.lecturers).modify(l2 :: _).some
    }(uni)
  pprint.pprintln(u4)

  // lecturer accessor from department
  val lecturers: Lens[Department, List[Lecturer]] = Focus[Department](_.lecturers)
  // salary accessor from lecturer
  val salary: Lens[Lecturer, Int] = Focus[Lecturer](_.salary)

  import monocle.Traversal

  // combining accessor via and then
  val allLecturersTr: Traversal[University, Lecturer] = departments.each.andThen(lecturers).each
  // combining accessor via and then
  val salariesTr: Traversal[University, Int] = allLecturersTr.andThen(salary)
  val uniUpdater: University => University = salariesTr.modify(_ + 2)
  val uniAfter: University = uniUpdater(uni)

  pprint.pprintln(uniAfter)

  // update names
  // create lenses
  val firstName: Lens[Lecturer, String] = Focus[Lecturer](_.firstName)
  val lastName: Lens[Lecturer, String] = Focus[Lecturer](_.lastName)
  //
  val upperCasedFirstName: University = allLecturersTr.andThen(firstName).index(0).modify(_.toUpper)(uni)
  val upperCasedLastName: University = allLecturersTr.andThen(lastName).index(0).modify(_.toUpper)(upperCasedFirstName)

  pprint.pprintln(upperCasedFirstName)
  pprint.pprintln(upperCasedLastName)

  // first and last at once (using the same setter-modifier)
  val firstAndLastNamesAtOnceTr: Traversal[Lecturer, String] = Traversal.apply2[Lecturer, String](_.firstName, _.lastName) {
    case (fn, ln, l) =>
      l.copy(firstName = fn, lastName = ln)
  }
  allLecturersTr.andThen(firstAndLastNamesAtOnceTr).index(0).modify(_.toUpper)(uni)

}
