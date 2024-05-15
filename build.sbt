import scala.sys.process.*
import scala.util.Try

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val v = Versions

lazy val sandbox = (project in file("sandbox"))
  .settings(
    Settings.common2,
    description := "sandbox, cat effects 3 based",
    libraryDependencies ++= Seq(
      "co.fs2"       %% "fs2-core"              % "3.9.4",
      "co.fs2"       %% "fs2-io"                % "3.9.4",
      // enum
      "com.beachape" %% "enumeratum"            % "1.7.3",
      "com.beachape" %% "enumeratum-circe"      % "1.7.3",
      "com.beachape" %% "enumeratum-cats"       % "1.7.3",
      "com.beachape" %% "enumeratum-scalacheck" % "1.7.3",
      // json
      "io.circe"     %% "circe-parser"          % "0.14.7",
      "io.circe"     %% "circe-generic-extras"  % "0.14.3",
      "io.circe"     %% "circe-fs2"             % "0.14.1",
    ),
  )

lazy val avro101 = (project in file("avro101"))
  .settings(
    scalaVersion := v.vScala212,
    javacOptions ++= CompilerOptions.javacOptions,
    scalacOptions ++= CompilerOptions.scalacOptions,
    scalacOptions -= ScalacOpts.warningsAsFatals, // we are learning, there is no sense to be so strict
    scalacOptions -= ScalacOpts.macroAnnotations, // 2.12 doesn't have it
    libraryDependencies ++= Libraries.testingToolkit2,
    libraryDependencies ++= Seq(
      Libraries.cats,
      "org.apache.avro"   % "avro"                % "1.11.0",
      "org.apache.kafka" %% "kafka-streams-scala" % "3.4.0",
    )
  )

lazy val mono101 = (project in file("mono101"))
  .settings(
    Settings.common2,
    libraryDependencies ++= Seq(
      "dev.optics" %% "monocle-core"  % "3.2.0",
      "dev.optics" %% "monocle-macro" % "3.2.0"
    )
  )

lazy val munitx = (project in file("munitx"))
  .settings(
    Settings.common2,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit"            % "1.0.0-M8",
      "org.scalameta" %% "munit-scalacheck" % "1.0.0-M8"
    )
  )

lazy val algorithms = (project in file("algorithms"))
  .settings(
    Settings.common2,
    // compiled with JDK17
    libraryDependencies += "org.springframework.security" % "spring-security-crypto" % "6.1.2",
    libraryDependencies += "co.fs2"                      %% "fs2-core"               % "3.9.3",
  )

lazy val amt = (project in file("amt"))
  .settings(
    Settings.common2,
    description := "AMT WSDL experiments",
    libraryDependencies ++= Seq(
      "org.apache.axis"   % "axis"              % "1.4",   // no transitive
      "org.apache.axis"   % "axis-saaj"         % "1.4",   // no transitive
      "org.apache.axis"   % "axis-jaxrpc"       % "1.4",   // no transitive
      "axis"              % "axis-wsdl4j"       % "1.5.1", // no transitive
      // "wsdl4j" % "wsdl4j" % "1.6.3", // no transitive
      // just to avoid warnings in runtime
      "commons-discovery" % "commons-discovery" % "0.5",   // transitive: commons-logging" % "commons-logging" % "1.1.1"
      "javax.mail"        % "mail"              % "1.4.7"  // transitive: "javax.activation" % "activation" % "1.1",
    )
  )

lazy val ce2 = project
  .in(file("ce2"))
  .settings(
    Settings.common2,
//    evictionErrorLevel := util.Level.Warn,
    description := "Cats Effects 2",
    libraryDependencies ++= Seq(
      "org.typelevel"     %% "cats-core"            % "2.10.0",
      "org.typelevel"     %% "cats-effect"          % "2.5.5",
      "org.typelevel"     %% "cats-effect-laws"     % "2.5.5",
      "io.circe"          %% "circe-generic-extras" % "0.14.3", // generic derivation: "circe-generic" => "circe-core"
      "org.http4s"        %% "http4s-ember-server"  % "0.21.34",
      "org.http4s"        %% "http4s-ember-client"  % "0.21.34",
      "org.http4s"        %% "http4s-circe"         % "0.21.34",
      "org.http4s"        %% "http4s-dsl"           % "0.21.34",
      "org.scala-lang"     % "scala-reflect"        % v.vScala213,
      "org.typelevel"     %% "cats-tagless-macros"  % "0.11",
      "org.scalameta"     %% "munit-scalacheck"     % "0.7.8",
      "org.typelevel"     %% "munit-cats-effect-2"  % "1.0.6",
      "ch.qos.logback"     % "logback-classic"      % "1.4.7",
      "org.mongodb.scala" %% "mongo-scala-driver"   % "4.9.0"
    ),
    dependencyOverrides ++= Seq(
      "io.circe" %% "circe-core" % "0.14.5"
    )
  )

lazy val ce3 = (project in file("ce3"))
  .enablePlugins(LaikaPlugin)
  .settings(
    Settings.common2,
    description := "CE3-based-related",
    libraryDependencies ++= Seq(
      // core
      "org.typelevel"               %% "cats-core"             % "2.10.0",
      "org.typelevel"               %% "cats-effect"           % "3.5.3",
      // stm
      "io.github.timwspence"        %% "cats-stm"              % "0.13.4",
      // streams
      "co.fs2"                      %% "fs2-core"              % "3.9.4",
      "co.fs2"                      %% "fs2-io"                % "3.9.4",
      "co.fs2"                      %% "fs2-reactive-streams"  % "3.9.4",
      // enum
      "com.beachape"                %% "enumeratum"            % "1.7.3",
      "com.beachape"                %% "enumeratum-circe"      % "1.7.3",
      "com.beachape"                %% "enumeratum-doobie"     % "1.7.3",
      "com.beachape"                %% "enumeratum-cats"       % "1.7.3",
      "com.beachape"                %% "enumeratum-scalacheck" % "1.7.3",
      // json
      "io.circe"                    %% "circe-parser"          % "0.14.6",
      "io.circe"                    %% "circe-optics"          % "0.14.1", // 0.15.0
      "io.circe"                    %% "circe-generic-extras"  % "0.14.3",
      "io.circe"                    %% "circe-yaml"            % "0.14.2", // 0.15.1
      "io.circe"                    %% "circe-fs2"             % "0.14.1",
      "io.circe"                    %% "circe-shapes"          % "0.14.6",
      "io.circe"                    %% "circe-testing"         % "0.14.6",
      // cats logging
      "org.typelevel"               %% "log4cats-core"         % "2.6.0",
      "org.typelevel"               %% "log4cats-slf4j"        % "2.6.0",
      //
      "com.github.fd4s"             %% "fs2-kafka"             % "3.2.0",
      "org.typelevel"               %% "cats-parse"            % "0.3.10",
      "com.github.cb372"            %% "cats-retry"            % "3.1.0",
      "org.typelevel"               %% "munit-cats-effect-3"   % "1.0.7",
      // http
      "org.http4s"                  %% "http4s-core"           % "0.23.23",
      "org.http4s"                  %% "http4s-dsl"            % "0.23.18",
      "org.http4s"                  %% "http4s-circe"          % "0.23.19",
      "org.http4s"                  %% "http4s-blaze-server"   % "0.23.15",
      "org.http4s"                  %% "http4s-blaze-client"   % "0.23.15",
      "org.http4s"                  %% "http4s-ember-server"   % "0.23.18",
      "org.http4s"                  %% "http4s-ember-client"   % "0.23.18",
      // tapir
      "com.softwaremill.sttp.tapir" %% "tapir-core"            % "1.7.2",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"      % "1.7.2",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"   % "1.7.2",
      "org.scalameta"               %% "scalameta"             % "4.8.11",
      // other
      "io.kubernetes"                % "client-java-api"       % "18.0.0",
      "io.kubernetes"                % "client-java"           % "18.0.0",
      "jakarta.mail"                 % "jakarta.mail-api"      % "2.1.1",
      "io.scalaland"                %% "chimney"               % "0.7.5",
      "org.tpolecat"                %% "skunk-core"            % "0.6.3",
      "io.7mind.izumi"              %% "logstage-core"         % "1.2.5",
      Libraries.doobieCore,
      Libraries.doobiePg,
      Libraries.sqlPostgres,
      Libraries.newtype,
      Libraries.refinedCore,
      Libraries.shapeless,
      "com.kubukoz"                 %% "debug-utils"           % "1.1.3",
      Libraries.catsMtl,
      Libraries.jsoup,
    ),
    // laika experiments
    laikaSite / target := target.value / "docs1",
    laikaIncludePDF := true,
    laikaPDF / artifactPath := target.value / "my-docs.pdf"
  )
  .enablePlugins(ScalaxbPlugin)
  .settings(
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
    )
  )

lazy val `ce3-docs` = (project in file("ce3-docs"))
  .settings(
    Settings.common2,
    libraryDependencies += "io.github.cibotech" %% "evilplot" % "0.9.1",
    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )
  .enablePlugins(MdocPlugin)

lazy val es68 = (project in file("es68"))
  .settings(
    Settings.common2,
    crossScalaVersions := Seq(Versions.vScala212, Versions.vScala213),
    description := "Elastic Search 6.8 Learning",
    libraryDependencies ++= Seq(
      "com.sksamuel.elastic4s" %% "elastic4s-core"       % "6.7.8",
      "com.sksamuel.elastic4s" %% "elastic4s-http"       % "6.7.8",
      "com.beachape"           %% "enumeratum"           % "1.7.2",
      "com.beachape"           %% "enumeratum-circe"     % "1.7.2",
      "io.circe"               %% "circe-parser"         % "0.14.6",
      "io.circe"               %% "circe-generic-extras" % "0.14.3",
      "org.typelevel"          %% "cats-core"            % "2.10.0",
      "com.github.pureconfig"  %% "pureconfig"           % "0.17.4",
    ),
  )

lazy val es68s_akka = (project in file("es68s_akka"))
  .settings(Settings.common2)
  .dependsOn(es68)
  .settings(
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.21"
  )

lazy val es68s_ce = (project in file("es68s_ce"))
  .settings(Settings.common2)
  .dependsOn(es68)
  .settings(
    libraryDependencies += "co.fs2" %% "fs2-core" % "3.9.2"
  )

lazy val es68s_zio = (project in file("es68s_zio"))
  .settings(Settings.common2)
  .dependsOn(es68)
  .settings(
    libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.15"
  )

lazy val es89 = (project in file("es89"))
  .settings(
    Settings.common2,
    description := "Elastic Search 8 Learning",
    libraryDependencies ++= Seq(
      "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % "8.9.2",
      "com.sksamuel.elastic4s" %% "elastic4s-testkit"       % "8.9.2",
      "org.typelevel"          %% "cats-core"               % "2.10.0",
    ),
  )

lazy val httpt = (project in file("httpt"))
  .settings(
    Settings.common2,
    description := "HTTP load tests",
    libraryDependencies ++= Seq(
      "org.http4s"    %% "http4s-dsl"          % "1.0.0-M39",
      "org.http4s"    %% "http4s-circe"        % "1.0.0-M39",
      "org.http4s"    %% "http4s-ember-server" % "1.0.0-M39", // 13.07.2023
      "org.http4s"    %% "http4s-blaze-server" % "1.0.0-M38", //  4.01.2023
      "org.http4s"    %% "http4s-jetty-server" % "1.0.0-M32",
      "org.typelevel" %% "log4cats-core"       % "2.6.0",
      "org.typelevel" %% "log4cats-slf4j"      % "2.6.0"
    ),
    dependencyOverrides ++= Seq(
      "org.http4s" %% "http4s-core"   % "1.0.0-M39",
      "org.http4s" %% "http4s-server" % "1.0.0-M39"
    )
  )

// sbt k8a/docker:publishLocal
// docker images | grep k8a
lazy val k8a = (project in file("k8a"))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    dockerBaseImage := "openjdk:11-jre-slim",
    dockerExposedPorts := Seq(8080),
    dockerGroupLayers := {
      case (_, path) if path.startsWith("alexr") => 2
      case _                                     => 1
    },
    description := "artifact to experiment with k8",
    Settings.common2,
    libraryDependencies ++= Seq(
      "com.github.cb372" %% "cats-retry"           % "3.1.0",
      "org.http4s"       %% "http4s-dsl"           % "1.0.0-M36",
      "org.http4s"       %% "http4s-blaze-server"  % "1.0.0-M36",
      "org.http4s"       %% "http4s-circe"         % "1.0.0-M36",
      "io.circe"         %% "circe-generic-extras" % "0.14.2"
    )
  )

/**  - create image: `sbt k8d/docker:publishLocal`
  *  - see what's created: `docker images | grep explore-docker-plugin`
  *  - look for the layers: `brew install dive`
  *  - run: `docker run explore-docker-plugin:1.2.3`
  *  - remove ALL containers created: `docker rm -vf $(docker ps -aq)`
  *  - remove ALL images created: `docker rmi -f $(docker images -aq)`
  *  - remove ALL docker artifacts: `docker system prune -a`
  */
val noopProcessLogger: ProcessLogger = ProcessLogger(_ => (), _ => ())
def run(cmd: String): Option[String] = Try(cmd.split(" ").toSeq.!!(noopProcessLogger).trim).toOption
def git(cmd: String): Option[String] = run(s"git $cmd")
def hash: Option[String] = git("rev-parse --short HEAD")
def branch: Option[String] = git("rev-parse --abbrev-ref HEAD")

lazy val k8d = (project in file("k8d"))
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    // https://github.com/sbt/sbt-buildinfo
    // https://www.youtube.com/watch?v=sAt0mwOVKAM
    // https://github.com/DevInsideYou/buildinfo/tree/main
    buildInfoPackage := "alexr.explore.meta",
    buildInfoObject := "BuildInfoImpl",
    buildInfoOptions ++= Seq(
      BuildInfoOption.Traits("BuildInfo"),
      BuildInfoOption.BuildTime,
    ),
    buildInfoKeys ++= Seq[BuildInfoKey](
      "branch" -> branch,
      "hash"   -> hash,
    ),
    // https://www.scala-sbt.org/sbt-native-packager/formats/docker.html
    /** https://hub.docker.com/_/eclipse-temurin
      * openjdk:8-jre-alpine                - 89 Mb
      * openjdk:8-alpine                    - 109 Mb
      * eclipse-temurin:8-jre               - 219 Mb
      * eclipse-temurin:8-jre-jammy         - 219 Mb
      * eclipse-temurin:17.0.11_9-jre-jammy - 253 Mb
      * eclipse-temurin:22-jre-jammy        - 271 Mb
      */
    dockerBaseImage := "eclipse-temurin:8-jre",
    // group layers to optimize the docker build
    dockerGroupLayers := {
      case (_, path) if path.startsWith("alexr") => 2
      case _                                     => 1
    },
    // https://piotrminkowski.com/2023/11/07/slim-docker-images-for-java/
    /** -Dqwe=asd goes to JVM PROPERTIES
      * abc123 goes to MAIN AGRS
      */
    dockerCmd := Seq("-Dqwe=asd", "abc123"),
    /** this goes to ENVIRONMENT */
    dockerEnvVars ++= Map("asd" -> "zxc"),
    Docker / packageName := "explore-docker-plugin",
    Docker / version := "1.2.3",
    // other things
    Settings.common2.init,
    libraryDependencies += LibrariesLihaoyi.pprint,
  )

lazy val fp_red = (project in file("fp_red"))
  .settings(
    Settings.common2,
    description := "FP in Scala (RED Book) Mostly plain Scala only a few libraries involved",
  )

/** Project to investigate Li Haoyi libraries https://www.lihaoyi.com https://www.handsonscala.com
  * https://github.com/lihaoyi
  *
  * +request +ujson: upickle vs ujson +api_macros
  */
lazy val lihaoyi = (project in file("lihaoyi"))
  .settings(
    Settings.common2,
    libraryDependencies ++= Seq(
      LibrariesLihaoyi.upickle,
      LibrariesLihaoyi.ujson,
      LibrariesLihaoyi.osLib,
      LibrariesLihaoyi.scalatags,
      LibrariesLihaoyi.requests,
      LibrariesLihaoyi.geny,
      LibrariesLihaoyi.fastparse,
      "org.commonmark" % "commonmark" % "0.21.0"
    )
  )

lazy val mix = (project in file("mix"))
  .settings(
    Settings.common2,
    libraryDependencies ++= Seq(
      "org.flywaydb" % "flyway-core" % "6.4.2",
      Libraries.jsoup,
    ),
  )

/** protobuf experiments */
lazy val pbx = (project in file("pbx"))
  .enablePlugins(ScalaxbPlugin)
  .settings(
    Settings.common2,
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
    )
  )

lazy val plain2 = (project in file("plain2"))
  .settings(
    Settings.common2,
    libraryDependencies ++= Seq(
      "org.typelevel"              %% "cats-core"   % "2.10.0",
      "io.chymyst"                 %% "curryhoward" % "0.3.8",
      "com.softwaremill.quicklens" %% "quicklens"   % "1.9.7",
    )
  )

lazy val plain3 = (project in file("plain3"))
  .settings(
    scalaVersion := v.vScala3,
    description := "Example sbt project that compiles using Scala 3"
  )

/** starting from 3.2.0 it has 2.13 support */
lazy val sparkx = (project in file("sparkx"))
  .settings(
    Settings.common2,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.5.1",
      "org.apache.spark" %% "spark-sql"  % "3.5.1"
    ),
  )

lazy val typesafe = (project in file("typesafe"))
  .settings(
    description := "Lightbend (Typesafe) Stack: Akka, Akka-Streams, Akka-Http Play, Lagom, Slick (https://www.lightbend.com)",
    Settings.common2,
    libraryDependencies ++= Seq(
      Libraries.cats,
      Libraries.sqlPostgres,
      "com.typesafe.akka"  %% "akka-actor"           % "2.6.21",
      "com.typesafe.akka"  %% "akka-actor-typed"     % "2.6.21",
      "com.typesafe.akka"  %% "akka-stream"          % "2.6.21",
      "org.reactivestreams" % "reactive-streams"     % "1.0.4",
      "com.typesafe.akka"  %% "akka-http"            % "10.5.0",
      "com.typesafe.akka"  %% "akka-http-spray-json" % "10.5.0",
      "com.typesafe.slick" %% "slick"                % "3.4.1",
      "com.typesafe.slick" %% "slick-hikaricp"       % "3.4.1",
      "com.typesafe.play"  %% "play-json"            % "2.9.4",
      "com.typesafe"        % "config"               % "1.4.2",
      "ch.qos.logback"      % "logback-classic"      % "1.4.7"
    )
  )

lazy val zio2 = (project in file("zio2"))
  .settings(
    description := "ZIO v2",
    Settings.common2,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"                % "2.10.0",
      "com.beachape"  %% "enumeratum"               % "1.7.3",
      "dev.zio"       %% "zio"                      % "2.1.1",
      "dev.zio"       %% "zio-streams"              % "2.1.1",
      "dev.zio"       %% "zio-json"                 % "0.6.2",
      "dev.zio"       %% "zio-logging"              % "2.2.3",
      "dev.zio"       %% "zio-logging-slf4j-bridge" % "2.2.3",
      "dev.zio"       %% "zio-interop-cats"         % "23.1.0.2",
      "dev.zio"       %% "zio-test"                 % "2.1.1" % Test,
      "dev.zio"       %% "zio-test-sbt"             % "2.1.1" % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

lazy val spring = (project in file("spring"))
  .settings(
    description := "SpringBoot Experiments",
    Settings.common2,
    libraryDependencies ++= Seq(
      "org.projectlombok"        % "lombok"                  % "1.18.30",
      "org.springframework.boot" % "spring-boot-starter-web" % "3.2.2",
    ),
  )

lazy val http4sws = (project in file("http4sws"))
  .settings(
    description := "WebSocket with http4s, cats-parse, cats effects, fs2 Streams",
    Settings.common2,
    libraryDependencies ++= Seq(
      "org.http4s"    %% "http4s-ember-server" % "0.23.25",
      "org.http4s"    %% "http4s-circe"        % "0.23.25",
      "org.http4s"    %% "http4s-dsl"          % "0.23.25",
      "io.circe"      %% "circe-generic"       % "0.14.6",
      "org.typelevel" %% "cats-parse"          % "1.0.0",
      "ch.qos.logback" % "logback-classic"     % "1.4.14",
    )
  )
