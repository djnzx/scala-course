import sbt._

object Libraries {
  def circe(artifact: String, v: String = Versions.circe): ModuleID = "io.circe" %% artifact % v
  def ciris(artifact: String): ModuleID = "is.cir"                               %% artifact % Versions.ciris
  def http4s(artifact: String): ModuleID = "org.http4s"                          %% artifact % Versions.http4sCe2
  def zio1(artifact: String): ModuleID = pf.zio                                  %% artifact % Versions.zio1v withSources () withJavadoc ()
  def zio2(artifact: String): ModuleID = pf.zio                                  %% artifact % Versions.zio2v withSources () withJavadoc ()
  def scalaz(artifact: String): ModuleID = "org.scalaz"                          %% artifact % Versions.scalaz
  def fs2(artifact: String): ModuleID = "co.fs2"                                 %% artifact % Versions.fs2ce2

  def slick(artifact: String): ModuleID = s"${pf.typesafe}.slick"          %% artifact % Versions.slick
  def akka(artifact: String, v: String): ModuleID = s"${pf.typesafe}.akka" %% artifact % v
  def akka(artifact: String): ModuleID = akka(artifact, Versions.akka)
  def akkaHttp(artifact: String): ModuleID = akka(artifact, Versions.akkaHttp)
  def slf4j(artifact: String): ModuleID = pf.slf4j                          % artifact % Versions.slf4j17
  val tsconfig: ModuleID = pf.typesafe                                      % "config" % Versions.tsconfig

  val shapeless = "com.chuusai"      %% "shapeless"     % Versions.shapeless
  val jsoup = "org.jsoup"             % "jsoup"         % Versions.jsoup
  val cats = pf.typelevel            %% "cats-core"     % Versions.cats withSources () withJavadoc ()
  val catsLaws = pf.typelevel        %% "cats-laws"     % Versions.cats withSources () withJavadoc ()
  val catsMtlCore = pf.typelevel     %% "cats-mtl-core" % Versions.catsMtlCore withSources () withJavadoc ()
  val catsEffect = pf.typelevel      %% "cats-effect"   % Versions.catsEffect2 withSources () withJavadoc ()
  val squants = pf.typelevel         %% "squants"       % Versions.squants
  val catsRetry = "com.github.cb372" %% "cats-retry"    % Versions.catsRetry

  val fs2core = fs2("fs2-core")
  val fs2io = fs2("fs2-io")
  val fs2reactive = fs2("fs2-reactive-streams")

  val scalazCore = scalaz("scalaz-core")
  val scalazEffect = scalaz("scalaz-effect")

  val circeCore = circe("circe-core")
  val circeGeneric = circe("circe-generic")
  val circeGenericEx = circe("circe-generic-extras", Versions.circeGenericExtras)
  val circeParser = circe("circe-parser")
  val circeRefined = circe("circe-refined")
  val circeShapes = circe("circe-shapes")
  val circeTesting = circe("circe-testing")

  val cirisCore = ciris("ciris")
  val cirisEnum = ciris("ciris-enumeratum")
  val cirisRefined = ciris("ciris-refined")

  val http4sServer = http4s("http4s-blaze-server")
  val http4sDsl = http4s("http4s-dsl")
  val http4sClient = http4s("http4s-blaze-client")
  val http4sCirce = http4s("http4s-circe")

  val slickCore = slick("slick")
  val slickHikari = slick("slick-hikaricp")

  val sqlPgDriver = "org.postgresql" % "postgresql" % Versions.sqlPg
  val sqlH2 = "com.h2database"       % "h2"         % Versions.sqlH2

  val http4sJwtAuth = "dev.profunktor"      %% "http4s-jwt-auth"     % Versions.http4sJwtAuth
  val redis4catsEffects = "dev.profunktor"  %% "redis4cats-effects"  % Versions.redis4cats
  val redis4catsLog4cats = "dev.profunktor" %% "redis4cats-log4cats" % Versions.redis4cats

  val refinedCore = "eu.timepit"   %% "refined"        % Versions.refined
  val refinedCats = "eu.timepit"   %% "refined-cats"   % Versions.refined
  val refinedScalaz = "eu.timepit" %% "refined-scalaz" % Versions.refined

  val log4cats = "io.chrisdavenport" %% "log4cats-slf4j" % Versions.log4cats
  val newtype = "io.estatico"        %% "newtype"        % Versions.newtype

  val javaxCrypto = "javax.xml.crypto" % "jsr105-api" % Versions.javaxCrypto

  val doobieCore = pf.tpolecat   %% "doobie-core"     % Versions.doobie
  val doobiePg = pf.tpolecat     %% "doobie-postgres" % Versions.doobie
  val doobieHikari = pf.tpolecat %% "doobie-hikari"   % Versions.doobie
  val skunkCore = pf.tpolecat    %% "skunk-core"      % Versions.skunk
  val skunkCirce = pf.tpolecat   %% "skunk-circe"     % Versions.skunk

  // Runtime
  val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

  // Test
  val scalaCheck = "org.scalacheck" %% "scalacheck" % Versions.scalaCheck
  // scalactic transitively comes from "scalatest-core"
  // val scalactic_ = "org.scalactic" %% "scalactic" % Versions.scalaTest

  val scalaTestWhole = "org.scalatest" %% "scalatest" % Versions.scalaTest

  val scalaTestShould = "org.scalatest"  %% "scalatest-shouldmatchers" % Versions.scalaTest
  val scalaTestFunSpec = "org.scalatest" %% "scalatest-funspec"        % Versions.scalaTest

  /** scalatestplus - is a project by scalatest for integrations org.scalatestplus::scalacheck-1-14 - library to
    * integrate: scalatest + scalacheck
    */
  val scalaTestScalaCheckIntegration = "org.scalatestplus" %% "scalacheck-1-15" % Versions.scalaTestPlus

  // https://index.scala-lang.org/ghik/silencer/silencer-plugin/1.4.2?target=_2.13
  val silencerAnnotation = "com.github.ghik" % "silencer-lib" % Versions.silencer % Provided cross CrossVersion.full

  val fansi: ModuleID = pf.lihaoyi      %% "fansi"      % "0.3.1"
  val pprint: ModuleID = pf.lihaoyi     %% "pprint"     % "0.7.2"
  val sourcecode: ModuleID = pf.lihaoyi %% "sourcecode" % "0.2.8"
}
