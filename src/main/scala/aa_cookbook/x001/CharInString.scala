package aa_cookbook.x001

object CharInString extends App {
  val s = "Hello. World"
  println(s.length)
  println(s.getBytes.length)

  val s1 = "Мир привет!"
  println(s1.length)
  println(s1.getBytes.length)

  for (i <- Range(0, s1.length)) {
    print(s1(i)) // s1.apply()
    println(" " + s1.codePointAt(i) )
  }


}
