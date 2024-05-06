package circe101.c8_decoder_shapeless

object Data {

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

}
