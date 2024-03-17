### What mdoc is?

my project version is **@VERSION@**

- attaches the output
```scala mdoc
  List(1,2,3) match {
    case Nil        => println("The list is empty")
    case x @ h :: _ => println(s"$x starts from: $h")
  }
```
- attaches the output, even if just an expression
```scala mdoc
case class Point2d(x: Int, y: Int)
val p = Point2d(10, 20)
```
- handles compiler errors
```scala mdoc:fail
sealed trait Color
case object Red extends Color
case object Yellow extends Color
case object Green extends Color

def show(c: Color): String = c match {
  case Red => "Red"
  case Green => "Green"
}
```
- handles runtime exceptions
```scala mdoc:crash
println(1/0)
```
- has huge postprocessing ability 

```scala mdoc:evilplot:assets/scatterplot.png
import com.cibo.evilplot._
import com.cibo.evilplot.plot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
import com.cibo.evilplot.numeric.Point

val data = Seq.tabulate(90) { i =>
  val degree = i * 8
  val radian = math.toRadians(degree)
  val p = Point(i.toDouble, math.sin(radian)*100)
  println(p)
  p
}

ScatterPlot(data)
        .xAxis()
        .yAxis()
        .frame()
        .xLabel("x")
        .yLabel("y")
        .render()
```

- has integration with Docusaurus:
  - [site](https://docusaurus.io)
  - [github](https://github.com/facebook/docusaurus)