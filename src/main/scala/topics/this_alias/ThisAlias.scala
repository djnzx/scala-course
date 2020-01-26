package topics.this_alias

object ThisAlias extends App {
  // to prevent any possible ambiguity (for example in complicated nested things)!
  self_ =>

  val a = 5;
  println(this.a)
  println(self_.a)
}
