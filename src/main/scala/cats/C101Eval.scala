package cats

object C101Eval extends App {

  import cats.Eval

  println("::now definition")
  val now:    Eval[Double] = Eval.now { println("EM"); math.random + 100 }       // eager + memo     === val
  println("::later definition")
  val later:  Eval[Double] = Eval.later { println("LM"); math.random + 200 }     // lazy + memo      === lazy val
  println("::always definition")
  val always: Eval[Double] = Eval.always {  println("LNM"); math.random + 300 }  // lazy + non memo, === def

//  val d1: Double = now.value
//  val d2: Double = later.value
//  val d3: Double = always.value

  println("::now running")
  1 to 3 map { _ => now } map { _.value } foreach println
  println("::later running")
  1 to 3 map { _ => later } map { _.value } foreach println
  println("::always running")
  1 to 3 map { _ => always } map { _.value } foreach println

  val saying = Eval.
    always { println("Step 1"); "The cat" }
    .map { str => println("Step 2"); s"$str sat on" }
    .memoize // memoize (cache everything until now)
    .map { str => println("Step 3"); s"$str the mat" }

  saying.value // Step 1, Step 2, Step 3
  saying.value // Step 3
}
