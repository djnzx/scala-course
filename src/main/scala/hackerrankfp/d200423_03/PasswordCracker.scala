package hackerrankfp.d200423_03

/**
  * https://www.hackerrank.com/challenges/password-cracker/problem
  * did not execute within the time limits...
  */
object PasswordCracker extends App {

  def passwordCracker(pwds: Seq[String], attempt: String): String = {
    def crack(part: String, parts: List[String]): Option[List[String]] = part match {
      case "" => Some(parts)
      case _ => pwds
        .flatMap { pwd => if (pwd.length <= part.length && pwd == part.substring(0, pwd.length)) Some(pwd) else None }
        .flatMap { sub => crack(part.substring(sub.length), sub::parts) }
        .headOption
    }

    crack(attempt, Nil) map { _.reverse.mkString(" ") } getOrElse "WRONG PASSWORD"
  }

  val r = passwordCracker(List(
    "a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa"),
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
//    "because", "can", "do", "must", "we", "what"), "wedowhatwemustbecausewecan"
  )
  println(r)
}
