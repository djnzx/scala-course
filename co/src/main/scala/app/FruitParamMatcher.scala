package app

import org.http4s.dsl.io.QueryParamDecoderMatcher

import SealedTraitDecoder._
object FruitParamMatcher extends QueryParamDecoderMatcher[Fruit]("f")