package app

import app.SealedTraitDecoder.customQueryParamDecoder
import org.http4s.dsl.io.QueryParamDecoderMatcher

object FruitParamMatcher extends QueryParamDecoderMatcher[Fruit]("f")