package topics.unapply

object UnApply2 extends App {
  
  case class Person(age: Int, name: String)
  
  def hello(p: Person) = {
    if (p.age < 18) s"Hi, ${p.name}"
    else if (p.age < 50) s"Hello, ${p.name}"
    else s"Good evening, ${p.name}, how are you?" 
  }
  
  object IsYoung {
    def unapply(p: Person) = if (p.age < 18) Some(p.name) else None
  }

  object IsAdult {
    def unapply(p: Person) = if (!(p.age < 18) && (p.age < 50)) Some(p.name) else None
  }

  object IsElderly {
    def unapply(p: Person) = if (!(p.age < 50)) Some(p.name) else None
  }
  
  def hello2(p: Person) = p match {
    case IsYoung(n)   => s"Hi, $n!"
    case IsAdult(n)   => s"Hello, $n" 
    case IsElderly(n) => s"Hello $n, how are you?"
    case _            => sys.error("hello2. non-covered case")
  }

  List(
    Person(10, "Alex"),
    Person(20, "Jim"),
    Person(60, "Jeremy"),
  )
    .map(hello2)
    .foreach(println)

}
