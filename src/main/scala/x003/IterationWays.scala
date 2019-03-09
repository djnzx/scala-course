package x003

object IterationWays extends App {
  for (i <- 0 to 1; j <- 4 to 5) {
    println(s"i: $i, j:$j")
  }

  for {
    i <- 0 to 1
    j <- 4 to 5
  } println(s"i: $i, j:$j")

  val a = Array.ofDim[Int](5,2)
  var counter =0
  // fill
  for {
    i <- a.indices // 0..4
    j <- a(0).indices // 0..1
  } {
    a(i)(j)=counter
    counter+=1
  }
  // print
  for {
    i <- a.indices
    j <- a(0).indices
  } println(s"i: $i, j:$j, a[$i][$j]=${a(i)(j)}")

  for (i <- 1 to 10 if i % 2 == 0 ) println(i)

  for {
    i <- 1 to 10
    if i % 2 == 0
    if i > 3
    if i < 6
  } println(i)

}
