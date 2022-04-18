package monoid

object TheTask {
  /** initial data */
  val data: Seq[String] = Seq("a", "bb", "ccc")

  /** 1. join to one string "abbccc" */
  lazy val r1: String = ???

  /** 2. join to one string "a bb ccc" */
  lazy val r2: String = ???

  /** 3. join to the List */
  lazy val r3: List[String] = ???

  /** 4. join to the Set */
  lazy val r4: Set[String] = ???
}

object NaiveSolution extends App {
  import TheTask.data

  val r1: String       = data.foldLeft(""                ) { (acc, a) => acc + a }
  val r2: String       = data.foldLeft(""                ) { (acc, a) => if (acc.isEmpty) a else s"$acc, $a" }
  val r3: List[String] = data.foldLeft(List.empty[String]) { (acc, a) => acc :+ a }
  val r4: Set[String]  = data.foldLeft(Set.empty[String] ) { (acc, a) => acc + a }
  /** the problem is every time we need to write the new implementation */
  pprint.log(r1)
  pprint.log(r2)
  pprint.log(r3)
  pprint.log(r4)
}

object ImprovementOne extends App {
  import TheTask.data

  /**
    * we spot some patterns
    * and created "empty" values and functions
    */
  val empty1: String = ""
  val f1: (String, String) => String = (acc: String, a: String) => s"$acc$a"
  
  val empty2: String = ""
  val f2: (String, String) => String = (acc: String, a: String) => if (acc.isEmpty) a else s"$acc, $a"
  
  val empty3: List[String] = List.empty
  val f3: (List[String], String) => List[String] = (acc: List[String], a: String) => acc :+ a
  
  val empty4: Set[String] = Set.empty
  val f4: (Set[String], String) => Set[String] = (acc: Set[String], a: String) => acc + a

  /**
    * and decoupled our implementation
    */
  def collect[A, ACC](xs: Seq[A])(empty: ACC)(f: (ACC, A) => ACC): ACC =
    xs.foldLeft(empty)(f)

  val r1: String       = collect(data)(empty1)(f1)
  val r2: String       = collect(data)(empty2)(f2)
  val r3: List[String] = collect(data)(empty3)(f3)
  val r4: Set[String]  = collect(data)(empty4)(f4)
  pprint.log(r1)
  pprint.log(r2)
  pprint.log(r3)
  pprint.log(r4)
}

object ImprovementTwo extends App {
  
  trait Collector[A, ACC] {
    def empty: ACC
    def combine(acc: ACC, a: A): ACC
  }
  
  def collect[A, ACC, C](xs: Seq[A])(implicit c: Collector[A, ACC]): ACC =
    xs.foldLeft(c.empty)(c.combine)

  val cs1: Collector[String, String] = new Collector[String, String] {
    override def empty: String = ""
    override def combine(acc: String, a: String): String = "$acc$a"
  }
  val cs2: Collector[String, String] = new Collector[String, String] {
    override def empty: String = ""
    override def combine(acc: String, a: String): String = if (acc.isEmpty) a else s"$acc, $a"
  }
  val cs3: Collector[String, List[String]] = new Collector[String, List[String] ] {
    override def empty: List[String] = List.empty
    override def combine(acc: List[String], a: String): List[String] = acc :+ a
  } 
  implicit val cs4: Collector[String, Set[String]] = new Collector[String, Set[String]] {
    override def empty: Set[String] = Set.empty
    override def combine(acc: Set[String], a: String): Set[String] = acc + a
  }

  import TheTask.data
  val r1: String       = collect(data)(cs1)
  val r2: String       = collect(data)(cs2)
  val r3: List[String] = collect(data)(cs3)
  val r4: Set[String]  = collect[String, Set[String], Collector[String, List[String]]](data)
  pprint.log(r1)
  pprint.log(r2)
  pprint.log(r3)
  pprint.log(r4)
}