package cats.kleisli

object KleisliApp08 extends App {
  val r = scala.util.Random

  // given

  val generate: Unit => Int = _ => r.nextInt(100)
  val process: Int => Int = v => (v * math.Pi).toInt
  val save: Int => Boolean = _ => true

  // way 1
  val generated: Int = generate()
  val processed: Int = process(generated)
  val saved: Boolean = save(processed)
  println(s"Result is: ${saved}")

  // way 2
  val combine_1: Unit => Boolean = _ => save(process(generate()))
  println(s"Result is: ${combine_1}")

  // way3
  val combine_2 = save compose process compose generate
  println(s"Result is: ${combine_2}")

  // way 3
  val combine_3: Unit => Boolean = generate andThen process andThen save
  println(s"Result 3 is: ${combine_3}")

}
