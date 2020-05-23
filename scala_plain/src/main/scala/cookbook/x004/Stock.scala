package cookbook.x004

class Stock {
  // setter and getter will be generated as well
  var delayedValue: Double = _

  // no getter, no setter because of private
  private var currentValue: Double = _
  private [this] var calculated: Double = _

  // won't compie because of 'private [this] var calculated'
//  def isHigher(that: Stock) = this.currentValue > that.calculated
}
