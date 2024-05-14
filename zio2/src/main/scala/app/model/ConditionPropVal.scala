package app.model

import cats.implicits.catsSyntaxEitherId
import zio.json.JsonDecoder
import zio.json.ast.Json
import app.api
import app.model.Predicate.{PredConstr1, PredConstrN}

private object ConditionPropVal {

  private def readProp(json: Json.Obj): Either[String, api.PropVal] =
    json.get("property")
      .toRight("field `property` not found")
      .flatMap(_.as[api.PropVal])

  private def readPred(json: Json.Obj): Either[String, api.Predicate] =
    json.get("predicate")
      .toRight("field `predicate` not found")
      .flatMap(_.as[api.Predicate])

  private def readValue(json: Json.Obj): Either[String, Json] =
    json.get("value")
      .toRight("field `predicate` not found")

  import app.api.{PropVal => apv}
  import app.model.{PropVal => mpv}
  import app.model.Condition._

  // format: off
  private def resolvePropName: api.PropVal => (JsonDecoder[mpv], Predicate[PropVal] => Condition[PropVal]) = {
    case apv.SportId                    => mpv.SportId.dec.widen[mpv]                    -> SportId
    case apv.CompetitionId              => mpv.CompetitionId.dec.widen[mpv]              -> CompetitionId
    case apv.RegionId                   => mpv.RegionId.dec.widen[mpv]                   -> RegionId
    case apv.MatchId                    => mpv.MatchId.dec.widen[mpv]                    -> MatchId
    case apv.MarketTypeId               => mpv.MarketTypeId.dec.widen[mpv]               -> MarketTypeId
    case apv.SelectionTypeId            => mpv.SelectionTypeId.dec.widen[mpv]            -> SelectionTypeId
    case apv.TypeId                     => mpv.TypeId.dec.widen[mpv]                     -> TypeId
    case apv.TotalCoefficient           => mpv.TotalCoefficient.dec.widen[mpv]           -> TotalCoefficient
    case apv.CoefficientOfAllSelections => mpv.CoefficientOfAllSelections.dec.widen[mpv] -> CoefficientOfAllSelections
    case apv.NumberOfSelections         => mpv.NumberOfSelections.dec.widen[mpv]         -> NumberOfSelections
  }
  // format: on

  private def resolvePredicate(
    pvDec: JsonDecoder[PropVal]
  ): api.Predicate => Either[
    (JsonDecoder[mpv], PredConstr1),
    (JsonDecoder[Set[mpv]], PredConstrN)
  ] = {
    case api.Predicate.in    => (JsonDecoder.set(pvDec) -> Predicate.in).asRight
    case api.Predicate.notin => (JsonDecoder.set(pvDec) -> Predicate.notin).asRight
    case api.Predicate.eq    => (pvDec                  -> Predicate.eq).asLeft
    case api.Predicate.neq   => (pvDec                  -> Predicate.neq).asLeft
    case api.Predicate.gte   => (pvDec                  -> Predicate.gte).asLeft
    case api.Predicate.gt    => (pvDec                  -> Predicate.gt).asLeft
    case api.Predicate.lte   => (pvDec                  -> Predicate.lte).asLeft
    case api.Predicate.lt    => (pvDec                  -> Predicate.lt).asLeft
  }

  val decoder: JsonDecoder[Condition[PropVal]] =
    Json.Obj.decoder.mapOrFail { json =>
      for {
        apiProp  <- readProp(json)
        apiPred  <- readPred(json)
        apiValue <- readValue(json)

        (pvDecoder, condConst) = resolvePropName(apiProp)

        pred <- resolvePredicate(pvDecoder)(apiPred) match {
          case Left((decoder, f))  => apiValue.as(decoder).map(f)
          case Right((decoder, f)) => apiValue.as(decoder).map(f)
        }
      } yield condConst(pred)
    }

}
