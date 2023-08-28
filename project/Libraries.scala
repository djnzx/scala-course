import sbt.*

object Libraries {
  val shapeless = "com.chuusai" %% "shapeless" % "2.3.10"

  val newtype       = "io.estatico" %% "newtype"        % "0.4.4"
  val refinedCore   = "eu.timepit"  %% "refined"        % "0.10.3"
  val refinedCats   = "eu.timepit"  %% "refined-cats"   % "0.10.3"
  val refinedScalaz = "eu.timepit"  %% "refined-scalaz" % "0.10.3"

  val jsoup     = "org.jsoup"         % "jsoup"      % "1.15.4"
  val cats      = pf.typelevel       %% "cats-core"  % "2.10.0"
  val catsLaws  = pf.typelevel       %% "cats-laws"  % "2.10.0"
  val catsMtl   = pf.typelevel       %% "cats-mtl"   % "1.3.1"
  val catsRetry = "com.github.cb372" %% "cats-retry" % "3.1.0"

  val sqlPostgres = "org.postgresql" % "postgresql" % "42.6.0"
  val sqlH2       = "com.h2database" % "h2"         % "1.4.200"

  val http4sJwtAuth      = "dev.profunktor" %% "http4s-jwt-auth"     % "0.0.7"
  val redis4catsEffects  = "dev.profunktor" %% "redis4cats-effects"  % "0.9.6"
  val redis4catsLog4cats = "dev.profunktor" %% "redis4cats-log4cats" % "0.9.6"

  val log4cats    = "io.chrisdavenport" %% "log4cats-slf4j" % "1.1.1"
  val javaxCrypto = "javax.xml.crypto"   % "jsr105-api"     % "1.0.1"

  val doobieCore   = pf.tpolecat %% "doobie-core"     % "1.0.0-RC2"
  val doobiePg     = pf.tpolecat %% "doobie-postgres" % "1.0.0-RC2"
  val doobieHikari = pf.tpolecat %% "doobie-hikari"   % "1.0.0-RC2"
  val skunkCore    = pf.tpolecat %% "skunk-core"      % "0.6.0"
  val skunkCirce   = pf.tpolecat %% "skunk-circe"     % "0.6.0"

  // Runtime
  val logback = "ch.qos.logback" % "logback-classic" % "1.4.7"

  // https://www.scalatest.org
  // https://mvnrepository.com/artifact/org.scalatest/scalatest
  val scalaTest             = "org.scalatest"              %% "scalatest"                 % "3.2.16"
  val scalaTestShould       = "org.scalatest"              %% "scalatest-shouldmatchers"  % "3.2.16"
  val scalaTestFunSpec      = "org.scalatest"              %% "scalatest-funspec"         % "3.2.16"
  // https://scalacheck.org
  // https://mvnrepository.com/artifact/org.scalacheck/scalacheck
  val scalaCheck            = "org.scalacheck"             %% "scalacheck"                % "1.17.0"
  // https://github.com/alexarchambault/scalacheck-shapeless
  val scalaCheckShapeless   = "com.github.alexarchambault" %% "scalacheck-shapeless_1.16" % "1.3.1"
  // https://www.scalactic.org
  // https://mvnrepository.com/artifact/org.scalactic/scalactic
  // val scalactic_ = "org.scalactic" %% "scalactic" % Versions.scalaTest
  val scalaCheckIntegration = "org.scalatestplus"          %% "scalacheck-1-17"           % "3.2.16.0"
  val scalaMockito          = "org.mockito"                %% "mockito-scala-scalatest"   % "1.17.14" // tr: "org.mockito" % "mockito-core" % "4.8.1"

  // https://index.scala-lang.org/ghik/silencer/silencer-plugin/1.4.2?target=_2.13
  // look for the plugin corresponding
  val silencerAnnotation = "com.github.ghik" % "silencer-lib" % "1.6.0" % Provided cross CrossVersion.full

  val testingToolkit: Seq[ModuleID] = Seq(
    scalaTest,              // runners, matchers
    scalaCheck,             // property based testing
    scalaCheckIntegration,  // scalaTest integration
    scalaCheckShapeless,    // Shapeless scalacheck Arbitrary[A] derivation
    scalaMockito,           // mock traits/classes
    LibrariesLihaoyi.pprint // println colored + colored console output + implicits to get source module/file/line/etc
  )

}
