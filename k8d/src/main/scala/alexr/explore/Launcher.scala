package alexr.explore

import java.time.{Instant, LocalDateTime, ZoneOffset}
import scala.jdk.CollectionConverters._

object Launcher {

  private val scalaVersion = util.Properties.versionNumberString
  private val props = System.getProperties
  private def prop(x: Object): String = System.getProperty(x.asInstanceOf[String])

  def main(args: Array[String]): Unit = {
    val bi: meta.BuildInfo = meta.BuildInfoImpl
    pprint.log(bi)
    pprint.log(scalaVersion)
    pprint.log(args)
    pprint.log(props.keySet.size())

    val useful = Seq(
      "java.specification.version",    // 17
      "java.version",                  // 17.0.10
      "java.class.version",            // 61.0 (17) => https://javaalmanac.io/bytecode/versions/
      "java.vm.specification.version", // "17"
      "java.vm.version",               // "17.0.10+11-LTS-240"
      "java.runtime.version",          // "17.0.10+11-LTS-240"
      "java.runtime.name",             //
      "java.specification.name",       //
      "java.specification.vendor",     //
      "java.home",                     // "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home"
      "sun.boot.library.path",         // "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/lib"
      "java.version.date",             // 2024-01-16
      "sun.jnu.encoding",              // UTF-8
      "native.encoding",               // UTF-8
      "file.encoding",                 // UTF-8
      "file.separator",                // /
      "line.separator",                // \n
      "os.name",                       // Mac OS X
      "sun.java.command",              // alexr.k8artifact.Launcher
      "os.arch",                       // aarch64
      "sun.arch.data.model",           // 64
      "java.io.tmpdir",                // /var/folders/66/nmchz8ln19l3v6vmnl9x4vwr0000gn/T/
      "user.name",                     // djnz
      "user.home",                     // /Users/djnz
      "user.dir",                      // /Users/djnz/dev/pp/scala-course
      "os.version",                    // 14.4.1
    )

    val notInteresting = Seq(
      "sun.io.unicode.encoding",
      "socksNonProxyHosts",
      "java.vendor",
      "java.vm.info",
      "java.library.path",
      "java.class.path",
      "java.vm.specification.name",
      "java.vm.name",
      "java.vendor.url.bug",
      "user.language",
      "user.country",
      "sun.cpu.endian",
      "sun.java.launcher",
      "sun.management.compiler",
      "ftp.nonProxyHosts",
      "http.nonProxyHosts",
      "java.vm.compressedOopsMode",
      "java.vm.specification.vendor",
      "java.vm.vendor",
      "java.vendor.url",
      "jdk.debug",
      "path.separator",
    )

    useful.foreach(x => pprint.log(x -> prop(x)))

    // the rest
    println("---")
    props.keySet.asScala.toList
      .map(_.asInstanceOf[String])
      .sorted
      .filterNot(x => useful.contains(x))
      .filterNot(x => notInteresting.contains(x))
      .foreach(x => pprint.log(x -> prop(x)))

    // path
    prop("java.library.path").split(":").foreach { path =>
      pprint.log(path)
    }
    // classpath
    prop("java.class.path").split(":").foreach { x =>
      val clz = x.stripPrefix("/Users/djnz/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2")
      pprint.log(clz)
    }
    // env
    System
      .getenv()
      .asScala
      .foreach { case (ek, ev) => pprint.log(ek -> ev) }

    pprint.log(LocalDateTime.now())
    pprint.log(Instant.now().atZone(ZoneOffset.of("+3")).toLocalTime)

  }

}
