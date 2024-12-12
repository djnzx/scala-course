import sbt.*

object Libraries {
  val shapeless             = "com.chuusai"                %% "shapeless"                 % "2.3.10"
  // types-related
  val newtype               = "io.estatico"                %% "newtype"                   % "0.4.4"
  val refinedCore           = "eu.timepit"                 %% "refined"                   % "0.10.3"
  val refinedCats           = "eu.timepit"                 %% "refined-cats"              % "0.10.3"
  // basic functional abstractions
  val cats                  = "org.typelevel"              %% "cats-core"                 % "2.10.0"
  val catsLaws              = "org.typelevel"              %% "cats-laws"                 % "2.10.0"
  val catsMtl               = "org.typelevel"              %% "cats-mtl"                  % "1.3.1"
  val catsRetry             = "com.github.cb372"           %% "cats-retry"                % "3.1.0"
  val sqlPostgres           = "org.postgresql"              % "postgresql"                % "42.6.0"
  //
  val http4sJwtAuth         = "dev.profunktor"             %% "http4s-jwt-auth"           % "1.2.1"
  val redis4catsEffects     = "dev.profunktor"             %% "redis4cats-effects"        % "1.5.0"
  val redis4catsLog4cats    = "dev.profunktor"             %% "redis4cats-log4cats"       % "1.5.0"
  //
  val jsoup                 = "org.jsoup"                   % "jsoup"                     % "1.16.1"
  // logging runtime
  val logback               = "ch.qos.logback"              % "logback-classic"           % "1.4.7"
  // https://scalacheck.org
  // https://mvnrepository.com/artifact/org.scalacheck/scalacheck
  val scalaCheck            = "org.scalacheck"             %% "scalacheck"                % "1.18.1"
  // https://www.scalatest.org
  // https://mvnrepository.com/artifact/org.scalatest/scalatest
  val scalaTest             = "org.scalatest"              %% "scalatest"                 % "3.2.19"
  // https://github.com/alexarchambault/scalacheck-shapeless
  val scalaCheckShapeless   = "com.github.alexarchambault" %% "scalacheck-shapeless_1.16" % "1.3.1"
  // https://www.scalactic.org
  // https://mvnrepository.com/artifact/org.scalactic/scalactic
  // val scalactic_ = "org.scalactic" %% "scalactic" % Versions.scalaTest
  val scalaCheckIntegration = "org.scalatestplus"          %% "scalacheck-1-18"           % "3.2.19.0"
  val scalaMockito          = "org.mockito"                %% "mockito-scala-scalatest"   % "1.17.37"  // tr: "org.mockito" % "mockito-core" % "4.8.1"
  // https://index.scala-lang.org/ghik/silencer/silencer-plugin/1.4.2?target=_2.13
  // look for the plugin corresponding
  val silencerAnnotation    = "com.github.ghik"             % "silencer-lib"              % "1.6.0" % Provided cross CrossVersion.full

  val testingToolkit2: Seq[ModuleID] = Seq(
    scalaTest,              // runners, matchers
    scalaCheck,             // property-based testing
    scalaCheckIntegration,  // scalaTest integration
    scalaCheckShapeless,    // Shapeless scalacheck Arbitrary[A] derivation, doesn't have scala3 things
    scalaMockito,           // mock traits/classes, doesn't have scala3 things
    LibrariesLihaoyi.pprint // println colored + colored console output + implicits to get source module/file/line/etc
  )

  val testingToolkit3: Seq[ModuleID] = Seq(
    scalaTest,              // runners, matchers
    scalaCheck,             // property-based testing
    scalaCheckIntegration,  // scalaTest integration
    LibrariesLihaoyi.pprint // println colored + colored console output + implicits to get source module/file/line/etc
  )

}
