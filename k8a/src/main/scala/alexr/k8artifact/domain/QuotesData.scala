package alexr.k8artifact.domain

import scala.util.Random

object QuotesData extends {

  private val data = Vector(
    "The greatest glory in living lies not in never falling, but in rising every time we fall.",
    "The way to get started is to quit talking and begin doing.",
    "Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking.",
    "If life were predictable it would cease to be life, and be without flavor.",
    "You look at what you have in life, you'll always have more. If you look at what you don't have in life, you'll never have enough.",
    "You set your goals ridiculously high and it's a failure, you will fail above everyone else's success.",
    "Life is what happens when you're busy making other plans."
  )

  def next: String = data.apply(Random.nextInt(data.length))

}
