package aa_cookbook.x004

class Person (var firstName: String, var lastName: String) {
  println("the constructor begins")

  private val HOME = System.getProperty("user.home")
  var age = 0

  override def toString: String = s"$firstName $lastName is $age years old"
  def printHome: Unit = println(HOME)
  def printFullName: Unit = println(this)
//  override def lastName_$eq(name: String) { this.lastName = name }

  printHome
  printFullName

  println("still in the constructor")

}
