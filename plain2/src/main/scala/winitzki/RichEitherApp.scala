package winitzki

object RichEitherApp extends App {

  implicit class EitherExtraSyntax[E, A](private val e: Either[E, A]) extends AnyVal {
    /** just map the left part */
    def leftMap[E2](f: E => E2): Either[E2, A] = e match {
      case Left(e) => Left(f(e))
      case Right(a) => Right(a)
    }
    /** map left part, but only to certain criteria */
    def leftRefine[E2 <: E](f: E => E2): Either[E2, A] = leftMap(f)
    def leftWiden[E2 >: E](f: E => E2): Either[E2, A] = leftMap(f)
  }

  class A
  class B extends A
  class C extends B
  class D

  /** doesn't compile, wrong place in the hierarchy */
  //  def test_refine1(e: Either[B, Int]) = e.leftRefine { _ => new A }
  /** works well, part of hierarchy */
  def test_refine2(e: Either[B, Int]) = e.leftRefine { _ => new B }
  /** works well, part of hierarchy */
  def test_refine3(e: Either[B, Int]) = e.leftRefine { _ => new C }
  /** doesn't compile: D doesn't belong to A -> B -> C hierarchy */
  //  def test_refine4(e: Either[B, Int]) = e.leftRefine { _ => new D }

  /** works well, part of hierarchy */
  def test_widen1(e: Either[B, Int]) = e.leftWiden { _ => new A }
  /** works well, part of hierarchy */
  def test_widen2(e: Either[B, Int]) = e.leftWiden { _ => new B }
  /** works, but seems weird to me */
  def test_widen3(e: Either[B, Int]) = e.leftRefine { _ => new C }
  /** doesn't compile: D doesn't belong to A -> B -> C hierarchy */
  //  def test_widen4(e: Either[B, Int]) = e.leftRefine { _ => new D }

}
