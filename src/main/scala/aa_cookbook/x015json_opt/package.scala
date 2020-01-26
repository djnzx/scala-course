package object x015json_opt {

  // data to process
  val personSome: OptPerson = OptPerson("Alex", 42, Some("Trainer"))
  val personNone: OptPerson = OptPerson("Dima", 33, None)
  val stringSome: String = """
      |{
      |   "name":"Alexey",
      |   "age":43,
      |   "extra":"Mentor"
      |}
      |""".stripMargin
  val stringNone: String = """
      |{
      |   "name":"Dmytro",
      |   "age":34
      |}
      |""".stripMargin

}
