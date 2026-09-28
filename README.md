I thought I knew Scala...

### 2014
Java 8 finally learned what a lambda is. Streams felt like a revolution. It was a gateway drug.

### 2015
"I've been using Scala for years," I told myself, after a basic Udemy course. Meanwhile, a detour
into DevOps taught me that code nobody can deploy is code that doesn't exist.

### 2016
Alvin Alexander's Scala Cookbook, Underscore's Essential Scala, and Docker. Recipes are
comfortable. Comfort is the enemy.

### 2017
Essential Scala again, this time for real, plus Essential Slick, Akka and CI/CD. Actors everywhere.
Did I need them? Probably not. Did I learn from them? Absolutely.

### 2018
Functional Programming Simplified and an advanced Udemy course. The word "simplified" was a lie,
and a beautiful one.

### 2019
The year the floor gave way: ZIO, Scala with Cats, Cats Effect, Scalaz via FP for Mortals,
ScalaCheck, Rock the JVM. Top 0.4% on HackerRank's FP track. It turns out I didn't know Scala at
all.

### 2020
Thanks, COVID. Every Lightbend certificate there was. The Red Book, Functional and Reactive Domain
Modeling, Practical FP in Scala, Hands-on Scala. fs2, http4s, circe, shapeless. Then Kubernetes,
Prometheus, Grafana, Spark, Kafka, Cassandra. Lockdown is a state of mind.

### 2021
Zionomicon and Programming in Scala, fifth edition. The Akka Platform microservices tutorial, every
step of it, from gRPC through event sourcing to Kafka projections. ForgeRock, four DataStax
Cassandra courses, and my own sbt plugins, because at some point you stop using the build tool and
start arguing with it.

### 2022
I stopped writing Java, and I stopped writing Spark. I went all in on pure FP and the Typelevel
stack: cats, Cats Effect, fs2, doobie, http4s, circe, shapeless, tapir, plus Kafka Streams and the
YouTube API. The rule was one new thing every week, no exceptions.

### 2023
Typelevel, again. Same libraries, deeper holes. If you think you've understood `IO`, you haven't
read it a second time.

### 2024
Time to leave home. The Scala-99 problems and the algorithms got repositories of their own, and
this one became what it always was: a lab notebook with scars.

### 2025
Back to the bench. Free monads, cats-retry, Skunk with munit, doobie streams, fs2-kafka with Avro
schemas, Flyway, hand-written macros that derive codecs for traits. Then the humbling part: floating
point, matrices, mean and median, and a chat with an LLM. The fundamentals never stop sending the
bill.

### 2026
Hashes, ciphers, RSA and Ed25519 keys taken apart byte by byte. Yoneda, DynamoDB, Protobuf and UTF-8
multibyte, which is not as boring as it sounds. And a purge: three thousand lines of old notes
deleted in one commit. Knowing Scala, it turns out, is mostly knowing what to throw away.

### sbt JVM options

Where sbt takes its JVM options from:

| Source | Scope | Terminal `sbt` | IntelliJ | What goes there |
|---|---|---|---|---|
| `~/.config/sbt/sbtopts` | all projects, this machine only | yes | no | memory, GC, code cache (`-J-Xmx6G`, ...) |
| `.sbtopts` (this repo) | this project, in git | yes | yes | what the project needs (`-J-Duser.timezone=GMT`) |
| Settings → Build Tools → sbt → VM parameters | IntelliJ | no | yes | memory for IntelliJ's sbt |

- `.sbtopts` is read after `~/.config/sbt/sbtopts`, so project flags win
- every line needs the `-J` prefix (it's stripped and passed to `java`)
- don't put JVM flags into `SBT_OPTS`: IntelliJ reports bare `-X...`/`-XX:...` as `Unrecognized sbt
  option`
- with the global flags above, sbt fails to start on Java 8 (`UseG1GC` is rejected), use Java 11+
