package parser

import java.util.Date

case class Table(
  roomName: Option[String],
  tableType: Option[String],
  stakes: Option[(Float, Float)],
  count: Int,
  open: Int,
  waiting: Int,
  interested: Int,
  isNew: Boolean,
  updatedAt: Date,
)

/**
 * TODO: Test.
 */
class TableStatusTweetParser {
  // TODO: There's probably a better way to do these.
  val RoomNames = Map(
    "CASINO_HELSINKI" -> "Casino Helsinki",
    "CLUB_TURKU" -> "Club Turku",
    "FEEL_VEGAS_HELSINKI" -> "Feel Vegas Helsinki",
    "FEEL_VEGAS_ISO_OMENA" -> "Feel Vegas Iso Omena",
    "FEEL_VEGAS_ITIS" -> "Feel Vegas Itis",
    "FEEL_VEGAS_JYVASKYLA" -> "Feel Vegas Jyväskylä",
    "FEEL_VEGAS_KUOPIO" -> "Feel Vegas Kuopio",
    "FEEL_VEGAS_ROVANIEMI" -> "Feel Vegas Rovaniemi",
    "FEEL_VEGAS_SELLO" -> "Feel Vegas Sello",
    "FEEL_VEGAS_TAMPERE" -> "Feel Vegas Tampere",
  )
  val GameTypes = Map(
    "TEXAS_HOLDEM" -> "NLHE",
    "POT_LIMIT_OMAHA_4" -> "PLO4",
    "POT_LIMIT_OMAHA_5" -> "PLO5",
    "POT_LIMIT_OMAHA_4_5" -> "PLO4/5",
    "DEALERS_CHOICE" -> "DC",
  )

  def parseTables(content: String, updatedAt: Date): List[Table] = {
    val lines = content.split("""(\n|\. )""")

    lines
      .filter(line => parseGameType(line).isDefined)
      .map(line => Table(
        parseRoomName(content),
        parseGameType(line),
        parseStakes(line),
        parseTableCount(line),
        parseOpen(line),
        parseWaiting(line),
        parseInterested(line),
        parseIsNew(line),
        updatedAt
      ))
      .toList
  }

  private def parseRoomName(content: String): Option[String] = {
    val keywordToRoomName = List(
      ("casino helsinki", RoomNames("CASINO_HELSINKI")),
      ("helsingin casino", RoomNames("CASINO_HELSINKI")),
      ("kasinolla", RoomNames("CASINO_HELSINKI")),
      ("casinolla", RoomNames("CASINO_HELSINKI")),
      ("chpokeri", RoomNames("CASINO_HELSINKI")),
      ("clubturku", RoomNames("FEEL_VEGAS_HELSINKI")),
      ("club turku", RoomNames("FEEL_VEGAS_HELSINKI")),
      ("feelvegashelsinki", RoomNames("FEEL_VEGAS_HELSINKI")),
      ("feel vegas helsinki", RoomNames("FEEL_VEGAS_HELSINKI")),
      ("feelvegasomena", RoomNames("FEEL_VEGAS_ISO_OMENA")),
      ("feel vegas iso omena", RoomNames("FEEL_VEGAS_ISO_OMENA")),
      ("feelvegasitis", RoomNames("FEEL_VEGAS_ITIS")),
      ("feel vegas itis", RoomNames("FEEL_VEGAS_ITIS")),
      ("feelvegasjyväskylä", RoomNames("FEEL_VEGAS_JYVASKYLA")),
      ("feel vegas jyväskylä", RoomNames("FEEL_VEGAS_JYVASKYLA")),
      ("feelvegaskuopio", RoomNames("FEEL_VEGAS_KUOPIO")),
      ("feel vegas kuopio", RoomNames("FEEL_VEGAS_KUOPIO")),
      ("feelvegaskuopio", RoomNames("FEEL_VEGAS_KUOPIO")),
      ("feel vegas kuopio", RoomNames("FEEL_VEGAS_KUOPIO")),
      ("feelvegasrollo", RoomNames("FEEL_VEGAS_ROVANIEMI")),
      ("feel vegas rollo", RoomNames("FEEL_VEGAS_ROVANIEMI")),
      ("feel vegas rovaniemi", RoomNames("FEEL_VEGAS_ROVANIEMI")),
      ("feelvegassello", RoomNames("FEEL_VEGAS_SELLO")),
      ("feel vegas sello", RoomNames("FEEL_VEGAS_SELLO")),
      ("feelvegastampere", RoomNames("FEEL_VEGAS_TAMPERE")),
      ("feel vegas tampere", RoomNames("FEEL_VEGAS_TAMPERE")),
    )

    keywordToRoomName.find(item => content.toLowerCase().contains(item._1)) match {
      case Some(item) => Some(item._2)
      case None => None
    }
  }

  private def parseGameType(content: String): Option[String] = {
    val keywordToGameType = List(
      ("tx", GameTypes("TEXAS_HOLDEM")),
      ("nl", GameTypes("TEXAS_HOLDEM")),
      ("plo4/5", GameTypes("POT_LIMIT_OMAHA_4_5")),
      ("plo4", GameTypes("POT_LIMIT_OMAHA_4")),
      ("plo5", GameTypes("POT_LIMIT_OMAHA_5")),
      ("plo", GameTypes("POT_LIMIT_OMAHA_4_5")),
      ("dc", GameTypes("DEALERS_CHOICE")),
    )

    keywordToGameType.find(item => content.toLowerCase().contains(item._1)) match {
      case Some(item) => Some(item._2)
      case None => None
    }
  }

  private def parseStakes(content: String): Option[(Float, Float)] = {
    val contentWithoutPlo45 = content.replace("4/5", "")
    val pattern = "([0-9]+(?:[.,][0-9]+)?)[/-]([0-9]+(?:[.,][0-9]+)?)".r.unanchored

    contentWithoutPlo45 match {
      case pattern(smallBlind, bigBlind) => Some((parseFloat(smallBlind), parseFloat(bigBlind)))
      case _ => None
    }
  }

  private def parseFinnishNumber(content: String, suffix: String, prefix: String): Option[Int] = {
    val numberRegExps = List(
      ("(yksi|yhden|yhtä)", 1),
      ("(kaksi|kahden|kahta|pari)", 2),
      ("(kolme|kolmen|kolmea|muutama)", 3),
      ("(neljä|neljän|neljää)", 4),
      ("(viisi|viiden|viittä)", 5),
      ("(kuusi|kuuden|kuutta)", 6),
      ("(seitsemän|seitsemää)", 7),
      ("(kahdeksan|kahdeksaa)", 8),
      ("(yhdeksän|yhdeksää)", 9),
      ("(kymmenen|yhden|yhtä)", 10),
    )

    val suffixMatches = s"(?i)(\\d+)\\s*$suffix".r.unanchored.findAllIn(content)
    if (suffixMatches.nonEmpty) {
      return Some(parseInt(suffixMatches.group(1)))
    }

    val prefixMatches = s"(?i)$prefix\\s*(\\d+)".r.unanchored.findAllIn(content)
    if (prefixMatches.nonEmpty) {
      return Some(parseInt(prefixMatches.group(1)))
    }

    numberRegExps.foreach(re => {
      val prefixNumberMatches = s"(?i)$prefix\\s*${re._1}".r.unanchored.findAllIn(content)
      if (prefixNumberMatches.nonEmpty) {
        return Some(re._2)
      }

      val suffixNumberMatches = s"(?i)${re._1}\\s*$suffix".r.unanchored.findAllIn(content)
      if (suffixNumberMatches.nonEmpty) {
        return Some(re._2)
      }
    })

    None
  }

  private def parseTableCount(content: String): Int = {
    if (parseInterested(content) > 0) {
      return 0
    }

    val matches = "(?i)(\\d+)x".r.unanchored.findAllIn(content)

    if (matches.nonEmpty) {
      parseInt(matches.group(1))
    } else {
      parseFinnishNumber(content, "pöytä", "pöytiä")
        .orElse(Some(1))
        .get
    }
  }

  private def parseOpen(content: String): Int = {
    parseFinnishNumber(content, "paikka", "paikkoja")
      .orElse(parseFinnishNumber(content, "vapaa", "vapaana"))
      .orElse(parseFinnishNumber(content, "pelaaja", "pelaajia"))
      .orElse(Some(0))
      .get
  }

  private def parseWaiting(content: String): Int = {
    parseFinnishNumber(content, "jonossa", "jonossa")
      .orElse(Some(0))
      .get
  }

  private def parseInterested(content: String): Int = {
    parseFinnishNumber(content, "kiinnostunut", "kiinnostuneita")
      .orElse(Some(if (content.contains("kiinnostus")) 1 else 0))
      .get
  }

  private def parseIsNew(content: String): Boolean = {
    content.contains("aukesi") || content.contains("uusi")
  }

  private def parseInt(s: String): Int = {
    s.replaceAll ("[^0-9]", "").toInt
  }

  private def parseFloat(s: String): Float = {
    s.replace(",", ".").replaceAll ("[^0-9.]", "").toFloat
  }
}
