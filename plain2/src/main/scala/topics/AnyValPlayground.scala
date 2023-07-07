package topics

object AnyValPlayground {

  object the_problem_easy_to_mix {
    val carId = "BMW"
    val personId = "123"

    def doWithCar(carId: String) = ???
    def doWithPerson(personId: String) = ???

    doWithCar(personId)
    doWithPerson(carId)
    // :(
  }

  object solution1_good_but_extra_allocation {
    case class CarId(value: String)
    case class PersonId(value: String)

    val carId = CarId("BMW")
    val personId = PersonId("123")

    def doWithCar(id: CarId) = ???
    def doWithPerson(id: PersonId) = ???

    doWithCar(carId)
    doWithPerson(personId)
    // can't mix, compiler will check
  }

  object solution2_NO_extra_allocation {
    case class CarId(value: String) extends AnyVal
    case class PersonId(value: String) extends AnyVal

    val carId = CarId("BMW")
    val personId = PersonId("123")

    def doWithCar(id: CarId) = ???
    def doWithPerson(id: PersonId) = ???

    doWithCar(carId)
    doWithPerson(personId)
  }

  object solution3_even_better_semantic_and_no_allocation {
    trait Car
    trait Person

    case class Id[A](value: String) extends AnyVal
    object Id {
      def apply[A](value: String) = new Id[A](value)
    }

    val carId = Id[Car]("BMW")
    val personId = Id[Person]("123")

    def doWithCar(id: Id[Car]) = ???
    def doWithPerson(id: Id[Person]) = ???

    doWithCar(carId)
    doWithPerson(personId)

    // can't mix. compiler will check
  }








}
