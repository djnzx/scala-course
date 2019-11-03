package x00lessons.tips12

class Tip09PredefValForExpectedErrors {
  // don't write partial function!
  def parseInt(s: String): Int = ???
  // use poser of compiler
  def parseInt(s: String): Option[Int] = ???

  /**
    * Option: Some / None    - no explanation
    * Try: Success/ Failure  - can keep Throwable
    * Either: Left / Right   - Any(Left)
    * ZIO
    */
}
