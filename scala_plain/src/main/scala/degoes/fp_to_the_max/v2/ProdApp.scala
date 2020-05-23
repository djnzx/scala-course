package degoes.fp_to_the_max.v2

object ProdApp extends App {
  // my codebase
  val sample = new FpToTheMaxV2
  import sample.{app, IO}

  // running application
  app[IO].run()
}
