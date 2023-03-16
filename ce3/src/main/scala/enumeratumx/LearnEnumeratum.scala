package enumeratumx

import enumeratum.EnumEntry.Lowercase
import enumeratum._

object LearnEnumeratum extends App {

  /** 1. extend EnumEntry + naming rule:
    * CapitalSnakecase
    * CapitalHyphencase
    * CapitalDotcase
    * CapitalWords
    *
    * Camelcase
    * Uppercase
    * Lowercase
    * Uncapitalised
    *
    * Snakecase        = CapitalSnakecase  + Lowercase
    * UpperSnakecase   = CapitalSnakecase  + Uppercase
    * Hyphencase       = CapitalHyphencase + Lowercase
    * UpperHyphencase  = CapitalHyphencase + Uppercase
    * Dotcase          = CapitalDotcase    + Lowercase
    * UpperDotcase     = CapitalDotcase    + Uppercase
    * Words            = CapitalWords      + Lowercase
    * UpperWords       = CapitalWords      + Uppercase
    * LowerCamelcase   = Camelcase         + Uncapitalised
    */
  sealed trait Color extends EnumEntry with Lowercase

  /** 2. object extends Enum[A] */

  object Color
      extends Enum[Color]
      with CirceEnum[Color]    // Encoder[A] + Decoder[A]
      with DoobieEnum[Color]   // Meta[A]
      with CatsEnum[Color]     // Show[A] + Hash[A] + Eq[A]
      with ScalacheckInstances // arbEnumEntry[EnumType <: EnumEntry] +
      with CogenInstances      // cogenEnumEntry[EnumType <: EnumEntry: Enum]
      {
    override def values: IndexedSeq[Color] = findValues

    case object Red extends Color
    case object LightRed extends Color
    case object Green extends Color
    case object LightGreen extends Color

  }

}
