package controllers

import java.util.Date
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import twitter4j._
import parser._

@Singleton
class RoomController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def rooms() = Action { implicit request: Request[AnyContent] =>
    val twitter = new TwitterFactory().getInstance
    val parser = new TableStatusTweetParser()

    val tweets = twitter.getUserTimeline("pokerifi", new Paging(1, 200))
      .toArray
      .toList
      .asInstanceOf[List[twitter4j.Status]]


    // TODO: Extract and test.
    val tablesByRoom = tweets
      .flatMap(tweet => parser.parseTables(tweet.getText, tweet.getCreatedAt))
      .groupBy(table => table.roomName)
      .map((kv: (Option[String], List[Table])) => {
        val mostRecentTimestamp = kv._2.maxBy(table => table.updatedAt).updatedAt.getTime

        (
          kv._1,
          kv._2.filter(table => table.updatedAt.getTime == mostRecentTimestamp)
        )
      })

    // 🤔
    implicit val tableFormat = Json.format[Table]

    Ok(Json.toJson(tablesByRoom))
  }
}
