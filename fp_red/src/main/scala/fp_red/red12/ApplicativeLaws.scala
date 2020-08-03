package fp_red.red12

/**
  * functor +
  * Left and right identity
  * map(v)(id) == v
  * map(map(v)(g))(f) == map(v)(f compose g)
  *
  * map2(unit(()), fa)((_,a) => a) == fa
  * map2(fa, unit(()))((a,_) => a) == fa
  *
  * assoc:
  * op(a, op(b, c)) == op(op(a, b), c)
  * compose(f, op(g, h)) == compose(compose(f, g), h)
  *
  * product(product(fa,fb),fc) == map(product(fa, product(fb,fc)))(assoc)
  *
  * map2(a,b)(productF(f,g)) == product(map(a)(f), map(b)(g))
  */
object ApplicativeLaws {
  def assoc[A,B,C](p: (A,(B,C))): ((A,B), C) = p match { case (a, (b, c)) => ((a,b), c) }
  def productF[I,O,I2,O2](f: I => O, g: I2 => O2): (I,I2) => (O,O2) = (i,i2) => (f(i), g(i2))
}
