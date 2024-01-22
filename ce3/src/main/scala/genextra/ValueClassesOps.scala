package genextra

trait ValueClassesOps {

  trait Value[A] {
    def value: A
  }

  implicit class UnwrapOptionOps[A](vas: Option[Value[A]]) {
    def value: Option[A] = vas.map(_.value)
  }

  implicit class UnwrapSeqOps[A](vas: Seq[Value[A]]) {
    def value: Seq[A] = vas.map(_.value)
  }

}
