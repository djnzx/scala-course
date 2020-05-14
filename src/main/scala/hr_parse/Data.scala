package hr_parse

object Data {

  val trainers = Vector(
    "alexr007",
  )

  val be4 = Vector(
    "abdurrazakliadil",
    "a_velibeyli6",
    "nagiyevintiqam",
    "fidan97ismailova",
    "ibcelal",
    "nazrinaghayeva",
    "qeferlitutu",
    "ruslan_hasanzada",
    "a_nazim2007",
    "Celine_H",
    "sarayeva_sevinc",
    "sidiqa",
    "Susann",
    "zeynebb2022",
    "kamranbekirovyz",
    "aqilzeka99",
  )

  val be3 = Vector(
    "elgun_cumayev",
    "eltacrecebov",
    "Mad_Rabb1t",
    "koceri_agalarov",
    "lcavadova",
    "ramilh085",
    "realserxanbeyli",
    "smammadova11",
    "shamistan1999",
    "ElbrusGarayev",
  )

  val debug = Vector("kamranbekirovyz")

  val users: Vector[String] = Vector(
      trainers, be4, be3
//      debug
    ) flatten

}
