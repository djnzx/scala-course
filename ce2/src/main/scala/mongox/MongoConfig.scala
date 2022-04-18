package mongox

final case class MongoConfig(host: String, port: Int, db: String) {
  def url: String = s"mongodb://$host:$port"
}
