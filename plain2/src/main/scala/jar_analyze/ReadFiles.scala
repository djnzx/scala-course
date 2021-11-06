package jar_analyze

import scala.io.BufferedSource
import scala.util.Using

object ReadFiles extends App {

  def cutJarSuffix(fileName: String) = fileName match {
    case s"$name.jar" => Some(name)
    case _ => None
  }

  def stringIgnoreCase(s1: String, s2: String) = s1.toLowerCase <= s2.toLowerCase

  val jarsOozieSparkInitial = obtainResource(jarOozieSparkInitial)
    .map { f =>
      Using(scala.io.Source.fromFile(f)) { s: BufferedSource =>
        s.getLines()
          .filter(_.length >= 59)
          .map(s => s.substring(59))
          .flatMap(cutJarSuffix)
          .toVector
          .sortWith(stringIgnoreCase)
      }.fold(_ => ???, identity)
    }
    .fold(???)(identity)

  val jarsOozieLib = obtainResource(jarOozieLib)
    .map { f =>
      Using(scala.io.Source.fromFile(f)) { s: BufferedSource =>
        s.getLines()
          .map(_.split("/"))
          .filter(_.length > 1)
          .map(_.last)
          .flatMap(cutJarSuffix)
          .toVector
          .sortWith(stringIgnoreCase)
      }.fold(_ => ???, identity)
    }
    .fold(???)(identity)

  val jarsOozieLibExt = obtainResource(jarOozieLibExt)
    .map { f =>
      Using(scala.io.Source.fromFile(f)) { s: BufferedSource =>
        s.getLines()
          .map(_.split("/"))
          .filter(_.length > 1)
          .map(_.last)
          .flatMap(cutJarSuffix)
          .toVector
          .sortWith(stringIgnoreCase)
      }.fold(_ => ???, identity)
    }
    .fold(???)(identity)

  val jarsSpark = obtainResource(jarSpark)
    .map { f =>
      Using(scala.io.Source.fromFile(f)) { s: BufferedSource =>
        s.getLines()
          .map(_.split("/"))
          .filter(_.length > 1)
          .map(_.last)
          .flatMap(cutJarSuffix)
          .toVector
          .sortWith(stringIgnoreCase)
      }.fold(_ => ???, identity)
    }
    .fold(???)(identity)

  val libToSpark = (jarsOozieLib.toSet -- jarsSpark.toSet).toVector.sortWith(stringIgnoreCase)
  val libExtToSpark = jarsOozieLibExt.toSet -- jarsSpark.toSet//).toVector.sortWith(stringIgnoreCase)
  val libToLibExt = (jarsOozieLib.toSet -- jarsOozieLibExt.toSet).toVector.sortWith(stringIgnoreCase)
  val libExtClean = jarsOozieLibExt.toSet -- jarsOozieLib -- jarsSpark
  val all = jarsOozieLib.toSet ++ jarsSpark.toSet ++ jarsOozieLibExt.toSet
//  writeFile("jarsOozieSparkInitial.txt", jarsOozieSparkInitial)
//  writeFile("jarsOozieLib-spark.txt", libToSpark)
//  writeFile("jarsOozieLibExt-spark.txt", libExtToSpark)
//  writeFile("jarsOozieLib-LibExt.txt", libToLibExt)
//  writeFile("jarsOozieLibExt.txt", jarsOozieLibExt)
//  writeFile("jarsSpark.txt", jarsSpark)
//  writeFile("jarsLibExtClean.txt", libExtClean.toVector.sortWith(stringIgnoreCase))

  writeFile("all", all.toVector.sortWith(stringIgnoreCase))
}
