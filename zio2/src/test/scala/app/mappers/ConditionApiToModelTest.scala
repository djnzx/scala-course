package app.mappers

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import zio.json.DecoderOps
import app.model.{Condition, Conditions, Predicate, Relation, PropVal => m}

class ConditionApiToModelTest extends AnyFunSuite with Matchers with Inside
    with ScalaCheckPropertyChecks {

  test("api.Conditions => model.Conditions") {
    val json =
      """{
        |  "relation": "all",
        |  "conditions" : [
        |    {
        |      "property" : "SportId",
        |      "predicate" : "eq",
        |      "value" : 117
        |    },
        |    {
        |      "property" : "TotalCoefficient",
        |      "predicate" : "gt",
        |      "value" : 13.56
        |    },
        |    {
        |      "property" : "RegionId",
        |      "predicate" : "in",
        |      "value" : [11, 12, 13]
        |    },
        |    {
        |      "property" : "CoefficientOfAllSelections",
        |      "predicate" : "notin",
        |      "value" : [1.2, 3.4, 5.6]
        |    }
        |  ]
        |}
        |""".stripMargin

    val obj = Conditions(
      Relation.All,
      Set(
        Condition.SportId(Predicate.IsEqualTo(m.SportId(117))),
        Condition.TotalCoefficient(Predicate.IsGreaterThen(m.TotalCoefficient(13.56))),
        Condition.RegionId(Predicate.InSet(
          m.RegionId(11),
          m.RegionId(12),
          m.RegionId(13)
        )),
        Condition.CoefficientOfAllSelections(Predicate.NotInSet(
          m.CoefficientOfAllSelections(1.2),
          m.CoefficientOfAllSelections(3.4),
          m.CoefficientOfAllSelections(5.6),
        )),
      )
    )

    inside(json.fromJson[Conditions]) {
      case Left(e)       => pprint.log("error" -> e)
      case Right(parsed) => parsed shouldBe obj
    }

  }

}
