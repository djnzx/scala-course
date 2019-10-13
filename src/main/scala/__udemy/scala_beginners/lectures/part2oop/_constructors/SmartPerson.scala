package __udemy.scala_beginners.lectures.part2oop._constructors

// primary constructor
class SmartPerson(val name: String, val surName: String, val age: Int) {
  def this(name: String) = this(name, "", -1)
  def this(name: String, surName: String) = this(name, surName, -2)
  override def toString = s"SmartPerson{name=$name, surName=$surName, age=$age}"
  def toString1 = "SmartPerson{name=%15s, surName=%-20s, age=%d}".format(name, surName, age)
}
