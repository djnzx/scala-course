package diwo

/**  messages */
object msg {
  def mustEq(must: Int, given: Int) = s"must equal $must, $given given"
  def mustBtw(mn: Int, mx: Int, given: Int) = s"must be in range[$mn, $mx], $given given"
  def cn(s: String) = s"count of numbers $s"
  def csn(s: String) = s"count of star numbers $s"
  def nt(s: String) = s"Normal ticket $s"
  def st(s: String) = s"System ticket $s"
  def dr(s: String) = s"Draw $s"
  def dr_parse(s: String) = s"Draw parse error, $s given"
  def file_not_found(s: String) = s"file $s not found"
  def file_is_empty(s: String) = s"file $s is empty"
  def size(s: String) = s"size $s"
}
