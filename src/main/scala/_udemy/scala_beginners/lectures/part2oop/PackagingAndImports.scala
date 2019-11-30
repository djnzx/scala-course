package _udemy.scala_beginners.lectures.part2oop

import java.util.Date
import java.sql.{Date => SqlDate}

import _udemy.scala_beginners.lectures.part2oop._wip.Writer
import _udemy.scala_beginners.playground.{PrinceCharming, Princess}

/**
  * Created by Daniel.
  */
object PackagingAndImports extends App {

  // package members are accessible by their simple name
  val writer = new Writer("Daniel", "RockTheJVM", 2018)

  // import the package
  val princess = new Princess  // playground.Cinderella = fully qualified name

  // packages are in hierarchy
  // matching folder structure.

  // package object
  sayHello
  println(SPEED_OF_LIGHT)

  // imports
  val prince = new PrinceCharming

  // 1. use FQ names
  val date = new Date
  val sqlDate1 = new Date(2018, 5, 4)
  val sqlDate2 = new SqlDate(2018, 5, 4)
  // 2. use aliasing

  // default imports
  // java.lang - String, Object, Exception
  // scala - Int, Nothing, Function
  // scala.Predef - println, ???

}
