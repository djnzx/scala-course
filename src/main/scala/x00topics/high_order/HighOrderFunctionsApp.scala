package x00topics.high_order

object HighOrderFunctionsApp extends App {

  // "normal" function
  type FII = Int => Int
  //  name   signature     params     code
  val mul10: Int => Int = (x: Int) => x * 10
  val mul11:    FII     = (x: Int) => x * 10
  val mul12:    FII     = mul11
  // usage
  val x = mul10(5)
  val y = mul11(6)
  val z = mul12(7)

  type FTI = (Int, Int) => Int
  //  name    signature           params              code
  val mul21: (Int, Int) => Int = (x: Int, y: Int) => x * y
  val mul22:           FTI     = (x: Int, y: Int) => x * y
  // any type can be inferred if we specify the function type
  val mul23:           FTI     = (x, y)           => x * y
  // or can't if we dont use function type
  val mul24                    = (x: Int, y: Int) => x * y

  // "high-order" function. function return function
  type HOFII = () => (Int) => Int
  val mul51: () => (Int) => Int = () => (x: Int) => x * 10
  val mul52:       HOFII        = () => (x: Int) => x * 10
  // any type can be inferred if we specify the function type
  val mul53:       HOFII        = () => (x) => x * 10
  // or can't if we dont use function type
  val mul54                     = () => (x: Int) => x * 10
  // usage
  val f: Int => Int =  mul51()
  val result2 = mul51()(10)
  val result1 = f(10)
}
