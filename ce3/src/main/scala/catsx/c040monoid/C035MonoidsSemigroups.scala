package catsx.c040monoid

object C035MonoidsSemigroups {
  /**
    * Integer addi􏰀on: a + b
    * - binary operation: (a, b)
    * - closed (always produces another value)
    * - has identity element (0): a + 0 = 0 + a = a
    * - it is associative: a + b == b + a
    *
    * Monoid has:
    * combine: (A, A) => A
    * empty: A = A `op` empty == empty `op` A
    */
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid
  }

  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = {
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
  }

  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = {
    (m.combine(x, m.empty) == x) && (m.combine(m.empty, x) == x)
  }

  /** let think about boolean:
    * binary operations are:
    * - OR   - monoid
    * - AND  - monoid
    * - XOR  - monoid
    * - NXOR - monoid
    */
  class BoolOR extends Monoid[Boolean] {
    override def empty: Boolean = false
    override def combine(x: Boolean, y: Boolean): Boolean = x || y
  }

  class BoolAND extends Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = x && y
  }

  class BoolXOR extends Monoid[Boolean] {
    override def empty: Boolean = false
    override def combine(x: Boolean, y: Boolean): Boolean = (x && !y) || (!x && y)
  }

  class BoolNXOR extends Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = !((x && !y) || (!x && y))
  }

  class SetMonoid[A] extends Monoid[Set[A]] {
    override def empty: Set[A] = Set.empty[A]
    override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
  }
}
