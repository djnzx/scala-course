package java8time

import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import org.scalatest.Inside
import org.scalatest.Succeeded
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class Java8DateTimeSpec extends AnyFunSpec with Matchers with ScalaCheckPropertyChecks with Inside {

  describe("local without zone") {

    it("local date") {

      /** it has now constructor */
      val ld0: LocalDate = LocalDate.now

      /** it has constructors */
      val ld1: LocalDate = LocalDate.of(2024, 3, 30)

      /** months can be represented as enums */
      val ld2: LocalDate = LocalDate.of(2024, Month.MARCH, 30)

      /** it has a parser */
      LocalDate.parse("2024-01-30") shouldBe LocalDate.of(2024, 1, 30)

      /** parser can be configured */
      LocalDate.parse("2024/01/30", DateTimeFormatter.ofPattern("yyyy/MM/dd")) shouldBe LocalDate.of(2024, 1, 30)

      /** it has a `show` */
      LocalDate.of(2024, 1, 30).toString shouldBe "2024-01-30"

      /** it can be formatted to your needs, you can build your own */
      LocalDate.of(2024, 12, 31).format(DateTimeFormatter.ISO_DATE) shouldBe "2024-12-31"
      LocalDate.of(2024, 12, 31).format(DateTimeFormatter.ISO_ORDINAL_DATE) shouldBe "2024-366"

      /** it has a min value */
      LocalDate.MIN shouldBe LocalDate.of(-999999999, 1, 1)

      /** it has a max value */
      LocalDate.MAX shouldBe LocalDate.of(999999999, 12, 31)

      /** two dates can be compared */
      LocalDate.of(2024, 3, 30) isBefore LocalDate.of(2024, 3, 31) shouldBe true
      LocalDate.of(2024, 4, 30) isAfter LocalDate.of(2024, 3, 31) shouldBe true
      LocalDate.of(2024, 3, 30) compareTo LocalDate.of(2024, 3, 31) shouldBe -1

      /** it has a algebra to seamlessly plus/minus Day / Week / Month / Year */
      LocalDate.of(2024, 3, 30).plusDays(10) shouldBe LocalDate.of(2024, 4, 9)
      LocalDate.of(2024, 3, 30).minus(15, ChronoUnit.MONTHS) shouldBe LocalDate.of(2022, 12, 30)

      /** since we have a lot of implicit */
      import scala.math.Ordered.orderingToOrdered
      ld1 == ld2 shouldBe true
      ld1 <= ld2 shouldBe true
      ld1 >= ld2 shouldBe true
      ld1 < ld2 shouldBe false
      ld1 > ld2 shouldBe false

    }

  }

  describe("non-local with zone") {

    it("1") {
      Succeeded
    }

  }

  describe("instant") {}

  describe("chrono") {}

  describe("temporal") {}

  describe("zones") {}

  describe("period") {}

  /** the missing parts are here
    * https://www.threeten.org/threeten-extra/
    * https://mvnrepository.com/artifact/org.threeten/threeten-extra/1.7.2
    */
}
