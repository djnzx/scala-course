package amt.parts

object AMTQueryPrice extends App {

  val amt = new AMT(Credentials(???, ???))

  val query0 = "12251PPA004"
  val query1 = Array("560059", "56005900", "L3K9124X0C")
  val query2 = Iterable("82110037331", "53020444")

  amt
    .fetchNumberDetails(query0)
    .foreach(println)

  amt
    .fetchNumberDetails(query1)
    .foreach(println)

  amt
    .fetchNumberDetails(query2)
    .foreach(println)

}
