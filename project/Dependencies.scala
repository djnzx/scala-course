import sbt._

object Dependencies {

  object Versions {
    val cats             = "2.2.0"
    val catsEffect       = "2.2.0"
    val catsMtlCore      = "0.7.1"
    val catsRetry        = "1.1.1"

    val circe            = "0.13.0"
    val ciris            = "1.0.4"
    val javaxCrypto      = "1.0.1"
    val fs2              = "2.3.0"
    val http4s           = "0.21.4"
    val http4sJwtAuth    = "0.0.4"
    val log4cats         = "1.0.1"
    val newtype          = "0.4.4"
    val refined           = "0.9.15"
    val redis4cats       = "0.9.6"
    val skunk            = "0.0.7"
    val doobie           = "0.9.4"
    val squants          = "1.6.0"
    val logback          = "1.2.3"
    val zio              = "1.0.3"
    val sqlPg            = "42.2.10"
    val sqlH2            = "1.4.200"
    val scalaz           = "7.2.30"
    val jsoup            = "1.13.1"
    val shapeless        = "2.3.3"

    // testing
    val scalaCheck       = "1.14.3"
    val scalaTest        = "3.2.2"
    val scalaTestPlus    = "3.1.1.1"

    // compiler plugins
    val silencer         = "1.6.0"
    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.0"
    val contextApplied   = "0.1.4"

    // typesafe stack
    val akka             = "2.6.10"
    val akkaHttp         = "10.2.1"
    val play             = "2.9.1"
    val slick            = "3.3.2"
    val tsconfig          = "1.4.0"
    val slf4j17          = "1.7.30"
  }

  // prefixes
  object pf {
    val typelevel = "org.typelevel"
    val typesafe  = "com.typesafe"
    val slf4j     = "org.slf4j"
    val tpolecat  = "org.tpolecat"
    val lihaoyi   = "com.lihaoyi"
    val zio       = "dev.zio"
  }

  object Libraries {
    def circe(artifact: String):             ModuleID = "io.circe"              %% artifact    % Versions.circe
    def ciris(artifact: String):             ModuleID = "is.cir"                %% artifact    % Versions.ciris
    def http4s(artifact: String):            ModuleID = "org.http4s"            %% artifact    % Versions.http4s
    def zio(artifact: String):               ModuleID = pf.zio                  %% artifact    % Versions.zio     withSources()  withJavadoc()
    def scalaz(artifact: String):            ModuleID = "org.scalaz"            %% artifact    % Versions.scalaz
    def fs2(artifact: String):               ModuleID = "co.fs2"                %% artifact    % Versions.fs2

    def slick(artifact: String):             ModuleID = s"${pf.typesafe}.slick" %% artifact    % Versions.slick
    def akka(artifact: String, v: String):   ModuleID = s"${pf.typesafe}.akka"  %% artifact    % v
    def akka(artifact: String):              ModuleID = akka(artifact, Versions.akka)
    def akkaHttp(artifact: String):          ModuleID = akka(artifact, Versions.akkaHttp)
    def slf4j(artifact: String):             ModuleID = pf.slf4j                %  artifact    % Versions.slf4j17
    val tsconfig:                            ModuleID = pf.typesafe             %  "config"     % Versions.tsconfig

    val shapeless     = "com.chuusai"      %% "shapeless"     % Versions.shapeless
    val jsoup         = "org.jsoup"        %  "jsoup"         % Versions.jsoup
    val cats          = pf.typelevel       %% "cats-core"     % Versions.cats         withSources()  withJavadoc()
    val catsLaws      = pf.typelevel       %% "cats-laws"     % Versions.cats         withSources()  withJavadoc()
    val catsMtlCore   = pf.typelevel       %% "cats-mtl-core" % Versions.catsMtlCore  withSources()  withJavadoc()
    val catsEffect    = pf.typelevel       %% "cats-effect"   % Versions.catsEffect   withSources()  withJavadoc()
    val squants       = pf.typelevel       %% "squants"       % Versions.squants
    val catsRetry     = "com.github.cb372" %% "cats-retry"    % Versions.catsRetry

    val fs2core       = fs2("fs2-core")
    val fs2reactive   = fs2("fs2-reactive-streams")

    val scalazCore    = scalaz("scalaz-core")
    val scalazEffect  = scalaz("scalaz-effect")

    val circeCore     = circe("circe-core")
    val circeGeneric  = circe("circe-generic")
    val circeParser   = circe("circe-parser")
    val circeRefined   = circe("circe-refined")

    val cirisCore     = ciris("ciris")
    val cirisEnum     = ciris("ciris-enumeratum")
    val cirisRefined   = ciris("ciris-refined")

    val http4sServer  = http4s("http4s-blaze-server")
    val http4sDsl     = http4s("http4s-dsl")
    val http4sClient  = http4s("http4s-blaze-client")
    val http4sCirce   = http4s("http4s-circe")

    val slickCore     = slick("slick")
    val slickHikari   = slick("slick-hikaricp")

    val sqlPg         = "org.postgresql"      % "postgresql"       % Versions.sqlPg
    val sqlH2         = "com.h2database"      % "h2"               % Versions.sqlH2

    val http4sJwtAuth      = "dev.profunktor" %% "http4s-jwt-auth"     % Versions.http4sJwtAuth
    val redis4catsEffects  = "dev.profunktor" %% "redis4cats-effects"  % Versions.redis4cats
    val redis4catsLog4cats = "dev.profunktor" %% "redis4cats-log4cats" % Versions.redis4cats

    val refinedCore    = "eu.timepit"          %% "refined"          % Versions.refined
    val refinedCats    = "eu.timepit"          %% "refined-cats"     % Versions.refined
    val refinedScalaz  = "eu.timepit"          %% "refined-scalaz"     % Versions.refined

    val log4cats      = "io.chrisdavenport"   %% "log4cats-slf4j"  % Versions.log4cats
    val newtype       = "io.estatico"         %% "newtype"         % Versions.newtype

    val javaxCrypto   = "javax.xml.crypto"    %  "jsr105-api"      % Versions.javaxCrypto

    val doobieCore    = pf.tpolecat           %% "doobie-core"     % Versions.doobie
    val doobiePg      = pf.tpolecat           %% "doobie-postgres" % Versions.doobie
    val doobieHikari  = pf.tpolecat           %% "doobie-hikari"   % Versions.doobie
    val skunkCore     = pf.tpolecat           %% "skunk-core"      % Versions.skunk
    val skunkCirce    = pf.tpolecat           %% "skunk-circe"     % Versions.skunk

    object CompilerPlugins {
      val betterMonadicFor = compilerPlugin("com.olegpy"      %% "better-monadic-for" % Versions.betterMonadicFor)
      val contextApplied   = compilerPlugin("org.augustjune"  %% "context-applied"    % Versions.contextApplied)
      val kindProjector    = compilerPlugin(pf.typelevel                 %% "kind-projector"     % Versions.kindProjector cross CrossVersion.full)
      val silencer         = compilerPlugin("com.github.ghik"  % "silencer-plugin"    % Versions.silencer      cross CrossVersion.full)
    }

    // Runtime
    val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

    // Test
    val scalaCheck       = "org.scalacheck"    %% "scalacheck"               % Versions.scalaCheck
    val scalactic        = "org.scalactic"     %% "scalactic"                % Versions.scalaTest
    val scalaTest        = "org.scalatest"     %% "scalatest"                % Versions.scalaTest
    val scalaTestShould  = "org.scalatest"     %% "scalatest-shouldmatchers" % Versions.scalaTest
    val scalaTestFunSpec = "org.scalatest"     %% "scalatest-funspec"        % Versions.scalaTest
    /**
      * scalatestplus - is a project by scalatest
      * for integrations
      * org.scalatestplus::scalacheck-1-14 - library to integrate: scalatest + scalacheck
      */
    val scalaTestPlus = "org.scalatestplus" %% "scalacheck-1-14" % Versions.scalaTestPlus

    // https://index.scala-lang.org/ghik/silencer/silencer-plugin/1.4.2?target=_2.13
    val silencerAnnotation = "com.github.ghik" % "silencer-lib" % Versions.silencer % Provided cross CrossVersion.full

    val pprint: ModuleID = pf.lihaoyi         %% "pprint"       % "0.6.4"
    val fansi: ModuleID  = pf.lihaoyi         %% "fansi"        % "0.2.12"
  }

}
