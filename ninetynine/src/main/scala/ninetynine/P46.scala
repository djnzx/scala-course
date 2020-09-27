package ninetynine

/**
  * Truth tables for logical expressions
  */
object P46 {
  type B = Boolean
  type F2 = (B, B) => B
  
  trait BF2 {
    def op: F2
  }
  
  def not(x: B) = !x
  
  val and = new BF2 {
    override def op: F2 = _ & _
    override def toString: String = "AND"
  }
  val or = new BF2 {
    override def op: F2 = _ | _
    override def toString: String = "OR"
  }
  val xor = new BF2 {
    override def op: F2 = _ ^ _
    override def toString: String = "XOR"
  }
  
//  def and(x: B, y: B) = x & y
//  def or(x: B, y: B) = x | y
//  def xor(x: B, y: B) = x ^ y

  def boolVals = Seq(true, false) 
  
  implicit class Bool5(private val x: B) extends AnyVal {
    def rp = "%-6s".format(x)
  }
  
  def header(f: BF2) = s"  A      B     ${f.toString}  " 
  
  def table2wo(f: BF2) =
    for {
      a <- boolVals
      b <- boolVals
      r = f.op(a, b)
    } yield s"${a.rp} ${b.rp} ${r.rp}"

  def table2(f: BF2) = header(f) +: table2wo(f) :+ ""
  
}

class P46Spec extends NNSpec {
  import P46._
  
  it("1") {
    val r = Seq(or, and, xor)
      .map(table2(_).mkString("\n"))
      .mkString("\n")
    
    Predef.println(r)
  }
}
