package upickle

/**
  * https://com-lihaoyi.github.io/upickle/
  * https://stackoverflow.com/questions/58694672/how-to-read-json-with-an-optional-field-serialized-by-a-missing-field-in-upickl
  */
object OptionPickler extends upickle.AttributeTagged {

  override implicit def OptionWriter[A: Writer]: Writer[Option[A]] =
    implicitly[Writer[A]].comap[Option[A]] {
      case None => null.asInstanceOf[A]
      case Some(x) => x
    }

  override implicit def OptionReader[A: Reader]: Reader[Option[A]] =
    new Reader.Delegate[Any, Option[A]](implicitly[Reader[A]].map(Some(_))) {
      override def visitNull(index: Int) = None
    }

}

case class ExchangeRateInfo(date: String,
                            bank: String,
                            baseCurrency: Int,
                            baseCurrencyLit: String,
                            exchangeRate: List[ExchangeRate]
                           )

case class ExchangeRate(baseCurrency: String,
                        currency: String,
                        saleRateNB: Double,
                        purchaseRateNB: Double,
                        saleRate: Option[Double] = None,
                        purchaseRate: Option[Double] = None
                       )

object ExchangeRateInfo {
  implicit val rw: OptionPickler.ReadWriter[ExchangeRateInfo] = OptionPickler.macroRW
}

object ExchangeRate {
  implicit val rw: OptionPickler.ReadWriter[ExchangeRate] = OptionPickler.macroRW
}

object UpickleReadExamples extends App {

  val data = """{
       "date":"01.12.2014","bank":"PB","baseCurrency":980,"baseCurrencyLit":"UAH","exchangeRate":
       [
           {"baseCurrency":"UAH","currency":"CHF","saleRateNB":15.6389750,"purchaseRateNB":15.6389750,"saleRate":17.0000000,"purchaseRate":15.5000000},
           {"baseCurrency":"UAH","currency":"EUR","saleRateNB":18.7949200,"purchaseRateNB":18.7949200,"saleRate":20.0000000,"purchaseRate":19.2000000},
           {"baseCurrency":"UAH","currency":"GBP","saleRateNB":23.6324910,"purchaseRateNB":23.6324910,"saleRate":25.8000000,"purchaseRate":24.0000000},
           {"baseCurrency":"UAH","currency":"PLZ","saleRateNB":4.4922010,"purchaseRateNB":4.4922010,"saleRate":5.0000000,"purchaseRate":4.2000000},
           {"baseCurrency":"UAH","currency":"RUR","saleRateNB":0.3052700,"purchaseRateNB":0.3052700,"saleRate":0.3200000,"purchaseRate":0.2800000},
           {"baseCurrency":"UAH","currency":"SEK","saleRateNB":2.0283750,"purchaseRateNB":2.0283750},
           {"baseCurrency":"UAH","currency":"UAH","saleRateNB":1.0000000,"purchaseRateNB":1.0000000},
           {"baseCurrency":"UAH","currency":"USD","saleRateNB":15.0564130,"purchaseRateNB":15.0564130,"saleRate":15.7000000,"purchaseRate":15.3500000},
           {"baseCurrency":"UAH","currency":"XAU","saleRateNB":17747.7470000,"purchaseRateNB":17747.7470000},
           {"baseCurrency":"UAH","currency":"CAD","saleRateNB":13.2107400,"purchaseRateNB":13.2107400,"saleRate":15.0000000,"purchaseRate":13.0000000}
       ]
   }"""
  val x = OptionPickler.read[ExchangeRateInfo](data)
  pprint.pprintln(x)
}
