package ml

object GradientDescent extends App {

  def f(x: Double): Double = math.abs(x * x * x) - 3 * x * x + x

  def findLocalMin(f: Double => Double, x0: Double): Double = {
    // TODO: how to setup ?
    val precision = 0.000001
    // TODO: how to setup ?
    var iter = 100
    // TODO: how to setup ?
    var step_cur = 0.1
    // TODO: how to setup ?
    var step_prev = 1.0

    var x_prev = x0
    var y_prev = f(x_prev)

    var x_cur = x0

    while (step_prev > precision && iter > 0) {
      iter -= 1
      val y_cur = f(x_cur)
      if (y_cur > y_prev) step_cur = -step_cur / 2
      x_prev = x_cur
      x_cur += step_cur * y_prev
      y_prev = y_cur
      step_prev = math.abs(x_cur - x_prev)
    }

    x_cur
  }

  println(findLocalMin(f, 1))  //  1.8164978634483617
  println(findLocalMin(f, -3)) // -2.1547001161359347

}

