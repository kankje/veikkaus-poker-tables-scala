import java.text.SimpleDateFormat

import org.scalatestplus.play._
import parser._

class TableStatusTweetParserSpec extends PlaySpec {
  "A TableStatusTweetParser" must {
    val parser = new TableStatusTweetParser

    "parse common case" in {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val tables = parser.parseTables("2x NL2,5-2,5 (1 jonossa)\n#chpokeri", dateFormat.parse("2019-01-01"))
      this.assert(tables.head.roomName === Some("Casino Helsinki"))
      this.assert(tables.head.tableType === Some("NLHE"))
      this.assert(tables.head.stakes === Some((2.5, 2.5)))
      this.assert(tables.head.count === 2)
      this.assert(tables.head.waiting === 1)
      this.assert(tables.head.interested === 0)
      this.assert(tables.head.open === 0)
      this.assert(tables.head.isNew === false)
    }

    "parse complicated case" in {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val tables = parser.parseTables(
        "2x 2.5/2.5 TX, 1 jonossa\n2.5-2.5 PLO4/5 aukesi, 2 paikkaa. Muutama kiinnostunut 5/5 TX\n\n#chpokeri",
        dateFormat.parse("2019-01-01")
      )

      this.assert(tables.head.roomName === Some("Casino Helsinki"))
      this.assert(tables.head.tableType === Some("NLHE"))
      this.assert(tables.head.stakes === Some((2.5, 2.5)))
      this.assert(tables.head.count === 2)
      this.assert(tables.head.waiting === 1)
      this.assert(tables.head.interested === 0)
      this.assert(tables.head.open === 0)
      this.assert(tables.head.isNew === false)

      this.assert(tables(1).roomName === Some("Casino Helsinki"))
      this.assert(tables(1).tableType === Some("PLO4/5"))
      this.assert(tables(1).stakes === Some((2.5, 2.5)))
      this.assert(tables(1).count === 1)
      this.assert(tables(1).waiting === 0)
      this.assert(tables(1).interested === 0)
      this.assert(tables(1).open === 2)
      this.assert(tables(1).isNew === true)

      this.assert(tables(2).roomName === Some("Casino Helsinki"))
      this.assert(tables(2).tableType === Some("NLHE"))
      this.assert(tables(2).stakes === Some((5, 5)))
      this.assert(tables(2).count === 0)
      this.assert(tables(2).waiting === 0)
      this.assert(tables(2).interested === 3)
      this.assert(tables(2).open === 0)
      this.assert(tables(2).isNew === false)
    }
  }
}
