package fps

class XIO[A] private (body: => A) {
  def run: A = body
  def map[B](f: A => B):         XIO[B] = XIO(f(this.run))
  def flatMap[B](f: A => XIO[B]): XIO[B] = XIO(f(this.run).run)
}

object XIO {
  def apply[A](a: => A): XIO[A] = new XIO(a)
}





