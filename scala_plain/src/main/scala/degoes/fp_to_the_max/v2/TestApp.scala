package degoes.fp_to_the_max.v2

object TestApp extends App {
  // my codebase
  val sample = new FpToTheMaxV2
  import sample.{app, TestIO}

  // test data
  var testDataset: sample.TestData = sample.TestData(
    input = "Alex" :: "1" :: "n" :: Nil,
    output = Nil,
    nums = List(0)
  )

  // running tests
  val s: String = app[TestIO].eval(testDataset).results
  println(s)
}
