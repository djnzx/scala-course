package shapelss

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless._

/** https://books.underscore.io/shapeless-guide/shapeless-guide.html#introduction */
object Chapter1Intro {

  /** we define the same structures */
  case class Employee(name: String, number: Int, manager: Boolean)
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)
  type JustTuple3 = (String, Int, Boolean)

  /** naive implementation */
  def employeeToCsv(e: Employee): List[String] =
    List(e.name, e.number.toString, e.manager.toString)

  /** we can define the representation */
  def iceCreamToCsv(c: IceCream): List[String] =
    List(c.name, c.numCherries.toString, c.inCone.toString)

  /** but we need to do it for each and every "same" type */
  def justTupleToCsv(c: JustTuple3): List[String] =
    List(c._1, c._2.toString, c._3.toString)

  /** shapeless approach: unpack case classes to Heterogeneous typed lists actually they have the same structure
    */
  type TypeSafeRaw = String :: Int :: Boolean :: HNil

  val genericEmployee: TypeSafeRaw = Generic[Employee].to(Employee("Dave", 123, false))
  val genericIceCream: TypeSafeRaw = Generic[IceCream].to(IceCream("Sundae", 1, false))
  val genericTuple3: TypeSafeRaw = Generic[JustTuple3].to(Tuple3("Joe", 33, true))

  /** and we can write generic implementation */
  def genericCsv(gen: TypeSafeRaw): List[String] = {
    val g0: String = gen(0)
    val g1: Int = gen(1)
    List(g0, g1.toString, gen(2).toString)
  }

  /** so this expression has a proper type */
  val repr: TypeSafeRaw = "Hello" :: 123 :: true :: HNil

}

class Chapter1IntroSpec extends AnyFunSpec with Matchers {

  import pprint.{pprintln => println}
  import Chapter1Intro._

  it("the `same` types represented without knowledge of their structure") {
    genericEmployee shouldEqual "Dave" :: 123 :: false :: HNil
    genericIceCream shouldEqual "Sundae" :: 1 :: false :: HNil
    genericTuple3 shouldEqual "Joe" :: 33 :: true :: HNil
  }

  it("2") {
    genericCsv(genericEmployee) shouldEqual List("Dave", "123", "false")
    genericCsv(genericIceCream) shouldEqual List("Sundae", "1", "false")
    genericCsv(genericTuple3) shouldEqual List("Joe", "33", "true")
  }

}
