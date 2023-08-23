import sbt.*

object Libraries {
  def http4s(artifact: String): ModuleID = "org.http4s" %% artifact % Versions.http4sCe2
  def zio2(artifact: String): ModuleID   = pf.zio       %% artifact % Versions.zio2v
  def fs2(artifact: String): ModuleID    = "co.fs2"     %% artifact % Versions.fs2ce2

  def slick(artifact: String): ModuleID           = s"${pf.typesafe}.slick" %% artifact % Versions.slick
  def akka(artifact: String, v: String): ModuleID = s"${pf.typesafe}.akka"  %% artifact % v
  def akka(artifact: String): ModuleID            = akka(artifact, Versions.akka)
  def akkaHttp(artifact: String): ModuleID        = akka(artifact, Versions.akkaHttp)
  def slf4j(artifact: String): ModuleID           = pf.slf4j                 % artifact % Versions.slf4j17
  val tsconfig: ModuleID                          = pf.typesafe              % "config" % Versions.tsconfig

  val shapeless  = "com.chuusai"      %% "shapeless"   % Versions.shapeless
  val jsoup      = "org.jsoup"         % "jsoup"       % Versions.jsoup
  val cats       = pf.typelevel       %% "cats-core"   % Versions.cats
  val catsLaws   = pf.typelevel       %% "cats-laws"   % Versions.cats
  val catsMtl    = pf.typelevel       %% "cats-mtl"    % Versions.catsMtl
  val catsEffect = pf.typelevel       %% "cats-effect" % Versions.catsEffect2
  val catsRetry  = "com.github.cb372" %% "cats-retry"  % Versions.catsRetry

  val fs2core = fs2("fs2-core")
  val fs2io   = fs2("fs2-io")

  val http4sServer = http4s("http4s-blaze-server")
  val http4sDsl    = http4s("http4s-dsl")
  val http4sClient = http4s("http4s-blaze-client")
  val http4sCirce  = http4s("http4s-circe")

  val slickCore   = slick("slick")
  val slickHikari = slick("slick-hikaricp")

  val sqlPgDriver = "org.postgresql" % "postgresql" % Versions.sqlPg
  val sqlH2       = "com.h2database" % "h2"         % Versions.sqlH2

  val http4sJwtAuth      = "dev.profunktor" %% "http4s-jwt-auth"     % Versions.http4sJwtAuth
  val redis4catsEffects  = "dev.profunktor" %% "redis4cats-effects"  % Versions.redis4cats
  val redis4catsLog4cats = "dev.profunktor" %% "redis4cats-log4cats" % Versions.redis4cats

  val refinedCore   = "eu.timepit" %% "refined"        % Versions.refined
  val refinedCats   = "eu.timepit" %% "refined-cats"   % Versions.refined
  val refinedScalaz = "eu.timepit" %% "refined-scalaz" % Versions.refined

  val log4cats = "io.chrisdavenport" %% "log4cats-slf4j" % Versions.log4cats
  val newtype  = "io.estatico"       %% "newtype"        % Versions.newtype

  val javaxCrypto = "javax.xml.crypto" % "jsr105-api" % Versions.javaxCrypto

  val doobieCore   = pf.tpolecat %% "doobie-core"     % Versions.doobie
  val doobiePg     = pf.tpolecat %% "doobie-postgres" % Versions.doobie
  val doobieHikari = pf.tpolecat %% "doobie-hikari"   % Versions.doobie
  val skunkCore    = pf.tpolecat %% "skunk-core"      % Versions.skunk
  val skunkCirce   = pf.tpolecat %% "skunk-circe"     % Versions.skunk

  // Runtime
  val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

  // Test
  val scalaTestWhole   = "org.scalatest" %% "scalatest"                % Versions.scalaTest
  val scalaTestShould  = "org.scalatest" %% "scalatest-shouldmatchers" % Versions.scalaTest
  val scalaTestFunSpec = "org.scalatest" %% "scalatest-funspec"        % Versions.scalaTest

  // https://mvnrepository.com/artifact/org.scalacheck/scalacheck
  val scalaCheck          = "org.scalacheck"             %% "scalacheck"                % "1.17.0"
  // https://github.com/alexarchambault/scalacheck-shapeless
  val scalaCheckShapeless = "com.github.alexarchambault" %% "scalacheck-shapeless_1.16" % "1.3.1"
  // scalactic transitively comes from "scalatest-core"
  // val scalactic_ = "org.scalactic" %% "scalactic" % Versions.scalaTest

  /** scalatestplus - is a project by scalatest for integrations org.scalatestplus::scalacheck-1-14 - library to
    * integrate: scalatest + scalacheck
    */
  val scalaTestScalaCheckIntegration = "org.scalatestplus" %% "scalacheck-1-17"         % Versions.scalaTestPlus
  val scalaMockito                   = "org.mockito"       %% "mockito-scala-scalatest" % "1.17.14" // tr: "org.mockito" % "mockito-core" % "4.8.1"

  // https://index.scala-lang.org/ghik/silencer/silencer-plugin/1.4.2?target=_2.13
  val silencerAnnotation = "com.github.ghik" % "silencer-lib" % Versions.silencer % Provided cross CrossVersion.full

  val fansi: ModuleID      = pf.lihaoyi %% "fansi"      % "0.4.0"
  val pprint: ModuleID     = pf.lihaoyi %% "pprint"     % "0.8.1"
  val sourcecode: ModuleID = pf.lihaoyi %% "sourcecode" % "0.3.0"
}
