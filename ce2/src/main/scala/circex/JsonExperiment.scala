package circex

import cats.implicits.catsSyntaxTuple7Semigroupal
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.parser._

object JsonExperiment extends App {

  val raw =
    """
      |{
      |  "kind" : "youtubeAnalytics#resultTable",
      |  "columnHeaders" : [
      |    {
      |      "name" : "month",
      |      "columnType" : "DIMENSION",
      |      "dataType" : "STRING"
      |    },
      |    {
      |      "name" : "views",
      |      "columnType" : "METRIC",
      |      "dataType" : "INTEGER"
      |    },
      |    {
      |      "name" : "estimatedMinutesWatched",
      |      "columnType" : "METRIC",
      |      "dataType" : "INTEGER"
      |    },
      |    {
      |      "name" : "averageViewPercentage",
      |      "columnType" : "METRIC",
      |      "dataType" : "FLOAT"
      |    },
      |    {
      |      "name" : "subscribersGained",
      |      "columnType" : "METRIC",
      |      "dataType" : "INTEGER"
      |    },
      |    {
      |      "name" : "likes",
      |      "columnType" : "METRIC",
      |      "dataType" : "INTEGER"
      |    },
      |    {
      |      "name" : "comments",
      |      "columnType" : "METRIC",
      |      "dataType" : "INTEGER"
      |    }
      |  ],
      |  "rows" : [
      |    [
      |      "2022-06",
      |      14,
      |      5,
      |      79.76,
      |      0,
      |      0,
      |      0
      |    ],
      |    [
      |      "2022-07",
      |      22,
      |      9,
      |      87.09,
      |      0,
      |      0,
      |      0
      |    ],
      |    [
      |      "2022-08",
      |      30,
      |      12,
      |      78.51,
      |      0,
      |      0,
      |      0
      |    ],
      |    [
      |      "2022-09",
      |      57,
      |      20,
      |      75.25,
      |      0,
      |      0,
      |      0
      |    ]
      |  ]
      |}
      |""".stripMargin

  case class Row(x: String, n1: Double, n2: Double, n3: Double, n4: Double, n5: Double, n6: Double)
  // TODO: use shapeless
  implicit val rowDecoder: Decoder[Row] = c => c.as[(String, Double, Double, Double, Double, Double, Double)].map((Row.apply _).tupled)

  val x = parse(raw)
    .flatMap(
      _.hcursor
        .downField("rows")
        .as[List[Row]]
    )
  println(x)

}
