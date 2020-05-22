package aa_cookbook.x001

object RegExpExtract extends App {
  def case1 = {
    val pattern = """([0-9]+)\s+([A-Za-z]+)\s+(\w+)""".r // we can omit DOUBLE backslash in case of using triple quotes
    val pattern(count, name, extra) = "100   bananas   extra"
    println(count)
    println(name)
    println(extra)

    val p1 = "ab*c".r
    val p1Matches = "abbbc" match {
      case p1() => true // no groups
      case _ => false
    }
    println(p1Matches)

    val p2 = "a(b*)c".r
    val p2Matches = "abbbc" match {
      case p2(_*) => true // any groups
      case _ => false
    }
    println(p2Matches)
    val numberOfB = "abbbc" match {
      case p2(x) => Some(x.length) // one group
      case _ => None
    }
    println(numberOfB)

    val p3 = "b*".r.unanchored
    val p3Matches = "abbbc" match {
      case p3() => true // find the b's
      case _ => false
    }
    println(p3Matches)
    val p4 = "a(b*)(c+)".r
    val p4Matches = "abbbcc" match {
      case p4(_*) => true // multiple groups
      case _ => false
    }
    println(p4Matches)
    val allGroups = "abbbcc" match {
      case p4(all@_*) => all.mkString("[", "/", "]") // "bbb/cc"
      case _ => ""
    }
    println(allGroups)
    val cGroup = "abbbcc" match {
      case p4(_, c) => c
      case _ => ""
    }
    println(cGroup) // cc
  }

  def search(zip: String) = {
    println(s"searching by zipcode:$zip")
  }

  def search(city: String, state: String) = {
    println(s"searching by city:$city and state:$state")
  }

  def case2(input: String) = {
    val matcher1 = "movies (\\d{5})".r
    val matcher2 = "movies near\\s+([A-Za-z]+),\\s+([A-Za-z]{2})".r

    input match {
      case matcher1(zip) => search(zip)
      case matcher2(city, state) => search(city, state)
      case _ => println("didn't match any pattern")
    }
  }

//  case2("movies 07203")
//  case2("movies near Brooklyn, NY")
//  case2("movies near Roselle NJ")
// https://stackoverflow.com/questions/8177143/regex-to-match-a-digit-two-or-four-times
  def case3(input: String) = {
    val dateMatcher = """(\d{1,2}).(\d{2}).(\d{2}(?:\d{2}))""".r

    input match {
      case dateMatcher(d,m,y) => println( s"matched! d:$d, m$m, y$y");
      case _ => println("didn't match")
    }
  }
  case3("1.01.17")
  case3("11.10.17")
  case3("11.10.2017")
  case3("11.10.200")
}
