package nomicon.ch04

import java.io.IOException

import zio._

object Ch41 extends App {
  
  /**
    * this signature is lying,
    * because it can throw ArithmeticException
    */
  val divByZero: ZIO[Any, Nothing,        Int] = UIO.effectTotal(1 / 0)
  val dbz_sand:  ZIO[Any, Cause[Nothing], Int] = divByZero.sandbox
  
  val dbz:       ZIO[Any, Nothing,        Int] = dbz_sand.unsandbox

  //              (failure: E => ZIO[R1, E2, B], success: A => ZIO[R1, E2, B]): ZIO[R1, E2, B]
//  divByZero.foldM()
  //              (failure: Cause[E] => ZIO[R1, E2, B],success: A => ZIO[R1, E2, B]): ZIO[R1, E2, B]
//  divByZero.foldCauseM()
  /**
    * the same error on the different levels should be treated in different way
    * so, low level defects can be recoverable errors on higher levels
    */
  def readFile(file: String): ZIO[Any, IOException, String] = ???

  /** I don't want to deal with IOException, any type of exception will be converted to Nothing */
  lazy val result: ZIO[Any, Nothing, String] = readFile("data.txt").orDie

  def readFile2(file: String): ZIO[Any, Throwable, String] = ???
  /** I'm interested only in IOException, 
    * everything else is not interesting for me */
  def readFile2Refined1(file: String) = readFile2(file)
    .refineOrDie {
      case e: IOException => e
    }
  /** mostly used only in situations when only one type of error is anticipated */
  def readFile2Refined2(file: String) = readFile2(file)
    .refineToOrDie[IOException]
  /** if we wish to change error somehow */
  def readFile2Refined3(file: String) = readFile2(file)
    .refineOrDieWith {
      case e: IOException => e
    } (identity)

  lazy val example = ZIO.effect(try {
    throw new Exception("Error using file!")
  } finally {
    throw new Exception("Couldn't close file!") // we will see only this
  })
    .orElse(console.putStrLn("HZ..."))

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = example.exitCode
}
