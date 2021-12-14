package udemy.scala_beginners.lectures.part3fp._list._immutable_content

class Persn(name: String, age: Int) {
  // v1
  def equals1(obj: Any): Boolean = obj match {
    case that: Persn => obj.isInstanceOf[Persn] && this.hashCode == that.hashCode
    case _           => false
  }

  // v2
  def equals2(obj: Any): Boolean = obj match {
    case that: Persn => obj.isInstanceOf[Persn] && this.hashCode == that.hashCode
    case _           => ???

  }

  override def equals(obj: Any): Boolean =
    obj.isInstanceOf[Persn] && this.hashCode == obj.hashCode

  override def hashCode(): Int = {
    Seq(name, age).map(_.##)./:(0)((a, b) => 31 * a + b)
  }

  override def toString = s"Persn{name: $name, age: $age}"
}

object Persn {
  def apply(name: String, age: Int): Persn = new Persn(name, age)
}
