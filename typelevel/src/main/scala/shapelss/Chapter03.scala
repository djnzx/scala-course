package shapelss

import shapeless._
import pprint.{pprintln => println}

/** https://books.underscore.io/shapeless-guide/shapeless-guide.html#sec:generic
  */
object Chapter03 extends App {

  trait CsvEncoder[A] {
    def encode(value: A): List[String]
  }

  case class Employee(name: String, number: Int, manager: Boolean)

  implicit val employeeEncoder: CsvEncoder[Employee] =
    new CsvEncoder[Employee] {
      def encode(e: Employee): List[String] =
        List(
          e.name,
          e.number.toString,
          if (e.manager) "yes" else "no",
        )
    }

  def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
    values.map(value => enc.encode(value).mkString(",")).mkString("\n")

  val employees: List[Employee] = List(
    Employee("Bill", 1, true),
    Employee("Peter", 2, false),
    Employee("Milton", 3, false),
  )

  val x = writeCsv(employees)
  println(x)

  implicit def pairEncoder[A, B](implicit aEncoder: CsvEncoder[A], bEncoder: CsvEncoder[B]): CsvEncoder[(A, B)] =
    new CsvEncoder[(A, B)] {
      def encode(pair: (A, B)): List[String] = {
        val (a, b) = pair
        aEncoder.encode(a) ++ bEncoder.encode(b)
      }
    }

  object CsvEncoder {
    // "Summoner" method. “summoner” or “materializer”
    def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc

    // "Constructor"(accessor) method
    def instance[A](func: A => List[String]): CsvEncoder[A] =
      new CsvEncoder[A] {
        def encode(value: A): List[String] = func(value)
      }

    // Globally visible type class instances
  }
  // accessor to the instance
  val ee1: CsvEncoder[Employee] = CsvEncoder[Employee]
  val ee2: CsvEncoder[Employee] = implicitly[CsvEncoder[Employee]]
  val ee3: CsvEncoder[Employee] = the[CsvEncoder[Employee]]

  def createEncoder[A](func: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      def encode(value: A): List[String] = func(value)
    }

  implicit val stringEncoder: CsvEncoder[String] =
    createEncoder(str => List(str))

  implicit val intEncoder: CsvEncoder[Int] =
    createEncoder(num => List(num.toString))

  implicit val booleanEncoder: CsvEncoder[Boolean] =
    createEncoder(bool => List(if (bool) "yes" else "no"))

  implicit val hnilEncoder: CsvEncoder[HNil] =
    createEncoder(hnil => Nil)

  implicit def hlistEncoder[H, T <: HList](
      implicit hEncoder: CsvEncoder[H],
      tEncoder: CsvEncoder[T],
    ): CsvEncoder[H :: T] =
    createEncoder { case h :: t =>
      hEncoder.encode(h) ++ tEncoder.encode(t)
    }

  val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] = implicitly

}
