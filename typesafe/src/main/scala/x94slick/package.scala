import slick.jdbc.PostgresProfile.api._;
import slick.lifted.{TableQuery, Tag};

package object x94slick {
  // row representation
  final case class Country(name: String, names: String, id: Long = 0L)

  // table representation, NOT NULL BY DEFAULT !!!
  final class CountryTable(tag: Tag) extends Table[Country](tag, "country") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc);
    def name = column[String]("name", O.Default(""));
    def names = column[String]("names");
    // mapping function: tuple => ...
    def * = (name, names, id).mapTo[Country];
  }

  // db connection
  val db: Database = Database.forConfig("chapter01my")

  // basic query
  lazy val countries: TableQuery[CountryTable] = TableQuery[CountryTable]
}
