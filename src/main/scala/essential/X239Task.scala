package essential

object X239Task extends App {

  implicit class StringOpsWeird(origin: String) {
    def ~==(another: String): Boolean = origin.equalsIgnoreCase(another)
  }

  assert("abcd" ~== "ABCD")
  assert("Kill" ~== "kiLL")
}
