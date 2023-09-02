package slickvt

object model {

  object TargetStrategies extends Enumeration {
    type TargetStrategy = Value
    val Include = Value("INCLUDE")
    val Exclude = Value("EXCLUDE")
  }

  import TargetStrategies._

  /** case classes to remap gRPC entities */
  case class PTag(tagId: String, rank: Int = 0)
  case class PlayerUuidFilter(includeUuids: Seq[String])
  case class CountryFilter(countries: Seq[String], strategy: TargetStrategy)
  case class AffiliateFilter(affiliates: Seq[String], strategy: TargetStrategy)
  case class TagFilter(includeTags: Seq[PTag], excludeTags: Seq[PTag])

  object TagFilter {
    object IncludesOnly {
      def unapply(tf: TagFilter): Option[Seq[PTag]] = Option.when(tf.includeTags.nonEmpty && tf.excludeTags.isEmpty)(tf.includeTags)
    }

    object ExcludesOnly {
      def unapply(tf: TagFilter): Option[Seq[PTag]] = Option.when(tf.includeTags.isEmpty && tf.excludeTags.nonEmpty)(tf.excludeTags)
    }

    object IncludesAndExcludes {
      def unapply(tf: TagFilter): Option[(Seq[PTag], Seq[PTag])] = Option.when(tf.includeTags.nonEmpty && tf.excludeTags.nonEmpty)(tf.includeTags -> tf.excludeTags)
    }
  }

  final val TestSuffix = "-test"
  final val TestPattern = s"%$TestSuffix"

  case class RQuery(
    brandId: String,
    playerId: Option[String] = None,
    countryFilter: Option[CountryFilter] = None,
    affiliateFilter: Option[AffiliateFilter] = None,
    tagFilter: Option[TagFilter] = None,
    playerUuidFilter: Option[PlayerUuidFilter] = None,
    isTest: Boolean = false
  )

}
