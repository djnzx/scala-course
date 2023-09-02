package slickvt

import akka.NotUsed
import akka.stream.scaladsl.Source
import model._
import slick.jdbc.PostgresProfile.api.{Tag => _, _}
import slick.lifted.Rep
import slickvt.model.TagFilter.{ExcludesOnly, IncludesAndExcludes, IncludesOnly}
import slickvt.model.TargetStrategies._

trait Impl extends StreamOps with SlickStreamingQueryOps {

  private def negateBy(strategy: TargetStrategy): Rep[Boolean] => Rep[Boolean] =
    if (strategy == Exclude) !_ else identity

  private def whereCountry(query: ProfileTable, country: CountryFilter) = country match {
    case CountryFilter(Seq(), _) => query.country.isDefined
    case CountryFilter(countries, strategy) => query.country
      .map { country => negateBy(strategy)(country inSet countries) }
      .getOrElse(LiteralColumn(false))
  }

  private def whereAffiliate(profiles: ProfileTable, affiliate: AffiliateFilter) = affiliate match {
    case AffiliateFilter(Seq(), _) => LiteralColumn(true)
    case AffiliateFilter(affiliates, strategy) => profiles.affiliateId
      .map { id => negateBy(strategy)(id inSet affiliates) }
      .getOrElse(LiteralColumn(strategy == Exclude))
  }

  private def wherePlayerUuids(profiles: ProfileTable, playerUuids: PlayerUuidFilter) =
    profiles.playerUUID inSet playerUuids.includeUuids

  def qCond(p: Boolean)(r: Rep[Boolean]): Rep[Boolean] = if (p) r else !r

  private def mkTagWhereClause(rqTags: Seq[PTag])(tags: TagTable): Rep[Boolean] =
    rqTags.groupMap(_.rank)(_.tagId)
      .map { case (rank, tagIds) => (tags.rank === rank) && (tags.tagId inSet tagIds) }
      .reduce(_ || _)

  def mkProfilesFilter(request: RQuery) =
    ProfileTable.profiles // case class RQuery(
      .filter(_.brandId === request.brandId) //   brandId: String,
      .filter(profiles => qCond(request.isTest)(profiles.playerUUID.like(TestPattern))) //   isTest: Boolean = false
      .filterOpt(request.affiliateFilter)(whereAffiliate) //   affiliateFilter: Option[AffiliateFilter] = None
      .filterOpt(request.countryFilter)(whereCountry) //   countryFilter: Option[CountryFilter] = None
      .filterOpt(request.playerUuidFilter)(wherePlayerUuids) //   playerUuidFilter: Option[PlayerUuidFilter] = None
      .filterOpt(request.playerId)(_.playerUUID === _) //   playerId: Option[String] = None

  private def mkTagsFilter(request: RQuery): Query[TagTable, Tag, Seq] =
    TagTable.tags
      .filter(_.brandId === request.brandId)
      .filterOpt(request.playerUuidFilter)(_.playerUUID inSet _.includeUuids)
      .filterOpt(request.playerId)(_.playerUUID === _)


  private def doFindProfilesWithTags(request: RQuery) = {
    val profilesFiltered = mkProfilesFilter(request)
    val tagsFiltered = mkTagsFilter(request)

    val tagsFiltered2 = request.tagFilter match {
      case Some(IncludesOnly(includes)) =>
        tagsFiltered
          .filter(mkTagWhereClause(includes))
      case Some(ExcludesOnly(excludes)) =>
        tagsFiltered
          .filterNot(mkTagWhereClause(excludes))
      case Some(IncludesAndExcludes(includes, excludes)) =>
        tagsFiltered
          .filter(mkTagWhereClause(includes))
          .filterNot(mkTagWhereClause(excludes))
      case _ =>
        tagsFiltered
    }

    profilesFiltered
      .joinLeft(tagsFiltered2).on(_.playerUUID === _.playerUUID)
      .map{ case (p, ots) => p -> ots.map(t => t.tagId -> t.rank)}
      .sortBy { case (p, _) => p.playerUUID }
      .result
  }

  def findProfilesWithTagsSource(request: RQuery) = {
    val s = runStream(doFindProfilesWithTags(request))

    regroupSequentialAfterJoin(s)(_._1)(_._2)
      .map { case (p, ts) => p -> ts.flatten }
  }

}
