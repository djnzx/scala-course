package x009

object P5ClosuresApp extends App {
  var hello = "Hello"
  def sayHello(name: String) { println(s"$hello, $name") }

  val foo = new x008.Foo
  foo.exec(sayHello, "A1")

  hello = "Hola"
  foo.exec(sayHello, "A2")

  var votingAge = 18
  val isOfVotingAge = (age: Int) => age >= votingAge
  val isOfVotingAge2: Int => Boolean = age => age >= votingAge
  val isOfVotingAge3: Int => Boolean = _ >= votingAge

  println(isOfVotingAge(16))
  println(isOfVotingAge(21))

  votingAge = 16
  println(isOfVotingAge(16))
  println(isOfVotingAge(21))



}
