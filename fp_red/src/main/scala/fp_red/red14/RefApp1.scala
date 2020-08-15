package fp_red.red14

object RefApp1 extends App {
  
  /** mutable variable wrapped into ST[STRef],
    * can't be used without proper context
    */
  val i0: ST[Nothing, STRef[Nothing, Int]] = STRef(10)

  val i1 = for {
    r <- STRef[Nothing, Int](10)
    a <- r.read
  } yield a
  
  val rst2: RunnableST[Int] = new RunnableST[Int] {
    override def apply[S]: ST[S, Int] = for {
      r <- STRef(10)
      a <- r.read
    } yield a
  }
  
  /** 
    * can be used because of proper wrapping
    * into RunnableST
    */
  val r: Int = ST.runST(rst2)
  pprint.log(r)
  
}
