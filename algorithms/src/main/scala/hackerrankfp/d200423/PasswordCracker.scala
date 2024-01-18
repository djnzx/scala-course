package hackerrankfp.d200423

/**
  * https://www.hackerrank.com/challenges/password-cracker/problem
  * 12 pcs did not execute within the time limits...
  */
object PasswordCracker extends App {
  def passwordCracker(pwds: Seq[String], attempt: String): String = {

    def crack0(part: String, parts: List[String]): Option[List[String]] =
      if (part.isEmpty) Some(parts) else
      pwds.sortWith { (s1, s2) => s2.length < s1.length }
        .flatMap { pwd => if (pwd.length <= part.length && pwd == part.substring(0, pwd.length)) Some(pwd) else None }
        .flatMap { sub => crack0(part.substring(sub.length), sub :: parts) }
        .headOption

    var i = 0

    def crack(part: String, parts: List[String]): Option[List[String]] = {
//      println(s"i: ${i+=1;i} part: $part, parts: $parts")
      println(s"${parts.reverse.mkString(" ")}")

      if (part.isEmpty) Some(parts) else
        pwds
          .sortWith { (s1, s2) => s2.length < s1.length }
          .flatMap { pwd => if (pwd.length <= part.length && pwd == part.substring(0, pwd.length)) Some(pwd) else None }
          .foldLeft((true, Option.empty[List[String]])) { (a, b) =>
            (a, b) match {
              case ((false, list), _  ) => (false, list)
              case ((true, _    ), pwd) => crack(part.substring(pwd.length), pwd :: parts) match {
                case Some(value) => (false, Some(value))
                case None => (true, None)
              }
            }
          }
          ._2
    }

    (if ((List(attempt).flatten.toSet -- pwds.flatten.toSet).nonEmpty) None
    else crack(attempt, Nil) map { _.reverse.mkString(" ") }
      ) getOrElse "WRONG PASSWORD"
  }

  def process(vocab: Vector[String], attempt: String): String = {
    println(attempt)
    passwordCracker(vocab, attempt)
  }

  def body(readLine: => String) = {
    val N: Int = readLine.toInt

    (1 to N)
      .map { _ =>
        readLine
        process(
          readLine.split(" ").toVector,
          readLine) }
      .foreach { println }
  }

  /**
    * main implementation for reading from the console
    */
//  def main(p: Array[String]): Unit = {
//    body { scala.io.StdIn.readLine }
    //    main_f(p)
//  }
  /**
    * main implementation for reading from the file
    */
//  def main_f(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File("src/main/scala/hackerrankfp/d200423_03/passwd.txt"))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
//  }
//
//
//
//  val pwds = List(
//    "ends", "open", "stop", "vest", "love"
//
//    //    "the", "cake", "is", "a", "lie", "thec", "ak", "ei", "sal", "ie"
////    "because", "can", "do", "must", "we", "what"
////    "a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa"
//  )
//  val attempt =
//    "vestloveendslovevestloveloveloveendslovevestendsopenloveopenstoploveopenendsopenopenstopvestopenloveloveendsvestopenlovevestopenlovestopendsvestveststopendsendsvestendsloveopenendsstopstopstopveststopveststoplovestoplovestopendsvestveststopopenstopveststoploveveststopstopopenvestveststopvestvestopenvestopenlovevestlovevestopenendslovevestvestlovevestvestendsopenopenopenstopopenendsloveopenopenendsstopopenstopstopstopendsendsstopveststoplovestopstopopenloveopenopenloveendsvestendsendsstopopenopenendsendsstopstoploveloveopenvestopenopenopenstopendslovestopendsstopopenopenstopvestloveendsloveveststopstopopenveststopstopvestlovevestendsveststopstopendsendsendslovestopstoplovelovestopopenendsloveloveloveendslovestopstopvestveststopopenvestvestendsvestopenendslovevestvestvestloveloveopenveststopopenendsendslovevestopenloveopenvestlovestoplovestopvestloveendsloveopenvestvestendsendsvestvestopenopenstopvestopenopenloveopenopenstopvestopenlovelovevestendsopenendsopenopenvestopenendsendsstoplovestopopenopenstopopenlovelovestopopenlovestopvestvestlovevestloveloveendsstopveststoplovestoploveloveloveveststopstoplovelovelovevestendslovestopvestvestvestlovelovestopopenveststopendsloveopenveststopopenvestloveendsvestvestvestlovestoploveopenendsendsendsstoploveopenendsvestveststopopenlovelovevestvestloveendsvestendsveststopopenveststopendsstoploveopenendsstoploveopenstopopenendsvestopenopenstopstopstopendsopenstoplovestoploveopenlovestopendsopenloveendsendsopenloveloveendsopenlovestopendsopenopenveststopopenopenloveendsendsopenendsendsopenvestvestendsopenendslovevestendsendsendslovestopendsstopopenendsloveopenvestopenendsloveendsstoplovevestveststopopenstopstopstopstopstopstopopenloveendsopenopenlovevestlovelovevestendsvestendsendslovestopopenvestendsvestlovestopopenopenopenopenendsopenopenopenstopopenopenopenendsstopstopstopendsendslovestopopenopenopenlovevestendsvestendsloveopenopenstoplovevestvestopenvestlovestopendsvestvestopenlovevestopenlovelovestopstopopenvestlovelovestopvest"
////    "thecakeisaliethecakeisalieakthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieathecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethethecakeisaliethecakeisaliethecakeisaliethecakeisaliethethecakeisalieakthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliesalthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliesalthecakeisaliethecakeisalielieakthecakeisalieliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieakthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieeithecakeisaliethecakeisalieeithecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieiethecakeisaliethecakeisaliethecakeisaliethecakeisalieisthecakeisaliethecakeisalieiscakeakthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieakcakethecakeisaliethecieiethecthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieeithecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieathecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieeithecakeisaliethecakeisaliethecakeisaliethecakeisalieacakethecakeisaliethecakeisaliesalthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalieakthecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisaliethecakeisalie"
////    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
////    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
////  "wedowhatwemustbecausewecan"
//  val r = passwordCracker(pwds, attempt)
//  println(r)
}
